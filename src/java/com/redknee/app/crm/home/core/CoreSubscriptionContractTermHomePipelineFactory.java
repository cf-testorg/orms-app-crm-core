/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee, no
 * unauthorised use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the licence agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright &copy; Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.home.core;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.app.crm.bean.AccountCategoryTransientHome;
import com.redknee.app.crm.billing.message.BillingMessageAwareHomeDecorator;
import com.redknee.app.crm.contract.SubscriptionContractTerm;
import com.redknee.app.crm.contract.SubscriptionContractTermTransientHome;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;
import com.redknee.app.crm.xhome.home.ConfigShareTotalCachingHome;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NotifyingHome;


/**
 * 
 * @author vijay.gote
 * @since 9.5
 */
public class CoreSubscriptionContractTermHomePipelineFactory implements PipelineFactory
{

    public CoreSubscriptionContractTermHomePipelineFactory()
    {
        super();
    }


    @Override
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = StorageSupportHelper.get(ctx).createHome(ctx, SubscriptionContractTerm.class,
                "SubscriptionContractTerm");
        home = new AdapterHome(
                ctx,
                home,
                new ExtendedBeanAdapter<com.redknee.app.crm.contract.SubscriptionContractTerm, com.redknee.app.crm.bean.core.SubscriptionContractTerm>(
                        com.redknee.app.crm.contract.SubscriptionContractTerm.class,
                        com.redknee.app.crm.bean.core.SubscriptionContractTerm.class));
        home = new BillingMessageAwareHomeDecorator().decorateHome(ctx, home);
        home = new NotifyingHome(home);
        home = new AuditJournalHome(ctx, home);
        return home;
    }
}
