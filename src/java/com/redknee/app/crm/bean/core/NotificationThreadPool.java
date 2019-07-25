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
package com.redknee.app.crm.bean.core;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.pipe.ThreadPool;
import com.redknee.framework.xlog.log.MajorLogMsg;

import com.redknee.app.crm.notification.ConcurrentNotificationAgent;
import com.redknee.app.crm.support.HomeSupportHelper;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class NotificationThreadPool extends com.redknee.app.crm.notification.NotificationThreadPool
{
    public static ThreadPool getThreadPool(Context ctx, String poolName)
    {
        NotificationThreadPool poolConf = null;
        
        try
        {
            poolConf = HomeSupportHelper.get(ctx).findBean(ctx, NotificationThreadPool.class, poolName);
        }
        catch (HomeException e)
        {
            new MajorLogMsg(NotificationThreadPool.class, "Error retrieving notification thread pool configuration '" + poolName + "'", e).log(ctx);
        }
        
        if (poolConf != null)
        {
            return poolConf.getThreadPool(ctx);
        }
        
        return null;
    }
    
    public ThreadPool getThreadPool(Context ctx)
    {
        ThreadPool pool = (ThreadPool) ctx.get(getThreadPoolContextKey());
        if (pool != null)
        {
            if (pool.getThreads() != getNumThreads())
            {
                pool.setThreads(getNumThreads());
            }
            
            if (pool.getMaxQueueSize() != getMaxQueueSize())
            {
                pool.setMaxQueueSize(getMaxQueueSize());
            }
        }
        return pool;
    }
    
    public synchronized void initializeThreadPool(Context ctx)
    {
        ThreadPool pool = getThreadPool(ctx);
        if (pool == null)
        {
            pool = new ThreadPool(
                    getPoolName(), 
                    getMaxQueueSize(), getNumThreads(), 
                    ConcurrentNotificationAgent.instance());
            ctx.put(getThreadPoolContextKey(), pool);
        }
    }

    public synchronized void terminateThreadPool(Context ctx)
    {
        ThreadPool pool = getThreadPool(ctx);
        if (pool != null)
        {
            pool.shutdown();
            while (ctx != null && ctx.has(getThreadPoolContextKey()))
            {
                ctx.put(getThreadPoolContextKey(), null);
                Object parent = ctx.get("..");
                if (parent instanceof Context)
                {
                    ctx = (Context) parent;
                }
                else
                {
                    break;
                }
            }
        }
    }

    private String getThreadPoolContextKey()
    {
        return NotificationThreadPool.class.getSimpleName() + "_" + getPoolName();
    }

}
