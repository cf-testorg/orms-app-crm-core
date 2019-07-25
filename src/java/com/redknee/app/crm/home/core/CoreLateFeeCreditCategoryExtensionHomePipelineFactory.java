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

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NoSelectAllHome;

import com.redknee.app.crm.extension.creditcategory.core.LateFeeCreditCategoryExtension;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.4
 */
public class CoreLateFeeCreditCategoryExtensionHomePipelineFactory implements PipelineFactory
{
    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = CoreSupport.bindHome(ctx, LateFeeCreditCategoryExtension.class);

        home = new AdapterHome(ctx, 
                new ExtendedBeanAdapter<com.redknee.app.crm.extension.creditcategory.LateFeeCreditCategoryExtension, com.redknee.app.crm.extension.creditcategory.core.LateFeeCreditCategoryExtension>(
                        com.redknee.app.crm.extension.creditcategory.LateFeeCreditCategoryExtension.class, 
                        com.redknee.app.crm.extension.creditcategory.core.LateFeeCreditCategoryExtension.class), 
                        home);
        
        home = new NoSelectAllHome(home);
        
        ConfigChangeRequestSupportHelper.get(ctx).registerHomeForConfigSharing(serverCtx, home, LateFeeCreditCategoryExtension.class);
        
        return home;
    }

}
