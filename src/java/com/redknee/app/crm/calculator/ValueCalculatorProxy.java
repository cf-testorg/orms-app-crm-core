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
package com.redknee.app.crm.calculator;

import java.util.ArrayList;
import java.util.Collection;

import com.redknee.framework.xhome.context.Context;


/**
 * Normal proxy implementation with helper methods.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class ValueCalculatorProxy extends AbstractValueCalculatorProxy
{
    public ValueCalculatorProxy()
    {
    }

    public ValueCalculatorProxy(ValueCalculator delegate)
    {
        setDelegate(delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Object> getDependentContextKeys(Context ctx)
    {
        if (getDelegate() instanceof AbstractValueCalculator)
        {
            return ((AbstractValueCalculator)getDelegate()).getDependentContextKeys(ctx);
        }
        return new ArrayList();
    }


    /**
     * {@inheritDoc}
     */
    public Object getValueAdvanced(Context ctx)
    { 		
    	if(getDelegate() == null) return null;
        return getDelegate().getValue(ctx);
    }

    /** Inserts a ValueCalculatorProxy before the current delegate. **/
    public void addProxy(ValueCalculatorProxy proxy)
    {
        proxy.setDelegate(getDelegate());

        setDelegate(proxy);  
    }


    /** Inserts a ValueCalculatorProxy before the last non ContextAgentProxy. **/
    public void appendProxy(ValueCalculatorProxy proxy)
    {
    	if(getDelegate() == null) return ;
        if ( getDelegate() instanceof ValueCalculatorProxy )
        {
            ValueCalculatorProxy delegate = (ValueCalculatorProxy) getDelegate();

            delegate.appendProxy(proxy);
        }
        else
        {
            addProxy(proxy);  
        }
    }


    /** Find the first instance of cls in the Decorator chain. **/ 
    public ValueCalculator findDecorator(Class cls)
    {
        if ( cls.isInstance(this) )
        {
            return this;
        }
        if(getDelegate() == null) return null;
        ValueCalculator delegate = getDelegate();
        if ( delegate instanceof ValueCalculatorProxy )
        {
            return ((ValueCalculatorProxy) delegate).findDecorator(cls);  
        }

        return cls.isInstance(delegate) ? delegate : null;
    }


    public boolean hasDecorator(Class cls)
    {
        return findDecorator(cls) != null;
    }

}
