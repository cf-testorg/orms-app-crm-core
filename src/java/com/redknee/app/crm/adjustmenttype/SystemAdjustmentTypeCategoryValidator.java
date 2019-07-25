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

import com.redknee.app.crm.bean.AdjustmentType;
import com.redknee.app.crm.bean.AdjustmentTypeHome;
import com.redknee.app.crm.bean.AdjustmentTypeXInfo;
import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

/**
 * Prevents system adjustment types from being moved.
 * 
 * @author cindy.wong@redknee.com
 * 
 */
public class SystemAdjustmentTypeCategoryValidator implements Validator
{

    /**
     * Creates a new system adjustment type validator.
     */
    protected SystemAdjustmentTypeCategoryValidator()
    {
        // empty
    }

    public static SystemAdjustmentTypeCategoryValidator instance()
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
            Home home = (Home) ctx.get(AdjustmentTypeHome.class);
            AdjustmentType oldAdjustmentType = null;
            try
            {
                oldAdjustmentType = (AdjustmentType) home.find(ctx, new EQ(
                        AdjustmentTypeXInfo.CODE, adjustmentType.getCode()));
            }
            catch (HomeException exception)
            {
                el.thrown(new IllegalStateException(
                        "Error finding old adjustment type", exception));
            }
            
            if (oldAdjustmentType != null
                    && oldAdjustmentType.getParentCode() != adjustmentType
                            .getParentCode())
            {
                el.thrown(new IllegalPropertyArgumentException(
                        AdjustmentTypeXInfo.PARENT_CODE,
                        "System adjustment type cannot be moved"));
            }
        }
        el.throwAll();
    }

    private static SystemAdjustmentTypeCategoryValidator instance_ = new SystemAdjustmentTypeCategoryValidator();
}
