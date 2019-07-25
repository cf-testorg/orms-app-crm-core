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
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.notification.NotificationResultCallbackProxy;
import com.redknee.app.crm.notification.ScheduledNotification;
import com.redknee.app.crm.support.HomeSupportHelper;

/**
 * Package-private notification callback class for use by {@link ScheduledTaskNotificationLiaison).
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
class ScheduledEntryMaintenanceCallback extends NotificationResultCallbackProxy
{
    ScheduledEntryMaintenanceCallback(ScheduledNotification entry)
    {
        super(entry.getCallback());
        this.entry_ = entry;
    }


    @Override
    public void reportSuccess(Context aCtx)
    {
        removeEntry(aCtx, entry_);
        super.reportSuccess(aCtx);
    }


    @Override
    public void reportFailure(Context aCtx, boolean recoverable, String msg)
    {
        if (!recoverable)
        {
            new MinorLogMsg(this, 
                    "Delivery failed witn non-recoverable error [" + msg
                    + "] for scheduled notification [" + entry_
                    + "].  Removing message from scheduled task queue so it will not be attempted again.", null).log(aCtx);
            removeEntry(aCtx, entry_);
        }else {
        	new MinorLogMsg(this, 
                    "Delivery failed witn recoverable error [" + msg
                    + "] for scheduled notification [" + entry_
                    + "].  It will be attempted again for delivery ", null).log(aCtx);
        }
        super.reportFailure(aCtx, recoverable, msg);
    }


    @Override
    public void reportFailure(Context aCtx, boolean recoverable, Exception cause)
    {
        if (!recoverable)
        {
            new MinorLogMsg(this, 
                    "Delivery failed witn non-recoverable error for scheduled notification [" + entry_
                    + "].  Removing message from scheduled task queue so it will not be attempted again.", cause).log(aCtx);
            removeEntry(aCtx, entry_);
        } else {
        	new MinorLogMsg(this, 
                    "Delivery failed witn recoverable error [" + cause.getMessage()
                    + "] for scheduled notification [" + entry_
                    + "].  It will be attempted again for delivery ", null).log(aCtx);
        }
        super.reportFailure(aCtx, recoverable, cause);
    }


    @Override
    public void reportFailure(Context aCtx, boolean recoverable, String msg, Exception cause)
    {
        if (!recoverable)
        {
            new MinorLogMsg(this, 
                    "Delivery failed witn non-recoverable error [" + msg
                    + "] for scheduled notification [" + entry_
                    + "].  Removing message from scheduled task queue so it will not be attempted again.", cause).log(aCtx);
            removeEntry(aCtx, entry_);
        } else {
        	new MinorLogMsg(this, 
                    "Delivery failed witn recoverable error [" + msg
                    + "] for scheduled notification [" + entry_
                    + "].  It will be attempted again for delivery ", null).log(aCtx);
        }
        super.reportFailure(aCtx, recoverable, msg, cause);
    }

    protected boolean removeEntry(Context ctx, ScheduledNotification entry)
    {
        boolean proceed = true;
        try
        {
            HomeSupportHelper.get(ctx).removeBean(ctx, entry);
        }
        catch (HomeException e)
        {
            try
            {
                proceed = HomeSupportHelper.get(ctx).findBean(ctx, ScheduledNotification.class, entry.getId()) != null;
                if (!proceed)
                {
                    new MinorLogMsg(this, "Error removing scheduled notification entry.  Delivery will be attempted again next time the " + entry.getLiaisonSchedule() + " is run.", e).log(ctx);
                }
            }
            catch (HomeException e1)
            {
                // NOP, proceed = true
            }
        }
        return proceed;
    }
    
    protected final ScheduledNotification entry_;
}