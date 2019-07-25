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
package com.redknee.app.crm.factory;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.FacetMgrUtil;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextFactory;

import com.redknee.app.crm.calculator.PropertyBasedValueCalculator;

public class CustomPropertyValueCalculatorFactory implements ContextFactory
{
    private final Class<? extends PropertyBasedValueCalculator> type_;
    private final Class<? extends AbstractBean> beanClass_;

    public CustomPropertyValueCalculatorFactory(Class<? extends PropertyBasedValueCalculator> type, Class<? extends AbstractBean> beanClass)
    {
        if (type == null)
        {
            throw new NullPointerException("Value Calculator type can not be null");
        }

        if (beanClass == null)
        {
            throw new NullPointerException("Bean class can not be null");
        }
        
        type_ = type;
        beanClass_ = beanClass;
    }

    public Object create(Context ctx)
    {
        PropertyBasedValueCalculator calc = (PropertyBasedValueCalculator) FacetMgrUtil.instantiate(ctx, type_);
        if (calc != null)
        {
            calc.setBeanClassName(beanClass_.getName());
        }
        return calc;
    }
}