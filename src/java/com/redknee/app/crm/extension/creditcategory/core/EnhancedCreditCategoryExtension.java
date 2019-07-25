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

import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.bean.CreditCategory;
import com.redknee.app.crm.extension.creditcategory.AbstractCreditCategoryExtension;
import com.redknee.app.crm.support.BeanLoaderSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;


/**
 * This class is purposely package-private and final.  It is only intended for use by core credit category concrete classes.
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
final class EnhancedCreditCategoryExtension
{
    public static CreditCategory getCreditCategory(Context ctx, int creditCategory)
    {
        CreditCategory category = BeanLoaderSupportHelper.get(ctx).getBean(ctx, CreditCategory.class);
        if( category != null 
                && (AbstractCreditCategoryExtension.DEFAULT_CREDITCATEGORY == creditCategory
                        || (category.getCode() == creditCategory)) )
        {
            return category;
        }
        
        if( AbstractCreditCategoryExtension.DEFAULT_CREDITCATEGORY == creditCategory )
        {
            return null;
        }
        
        try
        {
            category = HomeSupportHelper.get(ctx).findBean(
                    ctx,
                    CreditCategory.class,
                    creditCategory);
        }
        catch (HomeException e)
        {
        } 
        
        if( category != null && SafetyUtil.safeEquals(category.getCode(), creditCategory) )
        {
            return category;
        }
        
        return null;
    }
}
