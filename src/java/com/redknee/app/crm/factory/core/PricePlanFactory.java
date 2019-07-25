package com.redknee.app.crm.factory.core;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextFactory;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.bean.SubscriberTypeEnum;
import com.redknee.app.crm.bean.core.PricePlan;
import com.redknee.app.crm.factory.ConstructorCallingBeanFactory;
import com.redknee.app.crm.support.LicensingSupportHelper;

public class PricePlanFactory extends ConstructorCallingBeanFactory<PricePlan>
{
    private static ContextFactory instance_ = null;
    public static ContextFactory instance()
    {
        if (instance_ == null)
        {
            instance_ = new PricePlanFactory();
        }
        return instance_;
    }
    
    protected PricePlanFactory()
    {
        super(PricePlan.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(final Context context)
    {
        final PricePlan bean = new PricePlan();

        if (LicensingSupportHelper.get(context).isLicensed(context, CoreCrmLicenseConstants.PREPAID_LICENSE_KEY))
        {
            bean.setPricePlanType(SubscriberTypeEnum.PREPAID);
        }
        else if ( LicensingSupportHelper.get(context).isLicensed(context, CoreCrmLicenseConstants.POSTPAID_LICENSE_KEY))
        {
            bean.setPricePlanType(SubscriberTypeEnum.POSTPAID);
        } 

        return bean;
    }

} // class