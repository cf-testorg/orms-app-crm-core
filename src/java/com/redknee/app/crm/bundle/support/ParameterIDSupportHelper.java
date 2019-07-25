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
package com.redknee.app.crm.bundle.support;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.support.SupportHelper;

/**
 * Support class for dealing with Parameter ID on corba calls with Bundle Manager
 *
 */
public class ParameterIDSupportHelper extends SupportHelper
{
    private ParameterIDSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    @Deprecated
    public static ParameterIDSupport get()
    {
        return get(ParameterIDSupport.class, DefaultParameterIDSupport.instance());
    }
    
    public static ParameterIDSupport get(Context ctx)
    {
        ParameterIDSupport instance = get(ctx, ParameterIDSupport.class, DefaultParameterIDSupport.instance());
        return instance;
    }
    
    public static ParameterIDSupport set(Context ctx, ParameterIDSupport instance)
    {
        return register(ctx, ParameterIDSupport.class, instance);
    }
}
