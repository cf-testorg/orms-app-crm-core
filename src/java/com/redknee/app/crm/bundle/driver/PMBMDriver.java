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

package com.redknee.app.crm.bundle.driver;

import com.redknee.framework.core.home.PMHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.product.bundle.manager.api.BundleService;

/**
 * Bundle Manager Driver Performance Measure Decorator.
 *
 * @author kevin.greer@redknee.com
 */
public class PMBMDriver extends AbstractPMBMDriver
{

    public PMBMDriver()
    {
    }

    public Home decorate(final String name, final Home home)
    {
        return new PMHome(getContext(), "BM:" + name, home);
    }

    // Homes

    /**
     * {@inheritDoc}
     */
    public Home bundleProfileApiHome()
    {
        return decorate("BundleProfile", getDelegate().bundleProfileApiHome());
    }

    /**
     * {@inheritDoc}
     */
    public Home bundleCategoryApiHome()
    {
        return decorate("BundleCategory", getDelegate().bundleCategoryApiHome());
    }

    /**
     * {@inheritDoc}
     */
    public Home bundleProfileApiHomev21()
    {
        return decorate("BundleProfilev21", getDelegate().bundleProfileApiHomev21());
    }

    /**
     * {@inheritDoc}
     */
    public Home bundleCategoryApiHomev21()
    {
        return decorate("BundleCategoryv21", getDelegate().bundleCategoryApiHomev21());
    }

    /**
     * {@inheritDoc}
     */
    public Home subscriberBucketApiHome()
    {
        return decorate("SubscriberBucket", getDelegate().subscriberBucketApiHome());
    }

    /**
     * {@inheritDoc}
     */
    public Home subscriberBucketRetrievalApiHome()
    {
        return decorate("SubscriberBucketRetrieval", getDelegate().subscriberBucketRetrievalApiHome());
    }

    /**
     * {@inheritDoc}
     */
    public Home balanceApplicationHome()
    {
        return decorate("BalanceApplication", getDelegate().balanceApplicationHome());
    }


    /**
     * {@inheritDoc}
     */
    public Home subscriberBucketApiHomev21()
    {
        return decorate("SubscriberBucketv21", getDelegate().subscriberBucketApiHomev21());
    }

    /**
     * {@inheritDoc}
     */
    public Home subscriberBucketRetrievalApiHomev21()
    {
        return decorate("SubscriberBucketRetrievalv21", getDelegate().subscriberBucketRetrievalApiHomev21());
    }

    // Services

    /**
     * {@inheritDoc}
     */
    public BundleService bundleService()
    {
        return getDelegate().bundleService();
    }

    /**
     * {@inheritDoc}
     */
    public com.redknee.product.bundle.manager.api.v21.BundleService bundleServicev21()
    {
        return getDelegate().bundleServicev21();
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return getClass().getName() + "(" + getDelegate() + ")";
    }
}

