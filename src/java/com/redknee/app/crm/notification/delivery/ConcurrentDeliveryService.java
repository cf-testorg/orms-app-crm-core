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

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.pipe.ThreadPool;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.core.NotificationThreadPool;
import com.redknee.app.crm.notification.ConcurrentNotificationAgent;
import com.redknee.app.crm.notification.NotificationResultCallback;
import com.redknee.app.crm.notification.RecipientInfo;
import com.redknee.app.crm.notification.message.NotificationMessage;

/**
 * This proxy service adds multi-threading support to it's delegate delivery service. It
 * instantiates a thread pool using the configured number of threads and max queue size.
 * Delegate implementations must be thread-safe.
 * 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class ConcurrentDeliveryService extends AbstractConcurrentDeliveryService
{
    private static final String RECIPIENT = ConcurrentDeliveryService.class.getSimpleName() + ":RECIPIENT";
    private static final String MESSAGE = ConcurrentDeliveryService.class.getSimpleName() + ":MESSAGE";
    private static final String CALLBACK = ConcurrentDeliveryService.class.getSimpleName() + ":CALLBACK";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(Context ctx, RecipientInfo recipient, NotificationMessage msg,
            NotificationResultCallback callback)
    {
        Context sCtx = ConcurrentNotificationAgent.getDelegationContext(ctx, this, getDelegate());
        
        sCtx.put(RECIPIENT, recipient);
        sCtx.put(MESSAGE, msg);
        sCtx.put(CALLBACK, callback);
        
        ThreadPool threadPool = NotificationThreadPool.getThreadPool(ctx, getPool());
        try
        {
            if (threadPool != null)
            { 
                threadPool.execute(sCtx);
            }
            else
            {
                new MinorLogMsg(this, "Thread Pool '" + getPool()
                        + "' not available.  Processing of '" + getDelegate().getClass().getName() + "' delivery service synchronously...", null).log(ctx);
                ConcurrentNotificationAgent.instance().execute(sCtx);
            }
        }
        catch (AgentException e)
        {
            new MajorLogMsg(this, "Error invoking delegate liaison asynchronously!", e).log(ctx);
        }
    }
    
    public static RecipientInfo getRecipient(Context ctx)
    {
        return (RecipientInfo) ctx.get(RECIPIENT);
    }
    
    public static NotificationMessage getMessage(Context ctx)
    {
        return (NotificationMessage) ctx.get(MESSAGE);
    }
    
    public static NotificationResultCallback getCallback(Context ctx)
    {
        return (NotificationResultCallback) ctx.get(CALLBACK);
    }
}
