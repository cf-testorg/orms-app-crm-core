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

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.app.crm.bean.AccountsDiscount;
import com.redknee.app.crm.bean.AccountsDiscountHome;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.msp.SpidAwareHome;

/**
 * Pipeline for service level discount 
 * 
 * @author shailesh.makhijani
 * @since 9.7.2
 */

public class AccountsDiscountPipelineFactory implements PipelineFactory {

	@Override
	public Home createPipeline(Context ctx, Context arg1)
			throws RemoteException, HomeException, IOException, AgentException {
		
		Home home = StorageSupportHelper.get(ctx).createHome(ctx, AccountsDiscount.class, "AccountsDiscount");
		home = ConfigChangeRequestSupportHelper.get(ctx).registerHomeForConfigSharing(ctx, home, AccountsDiscount.class);
		home = new SpidAwareHome(ctx, home);
		ctx.put(AccountsDiscountHome.class, home);
		
		return home;
	}

}
