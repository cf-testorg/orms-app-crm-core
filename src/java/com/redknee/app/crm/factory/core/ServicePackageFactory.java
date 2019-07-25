package com.redknee.app.crm.factory.core;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextFactory;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.bean.SubscriberTypeEnum;
import com.redknee.app.crm.bean.core.ServicePackage;
import com.redknee.app.crm.factory.ConstructorCallingBeanFactory;
import com.redknee.app.crm.support.LicensingSupportHelper;

public class ServicePackageFactory extends ConstructorCallingBeanFactory<ServicePackage>
{
    private static ContextFactory instance_ = null;
    public static ContextFactory instance()
    {
        if (instance_ == null)
        {
            instance_ = new ServicePackageFactory();
        }
        return instance_;
    }
    
    protected ServicePackageFactory()
    {
        super(ServicePackage.class);
    }

	
    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(final Context context)
    {
        final ServicePackage bean = (ServicePackage) super.create(context);

        if (LicensingSupportHelper.get(context).isLicensed(context, CoreCrmLicenseConstants.PREPAID_LICENSE_KEY))
        {
            bean.setType(SubscriberTypeEnum.PREPAID);
        }
        else if ( LicensingSupportHelper.get(context).isLicensed(context, CoreCrmLicenseConstants.POSTPAID_LICENSE_KEY))
        {
            bean.setType(SubscriberTypeEnum.POSTPAID);
        } 

        return bean;
    }

}
