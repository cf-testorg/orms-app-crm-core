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
import com.redknee.framework.xhome.home.AgencyHome;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.LoggingHome;
import com.redknee.framework.xhome.home.RMIHomeServer;

import com.redknee.app.crm.configshare.ConfigChangeRequest;
import com.redknee.app.crm.configshare.ConfigChangeRequestHome;
import com.redknee.app.crm.configshare.ConfigChangeRequestSynchronizedHome;
import com.redknee.app.crm.configshare.ConfigChangeWriteExceptionCleanupHome;
import com.redknee.app.crm.configshare.DefaultConfigShareNodeHomeFactory;
import com.redknee.app.crm.configshare.ReceiveConfigChangeRequestHome;
import com.redknee.app.crm.xhome.home.FailureAuditingJournalHome;
import com.redknee.util.partitioning.partition.factory.ContextNodeHomeFactory;
import com.redknee.util.partitioning.partition.factory.LoggingNodeHomeFactory;
import com.redknee.util.partitioning.partition.factory.NodeHomeFactory;
import com.redknee.util.partitioning.partition.factory.PMNodeHomeFactory;
import com.redknee.util.partitioning.xhome.ConfigurablePartitionHome;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class ConfigChangeRequestHomePipelineFactory implements PipelineFactory
{
    public static final Object LOCAL_CONFIG_CHANGE_REQUST_HOME_CTX_KEY = ConfigChangeRequestHome.class.getName() + "_LOCALONLY";

    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home localHome = new ReceiveConfigChangeRequestHome(ctx);

        localHome = new ConfigChangeWriteExceptionCleanupHome(ctx, localHome);

        // Audit journal to track failed incoming changes
        localHome = new FailureAuditingJournalHome(ctx, LOCAL_CONFIG_CHANGE_REQUST_HOME_CTX_KEY, localHome);
        
        // Audit journal to track successful incoming changes
        localHome = new AuditJournalHome(ctx, localHome);
        
        // Home to synchronize requests made to update the same bean
        localHome = new ConfigChangeRequestSynchronizedHome(ctx, localHome);
        
        // Decorate the change request home here
        localHome = new LoggingHome(ctx, localHome);
        
        ctx.put(LOCAL_CONFIG_CHANGE_REQUST_HOME_CTX_KEY, localHome);

        // Put the local home in the context.  It will be retrieved during installation of the partitioning home.
        Context sCtx = ctx.createSubContext();
        sCtx.put(ConfigChangeRequestHome.class, localHome);
        
        // Register RMIHomeServer for remote partitioning to work
        new RMIHomeServer(ctx, new AgencyHome(ctx, localHome), ConfigChangeRequestHome.class.getName()).register();

        // Install the partitioning home on top of the local home
        NodeHomeFactory homeFactory = ContextNodeHomeFactory.instance();
        homeFactory = new DefaultConfigShareNodeHomeFactory(sCtx, homeFactory);
        homeFactory = new PMNodeHomeFactory(sCtx, homeFactory);
        homeFactory = new LoggingNodeHomeFactory(sCtx, homeFactory);
        
        final ConfigurablePartitionHome home = new ConfigurablePartitionHome(sCtx, ConfigChangeRequest.class, homeFactory);
        
        return home;
    }

}
