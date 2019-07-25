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
import com.redknee.app.crm.notification.message.BinaryNotificationMessage;
import com.redknee.app.crm.notification.message.EmailNotificationMessage;
import com.redknee.app.crm.notification.message.SmsNotificationMessage;
import com.redknee.app.crm.notification.template.NotificationTemplate;

/**
 * The Liaison's job is to create messages on behalf of the caller by invoking the
 * appropriate message generator with the appropriate parameters. Upon successful message
 * generation, it submits the message to the appropriate delivery service. It is
 * responsible for choosing the correct type of delivery service depending on the message.
 * 
 * The implementation of the delivery service determines the timing of message generation
 * and the timing of message delivery. From an interface perspective, there are no
 * guarantees that either event happens immediately. Feature-specific liaisons may be
 * required to satisfy ER logging requirements or to implement some custom logic to ensure
 * that special message generators or delivery services are used.
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public interface NotificationLiaison
{
    public void sendNotification(Context ctx, NotificationTemplate template, RecipientInfo destination, KeyValueFeatureEnum... features);
    public void sendNotification(Context ctx, Class<SmsNotificationMessage> type, NotificationTemplate template, String destination, KeyValueFeatureEnum... features);
    public void sendNotification(Context ctx, Class<BinaryNotificationMessage> type, NotificationTemplate template, OutputStreamFactory destination, KeyValueFeatureEnum... features);
    public void sendNotification(Context ctx, Class<EmailNotificationMessage> type, NotificationTemplate template, EmailAddresses destination, KeyValueFeatureEnum... features);
}
