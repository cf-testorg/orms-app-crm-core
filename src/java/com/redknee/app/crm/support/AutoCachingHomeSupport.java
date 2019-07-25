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
package com.redknee.app.crm.support;


import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.home.AbstractCache;
import com.redknee.framework.xhome.home.AbstractClassAwareHome;
import com.redknee.framework.xhome.home.CacheConfigHome;
import com.redknee.framework.xhome.home.CachingHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.home.LRUCachingHome;
import com.redknee.framework.xhome.home.NullHome;
import com.redknee.framework.xhome.home.TransientHome;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.xhome.home.TotalCachingHome;

/**
 * This version of HomeSupport manipulates the caller's context by prepending
 * LRUCachingHomes to homes that may benefit from them (e.g. XDB, RMI, etc).
 * 
 * It MUST only be used by callers who are passing some sub-context of the
 * application context to avoid thrashing the app context! 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.5
 */
public class AutoCachingHomeSupport extends DefaultHomeSupport
{
    private static final Object ALREADY_PROCESSED_CTX_KEY_PREFIX = "AutoCached-";
    
    public static int DEFAULT_TOTAL_CACHE_THRESHOLD = 100;
    
    private int totalCachingThreshold_ = DEFAULT_TOTAL_CACHE_THRESHOLD;
    
    public AutoCachingHomeSupport()
    {
        super();
    }

    public AutoCachingHomeSupport(int totalCachingThreshold)
    {
        super();
        totalCachingThreshold_ = totalCachingThreshold;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Home getHome(Context ctx, Object contextKey) throws HomeException
    {
        Home home = super.getHome(ctx, contextKey);

        String processedCtxKey = ALREADY_PROCESSED_CTX_KEY_PREFIX + String.valueOf(contextKey);
        if (ctx.getBoolean(processedCtxKey, false))
        {
            return home;
        }
        
        if (home instanceof HomeProxy)
        {
            HomeProxy proxy = (HomeProxy) home;
            if (proxy.hasDecorator(AbstractCache.class)
                    || proxy.hasDecorator(TransientHome.class)
                    || proxy.hasDecorator(TotalCachingHome.class))
            {
                return home;
            }
        }
        
        boolean isHomeWrapped = false;

        String cacheConfigKey = "AutoCache-";
        if (contextKey instanceof Class)
        {
            cacheConfigKey += ((Class) contextKey).getSimpleName();
        }
        else
        {
            cacheConfigKey += String.valueOf(contextKey);
        }
        
        // We don't want these auto-caches to have cache config records created for them.  They pollute the journal.
        Context nullCacheConfigCtx = ctx.createSubContext();
        nullCacheConfigCtx.put(CacheConfigHome.class, NullHome.instance());
        
        int totalCachingThreshold = Math.max(totalCachingThreshold_, 1);
        if (super.hasBeans(ctx, home, totalCachingThreshold, True.instance()))
        {
            home = new LRUCachingHome(nullCacheConfigCtx, cacheConfigKey, totalCachingThreshold, true, null, home);
            isHomeWrapped = true;
        }
        else
        {
            Home transientHome = null;
            Class transientHomeClass = null;
            try
            {
                Object beanType = home.cmd(AbstractClassAwareHome.CLASS_CMD);
                if (beanType instanceof Class)
                {
                    transientHome = (Home) XBeans.getInstanceOf(ctx, (Class) beanType, TransientHome.class);
                }
            }
            catch (HomeException e)
            {
            }
            if (transientHome != null)
            {
                home = new CachingHome(nullCacheConfigCtx, cacheConfigKey, transientHome, home);
                isHomeWrapped = true;
            }
        }
        
        if (isHomeWrapped)
        {
            ctx.put(contextKey, home);
            
            if (LogSupport.isDebugEnabled(ctx))
            {
                String ctxName = ctx.getName();
                String msgSuffix = "";
                if (ctxName != null && ctxName.trim().length() > 0)
                {
                    msgSuffix = " in context " + ctxName.trim();
                }
                new DebugLogMsg(this, "Home with context key " + contextKey + " wrapped with " + home.getClass().getName() + msgSuffix, null).log(ctx);
            }
        }

        ctx.put(processedCtxKey, true);
        
        return home;
    }
}
