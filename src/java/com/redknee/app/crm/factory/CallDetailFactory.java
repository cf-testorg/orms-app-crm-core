package com.redknee.app.crm.factory;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextFactory;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.bean.SubscriberTypeEnum;
import com.redknee.app.crm.bean.calldetail.CallDetail;
import com.redknee.app.crm.support.LicensingSupportHelper;

public class CallDetailFactory implements ContextFactory
{
    private static ContextFactory instance_ = null;
    public static ContextFactory instance()
    {
        if (instance_ == null)
        {
            instance_ = new CallDetailFactory();
        }
        return instance_;
    }
    
    protected CallDetailFactory()
    {
    }

    /**
     * {@inheritDoc}
     */
    public Object create(final Context context)
    {
        final CallDetail bean = new CallDetail();

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
