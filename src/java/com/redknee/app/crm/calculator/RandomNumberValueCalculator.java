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
import java.util.Random;

import com.redknee.framework.xhome.beans.XCloneable;
import com.redknee.framework.xhome.context.Context;


/**
 * Calculator for generating random numbers of type 'long'.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class RandomNumberValueCalculator extends AbstractValueCalculator implements XCloneable
{
    protected final Random generator_;
    
    public RandomNumberValueCalculator()
    {
        generator_ = new Random();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Object> getDependentContextKeys(Context ctx)
    {
        return new ArrayList<Object>();
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Object getValueAdvanced(Context ctx)
    {
        return generator_.nextLong();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean transientEquals(Object o)
    {
        return equals(o);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean persistentEquals(Object o)
    {
        return equals(o);
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
