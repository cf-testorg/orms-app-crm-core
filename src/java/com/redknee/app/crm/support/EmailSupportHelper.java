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
 * This class should implement static versions of all {@link com.redknee.app.crm.support.EmailSupport} methods.
 * 
 * The only change that should be made to method signatures in the interface is
 * to add the context to facilitate implementation class retrieval. 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class EmailSupportHelper extends SupportHelper 
{
    private EmailSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    @Deprecated
    public static EmailSupport get()
    {
        return get(EmailSupport.class, DefaultEmailSupport.instance());
    }
    
    public static EmailSupport get(Context ctx)
    {
        EmailSupport instance = get(ctx, EmailSupport.class, DefaultEmailSupport.instance());
        return instance;
    }
    
    public static EmailSupport set(Context ctx, EmailSupport instance)
    {
        return register(ctx, EmailSupport.class, instance);
    }
}
