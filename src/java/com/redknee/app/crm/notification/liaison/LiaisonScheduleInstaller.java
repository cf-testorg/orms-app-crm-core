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

import com.redknee.framework.core.cron.CronAgent;
import com.redknee.framework.core.cron.TaskEntry;
import com.redknee.framework.core.cron.TaskStatusEnum;
import com.redknee.framework.core.cron.XCronLifecycleAgentControlConfig;
import com.redknee.framework.lifecycle.LifecycleAgent;
import com.redknee.framework.lifecycle.LifecycleAgentControl;
import com.redknee.framework.lifecycle.LifecycleStateEnum;
import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.notification.LiaisonSchedule;
import com.redknee.app.crm.support.HomeSupportHelper;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class LiaisonScheduleInstaller
{
    private static LiaisonScheduleInstaller instance_ = null;
    public static LiaisonScheduleInstaller instance()
    {
        if (instance_ == null)
        {
            instance_ = new LiaisonScheduleInstaller();
        }
        return instance_;
    }
    
    protected LiaisonScheduleInstaller()
    {
    }
    
    public void execute(Context ctx, boolean uninstall, LiaisonSchedule schedule)
    {
        if (schedule.getScheduleName() != null && schedule.getScheduleName().trim().length() > 0)
        {
            installScheduledTask(ctx, uninstall, schedule);
            installLifecycleAgent(ctx, uninstall, schedule);
        }
    }

    protected boolean installScheduledTask(Context ctx, boolean uninstall, LiaisonSchedule sched)
    {
        boolean success = true;
        
        String taskName = "Notification Task [" + sched.getScheduleName() + "]";
        String agentName = "Pending Notifications [" + sched.getScheduleName() + "]";
        String schedule = sched.getSchedule();
        
        TaskEntry task = null;
        try
        {
            task = HomeSupportHelper.get(ctx).findBean(ctx, TaskEntry.class, taskName);
        }
        catch (HomeException e)
        {
            new MinorLogMsg(this, "Error looking up scheduled task with ID " + taskName + ".  Will attempt creation...", e).log(ctx);
        }
        
        if (uninstall)
        {
            if (task != null)
            {
                try
                {
                    HomeSupportHelper.get(ctx).removeBean(ctx, task);
                }
                catch (HomeException e)
                {
                    new MinorLogMsg(this, "Failed to remove scheduled task '" + taskName + "'", e).log(ctx);
                    success = false;
                }
            }
            
            return success;
        }
        
        XCronLifecycleAgentControlConfig lifecycleConfig = new XCronLifecycleAgentControlConfig();
        lifecycleConfig.setAgent(agentName);
        
        if (task == null)
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "Creating scheduled task with ID " + taskName + "...", null).log(ctx);
            }

            task = new TaskEntry();
            task.setName(taskName);
            if (schedule != null)
            {
                task.setCronEntry(schedule);
            }
            task.setDefaultStatus(TaskStatusEnum.AVAILABLE);
            task.setStatus(TaskStatusEnum.AVAILABLE);
            task.setDescription(taskName);
            task.setAgentType("Lifecycle Agent Control");
            task.setAgent(lifecycleConfig);

            try
            {
                HomeSupportHelper.get(ctx).createBean(ctx, task);
                success &= true;
            }
            catch (HomeException e)
            {
                try
                {
                    HomeSupportHelper.get(ctx).storeBean(ctx, task);
                    success &= true;
                }
                catch (HomeException e1)
                {
                    new MajorLogMsg(this, 
                            "Error initializing scheduled task '" + taskName
                            + "' with schedule '" + schedule
                            + "' for lifecycle agent '" + agentName + "'", e).log(ctx);
                    success = false;
                }
            }
        }
        else
        {
            boolean dirty = false;
            
            if (schedule != null
                    && !SafetyUtil.safeEquals(task.getCronEntry(), schedule))
            {
                task.setCronEntry(schedule);
                task.setStatus(TaskStatusEnum.AVAILABLE);
                dirty = true;
            }
            
            CronAgent agent = task.getAgent();
            if (!(agent instanceof XCronLifecycleAgentControlConfig)
                    || !SafetyUtil.safeEquals(((XCronLifecycleAgentControlConfig)agent).getAgent(), agentName))
            {
                task.setAgent(lifecycleConfig);
                dirty = true;
            }
            
            if (dirty)
            {
                try
                {
                    HomeSupportHelper.get(ctx).storeBean(ctx, task);
                    success &= true;
                }
                catch (HomeException e)
                {
                    new MajorLogMsg(this, 
                            "Error initializing scheduled task '" + taskName
                            + "' with schedule '" + schedule
                            + "' for lifecycle agent '" + agentName + "'", e).log(ctx);
                    success = false;
                }
            }
        }
        return success;
    }
    
    protected boolean installLifecycleAgent(Context ctx, boolean uninstall, LiaisonSchedule sched)
    {
        boolean success = true;

        String agentName = "Pending Notifications [" + sched.getScheduleName() + "]";

        LifecycleAgent agent = null;

        LifecycleAgentControl ctl = null;
        try
        {
            ctl = HomeSupportHelper.get(ctx).findBean(ctx, LifecycleAgentControl.class, agentName);
            if (ctl != null)
            {
                agent = ctl.getLifecycleAgent();

                if (uninstall)
                {
                    try
                    {
                        HomeSupportHelper.get(ctx).removeBean(ctx, ctl);
                    }
                    catch (HomeException e)
                    {
                        new MinorLogMsg(this, "Failed to remove scheduled task '" + agentName + "'", e).log(ctx);
                        success = false;
                    }
                }
            }

            if (!(agent instanceof ScheduledLiaisonLifecycleAgent)
                    || !SafetyUtil.safeEquals(
                            agentName, 
                            ((ScheduledLiaisonLifecycleAgent) agent).getAgentId()))
            {
                if (agent != null)
                {
                    try
                    {
                        agent.doCmd(ctx, LifecycleStateEnum.STOP);
                    }
                    catch (Exception e)
                    {
                        new DebugLogMsg(this, "Unable to stop scheduled lifecycle agent [" + e.getMessage() + "]", null).log(ctx);
                    }
                }
                
                if (!uninstall)
                {
                    agent = new ScheduledLiaisonLifecycleAgent(ctx, sched.getScheduleName(), agentName);
                }
            }
        }
        catch (Exception e)
        {
            new MajorLogMsg(this, "Error initializing lifecycle agent '" + agentName + "'", e).log(ctx);
            success = false;
        }

        return success;
    }
}
