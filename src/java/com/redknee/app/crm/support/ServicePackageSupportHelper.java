/*
 * Created on 2005-1-12
 */
package com.redknee.app.crm.support;

import com.redknee.framework.xhome.context.Context;


/**
 * This class should implement static versions of all {@link com.redknee.app.crm.support.ServicePackageSupport} methods.
 *
 * The only change that should be made to method signatures in the interface is
 * to add the context to facilitate implementation class retrieval. 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.5
 */
public class ServicePackageSupportHelper extends SupportHelper
{
    private ServicePackageSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    @Deprecated
    public static ServicePackageSupport get()
    {
        return get(ServicePackageSupport.class, DefaultServicePackageSupport.instance());
    }
    
    public static ServicePackageSupport get(Context ctx)
    {
        ServicePackageSupport instance = get(ctx, ServicePackageSupport.class, DefaultServicePackageSupport.instance());
        return instance;
    }
    
    public static ServicePackageSupport set(Context ctx, ServicePackageSupport instance)
    {
        return register(ctx, ServicePackageSupport.class, instance);
    }
}
