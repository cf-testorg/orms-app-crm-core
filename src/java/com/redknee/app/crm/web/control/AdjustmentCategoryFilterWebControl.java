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
package com.redknee.app.crm.web.control;

import java.io.PrintWriter;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.AdjustmentTypeEnum;
import com.redknee.app.crm.bean.Transaction;
import com.redknee.app.crm.support.AdjustmentTypeSupportHelper;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class AdjustmentCategoryFilterWebControl extends ProxyWebControl
{
    private final AdjustmentTypeEnum[] category_;
    private final boolean showOnMatch_;

    public AdjustmentCategoryFilterWebControl(WebControl delegate, AdjustmentTypeEnum... category)
    {
        this(delegate, true, category);
    }
    
    public AdjustmentCategoryFilterWebControl(WebControl delegate, boolean showOnMatch, AdjustmentTypeEnum... category)
    {
        super(delegate);
        category_ = category;
        showOnMatch_ = showOnMatch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toWeb(Context ctx, PrintWriter out, String name, Object beanObj)
    {
        boolean isOutput = false;
        if (beanObj instanceof Transaction)
        {
            for (AdjustmentTypeEnum category : category_)
            {
                if (AdjustmentTypeSupportHelper.get(ctx).isInCategory(ctx, ((Transaction)beanObj).getAdjustmentType(), category))
                {
                    if (showOnMatch_)
                    {
                        super.toWeb(ctx, out, name, beanObj);
                        isOutput = true;
                    }
                    break;
                }
            }
        }
        if (!isOutput && !showOnMatch_)
        {
            super.toWeb(ctx, out, name, beanObj);
        }
    }
}
