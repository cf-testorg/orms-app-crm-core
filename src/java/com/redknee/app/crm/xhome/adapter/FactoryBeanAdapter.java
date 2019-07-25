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
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextFactory;
import com.redknee.framework.xlog.log.InfoLogMsg;


/**
 * A bean adapter that uses a factory to create a new instance of the adapt/unadapt bean.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.6 
 */
public class FactoryBeanAdapter<UNADAPT_TYPE extends AbstractBean, ADAPT_TYPE extends AbstractBean> extends BeanAdapter<UNADAPT_TYPE, ADAPT_TYPE>
{
    public static final String CURRENT_CLASS_KEY = "FactoryBeanAdapter.CurrentClass";
    
    public FactoryBeanAdapter(Class<UNADAPT_TYPE> unAdaptClass, Class<ADAPT_TYPE> adaptClass, ContextFactory factory)
    {
        this(unAdaptClass, adaptClass, factory, factory);
    }
    
    public FactoryBeanAdapter(Class<UNADAPT_TYPE> unAdaptClass, Class<ADAPT_TYPE> adaptClass, ContextFactory unAdaptContextFactory, ContextFactory adaptContextFactory)
    {
        super(unAdaptClass, adaptClass);
        unAdaptContextFactory_ = unAdaptContextFactory;
        adaptContextFactory_ = adaptContextFactory;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected Object newInstance(Context ctx, Class clazz)
    {
        boolean isSet = false;
        Object result = null;
        if (this.getAdaptClass() == clazz)
        {
            result = getAdaptContextFactory().create(ctx.createSubContext().put(CURRENT_CLASS_KEY, clazz));
            isSet = true;
        }
        else if (this.getUnAdaptClass() == clazz)
        {
            result = getUnAdaptContextFactory().create(ctx.createSubContext().put(CURRENT_CLASS_KEY, clazz));
            isSet = true;
        }
        if (!isSet
                || (result != null && !clazz.isInstance(result)))
        {
            new InfoLogMsg(this, 
                    "No compatibile instance of class " + clazz.getClass().getSimpleName()
                    + " could be generated.  Retrieving new instance...", null).log(ctx);
            result = super.newInstance(ctx, clazz);
        }
        return result;
    }
    
    public ContextFactory getUnAdaptContextFactory()
    {
        return unAdaptContextFactory_;
    }

    public void setUnAdaptContextFactory(ContextFactory unAdaptContextFactory)
    {
        this.unAdaptContextFactory_ = unAdaptContextFactory;
    }

    public ContextFactory getAdaptContextFactory()
    {
        return adaptContextFactory_;
    }

    public void setAdaptContextFactory(ContextFactory adaptContextFactory)
    {
        this.adaptContextFactory_ = adaptContextFactory;
    }


    protected ContextFactory unAdaptContextFactory_;
    protected ContextFactory adaptContextFactory_;
}
