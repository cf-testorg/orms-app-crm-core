/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily 
 * available. Additionally, source code is, by its very nature, confidential 
 * information and inextricably contains trade secrets and other information 
 * proprietary, valuable and sensitive to Redknee, no unauthorised use, 
 * disclosure, manipulation or otherwise is permitted, and may only be used 
 * in accordance with the terms of the licence agreement entered into with 
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.web.agent;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.redknee.framework.core.DashboardAgent;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.renderer.TableRenderer;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.client.ConnectionStatus;
import com.redknee.app.crm.client.RemoteServiceStatus;
import com.redknee.app.crm.support.FrameworkSupportHelper;
import com.redknee.app.crm.support.SystemStatusSupportHelper;


/**
 * Provides a System Status page that attempts to report the current status of
 * components in the application.  Components are registered by calling
 * registerExternalService(), passing in the operating context and the Context
 * key that should be used for getting the service from the context.
 * 
 * @author Aaron Gourley
 * @since 7.5
 * 
 * Support clustered corba client
 * Display the remote services that implement RemoteServiceStatus interface
 * @author rchen
 * @since June 25, 2009 
 */
public class SystemStatusDashboardAgent implements DashboardAgent
{
    /* (non-Javadoc)
     * @see com.redknee.framework.xhome.context.ContextAgent#execute(com.redknee.framework.xhome.context.Context)
     */
    public void execute(Context ctx) throws AgentException
    {
        try
        {
            final PrintWriter out = (PrintWriter) ctx.get(PrintWriter.class);
            final TableRenderer renderer =  FrameworkSupportHelper.get(ctx).getTableRenderer(ctx);

            renderer.Table(ctx, out, "External Service Status");

            int index = displayTableHead(ctx, out, renderer, 0);
            
            displayServiceStatus(ctx, out, renderer, index);
            
            renderer.TableEnd(ctx, out);
        }
        catch (Exception e)
        {
            new DebugLogMsg(this, e.getClass().getSimpleName() + " occurred in " + SystemStatusDashboardAgent.class.getSimpleName() + ".execute(): " + e.getMessage(), e).log(ctx);
            throw new AgentException(e);
        }
    }

    private int displayTableHead(Context ctx, final PrintWriter out, final TableRenderer renderer, int index)
    {
        renderer.TR(ctx, out, null, index++);
        renderer.TD(ctx, out);
        out.println("Name");
        renderer.TDEnd(ctx, out);
        renderer.TD(ctx, out);
        out.println("Description");
        renderer.TDEnd(ctx, out);
        renderer.TD(ctx, out);
        out.println("Service Status");
        renderer.TDEnd(ctx, out);
        renderer.TD(ctx, out);
        out.println("Connection Status");
        renderer.TDEnd(ctx, out);
        renderer.TREnd(ctx, out);
        return index;
    }

    private void displayServiceStatus(Context ctx, final PrintWriter out, final TableRenderer renderer, int index)
    {
        final List<RemoteServiceStatus> services = this.getExternalServices(ctx);

        for (RemoteServiceStatus service : services)
        {
            renderer.TR(ctx, out, null, index++);
            renderer.TD(ctx, out);
            out.println(service.getName());
            renderer.TDEnd(ctx, out);
            renderer.TD(ctx, out);
            out.println(service.getDescription());
            renderer.TDEnd(ctx, out);
            renderer.TD(ctx, out);
            boolean alive = service.isAlive();
            if (!alive)
            {
                out.print("<FONT color=\"red\">");
            }
            out.println(service.getServiceStatus());
            if (!alive)
            {
                out.print("</FONT>");
            }
            renderer.TDEnd(ctx, out);
            renderer.TD(ctx, out);
            
            ConnectionStatus[] connections = service.getConnectionStatus();
            renderer.Table(ctx, out, "");
            int j = 0;
            for (ConnectionStatus conn : connections)
            {
                renderer.TR(ctx, out, null, j++);
                renderer.TD(ctx, out);
                out.println(conn.getRemoteInfo());
                renderer.TDEnd(ctx, out);
                renderer.TD(ctx, out);
                out.println(conn.getStatus());
                renderer.TDEnd(ctx, out);
                renderer.TREnd(ctx, out);
            }
            renderer.TableEnd(ctx, out);
            
            renderer.TDEnd(ctx, out);
            renderer.TREnd(ctx, out);
        }
    }

    /**
     * Gets the list of RemoteServiceStatus that should be queried for status.
     *
     * @param context The operating context.
     * @return The list of RemoteServiceStatus that should be queried for status.
     */
    private List<RemoteServiceStatus> getExternalServices(final Context context)
    {
        final List<RemoteServiceStatus> services = new ArrayList<RemoteServiceStatus>(SystemStatusSupportHelper.get(context).getServices());
        
        final Set<Object> keys = SystemStatusSupportHelper.get(context).getKeys();
        
        for (Object key : keys)
        {
            final Object service = context.get(key);
            if (service instanceof RemoteServiceStatus)
            {
                services.add((RemoteServiceStatus)service);
            }
            else
            {
                LogSupport.minor(context, this, "Registered services is expected to be RemoteServiceStatus, but actually " + service.getClass().getName());
            }
        }

        Collections.sort(
                services,
                new Comparator<RemoteServiceStatus>()
                {
                    public int compare(final RemoteServiceStatus service1, final RemoteServiceStatus service2)
                    {
                        return String.CASE_INSENSITIVE_ORDER.compare(
                                service1.getName(),
                                service2.getName());
                    }
                });

        return services;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
