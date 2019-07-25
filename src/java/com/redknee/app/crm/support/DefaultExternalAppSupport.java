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
package com.redknee.app.crm.support;


import com.redknee.app.crm.bean.ServiceBase;
import com.redknee.app.crm.bean.externalapp.ExternalAppEnum;
import com.redknee.app.crm.bean.externalapp.ExternalAppErrorCodeMsg;
import com.redknee.app.crm.bean.externalapp.ExternalAppErrorCodeMsgXInfo;
import com.redknee.app.crm.bean.externalapp.ExternalAppResultCode;
import com.redknee.app.crm.bean.externalapp.ExternalAppResultCodeHome;
import com.redknee.app.crm.bean.externalapp.ExternalAppResultCodeXInfo;
import com.redknee.framework.xhome.auth.bean.User;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.language.Lang;
import com.redknee.framework.xhome.language.LangXInfo;
import com.redknee.framework.xhome.language.MessageMgr;
import com.redknee.framework.xlog.log.LogSupport;


/**
 * @author Marcio Marques
 * @since 9.1.3
 */
public class DefaultExternalAppSupport implements ExternalAppSupport
{
    private enum ProvisionTypeEnum
    {
        PROVISION, UNPROVISION, SUSPENSION, RESUME
    }
    
    protected static ExternalAppSupport instance_ = null;
    
    public static int DEFAULT_RESULT_CODE = 9999;
    public static String DEFAULT_ERROR_CODE_MESSAGE = "Unknown error code";

