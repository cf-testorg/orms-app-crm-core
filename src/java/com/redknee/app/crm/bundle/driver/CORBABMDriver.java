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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextSupport;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.msp.SpidAwareHome;
import com.redknee.framework.xlog.log.MajorLogMsg;

import com.redknee.app.crm.bundle.BundleCategoryTransientHome;
import com.redknee.app.crm.bundle.BundleCategoryXDBHome;
import com.redknee.app.crm.bundle.BundleProfileTransientHome;
import com.redknee.app.crm.bundle.home.BMCORBABundleProfileInfoHome;
import com.redknee.app.crm.bundle.home.BMCORBACategoryInfoHome;
import com.redknee.app.crm.bundle.home.BMCORBASubscriberBucketInfoHome;
import com.redknee.app.crm.bundle.home.BundleProfileFieldSettingHome;
import com.redknee.app.crm.bundle.service.CORBABundleCategoryHandler;
import com.redknee.app.crm.bundle.service.CORBABundleProfileHandler;
import com.redknee.app.crm.bundle.service.CORBASubscriberBucketHandler;
import com.redknee.app.crm.bundle.service.CRMBundleCategory;
import com.redknee.app.crm.bundle.service.CRMBundleProfile;
import com.redknee.app.crm.bundle.service.CRMSubscriberBucketProfile;
import com.redknee.app.crm.client.BMBundleCategoryCorbaClient;
import com.redknee.app.crm.client.BMBundleProfileCorbaClient;
import com.redknee.app.crm.client.BMSubscriberBucketCorbaClient;
import com.redknee.app.crm.client.BundleCategoryProvisionClient;
import com.redknee.app.crm.client.BundleProfileProvisionClient;
import com.redknee.app.crm.client.ConnectionStatus;
import com.redknee.app.crm.core.agent.Install;
import com.redknee.app.crm.xhome.adapter.StaticContextBeanAdapter;
import com.redknee.app.crm.xhome.home.TotalCachingHome;
import com.redknee.product.bundle.manager.api.BalanceApplicationXDBHome;
import com.redknee.product.bundle.manager.api.BundleService;

/**
 * Driver to hold all CORBA related homes when CRM select a CORBA integration with BM.
 *
 * @author arturo.medina@redknee.com
 */
public class CORBABMDriver extends AbstractCORBABMDriver implements BMDriver, BMRemoteService
{
    private static final String SERVICE_NAME = "BundleManager";
    private static final String SERVICE_DESCRIPTION = "CORBA client for Bundle Manager services";

    /**
     * Initiates the BM services and pipelines to use on the CORBA service.
     *
     * @param ctx the operating context
     */
    public CORBABMDriver(final Context ctx)
    {
        setContext(ctx);
    }

    public CORBABMDriver()
    {
    }

    /**
     * Instantiates the homes needed for the CORBA interface
     *
     * @param ctx the operating context
     */
    private void initHomes(final Context ctx)
    {
        final Context subCtx = ctx.createSubContext();
        final Context appCtx = (Context)ctx.get("app");
        try
        {
            bundleProfileHome_ = (Home) ctx.get(com.redknee.app.crm.bundle.BundleProfileXDBHome.class);
            bundleProfileHome_ = new TotalCachingHome(subCtx, new BundleProfileTransientHome(ctx), bundleProfileHome_);
            bundleProfileHome_ = new AdapterHome(
                    subCtx,
                    bundleProfileHome_, 
                    new StaticContextBeanAdapter<com.redknee.app.crm.bundle.BundleProfile, com.redknee.app.crm.bean.core.BundleProfile>(
                            com.redknee.app.crm.bundle.BundleProfile.class, 
                            com.redknee.app.crm.bean.core.BundleProfile.class, appCtx));
            
            bundleProfileHome_ = new BMCORBABundleProfileInfoHome(subCtx, bundleProfileHome_);
            bundleProfileHome_ = new BundleProfileFieldSettingHome(subCtx, bundleProfileHome_);
            bundleProfileHome_ = new SpidAwareHome(subCtx, bundleProfileHome_);
        }
        catch (Throwable t)
        {
            new MajorLogMsg(this, "Error creating BundleProfileHome.", t).log(subCtx);
        }
        try
        {
            bundleCategoryHome_ = new BundleCategoryXDBHome(subCtx, "BundleCategory");
            bundleCategoryHome_ = new TotalCachingHome(subCtx, new BundleCategoryTransientHome(subCtx), bundleCategoryHome_);
            bundleCategoryHome_ = new BMCORBACategoryInfoHome(subCtx, bundleCategoryHome_);
            bundleCategoryHome_ = new SpidAwareHome(subCtx, bundleCategoryHome_);
        }
        catch (Throwable t)
        {
            new MajorLogMsg(this, "Error creating BundleCategoryHome.", t).log(subCtx);
        }
        try
        {
            subscriberBucketHome_ = new BMCORBASubscriberBucketInfoHome(subCtx);
            subscriberBucketHome_ = new SpidAwareHome(subCtx, subscriberBucketHome_);
        }
        catch (Throwable t)
        {
            new MajorLogMsg(this, "Error creating SubscriberBucketHome.", t).log(subCtx);
        }

        try
        {
            balanceApplicationHome_ = new BalanceApplicationXDBHome(subCtx);
        }
        catch (Throwable t)
        {
            new MajorLogMsg(this, "Error creating BalanceApplicationHome.", t).log(subCtx);
        }
    }

    /**
     * Instantiates the BM services for CRM
     *
     * @param ctx the operating context
     */
    private void initServices(final Context ctx)
    {
        categoryService_ = new CORBABundleCategoryHandler();
        bundleService_ = new CORBABundleProfileHandler();
        bucketService_ = new CORBASubscriberBucketHandler();
    }

