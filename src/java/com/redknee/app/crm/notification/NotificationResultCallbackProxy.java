/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily
 * available. Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee. No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used in
 * accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */

package com.redknee.app.crm.notification;

import com.redknee.framework.xhome.context.Context;

/**
 * @author cindy.wong@redknee.com
 * @since 8.3
 */
public class NotificationResultCallbackProxy extends AbstractNotificationResultCallbackProxy
{
    public NotificationResultCallbackProxy()
    {
        super();
    }

    public NotificationResultCallbackProxy(NotificationResultCallback delegate)
    {
        super();
        setDelegate(delegate);
    }

    /**
     * @see com.redknee.app.crm.notification.NotificationResultCallback#reportAttempt(com.redknee.framework.xhome.context.Context)
     */
    @Override
    public void reportAttempt(Context ctx)
    {
        getDelegate().reportAttempt(ctx);
    }

    /**
     * @see com.redknee.app.crm.notification.NotificationResultCallback#reportFailure(com.redknee.framework.xhome.context.Context,
     *      boolean, java.lang.String)
     */
    @Override
    public void reportFailure(Context ctx, boolean recoverable, String msg)
    {
        getDelegate().reportFailure(ctx, recoverable, msg);
    }

    /**
     * @see com.redknee.app.crm.notification.NotificationResultCallback#reportFailure(com.redknee.framework.xhome.context.Context,
     *      boolean, java.lang.Exception)
     */
    @Override
    public void reportFailure(Context ctx, boolean recoverable, Exception cause)
    {
        getDelegate().reportFailure(ctx, recoverable, cause);
    }

    /**
     * @see com.redknee.app.crm.notification.NotificationResultCallback#reportFailure(com.redknee.framework.xhome.context.Context,
     *      boolean, java.lang.String, java.lang.Exception)
     */
    @Override
    public void reportFailure(Context ctx, boolean recoverable, String msg,
            Exception cause)
    {
        getDelegate().reportFailure(ctx, recoverable, msg, cause);
    }

    /**
     * @see com.redknee.app.crm.notification.NotificationResultCallback#reportSuccess(com.redknee.framework.xhome.context.Context)
     */
    @Override
    public void reportSuccess(Context ctx)
    {
        getDelegate().reportSuccess(ctx);
    }


    /** Inserts a NotificationResultCallbackProxy before the current delegate. **/
    public void addProxy(Context ctx, NotificationResultCallbackProxy proxy)
    {
        proxy.setDelegate(getDelegate());

        setDelegate(proxy);
    }


    /** Inserts a NotificationResultCallbackProxy before the last non NotificationResultCallbackProxy. **/
    public void appendProxy(Context ctx, NotificationResultCallbackProxy proxy)
    {
        if (getDelegate() instanceof NotificationResultCallbackProxy)
        {
            NotificationResultCallbackProxy delegate = (NotificationResultCallbackProxy) getDelegate();

            delegate.appendProxy(ctx, proxy);
        }
        else
        {
            addProxy(ctx, proxy);
        }
    }

    public final NotificationResultCallback findDecorator(Class cls)
    {
        if (cls.isInstance(this))
        {
            return this;
        }

        if (getDelegate() instanceof NotificationResultCallbackProxy)
        {
            return ((NotificationResultCallbackProxy) getDelegate())
                    .findDecorator(cls);
        }

        if (cls.isInstance(getDelegate()))
        {
            return getDelegate();
        }

        return null;
    }

    public final boolean hasDecorator(Class cls)
    {
        return findDecorator(cls) != null;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "(" + delegate_ + ")";
    }
}
