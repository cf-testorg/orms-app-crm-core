/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.factory.core;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextFactory;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.bean.SubscriberTypeEnum;
import com.redknee.app.crm.bean.core.Msisdn;
import com.redknee.app.crm.factory.ConstructorCallingBeanFactory;
import com.redknee.app.crm.support.LicensingSupportHelper;


/**
 * Creates default Msisdn beans.
 * 
 * @author kumaran.sivasubramaniam@redknee.com
 */
public class MsisdnFactory extends ConstructorCallingBeanFactory<Msisdn>
{
    private static ContextFactory instance_ = null;
    public static ContextFactory instance()
    {
        if (instance_ == null)
        {
            instance_ = new MsisdnFactory();
        }
        return instance_;
    }
    
    protected MsisdnFactory()
    {
        super(Msisdn.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(final Context ctx)
    {
        Msisdn msisdn = (Msisdn) super.create(ctx);

        if ( LicensingSupportHelper.get(ctx).isLicensed(ctx, CoreCrmLicenseConstants.PREPAID_LICENSE_KEY))
        {
            msisdn.setSubscriberType(SubscriberTypeEnum.PREPAID);
        }
        else if (LicensingSupportHelper.get(ctx).isLicensed(ctx, CoreCrmLicenseConstants.POSTPAID_LICENSE_KEY))
        {
            msisdn.setSubscriberType(SubscriberTypeEnum.POSTPAID);
        } 
            
        
        return msisdn;
    }
} // class
