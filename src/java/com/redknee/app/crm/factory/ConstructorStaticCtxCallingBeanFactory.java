package com.redknee.app.crm.factory;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextFactory;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;

public class ConstructorStaticCtxCallingBeanFactory<TYPE extends AbstractBean> implements ContextFactory
{

    final private Class<TYPE> type_;

    /**
     * @param coreClass
     */
    public ConstructorStaticCtxCallingBeanFactory(Class<TYPE> type)
    {
        type_ = type;
    }

    /**
     * {@inheritDoc}
     */
    public Object create(Context ctx)
    {
        TYPE bean = null;
        if (type_ != null)
        {
            // Prevent recursive bean factory invocation
        	Context appctx = (Context)ctx.get("app"); 
            Context sCtx = appctx.createSubContext();
            sCtx.put("BeanFactory::" + type_.getName(), null);
            try
            {
                bean = (TYPE) XBeans.instantiate(type_, sCtx);
            }
            catch (Exception e)
            {
                if (LogSupport.isDebugEnabled(sCtx))
                {
                    new DebugLogMsg(this, InstantiationException.class.getSimpleName() + " occurred in " + ConstructorCallingBeanFactory.class.getSimpleName() + ".create(): " + e.getMessage(), e).log(ctx);
                }
            }
        }
        return bean;
    }


}
