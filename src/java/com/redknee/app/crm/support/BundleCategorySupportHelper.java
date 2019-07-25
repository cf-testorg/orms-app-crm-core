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

public class BundleCategorySupportHelper extends SupportHelper
{

    private BundleCategorySupportHelper()
    {
    }


    /**
     * @deprecated Use contextualized version of method
     */
    @Deprecated
    public static BundleCategorySupport get()
    {
        return get(BundleCategorySupport.class, DefaultBundleCategorySupport.instance());
    }


    public static BundleCategorySupport get(Context ctx)
    {
        BundleCategorySupport instance = get(ctx, BundleCategorySupport.class, DefaultBundleCategorySupport
                .instance());
        return instance;
    }


    public static BundleCategorySupport set(Context ctx, BundleCategorySupport instance)
    {
        return register(ctx, BundleCategorySupport.class, instance);
    }
}
