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

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.CachingHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NotifyingHome;

import com.redknee.app.crm.bean.CT23Rule;
import com.redknee.app.crm.bean.CT23RuleTransientHome;
import com.redknee.app.crm.bean.CT23RuleXDBHome;
import com.redknee.app.crm.xhome.home.CreateOrStoreCT2RuleHome;


/**
 * @author bdhavalshankh
 * @since 9.5
 */
public class CT23RuleHomePipelineFactory implements PipelineFactory
{

    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        //This table is going to be bulkloaded only. 
        
        Home home = new CT23RuleXDBHome(ctx, "CT23Rule");
        home = new NotifyingHome(new CachingHome(ctx, CT23Rule.class, new CT23RuleTransientHome(ctx), home));
        //If the record being bulkloaded exist, then update the record.
        home = new CreateOrStoreCT2RuleHome(ctx, home);
        
        return home;
    }

}
