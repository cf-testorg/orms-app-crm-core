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
package com.redknee.app.crm.bundle.service;

import java.util.Collection;
import java.util.Map;

import com.redknee.app.crm.bundle.exception.BucketAlreadyExistsException;
import com.redknee.app.crm.bundle.exception.BucketDoesNotExistsException;
import com.redknee.app.crm.bundle.exception.BundleDoesNotExistsException;
import com.redknee.app.crm.bundle.exception.BundleManagerException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.product.bundle.manager.provision.common.param.Parameter;
import com.redknee.app.crm.bundle.*;

/**
 * Service that handles all basic BM services for SubscriberBuckets in the system
 * @author arturo.medina@redknee.com
 *
 */
public interface BundleManagerSubscriberBucket
{
    /**
     * Creates a bucket in BM
     * @param ctx
     * @param msisdn MSISDN that combined with subscription type defines the subscription
     * @param subscriptionType subscription type that combined with MSISDN defines the subscription
     * @param bundleId 
     * @param bucket
     * @throws BucketAlreadyExistsException
     * @throws BundleManagerException
     */
    public void createBucket(Context ctx, String msisdn, int subscriptionType, long bundleId)
        throws BucketAlreadyExistsException, BundleManagerException;

    
    /**
     * Creates a bucket in BM
     * @param ctx
     * @param subs 
     * @param msisdns a Collection of all msisdns to bulk create
     * @param buckets a Collection of all buckets to add in BM
     * @throws BucketAlreadyExistsException
     * @throws BundleManagerException
     */
    public void createBuckets(Context ctx, Collection subs, Collection buckets)
        throws BucketAlreadyExistsException, BundleManagerException;

    /**
     * Creates a bucket in BM
     * @param ctx
     * @param bucket
     * @throws BucketAlreadyExistsException
     * @throws BundleManagerException
     */
    public void createBucket(Context ctx, SubscriberBucket bucket)
        throws BucketAlreadyExistsException, BundleManagerException;

    
    /**
     * Returns a bucket from BM mapping it to a useful CRM bucket bean
     * @param ctx
     * @param msisdn MSISDN that combined with subscription type defines the subscription
     * @param subscriptionType subscription type that combined with MSISDN defines the subscription
     * @param bundleId 
     * @param bucketId
     * @return
     * @throws BucketDoesNotExistsException
     * @throws BundleManagerException
     */
    public SubscriberBucket getBucket(Context ctx, String msisdn, int subscriptionType, long bundleId)
        throws BucketDoesNotExistsException, BundleManagerException;
    
    /**
     * Update a bucket in BM and any additional information on CRM
     * @param ctx 
     * @param msisdn 
     * @param spid 
     * @param subscriptionType subscription type that combined with MSISDN defines the subscription
     * @param bundleId 
     * @param active 
     * @param prorate 
     * @throws BucketDoesNotExistsException
     * @throws BundleManagerException
     */
    public void updateBucket(Context ctx, String msisdn, int spid, int subscriptionType, long bundleId, boolean active, boolean prorate)
        throws BucketDoesNotExistsException, BundleManagerException;
    
    /**
     * Removes a bucket in BM, deleting (if any) any additional CRM information
     * @param ctx
     * @param spid 
     * @param msisdn MSISDN that combined with subscription type defines the subscription
     * @param subscriptionType subscription type that combined with MSISDN defines the subscription
     * @param bundleId
     * @return the overUsage for this subscriber
     * @throws BundleDoesNotExistsException
     * @throws BundleManagerException
     */
    public long removeBucket(Context ctx, int spid, String msisdn, int subscriptionType, long bundleId)
        throws BucketDoesNotExistsException, BundleManagerException;
    
    /**
     * Bulk removes all buckets from a list of subscribers
     * @param ctx
     * @param msisdns
     * @param bundleLists
     * @return 
     * @throws BundleManagerException
     */
    public Map removeBuckets(Context ctx, Collection msisdns, Collection bundleLists)
        throws BundleManagerException;

    
    /**
     * Increases the balance in a bucket in BM
     * @param ctx
     * @param msisdn MSISDN that combined with subscription type defines the subscription
     * @param spid
     * @param subscriptionType subscription type that combined with MSISDN defines the subscription
     * @param bundleId
     * @param amount
     * @return the result of the call 0 is successful
     * @throws BundleManagerException
     */
    public int increaseBalanceLimit(Context ctx, String msisdn, int spid, int subscriptionType, long bundleId, long amount)
        throws BundleManagerException;
    
    /**
     * Increases the balance in a bucket in BM
     * @param ctx
     * @param msisdn MSISDN that combined with subscription type defines the subscription
     * @param spid
     * @param subscriptionType subscription type that combined with MSISDN defines the subscription
     * @param bundleId
     * @param amount
     * @param inParamSet BM generic input parameters.
     * @param outParameterMap Map of BM generic output parameters. The method invocation is suppose to fill in this map.
     * @return the result of the call 0 is successful
     * @throws BundleManagerException
     */
    public int increaseBalanceLimit(Context ctx, String msisdn, int spid, int subscriptionType, long bundleId, long amount, Parameter[] inParamSet, Map<Short, Parameter> outParameterMap)
        throws BundleManagerException;
    
    /**
     * Decreases the balance in a bucket in BM
     * @param ctx
     * @param msisdn MSISDN that combined with subscription type defines the subscription
     * @param spid
     * @param subscriptionType subscription type that combined with MSISDN defines the subscription
     * @param bundleId
     * @param amount
     * @return the result of the call 0 is successful
     * @throws BundleManagerException
     */
    public int decreaseBalanceLimit(Context ctx, String msisdn, int spid, int subscriptionType, long bundleId, long amount)
        throws BundleManagerException;
    
    /**
     * Decreases the balance in a bucket in BM
     * @param ctx
     * @param msisdn MSISDN that combined with subscription type defines the subscription
     * @param spid
     * @param subscriptionType subscription type that combined with MSISDN defines the subscription
     * @param bundleId
     * @param amount
     * @param inParamSet URCS generic input parameters.
     * @param outParameterMap Map of BM generic output parameters. The method invocation is suppose to fill in this map.
     * @return the result of the call 0 is successful
     * @throws BundleManagerException
     */
    public int decreaseBalanceLimit(Context ctx, String msisdn, int spid, int subscriptionType, long bundleId, long amount, Parameter[] inParamSet, Map<Short, Parameter> outParameterMap)
        throws BundleManagerException;
    

    /**
     * Updates the status of a bucket
     * (this method replace the updateBucketActive from the bundle services) 
     * @param ctx
     * @param msisdn MSISDN that combined with subscription type defines the subscription
     * @param spid 
     * @param subscriptionType subscription type that combined with MSISDN defines the subscription
     * @param bundleId
     * @param status
     * @param prorated
     * @throws BucketDoesNotExistsException 
     * @throws BundleManagerException 
     */
    public void updateBucketStatus(Context ctx, String msisdn, int spid, int subscriptionType, long bundleId, boolean status, boolean prorated)
        throws BucketDoesNotExistsException, BundleManagerException;
    
}
