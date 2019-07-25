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
 * This class should implement static versions of all {@link com.redknee.app.crm.support.BeanLoaderSupport} methods.
 * 
 * The only change that should be made to method signatures in the interface is
 * to add the context to facilitate implementation class retrieval. 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public final class BeanLoaderSupportHelper extends SupportHelper
{
    private BeanLoaderSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static BeanLoaderSupport get()
    {
        return get(BeanLoaderSupport.class, DefaultBeanLoaderSupport.instance());
    }
    
    public static BeanLoaderSupport get(Context ctx)
    {
        BeanLoaderSupport instance = get(ctx, BeanLoaderSupport.class, DefaultBeanLoaderSupport.instance());
        return instance;
    }
    
    public static BeanLoaderSupport set(Context ctx, BeanLoaderSupport instance)
    {
        return register(ctx, BeanLoaderSupport.class, instance);
    }
}