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
import com.redknee.framework.xhome.context.ContextAware;

import com.redknee.util.snippet.log.Logger;

/**
 * Deal with common chores for all clients.
 *
 * @author ray.chen@redknee.com
 */
public abstract class AbstractExtendedCrmClient<S, T extends S> implements ContextAware
{
    private final RemoteServiceStatusImpl<T> serviceStatus_;
    private final Class<T>                   serviceType_;
    private final Context                    ctx_;

    protected static final int SUCCESS = 0;
    protected static final int FAILED = -1;
    protected static final int COMMUNICATION_FAILURE = 301;

    public AbstractExtendedCrmClient(final Context ctx, final String name, final String desc, final Class<T> serviceType,
            final int trapId, final int clearId)
    {
        this(ctx, serviceType, new RemoteServiceStatusImpl<T>(ctx, name, desc, serviceType, trapId, clearId));
    }

    public AbstractExtendedCrmClient(final Context ctx, final String name, final String desc, final Class<T> serviceType)
    {
        this(ctx, serviceType, new RemoteServiceStatusImpl<T>(ctx, name, desc, serviceType));
    }

    protected AbstractExtendedCrmClient(final Context ctx, final Class<T> serviceType,
            final RemoteServiceStatusImpl<T> serviceStatus)
    {
        ctx_ = ctx;
        serviceType_ = serviceType;
        serviceStatus_ = serviceStatus;
    }

    protected RemoteServiceStatus getServiceStatus()
    {
        return serviceStatus_;
    }

    protected int checkServiceAvailability()
    {
        final int ret = getServiceStatus().isAlive() ? SUCCESS : FAILED;

        if (ret == FAILED)
        {
            Logger.minor(getContext(), this, serviceType_.getName() + " is not available.");
        }

        return ret;
    }


    @SuppressWarnings("unchecked")
    protected S getClient(final Context ctx) throws RemoteServiceException
    {
        final Object client = ctx.get(serviceType_);

        if (client == null || !serviceType_.isInstance(client))
        {
            throw new RemoteServiceException(FAILED, "Failure: unable to get the client instance of remote service.");
        }

        return (S) client;
    }


    // this method is semantically SAME with getClient(), but provided to support the old
    // CORBA clients that always validate service references by checking null.
    @SuppressWarnings("unchecked")
    protected S getService()
    {
        final Object client = getContext().get(serviceType_);

        if (client == null || !serviceType_.isInstance(client))
        {
            Logger.major(getContext(), this, "Failure: unable to get the client instance of remote service.");
            return null;
        }

        return (S) client;
    }


    @Override
    public Context getContext()
    {
        return ctx_;
    }


    @Override
    public void setContext(final Context ctx)
    {
        throw new UnsupportedOperationException("Context cannot be specified in this class.");
    }

}
