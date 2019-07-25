/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily
 * available. Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee. No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used in
 * accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */

package com.redknee.app.crm.adjustmenttype;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.bean.AdjustmentType;
import com.redknee.app.crm.bean.AdjustmentTypeEnum;
import com.redknee.app.crm.bean.AdjustmentTypeXInfo;
import com.redknee.app.crm.home.core.CoreAdjustmentTypeHomePipelineFactory;

/**
 * Limits manually created adjustment type to certain system categories.
 * 
 * @author cindy.wong@redknee.com
 * 
 */
public class ManualAdjustmentTypeCategoryValidator implements Validator
{

    protected ManualAdjustmentTypeCategoryValidator()
    {
        // empty
    }

    public static ManualAdjustmentTypeCategoryValidator instance()
    {
        return instance_;
    }

    /**
     * @see com.redknee.framework.xhome.beans.Validator#validate(com.redknee.framework.xhome.context.Context,
     *      java.lang.Object)
     */
    @Override
    public void validate(Context ctx, Object obj) throws IllegalStateException
    {
        CompoundIllegalStateException el = new CompoundIllegalStateException();
        
        AdjustmentType adjustmentType = (AdjustmentType) obj;

        if (adjustmentType.isSystem())
        {
            el.thrown(new IllegalPropertyArgumentException(
                    AdjustmentTypeXInfo.SYSTEM,
                    "Creation of system adjustment types is not allowed"));
        }

        boolean allowed = false;
        int parent = adjustmentType.getParentCode();
        while (parent != 0)
        {
            if (allowedCategories.contains(new Integer(parent)))
            {
                allowed = true;
                break;
            }

            Home home = (Home) ctx
                    .get(CoreAdjustmentTypeHomePipelineFactory.ADJUSTMENT_TYPE_READ_ONLY_HOME);
            try
            {
                adjustmentType = (AdjustmentType) home.find(ctx, new EQ(
                        AdjustmentTypeXInfo.CODE, parent));
            }
            catch (HomeException exception)
            {
                el.thrown(new IllegalStateException(
                        "Error trying to look up ancestor", exception));
            }

            if (adjustmentType == null)
            {
                el.thrown(new IllegalPropertyArgumentException(
                        AdjustmentTypeXInfo.PARENT_CODE,
                        "Error looking up ancestor " + parent));
            }
            parent = adjustmentType.getParentCode();
        }

        if (!allowed)
        {
            StringBuffer sb = new StringBuffer();
            for (Integer cat : allowedCategories)
            {
                sb.append('"');
                AdjustmentTypeEnum e = null;
                for (Iterator i = AdjustmentTypeEnum.COLLECTION.iterator(); i.hasNext();)
                {
                    AdjustmentTypeEnum cur = (AdjustmentTypeEnum) i.next();
                    if (cur.getIndex() == cat.shortValue())
                    {
                        e = cur;
                        break;
                    }
                }
                sb.append(e.getDescription(ctx));
                sb.append("\" ");
            }
            el.thrown(new IllegalPropertyArgumentException(
                    AdjustmentTypeXInfo.PARENT_CODE,
                    "Adjustment types can only be created under one of the following categories: "
                            + sb.toString()));
        }
        el.throwAll();
    }

    private static final Set<Integer> allowedCategories = new HashSet<Integer>(
            Arrays.asList(new Integer[]
                    {
                            Integer.valueOf(AdjustmentTypeEnum.DepositPayments.getIndex()),
                            Integer.valueOf(AdjustmentTypeEnum.DepositReleaseCategory.getIndex()),
                            Integer.valueOf(AdjustmentTypeEnum.OneTimeCharges.getIndex()),
                            Integer.valueOf(AdjustmentTypeEnum.Other.getIndex()),
                            Integer.valueOf(AdjustmentTypeEnum.Notes.getIndex()),
                            Integer.valueOf(AdjustmentTypeEnum.StandardPayments.getIndex()),
	            Integer.valueOf(AdjustmentTypeEnum.WriteOffMoneyBalance
	                .getIndex()),
	            Integer.valueOf(AdjustmentTypeEnum.LateFee.getIndex())
                    }));

    private static ManualAdjustmentTypeCategoryValidator instance_ = new ManualAdjustmentTypeCategoryValidator();
}