    /**
     * {@inheritDoc}
     */
    public Home balanceApplicationHome()
    {
        return balanceApplicationHome_;
    }

    /**
     * {@inheritDoc}
     */
    public Home bundleCategoryApiHome()
    {
        return bundleCategoryHome_;
    }

    /**
     * {@inheritDoc}
     */
    public Home bundleCategoryApiHomev21()
    {
        return bundleCategoryHome_;
    }

    /**
     * {@inheritDoc}
     */
    public Home bundleProfileApiHome()
    {
        return bundleProfileHome_;
    }

    /**
     * {@inheritDoc}
     */
    public Home bundleProfileApiHomev21()
    {
        return bundleProfileHome_;
    }

    /**
     * {@inheritDoc}
     */
    public BundleService bundleService()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public com.redknee.product.bundle.manager.api.v21.BundleService bundleServicev21()
    {
        return null;
    }


    /**
     * {@inheritDoc}
     */
    public Home subscriberBucketApiHome()
    {
        return subscriberBucketHome_;
    }

    /**
     * {@inheritDoc}
     */
    public Home subscriberBucketApiHomev21()
    {
        return subscriberBucketHome_;
    }

    /**
     * {@inheritDoc}
     */
    public Home subscriberBucketRetrievalApiHome()
    {
        return subscriberBucketHome_;
    }

    /**
     * {@inheritDoc}
     */
    public Home subscriberBucketRetrievalApiHomev21()
    {
        return subscriberBucketHome_;
    }

    /**
     * {@inheritDoc}
     */
    public Home subscriberBucketRetrievalApiHomev26()
    {
        return subscriberBucketHome_;
    }

    /**
     * {@inheritDoc}
     */
    public Context getContext()
    {
        return context_;
    }

    /**
     * {@inheritDoc}
     */
    public void setContext(Context arg0)
    {
        context_ = arg0;
    }

    /**
     * {@inheritDoc}
     */
    public CRMBundleCategory getBundleCategoryService()
    {
        return categoryService_;
    }

    /**
     * {@inheritDoc}
     */
    public CRMBundleProfile getBundleProfileService()
    {
        return bundleService_;
    }

    /**
     * {@inheritDoc}
     */
    public CRMSubscriberBucketProfile getSubscriberBucketService()
    {
        return bucketService_;
    }


    /**
     * {@inheritDoc}
     */
    public void injectBundleManagerHomes(final Context ctx)
    {
        ctx.put(BUNDLE_PROFILE_DRIVER_KEY, bundleProfileHome_);
        ctx.put(BUNDLE_CATEGORY_DRIVER_KEY, bundleCategoryHome_);
        ctx.put(SUBSCRIBER_BUCKET_DRIVER_KEY, subscriberBucketHome_);

        //Business services
        ctx.put(CRMBundleCategory.class, getBundleCategoryService());

        ctx.put(CRMBundleProfile.class, getBundleProfileService());

        ctx.put(CRMSubscriberBucketProfile.class, getSubscriberBucketService());
    }

    /**
     * @{inheritDoc}
     */
    public void install(Context ctx)
    {
        initHomes(ctx);
        initServices(ctx);
        injectBundleManagerHomes(ctx);
        
        // Make sure that the CORBA clients are invalidated before installing new ones.
        uninstall(ctx);
        installCorbaClients(ctx);
    }

    /**
     * @{inheritDoc}
     */
    public void uninstall(Context ctx)
    {
    }
    
    public void installCorbaClients(Context ctx)
    {        
        try 
        {
            BundleProfileProvisionClient bundleProfileCorbaClient = new BMBundleProfileCorbaClient(ctx);
            ctx.put(BundleProfileProvisionClient.class, bundleProfileCorbaClient);

        }
        catch (Throwable e)
        {
            Install.failAndContinue(ctx, "BMBundleProfileCorbaClient", e);
        }

        try
        {
            ctx.put(BundleCategoryProvisionClient.class, new BMBundleCategoryCorbaClient(ctx));

        }
        catch (Throwable e)
        {
            Install.failAndContinue(ctx, "BMBundleCategoryCorbaClient", e);
        }

        try
        {
            ctx.put(BMSubscriberBucketCorbaClient.class, new BMSubscriberBucketCorbaClient(ctx));
//            SystemStatusSupport.registerExternalService(ctx, BMSubscriberBucketCorbaClient.class);
        }
        catch (Throwable e)
        {
            Install.failAndContinue(ctx, "BMSubscriberBucketCorbaClient", e);
        }

    }

    /* (non-Javadoc)
     * @see com.redknee.app.crm.client.ExternalService#getServiceDescription()
     */
    public String getDescription()
    {
        return SERVICE_DESCRIPTION;
    }

    /* (non-Javadoc)
     * @see com.redknee.app.crm.client.ExternalService#getServiceName()
     */
    public String getName()
    {
        return SERVICE_NAME;
    }


    /* (non-Javadoc)
      * @see com.redknee.app.crm.client.ExternalService#isServiceAlive()
      */
    public boolean isAlive()
    {
        return true;
    }

    protected Home bundleProfileHome_;
    protected Home bundleCategoryHome_;
    protected Home subscriberBucketHome_;
    protected Home subscriberBucketRetrievalHome_;
    protected Home balanceApplicationHome_;
    protected CRMBundleCategory categoryService_;
    protected CRMBundleProfile bundleService_;
    protected CRMSubscriberBucketProfile bucketService_;

    protected Context context_ = new ContextSupport();

    @Override
    public ConnectionStatus[] getConnectionStatus()
    {
        return null;
    }

    @Override
    public String getServiceStatus()
    {
        return null;
    }
}
