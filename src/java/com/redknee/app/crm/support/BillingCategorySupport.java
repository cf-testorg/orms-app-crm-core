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

import java.util.Collection;
import java.util.List;

import com.redknee.app.crm.bean.calldetail.BillingCategory;
import com.redknee.app.crm.bean.calldetail.BillingCategoryEnum;
import com.redknee.app.crm.bean.calldetail.InvoiceXmlDetailFormatEnum;
import com.redknee.app.crm.bean.calldetail.UsageGroupEnum;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;


public interface BillingCategorySupport extends Support
{

    /**
     * Gets the BillingCategory for the given BillingCategoryEnum
     * 
     * @param context
     * @param billingCategoryEnum
     * @return
     * @throws HomeException
     */
    public BillingCategory getBillingCategoryByBillingCategoryEnum(final Context context,
            final BillingCategoryEnum billingCategoryEnum) throws HomeException;


    /**
     * Gets the BillingCategory for the given BillingCategory Id
     * 
     * @param context
     * @param billingCategoryId
     * @return
     * @throws HomeException
     */
    public BillingCategory getBillingCategoryById(final Context context, final short billingCategoryId)
            throws HomeException;


    /**
     * Gets BillingCategories List for InvoiceXmlDetailFormatEnum
     * 
     * @param context
     * @param invoiceXmlDetailFormatEnum
     * @return
     * @throws HomeException
     */
    public List<BillingCategory> getBillingCategoriesForInvoiceXmlDetailFormatEnum(final Context context,
            final InvoiceXmlDetailFormatEnum invoiceXmlDetailFormatEnum) throws HomeException;


    /**
     * Gets the active BillingCategory
     * 
     * @param context
     * @return
     * @throws HomeException
     */
    public List<BillingCategory> getActiveAndInactiveBillingCategories(final Context context)
            throws HomeException;
    
    
    /**
     * Gets whether the the billing category corresponds to VOICE, SMS or DATA
     * @param context
     * @param billingCategoryId
     * @return
     * @throws HomeException
     */
    public UsageGroupEnum getUsageGroupByBillingCategoryId(final Context context, short billingCategoryId)
                throws HomeException;
    
    /**
     * 
     * @param context
     * @param billingCategory
     * @return
     * @throws HomeException
     */
    public UsageGroupEnum getUsageGroupByBillingCategory(final Context context, final BillingCategory billingCategory);
}
