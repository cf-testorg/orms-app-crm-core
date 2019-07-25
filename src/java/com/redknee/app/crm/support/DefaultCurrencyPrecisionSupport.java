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
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.CurrencyPrecision;

/**
 * @author cindy.wong@redknee.com
 * @since 2010-11-24
 */
public class DefaultCurrencyPrecisionSupport implements
    CurrencyPrecisionSupport
{
	public static final String DISPLAY_PRECISION_CURRENCY_KEY_SUFFIX =
	    "displayPrecision";
	protected static CurrencyPrecisionSupport instance_ = null;
	public static final String STORAGE_PRECISION_CURRENCY_KEY_SUFFIX =
	    "storagePrecision";

	public static CurrencyPrecisionSupport instance()
	{
		if (instance_ == null)
		{
			instance_ = new DefaultCurrencyPrecisionSupport();
		}
		return instance_;
	}

	protected DefaultCurrencyPrecisionSupport()
	{
	}

	protected long convertDecimalValueToLong(Context ctx, Currency currency,
	    double value)
	{
		// Shift the decimal place so that no precision is lost when converting
		// to a long
		double shiftedValue = value;
		for (int i = 0; i < currency.getPrecision(); i++)
		{
			shiftedValue *= 10;
		}

		// Convert to long
		return ((Number) shiftedValue).longValue();
	}
	
	protected double convertLongValuetoDecimal(Context ctx, Currency currency, long value){
		double shiftValue=value;
		
		for(int i =1; i<=currency.getPrecision(); i++){
			shiftValue /=10f;
		}
		return (shiftValue);
	}

	/**
	 * @param ctx
	 * @param currency
	 * @param value
	 * @return
	 * @see com.redknee.app.crm.support.CurrencyPrecisionSupport#formatDisplayCurrencyValue(com.redknee.framework.xhome.context.Context,
	 *      com.redknee.framework.core.locale.Currency, long)
	 */
	@Override
	public String formatDisplayCurrencyValue(Context ctx, Currency currency,
	    long value)
	{
		return getDisplayPrecisionCurrency(ctx, currency).formatValue(value);
	}

	@Override
	public String formatDisplayCurrencyValue(Context ctx, Currency currency,
	    Number value)
	{
		long longValue = 0;
		if (value instanceof Double || value instanceof Float)
		{
			longValue =
			    convertDecimalValueToLong(ctx, currency, value.doubleValue());
		}
		else
		{
			longValue = value.longValue();
		}
		return formatDisplayCurrencyValue(ctx, currency, longValue);
	}

	@Override
	public String formatDisplayCurrencyValue(Context ctx, String currencyCode,
	    long value)
	{
		Currency currency = getCurrency(ctx, currencyCode);
		return getDisplayPrecisionCurrency(ctx, currency).formatValue(value);
	}

	@Override
	public String formatDisplayCurrencyValue(Context ctx, String currencyCode,
	    Number value)
	{
		Currency currency = getCurrency(ctx, currencyCode);
		return formatDisplayCurrencyValue(ctx, currency, value);
	}

	/**
	 * @param ctx
	 * @param currency
	 * @param value
	 * @return
	 * @see com.redknee.app.crm.support.CurrencyPrecisionSupport#formatStorageCurrencyValue(com.redknee.framework.xhome.context.Context,
	 *      com.redknee.framework.core.locale.Currency, long)
	 */
	@Override
	public String formatStorageCurrencyValue(Context ctx, Currency currency,
	    long value)
	{
		return getStoragePrecisionCurrency(ctx, currency).formatValue(value);
	}

	@Override
	public String formatStorageCurrencyValue(Context ctx, Currency currency,
	    Number value)
	{
		long longValue = 0;
		if (value instanceof Double || value instanceof Float)
		{
			longValue =
			    convertDecimalValueToLong(ctx, currency, value.doubleValue());
		}
		else
		{
			longValue = value.longValue();
		}
		return formatStorageCurrencyValue(ctx, currency, longValue);
	}

	@Override
	public String formatStorageCurrencyValue(Context ctx, String currencyCode,
	    long value)
	{
		Currency currency = getCurrency(ctx, currencyCode);
		return getStoragePrecisionCurrency(ctx, currency).formatValue(value);
	}

	@Override
	public String formatStorageCurrencyValue(Context ctx, String currencyCode,
	    Number value)
	{
		Currency currency = getCurrency(ctx, currencyCode);
		return formatStorageCurrencyValue(ctx, currency, value);
	}

	@Override
    public Currency getCurrency(Context ctx, String currencyCode)
	{
		Currency currency = null;
		if (currencyCode != null)
		{
			try
			{
				currency =
				    HomeSupportHelper.get(ctx).findBean(ctx, Currency.class,
				        currencyCode);
			}
			catch (HomeException e)
			{
				LogSupport.minor(ctx, this, "Error retrieving currency "
				    + currencyCode, e);
			}
		}
		return currency;

	}

	protected Currency getDisplayPrecisionCurrency(Context ctx,
	    Currency currency)
	{
		Currency result =
		    (Currency) ctx.get(getDisplayPrecisionCurrencyKey(currency));
		if (result == null)
		{
			CurrencyPrecision precision =
			    (CurrencyPrecision) ctx.get(CurrencyPrecision.class);
			if (precision == null)
			{
				LogSupport
				    .minor(ctx, this,
				        "Cannot find currency precision object in context; using supplied currency");
				return currency;
			}

			if (currency.getPrecision() == precision.getDisplayPrecision())
			{
				ctx.put(getDisplayPrecisionCurrencyKey(currency), currency);
				return currency;
			}
			try
			{
				result = (Currency) currency.deepClone();
			}
			catch (CloneNotSupportedException exception)
			{
				LogSupport.crit(ctx, this, "Fail to clone currency object");
				result = currency;
			}

			result.setFormat(getPrecisionFormatString(precision
			    .getDisplayPrecision()));
		}
		return result;
	}

	protected String getDisplayPrecisionCurrencyKey(Currency currency)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName());
		sb.append('.');
		sb.append(currency.getCode());
		sb.append('.');
		sb.append(DISPLAY_PRECISION_CURRENCY_KEY_SUFFIX);
		return sb.toString();
	}

	protected String getPrecisionFormatString(int precision)
	{
		StringBuilder sb = new StringBuilder();
		sb.append('#');
		if (precision > 0)
		{
			sb.append('.');
			for (int p = 0; p < precision; p++)
			{
				sb.append('#');
			}
		}
		return sb.toString();
	}

	protected Currency getStoragePrecisionCurrency(Context ctx,
	    Currency currency)
	{
		Currency result =
		    (Currency) ctx.get(getStoragePrecisionCurrencyKey(currency));
		if (result == null)
		{
			CurrencyPrecision precision =
			    (CurrencyPrecision) ctx.get(CurrencyPrecision.class);
			if (precision == null)
			{
				LogSupport
				    .minor(ctx, this,
				        "Cannot find currency precision object in context; using supplied currency");
				return currency;
			}

			if (currency.getPrecision() == precision.getStoragePrecision())
			{
				ctx.put(getStoragePrecisionCurrencyKey(currency), currency);
				return currency;
			}
			try
			{
				result = (Currency) currency.deepClone();
			}
			catch (CloneNotSupportedException exception)
			{
				LogSupport.crit(ctx, this, "Fail to clone currency object");
				result = currency;
			}

			result.setFormat(getPrecisionFormatString(precision
			    .getStoragePrecision()));
		}
		return result;
	}

	protected String getStoragePrecisionCurrencyKey(Currency currency)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName());
		sb.append('.');
		sb.append(currency.getCode());
		sb.append('.');
		sb.append(STORAGE_PRECISION_CURRENCY_KEY_SUFFIX);
		return sb.toString();
	}
}
