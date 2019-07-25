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
package com.redknee.app.crm.core.agent;

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgent;
import com.redknee.framework.xlog.log.CritLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.MajorLogMsg;


/**
 * This is automatically run by FW run levels.  See app-crm-core-context.sch.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class Install extends CoreSupport implements ContextAgent
{

    /**
     * {@inheritDoc}
     */
    public void execute(Context ctx) throws AgentException
    {
        if (ctx.get("AppCrmCore") instanceof Context)
        {
            // Get the application context.  When this runs from within a run level, it is passed the core context.
            // We need it to run on the application context because the journals have initialized it properly.
            ctx = (Context) ctx.get("AppCrmCore");
            new InfoLogMsg(this, "[AppCrmCore-Install] Using application context instead of core context for installation...", null).log(ctx);
        }
        
        try
        {
            new BeanInstall().execute(ctx);

            new FacetInstall().execute(ctx);
            new BeanFactoryInstall().execute(ctx);
            
            new StorageInstall().execute(ctx);
            
            new ServiceInstall().execute(ctx);
            
        }
        catch (Throwable t)
        {
            new CritLogMsg(this, "AppCrmCore installation failed due to exception.", t).log(ctx);
            throw new AgentException("Fail to complete AppCrmCore install", t);
        }
    }


    /**
     * Reports trouble initializing a component of the system.
     * @param ctx context, used for logging
     * @param component the component that failed loading
     * @param e the original exception
     */
    public static void failAndContinue(Context ctx, String component, Throwable e)
    {
        // TODO: add a flag in an internal table of components saying that this component failed to load
        new MajorLogMsg(Install.class.getName(), "Initializing component {" + component
            + "} failed. Install Continues. Original message:" + e.getMessage(), e).log(ctx);
    }
}
