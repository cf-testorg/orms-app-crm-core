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
package com.redknee.app.crm.bundle;

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;

import com.redknee.app.crm.bundle.driver.Constants;
import com.redknee.app.crm.support.BundleSupportHelper;
import com.redknee.product.bundle.manager.api.v21.Balances;
import com.redknee.product.bundle.manager.api.v21.SubscriberBucketRetrievalApi;
import com.redknee.product.bundle.manager.api.v21.SubscriberBucketRetrievalApiHome;
import com.redknee.product.bundle.manager.api.v21.SubscriberBucketRetrievalApiXDBHome;
import com.redknee.product.bundle.manager.api.v21.SubscriberBucketRetrievalApiXInfo;

/**
 * Handles the Subscriber Bucket for Model 2.5 and below
 * @author arturo.medina@redknee.com
 *
 */
public class SubcriberBucketModelBundleManagerV21 implements
        SubcriberBucketModelBundleManager
{

    /**
     * {@inheritDoc}
     */
    public SubscriberBucket adaptSubscriberBucket(Context ctx, Object source,
            SubscriberBucket destination)
    {
        if (source instanceof SubscriberBucketRetrievalApi)
        {
            SubscriberBucketRetrievalApi profile = (SubscriberBucketRetrievalApi) source;
            destination.setRegularBal(adaptRegularBalance(ctx, profile.getRegularBal()));
            destination.setUnitType((UnitTypeEnum)BundleSupportHelper.get(ctx).mapBundleEnums(profile.getUnitType(), destination.getUnitType()));
        }

        return destination;
    }

    private Balance adaptRegularBalance(Context ctx, Balances regularBal)
    {
        return BundleSupportHelper.get(ctx).adaptRegularBalance(ctx, regularBal);
    }

    /**
     * {@inheritDoc}
     */

    public PropertyInfo getBucketIdXInfo()
    {
        return SubscriberBucketRetrievalApiXInfo.BUCKET_ID;
    }

    /**
     * {@inheritDoc}
     */
    public PropertyInfo getBundleIdXInfo()
    {
        return SubscriberBucketRetrievalApiXInfo.BUNDLE_ID;
    }

    /**
     * {@inheritDoc}
     */
    public PropertyInfo getMSISDNXInfo()
    {
        return SubscriberBucketRetrievalApiXInfo.MSISDN;
    }

    /**
     * {@inheritDoc}
     */
    public Class getSubscriberBucketHome()
    {
        return SubscriberBucketRetrievalApiHome.class;
    }

    /**
     * {@inheritDoc}
     */
    public Home getSubscriberBucketXDBHome(Context ctx)
    {
        return new SubscriberBucketRetrievalApiXDBHome(ctx);
    }

    /**
     * {@inheritDoc}
     */
    public XInfo getSubscriberBucketXInfo()
    {
        return SubscriberBucketRetrievalApiXInfo.instance();
    }

    /**
     * {@inheritDoc}
     */
    public Home getSubscriberBucketXMLHome(Context ctx, String fileName)
    {
        return new com.redknee.product.bundle.manager.api.v21.SubscriberBucketRetrievalApiXMLHome(
                ctx, CoreSupport.getFile(ctx, Constants.SUB_BUCKET_RETRIEVAL_HOME_V21));
    }

    /**
     * {@inheritDoc}
     */
    public boolean installSubscriberBucketV6()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public Object unAdaptSubscriberBucket(Context ctx, SubscriberBucket bucket,
            Object destination)
    {
        if (destination instanceof SubscriberBucketRetrievalApi)
        {
            SubscriberBucketRetrievalApi profile = (SubscriberBucketRetrievalApi) destination;
            profile.setRegularBal(unAdaptRegularBalance(ctx, bucket.getRegularBal()));
            profile.setUnitType((com.redknee.product.bundle.manager.api.v21.UnitTypeEnum)
                    BundleSupportHelper.get(ctx).mapBundleEnums(bucket.getUnitType(), profile.getUnitType()));

        }
        return destination;
    }

    
    private Balances unAdaptRegularBalance(Context ctx, Balance regularBal)
    {
        return BundleSupportHelper.get(ctx).unAdaptRegularBalance(ctx, regularBal);
    }

}
