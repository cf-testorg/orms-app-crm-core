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

import com.redknee.app.crm.notification.NotificationResultCallback;
import com.redknee.app.crm.notification.RecipientInfo;
import com.redknee.app.crm.notification.message.NotificationMessage;


/**
 * Standard proxy implementation that forwards requests to a delegate.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class MessageDeliveryServiceProxy extends AbstractMessageDeliveryServiceProxy
{

    /**
     * {@inheritDoc}
     */
    public void sendMessage(Context ctx, RecipientInfo recipient, NotificationMessage msg,
            NotificationResultCallback callback)
    {
        getDelegate().sendMessage(ctx, recipient, msg, callback);
    }


    /** Inserts a MessageDeliveryServiceProxy before the current delegate. **/
    public void addProxy(Context ctx, MessageDeliveryServiceProxy proxy)
    {
        proxy.setDelegate(getDelegate());

        setDelegate(proxy);
    }


    /** Inserts a MessageDeliveryServiceProxy before the last non MessageDeliveryServiceProxy. **/
    public void appendProxy(Context ctx, MessageDeliveryServiceProxy proxy)
    {
        if (getDelegate() instanceof MessageDeliveryServiceProxy)
        {
            MessageDeliveryServiceProxy delegate = (MessageDeliveryServiceProxy) getDelegate();

            delegate.appendProxy(ctx, proxy);
        }
        else
        {
            addProxy(ctx, proxy);
        }
    }


    /** Find the first instance of cls in the Decorator chain. **/
    public MessageDeliveryService findDecorator(Class cls)
    {
        if (cls.isInstance(this))
        {
            return this;
        }
        MessageDeliveryService delegate = getDelegate();
        if (delegate instanceof MessageDeliveryServiceProxy)
        {
            return ((MessageDeliveryServiceProxy) delegate).findDecorator(cls);
        }

        return cls.isInstance(delegate) ? delegate : null;
    }


    public boolean hasDecorator(Class cls)
    {
        return findDecorator(cls) != null;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "(" + delegate_ + ")";
    }
}
