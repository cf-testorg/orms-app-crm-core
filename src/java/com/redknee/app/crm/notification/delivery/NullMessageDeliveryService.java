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

import com.redknee.framework.xhome.beans.XCloneable;
import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.notification.NotificationResultCallback;
import com.redknee.app.crm.notification.RecipientInfo;
import com.redknee.app.crm.notification.message.NotificationMessage;


/**
 * This delivery service does nothing.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class NullMessageDeliveryService implements MessageDeliveryService, XCloneable
{
    protected static MessageDeliveryService instance_ = null;

    public static MessageDeliveryService instance()
    {
        if (instance_ == null)
        {
            instance_ = new NullMessageDeliveryService();
        }
        return instance_;
    }
    
    public NullMessageDeliveryService()
    {
    }
    

    /**
     * {@inheritDoc}
     */
    public void sendMessage(Context ctx, RecipientInfo recipient, NotificationMessage msg,
            NotificationResultCallback callback)
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

}
