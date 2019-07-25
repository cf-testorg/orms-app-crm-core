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
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.ContextualizingHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.LastModifiedAwareHome;
import com.redknee.framework.xhome.home.NoSelectAllHome;
import com.redknee.framework.xhome.msp.SpidAwareHome;

import com.redknee.app.crm.bean.core.Msisdn;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.technology.TechnologyAwareHome;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CoreMsisdnHomePipelineFactory implements PipelineFactory
{
    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
    AgentException
    {
        Home msisdnHome = StorageSupportHelper.get(ctx).createHome(ctx, Msisdn.class, "MSISDN");
        msisdnHome = new AuditJournalHome(ctx, msisdnHome);

        // Install a home to adapt between business logic bean and data bean
        msisdnHome = new AdapterHome(
                ctx, 
                msisdnHome, 
                new ExtendedBeanAdapter<com.redknee.app.crm.bean.Msisdn, com.redknee.app.crm.bean.core.Msisdn>(
                        com.redknee.app.crm.bean.Msisdn.class, 
                        com.redknee.app.crm.bean.core.Msisdn.class));

        msisdnHome = new ContextualizingHome(ctx, msisdnHome);
        msisdnHome = new LastModifiedAwareHome(msisdnHome);
        msisdnHome = new TechnologyAwareHome(ctx, msisdnHome);
        msisdnHome = new SpidAwareHome(ctx, msisdnHome);
        msisdnHome = new NoSelectAllHome(msisdnHome);
        
        return msisdnHome;
    }

}
