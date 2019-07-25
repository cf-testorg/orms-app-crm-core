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
package com.redknee.app.crm.notification.message;

import com.redknee.framework.xhome.util.time.Time;

/**
 * An SMS notification message contains a textual representation of an SMS message.
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public interface SmsNotificationMessage extends NotificationMessage
{
    public String getFrom();

    public void setFrom(String from);
    
    public String getMessage();

    public void setMessage(String message);
    
    /**
     * @return Time at which the SMSC should deliver the message to the subscriber.
     */
    public Time getTimeToSend();

    public void setTimeToSend(Time time);
}
