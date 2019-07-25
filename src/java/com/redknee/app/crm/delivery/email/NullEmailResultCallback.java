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
package com.redknee.app.crm.delivery.email;

import com.redknee.framework.xhome.context.Context;


/**
 * Basic 'null' implementation
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class NullEmailResultCallback implements EmailResultCallback
{
    private static NullEmailResultCallback instance_ = null;
    public static NullEmailResultCallback instance()
    {
        if (instance_ == null)
        {
            instance_ = new NullEmailResultCallback();
        }
        return instance_;
    }
    
    protected NullEmailResultCallback()
    {
        
    }

    /**
     * {@inheritDoc}
     */
    public void reportAttempt(Context ctx)
    {
    }

    /**
     * {@inheritDoc}
     */
    public void reportFailure(Context ctx, boolean recoverable, String msg)
    {
    }

    /**
     * {@inheritDoc}
     */
    public void reportFailure(Context ctx, boolean recoverable, Exception cause)
    {
    }

    /**
     * {@inheritDoc}
     */
    public void reportFailure(Context ctx, boolean recoverable, String msg, Exception cause)
    {
    }

    /**
     * {@inheritDoc}
     */
    public void reportSuccess(Context ctx)
    {
    }

}
