package com.redknee.app.crm.factory;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.LookupContextFactory;


public class ContextRedirectingContextFactory extends LookupContextFactory
{
    private static final Object CYCLE_GUARD_CTX_KEY_PREFIX = "CONTEXT_REDIRECT_TRACKER_KEY_";
    
    public ContextRedirectingContextFactory(Object key)
    {
        super(key);
        cycleGuard_ = CYCLE_GUARD_CTX_KEY_PREFIX + String.valueOf(key);
    }

    @Override
    public Object create(Context ctx)
    {
        if (!ctx.has(cycleGuard_))
        {
            Context sCtx = ctx.createSubContext();
            sCtx.put(cycleGuard_, key_);
            return super.create(sCtx);
        }
        return null;
    }
    
    private String cycleGuard_;
}
