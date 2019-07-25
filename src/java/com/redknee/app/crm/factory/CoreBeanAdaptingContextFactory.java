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

import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;


/**
 * Adapts the bean returned by the delegate to a core bean.
 *
 * @author aaron.gourley@redknee.com
 */
public class CoreBeanAdaptingContextFactory<BASE extends AbstractBean, CONCRETE extends BASE>
    extends ContextFactoryProxy
{
    private Class<BASE> baseType_;
    private Class<CONCRETE> concreteType_;
    
    public CoreBeanAdaptingContextFactory(Class<BASE> baseType, Class<CONCRETE> concreteType, final ContextFactory delegate)
    {
        super(delegate);
        baseType_ = baseType;
        concreteType_ = concreteType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(final Context ctx)
    {
        final BASE type = (BASE) super.create(ctx);
        
        try
        {
            // Adapt the model bean to a core bean instance.
            Adapter beanAdapter = new ExtendedBeanAdapter<BASE, CONCRETE>(
                    baseType_, 
                    concreteType_);
            
            return beanAdapter.adapt(ctx, type);
        }
        catch (HomeException e)
        {
            new MajorLogMsg(this, "Error adapting from base type '" + baseType_.getName()
                    + "' to concrete type '" + concreteType_.getName() + "'.  Returning base instance.  Message: " + e.getMessage(), e).log(ctx);
        }

        return type;
    }

} // class
