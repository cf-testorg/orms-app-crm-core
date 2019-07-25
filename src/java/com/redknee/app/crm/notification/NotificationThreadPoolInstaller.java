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
package com.redknee.app.crm.notification;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.pipe.ThreadPool;


/**
 * This class exposes a method to support install/uninstall of
 * notification thread pools from configuration
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class NotificationThreadPoolInstaller
{
    private static NotificationThreadPoolInstaller instance_ = null;
    public static NotificationThreadPoolInstaller instance()
    {
        if (instance_ == null)
        {
            instance_ = new NotificationThreadPoolInstaller();
        }
        return instance_;
    }
    
    protected NotificationThreadPoolInstaller()
    {
    }
    
    public void execute(Context ctx, boolean uninstall, com.redknee.app.crm.bean.core.NotificationThreadPool config)
    {
        ThreadPool pool = config.getThreadPool(ctx);
        if (pool != null)
        {
            if (uninstall)
            {
                config.terminateThreadPool(ctx);
            }
            else
            {
                if (pool.getThreads() != config.getNumThreads())
                {
                    pool.setThreads(config.getNumThreads());
                }
                
                if (pool.getMaxQueueSize() != config.getMaxQueueSize())
                {
                    pool.setMaxQueueSize(config.getMaxQueueSize());
                }
            }
        }
        else if (!uninstall)
        {
            config.initializeThreadPool(ctx);
        }
    }
}
