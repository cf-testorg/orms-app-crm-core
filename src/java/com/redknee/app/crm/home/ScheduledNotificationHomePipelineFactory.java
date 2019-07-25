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
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.ValidatingHome;

import com.redknee.app.crm.notification.AppNameSettingScheduledNotificationHome;
import com.redknee.app.crm.notification.ScheduledNotification;
import com.redknee.app.crm.notification.ScheduledNotificationXInfo;
import com.redknee.app.crm.support.StorageSupportHelper;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class ScheduledNotificationHomePipelineFactory implements PipelineFactory
{

    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = StorageSupportHelper.get(ctx).createHome(ctx, ScheduledNotification.class, "ScheduledNotification");
        
        home = new ValidatingHome(home);
        
        home = home.where(ctx, new EQ(ScheduledNotificationXInfo.APP_NAME, CoreSupport.getApplication(ctx).getName()));
        
        home = new AppNameSettingScheduledNotificationHome(home);
        
        return home;
    }

}
