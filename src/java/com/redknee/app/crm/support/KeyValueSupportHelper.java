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
 * This class should implement static versions of all {@link com.redknee.app.crm.support.KeyValueSupport} methods.
 * 
 * The only change that should be made to method signatures in the interface is
 * to add the context to facilitate implementation class retrieval. 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class KeyValueSupportHelper extends SupportHelper
{
    private KeyValueSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static KeyValueSupport get()
    {
        return get(KeyValueSupport.class, DefaultKeyValueSupport.instance());
    }
    
    public static KeyValueSupport get(Context ctx)
    {
        KeyValueSupport instance = get(ctx, KeyValueSupport.class, DefaultKeyValueSupport.instance());
        return instance;
    }
    
    public static KeyValueSupport set(Context ctx, KeyValueSupport instance)
    {
        return register(ctx, KeyValueSupport.class, instance);
    }
}
