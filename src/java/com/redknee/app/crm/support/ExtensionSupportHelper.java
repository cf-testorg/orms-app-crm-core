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
package com.redknee.app.crm.support;

import com.redknee.framework.xhome.context.Context;


/**
 * This class should implement static versions of all {@link com.redknee.app.crm.support.ExtensionSupport} methods.
 * 
 * The only change that should be made to method signatures in the interface is
 * to add the context to facilitate implementation class retrieval. 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class ExtensionSupportHelper extends SupportHelper
{
    private ExtensionSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static ExtensionSupport get()
    {
        return get(ExtensionSupport.class, DefaultExtensionSupport.instance());
    }
    
    public static ExtensionSupport get(Context ctx)
    {
        ExtensionSupport instance = get(ctx, ExtensionSupport.class, DefaultExtensionSupport.instance());
        return instance;
    }
    
    public static ExtensionSupport set(Context ctx, ExtensionSupport instance)
    {
        return register(ctx, ExtensionSupport.class, instance);
    }
}
