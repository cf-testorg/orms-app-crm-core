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
import com.redknee.framework.xhome.xenum.AbstractEnum;

/**
 * Pass the chain of responsibility to another delegate to evaluate the enum
 * @author arturo.medina@redknee.com
 *
 */
public abstract class AbstractPredicateEvaluator
{

    /**
     * Accepts the evaluator
     * @param delegate
     */
    public AbstractPredicateEvaluator(EnumPredicateEvaluator delegate)
    {
        delegate_ = delegate;
    }

    /**
     * Delegates the evaluation to the delegated 
     * @param ctx
     * @param value
     * @return
     */
    public boolean delegate(Context ctx, AbstractEnum value)
    {
        boolean returnValued = true;
        if (delegate_ != null)
        {
            returnValued  = delegate_.evaluate(ctx, value);
        }
        return returnValued;
    }
    
    /**
     * The delegate to pass the 
     */
    private EnumPredicateEvaluator delegate_;
}
