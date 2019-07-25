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

import java.io.PrintWriter;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.NotPredicate;
import com.redknee.framework.xhome.webcontrol.EnumIndexWebControl;
import com.redknee.framework.xhome.xenum.EnumCollection;

import com.redknee.app.crm.filter.EqualsPredicate;

/**
 * Need 2 different toWeb() outputs. No easy way to hot swap the Enum Collection, so hot swapping the WebControls.
 *
 * @author victor.stratan@redknee.com
 */
public class ResourceDeviceStateEnumIndexWebControl extends EnumIndexWebControl
{
    private final EnumCollection ALT_COLLECTION = ResourceDeviceStateEnum.COLLECTION.where(null,
            new NotPredicate(new EqualsPredicate(ResourceDeviceStateEnum.IN_USE)));
    private final EnumIndexWebControl altWebControl = new EnumIndexWebControl(ALT_COLLECTION);

    /**
     * Constructor with no parameters. We know what collections we'll use.
     */
    public ResourceDeviceStateEnumIndexWebControl()
    {
        super(ResourceDeviceStateEnum.COLLECTION);
    }

    /**
     * {@inheritDoc}
     */
    public void toWeb(final Context ctx, final PrintWriter out, final String name, final Object obj)
    {
        int  mode = ctx.getInt("MODE", DISPLAY_MODE);

        if (mode == CREATE_MODE || mode == EDIT_MODE)
        {
            altWebControl.toWeb(ctx, out, name, obj);
        }
        else
        {
            super.toWeb(ctx, out, name, obj);
        }
    }
}
