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
import com.redknee.framework.xhome.home.NoSelectAllHome;
import com.redknee.framework.xhome.home.ValidatingHome;

import com.redknee.app.crm.bean.AlcatelSSCPropertyXInfo;
import com.redknee.app.crm.bean.KeyValueEntryHome;
import com.redknee.app.crm.extension.ExtensionInstallationHome;
import com.redknee.app.crm.extension.service.AlcatelSSCServiceExtensionXInfo;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;
import com.redknee.app.crm.xhome.home.ContextRedirectingHome;
import com.redknee.app.crm.xhome.home.KeyValueEntryFlatteningHome;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CoreAlcatelSSCServiceExtensionHomePipelineFactory implements PipelineFactory
{
    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = new ContextRedirectingHome(ctx, KeyValueEntryHome.class);
        
        home = new KeyValueEntryFlatteningHome(
                ctx, 
                home,
                AlcatelSSCServiceExtensionXInfo.KEY_VALUE_PAIRS,
                AlcatelSSCPropertyXInfo.KEY,
                AlcatelSSCPropertyXInfo.VALUE);
        
        home = new AdapterHome(ctx, 
                new ExtendedBeanAdapter<com.redknee.app.crm.extension.service.AlcatelSSCServiceExtension, com.redknee.app.crm.extension.service.core.AlcatelSSCServiceExtension>(
                        com.redknee.app.crm.extension.service.AlcatelSSCServiceExtension.class, 
                        com.redknee.app.crm.extension.service.core.AlcatelSSCServiceExtension.class), 
                        home);

        home = new ExtensionInstallationHome(ctx, home);
        
        home = new ValidatingHome(home);
        
        home = new NoSelectAllHome(home);
        
        return home;
    }

}
