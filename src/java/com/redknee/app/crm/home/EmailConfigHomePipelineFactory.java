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

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.cluster.RMIClusteredHome;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NotifyingHome;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.home.ValidatingHome;
import com.redknee.framework.xhome.msp.SpidAwareHome;

import com.redknee.app.crm.core.agent.StorageInstall;
import com.redknee.app.crm.delivery.email.CRMEmailConfig;
import com.redknee.app.crm.delivery.email.CRMEmailConfigHome;
import com.redknee.app.crm.support.EmailSupportHelper;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class EmailConfigHomePipelineFactory implements PipelineFactory
{

    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = CoreSupport.bindHome(ctx, CRMEmailConfig.class);

        home = new AuditJournalHome(ctx, home);
        
        home = new NotifyingHome(home);
        
        EmailSupportHelper.get(ctx).installEmailThrottle(home);
        
        home = new RMIClusteredHome(ctx, CRMEmailConfigHome.class.getName() + StorageInstall.CORE_CLUSTER_SUFFIX, home);

        home = new SortingHome(home);
        home = new SpidAwareHome(ctx, home);
        home = new ValidatingHome(EmailConfigReplyToAddressValidator.instance(), home);
                
        return home;
    }

}
