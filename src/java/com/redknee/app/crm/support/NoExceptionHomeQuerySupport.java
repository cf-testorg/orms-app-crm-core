package com.redknee.app.crm.support;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.MinorLogMsg;

/**
 * in many cases that we want to return null instead of 
 * thrown exception, especially when the return is an optional 
 * value.  ideally, we can use AOP to replace such static style coding. 
 *  
 * @author lxia
 *
 */

public class NoExceptionHomeQuerySupport
{

    
    /** 
     *  
     * @param <T>
     * @param ctx
     * @param beanType
     * @param where
     * @return
     */
    static public <T extends AbstractBean> T findBean(Context ctx, Class<T> beanType, Object where)
    {
       try
       {
           return HomeSupportHelper.get(ctx).findBean(ctx,beanType, where); 
       } catch(Throwable t)
       {
           new MinorLogMsg( NoExceptionHomeQuerySupport.class, " Home operation failure", t).log(ctx); 
       }
       
       return null; 
    }
}
