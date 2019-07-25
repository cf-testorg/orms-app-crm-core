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
package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.AdjustmentTypeTableWebControl;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CustomAdjustmentTypeTableWebControl extends AdjustmentTypeTableWebControl
{
    @Override
    public WebControl getParentCodeWebControl()
    {
        return CustomAdjustmentTypeWebControl.CUSTOM_PARENT_CODE_WC;
    }

    @Override
    public WebControl getCategoryWebControl()
    {
        return CustomAdjustmentTypeWebControl.CUSTOM_CATEGORY_WC;
    }

    @Override
    public WebControl getAdjustmentSpidInfoWebControl()
    {
        return CustomAdjustmentTypeWebControl.CUSTOM_ADJUSTMENT_SPID_INFO_WC;
    }
}
