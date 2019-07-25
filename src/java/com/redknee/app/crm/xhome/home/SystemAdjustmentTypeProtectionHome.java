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

package com.redknee.app.crm.xhome.home;

import com.redknee.app.crm.bean.AdjustmentType;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;

/**
 * Protects system adjustment types from being removed.
 * 
 * @author cindy.wong@redknee.com
 * 
 */
public class SystemAdjustmentTypeProtectionHome extends HomeProxy
{

    private static final long serialVersionUID = 1L;

    public SystemAdjustmentTypeProtectionHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }

    public void remove(Context ctx, Object obj) throws HomeException
    {
        AdjustmentType adjustmentType = (AdjustmentType) getDelegate().find(ctx, obj);
        if (adjustmentType != null && adjustmentType.isSystem())
        {
            throw new HomeException("System adjustment type cannot be removed");
        }
    }
}
