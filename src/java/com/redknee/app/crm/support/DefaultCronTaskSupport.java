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
 * Copyright &copy; Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.support;

import com.redknee.framework.core.cron.AgentEntry;
import com.redknee.framework.core.cron.TaskEntry;
import com.redknee.framework.xhome.context.Context;


/**
 * A collection of useful routines for use with CronTask agents.
 *
 * @author gary.anderson@redknee.com
 */
public
class DefaultCronTaskSupport implements CronTaskSupport
{

    protected static CronTaskSupport instance_ = null;
    public static CronTaskSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultCronTaskSupport();
        }
        return instance_;
    }

    protected DefaultCronTaskSupport()
    {
    }

    /**
     * Gets the value for Parameter #1.  This assumes that the task is in the
     * context with the key "AgentEntry.class".
     *
     * @param context The operating context.
     * @return The value for Parameter #1.
     */
    public String getParameter1(final Context context)
    {
        String value = null;

        TaskEntry task = (TaskEntry) context.get(TaskEntry.class);

        if (task == null)
        {
            final AgentEntry entry = (AgentEntry) context.get(AgentEntry.class);
            if (entry != null)
            {
                task = entry.getTask();
            }
        }

        if (task != null)
        {
            value = task.getParam0();
        }

        return value;
    }
    
    
    /**
     * Gets the value for Parameter #2.  This assumes that the task is in the
     * context with the key "AgentEntry.class".
     *
     * @param context The operating context.
     * @return The value for Parameter #2.
     */
    public String getParameter2(final Context context)
    {
        String value = null;

        TaskEntry task = (TaskEntry)context.get(TaskEntry.class);

        if (task == null)
        {
            final AgentEntry entry = (AgentEntry)context.get(AgentEntry.class);
            if (entry!=null)
            {
                task = entry.getTask();
            }
        }
        
        if (task!=null)
        {
            value = task.getParam1();
        }

        return value;
    }
    
   
    
} // class
