package com.redknee.app.crm.support;

import com.redknee.framework.xhome.context.Context;


/**
 * This class should implement static versions of all {@link com.redknee.app.crm.support.StorageSupport} methods.
 *
 * The only change that should be made to method signatures in the interface is
 * to add the context to facilitate implementation class retrieval. 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class StorageSupportHelper extends SupportHelper
{
    private StorageSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static StorageSupport get()
    {
        return get(StorageSupport.class, DefaultStorageSupport.instance());
    }
    
    public static StorageSupport get(Context ctx)
    {
        StorageSupport instance = get(ctx, StorageSupport.class, DefaultStorageSupport.instance());
        return instance;
    }
    
    public static StorageSupport set(Context ctx, StorageSupport instance)
    {
        return register(ctx, StorageSupport.class, instance);
    }
}
