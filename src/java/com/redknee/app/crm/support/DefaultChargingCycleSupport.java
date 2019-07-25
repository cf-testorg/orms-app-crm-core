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
package com.redknee.app.crm.support;

import com.redknee.app.crm.bean.ChargingCycleEnum;
import com.redknee.app.crm.service.ChargingCycleHandler;
import com.redknee.app.crm.service.DailyPeriodHandler;
import com.redknee.app.crm.service.DefaultHandler;
import com.redknee.app.crm.service.MonthlyPeriodHandler;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class DefaultChargingCycleSupport implements ChargingCycleSupport
{
    protected static ChargingCycleSupport instance_ = null;
    public static ChargingCycleSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultChargingCycleSupport();
        }
        return instance_;
    }

    protected DefaultChargingCycleSupport()
    {
    }
    
    public ChargingCycleHandler getHandler(ChargingCycleEnum chargingCycle)
    {
        switch (chargingCycle.getIndex())
        {
        case ChargingCycleEnum.MONTHLY_INDEX:
            return MonthlyPeriodHandler.instance();

        case ChargingCycleEnum.DAILY_INDEX:
            return DailyPeriodHandler.instance();
        }

        // Other handlers are implemented in specific application (e.g. AppCrm).
        // Such apps should override this support method.
        return DefaultHandler.instance();
    
    }
}
