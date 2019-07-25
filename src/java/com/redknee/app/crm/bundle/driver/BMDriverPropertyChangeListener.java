/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily 
 * available. Additionally, source code is, by its very nature, confidential 
 * information and inextricably contains trade secrets and other information 
 * proprietary, valuable and sensitive to Redknee, no unauthorised use, 
 * disclosure, manipulation or otherwise is permitted, and may only be used 
 * in accordance with the terms of the licence agreement entered into with 
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright Redknee Inc. and its subsidiaries. All Rights Reserved. 
 */
package com.redknee.app.crm.bundle.driver;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.redknee.framework.core.home.PMHome;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAwareSupport;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.RMIHomeServer;
import com.redknee.framework.xhome.home.ReadOnlyHome;
import com.redknee.framework.xlog.log.MajorLogMsg;

import com.redknee.app.crm.bundle.BundleCategoryHome;
import com.redknee.app.crm.bundle.SubcriberBucketModelBundleManager;
import com.redknee.app.crm.bundle.SubscriberBucketHome;
import com.redknee.app.crm.support.BundleSupportHelper;
import com.redknee.app.crm.support.DeploymentTypeSupportHelper;

/**
 * Property change listener to reload the BM Driver on config change.
 *
 * @author Aaron Gourley
 */
public class BMDriverPropertyChangeListener extends ContextAwareSupport implements PropertyChangeListener
{
    public BMDriverPropertyChangeListener(Context ctx)
    {
        setContext(ctx);
    }

    /**
     * @{inheritDoc}
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
        final Context ctx = getContext();
        Object oldValue = evt.getOldValue();
        if( oldValue instanceof BMDriver )
        {
            ((BMDriver)oldValue).uninstall(ctx);
        }
        Object newValue = evt.getNewValue();
        if( newValue instanceof BMDriver )
        {
            BMDriver driver = (BMDriver)newValue;
            driver.setContext(ctx);
            driver.install(ctx);
            installRMIHomeDecorators(ctx);
        }
    }

    /**
     * Install all the RMI decorator homes for services to another applications
     * @param ctx
     * @param driver
     */
    private void installRMIHomeDecorators(Context ctx)
    {
        SubcriberBucketModelBundleManager generator =
            BundleSupportHelper.get(ctx).getSubscriberBucketModel(ctx);

        // [ltang]
        if (DeploymentTypeSupportHelper.get(ctx).isBas(ctx) || DeploymentTypeSupportHelper.get(ctx).isSingle(ctx))
        {
            registerRMIHomeServer(ctx, BundleCategoryHome.class, BMDriver.BUNDLE_CATEGORY_DRIVER_KEY);
        }

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
    private void registerRMIHomeServer(Context ctx, Class homeClass, String key)
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

}
