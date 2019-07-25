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
package com.redknee.app.crm.client;

import java.util.Collection;
import com.redknee.product.bundle.manager.provision.v5_0.bucket.SubscriberBucketRetrievalV2;
import com.redknee.product.bundle.manager.provision.v5_0.bucket.BalancesV2;

/**
 * A holder for SubProvisioning V4 API.
 * 
 * @author sbanerjee
 * 
 * Should we have 'V4' suffixed to this class.
 *
 */
public class SubscriberBucketsAndCategorySummaryHolder
{
    private Collection<SubscriberBucketRetrievalV2> buckets;
    private Collection<BalancesV2> categorySummary;
    /**
     * @return the buckets
     */
    public Collection<SubscriberBucketRetrievalV2> getBuckets()
    {
        return buckets;
    }
    /**
     * @param buckets the buckets to set
     */
    public void setBuckets(Collection<SubscriberBucketRetrievalV2> buckets)
    {
        this.buckets = buckets;
    }
    /**
     * @return the categorySummary
     */
    public Collection<BalancesV2> getCategorySummary()
    {
        return categorySummary;
    }
    /**
     * @param categorySummary the categorySummary to set
     */
    public void setCategorySummary(Collection<BalancesV2> categorySummary)
    {
        this.categorySummary = categorySummary;
    }
}