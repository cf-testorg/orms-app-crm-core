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
package com.redknee.app.crm.home;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;

/**
 * @author alok.sohani
 *
 */
public class AccountOverPaymentHistoryOperationsHome extends HomeProxy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public AccountOverPaymentHistoryOperationsHome(Home delegate)
    {
        super(delegate);
    }
	
	public AccountOverPaymentHistoryOperationsHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }
	
	/**
	 * Storing AccountOverPaymentHistory entry is not supported
	 */
	public Object store(Context ctx, Object bean) throws HomeException
	{
		
		throw new UnsupportedOperationException();
		
	}
	
	/**
	 * Removing AccountOverPaymentHistory entry is not supported
	 */
	public void remove(Context ctx, Object bean) throws HomeException
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Removing All AccountOverPaymentHistory entries is not supported
	 */
	public void removeAll(Context ctx, Object bean) throws HomeException
	{
		throw new UnsupportedOperationException();
	}
}
