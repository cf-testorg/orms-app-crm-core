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

import com.redknee.app.crm.bean.AuxiliaryService;
import com.redknee.app.crm.extension.ExtensionForeignKeyAdapter;
import com.redknee.app.crm.extension.ExtensionHandlingHome;
import com.redknee.app.crm.extension.auxiliaryservice.AuxiliaryServiceExtension;
import com.redknee.app.crm.extension.auxiliaryservice.AuxiliaryServiceExtensionXInfo;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.LRUCachingHome;
import com.redknee.framework.xhome.home.NoSelectAllHome;
import com.redknee.framework.xhome.home.ValidatingHome;

/**
 * A factory which creates the pipeline for <code>AuxiliaryServiceHome</code>.
 *
 * @author cindy.wong@redknee.com
 */
public class CoreAuxiliaryServiceHomePipelineFactory implements PipelineFactory
{
    /**
     * {@inheritDoc}
     */
    public Home createPipeline(final Context ctx, final Context serverContext)
    {
        Home home = new LRUCachingHome(ctx, AuxiliaryService.class, true, StorageSupportHelper.get(ctx).createHome(ctx, AuxiliaryService.class, "AUXILIARYSERVICE"));
        
        
        // Install a home to adapt between business logic bean and data bean
        home = new AdapterHome(
                ctx, 
                home, 
                new ExtendedBeanAdapter<com.redknee.app.crm.bean.AuxiliaryService, com.redknee.app.crm.bean.core.AuxiliaryService>(
                        com.redknee.app.crm.bean.AuxiliaryService.class, 
                        com.redknee.app.crm.bean.core.AuxiliaryService.class));
        
        home = new ExtensionHandlingHome<AuxiliaryServiceExtension>(
                ctx, 
                AuxiliaryServiceExtension.class, 
                AuxiliaryServiceExtensionXInfo.AUXILIARY_SERVICE_ID, 
                home);
        
        home = new AdapterHome(home, 
                new ExtensionForeignKeyAdapter(
                        AuxiliaryServiceExtensionXInfo.AUXILIARY_SERVICE_ID));

        home = new ValidatingHome(home);
        
        home = new NoSelectAllHome(home);
        
        return home;
    }
}
