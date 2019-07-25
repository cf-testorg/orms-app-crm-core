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

import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.ERLogMsg;

import com.redknee.app.crm.bean.core.ResourceDevice;
import com.redknee.app.crm.support.FrameworkSupport;
import com.redknee.app.crm.xhome.home.SimpleBeanERHome;

/**
 * 
 *
 * @author victor.stratan@redknee.com
 */
public class ResourceDeviceERLogHome extends SimpleBeanERHome
{
    public ResourceDeviceERLogHome(final Home delegate)
    {
        super(delegate, IDENTIFIER, CLASS, TITLE, FIELDS, ResourceDeviceXInfo.RESOURCE_ID);
    }

    private static final int IDENTIFIER = 1135;
    private static final int CLASS = 1100;
    private static final String TITLE = "Resource Device Update:1:0";

    private static final PropertyInfo[] FIELDS =
    {
        ResourceDeviceXInfo.GROUP_ID,
        ResourceDeviceXInfo.TECHNOLOGY,
        ResourceDeviceXInfo.DEFAULT_PACKAGE_ID,
        ResourceDeviceXInfo.SUBSCRIPTION_ID,
        ResourceDeviceXInfo.DEALER_CODE,
        ResourceDeviceXInfo.SERIAL_NUMBER,
        ResourceDeviceXInfo.IMEI,
        ResourceDeviceXInfo.MANUFACTURER,
        ResourceDeviceXInfo.MAKE,
        ResourceDeviceXInfo.MODEL,
        ResourceDeviceXInfo.STATE,
    };

    protected Object getOriginal(final Context context, final Object object) throws HomeException
    {
        final ResourceDevice bean = (ResourceDevice)object;
        final EQ criteria = new EQ(ResourceDeviceXInfo.RESOURCE_ID, bean.getResourceID());

        return super.find(context, criteria);
    }


}
