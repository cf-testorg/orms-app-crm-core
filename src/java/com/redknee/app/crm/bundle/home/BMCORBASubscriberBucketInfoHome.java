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

import java.util.ArrayList;
import java.util.List;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.home.NullHome;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bundle.BMReturnCodeMsgMapping;
import com.redknee.app.crm.bundle.RecurrenceOptions;
import com.redknee.app.crm.bundle.SubscriberBucket;
import com.redknee.app.crm.bundle.SubscriberBucketHome;
import com.redknee.app.crm.client.BMSubscriberBucketCorbaClient;
import com.redknee.app.crm.client.ProvisioningHomeException;
import com.redknee.product.bundle.manager.provision.common.param.ActivationSchemeType;
import com.redknee.product.bundle.manager.provision.common.param.Parameter;
import com.redknee.product.bundle.manager.provision.common.param.ParameterID;
import com.redknee.product.bundle.manager.provision.common.param.ParameterValue;

/**
 * Stores and creates BM related subscriber bucket information via CORBA
 * @author @redknee.com
 *
 */
public class BMCORBASubscriberBucketInfoHome extends HomeProxy implements SubscriberBucketHome
{
    /**
     * 
     */
    private static final long serialVersionUID = -8766615573392241290L;

    /**
     * Accepts the delegate and the context
     * @param ctx
     * @param delegate
     */
    public BMCORBASubscriberBucketInfoHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }
    
    /**
     * Accepts the delegate and the context
     * @param ctx
     */
    public BMCORBASubscriberBucketInfoHome(Context ctx)
    {
        super(ctx, NullHome.instance());
    }    
    

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx, Object bean)
        throws HomeException
    {
        SubscriberBucket subscriberBucket = (SubscriberBucket) bean;

        BMSubscriberBucketCorbaClient client = (BMSubscriberBucketCorbaClient) ctx.get(BMSubscriberBucketCorbaClient.class);
        if (client == null)
        {
            new MajorLogMsg(this, "BMSubscriberBucketCorbaClient not found in context", null).log(ctx);
            throw new HomeException("BMSubscriberBucketCorbaClient not found in context");
        }
        
        String msisdn = subscriberBucket.getMsisdn();
        long bundleId = subscriberBucket.getBundleId();
        
        RecurrenceOptions options = subscriberBucket.getOptions();
        List<Parameter> inParamList = new ArrayList<Parameter>();
        
        ParameterValue paramValue = new ParameterValue();
        paramValue.longValue(options.getBundleId());
        inParamList.add(new Parameter(ParameterID.IN_BUNDLE_ID, paramValue));        
        
        if (options.getExpiryTimePresent())
        {
            ParameterValue paramValue2 = new ParameterValue();
            paramValue2.longValue(options.getExpiryTime());
            inParamList.add(new Parameter(ParameterID.IN_BUCKET_EXPIRY_TIME, paramValue2));
        }        
        
        if (options.getBillingDatePresent())
        {
            ParameterValue paramValue3 = new ParameterValue();
            if (options.getBillingDate() == -1)
            {
                // this is to accomodate for the old way of specifying the FIRST_CALL_ACTIVATION activation
                paramValue3.intValue(ActivationSchemeType.FIRST_CALL_ACTIVATION);
                inParamList.add(new Parameter(ParameterID.IN_ACTIVATION_SCHEME, paramValue3));                
            }
            else
            {
                paramValue3.intValue(options.getBillingDate());
                inParamList.add(new Parameter(ParameterID.IN_BILL_CYCLE_DATE, paramValue3));                
            }
        }        
        
        // TODO change this condition based on bcd interval
        if(true)
        {
            ParameterValue paramValue4 = new ParameterValue();
            paramValue4.booleanValue(true);
            inParamList.add(new Parameter(ParameterID.IN_EXPIRE_ON_BCD, paramValue4));            
        }        
        
        int retInt = -1;
        retInt = client.createBucket(msisdn, subscriberBucket.getSubscriptionType(), bundleId, inParamList.toArray(new Parameter[inParamList.size()]));
        if (retInt != 0)
        {
            new MinorLogMsg(this, "BMCORBASubscriberBucketInfoHome:create() failed: Result code = " + retInt 
                    + ". Msisdn=" + msisdn + " BundleId=" + bundleId, null).log(ctx);
            throw new ProvisioningHomeException(BMReturnCodeMsgMapping.getBucketMessage(retInt) + " (Code=" + retInt +
                    ")", retInt);
        }
                
        return subscriberBucket;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object store(Context ctx, Object bean)
    throws HomeException
    {
        new MinorLogMsg(this, "BMCORBASubscriberBucketInfoHome:store() not supported", null).log(ctx);
        return bean;
    }

    @Override
    public void remove(Context ctx, Object bean)
    throws HomeException
    {

        SubscriberBucket subscriberBucket = (SubscriberBucket) bean;
       
        BMSubscriberBucketCorbaClient client = (BMSubscriberBucketCorbaClient) ctx.get(BMSubscriberBucketCorbaClient.class);
        if (client == null)
        {
            new MajorLogMsg(this, "BMSubscriberBucketCorbaClient not found in context", null).log(ctx);
            throw new HomeException("BMSubscriberBucketCorbaClient not found in context");
        }
        
        String msisdn = subscriberBucket.getMsisdn();
        long bundleId = subscriberBucket.getBundleId();
        
        int retInt = -1;
        retInt = client.removeBucket(msisdn, subscriberBucket.getSubscriptionType(), bundleId);

        if (retInt != 0)
        {
            new MinorLogMsg(this, "BMCORBASubscriberBucketInfoHome:remove() failed: Result code = " + retInt 
                    + ". Msisdn=" + msisdn + " BundleId=" + bundleId, null).log(ctx);
            throw new ProvisioningHomeException(BMReturnCodeMsgMapping.getBucketMessage(retInt) + " (Code=" + retInt +
                    ")", retInt);
        }
        
        super.remove(ctx, bean);
    }
}
