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
package com.redknee.app.crm.home;

import com.redknee.app.crm.access.control.PermissionAware;
import com.redknee.app.crm.bean.IdentifierAware;
import com.redknee.framework.auth.permission.PermissionInfo;
import com.redknee.framework.auth.permission.PermissionInfoHome;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.support.IdentitySupport;
import com.redknee.framework.xlog.log.DebugLogMsg;


/**
 * Sets the adjustment type's permission.
 * 
 * @author simar.singh@redknee.com
 * @since 8.5
 */
public class PermissionAwarePermissionSettingHome extends HomeProxy
{

    private static final long serialVersionUID = 1L;


    public PermissionAwarePermissionSettingHome(Home home)
    {
        super(home);
    }


    @Override
    public Object create(Context ctx, Object obj) throws HomeInternalException, HomeException
    {
        XInfo xInfo = (XInfo) XBeans.getInstanceOf(ctx, obj.getClass(), XInfo.class);
        Object defaultValue = xInfo.getID().getDefault();
        Object currentValue = xInfo.getID().get(obj);
        
        if (!currentValue.equals(defaultValue))
        {
            PermissionAware permissionAware = (PermissionAware) obj;
            addPermissionInfo(ctx, permissionAware);
            return getDelegate().create(ctx, permissionAware);
        }
        else
        {
            PermissionAware permissionAware = (PermissionAware) getDelegate().create(ctx, obj);
            addPermissionInfo(ctx, permissionAware);
            return getDelegate().store(ctx, permissionAware);
        }
    }


    /**
     * Adds a new permission to PermissionInfoHome.
     * 
     * @param ctx
     *            Operating context.
     * @param permission
     *            Permission to add.
     * @throws HomeInternalException
     *             Thrown by PermissionInfoHome.
     * @throws HomeException
     *             Thrown by PermissionInfoHome.
     */
    private void addPermissionInfo(Context ctx, PermissionAware permissionAware) throws HomeInternalException, HomeException
    {
        String permission = permissionAware.getRootPermission();
        permissionAware.setPermission(permission);

        Home home = (Home) ctx.get(PermissionInfoHome.class);
        PermissionInfo info = new PermissionInfo();
        info.setName(permission);
        info.setEnabled(true);
        try
        {
            home.create(ctx, info);
        }
        catch (HomeException e)
        {
            new DebugLogMsg(this, "Tried creating the permission but failed, will try a store operation instead", e)
                    .log(ctx);
            home.store(ctx, info);
        }
    }
}
