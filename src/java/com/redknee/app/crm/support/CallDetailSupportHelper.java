package com.redknee.app.crm.support;

import com.redknee.framework.xhome.context.Context;


public class CallDetailSupportHelper extends SupportHelper
{
    private CallDetailSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static CallDetailSupport get()
    {
        return get(CallDetailSupport.class, DefaultCallDetailSupport.instance());
    }
    
    public static CallDetailSupport get(Context ctx)
    {
        CallDetailSupport instance = get(ctx, CallDetailSupport.class, DefaultCallDetailSupport.instance());
        return instance;
    }
    
    public static CallDetailSupport set(Context ctx, CallDetailSupport instance)
    {
        return register(ctx, CallDetailSupport.class, instance);
    }
}
