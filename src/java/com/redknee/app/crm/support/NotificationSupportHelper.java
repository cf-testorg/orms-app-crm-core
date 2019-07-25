/*
 * Created on 2005-1-12
 */
package com.redknee.app.crm.support;

import com.redknee.framework.xhome.context.Context;


/**
 * This class should implement static versions of all {@link com.redknee.app.crm.support.NotificationSupportHelper} methods.
 *
 * The only change that should be made to method signatures in the interface is
 * to add the context to facilitate implementation class retrieval. 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class NotificationSupportHelper extends SupportHelper
{
    private NotificationSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    @Deprecated
    public static NotificationSupport get()
    {
        return get(NotificationSupport.class, DefaultNotificationSupport.instance());
    }
    
    public static NotificationSupport get(Context ctx)
    {
        NotificationSupport instance = get(ctx, NotificationSupport.class, DefaultNotificationSupport.instance());
        return instance;
    }
    
    public static NotificationSupport set(Context ctx, NotificationSupport instance)
    {
        return register(ctx, NotificationSupport.class, instance);
    }
}
