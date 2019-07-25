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
package com.redknee.app.crm.web.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.redknee.app.crm.web.agent.SystemStatusDashboardAgent;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.RequestServicer;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;

/**
 * Provides a System Status page that attempts to report the current status of
 * components in the application.  Components are registered by calling
 * registerExternalService(), passing in the operating context and the Context
 * key that should be used for getting the service from the context.
 *
 * @author gary.anderson@redknee.com
 */
public class SystemStatusRequestServicer
        implements RequestServicer
{
    public void service(
            final Context context,
            final HttpServletRequest req,
            final HttpServletResponse res)
        throws ServletException, IOException
    {
        try
        {
            Context sCtx = context.createSubContext();
            sCtx.put(PrintWriter.class, res.getWriter());
            new SystemStatusDashboardAgent().execute(sCtx);
        }
        catch (Exception e)
        {
            if (LogSupport.isDebugEnabled(context))
            {
                new DebugLogMsg(this, e.getMessage(), e).log(context);
            }
        }
    }

} // class
