/*
 * Created on 2005-1-12
 */
package com.redknee.app.crm.support;

import com.redknee.framework.xhome.context.Context;


/**
 * This class should implement static versions of all {@link com.redknee.app.crm.support.MultiDbSupport} methods.
 *
 * The only change that should be made to method signatures in the interface is
 * to add the context to facilitate implementation class retrieval. 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class PackageSupportHelper extends SupportHelper
{
    private PackageSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static PackageSupport get()
    {
        return get(PackageSupport.class, DefaultPackageSupport.instance());
    }
    
    public static PackageSupport get(Context ctx)
    {
        PackageSupport instance = get(ctx, PackageSupport.class, DefaultPackageSupport.instance());
        return instance;
    }
    
    public static PackageSupport set(Context ctx, PackageSupport instance)
    {
        return register(ctx, PackageSupport.class, instance);
    }
}
