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
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.msp.SpidAwareHome;

import com.redknee.app.crm.bean.ReasonCode;
import com.redknee.app.crm.home.ReasonCodeIDSettingHome;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;
import com.redknee.app.crm.support.StorageSupportHelper;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CoreReasonCodeHomePipelineFactory implements PipelineFactory
{

    /**
     * {@inheritDoc}
     */
    @Override
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = StorageSupportHelper.get(ctx).createHome(ctx, ReasonCode.class, "REASONCODE");
        home = new AuditJournalHome(ctx, home);
        
        home = new SortingHome(ctx, home);
        
        home = new SpidAwareHome(ctx, home);
		home =
		    ConfigChangeRequestSupportHelper.get(ctx)
		        .registerHomeForConfigSharing(ctx, home, ReasonCode.class);
        
		home =  new ReasonCodeIDSettingHome(ctx, home);
	        
        return home;
    }

}
