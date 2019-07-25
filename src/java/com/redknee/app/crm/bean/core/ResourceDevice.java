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
package com.redknee.app.crm.bean.core;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.resource.ResourceDeviceDefaultPackage;
import com.redknee.app.crm.resource.ResourceDeviceDefaultPackageXInfo;
import com.redknee.app.crm.resource.ResourceDeviceGroup;
import com.redknee.app.crm.resource.ResourceDeviceGroupXInfo;
import com.redknee.app.crm.resource.ResourceDeviceXInfo;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.util.snippet.log.Logger;

/**
 * 
 *
 * @author victor.stratan@redknee.com
 */
public class ResourceDevice extends com.redknee.app.crm.resource.ResourceDevice 
implements com.redknee.framework.xhome.context.ContextAware
{
    public static ResourceDevice getResourceDevice(final Context ctx, final String resourceID)
    {
        final EQ where = new EQ(ResourceDeviceXInfo.RESOURCE_ID, resourceID);
        try
        {
            return HomeSupportHelper.get(ctx).findBean(ctx, ResourceDevice.class, where);
        }
        catch (HomeException e)
        {
            Logger.minor(ctx, ResourceDevice.class, "Unable to retreive ResourceDevice ID=[" + resourceID + "]", e);
        }

        return null;
    }

    /**
     * Switch the assignment of a Resource to a new Subscription.
     *
     * @param ctx the operating Context
     * @param resourceID ID of the Resource to switch
     * @param oldSubscriptionId the ID of the Subscription that the Resource is assigned to now. Used in validation.
     * @param newSubscriptionId the ID of the Subscription to which the Resource should be reassigned.
     * @throws HomeException if validation fails
     */
    public static void switchSubscription(final Context ctx, final String resourceID, final String oldSubscriptionId,
            final String newSubscriptionId) throws HomeException
    {
        final ResourceDevice resource = ResourceDevice.getResourceDevice(ctx, resourceID);
        if (resource == null)
        {
            throw new HomeException("Resource Device [" + resourceID
                    + "] cannot be located. Switch request rejected.");
        }
        resource.switchSubscription(ctx, oldSubscriptionId, newSubscriptionId);
    }

    /**
     * Switch the assignment of a Resource to a new Subscription.
     *
     * @param ctx the operating Context
     * @param oldSubscriptionId the ID of the Subscription that the Resource is assigned to now. Used in validation.
     * @param newSubscriptionId the ID of the Subscription to which the Resource should be reassigned.
     * @throws HomeException if validation fails
     */
    public void switchSubscription(final Context ctx, final String oldSubscriptionId,
            final String newSubscriptionId) throws HomeException
    {
        if (this.getSubscriptionID() != null && !this.getSubscriptionID().equals(oldSubscriptionId))
        {
            throw new HomeException("Resource Device [" + this.getResourceID()
                    + "] does not belong to the specified Subscription [" + oldSubscriptionId
                    + "]. Switch to new Subscription [" + newSubscriptionId + "] rejected.");
        }
        
        if (newSubscriptionId == null)
        {
            throw new HomeException("Resource Device [" + this.getResourceID()
                    + "] assigned to Subscription [" + oldSubscriptionId
                    + "]. Cannot be switched to NULL Subscription. Switch request rejected.");
        }

        this.setSubscriptionID(newSubscriptionId);
        HomeSupportHelper.get(ctx).storeBean(ctx, this);
    }

    /**
     * @param ctx
     */
    public void setTechnologyFromGroup(final Context ctx)
    {
        if (getGroupID() != ResourceDevice.DEFAULT_GROUPID)
        {
            try
            {
                EQ where = new EQ(ResourceDeviceGroupXInfo.GROUP_ID, Long.valueOf(getGroupID()));
                ResourceDeviceGroup group = HomeSupportHelper.get(ctx).findBean(ctx, ResourceDeviceGroup.class, where);
                if (group != null)
                {
                    setTechnology(group.getTechnology());
                }
                else
                {
                    setTechnology(ResourceDevice.DEFAULT_TECHNOLOGY);
                }
            }
            catch (HomeException e)
            {
                Logger.minor(ctx, this, "Unable to set Technology from ResourceDeviceGroup ["
                        + getGroupID() + "]: " + e.getMessage(), e);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated use method with context parameter. this method is only for GUI
     */
    @Deprecated
    @Override
    public String getDefaultPackageID()
    {
        return getDefaultPackageID(getContext());
    }

    public String getDefaultPackageID(final Context ctx)
    {
        if (super.getDefaultPackageID() == null)
        {
            final EQ condition = new EQ(ResourceDeviceDefaultPackageXInfo.RESOURCE_ID, this.getResourceID());
            try
            {
                ResourceDeviceDefaultPackage link = null;
                if (ctx != null)
                {
                    link = HomeSupportHelper.get(ctx).findBean(ctx, ResourceDeviceDefaultPackage.class, condition);
                }
                if (link != null && link.getPackID() != null && link.getPackID().length() > 0)
                {
                    super.setDefaultPackageID(link.getPackID());
                }
                else
                {
                    super.setDefaultPackageID("");
                }
            }
            catch (HomeException e)
            {
                Logger.minor(ctx, this, "", e);
            }
        }

        return super.getDefaultPackageID();
    }

    @Override
    public void setDefaultPackageID(String defaultPackageID) throws IllegalArgumentException
    {
        super.setDefaultPackageID(defaultPackageID);
        defaultPackageIDset = true;
    }
    
    public boolean isDefaultPackageIDset()
    {
        return defaultPackageIDset;
    }

    /**
     * {@inheritDoc}
     */
    public Context getContext()
    {
        return context_;
    }

    /**
     * {@inheritDoc}
     */
    public void setContext(final Context context)
    {
        context_ = context;
    }

    private boolean defaultPackageIDset = false;

    protected transient Context context_;
}
