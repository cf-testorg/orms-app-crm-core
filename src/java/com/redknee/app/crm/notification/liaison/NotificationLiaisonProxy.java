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
package com.redknee.app.crm.notification.liaison;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.io.OutputStreamFactory;
import com.redknee.app.crm.notification.EmailAddresses;
import com.redknee.app.crm.notification.RecipientInfo;
import com.redknee.app.crm.notification.generator.MessageGenerationException;
import com.redknee.app.crm.notification.message.BinaryNotificationMessage;
import com.redknee.app.crm.notification.message.EmailNotificationMessage;
import com.redknee.app.crm.notification.message.NotificationMessage;
import com.redknee.app.crm.notification.message.SmsNotificationMessage;
import com.redknee.app.crm.notification.template.NotificationTemplate;


/**
 * Standard proxy implementation that forwards requests to a delegate.
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class NotificationLiaisonProxy extends AbstractNotificationLiaisonProxy
{

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendNotification(Context ctx, NotificationTemplate template, RecipientInfo destination, KeyValueFeatureEnum... features)
    {
        getDelegate().sendNotification(ctx, template, destination, features);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void sendNotification(Context ctx, Class<SmsNotificationMessage> type, NotificationTemplate template,
            String destination, KeyValueFeatureEnum... features)
    {
        getDelegate().sendNotification(ctx, type, template, destination, features);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void sendNotification(Context ctx, Class<BinaryNotificationMessage> type, NotificationTemplate template,
            OutputStreamFactory destination, KeyValueFeatureEnum... features)
    {
        getDelegate().sendNotification(ctx, type, template, destination, features);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void sendNotification(Context ctx, Class<EmailNotificationMessage> type, NotificationTemplate template,
            EmailAddresses destination, KeyValueFeatureEnum... features)
    {
        getDelegate().sendNotification(ctx, type, template, destination, features);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected NotificationMessage generateMessage(Context ctx, Class<? extends NotificationMessage> type,
            NotificationTemplate template, KeyValueFeatureEnum... features) throws MessageGenerationException
    {
        NotificationLiaison delegate = getDelegate();
        if (delegate instanceof AbstractNotificationLiaison)
        {
            ((AbstractNotificationLiaison) delegate).generateMessage(ctx, type, template, features);
        }
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void sendMessage(Context ctx, NotificationMessage msg, RecipientInfo destination)
    {
        NotificationLiaison delegate = getDelegate();
        if (delegate instanceof AbstractNotificationLiaison)
        {
            ((AbstractNotificationLiaison) delegate).sendMessage(ctx, msg, destination);
        }
    }


    /** Inserts a NotificationLiaisonProxy before the current delegate. **/
    public void addProxy(Context ctx, NotificationLiaisonProxy proxy)
    {
        proxy.setDelegate(getDelegate());

        setDelegate(proxy);
    }


    /** Inserts a NotificationLiaisonProxy before the last non NotificationLiaisonProxy. **/
    public void appendProxy(Context ctx, NotificationLiaisonProxy proxy)
    {
        if (getDelegate() instanceof NotificationLiaisonProxy)
        {
            NotificationLiaisonProxy delegate = (NotificationLiaisonProxy) getDelegate();

            delegate.appendProxy(ctx, proxy);
        }
        else
        {
            addProxy(ctx, proxy);
        }
    }


    /** Find the first instance of cls in the Decorator chain. **/
    public NotificationLiaison findDecorator(Class cls)
    {
        if (cls.isInstance(this))
        {
            return this;
        }
        NotificationLiaison delegate = getDelegate();
        if (delegate instanceof NotificationLiaisonProxy)
        {
            return ((NotificationLiaisonProxy) delegate).findDecorator(cls);
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
