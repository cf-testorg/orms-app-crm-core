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
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextFactory;
import com.redknee.framework.xhome.context.ContextFactoryProxy;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.MajorLogMsg;

import com.redknee.app.crm.xhome.adapter.BeanAdapter;


/**
 * Adapts the bean returned by the delegate to another similar bean.
 *
 * @author aaron.gourley@redknee.com
 */
public class BeanAdaptingContextFactory<TYPE1 extends AbstractBean, TYPE2 extends AbstractBean>
    extends ContextFactoryProxy
{
    private Class<TYPE1> type1_;
    private Class<TYPE2> type2_;

    public BeanAdaptingContextFactory(Class<TYPE1> type1, Class<TYPE2> type2, final ContextFactory delegate)
    {
        super(delegate);
        type1_ = type1;
        type2_ = type2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(final Context ctx)
    {
        final TYPE1 type = (TYPE1) super.create(ctx);
        
        try
        {
            // Adapt the model bean to a core bean instance.
            Adapter beanAdapter = new BeanAdapter<TYPE1, TYPE2>(
                    type1_, 
                    type2_);
            
            return beanAdapter.adapt(ctx, type);
        }
        catch (HomeException e)
        {
            new MajorLogMsg(this, "Error adapting from type '" + type1_.getName()
                    + "' to type '" + type2_.getName() + "'.  Returning base instance.  Message: " + e.getMessage(), e).log(ctx);
        }

        return type;
    }

} // class
