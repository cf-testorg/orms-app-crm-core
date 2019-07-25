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
import com.redknee.app.crm.technology.TechnologyEnum;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.visitor.AbortVisitException;

/**
 * Calls the evaluators to verify if this enum can be displayed or not
 * @author arturo.medina@redknee.com
 *
 */
public class TechnologyTypeEnumPredicate implements Predicate
{
    /**
     * 
     */
    private static final long serialVersionUID = 8376172601400192677L;
    
    /**
     * Sets up the chain of evaluators for Technology on the subscription
     */
    public TechnologyTypeEnumPredicate()
    {
        evaluator_ = new TechnologyLicenseEnumPredicateEvaluator(
                            new SubscriptionClassTechnologyEnumPredicateEvaluator());
    }

    /**
     * {@inheritDoc}
     */
    public boolean f(Context ctx, Object obj) throws AbortVisitException
    {
        if (obj == null || !(obj instanceof TechnologyEnum))
        {
            return false;
        }
        
        TechnologyEnum tType = (TechnologyEnum)obj;
        
        if (tType == TechnologyEnum.ANY)
        {
            return false;
        }
        
        return evaluator_.evaluate(ctx, tType);
    }

    private EnumPredicateEvaluator evaluator_;

}
