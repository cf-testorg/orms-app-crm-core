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
package com.redknee.app.crm.notification.delivery;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.pipe.Throttle;
import com.redknee.framework.xhome.pipe.ThrottleInfo;

import com.redknee.app.crm.notification.NotificationResultCallback;
import com.redknee.app.crm.notification.RecipientInfo;
import com.redknee.app.crm.notification.message.NotificationMessage;

/**
 * This proxy service applies a send rate limit on it's delegate delivery service.
 * 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class ThrottledDeliveryService extends AbstractThrottledDeliveryService
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(Context ctx, RecipientInfo recipient, NotificationMessage msg,
            NotificationResultCallback callback)
    {
        getThrottleObject().sleep();
        super.sendMessage(ctx, recipient, msg, callback);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setThrottle(ThrottleInfo throttle) throws IllegalArgumentException
    {
        super.setThrottle(throttle);
        
        synchronized (getThrottleLock())
        {
            throttleObj_ = null;
        }
    }

    
    protected Throttle getThrottleObject()
    {
        synchronized (getThrottleLock())
        {
            if (throttleObj_ == null)
            {
                throttleObj_ = new Throttle(getThrottle());
            }
        }
        return throttleObj_;
    }
    
    protected synchronized Object getThrottleLock()
    {
        // This null check is required because the super-class constructor calls
        // setThrottle() before throttleLock_ variable is initialized.
        if (throttleLock_ == null)
        {
            return this;
        }
        return throttleLock_;
    }

    protected Object throttleLock_ = new Object();
    protected Throttle throttleObj_ = null;
}
