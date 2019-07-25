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

import com.redknee.app.crm.bean.DebtCollectionAgency;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.msp.SpidAwareHome;
import com.redknee.framework.xlog.log.LogSupport;


/**
 * Creates a Home pipeline for Debt Collection Agencies
 * @author Marcio Marques
 */
public class CoreDebtCollectionAgencyHomePipelineFactory implements PipelineFactory
{
    public CoreDebtCollectionAgencyHomePipelineFactory()
    {
        super();
    }

    public Home createPipeline(Context ctx, Context serverCtx)
            throws RemoteException, HomeException, IOException, AgentException
    {
        LogSupport.info(ctx, this, "Installing the Debt Collection Agency home ");
        Home home = StorageSupportHelper.get(ctx).createHome(ctx, DebtCollectionAgency.class, "DEBTCOLLECTIONAGENCY");
        home = new AuditJournalHome(ctx, home);
        home = new SortingHome(ctx, home);
        home = new SpidAwareHome(ctx, home);
        LogSupport.info(ctx, this, "Debt Collection Agency installed successfully");
        return home;

    }
}
