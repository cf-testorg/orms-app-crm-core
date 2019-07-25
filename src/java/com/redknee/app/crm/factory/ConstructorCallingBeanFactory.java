/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s).  A complete listing of authors of this work is readily
 * available.  Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee.  No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the license agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.factory;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextFactory;
import com.redknee.framework.xhome.context.ContextSupport;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class ConstructorCallingBeanFactory<TYPE extends AbstractBean> implements ContextFactory
{
    final private Class<TYPE> type_;

    /**
     * @param coreClass
     */
    public ConstructorCallingBeanFactory(Class<TYPE> type)
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
            Context sCtx = ctx.createSubContext();
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
