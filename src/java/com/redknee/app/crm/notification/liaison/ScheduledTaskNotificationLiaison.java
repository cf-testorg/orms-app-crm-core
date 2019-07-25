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
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.notification.NotificationResultCallback;
import com.redknee.app.crm.notification.RecipientInfo;
import com.redknee.app.crm.notification.ScheduledNotification;
import com.redknee.app.crm.notification.ScheduledNotificationXInfo;
import com.redknee.app.crm.notification.message.NotificationMessage;
import com.redknee.app.crm.support.CalendarSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;


/**
 * This liaison generates messages immediately and stores them in a
 * ScheduledNotification home for delivery according to a schedule. To facilitate
 * delivery, a Framework LifecycleAgent is created (or referenced) and a Framework
 * TaskEntry. The lifecycle agent is responsible for delivering messages stored in the
 * ScheduledNotification records containing the agent's identifier within. This agent
 * is started according to the schedule defined in the task entry.
 * 
 * If communicating with a binary-style message generator, then this liaison will pass an
 * output stream to a temporary file to the generator to ensure that the message content
 * is available even after an application restart.
 * 
 * Limitations:
 * 
 * - The message and all objects stored in the RecipientInfo field must be capable of
 * being output to the ScheduledNotification home. This is not an issue if a
 * transient home is used, but if a transient home is used then the pending notifications
 * will be lost on restart. The safest way to guarantee delivery is to ensure that
 * everything is model generated. This is primarily an issue for binary messages.
 * 
 * - If the caller passes a custom callback object to a ScheduledTaskNotificationLiaison,
 * then it must be capable of being output to the ScheduledNotification home. If the
 * home is a transient home, then the callback object will be lost on restart. If the
 * callback is serializable to a persistent home, then it is likely that a different
 * instance of the callback will be invoked. Therefore the caller should not assume that
 * it's exact instance of callback object will be invoked by the delivery service.
 * 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class ScheduledTaskNotificationLiaison extends AbstractScheduledTaskNotificationLiaison
{
    /**
     * {@inheritDoc}
     */
    @Override
    protected void sendMessage(Context ctx, NotificationMessage msg, RecipientInfo destination)
    {
        if (getLiaisonSchedule() == null || getLiaisonSchedule().length() == 0)
        {
            new InfoLogMsg(this, "No schedule configured in scheduled liaison.  Attempting message delivery immediately.", null).log(ctx);
            super.sendMessage(ctx, msg, destination);
            return;
        }
        
        ScheduledNotification entry = null;
        try
        {
            entry = (ScheduledNotification) XBeans.instantiate(ScheduledNotification.class, ctx);
        }
        catch (Exception e)
        {
            entry = new ScheduledNotification();
        }
        String ban = (String)ctx.get("BAN");
        entry.setCleanupKey(String.valueOf(ctx.get(ScheduledNotificationXInfo.CLEANUP_KEY, ScheduledNotification.DEFAULT_CLEANUPKEY)));
        
        if(ban != null)
        {
        	entry.setBan(ban);
        }
        entry.setTimestamp(CalendarSupportHelper.get(ctx).getRunningDate(ctx));
        entry.setMessage(msg);
        entry.setDestination(destination);
        entry.setLiaisonSchedule(getLiaisonSchedule());
        
        NotificationResultCallback callback = (NotificationResultCallback) ctx.get(NotificationResultCallback.class);
        if (callback != null)
        {
            entry.setCallback(callback);
        }
        
        boolean success = false;
        
        try
        {
            entry = HomeSupportHelper.get(ctx).createBean(ctx, entry);
            success = true;
        }
        catch (HomeException e)
        {
            try
            {
                entry = HomeSupportHelper.get(ctx).storeBean(ctx, entry);
                success = true;
            }
            catch (HomeException e1)
            {
                String errorMsg = "Error scheduling notification for later delivery by scheduled task " + getLiaisonSchedule();
                new MinorLogMsg(this, errorMsg, e).log(ctx);
                if (callback != null)
                {
                    callback.reportFailure(ctx, false, errorMsg, e);
                }
            }
        }
        
        if (success && LogSupport.isDebugEnabled(ctx))
        {
            new DebugLogMsg(this, "Submitted message for scheduled delivery [" + entry + "]", null).log(ctx);
        }
    }
    
    public static void setCleanupKey(Context ctx, String cleanupKey)
    {
        ctx.put(ScheduledNotificationXInfo.CLEANUP_KEY, cleanupKey);
    }
}
