/*
* This code is a protected work and subject to domestic and international
* copyright law(s). A complete listing of authors of this work is readily
* available. Additionally, source code is, by its very nature, confidential
* information and inextricably contains trade secrets and other information
* proprietary, valuable and sensitive to Redknee, no unauthorised use,
* disclosure, manipulation or otherwise is permitted, and may only be used
* in accordance with the terms of the licence agreement entered into with
* Redknee Inc. and/or its subsidiaries.
*
* Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
*/


package com.redknee.app.crm.web.control;

import com.redknee.app.crm.bean.AdjustmentTypeHome;
import com.redknee.app.crm.bean.AdjustmentTypeXInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;


/**
 * @author victor.stratan@redknee.com
 *
 * This web control proxy is used to filter ether category adjustment types or 
 * adjustment types which are not categories.  
 */
public class AdjustmentTypeFilterCategoryProxyWebControl extends ProxyWebControl
{

    public AdjustmentTypeFilterCategoryProxyWebControl(
            final WebControl delegate,
            final boolean category,
            final int mode)
    {
        super(delegate);
        category_ = Boolean.valueOf(category);
        mode_ = mode;
    }


    public AdjustmentTypeFilterCategoryProxyWebControl(
            final WebControl delegate,
            final boolean category)
    {
        this(delegate, category, NOT_SPECIFIED);
    }


    public Context wrapContext(Context ctx)
    {
        if (mode_ != NOT_SPECIFIED && ctx.getInt("MODE", mode_) != mode_)
        {
            return ctx;
        }
        else
        {
            Context subCtx = ctx.createSubContext();
            final Home adjustmentHome = (Home)ctx.get(AdjustmentTypeHome.class);
            Home  whereHome = adjustmentHome.where(ctx, new EQ(AdjustmentTypeXInfo.CATEGORY, category_));

            subCtx.put(AdjustmentTypeHome.class, whereHome);
            return subCtx;
        }

    }

    private final static int NOT_SPECIFIED = -1;

    private Boolean category_;
    private int mode_;
}
