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

import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.GenericTransientHome;
import com.redknee.framework.xhome.home.HomeException;

public class DynamicNotificationTypeScheduleHome extends NotificationTypeScheduleTransientHome
{
    public DynamicNotificationTypeScheduleHome(Context ctx)
    {
        super(ctx);
        setDelegate(new SimulatedNotificationTypeScheduleHome(ctx, NotificationTypeSchedule.class));
    }
    
    public DynamicNotificationTypeScheduleHome(Context ctx, Adapter adapter)
    {
        super(ctx, adapter);
        setDelegate(new SimulatedNotificationTypeScheduleHome(ctx, NotificationTypeSchedule.class));
    }
    
    private final class SimulatedNotificationTypeScheduleHome extends GenericTransientHome
    {
        private SimulatedNotificationTypeScheduleHome(final Context ctx, Class cls)
        {
            super(ctx, cls); 
            map_ = new DynamicNotificationTypeScheduleMap(ctx);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object create(Context ctx, Object obj) throws HomeException
        {
            obj = super.create(ctx, obj);
            
            if (!(obj instanceof Identifiable) || !map_.containsKey(((Identifiable)obj).ID()))
            {
                throw new HomeException("Creation of this object is not supported by this application.");
            }
            
            return obj;
        }
    }
}