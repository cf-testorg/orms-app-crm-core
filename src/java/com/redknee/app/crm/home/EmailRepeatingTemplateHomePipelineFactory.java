/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily
 * available. Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee. No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used in
 * accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
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
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.SortingHome;

import com.redknee.app.crm.core.agent.StorageInstall;
import com.redknee.app.crm.delivery.email.EmailRepeatingTemplate;
import com.redknee.app.crm.delivery.email.EmailRepeatingTemplateHome;

/**
 * @author cindy.wong@redknee.com
 * @since 8.3
 */
public class EmailRepeatingTemplateHomePipelineFactory implements
        PipelineFactory
{

    /**
     * @see com.redknee.app.crm.home.PipelineFactory#createPipeline(com.redknee.framework.xhome.context.Context,
     *      com.redknee.framework.xhome.context.Context)
     */
    @Override
    public Home createPipeline(Context ctx, Context serverCtx)
            throws RemoteException, HomeException, IOException, AgentException
    {
        Home home = CoreSupport.bindHome(ctx, EmailRepeatingTemplate.class);
        home = new RMIClusteredHome(ctx, EmailRepeatingTemplateHome.class
                .getName() + StorageInstall.CORE_CLUSTER_SUFFIX, home);

        home = new SortingHome(home);

        return home;
    }

}
