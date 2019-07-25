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
import com.redknee.app.crm.bundle.StatusEnum;
import com.redknee.app.crm.bundle.SubscriberBucket;
import com.redknee.app.crm.bundle.UnitTypeEnum;
import com.redknee.product.bundle.manager.provision.v5_0.bucket.SubscriberBucketRetrievalV2;
import com.redknee.product.bundle.manager.provision.v5_0.bucket.SubscriberBucketRetrieval;

/**
 * @author sbanerjee
 *
 */
@SuppressWarnings("serial")
public class SubscriberBucketV4CrmSubscriberBucketAdapter extends
        SubscriberBucket
{

    public SubscriberBucketV4CrmSubscriberBucketAdapter(
            SubscriberBucketRetrievalV2 bal)
    {
        set(bal);
    }
    
    public SubscriberBucketV4CrmSubscriberBucketAdapter(
            SubscriberBucketRetrieval bal)
    {
        set(bal);
    }
    
    
    public void set(SubscriberBucketRetrievalV2 bal)
    {
        adaptV2(bal, this);
    }
    
    public void set(SubscriberBucketRetrieval bal)
    {
        adapt(bal, this);
    }
    
    public static void adapt(SubscriberBucketRetrieval from, SubscriberBucket to)
    {
        Balance balance = new SubscriberBucketV4ToBalanceAdapter(from);
        to.setMsisdn(from.msisdn);
        to.setBundleId(from.bundleId);
        to.setSpid(from.spid);
        to.setBucketId(from.bucketId);
        to.setRegularBal(balance);
        to.setUnitType(UnitTypeEnum.get((short)from.unitType.value()));
        to.setExpiryTime(from.expiryTime);
        to.setProvisionTime(from.provisionTime);
        to.setActivationTime(from.activationTime);
        to.setStatus(StatusEnum.get((short)from.status.value()));
    }
    
    public static void adaptV2(SubscriberBucketRetrievalV2 from, SubscriberBucket to)
    {
        Balance balance = new SubscriberBucketV4ToBalanceAdapter(from);
        to.setMsisdn(from.msisdn);
        to.setBundleId(from.bundleId);
        to.setSpid(from.spid);
        to.setBucketId(from.bucketId);
        to.setRegularBal(balance);
        to.setUnitType(UnitTypeEnum.get((short)from.unitType.value()));
        to.setExpiryTime(from.expiryTime);
        to.setProvisionTime(from.provisionTime);
        to.setActivationTime(from.activationTime);
        to.setStatus(StatusEnum.get((short)from.status.value()));
    }
    
}