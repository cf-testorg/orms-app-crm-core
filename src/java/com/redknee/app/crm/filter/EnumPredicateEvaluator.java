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
 * Evaluates an enum to decide if it can be added to a collection in a predicate
 * @author arturo.medina@redknee.com
 *
 */
public interface EnumPredicateEvaluator
{
    /**
     * Evaluates the enum
     * @param ctx the operating context
     * @param value the enum to evaluate
     * @return true if it can be added to the collection
     */
    public boolean evaluate(Context ctx, AbstractEnum value);
}
