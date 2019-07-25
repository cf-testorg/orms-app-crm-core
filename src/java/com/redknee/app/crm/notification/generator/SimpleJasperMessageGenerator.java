/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s).  A complete listing of authors of this work is readily
 * available.  Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee.  No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the license agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.notification.generator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.calculator.ConstantValueCalculator;
import com.redknee.app.crm.calculator.ToStringValueCalculator;
import com.redknee.app.crm.io.InputStreamFactory;
import com.redknee.app.crm.notification.message.BasicBinaryNotification;
import com.redknee.app.crm.notification.message.BinaryNotificationMessage;
import com.redknee.app.crm.notification.message.NotificationMessage;
import com.redknee.app.crm.notification.template.CompilableNotificationTemplate;
import com.redknee.app.crm.notification.template.NotificationTemplate;


/**
 * Simple Jasper Message Generator
 *
 * @author aaron.gourley@redknee.com
 * @author mangaraj.sahoo@redknee.com
 * @since 8.8/9.0
 */
public class SimpleJasperMessageGenerator extends AbstractSimpleJasperMessageGenerator
{
    /**
     * {@inheritDoc}
     */
    public NotificationMessage generate(Context ctx, NotificationTemplate template, KeyValueFeatureEnum... features) 
    throws MessageGenerationException
    {
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
        BinaryNotificationMessage message = null;
        JasperDesign jasperDesign = null;
        
        if (template instanceof CompilableNotificationTemplate)
        {
            CompilableNotificationTemplate compilableTemplate = (CompilableNotificationTemplate) template;
            Object compiledTemplate = compilableTemplate.getCompiledTemplate(ctx);
            if (compiledTemplate instanceof JasperReport)
            {
                jasperReport = (JasperReport) compiledTemplate;
            }
        }
        
        try
        {
            String templateMessage = template.getTemplateMessage(ctx);
            jasperDesign = JRXmlLoader.load(new ByteArrayInputStream(templateMessage.getBytes()));
        
            if (jasperReport == null)
            {
                jasperReport = JasperCompileManager.compileReport(jasperDesign);
            }
        }
        catch (JRException e)
        {
            throw new MessageGenerationException(e);
        }
        
        if (jasperReport != null)
        {
            try
            {
                Map<String, Object> params = null;
                if (jasperDesign!=null)
                {
                    Map<String, JRParameter> templateParameters = jasperDesign.getParametersMap();
                    if (LogSupport.isDebugEnabled(ctx))
                    {
                        LogSupport.debug(ctx, this, "JR Template Parameters: " + templateParameters);
                    }
                    if (templateParameters != null)
                    {
                        params = generateKeyValuePairs(ctx, templateParameters.keySet(), features);
                        boolean isParamTypeNotSupported = false;
                        for (Map.Entry<String, Object> entry : params.entrySet())
                        {
                            String key = entry.getKey();
                            JRParameter jrParam = templateParameters.get(key);
                            if (jrParam != null)
                            {
                                Object value = entry.getValue();
                                Class jrParamType = jrParam.getValueClass();
                                if (jrParamType != null)
                                {
                                    if (value != null && !jrParamType.isInstance(value))
                                    {
                                        if (String.class.isAssignableFrom(jrParamType))
                                        {
                                            ConstantValueCalculator calc = new ConstantValueCalculator();
                                            calc.setValue(value);
                                            Object stringValue = new ToStringValueCalculator(calc).getValue(ctx);
                                            entry.setValue(stringValue);
                                        }
                                        else
                                        {
                                            if (LogSupport.isDebugEnabled(ctx))
                                            {
                                                String msg = "Unsupported Param Key: " + key + ", Value: "+ value + ", Type: "+ jrParamType;
                                                LogSupport.debug(ctx, this, msg);
                                            }
                                            // Calculated value is not compatible with parameter definition?
                                            // TODO: What to do here? Hope it works or remove from parameter map?
                                            isParamTypeNotSupported = true;
                                        }
                                    }
                                }
                            }
                        }
                        if (isParamTypeNotSupported)
                        {
                            throw new MessageGenerationException("Only STRING parameters are supported.");
                        }
                    }
                }
                
                jasperPrint = JasperFillManager.fillReport(jasperReport, params);

                OutputStream pdfOutput = null;
                InputStreamFactory factory = null;
                try
                {
                    /*
                     * Create an output stream to hold the PDF data prior to message delivery
                     * Note that even if the final desination of the message is a file, writing
                     * to an intermediate stream is desirable, even if that stream is a temporary
                     * file.  Reason being that in the case of scheduled delivery the final file
                     * should not be output to the destination directory until the scheduled time.
                     * Processes may be monitoring that directory for changes for automated printing
                     * for example, and they may want that automated printing to happen according to
                     * the schedule.
                     */
                    pdfOutput = getIntermediateOutputStream(ctx, "JasperMsg");
                    JasperExportManager.exportReportToPdfStream(jasperPrint, pdfOutput);
                    factory = getInputStreamFactoryForIntermediateOutputStream(ctx, pdfOutput);
                }
                finally
                {
                    if (pdfOutput != null)
                    {
                        try
                        {
                            pdfOutput.close();
                        }
                        catch (IOException e)
                        {
                            new InfoLogMsg(this, "Error occurred closing temporary output stream: " + e.getMessage(), null).log(ctx);
                        }
                    }
                }
                
                if (factory == null)
                {
                    throw new MessageGenerationException("Could not generate input stream for Jasper notification message!");
                }
                
                message = new BasicBinaryNotification();
                message.setInputStreamGenerator(factory);
            }
            catch (JRException e)
            {
                throw new MessageGenerationException(e);
            }
        }
        
        return message;
    }

}
