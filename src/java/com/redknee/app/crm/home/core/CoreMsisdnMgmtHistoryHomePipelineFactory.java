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
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NoSelectAllHome;
import com.redknee.framework.xhome.home.SortingHome;

import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.numbermgn.MsisdnMgmtHistoryXDBHome;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CoreMsisdnMgmtHistoryHomePipelineFactory implements PipelineFactory
{
    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = new MsisdnMgmtHistoryXDBHome(ctx, "MSISDNMGMTHISTORY");

        // Install a home to adapt between business logic bean and data bean
        home = new AdapterHome(
                ctx, 
                home, 
                new ExtendedBeanAdapter<com.redknee.app.crm.numbermgn.MsisdnMgmtHistory, com.redknee.app.crm.bean.core.MsisdnMgmtHistory>(
                        com.redknee.app.crm.numbermgn.MsisdnMgmtHistory.class, 
                        com.redknee.app.crm.bean.core.MsisdnMgmtHistory.class));
        
        home = new SortingHome(home);
        
        home = new NoSelectAllHome(home);
        
        return home;
    }

}
