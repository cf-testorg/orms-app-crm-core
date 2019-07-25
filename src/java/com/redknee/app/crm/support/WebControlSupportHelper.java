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
package com.redknee.app.crm.support;

import com.redknee.framework.xhome.context.Context;

/**
 * @author jchen
 */
public class WebControlSupportHelper extends SupportHelper
{
    private WebControlSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static WebControlSupport get()
    {
        return get(WebControlSupport.class, DefaultWebControlSupport.instance());
    }
    
    public static WebControlSupport get(Context ctx)
    {
        WebControlSupport instance = get(ctx, WebControlSupport.class, DefaultWebControlSupport.instance());
        return instance;
    }
    
    public static WebControlSupport set(Context ctx, WebControlSupport instance)
    {
        return register(ctx, WebControlSupport.class, instance);
    }
}
