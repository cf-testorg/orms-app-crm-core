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
package com.redknee.app.crm.web.control;

import java.io.PrintWriter;
import java.util.Collection;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.core.ResourceDevice;
import com.redknee.app.crm.resource.ResourceDeviceGroup;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.util.snippet.log.Logger;


/**
 * WebControl decorator that sets the technology
 *
 * @author victor.stratan@redknee.com
 */
public class SetTechnologyFromGroupWebControl extends ProxyWebControl
{
    /**
     * @param delegate
     */
    public SetTechnologyFromGroupWebControl(WebControl delegate)
    {
        super(delegate);
    }

    @Override
    public void toWeb(Context ctx, PrintWriter p1, String p2, Object p3)
    {
        final ResourceDevice device = (ResourceDevice) ctx.get(AbstractWebControl.BEAN);
        if (device.getGroupID() == ResourceDevice.DEFAULT_GROUPID)
        {
            try
            {
                Collection<ResourceDeviceGroup> groups = HomeSupportHelper.get(ctx).getBeans(ctx, ResourceDeviceGroup.class);
                if (groups != null && groups.size() > 0)
                {
                    ResourceDeviceGroup group = groups.iterator().next();
                    device.setGroupID(group.getGroupID());

                    device.setTechnologyFromGroup(ctx);
                }
            }
            catch (Exception e)
            {
                Logger.minor(ctx, this, "Unable to retrieve ResourceDeviceGroup: " + e.getMessage(), e);
            }
        }
        else
        {
            device.setTechnologyFromGroup(ctx);
        }


        super.toWeb(ctx, p1, p2, Integer.valueOf(device.getTechnology()));
    }

}
