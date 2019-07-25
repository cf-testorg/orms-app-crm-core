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

import java.util.List;

import java.util.Collection;

import com.redknee.app.crm.bundle.exception.BucketDoesNotExistsException;
import com.redknee.app.crm.bundle.exception.BundleManagerException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.app.crm.bundle.*;


/**
 * Handles all CRM related business logic related to Bundle Manager's Bucket profile
 * @author arturo.medina@redknee.com
 *
 */
public interface CRMSubscriberBucketProfile extends BundleManagerSubscriberBucket
{
    /**
     * Returns a particular bucket based on it's bucket Id
     * @param ctx
     * @param bucketId
     * @return
     * @throws BucketDoesNotExistsException
     * @throws BundleManagerException
     */
    public SubscriberBucket getBucketById(Context ctx, long bucketId)
        throws BucketDoesNotExistsException, BundleManagerException;
    
    /**
     * Returns all bucket for a particular subscriber
     * @param ctx
     * @param msisdn MSISDN that combined with subscription type defines the subscription
     * @param subscriptionType subscription type that combined with MSISDN defines the subscription
     * @return
     * @throws BundleManagerException
     */
    public Home getBuckets(Context ctx, String msisdn, int subscriptionType)
        throws BundleManagerException;
    
    /**
     * Returns all relevant balance information for every bucket belonging to a subscriber. Also includes
     * a per-category summary of balances information.
     * @param ctx
     * @param msisn
     * @return A holder containing
     *  <ul> 
     * <li/>A summary (list) of balances information, per-category
     * <li/>Buckets List
     * </ul>
     * @throws BundleManagerException
     */
    public SubscriberBucketsAndBalances getBucketsWithCategorySummary(Context ctx, String msisn)
        throws BundleManagerException;
    
    /**
     * Get all balances from bucket of a particular subscriber' msisdn
     * @param ctx
     * @param msisdn MSISDN that combined with subscription type defines the subscription
     * @param subscriptionType subscription type that combined with MSISDN defines the subscription
     * @return
     * @throws BucketDoesNotExistsException
     * @throws BundleManagerException
     */
    public List getBalances(Context ctx, String msisdn, int subscriptionType)
        throws BucketDoesNotExistsException, BundleManagerException;


}
