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

public class BundleProfileSupportHelper extends SupportHelper
{

    private BundleProfileSupportHelper()
    {
    }


    /**
     * @deprecated Use contextualized version of method
     */
    @Deprecated
    public static BundleProfileSupport get()
    {
        return get(BundleProfileSupport.class, DefaultBundleProfileSupport.instance());
    }


    public static BundleProfileSupport get(Context ctx)
    {
        BundleProfileSupport instance = get(ctx, BundleProfileSupport.class, DefaultBundleProfileSupport
                .instance());
        return instance;
    }


    public static BundleProfileSupport set(Context ctx, BundleProfileSupport instance)
    {
        return register(ctx, BundleProfileSupport.class, instance);
    }
}
