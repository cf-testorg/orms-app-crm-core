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
import com.redknee.framework.xlog.log.PMLogMsg;

/**
 * A notification result callback object which logs PM.
 * 
 * @author cindy.wong@redknee.com
 * @since 8.3
 */
public class PMNotificationResultCallback extends
        NotificationResultCallbackProxy
{

    public PMNotificationResultCallback()
    {
        super();
    }

    public PMNotificationResultCallback(NotificationResultCallback delegate)
    {
        super(delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportAttempt(Context ctx)
    {
        PMLogMsg pm = new PMLogMsg(getDelegate().getClass().getSimpleName(),
                "reportAttempt()");
        try
        {
            super.reportAttempt(ctx);
        }
        finally
        {
            pm.log(ctx);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportFailure(Context ctx, boolean recoverable, Exception cause)
    {
        PMLogMsg pm = new PMLogMsg(getDelegate().getClass().getSimpleName(),
                "reportFailure(" + (recoverable ? "" : "non-")
                        + "recoverable/no-message)");
        try
        {
            super.reportFailure(ctx, recoverable, cause);
        }
        finally
        {
            pm.log(ctx);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportFailure(Context ctx, boolean recoverable, String msg,
            Exception cause)
    {
        PMLogMsg pm = new PMLogMsg(getDelegate().getClass().getSimpleName(),
                "reportFailure(" + (recoverable ? "" : "non-") + "recoverable)");
        try
        {
            super.reportFailure(ctx, recoverable, msg, cause);
        }
        finally
        {
            pm.log(ctx);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportFailure(Context ctx, boolean recoverable, String msg)
    {
        PMLogMsg pm = new PMLogMsg(getDelegate().getClass().getSimpleName(),
                "reportFailure(" + (recoverable ? "" : "non-")
                        + "recoverable/no-cause)");
        try
        {
            super.reportFailure(ctx, recoverable, msg);
        }
        finally
        {
            pm.log(ctx);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportSuccess(Context ctx)
    {
        PMLogMsg pm = new PMLogMsg(getDelegate().getClass().getSimpleName(),
                "reportSuccess()");
        try
        {
            super.reportSuccess(ctx);
        }
        finally
        {
            pm.log(ctx);
        }
    }
}
