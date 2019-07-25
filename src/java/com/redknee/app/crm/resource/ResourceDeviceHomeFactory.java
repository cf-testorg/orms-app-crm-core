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
package com.redknee.app.crm.resource;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.framework.xhome.beans.CompoundValidator;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.ContextualizingHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.ValidatingHome;
import com.redknee.framework.xhome.msp.SpidAwareHome;

import com.redknee.app.crm.bean.core.ResourceDevice;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.technology.TechnologyAwareHome;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;

/**
 * 
 *
 * @author victor.stratan@redknee.com
 */
public class ResourceDeviceHomeFactory implements PipelineFactory
{
    /**
     *
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = com.redknee.app.crm.support.StorageSupportHelper.get(ctx).createHome(ctx, ResourceDevice.class, "RESOURCEDEVICE");

        // Install a home to adapt between business logic bean and data bean
        home = new AdapterHome(
                ctx, 
                home, 
                new ExtendedBeanAdapter<com.redknee.app.crm.resource.ResourceDevice, com.redknee.app.crm.bean.core.ResourceDevice>(
                        com.redknee.app.crm.resource.ResourceDevice.class, 
                        com.redknee.app.crm.bean.core.ResourceDevice.class));

        
        home = new ResourceDeviceDefaultPackageSaveHome(home);
        home = new ResourceDeviceERLogHome(home);

        
        home = new ResourceDeviceRemoveValidationHome(home);
        
        final CompoundValidator validator = new CompoundValidator();
        validator.add(new ResourceDeviceStateValidator());
        validator.add(new ResourceDeviceDealerCodeValidator());
        home = new ValidatingHome(validator, home);
        home = new ResourceDeviceTechnologyGroupSettingHome(ctx,home);
        home = new TechnologyAwareHome(ctx, home);
        home = new SpidAwareHome(ctx, home);
        home = new ContextualizingHome(ctx, home);
        return home;
    }

}
