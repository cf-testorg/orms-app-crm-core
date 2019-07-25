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
package com.redknee.app.crm.home;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.app.crm.bean.bank.Bank;
import com.redknee.app.crm.bean.bank.BankTransientHome;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.xhome.home.ConfigShareTotalCachingHome;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NotifyingHome;
import com.redknee.framework.xhome.msp.SpidAwareHome;

/**
 * @author cindy.wong@redknee.com
 * @since 2011-03-03
 */
public class BankHomePipelineFactory implements PipelineFactory
{

	@Override
	public Home createPipeline(Context ctx, Context serverCtx)
	    throws RemoteException, HomeException, IOException, AgentException
	{
		Home home =
		    StorageSupportHelper.get(ctx).createHome(ctx, Bank.class, "BANKS");
		home = new NotifyingHome(home);
		
		// Note: TotalCachingHome automatically registers the home for config sharing, so no need to do it explicitly
		home =  new ConfigShareTotalCachingHome( ctx, new BankTransientHome(ctx),home);
		
		home = new SpidAwareHome(ctx, home);        
		return home;
	}

}
