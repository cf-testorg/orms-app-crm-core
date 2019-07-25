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

package com.redknee.app.crm.bean.webcontrol;

import com.redknee.app.crm.bean.AdjustmentTypeEnum;
import com.redknee.app.crm.bean.AdjustmentTypeKeyWebControl;
import com.redknee.app.crm.bean.EarlyRewardConfigurationWebControl;
import com.redknee.app.crm.web.control.AdjustmentTypeCategoryProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

/**
 * @author cindy.wong@redknee.com
 * @since 8.4
 */
public class CustomEarlyRewardConfigurationWebControl extends EarlyRewardConfigurationWebControl
{
    @Override
    public WebControl getAdjustmentTypeWebControl()
    {
        return CUSTOM_ADJUSTMENT_TYPE_WC;
    }

    public static final WebControl CUSTOM_ADJUSTMENT_TYPE_WC = new AdjustmentTypeCategoryProxyWebControl(
            new AdjustmentTypeKeyWebControl(), AdjustmentTypeEnum.EarlyReward);

}
