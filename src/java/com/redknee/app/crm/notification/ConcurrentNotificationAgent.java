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
package com.redknee.app.crm.notification;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgent;

import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.io.OutputStreamFactory;
import com.redknee.app.crm.notification.delivery.ConcurrentDeliveryService;
import com.redknee.app.crm.notification.delivery.MessageDeliveryService;
import com.redknee.app.crm.notification.liaison.ConcurrentNotificationLiaison;
import com.redknee.app.crm.notification.liaison.ConcurrentNotificationLiaison.NotificationLiaisonOperation;
import com.redknee.app.crm.notification.liaison.NotificationLiaison;
import com.redknee.app.crm.notification.message.BinaryNotificationMessage;
import com.redknee.app.crm.notification.message.EmailNotificationMessage;
import com.redknee.app.crm.notification.message.NotificationMessage;
import com.redknee.app.crm.notification.message.SmsNotificationMessage;
import com.redknee.app.crm.notification.template.NotificationTemplate;

public class ConcurrentNotificationAgent implements ContextAgent
{
    private static ContextAgent instance_ = null;
    public static ContextAgent instance()
    {
        if (instance_ == null)
        {
            instance_ = new ConcurrentNotificationAgent();
        }
        return instance_;
    }
    
    protected ConcurrentNotificationAgent()
    {
    }
    
    private static final String OPERATING_CONTEXT = ConcurrentNotificationAgent.class.getSimpleName() + ":OPERATING_CONTEXT";
    private static final Object CALLER = ConcurrentNotificationAgent.class.getSimpleName() + ":CALLER";
    private static final Object DELEGATE = ConcurrentNotificationAgent.class.getSimpleName() + ":DELEGATE";
    
    public static Context getDelegationContext(Context ctx, Object caller, Object delegate)
    {
        Context sCtx = ctx.createSubContext();
        sCtx.put(OPERATING_CONTEXT, ctx);
        sCtx.put(CALLER, caller);
        sCtx.put(DELEGATE, delegate);
        return sCtx;
    }

    public void execute(Context ctx) throws AgentException
    {
        Object caller = ctx.get(CALLER);
        if (caller instanceof ConcurrentDeliveryService)
        {
            MessageDeliveryService deliveryService = (MessageDeliveryService) ctx.get(DELEGATE);
            
            RecipientInfo recipient = ConcurrentDeliveryService.getRecipient(ctx);
            NotificationMessage msg = ConcurrentDeliveryService.getMessage(ctx);
            NotificationResultCallback callback = ConcurrentDeliveryService.getCallback(ctx);

            Context opCtx = (Context) ctx.get(OPERATING_CONTEXT, ctx);
            deliveryService.sendMessage(opCtx, recipient, msg, callback);
        }
        else if (caller instanceof ConcurrentNotificationLiaison)
        {
            NotificationLiaison liaison = (NotificationLiaison) ctx.get(DELEGATE);
            
            NotificationLiaisonOperation op = ConcurrentNotificationLiaison.getOperation(ctx);
            
            Class type = ConcurrentNotificationLiaison.getMessageType(ctx);
            NotificationTemplate template = ConcurrentNotificationLiaison.getTemplate(ctx);
            Object destination = ConcurrentNotificationLiaison.getDestination(ctx);
            KeyValueFeatureEnum[] features = ConcurrentNotificationLiaison.getKeyValueFeatureEnums(ctx);
            
            Context opCtx = (Context) ctx.get(OPERATING_CONTEXT, ctx);
            switch (op)
            {
            case SEND_GENERIC_NOTIFICATION:
                liaison.sendNotification(opCtx, template, (RecipientInfo) destination, features);
                break;
            case SEND_SMS_NOTIFICATION:
                liaison.sendNotification(opCtx, (Class<SmsNotificationMessage>) type, template, (String) destination, features);
                break;
            case SEND_EMAIL_NOTIFICATION:
                liaison.sendNotification(opCtx, (Class<EmailNotificationMessage>) type, template, (EmailAddresses) destination, features);
                break;
            case SEND_BINARY_NOTIFICATION:
                liaison.sendNotification(opCtx, (Class<BinaryNotificationMessage>) type, template, (OutputStreamFactory) destination, features);
                break;
            }
        }
    }
}