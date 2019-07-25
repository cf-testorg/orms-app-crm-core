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

import com.redknee.app.crm.bean.core.ResourceDevice;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;


/**
 * Create and update default package link bean.
 * 
 * @author simar.singh@redknee.com
 */
public class ResourceDeviceTechnologyGroupSettingHome extends HomeProxy
{
    
    public ResourceDeviceTechnologyGroupSettingHome(Context ctx, Home delegage)
    {
        super(ctx,delegage);
    }

    /**
     * For serialization. Although Serializing HomeProxies is a bad idea.
     */
    private static final long serialVersionUID = 1L;


    /**
     * Set the technology automatically derived from Device Group of the Resource Device 
     */
    @Override
    public Object create(final Context ctx, final Object obj) throws HomeException
    {
        final ResourceDevice resourceDevice = (ResourceDevice) obj;
        resourceDevice.setTechnologyFromGroup(ctx);
        return super.create(ctx, obj);
    }
}
