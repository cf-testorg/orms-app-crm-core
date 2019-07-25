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

package com.redknee.app.crm.home;

import com.redknee.app.crm.bean.IdentifierEnum;
import com.redknee.app.crm.bean.PricePlan;
import com.redknee.app.crm.support.IdentifierSequenceSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.LogSupport;

/**
 * 
 * Sets primary key for this Price Plan using an
 * IdentifierSequence. 
 * 
 * @author bdhavalshankh
 * @since 9.5.6
 *
 */
public class PricePlanIDSettingHome extends HomeProxy 
{
    private static final long startValue = 100000;
    
    public PricePlanIDSettingHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }

    @Override
    public Object create(Context ctx, Object obj) throws HomeException,
            HomeInternalException 
    {
        PricePlan pricePlan = (PricePlan) obj;
        
        IdentifierSequenceSupportHelper.get(ctx).ensureSequenceExists(ctx, IdentifierEnum.PRICEPLAN_ID,
                startValue, Long.MAX_VALUE);
        
        long pricePlanID = IdentifierSequenceSupportHelper.get(ctx).getNextIdentifier(
                ctx,
                IdentifierEnum.PRICEPLAN_ID,
                null);
        
        if(pricePlan.getId() == PricePlan.DEFAULT_ID)
        {
            pricePlan.setId(pricePlanID);
            pricePlan.setIdentifier(pricePlanID);
        }
        
        LogSupport.info(ctx, this, "Price plan ID set to: " + pricePlan.getId());
        
        return super.create(ctx, pricePlan);
    }

    
}
