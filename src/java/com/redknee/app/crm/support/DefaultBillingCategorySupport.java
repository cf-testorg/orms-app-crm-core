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

import java.util.List;

import com.redknee.app.crm.bean.calldetail.BillingCategory;
import com.redknee.app.crm.bean.calldetail.BillingCategoryEnum;
import com.redknee.app.crm.bean.calldetail.BillingCategoryHome;
import com.redknee.app.crm.bean.calldetail.BillingCategoryStateEnum;
import com.redknee.app.crm.bean.calldetail.BillingCategoryXInfo;
import com.redknee.app.crm.bean.calldetail.InvoiceXmlDetailFormatEnum;
import com.redknee.app.crm.bean.calldetail.UsageGroupEnum;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.Or;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;


/**
 * Provides utility functions for working with AdjustmentTypes.
 * 
 * @author bhupendra.pandey@redknee.com
 */
public final class DefaultBillingCategorySupport implements BillingCategorySupport
{

    /**
     * We'll use this flag to turn off debug messages for this class. If you need to do
     * local debugging, set to true, but don't commit a true value.
     */
    private static final boolean LOCAL_DEBUG = false;
    protected static BillingCategorySupport instance_ = null;


    public static BillingCategorySupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultBillingCategorySupport();
        }
        return instance_;
    }


    protected DefaultBillingCategorySupport()
    {
    }


    @Override
    public List<BillingCategory> getActiveAndInactiveBillingCategories(Context context) throws HomeException
    {
        Home billingCategoryHome = (Home) context.get(BillingCategoryHome.class);
        Or or = new Or();
        or.add(new EQ(BillingCategoryXInfo.STATUS, BillingCategoryStateEnum.ACTIVE));
        or.add(new EQ(BillingCategoryXInfo.STATUS, BillingCategoryStateEnum.INACTIVE));
        return (List<BillingCategory>)billingCategoryHome.select(context, or);
    }


    @Override
    public BillingCategory getBillingCategoryByBillingCategoryEnum(Context context,
            BillingCategoryEnum billingCategoryEnum) throws HomeException
    {
        return HomeSupportHelper.get(context).findBean(context, BillingCategory.class, 
                new EQ(BillingCategoryXInfo.ID, Short.valueOf((billingCategoryEnum.getIndex()))));
    }


    @Override
    public BillingCategory getBillingCategoryById(Context context, short billingCategoryId) throws HomeException
    {
        Home billingCategoryHome = (Home) context.get(BillingCategoryHome.class);
        return (BillingCategory) billingCategoryHome.find(context, Short.valueOf(billingCategoryId));
    }


    @Override
    public List<BillingCategory> getBillingCategoriesForInvoiceXmlDetailFormatEnum(Context context,
            InvoiceXmlDetailFormatEnum invoiceXmlDetailFormatEnum) throws HomeException
    {
        Home billingCategoryHome = (Home) context.get(BillingCategoryHome.class);
        Or or = new Or();
        or.add(new EQ(BillingCategoryXInfo.STATUS, BillingCategoryStateEnum.ACTIVE));
        or.add(new EQ(BillingCategoryXInfo.STATUS, BillingCategoryStateEnum.INACTIVE));
        And and = new And();
        and.add(new EQ(BillingCategoryXInfo.TYPE_OF_XML_FORMAT, invoiceXmlDetailFormatEnum)).add(or);
        return (List<BillingCategory>)billingCategoryHome.select(context, and);
    }
    
    @Override
    public UsageGroupEnum getUsageGroupByBillingCategoryId(final Context context, short billingCategoryId)
        throws HomeException
    {
        BillingCategory bc = this.getBillingCategoryById(context, billingCategoryId);
        short xmlFormatIndex = bc.getTypeOfXmlFormat().getIndex();
        return getUsageGroupByXmlFormatId(xmlFormatIndex);
    }

    @Override
    public UsageGroupEnum getUsageGroupByBillingCategory(Context context,
            BillingCategory billingCategory) //throws HomeException
    {
        if(billingCategory==null)
            return UsageGroupEnum.NONE;
        
        return getUsageGroupByXmlFormatId(billingCategory.getTypeOfXmlFormat().getIndex());
    }


    /**
     * @param xmlFormatIndex
     * @return
     */
    public UsageGroupEnum getUsageGroupByXmlFormatId(short xmlFormatIndex)
    {
        switch(xmlFormatIndex)
        {
            case InvoiceXmlDetailFormatEnum.DATA_INFORMATION_INDEX:
            case InvoiceXmlDetailFormatEnum.ROAMING_DATA_INDEX:
                return UsageGroupEnum.DATA;
            case InvoiceXmlDetailFormatEnum.CALL_INDEX:
            case InvoiceXmlDetailFormatEnum.ROAMING_CALLS_INDEX:
                return UsageGroupEnum.VOICE;
            case InvoiceXmlDetailFormatEnum.TEXT_MESSAGES_INDEX:
                return UsageGroupEnum.SMS;
                
            default:
                return UsageGroupEnum.NONE;
        }
    }

    
} // class
