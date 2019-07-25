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
package com.redknee.app.crm.filter;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;


/**
 * A Predicate that returns true when the given object equals the configured
 * object.  That is, if (match.equals(object)).
 *
 * @author gary.anderson@redknee.com
 */
public final
class EqualsPredicate
    implements Predicate
{
    /**
     * Creates a new EqualsPredicate for the given object.
     *
     * @param match The object to which all others are compared.
     */
    public EqualsPredicate(final Object match)
    {
        if (match == null)
        {
            throw new IllegalArgumentException("The match parameter is null.");
        }

        match_ = match;
    }


    // INHERIT
    public boolean f(Context ctx,final Object object)
    {
        return match_.equals(object);
    }

    
    /**
     * The object to which all others are compared.
     */
    private final Object match_;

}// class