    public static ExternalAppSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultExternalAppSupport();
        }
        return instance_;
    }

    protected DefaultExternalAppSupport()
    {
    }

    public int getResultCode(Context ctx, ExternalAppEnum externalApp)
    {
        int result = DEFAULT_RESULT_CODE;
        
        try
        {
            ExternalAppResultCode mapping = HomeSupportHelper.get(ctx).findBean(ctx, ExternalAppResultCode.class, new EQ(ExternalAppResultCodeXInfo.EXTERNAL_APP, Integer.valueOf(externalApp.getIndex())));
            if (mapping!=null)
            {
                result = mapping.getResultCode();
            }
        }
        catch (HomeException e)
        {
            LogSupport.minor(ctx, this, "Unable to retrieve external app " + externalApp + " result code: " + e.getMessage(), e);
        }

        return result;
    }
    
    public String getProvisionErrorMessage(Context ctx, ExternalAppEnum externalApp, int errorCode, ServiceBase service)
    {
        return getErrorMessage(ctx, externalApp, errorCode, service, ProvisionTypeEnum.PROVISION);
    }
    
    public String getUnprovisionErrorMessage(Context ctx, ExternalAppEnum externalApp, int errorCode, ServiceBase service)
    {
        return getErrorMessage(ctx, externalApp, errorCode, service, ProvisionTypeEnum.UNPROVISION);
    }

    public String getSuspensionErrorMessage(Context ctx, ExternalAppEnum externalApp, int errorCode, ServiceBase service)
    {
        return getErrorMessage(ctx, externalApp, errorCode, service, ProvisionTypeEnum.SUSPENSION);
    }
    
    public String getResumeErrorMessage(Context ctx, ExternalAppEnum externalApp, int errorCode, ServiceBase service)
    {
        return getErrorMessage(ctx, externalApp, errorCode, service, ProvisionTypeEnum.RESUME);
    }

    private String getErrorMessage(Context context, ExternalAppEnum externalApp, int errorCode, ServiceBase service, ProvisionTypeEnum provisionType)
    {
        Context subContext = context.createSubContext();
        StringBuilder result = new StringBuilder();
        String language = ExternalAppErrorCodeMsg.DEFAULT_LANGUAGE;

        final User principal = (User) subContext.get(java.security.Principal.class, new User());
        if (principal!=null && principal.getLanguage()!=null && !principal.getLanguage().trim().isEmpty())
        {
            language = principal.getLanguage();
            subContext.put(Lang.class, getLanguage(subContext, language));
        }

        MessageMgr mmgr = new MessageMgr(subContext, getModule());
        if (service instanceof com.redknee.app.crm.bean.AuxiliaryService)
        {
            result.append(mmgr.get(AUXILIARY_SERVICE_KEY, DEFAULT_AUXILIARY_SERVICE_VALUE));
        }
        else
        {
            result.append(mmgr.get(SERVICE_KEY, DEFAULT_SERVICE_VALUE));
        }
        
        result.append(" '");
        result.append(service.getID());
        result.append(" - ");
        result.append(service.getName());
        result.append("' ");

        if (ProvisionTypeEnum.PROVISION.equals(provisionType))
        {
            result.append(mmgr.get(PROVISION_KEY, DEFAULT_PROVISION_VALUE));
        }
        else if (ProvisionTypeEnum.UNPROVISION.equals(provisionType))
        {
            result.append(mmgr.get(UNPROVISION_KEY, DEFAULT_UNPROVISION_VALUE));
        }
        if (ProvisionTypeEnum.SUSPENSION.equals(provisionType))
        {
            result.append(mmgr.get(SUSPENSION_KEY, DEFAULT_SUSPENSION_VALUE));
        }
        if (ProvisionTypeEnum.RESUME.equals(provisionType))
        {
            result.append(mmgr.get(RESUME_KEY, DEFAULT_RESUME_VALUE));
        }
        
        result.append(": ");
        result.append(getErrorCodeMessage(subContext, externalApp, errorCode));
        
        return result.toString();
    }

    public String getErrorCodeMessage(Context context, ExternalAppEnum externalApp, int errorCode)
    {
        Context subContext = context.createSubContext();
        String result = DEFAULT_ERROR_CODE_MESSAGE;
        String language = ExternalAppErrorCodeMsg.DEFAULT_LANGUAGE;

        final User principal = (User) subContext.get(java.security.Principal.class, new User());
        if (principal!=null && principal.getLanguage()!=null && !principal.getLanguage().trim().isEmpty())
        {
            language = principal.getLanguage();
            subContext.put(Lang.class, getLanguage(subContext, language));
        }
        
        try
        {
            And filter = new And();
            filter.add(new EQ(ExternalAppErrorCodeMsgXInfo.EXTERNAL_APP, Integer.valueOf(externalApp.getIndex())));
            filter.add(new EQ(ExternalAppErrorCodeMsgXInfo.ERROR_CODE, Integer.valueOf(errorCode)));
            filter.add(new EQ(ExternalAppErrorCodeMsgXInfo.LANGUAGE, language));
            
            ExternalAppErrorCodeMsg mapping = HomeSupportHelper.get(subContext).findBean(subContext, ExternalAppErrorCodeMsg.class,
                    filter);
            
            if (mapping!=null)
            {
                result = mapping.getMessage();
            }
            else if (!ExternalAppErrorCodeMsg.DEFAULT_LANGUAGE.equals(language))
            {
                LogSupport.minor(subContext, this, "No external app " + externalApp + " error code " + errorCode + " " + language + " message configured. Retrieving default language message.");
                filter = new And();
                filter.add(new EQ(ExternalAppErrorCodeMsgXInfo.EXTERNAL_APP, Integer.valueOf(externalApp.getIndex())));
                filter.add(new EQ(ExternalAppErrorCodeMsgXInfo.ERROR_CODE, Integer.valueOf(errorCode)));
                filter.add(new EQ(ExternalAppErrorCodeMsgXInfo.LANGUAGE, ExternalAppErrorCodeMsg.DEFAULT_LANGUAGE));
                
                mapping = HomeSupportHelper.get(subContext).findBean(subContext, ExternalAppErrorCodeMsg.class,
                        filter);
                
                if (mapping!=null)
                {
                    result = mapping.getMessage();
                }
            }
            
        }
        catch (HomeException e)
        {
            LogSupport.minor(subContext, this, "Unable to retrieve external app " + externalApp + " error code " + errorCode + " " + language + " message: " + e.getMessage(), e);
        }

        MessageMgr mmgr = new MessageMgr(subContext, getModule());
        return result + " (" + mmgr.get(CODE_KEY, DEFAULT_CODE_VALUE) + " = " + errorCode + ")";
    }
    
    private Lang getLanguage(final Context ctx, final String language)
    {
        Lang lang = (Lang) ctx.get(Lang.class, Lang.DEFAULT);
        try
        {
            lang = HomeSupportHelper.get(ctx).findBean(ctx, Lang.class, new EQ(LangXInfo.CODE, language));
        }
        catch (Throwable t)
        {
            LogSupport.minor(ctx, this, "Unable to retrieve language " + language + ": " + t.getMessage(), t);
        }
        
        return lang;
    }
    
    
    public void addExternalAppResultCodes(Context ctx)
    {
        addExternalAppResultCodeBean(ctx, ExternalAppEnum.BSS, 3009);
        addExternalAppResultCodeBean(ctx, ExternalAppEnum.URCS, 3008);
        addExternalAppResultCodeBean(ctx, ExternalAppEnum.HLR, 3011);
        addExternalAppResultCodeBean(ctx, ExternalAppEnum.VOICE, 3006);
        addExternalAppResultCodeBean(ctx, ExternalAppEnum.SMS, 3007);
        addExternalAppResultCodeBean(ctx, ExternalAppEnum.DATA, 3013);
        addExternalAppResultCodeBean(ctx, ExternalAppEnum.VOICEMAIL, 3022);
        addExternalAppResultCodeBean(ctx, ExternalAppEnum.SPG, 3026);
        addExternalAppResultCodeBean(ctx, ExternalAppEnum.BLACKBERRY, 3021);
        addExternalAppResultCodeBean(ctx, ExternalAppEnum.ALCATEL_SSC, 3014);
        addExternalAppResultCodeBean(ctx, ExternalAppEnum.DATA_OPTIN, 3023);
        addExternalAppResultCodeBean(ctx, ExternalAppEnum.RBT, 3027);
        addExternalAppResultCodeBean(ctx, ExternalAppEnum.HOMEZONE, 3025);
        addExternalAppResultCodeBean(ctx, ExternalAppEnum.FF, 3024);
    }  
    
    

    public void addExternalAppErrorCodeMessages(Context ctx)
    {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    
    private void addExternalAppResultCodeBean(Context ctx, ExternalAppEnum externalApp, int resultCode)
    {
        final Home home = (Home) ctx.get(ExternalAppResultCodeHome.class);
        try
        {
            ExternalAppResultCode record = new ExternalAppResultCode();
            record.setExternalApp(externalApp.getIndex());
            record.setResultCode(resultCode);
            home.create(ctx, record);
        }
        catch (HomeException e)
        {
            LogSupport.minor(ctx, this, "Unable to add external application '" + externalApp.getDescription() + "' result code " + resultCode + ": " + e.getMessage(), e);
        }
    }




    private String AUXILIARY_SERVICE_KEY = "Auxiliary Service";
    private String SERVICE_KEY = "Service";
    private String PROVISION_KEY = "Provision failure";
    private String UNPROVISION_KEY = "Unprovision failure";
    private String SUSPENSION_KEY = "Suspension failure";
    private String RESUME_KEY = "Resume failure";
    private String CODE_KEY = "Code";

    private String DEFAULT_AUXILIARY_SERVICE_VALUE = "Auxiliary Service";
    private String DEFAULT_SERVICE_VALUE = "Service";
    private String DEFAULT_PROVISION_VALUE = "provision failed";
    private String DEFAULT_UNPROVISION_VALUE = "unprovision failed";
    private String DEFAULT_SUSPENSION_VALUE = "suspension failed";
    private String DEFAULT_RESUME_VALUE = "resume failed";
    private String DEFAULT_CODE_VALUE = "Code";
    
    private Class getModule()
    {
        return ExternalAppSupport.class;
    }
}
