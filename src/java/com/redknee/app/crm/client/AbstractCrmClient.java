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
package com.redknee.app.crm.client;

import com.redknee.framework.xhome.context.Context;

/**
 * Deal with common chores for all clients.
 *
 * @author ray.chen@redknee.com
 */
public abstract class AbstractCrmClient<T> extends AbstractExtendedCrmClient<T, T>
{
    public AbstractCrmClient(final Context ctx, final String name, final String desc, final Class<T> serviceType,
            final int trapId, final int clearId)
    {
        super(ctx, name, desc, serviceType, trapId, clearId);
    }

    public AbstractCrmClient(final Context ctx, final String name, final String desc, final Class<T> serviceType)
    {
        super(ctx, name, desc, serviceType);
    }

    protected AbstractCrmClient(final Context ctx, final Class<T> serviceType,
            final RemoteServiceStatusImpl<T> serviceStatus)
    {
        super(ctx, serviceType, serviceStatus);
    }
}
