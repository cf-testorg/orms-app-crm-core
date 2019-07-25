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
package com.redknee.app.crm.collection;

import com.redknee.framework.xhome.filter.Predicate;


/**
 * Provides a simple abstract base from which can be derived predicates that
 * store a value between calls to {@link #f}.
 *
 * @author gary.anderson@redknee.com
 */
public abstract
class ValueStoringPredicate
    implements Predicate
{
    /**
     * Creates a new ValueStoringPredicate with a null value.
     */
    public ValueStoringPredicate()
    {
        this(null);
    }


    /**
     * Creates a new ValueStoringPredicate with the given value.
     *
     * @param value The value stored in the predicate.
     */
    public ValueStoringPredicate(final Object value)
    {
        value_ = value;
    }


    /**
     * Gets the value stored in the predicate.
     *
     * @return The value stored in the predicate.
     */
    public Object getValue()
    {
        return value_;
    }


    /**
     * Sets the value stored in the predicate.
     *
     * @param value The value stored in the predicate.
     */
    public void setValue(final Object value)
    {
        value_ = value;
    }


    /**
     * The value stored in the predicate.
     */
    private Object value_;

} // class
