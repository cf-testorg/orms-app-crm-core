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

import com.redknee.app.crm.bean.ServicePackage;
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
public class CoreServicePackageHomePipelineFactory implements PipelineFactory
{

    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = StorageSupportHelper.get(ctx).createHome(ctx, ServicePackage.class, "ServicePackage");
        
        // Install a home to adapt between business logic bean and data bean
        home = new AdapterHome(
                ctx, 
                home, 
                new ExtendedBeanAdapter<com.redknee.app.crm.bean.ServicePackage, com.redknee.app.crm.bean.core.ServicePackage>(
                        com.redknee.app.crm.bean.ServicePackage.class, 
                        com.redknee.app.crm.bean.core.ServicePackage.class));
        
        home = new TechnologyAwareHome(ctx, home);
        
        return home;
    }

}
