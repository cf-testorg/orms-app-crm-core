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
package com.redknee.app.crm.home.core;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.ReadOnlyHome;

import com.redknee.app.crm.bean.AgedDebt;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;

/**
 * @author cindy.wong@redknee.com
 * @since 9.0
 */
public class CoreAgedDebtHomePipelineFactory implements PipelineFactory
{
	public CoreAgedDebtHomePipelineFactory()
	{
		super();
	}

	@Override
	public Home createPipeline(Context ctx, Context serverCtx)
	    throws RemoteException, HomeException, IOException, AgentException
	{
		Home home =
		    StorageSupportHelper.get(ctx).createHome(ctx,
		        AgedDebt.class, "AgedDebt");
		home = new ReadOnlyHome(home);
		return home;
	}

}

