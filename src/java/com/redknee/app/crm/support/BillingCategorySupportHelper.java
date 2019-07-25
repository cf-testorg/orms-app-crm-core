/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.support;

import com.redknee.framework.xhome.context.Context;


/**
 * This class eases installation/retrieval of the AdjustmentTypeSupport interface
 * 
 * @author bhupendra.pandey@redknee.com
 */
public final class BillingCategorySupportHelper extends SupportHelper
{

    private BillingCategorySupportHelper()
    {
    }


    /**
     * @deprecated Use contextualized version of method
     */
    public static BillingCategorySupport get()
    {
        return get(BillingCategorySupport.class, DefaultBillingCategorySupport.instance());
    }


    public static BillingCategorySupport get(Context ctx)
    {
        BillingCategorySupport instance = get(ctx, BillingCategorySupport.class, DefaultBillingCategorySupport
                .instance());
        return instance;
    }


    public static BillingCategorySupport set(Context ctx, BillingCategorySupport instance)
    {
        return register(ctx, BillingCategorySupport.class, instance);
    }
} // class
