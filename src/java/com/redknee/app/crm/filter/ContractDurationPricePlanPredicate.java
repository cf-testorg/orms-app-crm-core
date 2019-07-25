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

import java.util.Collection;
import java.util.Map;

import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.webcontrol.OptionalLongWebControl;

import com.redknee.app.crm.bean.core.PricePlan;
import com.redknee.app.crm.bean.payment.Contract;
import com.redknee.app.crm.support.BeanLoaderSupportHelper;

/**
 * Predicate that returns true if a contract can be located in the context
 * that does not violate the given price plan's duration restrictions.
 * 
 * Note that prior to using this predicate, the caller should install
 * the appropriate bean loader map in the context.  Alternatively, this
 * can be simplified by passing the bean loader map in the constructor.
 * 
 * The bean loader map needs to have a path to com.redknee.app.crm.bean.payment.Contract
 * in order for this predicate to have any chance of returning true.  
 *
 * @author aaron.gourley@redknee.com
 * @since 8.6
 */
public class ContractDurationPricePlanPredicate implements Predicate
{
    public ContractDurationPricePlanPredicate()
    {
    }
    
    public ContractDurationPricePlanPredicate(Map<Class, Collection<PropertyInfo>> beanLoaderMap)
    {
        beanLoaderMap_ = beanLoaderMap;
    }

    /**
     * {@inheritDoc}
     */
    public boolean f(Context ctx, Object obj) throws AbortVisitException
    {
        if (obj instanceof PricePlan)
        {
            PricePlan plan = (PricePlan) obj;
            if (plan.isApplyContractDurationCriteria())
            {
                Context sCtx = ctx.createSubContext();
                sCtx.put(PricePlan.class, plan);
                if (beanLoaderMap_ != null)
                {
                    BeanLoaderSupportHelper.get(sCtx).setBeanLoaderMap(sCtx, beanLoaderMap_);
                }
                
                Contract contract = BeanLoaderSupportHelper.get(ctx).getBean(sCtx, Contract.class);
                if (contract != null)
                {
                    long duration = contract.getContractDurationInDays();
                    long minDuration = plan.getMinContractDurationInDays();
                    long maxDuration = plan.getMaxContractDurationInDays();
                    
                    boolean isMinViolation = minDuration != OptionalLongWebControl.DEFAULT_VALUE && duration < minDuration;
                    boolean isMaxViolation = maxDuration != OptionalLongWebControl.DEFAULT_VALUE && duration > maxDuration;
                    
                    return !(isMinViolation || isMaxViolation);
                }
            }
            else
            {
                return true;
            }
        }
        return false;
    }

    protected Map<Class, Collection<PropertyInfo>> beanLoaderMap_;
}
