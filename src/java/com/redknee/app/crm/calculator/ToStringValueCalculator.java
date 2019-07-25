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

import com.redknee.framework.xhome.context.Context;


/**
 * Calculator to convert all output to string objects
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class ToStringValueCalculator extends ValueCalculatorProxy
{
    public ToStringValueCalculator()
    {
        super();
    }

    public ToStringValueCalculator(ValueCalculator delegate)
    {
        super(delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAdvanced(Context ctx)
    {
        Object value = super.getValueAdvanced(ctx);
        if (value instanceof String)
        {
            return value;
        }
        else if (value == null)
        {
            return "";
        }
        return String.valueOf(value);
    }
}
