/*
 *  Install.java
 *
 *  Author : kgreer
 *  Date   : Oct 03, 2005
 *
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily
 * available. Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee, no unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the license agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */

package com.redknee.app.crm.bundle.driver;


import com.redknee.app.crm.bundle.BundleCategoryHome;
import com.redknee.app.crm.bundle.SubcriberBucketModelBundleManager;
import com.redknee.app.crm.bundle.SubscriberBucketHome;
import com.redknee.app.crm.support.BundleSupportHelper;
import com.redknee.app.crm.support.DeploymentTypeSupportHelper;
import com.redknee.framework.core.home.PMHome;
import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgent;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.RMIHomeServer;
import com.redknee.framework.xhome.home.ReadOnlyHome;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.product.bundle.manager.api.BalanceApplicationHome;
import com.redknee.product.bundle.manager.api.BundleService;

/**
 * Install BundleManager Driver support.
 *
 * @author kgreer
 */
public class Install extends CoreSupport implements ContextAgent
{

    /**
     * Default constructor
     */
    public Install()
    {
    }

    /**
     * {@inheritDoc}
     */
    public void execute(Context ctx) throws AgentException
    {
        final BMDriverProxy driver = (BMDriverProxy) bindBean(ctx,
                BMDriverProxy.class);

        driver.setContext(ctx);
        driver.install(ctx);
        
        prepareBundleManagerDependency(ctx, driver);
        installRMIHomeDecorators(ctx, driver);
        
        driver.addPropertyChangeListener(new BMDriverPropertyChangeListener(ctx));
    }

    /**
     * Install all the RMI decorator homes for services to another applications
     * @param ctx
     * @param driver
     */
    private void installRMIHomeDecorators(Context ctx, BMDriverProxy driver)
    {
        SubcriberBucketModelBundleManager generator =
            BundleSupportHelper.get(ctx).getSubscriberBucketModel(ctx);

        // [ltang]
        if (DeploymentTypeSupportHelper.get(ctx).isBas(ctx) || DeploymentTypeSupportHelper.get(ctx).isSingle(ctx))
        {
            registerRMIHomeServer(ctx, BundleCategoryHome.class, BMDriver.BUNDLE_CATEGORY_DRIVER_KEY);
        }

        // [jhughes] required by selfcare
        registerRMIHomeServer(ctx, SubscriberBucketHome.class, BMDriver.SUBSCRIBER_BUCKET_DRIVER_KEY);
        if (generator.installSubscriberBucketV6())
        {
            registerRMIHomeServer(ctx, generator.getSubscriberBucketHome(), BMDriver.SUBSCRIBER_BUCKET_DRIVER_KEY);
        }

    }

    /**
     * Registers a home as an RMIHome server
     * @param ctx the operating context
     * @param homeClass the home class that needs to be registered to
     */
    protected void registerRMIHomeServer(Context ctx, Class homeClass, String key)
    {
        try
        {
            Home home = (Home) ctx.get(homeClass);
            //there is a contextualizing key home that is nullifing the real key
            if (home == null)
            {
                home =(Home) ctx.get(key);
            }
            home = new PMHome(ctx,homeClass.getName(),home);
            home = new ReadOnlyHome(home);
            new RMIHomeServer(ctx,home,homeClass.getName()).register();
        }
        catch (java.rmi.RemoteException e)
        {
            new MajorLogMsg(this,
                    "failed to register  " + homeClass.getName() + " : "
                            + e.getMessage(), e).log(ctx);
        }
    }

    /**
     * For Backward compatibility with 2.5 and older, CRM will install all the previous homes
     * in it's context.
     * This homes are exclusively for RMI Handler to BM
     * This method can be deprecated one the Model Bundle Manager is deleted or not maintained anymore
     * @param ctx
     * @param driver
     */
    private void prepareBundleManagerDependency(Context ctx,
            BMDriverProxy driver)
    {
        // Homes

        SubcriberBucketModelBundleManager generator =
            BundleSupportHelper.get(ctx).getSubscriberBucketModel(ctx);

        ctx.put(com.redknee.product.bundle.manager.api.BundleProfileApiHome.class,
                driver.bundleProfileApiHome());
        ctx.put(com.redknee.product.bundle.manager.api.v21.BundleCategoryApiHome.class,
                driver.bundleCategoryApiHomev21());

        // [ltang]
        if (DeploymentTypeSupportHelper.get(ctx).isBas(ctx) || DeploymentTypeSupportHelper.get(ctx).isSingle(ctx))
        {
            try
            {
                new RMIHomeServer(ctx,
                        new ReadOnlyHome(
                                new PMHome(
                                        ctx,
                                        com.redknee.product.bundle.manager.api.v21.BundleCategoryApiHome.class.getName(),
                                        (Home) ctx.get(com.redknee.product.bundle.manager.api.v21.BundleCategoryApiHome.class))),
                                        com.redknee.product.bundle.manager.api.v21.BundleCategoryApiHome.class.getName()).register();
            }
            catch (Exception e)
            {
                new com.redknee.framework.xlog.log.MajorLogMsg(this, "failed to register RMIHomeServer for BundleCategoryApiHome: "+e.getMessage(), e).log(ctx);
            }
        }
        
        ctx.put(com.redknee.product.bundle.manager.api.v21.SubscriberBucketApiHome.class,
                driver.subscriberBucketApiHomev21());
        ctx.put(com.redknee.product.bundle.manager.api.v21.SubscriberBucketRetrievalApiHome.class,
                        driver.subscriberBucketRetrievalApiHomev21());

        //If CRM has installed model 2_6 and up this should be installed
        if (generator != null && generator.installSubscriberBucketV6())
        {
            ctx.put(generator.getSubscriberBucketHome(),
                    driver.subscriberBucketRetrievalApiHomev26());
        }

        ctx.put(BalanceApplicationHome.class, driver.balanceApplicationHome());

        // Services

        ctx.put(BundleService.class, driver.bundleService());
        ctx.put(com.redknee.product.bundle.manager.api.v21.BundleService.class,
                driver.bundleServicev21());
    }

}
