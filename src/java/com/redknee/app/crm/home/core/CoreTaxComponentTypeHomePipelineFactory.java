/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
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

import com.redknee.app.crm.bean.TaxComponentType;
import com.redknee.app.crm.bean.TaxComponentTypeHome;
import com.redknee.app.crm.core.agent.StorageInstall;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;


/**
 * 
 * 
 * @author bhupendra.pandey@redknee.com
 * @since
 */
public class CoreTaxComponentTypeHomePipelineFactory implements PipelineFactory
{

    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home taxtypeHome = CoreSupport.bindHome(ctx, TaxComponentType.class);
        
        taxtypeHome = new RMIClusteredHome(ctx, TaxComponentTypeHome.class.getName() + StorageInstall.CORE_CLUSTER_SUFFIX, taxtypeHome);
        
        taxtypeHome = ConfigChangeRequestSupportHelper.get(ctx).registerHomeForConfigSharing(ctx, taxtypeHome,
                TaxComponentType.class);
        
        return taxtypeHome;
    }
}
