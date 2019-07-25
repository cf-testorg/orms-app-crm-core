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
package com.redknee.app.crm.home;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NotifyingHome;

import com.redknee.app.crm.notification.LiaisonSchedule;
import com.redknee.app.crm.notification.ScheduledNotificationXInfo;
import com.redknee.app.crm.notification.liaison.LiaisonScheduleInstallationHomeChangeListener;
import com.redknee.app.crm.notification.liaison.LiaisonScheduleInstallationVisitor;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class LiaisonScheduleHomePipelineFactory implements PipelineFactory
{

    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = CoreSupport.bindHome(ctx, LiaisonSchedule.class);

        home = new NotifyingHome(home);

        // Add listener to install/uninstall/update schedules when config changes occur 
        ((NotifyingHome)home).addHomeChangeListener(new LiaisonScheduleInstallationHomeChangeListener(ctx));
        
        home = new DependencyCheckOnRemveHome(ScheduledNotificationXInfo.LIAISON_SCHEDULE, home);
        
        // Install the existing schedules on startup
        home.forEach(ctx, new LiaisonScheduleInstallationVisitor());
        
        return home;
    }

}
