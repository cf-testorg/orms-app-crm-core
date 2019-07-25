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
 * This class should implement static versions of all {@link com.redknee.app.crm.support.LicensingSupport} methods.
 *
 * The only change that should be made to method signatures in the interface is
 * to add the context to facilitate implementation class retrieval. 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public final class LicensingSupportHelper extends SupportHelper
{
    private LicensingSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static LicensingSupport get()
    {
        return get(LicensingSupport.class, DefaultLicensingSupport.instance());
    }
    
    public static LicensingSupport get(Context ctx)
    {
        LicensingSupport instance = get(ctx, LicensingSupport.class, DefaultLicensingSupport.instance());
        return instance;
    }
    
    public static LicensingSupport set(Context ctx, LicensingSupport instance)
    {
        return register(ctx, LicensingSupport.class, instance);
    }
}
