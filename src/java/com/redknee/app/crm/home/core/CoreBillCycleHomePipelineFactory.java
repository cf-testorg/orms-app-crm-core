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

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.cluster.RMIClusteredHome;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.ContextualizingHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.home.ValidatingHome;

import com.redknee.app.crm.bean.BillCycle;
import com.redknee.app.crm.bean.BillCycleHome;
import com.redknee.app.crm.billing.message.BillingMessageAwareHomeDecorator;
import com.redknee.app.crm.core.agent.StorageInstall;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;
import com.redknee.app.crm.xhome.validator.BillCycleDayValidator;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CoreBillCycleHomePipelineFactory implements PipelineFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = CoreSupport.bindHome(ctx, BillCycle.class);
        
        // Install a home to adapt between business logic bean and data bean
        home = new AdapterHome(
                ctx, 
                home, 
                new ExtendedBeanAdapter<com.redknee.app.crm.bean.BillCycle, com.redknee.app.crm.bean.core.BillCycle>(
                        com.redknee.app.crm.bean.BillCycle.class, 
                        com.redknee.app.crm.bean.core.BillCycle.class));

        home = new RMIClusteredHome(ctx, BillCycleHome.class.getName() + StorageInstall.CORE_CLUSTER_SUFFIX, home);
        
        home = new ContextualizingHome(ctx, home);
        
        home = new SortingHome(home);
        
        home = new ValidatingHome(home, new BillCycleDayValidator());
        
        // this has to be near the end because it instruments the pipeline
        home = new BillingMessageAwareHomeDecorator().decorateHome(ctx, home);
        
		home =
		    ConfigChangeRequestSupportHelper.get(ctx)
		        .registerHomeForConfigSharing(ctx, home, BillCycle.class);
        
        return home;
    }

}
