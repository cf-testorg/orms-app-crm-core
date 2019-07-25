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
 * The delivery service takes a message and delivers it to the recipient. The
 * implementation determines the delivery protocol (e.g. SMS, email, output stream, etc),
 * as well as any multithreaded processing & queuing details. The sendMessage() interface
 * takes a callback object to facilitate the possibility that message delivery is
 * asynchronous. The callback object will be notified of attempts, successes, failures,
 * and in the event of failure whether or not the delivery will be attempted again (i.e.
 * fatal failures vs potentially recoverable failures).
 * 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public interface MessageDeliveryService
{
    public void sendMessage(Context ctx, RecipientInfo recipient, NotificationMessage msg, NotificationResultCallback callback);
}
