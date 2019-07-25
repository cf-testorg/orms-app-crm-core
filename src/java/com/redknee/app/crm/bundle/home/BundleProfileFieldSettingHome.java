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
package com.redknee.app.crm.bundle.home;

import com.redknee.app.crm.bean.ActivationFeeModeEnum;
import com.redknee.app.crm.bean.ServicePeriodEnum;
import com.redknee.app.crm.bundle.ActivationFeeCalculationEnum;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.LogSupport;

/**
 * Sets the BundleProfile fields as per the requirement.
 * @author suyash.gaidhani@redknee.com
 *
 */
public class BundleProfileFieldSettingHome extends HomeProxy
{
    /**
     * 
     */
    private static final long serialVersionUID = -8766615573392241289L;    

    /**
     * Accepts the delegate and the context
     * @param ctx
     * @param delegate
     */
    public BundleProfileFieldSettingHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx, Object bean) throws HomeException
    {
    	com.redknee.app.crm.bean.core.BundleProfile bundleProfile = (com.redknee.app.crm.bean.core.BundleProfile) bean;
    	
    	if(ServicePeriodEnum.DAILY.equals(bundleProfile.getChargingRecurrenceScheme()))
        {
    		bundleProfile.setRecurringStartValidity(1);
    		if(LogSupport.isDebugEnabled(ctx))
    		{
    			LogSupport.debug(ctx, this, "Setting the Bundle Profile : Recurrence Number of Units (recurringStartValidity) to : 1");
    		}
        }
    	else if (ServicePeriodEnum.ONE_TIME.equals(bundleProfile.getChargingRecurrenceScheme())
        		|| ServicePeriodEnum.MONTHLY.equals(bundleProfile.getChargingRecurrenceScheme()))
        {
    		bundleProfile.setRecurringStartValidity(0);
    		if(LogSupport.isDebugEnabled(ctx))
    		{
    			LogSupport.debug(ctx, this, "Setting the Bundle Profile : Recurrence Number of Units (recurringStartValidity) to : 0");
    			
    		}
        }
    	if(ActivationFeeCalculationEnum.PRORATE.equals(bundleProfile.getActivationFeeCalculation()))
    	{
    	    bundleProfile.setProrateBundleQuota(true);
    	    bundleProfile.setPostpaidSubCreationOnly(true);
    	    if(LogSupport.isDebugEnabled(ctx))
            {
                LogSupport.debug(ctx, this, "Setting the Bundle Profile : Postpaid Sub creation Only and Prorate Bundle Quota flag: TRUE" +
                		"for AUX bundles with PRORATE activation fee");
                
            }
    	}
     	return super.create(ctx, bean);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object store(Context ctx, Object bean) throws HomeException
    {
        com.redknee.app.crm.bean.core.BundleProfile bundleProfile = (com.redknee.app.crm.bean.core.BundleProfile) bean;
        
        if(ActivationFeeCalculationEnum.PRORATE.equals(bundleProfile.getActivationFeeCalculation()))
        {
            bundleProfile.setProrateBundleQuota(true);
            bundleProfile.setPostpaidSubCreationOnly(true);
            if(LogSupport.isDebugEnabled(ctx))
            {
                LogSupport.debug(ctx, this, "Setting the Bundle Profile : Postpaid Sub creation Only and Prorate Bundle Quota flag: TRUE" +
                        "for AUX bundles with PRORATE activation fee");
                
            }
        }
        return super.store(ctx, bean);
    }
}
