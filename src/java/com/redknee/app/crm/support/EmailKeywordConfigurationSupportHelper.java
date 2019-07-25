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
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class EmailKeywordConfigurationSupportHelper extends SupportHelper
{
    private EmailKeywordConfigurationSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static EmailKeywordConfigurationSupport get()
    {
        return get(EmailKeywordConfigurationSupport.class, DefaultEmailKeywordConfigurationSupport.instance());
    }
    
    public static EmailKeywordConfigurationSupport get(Context ctx)
    {
        EmailKeywordConfigurationSupport instance = get(ctx, EmailKeywordConfigurationSupport.class, DefaultEmailKeywordConfigurationSupport.instance());
        return instance;
    }
    
    public static EmailKeywordConfigurationSupport set(Context ctx, EmailKeywordConfigurationSupport instance)
    {
        return register(ctx, EmailKeywordConfigurationSupport.class, instance);
    }
}
