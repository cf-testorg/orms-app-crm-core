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
package com.redknee.app.crm.xhome.adapter;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.FacetMgr;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xhome.context.ContextLocator;
import com.redknee.framework.xhome.context.ContextSupport;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.exception.XBeansCopyLoggingExceptionListener;


/**
 * This class adapts a bean class to/from another bean class.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class BeanAdapter<UNADAPT_TYPE extends AbstractBean, ADAPT_TYPE extends AbstractBean> implements Adapter
{
    private static final long serialVersionUID = 1L;
    
    public BeanAdapter(Class<UNADAPT_TYPE> unAdaptClass, Class<ADAPT_TYPE> adaptClass)
    {
        unAdaptClass_ = unAdaptClass;
        adaptClass_ = adaptClass;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object adapt(final Context ctx, final Object bean) throws HomeException
    {
        if (ctx != null)
        {
            return adapt(ctx, bean, getAdaptClass());
        }
        else
        {
            return adapt(ContextLocator.locate(), bean, getAdaptClass());
        }
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object unAdapt(Context ctx, Object bean) throws HomeException
    {
        if (ctx != null)
        {
            return adapt(ctx, bean, getUnAdaptClass());
        }
        else
        {
            return adapt(ContextLocator.locate(), bean, getAdaptClass());
        }
    }


    private Object adapt(Context context, final Object bean, final Class destinationClass)
    {
        if (bean == null)
        {
            return null;
        }
        
        if (!context.has(FacetMgr.class))
        {
            Object coreCtx = context.get("core");
            if (coreCtx instanceof Context)
            {
                context = (Context) coreCtx;
            }
        }
        
        final Context subCtx = context.createSubContext();
        Object result = null;
        if (destinationClass.isAssignableFrom(bean.getClass()))
        {
            result = bean;
        }
        else
        {
            // Bean is not an instance of the base class.  Use XBeans.copy() to make it into one.
            result = newInstance(subCtx, destinationClass);

            if (result != null)
            {
                result = XBeans.copy(subCtx, bean, result, new XBeansCopyLoggingExceptionListener(subCtx, bean, result));
                
                if (bean instanceof ContextAware && result instanceof ContextAware)
                {
                    try
                    {
                        ((ContextAware)result).setContext(((ContextAware)bean).getContext());
                    }
                    catch (Throwable t)
                    {
                        new InfoLogMsg(this, "Unable to set context of " + destinationClass.getName() + " instance: " + t.getMessage(), null).log(subCtx);
                    }
                }
            }
            else
            {
                new MinorLogMsg(this, "Unable to adapt from " + bean.getClass().getName() + " to " + destinationClass.getName() + ".  No valid instance of the target class was created.", null).log(subCtx);
            }
        }
        
        return result;
    }

    protected Object newInstance(final Context ctx, final Class clazz)
    {
        Object result = null;
        try
        {
            // We don't want to use a bean factory for this.  We will be overwriting
            // all properties anyways.  Additionally, if we were to use a bean factory
            // for doing this with core beans and data beans, a cycle could result in
            // the instantiation process where one bean factory inadvertantly calls the
            // other
            final Context sCtx = ctx.createSubContext();
            sCtx.put("BeanFactory::" + clazz.getName(), null);
            result = XBeans.instantiate(clazz, sCtx);
        }
        catch (Exception e)
        {
            new MajorLogMsg(this, "DEV ERROR: Misuse of " + BeanAdapter.class.getSimpleName() + " class.  Classes must be instantiable.", null).log(ctx);
        }
        return result;
    }

	public Class<UNADAPT_TYPE> getUnAdaptClass()
    {
        return unAdaptClass_;
    }
    
	public Class<ADAPT_TYPE> getAdaptClass()
    {
        return adaptClass_;
    }
    
    private final Class<UNADAPT_TYPE> unAdaptClass_;
    private final Class<ADAPT_TYPE> adaptClass_;
}
