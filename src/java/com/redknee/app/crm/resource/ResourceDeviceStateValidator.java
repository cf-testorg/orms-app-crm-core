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

import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.exception.RethrowExceptionListener;

/**
 * 
 *
 * @author victor.stratan@redknee.com
 */
public class ResourceDeviceStateValidator implements Validator
{
    /**
     *
     * {@inheritDoc}
     */
    public void validate(final Context ctx, final Object obj) throws IllegalStateException
    {
        final ResourceDevice resource = (ResourceDevice) obj;
        final RethrowExceptionListener exceptions = new RethrowExceptionListener();
        
        if (resource.getState() != ResourceDeviceStateEnum.IN_USE_INDEX)
        {
            if (resource.getSubscriptionID() != null && resource.getSubscriptionID().length() > 0)
            {
                exceptions.thrown(new IllegalPropertyArgumentException(ResourceDeviceXInfo.SUBSCRIPTION_ID,
                        "Resource Device Subscription ID can be set only when state is "
                        + ResourceDeviceStateEnum.IN_USE.getDescription(ctx)));
            }
        }
        else
        {
            if (resource.getSubscriptionID() == null || resource.getSubscriptionID().length() == 0)
            {
                exceptions.thrown(new IllegalPropertyArgumentException(ResourceDeviceXInfo.STATE,
                        "Resource Device Subscription ID has to be when state is "
                        + ResourceDeviceStateEnum.IN_USE.getDescription(ctx)));
            }
        }

        exceptions.throwAllAsCompoundException();
    }
}
