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

import java.util.Collection;

import com.redknee.framework.xhome.context.Context;


/**
 * This calculator returns a value from the context.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class ContextObjectValueCalculator extends ConstantValueCalculator
{

    public ContextObjectValueCalculator()
    {
        super();
    }


    public ContextObjectValueCalculator(Class value)
    {
        super(value);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Object> getDependentContextKeys(Context ctx)
    {
        final Collection keyCollection = super.getDependentContextKeys(ctx);
        keyCollection.add(getContextKey(getValue()));
        return keyCollection;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAdvanced(Context ctx)
    {
        return ctx.get(getContextKey(getValue()));
    }



    protected Object getContextKey(Object value)
    {
        if (value instanceof String
                && ((String) value).endsWith(".class"))
        {
            String strValue = (String) value;
            try
            {
                return Class.forName(strValue.substring(0, strValue.length()-6));
            }
            catch (ClassNotFoundException e)
            {
                // NOP
            }
        }
        return value;
    }
}
