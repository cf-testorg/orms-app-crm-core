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

import com.redknee.framework.xhome.beans.XCloneable;
import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.io.OutputStreamFactory;
import com.redknee.app.crm.notification.EmailAddresses;
import com.redknee.app.crm.notification.RecipientInfo;
import com.redknee.app.crm.notification.message.BinaryNotificationMessage;
import com.redknee.app.crm.notification.message.EmailNotificationMessage;
import com.redknee.app.crm.notification.message.SmsNotificationMessage;
import com.redknee.app.crm.notification.template.NotificationTemplate;


/**
 * This liaison implementation does nothing.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class NullNotificationLiaison implements NotificationLiaison, XCloneable
{
    protected static NotificationLiaison instance_ = null;

    public static NotificationLiaison instance()
    {
        if (instance_ == null)
        {
            instance_ = new NullNotificationLiaison();
        }
        return instance_;
    }
    
    public NullNotificationLiaison()
    {
    }

    /**
     * {@inheritDoc}
     */
    public void sendNotification(Context ctx, NotificationTemplate template, RecipientInfo destination, KeyValueFeatureEnum... features)
    {
    }


    /**
     * {@inheritDoc}
     */
    public void sendNotification(Context ctx, Class<SmsNotificationMessage> type, NotificationTemplate template,
            String destination, KeyValueFeatureEnum... features)
    {
    }


    /**
     * {@inheritDoc}
     */
    public void sendNotification(Context ctx, Class<BinaryNotificationMessage> type, NotificationTemplate template,
            OutputStreamFactory destination, KeyValueFeatureEnum... features)
    {
    }


    /**
     * {@inheritDoc}
     */
    public void sendNotification(Context ctx, Class<EmailNotificationMessage> type, NotificationTemplate template,
            EmailAddresses destination, KeyValueFeatureEnum... features)
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
