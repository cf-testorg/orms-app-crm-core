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

import com.redknee.app.crm.bundle.Balance;
import com.redknee.product.bundle.manager.provision.v5_0.bucket.BalancesV2;
import com.redknee.product.bundle.manager.provision.v5_0.bucket.Balances;

import com.redknee.product.bundle.manager.provision.v5_0.bucket.SubscriberBucketRetrievalV2;
import com.redknee.product.bundle.manager.provision.v5_0.bucket.SubscriberBucketRetrieval;

/**
 * @author sbanerjee
 * Refactored from com.redknee.app.crm.bundle.service.CORBASubscriberBucketHandler
 */
@SuppressWarnings("serial")
public class SubscriberBucketV4ToBalanceAdapter extends Balance
{
    public SubscriberBucketV4ToBalanceAdapter(SubscriberBucketRetrieval bal)
    {
        set(bal);
    }
    
    public void set(SubscriberBucketRetrieval bal)
    {
        adapt(bal, this);
    }

    public SubscriberBucketV4ToBalanceAdapter(SubscriberBucketRetrievalV2 bal)
    {
        set(bal);
    }
    
    public void set(SubscriberBucketRetrievalV2 bal)
    {
        adaptV2(bal, this);
    }

    public static void adaptV2(SubscriberBucketRetrievalV2 from, Balance to)
    {
        final BalancesV2 regularBalance = from.regularBalance;
        adaptV2(regularBalance, to);
    }

    
    public static void adapt(SubscriberBucketRetrieval from, Balance to)
    {
        final Balances regularBalance = from.regularBalance;
        adapt(regularBalance, to);
    }

    /**
     * @param to
     * @param from
     * @throws IllegalArgumentException
     */
    public static void adapt(final Balances from, Balance to)
            throws IllegalArgumentException
    {
        to.setApplicationId(from.applicationId);
        //(bal.regularBalance.unlimitedQuota);
        to.setPersonalLimit(from.personalLimit);
        to.setPersonalUsed(from.personalUsed);
        to.setPersonalBalance(from.personalBalance);
        to.setRolloverLimit(from.rolloverLimit);
        to.setRolloverUsed(from.rolloverUsed);
        to.setRolloverBalance(from.rolloverBalance);
        to.setGroupLimit(from.groupLimit);
        to.setGroupUsed(from.groupUsed);
        to.setGroupBalance(from.groupBalance);
    }
    
    /**
     * @param to
     * @param from
     * @throws IllegalArgumentException
     */
    public static void adaptV2(final BalancesV2 from, Balance to)
            throws IllegalArgumentException
    {
        to.setApplicationId(from.applicationId);
        //(bal.regularBalance.unlimitedQuota);
        to.setPersonalLimit(from.personalLimit);
        to.setPersonalUsed(from.personalUsed);
        to.setPersonalBalance(from.personalBalance);
        to.setRolloverLimit(from.rolloverLimit);
        to.setRolloverUsed(from.rolloverUsed);
        to.setRolloverBalance(from.rolloverBalance);
        to.setGroupLimit(from.groupLimit);
        to.setGroupUsed(from.groupUsed);
        to.setGroupBalance(from.groupBalance);
    }
}
