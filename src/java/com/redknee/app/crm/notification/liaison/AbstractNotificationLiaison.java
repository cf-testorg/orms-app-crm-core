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

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.MinorLogMsg;

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
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public abstract class AbstractNotificationLiaison extends AbstractBean implements NotificationLiaison
{

    /**
     * {@inheritDoc}
     */
    public void sendNotification(Context ctx, NotificationTemplate template, RecipientInfo destination, KeyValueFeatureEnum... features)
    {
        NotificationMessage msg = null;
        try
        {
            if (!(template instanceof NotificationMessage))
            {
                msg = generateMessage(ctx, null, template, features);
            }
            else
            {
                msg = (NotificationMessage) template;
            }
            
            sendMessage(ctx, msg, destination);
        }
        catch (MessageGenerationException e)
        {
            String templateType = (template == null ? null : template.getClass().getName());
            new MinorLogMsg(this, "Error occurred generating message from template of type '" + templateType + "'", e).log(ctx);
        }
    }


    /**
     * {@inheritDoc}
     */
    public void sendNotification(Context ctx, Class<SmsNotificationMessage> type, NotificationTemplate template,
            String destination, KeyValueFeatureEnum... features)
    {
        NotificationMessage msg = null;
        try
        {
            if (type == null || !type.isInstance(template))
            {
                msg = generateMessage(ctx, type, template, features);
            }
            else
            {
                msg = (NotificationMessage) template;
            }

            RecipientInfo destinationInfo = new RecipientInfo();
            destinationInfo.setSmsTo(destination);

            sendMessage(ctx, msg, destinationInfo);
        }
        catch (MessageGenerationException e)
        {
            String templateType = (template == null ? null : template.getClass().getName());
            String msgType = (type == null ? null : type.getName());
            new MinorLogMsg(this, "Error occurred generating " + msgType + " message from template of type '" + templateType + "'", e).log(ctx);
        }
    }


    /**
     * {@inheritDoc}
     */
    public void sendNotification(Context ctx, Class<BinaryNotificationMessage> type, NotificationTemplate template,
            OutputStreamFactory destination, KeyValueFeatureEnum... features)
    {
        NotificationMessage msg = null;
        try
        {
            if (type == null || !type.isInstance(template))
            {
                msg = generateMessage(ctx, type, template, features);
            }
            else
            {
                msg = (NotificationMessage) template;
            }

            RecipientInfo destinationInfo = new RecipientInfo();
            destinationInfo.setPostToGenerator(destination);

            sendMessage(ctx, msg, destinationInfo);
        }
        catch (MessageGenerationException e)
        {
            String templateType = (template == null ? null : template.getClass().getName());
            String msgType = (type == null ? null : type.getName());
            new MinorLogMsg(this, "Error occurred generating " + msgType + " message from template of type '" + templateType + "'", e).log(ctx);
        }
    }


    /**
     * {@inheritDoc}
     */
    public void sendNotification(Context ctx, Class<EmailNotificationMessage> type, NotificationTemplate template,
            EmailAddresses destination, KeyValueFeatureEnum... features)
    {
        NotificationMessage msg = null;
        try
        {
            if (type == null || !type.isInstance(template))
            {
                msg = generateMessage(ctx, type, template, features);
            }
            else
            {
                msg = (NotificationMessage) template;
            }

            RecipientInfo destinationInfo = new RecipientInfo();
            destinationInfo.setEmailTo(destination);

            sendMessage(ctx, msg, destinationInfo);
        }
        catch (MessageGenerationException e)
        {
            String templateType = (template == null ? null : template.getClass().getName());
            String msgType = (type == null ? null : type.getName());
            new MinorLogMsg(this, "Error occurred generating " + msgType + " message from template of type '" + templateType + "'", e).log(ctx);
        }
    }


    protected abstract NotificationMessage generateMessage(Context ctx, Class<? extends NotificationMessage> type, NotificationTemplate template, KeyValueFeatureEnum... features) throws MessageGenerationException;


    protected abstract void sendMessage(Context ctx, NotificationMessage msg, RecipientInfo destination);
}
