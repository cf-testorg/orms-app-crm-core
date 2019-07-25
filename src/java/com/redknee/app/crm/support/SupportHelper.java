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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextLocator;
import com.redknee.framework.xhome.context.ContextSupport;


/**
 * Generic support class installation/retrieval tool.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.4
 */
public class SupportHelper
{
    protected SupportHelper()
    {
    }

    /**
     * @deprecated Use contextualized version of method
     */
    @Deprecated
    public static <T extends Support> T get(Class<T> cls)
    {
        return get(ContextLocator.locate(), cls);
    }

    /**
     * @deprecated Use contextualized version of method
     */
    @Deprecated
    public static <T extends Support> T get(Class<T> cls, T defaultValue)
    {
        return get(ContextLocator.locate(), cls, defaultValue);
    }
    
    public static <T extends Support> T get(Context ctx, Class<T> cls)
    {
        return get(ctx, cls, null);
    }
    
    public static <T extends Support> T get(Context ctx, Class<T> cls, T defaultValue)
    {
        T instance = null;
        if (ctx != null)
        {
            instance = (T) ctx.get(cls);
        }
        if (instance == null)
        {
            instance = defaultValue;
        }
        return instance;
    }
    
    public static <T extends Support> T register(Context ctx, Class<T> cls, T instance)
    {
        ctx.put(cls, instance == null ? null : instance);
        return instance;
    }
}
