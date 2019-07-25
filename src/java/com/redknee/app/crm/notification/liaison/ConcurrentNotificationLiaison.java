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

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.pipe.ThreadPool;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.bean.core.NotificationThreadPool;
import com.redknee.app.crm.io.OutputStreamFactory;
import com.redknee.app.crm.notification.ConcurrentNotificationAgent;
import com.redknee.app.crm.notification.EmailAddresses;
import com.redknee.app.crm.notification.RecipientInfo;
import com.redknee.app.crm.notification.message.BinaryNotificationMessage;
import com.redknee.app.crm.notification.message.EmailNotificationMessage;
import com.redknee.app.crm.notification.message.SmsNotificationMessage;
import com.redknee.app.crm.notification.template.NotificationTemplate;

/**
 * This proxy liaison adds multi-threading support to it's delegate liaison. It
 * instantiates a thread pool using the configured number of threads and max queue size.
 * Delegate implementations must be thread-safe.
 * 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class ConcurrentNotificationLiaison extends AbstractConcurrentNotificationLiaison
{
    private static final String LIAISON_OPERATION = ConcurrentNotificationLiaison.class.getSimpleName() + ":OPERATION";
    private static final String MESSAGE_TYPE = ConcurrentNotificationLiaison.class.getSimpleName() + ":MESSAGE_TYPE";
    private static final String TEMPLATE = ConcurrentNotificationLiaison.class.getSimpleName() + ":TEMPLATE";
    private static final String DESTINATION = ConcurrentNotificationLiaison.class.getSimpleName() + ":DESTINATION";
    private static final String KEY_FEATURES = ConcurrentNotificationLiaison.class.getSimpleName() + ":KEY_FEATURES";

    public enum NotificationLiaisonOperation
    {
        SEND_GENERIC_NOTIFICATION,
        SEND_SMS_NOTIFICATION,
        SEND_EMAIL_NOTIFICATION,
        SEND_BINARY_NOTIFICATION
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendNotification(Context ctx, NotificationTemplate template, RecipientInfo destination, KeyValueFeatureEnum... features)
    {
        delegate(ctx, NotificationLiaisonOperation.SEND_GENERIC_NOTIFICATION, null, template, destination, features);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendNotification(Context ctx, Class<SmsNotificationMessage> type, NotificationTemplate template,
            String destination, KeyValueFeatureEnum... features)
    {
        delegate(ctx, NotificationLiaisonOperation.SEND_SMS_NOTIFICATION, type, template, destination, features);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendNotification(Context ctx, Class<BinaryNotificationMessage> type, NotificationTemplate template,
            OutputStreamFactory destination, KeyValueFeatureEnum... features)
    {
        delegate(ctx, NotificationLiaisonOperation.SEND_BINARY_NOTIFICATION, type, template, destination, features);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendNotification(Context ctx, Class<EmailNotificationMessage> type, NotificationTemplate template,
            EmailAddresses destination, KeyValueFeatureEnum... features)
    {
        delegate(ctx, NotificationLiaisonOperation.SEND_EMAIL_NOTIFICATION, type, template, destination, features);
    }

    protected void delegate(Context ctx, NotificationLiaisonOperation operation, Class type, NotificationTemplate template, Object destination, KeyValueFeatureEnum... features)
    {
        Context sCtx = ConcurrentNotificationAgent.getDelegationContext(ctx, this, getDelegate());
        
        sCtx.put(LIAISON_OPERATION, operation);
        
        if (type != null)
        {
            sCtx.put(MESSAGE_TYPE, type);
        }
        
        sCtx.put(TEMPLATE, template);
        sCtx.put(DESTINATION, destination);
        
        sCtx.put(KEY_FEATURES, features);

        try
        {
            ThreadPool threadPool = NotificationThreadPool.getThreadPool(ctx, getPool());
            if (threadPool != null)
            {
                threadPool.execute(sCtx);
            }
            else
            {
                new MinorLogMsg(this, "Thread Pool '" + getPool()
                        + "' not available.  Processing of '" + getDelegate().getClass().getName()
                        + "' liaison synchronously...", null).log(ctx);
                ConcurrentNotificationAgent.instance().execute(sCtx);
            }
        }
        catch (AgentException e)
        {
            new MajorLogMsg(this, "Error invoking delegate liaison!", e).log(ctx);
        }
    }
    
    public static NotificationLiaisonOperation getOperation(Context ctx)
    {
        return (NotificationLiaisonOperation) ctx.get(LIAISON_OPERATION);
    }
    
    public static Class getMessageType(Context ctx)
    {
        return (Class) ctx.get(MESSAGE_TYPE);
    }
    
    public static NotificationTemplate getTemplate(Context ctx)
    {
        return (NotificationTemplate) ctx.get(TEMPLATE);
    }
    
    public static Object getDestination(Context ctx)
    {
        return ctx.get(DESTINATION);
    }
    
    public static KeyValueFeatureEnum[] getKeyValueFeatureEnums(Context ctx)
    {
        return (KeyValueFeatureEnum[]) ctx.get(KEY_FEATURES);
    }
}
