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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;

import com.redknee.app.crm.bean.core.ResourceDevice;
import com.redknee.util.snippet.log.Logger;

/**
 * Create and update default package link bean.
 *
 * @author victor.stratan@redknee.com
 */
public class ResourceDeviceDefaultPackageSaveHome extends HomeProxy
{
    /**
     * For serialization. Although Serializing HomeProxies is a bad idea. 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor accepts delegate Home parameter.
     *
     * @param home delegate Home
     */
    public ResourceDeviceDefaultPackageSaveHome(Home home)
    {
        super(home);
    }

    @Override
    public Object create(final Context ctx, final Object obj) throws HomeException
    {
        final ResourceDevice request = (ResourceDevice) obj;
        final ResourceDevice result = (ResourceDevice) super.create(ctx, obj);

        if (request.isDefaultPackageIDset()
                && request.getDefaultPackageID(ctx) != null
                && request.getDefaultPackageID(ctx).length() > 0)
        {
            final ResourceDeviceDefaultPackage link = new ResourceDeviceDefaultPackage();
            link.setResourceID(result.getResourceID());
            link.setPackID(request.getDefaultPackageID(ctx));
            link.setPackTechnology(request.getTechnology());
            
            
            final Home linkHome = (Home) ctx.get(ResourceDeviceDefaultPackageHome.class);
            
            try
            {
                linkHome.create(ctx, link);
            }
            catch (HomeException e)
            {
                Logger.minor(ctx, this, "Unable to create ResourceDeviceDefaultPackage bean."
                        + " Rolling back creation of ResourceDevice [" + result.getResourceID() + "]", e);
                super.remove(ctx, new EQ(ResourceDeviceXInfo.RESOURCE_ID, result.getResourceID()));
                throw e;
            }
        }
        return result;
    }

    @Override
    public Object store(final Context ctx, final Object obj) throws HomeException
    {
        final ResourceDevice request = (ResourceDevice) obj;
        ResourceDevice oldResource = null;
        if (request.isDefaultPackageIDset())
        {
            final EQ condition = new EQ(ResourceDeviceXInfo.RESOURCE_ID, request.getResourceID());
            oldResource = (ResourceDevice) super.find(ctx, condition);
        }

        final ResourceDevice result = (ResourceDevice) super.store(ctx, obj);

        if (request.isDefaultPackageIDset())
        {
            final Home linkHome = (Home) ctx.get(ResourceDeviceDefaultPackageHome.class);

            final ResourceDeviceDefaultPackage oldLink = new ResourceDeviceDefaultPackage();
            oldLink.setResourceID(oldResource.getResourceID());
            oldLink.setPackID(oldResource.getDefaultPackageID(ctx));
            oldLink.setPackTechnology(oldResource.getTechnology());

            try
            {
                linkHome.remove(ctx, oldLink);
            }
            catch (HomeException e)
            {
                Logger.minor(ctx, this, "Unable to update ResourceDeviceDefaultPackage bean."
                        + " Unable to Rollong back update of ResourceDevice [" + result.getResourceID() + "]", e);
                throw e;
            }

            final ResourceDeviceDefaultPackage link = new ResourceDeviceDefaultPackage();
            link.setResourceID(result.getResourceID());
            link.setPackID(request.getDefaultPackageID(ctx));
            link.setPackTechnology(request.getTechnology());

            try
            {
                linkHome.create(ctx, link);
            }
            catch (HomeException e)
            {
                Logger.minor(ctx, this, "Unable to update ResourceDeviceDefaultPackage bean."
                        + " Unable to Rollong back update of ResourceDevice [" + result.getResourceID() + "]", e);
                throw e;
            }
        }

        return result;
    }

}
