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

import com.redknee.framework.xhome.beans.XCloneable;
import com.redknee.framework.xhome.context.Context;

/**
 * A no-op Notification result callback object.
 * 
 * @author cindy.wong@redknee.com
 * @since 8.3
 */
public final class NullNotificationResultCallback implements
        NotificationResultCallback, XCloneable
{

    private NullNotificationResultCallback()
    {
        // no-op
    }

    public static NullNotificationResultCallback instance()
    {
        return instance;
    }

    /**
     * @see com.redknee.app.crm.notification.NotificationResultCallback#reportAttempt(com.redknee.framework.xhome.context.Context)
     */
    @Override
    public void reportAttempt(Context ctx)
    {
        // no-op
    }

    /**
     * @see com.redknee.app.crm.notification.NotificationResultCallback#reportFailure(com.redknee.framework.xhome.context.Context,
     *      boolean, java.lang.String)
     */
    @Override
    public void reportFailure(Context ctx, boolean recoverable, String msg)
    {
        // no-op
    }

    /**
     * @see com.redknee.app.crm.notification.NotificationResultCallback#reportFailure(com.redknee.framework.xhome.context.Context,
     *      boolean, java.lang.Exception)
     */
    @Override
    public void reportFailure(Context ctx, boolean recoverable, Exception cause)
    {
        // no-op
    }

    /**
     * @see com.redknee.app.crm.notification.NotificationResultCallback#reportFailure(com.redknee.framework.xhome.context.Context,
     *      boolean, java.lang.String, java.lang.Exception)
     */
    @Override
    public void reportFailure(Context ctx, boolean recoverable, String msg,
            Exception cause)
    {
        // no-op
    }

    /**
     * @see com.redknee.app.crm.notification.NotificationResultCallback#reportSuccess(com.redknee.framework.xhome.context.Context)
     */
    @Override
    public void reportSuccess(Context ctx)
    {
        // no-op
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    private static NullNotificationResultCallback instance = new NullNotificationResultCallback();
}
