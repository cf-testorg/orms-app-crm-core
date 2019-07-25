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
package com.redknee.app.crm.home;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NotifyingHome;

import com.redknee.app.crm.bean.core.NotificationThreadPool;
import com.redknee.app.crm.notification.NotificationThreadPoolInstallationHomeChangeListener;
import com.redknee.app.crm.notification.NotificationThreadPoolInstallationVisitor;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class NotificationThreadPoolHomePipelineFactory implements PipelineFactory
{
    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = CoreSupport.bindHome(ctx, NotificationThreadPool.class);

        home = new AdapterHome(ctx, 
                new ExtendedBeanAdapter<com.redknee.app.crm.notification.NotificationThreadPool, com.redknee.app.crm.bean.core.NotificationThreadPool>(
                        com.redknee.app.crm.notification.NotificationThreadPool.class, 
                        com.redknee.app.crm.bean.core.NotificationThreadPool.class), 
                        home);
        
        home = new NotifyingHome(home);

        // Add listener to install/uninstall/update thread pools when config changes occur 
        ((NotifyingHome)home).addHomeChangeListener(new NotificationThreadPoolInstallationHomeChangeListener(ctx));
        
        // Install the existing notification thread pools on startup
        home.forEach(ctx, new NotificationThreadPoolInstallationVisitor());
        
        return home;
    }
}
