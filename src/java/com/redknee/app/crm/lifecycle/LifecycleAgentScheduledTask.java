/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.lifecycle;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.redknee.app.crm.support.CalendarSupportHelper;
import com.redknee.app.crm.support.CronTaskSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.framework.core.cron.TaskEntry;
import com.redknee.framework.core.cron.TaskEntryContextFactory;
import com.redknee.framework.core.cron.agent.CronContextAgentException;
import com.redknee.framework.lifecycle.LifecycleAgentControl;
import com.redknee.framework.lifecycle.LifecycleException;
import com.redknee.framework.lifecycle.LifecycleStateEnum;
import com.redknee.framework.lifecycle.RunnableLifecycleAgentSupport;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.PMLogMsg;
import com.redknee.framework.xlog.log.SeverityEnum;
import com.redknee.framework.xlog.om.PMLog;


/**
 * This abstract class should be implemented by all cron tasks using lifecycle agents.
 * 
 * @author Marcio Marques
 * @since 9.0
 * 
 */
public abstract class LifecycleAgentScheduledTask extends RunnableLifecycleAgentSupport
{

    private static final long serialVersionUID = 1L;


    /**
     * Creates a LifecycleAgentScheduledTask bean.
     * 
     * @param ctx
     * @param agentId
     * @throws AgentException
     */
    public LifecycleAgentScheduledTask(Context ctx, String agentId) throws AgentException
    {
        super(ctx, agentId);
        Context subCtx = ctx.createSubContext();
        subCtx.put(TaskEntry.class, new TaskEntryContextFactory(agentId));
        setContext(subCtx);
        try
        {
            LifecycleAgentControl ctl = HomeSupportHelper.get(subCtx).findBean(subCtx, LifecycleAgentControl.class,
                    agentId);
            if (ctl == null)
            {
                ctl = new LifecycleAgentControl();
                ctl.setAgentId(agentId);
                ctl.setInitialState(LifecycleStateEnum.INITIALIZE);
                // Don't start this agent with the lifecycle manager
                ctl.setDependent(false);
                ctl.setTrans(getTrans());
                ctl = HomeSupportHelper.get(subCtx).createBean(subCtx, ctl);
            }
        }
        catch (HomeException e)
        {
            throw new AgentException(e);
        }
        // Initialize the lifecycle agent with FW
        execute(subCtx);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final void doRun(Context ctx) throws LifecycleException
    {
        if (isEnabled(ctx))
        {
            if (LogSupport.isEnabled(ctx,  SeverityEnum.INFO))
            {
                StringBuilder sb = new StringBuilder();
                sb.append(this.getAgentId());
                sb.append(" lifecycle agent cron task initiated.");
                LogSupport.info(ctx,this, sb.toString());
            }

            PMLogMsg logMsg = new PMLogMsg("LifeCycleAgentScheduledTask", getAgentId());

            try
            {

                start(ctx);
    
                if (LogSupport.isEnabled(ctx,  SeverityEnum.INFO))
                {
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.getAgentId());
                    sb.append(" lifecycle agent cron task finished executing.");
                    LogSupport.info(ctx,this, sb.toString());
                }
            }
            catch (final AbortVisitException exception)
            {
                StringBuilder sb = new StringBuilder();
                sb.append(this.getAgentId());
                sb.append(" lifecycle agent cron task was aborted.");
                String message = sb.toString();
                if (LogSupport.isEnabled(ctx,  SeverityEnum.INFO))
                {
                    LogSupport.info(ctx, this, message, exception);
                }
                throw new CronContextAgentException(message, exception);
            }
            catch (final Throwable t)
            {
                StringBuilder sb = new StringBuilder();
                sb.append(this.getAgentId());
                sb.append(" lifecycle agent cron task could not be completely executed.");
                String message = sb.toString();
                LogSupport.major(ctx, this, message, t);
                throw new CronContextAgentException(message, t);
            }
            finally
            {
                logMsg.log(ctx);
            }
        }
        else
        {
            if (LogSupport.isEnabled(ctx,  SeverityEnum.INFO))
            {
                StringBuilder sb = new StringBuilder();
                sb.append(this.getAgentId());
                sb.append(" lifecycle agent cron task not executed for it is not enabled.");
                LogSupport.info(ctx,this, sb.toString());
            }
        }
    }


    /**
     * The body of the agent.
     * 
     * @param ctx
     * @throws LifecycleException
     */
    protected abstract void start(Context ctx) throws LifecycleException, HomeException;


    /**
     * Checks if the agent is enabled and therefore should execute.
     * 
     * @param ctx
     * @return
     */
    public boolean isEnabled(Context ctx)
    {
        return true;
    }

    /**
     * Gets the running date.
     * 
     * @param context
     *            The operating context.
     * @return The "current date" for the dunning report generation run.
     * @throws AgentException
     *             thrown if any Exception is thrown during date parsing. Original
     *             Exception is linked.
     */
    protected <T extends Object> T getParameter1(final Context context, final Class<T> parameterClass)
    {
        return parseParameter(context, CronTaskSupportHelper.get(context).getParameter1(context), parameterClass);
    }
    
    protected <T extends Object> T getParameter2(final Context context, final Class<T> parameterClass)
    {
        return parseParameter(context, CronTaskSupportHelper.get(context).getParameter2(context), parameterClass);
    }
    
    private <T extends Object> T parseParameter(final Context context, final String parameter, final Class<T> parameterClass)
    {
        T result = null;
        
        if (parameter!=null && parameter.trim().length()>0)
        {
            
            try
            {
                if (String.class.isAssignableFrom(parameterClass))
                {
                    result = (T) parameter;
                }
                else if (Number.class.isAssignableFrom(parameterClass))
                {
                    result = (T) parameterClass.getMethod("valueOf", String.class).invoke(null, parameter);
                }
                else if (Date.class.isAssignableFrom(parameterClass))
                {
                    final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STRING);
                    return (T) CalendarSupportHelper.get(context).getDateWithNoTimeOfDay(dateFormat.parse(parameter.trim()));
                }
            }
            catch (Exception e)
            {
                LogSupport.minor(context, this, "Unable to parse parameter '" + parameter + "' as a " + parameterClass.getSimpleName());
            }
        }
        
        return result;
    }
    
    /**
     * The date format used for specifying the "current date" in parameter 1. This format
     * is currently consistent with other CronAgents.
     */
    private static final String DATE_FORMAT_STRING = "yyyyMMdd";
}
