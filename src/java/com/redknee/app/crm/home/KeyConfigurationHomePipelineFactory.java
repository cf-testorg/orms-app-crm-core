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
package com.redknee.app.crm.home;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.beans.CompoundValidator;
import com.redknee.framework.xhome.cluster.RMIClusteredHome;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.home.ValidatingHome;

import com.redknee.app.crm.bean.KeyConfiguration;
import com.redknee.app.crm.bean.KeyConfigurationHome;
import com.redknee.app.crm.core.agent.StorageInstall;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;
import com.redknee.app.crm.xhome.validator.GRRKeyConfigurationCheckHome;
import com.redknee.app.crm.xhome.validator.KeyFeatureChangeValidator;


/**
 * @author abaid
 * 
 */
public class KeyConfigurationHomePipelineFactory implements PipelineFactory
{

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.redknee.app.crm.home.PipelineFactory#createPipeline(com.redknee.framework.xhome
     * .context.Context, com.redknee.framework.xhome.context.Context)
     */
    public Home createPipeline(final Context ctx, final Context serverCtx) throws RemoteException, HomeException,
            IOException, AgentException
    {
        Home home = CoreSupport.bindHome(ctx, KeyConfiguration.class);
        home = new RMIClusteredHome(ctx, KeyConfigurationHome.class.getName() + StorageInstall.CORE_CLUSTER_SUFFIX, home);

        CompoundValidator validator = new CompoundValidator();
        validator.add(new KeyFeatureChangeValidator());
        home = new ValidatingHome(home, validator);
        
        home = new GRRKeyConfigurationCheckHome(ctx,home);
                
        home = new SortingHome(home);
        home =
            ConfigChangeRequestSupportHelper.get(ctx)
                .registerHomeForConfigSharing(ctx, home, KeyConfiguration.class);
        
        home =
            ConfigChangeRequestSupportHelper.get(ctx)
                .registerHomeForConfigSharing(ctx, home, KeyConfiguration.class);
        
        return home;
    }
}
