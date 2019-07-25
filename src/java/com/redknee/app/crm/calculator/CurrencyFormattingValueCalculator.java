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

import java.util.ArrayList;
import java.util.Collection;

import com.redknee.framework.core.bean.Application;
import com.redknee.framework.core.locale.Currency;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.support.CurrencyPrecisionSupportHelper;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class CurrencyFormattingValueCalculator extends AbstractCurrencyFormattingValueCalculator
{
    public CurrencyFormattingValueCalculator()
    {
        super();
    }
    
    public CurrencyFormattingValueCalculator(ValueCalculator delegate)
    {
        super();
        setDelegate(delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Object> getDependentContextKeys(Context ctx)
    {
        Collection dependentContextKeys = super.getDependentContextKeys(ctx);
        if (dependentContextKeys == null)
        {
            dependentContextKeys = new ArrayList();
        }

        dependentContextKeys.add(Application.class);

        return dependentContextKeys;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAdvanced(Context ctx)
    {
        Object value = super.getValueAdvanced(ctx);

        String currencyCode = getCurrency(ctx);
		Currency currency =
		    CurrencyPrecisionSupportHelper.get(ctx).getCurrency(ctx,
		        currencyCode);

        if (currency != null)
        {
			if (value instanceof Number)
			{
				value =
				    CurrencyPrecisionSupportHelper.get(ctx)
				        .formatDisplayCurrencyValue(ctx, currency,
				            (Number) value);
			}
        }
        else if (value instanceof Number)
        {
            new MinorLogMsg(ctx, "Unable to apply currency format.  No currency available.", null).log(ctx);
        }

        return value;
    }

    /**
     * {@inheritDoc}
     */
    public String getCurrency(Context ctx)
    {
        Application application = (Application) ctx.get(Application.class);
        if (application != null)
        {
            return application.getLocaleIsoCurrency();
        }
        else
        {
            new MinorLogMsg(ctx, "Unable to apply currency format.  Application config not available.", null).log(ctx);
        }
        return null;
    }

}
