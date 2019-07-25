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

import java.io.IOException;

import com.redknee.app.crm.bean.SystemNoteSubTypeEnum;
import com.redknee.app.crm.bean.SystemNoteTypeEnum;
import com.redknee.app.crm.io.ExplicitCloseOutputStreamFactory;
import com.redknee.app.crm.io.OutputStreamFactory;
import com.redknee.app.crm.notification.NotificationResultCallback;
import com.redknee.app.crm.notification.RecipientInfo;
import com.redknee.app.crm.notification.ScheduledNotification;
import com.redknee.app.crm.notification.message.NotificationMessage;
import com.redknee.app.crm.support.NoteSupportHelper;
import com.redknee.framework.lifecycle.LifecycleAgentSupport;
import com.redknee.framework.lifecycle.LifecycleStateEnum;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;

/**
 * Package-private visitor for use by {@link ScheduledTaskNotificationLiaison).
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
class ScheduledNotificationDeliveryVisitor implements Visitor
{
    final LifecycleAgentSupport agent_;

    public ScheduledNotificationDeliveryVisitor(LifecycleAgentSupport agent)
    {
        agent_ = agent;
    }

    public void visit(Context ctx, Object obj) throws AgentException, AbortVisitException
    {
        if (!LifecycleStateEnum.RUNNING.equals(agent_.getState()))
        {
            String msg = "Lifecycle agent " + agent_.getAgentId() + " no longer running.  Remaining notifications will be sent next time it is run.";
            new InfoLogMsg(this, msg, null).log(ctx);
            throw new AbortVisitException(msg);
        }
        
        if (obj instanceof ScheduledNotification)
        {
            final ScheduledNotification entry = (ScheduledNotification) obj;

            AbstractNotificationLiaison delegate = getDelegate(ctx);
            if (delegate != null)
            {
                NotificationMessage msg = entry.getMessage();
                
                RecipientInfo recipient = entry.getDestination();
                
                NotificationResultCallback callback = new ScheduledEntryMaintenanceCallback(entry);
                
                ctx.put(NotificationResultCallback.class, callback);
                ctx.put(ScheduledNotification.class, entry);

                delegate.sendMessage(ctx, msg, recipient);
                
                OutputStreamFactory postTo = recipient.getPostToGenerator();
                if (postTo instanceof ExplicitCloseOutputStreamFactory)
                {
                    try
                    {
                        // The scheduled execution must close the output stream because the
                        // caller either no longer exists or the caller is holding a different
                        // instance of the 'postTo' output stream factory.
                        ((ExplicitCloseOutputStreamFactory) postTo).closeRawOutputStream(ctx);
                    }
                    catch (IOException e)
                    {
                        new MinorLogMsg(this, "Error closing output stream after message delivery: " + e.getMessage(), e).log(ctx);
                    }
                }
            try
            {
            	if(entry.getBan() != null && !entry.getBan().trim().equals("") )
            	{
            		NoteSupportHelper.get(ctx).addAccountNote(ctx, entry.getBan(), " Scheduled Notification sent. Content:"  + msg.toString(), SystemNoteTypeEnum.EVENTS, SystemNoteSubTypeEnum.NOTIFICATION);
            	}
            }
            catch (Exception e)
            {
            	LogSupport.major(ctx, this, "Unable to add Account Note: ",e);
			}
            }
            else
            {
                throw new AgentException("No suitable real-time notification liaison installed in context under class key '" 
                        + RealTimeNotificationLiaison.class.getName() + "'");
            }
        }
    }

    protected AbstractNotificationLiaison getDelegate(Context ctx)
    {
        if (delegate_ != null)
        {
            return delegate_;
        }
        
        NotificationLiaison delegate = (NotificationLiaison) ctx.get(RealTimeNotificationLiaison.class);
        if (delegate instanceof AbstractNotificationLiaison)
        {
            delegate_ = (AbstractNotificationLiaison) delegate;
        }
        return delegate_;
    }
    
    protected AbstractNotificationLiaison delegate_ = null;
}