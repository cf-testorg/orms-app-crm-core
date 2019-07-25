package com.redknee.app.crm.support;

import com.redknee.app.crm.bean.ServiceTypeEnum;
import com.redknee.app.crm.bean.service.ExternalAppMapping;
import com.redknee.app.crm.bean.service.ExternalAppMappingHome;
import com.redknee.app.crm.bean.service.ExternalAppMappingXInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.logger.Logger;


public class DefaultExternalAppMappingSupport implements ExternalAppMappingSupport
{

    protected static ExternalAppMappingSupport instance_ = null;


    public static ExternalAppMappingSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultExternalAppMappingSupport();
        }
        return instance_;
    }


    protected DefaultExternalAppMappingSupport()
    {
    }


    public long getExternalApplicationId(final Context ctx, final ServiceTypeEnum serviceType) throws HomeException
    {
        final ExternalAppMapping app = getExternalAppMapping(ctx, serviceType);
        return app.getId();
    }


    public ExternalAppMapping getExternalAppMapping(final Context ctx, final ServiceTypeEnum serviceType)
            throws HomeException
    {
        final Home home = (Home) ctx.get(ExternalAppMappingHome.class);
        if (home == null)
        {
            LogSupport.major(ctx, ExternalAppMappingSupport.class, "ExternalAppMapping Home NOT AVAILABLE yet.");
            return null;
        }
        final ExternalAppMapping app = (ExternalAppMapping) home.find(ctx, new EQ(ExternalAppMappingXInfo.SERVICE_TYPE,
                serviceType));
        return app;
    }


    public void addExternalAppMappingBeans(final Context ctx)
    {
        addExternalAppMappingBeans(ctx, (Home) ctx.get(ExternalAppMappingHome.class));
    }


    public void addExternalAppMappingBeans(final Context ctx, final Home home)
    {
        createExternalAppMapping(ctx, home, ServiceTypeEnum.GENERIC, "Generic");
        createExternalAppMapping(ctx, home, ServiceTypeEnum.VOICEMAIL, "Voicemail");
        createExternalAppMapping(ctx, home, ServiceTypeEnum.VOICE, "Voice");
        createExternalAppMapping(ctx, home, ServiceTypeEnum.SMS, "Sms");
        createExternalAppMapping(ctx, home, ServiceTypeEnum.DATA, "IPC");
        createExternalAppMapping(ctx, home, ServiceTypeEnum.TRANSFER, "Transfer");
        createExternalAppMapping(ctx, home, ServiceTypeEnum.EVDO, "EVDO");
        createExternalAppMapping(ctx, home, ServiceTypeEnum.BLACKBERRY, "Blackberry");
        createExternalAppMapping(ctx, home, ServiceTypeEnum.ALCATEL_SSC, "Alcatel");
        createExternalAppMapping(ctx, home, ServiceTypeEnum.SERVICE_PROVISIONING_GATEWAY, "SPG");
        createExternalAppMapping(ctx, home, ServiceTypeEnum.PACKAGE, "Generic");
        createExternalAppMapping(ctx, home, ServiceTypeEnum.EXTERNAL_PRICE_PLAN, "Generic");
        createExternalAppMapping(ctx, home, ServiceTypeEnum.CALLING_GROUP, "CallingGroup");
    }


    public void createExternalAppMapping(final Context ctx, final Home home, final ServiceTypeEnum type,
            final String handler)
    {
        final ExternalAppMapping record = new ExternalAppMapping();
        record.setId(type.getIndex());
        record.setServiceType(type);
        record.setHandler(handler);
        try
        {
            home.create(ctx, record);
        }
        catch (HomeException e)
        {
            LogSupport.major(ctx, ExternalAppMappingSupport.class, "Unable to configure bean " + record, e);
        }
    }
}


