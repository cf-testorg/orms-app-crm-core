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
public class CronTaskSupportHelper extends SupportHelper
{
    private CronTaskSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static CronTaskSupport get()
    {
        return get(CronTaskSupport.class, DefaultCronTaskSupport.instance());
    }
    
    public static CronTaskSupport get(Context ctx)
    {
        CronTaskSupport instance = get(ctx, CronTaskSupport.class, DefaultCronTaskSupport.instance());
        return instance;
    }
    
    public static CronTaskSupport set(Context ctx, CronTaskSupport instance)
    {
        return register(ctx, CronTaskSupport.class, instance);
    }
}