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
package com.redknee.app.crm.notification.generator;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.notification.message.NotificationMessage;
import com.redknee.app.crm.notification.template.NotificationTemplate;


/**
 * Standard proxy implementation that forwards requests to a delegate.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class MessageGeneratorProxy extends AbstractMessageGeneratorProxy
{

    /**
     * {@inheritDoc}
     */
    public NotificationMessage generate(Context ctx, NotificationTemplate template, KeyValueFeatureEnum... features) throws MessageGenerationException
    {
        return getDelegate().generate(ctx, template, features);
    }


    /** Inserts a MessageGeneratorProxy before the current delegate. **/
    public void addProxy(Context ctx, MessageGeneratorProxy proxy)
    {
        proxy.setDelegate(getDelegate());

        setDelegate(proxy);
    }


    /** Inserts a MessageGeneratorProxy before the last non MessageGeneratorProxy. **/
    public void appendProxy(Context ctx, MessageGeneratorProxy proxy)
    {
        if (getDelegate() instanceof MessageGeneratorProxy)
        {
            MessageGeneratorProxy delegate = (MessageGeneratorProxy) getDelegate();

            delegate.appendProxy(ctx, proxy);
        }
        else
        {
            addProxy(ctx, proxy);
        }
    }


    /** Find the first instance of cls in the Decorator chain. **/
    public MessageGenerator findDecorator(Class cls)
    {
        if (cls.isInstance(this))
        {
            return this;
        }
        MessageGenerator delegate = getDelegate();
        if (delegate instanceof MessageGeneratorProxy)
        {
            return ((MessageGeneratorProxy) delegate).findDecorator(cls);
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
