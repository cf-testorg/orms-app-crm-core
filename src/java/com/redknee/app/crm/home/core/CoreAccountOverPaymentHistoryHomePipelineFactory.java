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
package com.redknee.app.crm.home.core;


import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NoSelectAllHome;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.app.crm.bean.AccountOverPaymentHistoryXDBHome;
import com.redknee.app.crm.home.AccountOverPaymentHistoryOperationsHome;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.home.UpdateLatestAccountOverPaymentHistoryHome;

/**
 * @author alok.sohani
 *
 */
public class CoreAccountOverPaymentHistoryHomePipelineFactory implements PipelineFactory {

	@Override
	public Home createPipeline(Context ctx, Context serverCtx)
			throws RemoteException, HomeException, IOException, AgentException {
		
		Home home = new AccountOverPaymentHistoryXDBHome(ctx, "ACCOUNTOVERPAYMENTHISTORY");
		
		home = new UpdateLatestAccountOverPaymentHistoryHome(ctx, home);
		
		home = new AccountOverPaymentHistoryOperationsHome(ctx,home);
		
		home = new NoSelectAllHome(home);		
		
		return home;
	}

}
