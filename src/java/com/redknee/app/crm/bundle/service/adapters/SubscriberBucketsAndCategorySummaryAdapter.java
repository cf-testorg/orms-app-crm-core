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
package com.redknee.app.crm.bundle.service.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.redknee.app.crm.bundle.Balance;
import com.redknee.app.crm.bundle.SubscriberBucket;
import com.redknee.app.crm.bundle.SubscriberBucketsAndBalances;
import com.redknee.app.crm.bundle.service.adapters.CollectionAdapter.ComponentAdapter;
import com.redknee.app.crm.client.SubscriberBucketsAndCategorySummaryHolder;
import com.redknee.product.bundle.manager.provision.v5_0.bucket.BalancesV2;
import com.redknee.product.bundle.manager.provision.v5_0.bucket.SubscriberBucketRetrievalV2;

/**V2
 * @author sbanerjee
 *
 */
public class SubscriberBucketsAndCategorySummaryAdapter extends
        SubscriberBucketsAndBalances
{

    public SubscriberBucketsAndCategorySummaryAdapter(
            SubscriberBucketsAndCategorySummaryHolder bucketsWithSummary)
    {
        
        adaptAndSetBuckets(bucketsWithSummary.getBuckets());
        adaptAndSetSummaryByCategory(bucketsWithSummary.getCategorySummary());
    }

    private void adaptAndSetSummaryByCategory(
            Collection<BalancesV2> balances)
    {
        final CollectionAdapter<BalancesV2, Balance, Collection<BalancesV2>, List<Balance>> 
                collectionAdapter = new 
                    CollectionAdapter<BalancesV2, Balance, Collection<BalancesV2>, List<Balance>>
            (
                    new ComponentAdapter<BalancesV2, Balance, Collection<BalancesV2>, List<Balance>>()
                    {
                        @Override
                        public Balance newInstance()
                        {
                            return new Balance();
                        }

                        @Override
                        public List<Balance> newCollection(int length)
                        {
                            return new ArrayList<Balance>();
                        }

                        @Override
                        public void adapt(BalancesV2 fromComponent,
                                Balance toComponent)
                        {
                            SubscriberBucketV4ToBalanceAdapter.adaptV2(fromComponent, toComponent);
                        }
                    }
            );
        
        this.setSummaryByCategory(collectionAdapter.adaptNonNull(balances));
    }

    /**
     * @param bucketsWithSummary
     */
    private void adaptAndSetBuckets(
            Collection<SubscriberBucketRetrievalV2> buckets)
    {
        final CollectionAdapter<SubscriberBucketRetrievalV2, SubscriberBucket, Collection<SubscriberBucketRetrievalV2>, List<SubscriberBucket>> 
                collectionAdapter = new
                        CollectionAdapter<SubscriberBucketRetrievalV2, SubscriberBucket, Collection<SubscriberBucketRetrievalV2>, List<SubscriberBucket>>
                (
                        new CollectionAdapter.ComponentAdapter<SubscriberBucketRetrievalV2, SubscriberBucket, Collection<SubscriberBucketRetrievalV2>, List<SubscriberBucket>>()
                        {
                            @Override
                            public SubscriberBucket newInstance()
                            {
                                return new SubscriberBucket();
                            }

                            @Override
                            public List<SubscriberBucket> newCollection(int length)
                            {
                                return new ArrayList<SubscriberBucket>();
                            }

                            @Override
                            public void adapt(SubscriberBucketRetrievalV2 fromComponent,
                                    SubscriberBucket toComponent)
                            {
                                SubscriberBucketV4CrmSubscriberBucketAdapter.adaptV2(fromComponent, toComponent);
                            }
                        }
                );
        
        this.setBuckets(collectionAdapter.adaptNonNull(buckets));
    }
}