/*
 * Created on Feb 23, 2005
 *
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
package com.redknee.app.crm.support;

import com.redknee.framework.xhome.context.Context;

public class CorbaSupportHelper extends SupportHelper
{
    private CorbaSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    @Deprecated
    public static CorbaSupport get()
    {
        return get(CorbaSupport.class, DefaultCorbaSupport.instance());
    }
    
    public static CorbaSupport get(Context ctx)
    {
        CorbaSupport instance = get(ctx, CorbaSupport.class, DefaultCorbaSupport.instance());
        return instance;
    }
    
    public static CorbaSupport set(Context ctx, CorbaSupport instance)
    {
        return register(ctx, CorbaSupport.class, instance);
    }
}
