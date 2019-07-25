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
package com.redknee.app.crm.extension.creditcategory.core;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.CreditCategory;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.4
 */
public class EarlyRewardCreditCategoryExtension extends com.redknee.app.crm.extension.creditcategory.EarlyRewardCreditCategoryExtension
{
    /**
     * Enhanced to use bean in context if available.
     * 
     * {@inheritDoc}
     */
    @Override
    public CreditCategory getCreditCategory(Context ctx)
    {
        return EnhancedCreditCategoryExtension.getCreditCategory(ctx, this.getCreditCategory());
    }
}
