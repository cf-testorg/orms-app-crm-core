/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee, no
 * unauthorised use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the licence agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.home.core;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.cluster.RMIClusteredHome;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NotifyingHome;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.home.TestSerializabilityHome;

import com.redknee.app.crm.bean.IdentifierEnum;
import com.redknee.app.crm.bean.TransactionMethod;
import com.redknee.app.crm.bean.TransactionMethodHome;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.sequenceId.IdentifierSettingHome;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;
import com.redknee.app.crm.support.IdentifierSequenceSupportHelper;

/**
 * Creates a Home pipeline for TransactionMethod
 * @author amedina
 *
 */
public class CoreTransactionMethodHomePipelineFactory implements PipelineFactory
{

	public CoreTransactionMethodHomePipelineFactory()
	{
		super();
	}

	@Override
    public Home createPipeline(Context ctx, Context serverCtx)
			throws RemoteException, HomeException, IOException, AgentException
	{
        Home txnMethodHome = CoreSupport.bindHome(ctx, TransactionMethod.class);
        txnMethodHome = new NotifyingHome(txnMethodHome);
        txnMethodHome = new AuditJournalHome(ctx, txnMethodHome);
        txnMethodHome = new RMIClusteredHome(ctx,
        									TransactionMethodHome.class.getName(),
        									txnMethodHome);
        txnMethodHome = new SortingHome(txnMethodHome);

        txnMethodHome = new TestSerializabilityHome(ctx,txnMethodHome);

        txnMethodHome = new IdentifierSettingHome(
                ctx,
                txnMethodHome,
                IdentifierEnum.TXNMETHOD_ID, null);

        IdentifierSequenceSupportHelper.get(ctx).ensureNextIdIsLargeEnough(ctx, IdentifierEnum.TXNMETHOD_ID, txnMethodHome);
        
		txnMethodHome =
		    ConfigChangeRequestSupportHelper.get(ctx)
		        .registerHomeForConfigSharing(ctx, txnMethodHome,
		            TransactionMethod.class);

        return txnMethodHome;

	}

}
