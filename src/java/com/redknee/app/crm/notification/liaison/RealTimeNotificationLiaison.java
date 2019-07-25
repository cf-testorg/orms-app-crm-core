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

import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.notification.LoggingNotificationResultCallback;
import com.redknee.app.crm.notification.NotificationResultCallback;
import com.redknee.app.crm.notification.RecipientInfo;
import com.redknee.app.crm.notification.delivery.MessageDeliveryService;
import com.redknee.app.crm.notification.generator.MessageGenerationException;
import com.redknee.app.crm.notification.generator.MessageGenerator;
import com.redknee.app.crm.notification.message.NotificationMessage;
import com.redknee.app.crm.notification.template.NotificationTemplate;


/**
 * This liaison generates and delivers messages immediately.
 * 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class RealTimeNotificationLiaison extends AbstractRealTimeNotificationLiaison
{
    /**
     * {@inheritDoc}
     */
    @Override
    protected NotificationMessage generateMessage(Context ctx, Class<? extends NotificationMessage> type, NotificationTemplate template, KeyValueFeatureEnum... features) throws MessageGenerationException
    {
        NotificationMessage result = null;
        
        MessageGenerator generator = null;
        
        if (type != null)
        {
            generator = (MessageGenerator) XBeans.getInstanceOf(ctx, type, MessageGenerator.class);
            if (generator == null)
            {
                new InfoLogMsg(this, "No suitable message generator is installed to generate messages of type '" + type.getName() + "'.  Will look-up generator by template type...", null).log(ctx);
            }
        }

        if (generator == null)
        {
            if (template != null)
            {
                generator = (MessageGenerator) XBeans.getInstanceOf(ctx, template, MessageGenerator.class);
                if (generator == null)
                {
                    new InfoLogMsg(this, "No suitable message generator is installed to generate messages from template of type '" + template.getClass().getName() + "'.  Will look-up generator from context...", null).log(ctx);
                }
            }
            else
            {
                new InfoLogMsg(this, "Template is null.  Will look-up generator from context...", null).log(ctx);
            }
        }
        
        if (generator == null)
        {
            generator = (MessageGenerator) ctx.get(MessageGenerator.class);
        }

        if (generator == null)
        {
            String templateType = (template == null ? null : template.getClass().getName());
            new MinorLogMsg(this, "No suitable message generator is installed to handle template of type '" + templateType + "'.  Returning null message!", null).log(ctx);
        }
        else
        {
            result = generator.generate(ctx, template, features);
        }
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void sendMessage(Context ctx, NotificationMessage msg, RecipientInfo destination)
    {
        MessageDeliveryService deliveryService = null;
        if (msg != null)
        {
            deliveryService = (MessageDeliveryService) XBeans.getInstanceOf(ctx, msg, MessageDeliveryService.class);
            if (deliveryService == null)
            {
                new InfoLogMsg(this, "No suitable delivery service is installed to delivery messages of type '" + msg.getClass().getName() + "'.  Will look-up default delivery service from context...", null).log(ctx);
            }
        }
        else
        {
            new InfoLogMsg(this, "Message is null.  Will look-up default delivery service from context...", null).log(ctx);
        }

        if (deliveryService == null)
        {
            deliveryService = (MessageDeliveryService) ctx.get(MessageDeliveryService.class);
        }

        if (deliveryService != null)
        {
            NotificationResultCallback callback = (NotificationResultCallback) ctx.get(NotificationResultCallback.class, new LoggingNotificationResultCallback());
            deliveryService.sendMessage(ctx, destination, msg, callback);
        }
        else
        {
            String msgType = (msg == null ? null : msg.getClass().getName());
            String deliveryErrorMsg = "No suitable delivery service is installed to handle messages of type '" + msgType + "'";
            new MinorLogMsg(this, deliveryErrorMsg, null).log(ctx);
            
            NotificationResultCallback callback = (NotificationResultCallback) ctx.get(NotificationResultCallback.class);
            if (callback != null)
            {
                callback.reportFailure(ctx, false, deliveryErrorMsg);
            }
        }
    }
    
}
