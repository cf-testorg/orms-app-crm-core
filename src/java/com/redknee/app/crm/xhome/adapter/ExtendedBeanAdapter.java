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
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.MajorLogMsg;


/**
 * This class adapts a bean class to/from an extended version of that bean class.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class ExtendedBeanAdapter<BASE extends AbstractBean, CONCRETE extends BASE> extends BeanAdapter
{
    private static final long serialVersionUID = 1L;
    
    
    public ExtendedBeanAdapter(Class<BASE> baseClass, Class<CONCRETE> concreteClass)
    {
        super(baseClass, concreteClass);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object adapt(Context ctx, Object bean) throws HomeException
    {
        return super.adapt(ctx,bean);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object unAdapt(Context ctx, Object bean) throws HomeException
    {
        BASE result = null;
        if (!getBaseClass().isAssignableFrom(bean.getClass()))
        {
            new MajorLogMsg(this, "DEV ERROR: Misuse of " + BeanAdapter.class.getSimpleName() + " class.  Bean class must be assignable to " + getBaseClass().getName(), null).log(ctx);
            return bean;
        }
        else
        {
            result = (BASE) bean;
        }
        return result;
    }

    public Class<BASE> getBaseClass()
    {
        return getUnAdaptClass();
    }
    
    public Class<CONCRETE> getConcreteClass()
    {
        return getAdaptClass();
    }
}
