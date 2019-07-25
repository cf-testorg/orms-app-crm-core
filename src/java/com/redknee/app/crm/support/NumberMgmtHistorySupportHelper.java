/*
 * Created on 2005-1-12
 */
package com.redknee.app.crm.support;

import com.redknee.framework.xhome.context.Context;


/**
 * This class should implement static versions of all {@link com.redknee.app.crm.support.NumberMgmtHistorySupport} methods.
 *
 * The only change that should be made to method signatures in the interface is
 * to add the context to facilitate implementation class retrieval. 
 * 
 * @author kumaran.sivasubramaniam@redknee.com
 * @since 8.3
 */
public class NumberMgmtHistorySupportHelper extends SupportHelper
{
    private NumberMgmtHistorySupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static NumberMgmtHistorySupport get()
    {
        return get(NumberMgmtHistorySupport.class, DefaultNumberMgmtHistorySupport.instance());
    }
    
    public static NumberMgmtHistorySupport get(Context ctx)
    {
        NumberMgmtHistorySupport instance = get(ctx, NumberMgmtHistorySupport.class, DefaultNumberMgmtHistorySupport.instance());
        return instance;
    }
    
    public static NumberMgmtHistorySupport set(Context ctx, NumberMgmtHistorySupport instance)
    {
        return register(ctx, NumberMgmtHistorySupport.class, instance);
    }
}
