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

import com.redknee.app.crm.client.RemoteServiceStatus;

import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xhome.home.Home;
import com.redknee.product.bundle.manager.api.BundleService;
import com.redknee.product.bundle.manager.api.ProfileService;
import com.redknee.framework.xhome.context.Context;

/**
 * Bundle Manager Driver.
 *
 * @author kevin.greer@redknee.com
 */
public interface BMDriver extends ContextAware, RemoteServiceStatus
{
    public static String BUNDLE_PROFILE_DRIVER_KEY = "BundleProfileDriverKey";
    public static String BUNDLE_CATEGORY_DRIVER_KEY = "BundleCategoryDriverKey";
    public static String SUBSCRIBER_BUCKET_DRIVER_KEY = "SubscriberBucketDriverKey";

    // Homes

    Home bundleProfileApiHome();

    Home bundleCategoryApiHome();

    Home subscriberBucketApiHome();

    Home subscriberBucketRetrievalApiHome();

    Home balanceApplicationHome();

    //v21 Homes
    Home bundleProfileApiHomev21();

    Home bundleCategoryApiHomev21();

    Home subscriberBucketApiHomev21();

    Home subscriberBucketRetrievalApiHomev21();

   public Home subscriberBucketRetrievalApiHomev26();

    // Services

    BundleService bundleService();

    //v21 Services

    com.redknee.product.bundle.manager.api.v21.BundleService bundleServicev21();

   public void injectBundleManagerHomes(Context ctx);
   
   public void install(Context ctx);
   
   public void uninstall(Context ctx);
}


