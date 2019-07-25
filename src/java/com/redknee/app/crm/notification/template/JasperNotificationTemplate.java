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
package com.redknee.app.crm.notification.template;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class JasperNotificationTemplate extends AbstractJasperNotificationTemplate implements CompilableNotificationTemplate<JasperReport>
{
    public JasperNotificationTemplate()
    {
        super();
        addPropertyChangeListener(TEMPLATEFILENAMES_PROPERTY, new MessageCompilingPropertyChangeListener(this));
    }

    /**
     * {@inheritDoc}
     */
    public synchronized JasperReport compile(Context ctx)
    {
        String templateMessage = getTemplateMessage(ctx);
        try
        {
            JasperDesign jasperDesign = JRXmlLoader.load(new ByteArrayInputStream(templateMessage.getBytes()));
            compiledReport_ = JasperCompileManager.compileReport(jasperDesign);
            
            List<FilenameHolder> templateFilenames = getTemplateFilenames();
            if (templateFilenames != null && templateFilenames.size() > 1)
            {
                for (int index=1; index < templateFilenames.size(); index++)
                {
                    String jrxmlFileName = templateFilenames.get(index).getFilename();
                    File jrxmlFile = new File(jrxmlFileName);
                    
                    String jasperFileName = jrxmlFileName.replaceAll(".jrxml", ".jasper");
                    File jasperFile = new File(jasperFileName);
                    
                    if (LogSupport.isDebugEnabled(ctx))
                    {
                        LogSupport.debug(ctx, this, "Processing Template: " + jrxmlFileName);
                    }
                    
                    if (jrxmlFile.exists() && (!jasperFile.exists() || jrxmlFile.lastModified() > jasperFile.lastModified()))
                    {
                        if (LogSupport.isDebugEnabled(ctx))
                        {
                            LogSupport.debug(ctx, this, "Compiling Template: " + jrxmlFileName);
                        }
                        JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);
                    }
                }
            }
        }
        catch (JRException e)
        {
            new MajorLogMsg(this, "Error compiling Jasper Report: " + e.getMessage(), e).log(ctx);
        }        
        return compiledReport_;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized JasperReport getCompiledTemplate(Context ctx)
    {
        if (compiledReport_ == null)
        {
            return compile(ctx);
        }
        return compiledReport_;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void resetCompiledTemplate(Context ctx)
    {
        compiledReport_ = null;
    }

    protected transient JasperReport compiledReport_ = null;
}
