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
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NotifyingHome;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.msp.SpidAwareHome;

import com.redknee.app.crm.bean.payment.PaymentPlan;
import com.redknee.app.crm.bean.payment.PaymentPlanHome;
import com.redknee.app.crm.home.PipelineFactory;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CorePaymentPlanHomePipelineFactory implements PipelineFactory
{

    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = CoreSupport.bindHome(ctx, PaymentPlan.class);
        
        home = new NotifyingHome(home);
        
        home = new RMIClusteredHome(ctx, PaymentPlanHome.class.getName(), home);
        
        home = new SortingHome(ctx, home);
        
        home = new SpidAwareHome(ctx, home);
        
        return home;
    }

}
