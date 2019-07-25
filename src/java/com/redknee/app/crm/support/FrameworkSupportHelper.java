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

/**
 * This class should implement static versions of all {@link com.redknee.app.crm.support.FrameworkSupport} methods.
 * 
 * The only change that should be made to method signatures in the interface is
 * to add the context to facilitate implementation class retrieval. 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public final class FrameworkSupportHelper extends SupportHelper
{
    private FrameworkSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static FrameworkSupport get()
    {
        return get(FrameworkSupport.class, DefaultFrameworkSupport.instance());
    }
    
    public static FrameworkSupport get(Context ctx)
    {
        FrameworkSupport instance = get(ctx, FrameworkSupport.class, DefaultFrameworkSupport.instance());
        return instance;
    }
    
    public static FrameworkSupport set(Context ctx, FrameworkSupport instance)
    {
        return register(ctx, FrameworkSupport.class, instance);
    }
}
