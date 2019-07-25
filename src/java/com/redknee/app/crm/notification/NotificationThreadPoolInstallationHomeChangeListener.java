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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAwareSupport;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeChangeEvent;
import com.redknee.framework.xhome.home.HomeChangeListener;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.home.NotifyingHomeItem;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.core.NotificationThreadPool;
import com.redknee.app.crm.support.HomeSupportHelper;


/**
 * This home change listener propogates config changes to the actual thread pool
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class NotificationThreadPoolInstallationHomeChangeListener extends ContextAwareSupport implements HomeChangeListener
{
    public NotificationThreadPoolInstallationHomeChangeListener(Context ctx)
    {
        setContext(ctx);
    }

    /**
     * {@inheritDoc}
     */
    public void homeChange(HomeChangeEvent evt)
    {
        Context ctx = getContext();
        
        Object source = evt.getSource();
        if (source instanceof NotifyingHomeItem)
        {
            source = ((NotifyingHomeItem) source).getNewObject();
        }
        switch (evt.getOperation().getIndex())
        {
        case HomeOperationEnum.CREATE_INDEX:
        case HomeOperationEnum.STORE_INDEX:
            if (source instanceof NotificationThreadPool)
            {
                NotificationThreadPoolInstaller.instance().execute(ctx, false, (NotificationThreadPool) source);
            }
            break;
        case HomeOperationEnum.REMOVE_INDEX:
            if (source instanceof NotificationThreadPool)
            {
                NotificationThreadPoolInstaller.instance().execute(ctx, true, (NotificationThreadPool) source);
            }
            break;
        case HomeOperationEnum.REMOVE_ALL_INDEX:
            Context sCtx = HomeSupportHelper.get(ctx).getWhereContext(ctx, NotificationThreadPool.class, source);
            Home home = (Home) sCtx.get(NotificationThreadPoolHome.class);
            if (home != null)
            {
                try
                {
                    home.forEach(sCtx, new NotificationThreadPoolInstallationVisitor(true));
                }
                catch (HomeException e)
                {
                    new MinorLogMsg(this, "Error occurred uninstalling notification thread pools.", e).log(ctx);
                }
            }
            break;
        }
    }

}
