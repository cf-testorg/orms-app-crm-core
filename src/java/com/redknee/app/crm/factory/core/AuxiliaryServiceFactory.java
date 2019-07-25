package com.redknee.app.crm.factory.core;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextFactory;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.bean.SubscriberTypeEnum;
import com.redknee.app.crm.bean.core.AuxiliaryService;
import com.redknee.app.crm.factory.ConstructorCallingBeanFactory;
import com.redknee.app.crm.support.LicensingSupportHelper;

public class AuxiliaryServiceFactory extends ConstructorCallingBeanFactory<AuxiliaryService>
{
    private static ContextFactory instance_ = null;
    public static ContextFactory instance()
    {
        if (instance_ == null)
        {
            instance_ = new AuxiliaryServiceFactory();
        }
        return instance_;
    }
    
    protected AuxiliaryServiceFactory()
    {
        super(AuxiliaryService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(final Context context)
    {
        final AuxiliaryService bean = (AuxiliaryService) super.create(context);

        if ( LicensingSupportHelper.get(context).isLicensed(context, CoreCrmLicenseConstants.POSTPAID_LICENSE_KEY))
        {
            bean.setSubscriberType(SubscriberTypeEnum.POSTPAID);
        }
        else if (LicensingSupportHelper.get(context).isLicensed(context, CoreCrmLicenseConstants.PREPAID_LICENSE_KEY))
        {
            bean.setSubscriberType(SubscriberTypeEnum.PREPAID);
        } 
        	
        return bean;
    }

} // class
