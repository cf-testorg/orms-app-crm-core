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
public class MultiDbSupportHelper extends SupportHelper
{
    private MultiDbSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static MultiDbSupport get()
    {
        return get(MultiDbSupport.class, DefaultMultiDbSupport.instance());
    }
    
    public static MultiDbSupport get(Context ctx)
    {
        MultiDbSupport instance = get(ctx, MultiDbSupport.class, DefaultMultiDbSupport.instance());
        return instance;
    }
    
    public static MultiDbSupport set(Context ctx, MultiDbSupport instance)
    {
        return register(ctx, MultiDbSupport.class, instance);
    }
}
