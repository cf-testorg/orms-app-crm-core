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
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.Visitor;


/**
 * This visitor installs or uninstalls thread-pools for existing thread-pool configuration entries
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class NotificationThreadPoolInstallationVisitor implements Visitor
{
    public NotificationThreadPoolInstallationVisitor()
    {
        this(false);
    }
    
    public NotificationThreadPoolInstallationVisitor(boolean isUninstall)
    {
        isUninstall_ = isUninstall;
    }

    /**
     * {@inheritDoc}
     */
    public void visit(Context ctx, Object obj) throws AgentException, AbortVisitException
    {
        if (obj instanceof com.redknee.app.crm.bean.core.NotificationThreadPool)
        {
            com.redknee.app.crm.bean.core.NotificationThreadPool config = (com.redknee.app.crm.bean.core.NotificationThreadPool) obj;
            
            NotificationThreadPoolInstaller.instance().execute(ctx, isUninstall_, config);
        }
    }


    private final boolean isUninstall_;
}
