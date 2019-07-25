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

/**
 * 
 *
 * @author victor.stratan@redknee.com
 */
public class ResourceDeviceConfig extends AbstractResourceDeviceConfig
{

    public boolean isMandatoryForSubscriptionType(final long subscriptionTypeID)
    {
        final ResourceDeviceConfigRow row = new ResourceDeviceConfigRow();
        row.setSubscriptionType(subscriptionTypeID);

        return this.getMandatoryForSubscriptionTypesList().contains(row);
    }

    public static boolean isMandatoryForSubscriptionType(final Context ctx, final long subscriptionTypeID)
    {
        final ResourceDeviceConfig config = (ResourceDeviceConfig) ctx.get(ResourceDeviceConfig.class);
        return config.isMandatoryForSubscriptionType(subscriptionTypeID);
    }
}
