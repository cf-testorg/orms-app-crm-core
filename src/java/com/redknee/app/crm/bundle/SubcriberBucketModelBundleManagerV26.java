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
import com.redknee.product.bundle.manager.api.v21.SubscriberBucketRetrievalApiV26;
import com.redknee.product.bundle.manager.api.v21.SubscriberBucketRetrievalApiV26Home;
import com.redknee.product.bundle.manager.api.v21.SubscriberBucketRetrievalApiV26XDBHome;
import com.redknee.product.bundle.manager.api.v21.SubscriberBucketRetrievalApiV26XInfo;


/**
 * Handles the Subscriber Bucket for Model 2.5 and below
 * <b> Note: This class need to either be deleted or all it's method commented out if
 * the model for BM is 2.5</b>
 * @author arturo.medina@redknee.com
 *
 */
public class SubcriberBucketModelBundleManagerV26 implements
        SubcriberBucketModelBundleManager
{

    /**
     * {@inheritDoc}
     */
    public SubscriberBucket adaptSubscriberBucket(Context ctx, Object source,
            SubscriberBucket destination)
    {
        if (source instanceof SubscriberBucketRetrievalApiV26)
        {
            SubscriberBucketRetrievalApiV26 profile = (SubscriberBucketRetrievalApiV26) source;
            destination.setRegularBal(adaptRegularBalance(ctx,profile.getRegularBal()));
            destination.setUnitType((UnitTypeEnum)BundleSupportHelper.get(ctx).mapBundleEnums(profile.getUnitType(), destination.getUnitType()));
            destination.setStatus((StatusEnum)BundleSupportHelper.get(ctx).mapBundleEnums(profile.getStatus(), destination.getStatus()));
        }
        return (SubscriberBucket) source;
    }

    /**
     * {@inheritDoc}
     */
    public PropertyInfo getBucketIdXInfo()
    {
        return SubscriberBucketRetrievalApiV26XInfo.BUCKET_ID;
    }

    /**
     * {@inheritDoc}
     */
    public PropertyInfo getBundleIdXInfo()
    {
        return SubscriberBucketRetrievalApiV26XInfo.BUNDLE_ID;
    }

    /**
     * {@inheritDoc}
     */
    public PropertyInfo getMSISDNXInfo()
    {
        return SubscriberBucketRetrievalApiV26XInfo.MSISDN;
    }

    /**
     * {@inheritDoc}
     */
    public Class getSubscriberBucketHome()
    {
        return SubscriberBucketRetrievalApiV26Home.class;
    }

    /**
     * {@inheritDoc}
     */
    public Home getSubscriberBucketXDBHome(Context ctx)
    {
        return new SubscriberBucketRetrievalApiV26XDBHome(ctx);
    }

    /**
     * {@inheritDoc}
     */
    public XInfo getSubscriberBucketXInfo()
    {
        return SubscriberBucketRetrievalApiV26XInfo.instance();
    }

    /**
     * {@inheritDoc}
     */
    public Home getSubscriberBucketXMLHome(Context ctx, String fileName)
    {
        return new com.redknee.product.bundle.manager.api.v21.SubscriberBucketRetrievalApiV26XMLHome(
                ctx, CoreSupport.getFile(ctx, Constants.SUB_BUCKET_RETRIEVAL_HOME_V26));
    }

    /**
     * {@inheritDoc}
     */
    public boolean installSubscriberBucketV6()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public Object unAdaptSubscriberBucket(Context ctx, SubscriberBucket source,
            Object destination)
    {
        SubscriberBucketRetrievalApiV26 profile = (SubscriberBucketRetrievalApiV26) destination;
        if (destination instanceof SubscriberBucketRetrievalApiV26)
        {
            profile.setRegularBal(unAdaptRegularBalance(ctx, source.getRegularBal()));

            profile.setUnitType((com.redknee.product.bundle.manager.api.v21.UnitTypeEnum)
                    BundleSupportHelper.get(ctx).mapBundleEnums(source.getUnitType(), profile.getUnitType()));

            profile.setStatus((com.redknee.product.bundle.manager.api.v21.StatusEnum)
                    BundleSupportHelper.get(ctx).mapBundleEnums(source.getStatus(), profile.getStatus()));
        }
        return profile;
    }

    /**
     *
     * @param ctx
     * @param regularBal
     * @return
     */
    private Balances unAdaptRegularBalance(Context ctx, Balance regularBal)
    {
        return BundleSupportHelper.get(ctx).unAdaptRegularBalance(ctx, regularBal);
    }

    /**
     *
     * @param ctx
     * @param regularBal
     * @return
     */
    private Balance adaptRegularBalance(Context ctx, Balances regularBal)
    {
        return BundleSupportHelper.get(ctx).adaptRegularBalance(ctx, regularBal);
    }


}

