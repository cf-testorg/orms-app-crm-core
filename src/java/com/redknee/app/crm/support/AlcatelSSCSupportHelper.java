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
 * This class should implement static versions of all {@link com.redknee.app.crm.support.AlcatelSSCSupport} methods.
 * 
 * The only change that should be made to method signatures in the interface is
 * to add the context to facilitate implementation class retrieval. 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class AlcatelSSCSupportHelper extends SupportHelper
{
    private AlcatelSSCSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static AlcatelSSCSupport get()
    {
        return get(AlcatelSSCSupport.class, DefaultAlcatelSSCSupport.instance());
    }
    
    public static AlcatelSSCSupport get(Context ctx)
    {
        AlcatelSSCSupport instance = get(ctx, AlcatelSSCSupport.class, DefaultAlcatelSSCSupport.instance());
        return instance;
    }
    
    public static AlcatelSSCSupport set(Context ctx, AlcatelSSCSupport instance)
    {
        return register(ctx, AlcatelSSCSupport.class, instance);
    }
}
