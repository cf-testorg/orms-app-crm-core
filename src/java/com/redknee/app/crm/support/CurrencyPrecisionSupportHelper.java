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

import com.redknee.framework.xhome.context.Context;

/**
 * @author cindy.wong@redknee.com
 * @since 2010-11-24
 */
public class CurrencyPrecisionSupportHelper extends SupportHelper
{

	private CurrencyPrecisionSupportHelper()
	{
	}

	/**
	 * @deprecated Use contextualized version of method
	 */
	@Deprecated
	public static CurrencyPrecisionSupport get()
	{
		return get(CurrencyPrecisionSupport.class,
		    DefaultCurrencyPrecisionSupport.instance());
	}

	public static CurrencyPrecisionSupport get(Context ctx)
	{
		CurrencyPrecisionSupport instance =
		    get(ctx, CurrencyPrecisionSupport.class,
		        DefaultCurrencyPrecisionSupport.instance());
		return instance;
	}

	public static CurrencyPrecisionSupport set(Context ctx,
	    CurrencyPrecisionSupport instance)
	{
		return register(ctx, CurrencyPrecisionSupport.class, instance);
	}
}
