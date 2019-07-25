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

import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.ServicePeriodEnum;
import com.redknee.app.crm.service.ServicePeriodHandler;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class DefaultServicePeriodSupport implements ServicePeriodSupport
{
    protected static ServicePeriodSupport instance_ = null;
    public static ServicePeriodSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultServicePeriodSupport();
        }
        return instance_;
    }

    protected DefaultServicePeriodSupport()
    {
    }
    
    public ServicePeriodHandler getHandler(ServicePeriodEnum servicePeriod)
    {
        // Handlers are implemented in specific application (e.g. AppCrm).
        // Such apps should override this support method.
        return null;
    }
    
    public boolean usesSpecialHandler(Context ctx, ServicePeriodEnum servicePeriod)
    {
        return !SafetyUtil.safeEquals(
                getHandler(servicePeriod), 
                ChargingCycleSupportHelper.get(ctx).getHandler(servicePeriod.getChargingCycle()));
    }
}
