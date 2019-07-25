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

import com.redknee.framework.xhome.context.Context;


/**
 * A collection of useful routines for use with CronTask agents.
 *
 * @author Marcio Marques
 */
public
interface CronTaskSupport extends Support
{
    /**
     * Gets the value for Parameter #1.  This assumes that the task is in the
     * context with the key "AgentEntry.class".
     *
     * @param context The operating context.
     * @return The value for Parameter #1.
     */
    public String getParameter1(final Context context);
    
    
    /**
     * Gets the value for Parameter #2.  This assumes that the task is in the
     * context with the key "AgentEntry.class".
     *
     * @param context The operating context.
     * @return The value for Parameter #2.
     */
    public String getParameter2(final Context context);
    
   
    
} // class
