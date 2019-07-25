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
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;

/**
 * A notification result callback object which logs results.
 * 
 * @author cindy.wong@redknee.com
 * @since 8.3
 */
public class LoggingNotificationResultCallback extends
        NotificationResultCallbackProxy
{

    public LoggingNotificationResultCallback()
    {
        super();
    }

    public LoggingNotificationResultCallback(NotificationResultCallback delegate)
    {
        super(delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportAttempt(Context ctx)
    {
        if (LogSupport.isDebugEnabled(ctx))
        {
            new DebugLogMsg(this, "ENTER - reportAttempt()", null).log(ctx);
        }
        try
        {
            super.reportAttempt(ctx);
        }
        catch (RuntimeException e)
        {
            new MinorLogMsg(this, "ERROR - reportAttempt(): " + e.getMessage(),
                    e).log(ctx);
            throw e;
        }
        finally
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "EXIT - reportAttempt()", null).log(ctx);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportFailure(Context ctx, boolean recoverable, Exception cause)
    {
        if (LogSupport.isDebugEnabled(ctx))
        {
            new DebugLogMsg(this, "ENTER - reportFailure("
                    + (recoverable ? "" : "non-") + "recoverable, Cause="
                    + cause.getMessage() + ")", cause).log(ctx);
        }
        try
        {
            new MinorLogMsg(this, (recoverable ? "" : "Non-") + "Recoverable error occurred during message delivery: " + cause.getMessage(), cause).log(ctx);
            super.reportFailure(ctx, recoverable, cause);
        }
        catch (RuntimeException e)
        {
            new MinorLogMsg(this, "ERROR - reportFailure(): " + e.getMessage(),
                    e).log(ctx);
            throw e;
        }
        finally
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "EXIT - reportFailure()", null).log(ctx);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportFailure(Context ctx, boolean recoverable, String msg,
            Exception cause)
    {
        if (LogSupport.isDebugEnabled(ctx))
        {
            new DebugLogMsg(this, "ENTER - reportFailure("
                    + (recoverable ? "" : "non-") + "recoverable, Msg=" + msg
                    + ", Cause=" + cause.getMessage() + ")", null).log(ctx);
        }
        try
        {
            new MinorLogMsg(this, (recoverable ? "" : "Non-") + "Recoverable error occurred during message delivery: " + msg + ", Cause=" + cause.getMessage() + ")", cause).log(ctx);
            super.reportFailure(ctx, recoverable, msg, cause);
        }
        catch (RuntimeException e)
        {
            new MinorLogMsg(this, "ERROR - reportFailure(): " + e.getMessage(),
                    e).log(ctx);
            throw e;
        }
        finally
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "EXIT - reportFailure()", null).log(ctx);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportFailure(Context ctx, boolean recoverable, String msg)
    {
        if (LogSupport.isDebugEnabled(ctx))
        {
            new DebugLogMsg(this, "ENTER - reportFailure("
                    + (recoverable ? "" : "non-") + "recoverable, Msg=" + msg
                    + ")", null).log(ctx);
        }
        try
        {
            new MinorLogMsg(this, (recoverable ? "" : "Non-") + "Recoverable error occurred during message delivery: " + msg, null).log(ctx);
            super.reportFailure(ctx, recoverable, msg);
        }
        catch (RuntimeException e)
        {
            new MinorLogMsg(this, "ERROR - reportFailure(): " + e.getMessage(),
                    e).log(ctx);
            throw e;
        }
        finally
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "EXIT - reportFailure()", null).log(ctx);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportSuccess(Context ctx)
    {
        if (LogSupport.isDebugEnabled(ctx))
        {
            new DebugLogMsg(this, "ENTER - reportSuccess()", null).log(ctx);
        }
        try
        {
            super.reportSuccess(ctx);
        }
        catch (RuntimeException e)
        {
            new MinorLogMsg(this, "ERROR - reportSuccess(): " + e.getMessage(),
                    e).log(ctx);
            throw e;
        }
        finally
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "EXIT - reportSuccess()", null).log(ctx);
            }
        }
    }
}
