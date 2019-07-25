package com.redknee.app.crm.support;

import com.redknee.framework.xhome.context.Context;

public class BundleSupportHelper extends SupportHelper
{
    private BundleSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    @Deprecated
    public static BundleSupport get()
    {
        return get(BundleSupport.class, DefaultBundleSupport.instance());
    }
    
    public static BundleSupport get(Context ctx)
    {
        BundleSupport instance = get(ctx, BundleSupport.class, DefaultBundleSupport.instance());
        return instance;
    }
    
    public static BundleSupport set(Context ctx, BundleSupport instance)
    {
        return register(ctx, BundleSupport.class, instance);
    }
}
