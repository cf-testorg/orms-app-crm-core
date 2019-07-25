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
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.xenum.AbstractEnum;

import com.redknee.app.crm.bean.SubscriptionClassAware;
import com.redknee.app.crm.bean.account.SubscriptionClass;
import com.redknee.app.crm.technology.TechnologyEnum;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class SubscriptionClassTechnologyEnumPredicateEvaluator extends AbstractPredicateEvaluator
implements
EnumPredicateEvaluator
{
    public SubscriptionClassTechnologyEnumPredicateEvaluator()
    {
        this(null);
    }
    
    public SubscriptionClassTechnologyEnumPredicateEvaluator(
            EnumPredicateEvaluator delegate)
    {
        super(delegate);
    }

    /**
     * {@inheritDoc}
     */
    public boolean evaluate(Context ctx, AbstractEnum value)
    {
        if (value == null || !(value instanceof TechnologyEnum))
        {
            return false;
        }

        boolean result = true;
        
        final TechnologyEnum type = (TechnologyEnum)value;
        if (ctx.get(AbstractWebControl.BEAN) instanceof SubscriptionClassAware)
        {
            SubscriptionClassAware bean = (SubscriptionClassAware) ctx.get(AbstractWebControl.BEAN);
            final SubscriptionClass subscriptionClass = bean.getSubscriptionClass(ctx);
            if (subscriptionClass != null)
            {
                final int technologyEnumIndex = subscriptionClass.getTechnologyType();
                if( technologyEnumIndex == TechnologyEnum.ANY_INDEX)
                {
                    result = TechnologyEnum.NO_TECH_INDEX != type.getIndex();
                }
                else
                {
                    result = (type.getIndex() == technologyEnumIndex);
                }
            }
            else
            {
                result = true; // if there is no subscription class assigned, allow all technologies.
            }
        }

        return result && delegate(ctx, value);
    }

}
