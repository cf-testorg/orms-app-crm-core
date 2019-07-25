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
package com.redknee.app.crm.resource;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.msp.SpidAwareHome;
import com.redknee.framework.xhome.relationship.NoRelationshipRemoveHome;

import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.home.PipelineFactory;


/**
 * 
 *
 * @author victor.stratan@redknee.com
 */
public class ResourceDeviceGroupHomeFactory implements PipelineFactory
{
    /**
     *
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = StorageSupportHelper.get(ctx).createHome(ctx, ResourceDeviceGroup.class, "RESOURCEDEVICEGROUP");
        home = new NoRelationshipRemoveHome(ctx, ResourceDeviceGroupXInfo.ResourceDevicesRelationship,
                "This Resource Device Group is in use.  Cannot delete this Resource Device Group.", home);
        home = new SpidAwareHome(ctx, home);
        return home;
    }

}
