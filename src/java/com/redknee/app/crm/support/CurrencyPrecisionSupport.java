/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily
 * available. Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee, no unauthorised use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the licence agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright &copy; Redknee Inc. and its subsidiaries. All Rights Reserved.
 *
 */
package com.redknee.app.crm.support;

import com.redknee.framework.core.locale.Currency;
import com.redknee.framework.xhome.context.Context;

/**
 * Support interface of Currency Precision.
 * 
 * @author cindy.wong@redknee.com
 * @since 8.6
 */
public interface CurrencyPrecisionSupport extends Support
{
	public Currency getCurrency(Context ctx, String currencyCode);

	public String formatDisplayCurrencyValue(Context ctx, Currency currency,
	    Number value);

	public String formatStorageCurrencyValue(Context ctx, Currency currency,
	    Number value);

	public String formatDisplayCurrencyValue(Context ctx, String currencyCode,
	    Number value);

	public String formatStorageCurrencyValue(Context ctx, String currencyCode,
	    Number value);

	public String formatDisplayCurrencyValue(Context ctx, Currency currency,
	    long value);

	public String formatStorageCurrencyValue(Context ctx, Currency currency,
	    long value);

	public String formatDisplayCurrencyValue(Context ctx, String currencyCode,
	    long value);

	public String formatStorageCurrencyValue(Context ctx, String currencyCode,
	    long value);
}
