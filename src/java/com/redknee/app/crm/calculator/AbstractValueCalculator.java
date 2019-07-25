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

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.support.BeanLoaderSupportHelper;


/**
 * This abstract class provides a means of prepopulating the context with dependent
 * context keys.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public abstract class AbstractValueCalculator extends AbstractBean implements ValueCalculator
{
    /**
     * {@inheritDoc}
     */
    public final Object getValue(Context ctx)
    {
        // Ensure that all required beans are in the context for the calculator's use
        BeanLoaderSupportHelper.get(ctx).prepareContext(ctx, getDependentContextKeys(ctx));
        return getValueAdvanced(ctx);
    }
    
    /**
     * @param ctx Operating context
     * @return Collection of context keys that are required for value calculation.
     */
    public abstract Collection<Object> getDependentContextKeys(Context ctx);
}
