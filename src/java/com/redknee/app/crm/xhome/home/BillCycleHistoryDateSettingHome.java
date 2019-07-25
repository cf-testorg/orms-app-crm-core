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
package com.redknee.app.crm.xhome.home;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;

import com.redknee.app.crm.bean.BillCycle;
import com.redknee.app.crm.bean.BillCycleHistory;
import com.redknee.app.crm.support.CalendarSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 9.1
 */
public class BillCycleHistoryDateSettingHome extends HomeProxy
{

    public BillCycleHistoryDateSettingHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx, Object obj) throws HomeException, HomeInternalException
    {
        populateBillCycleDay(ctx, obj);
        
        Object retObj = super.create(ctx, obj);
        
        return retObj;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object store(Context ctx, Object obj) throws HomeException, HomeInternalException
    {
        populateBillCycleDay(ctx, obj);
        Object retObj = super.store(ctx, obj);
        return retObj;
    }

    public void populateBillCycleDay(Context ctx, Object obj) throws HomeException
    {
        if (obj instanceof BillCycleHistory)
        {
            BillCycleHistory hist = (BillCycleHistory) obj;
            if (hist.getBillCycleChangeDate() == null)
            {
                hist.setBillCycleChangeDate(CalendarSupportHelper.get(ctx).getRunningDate(ctx));
            }
            
            BillCycle oldBC = HomeSupportHelper.get(ctx).findBean(ctx, BillCycle.class, hist.getOldBillCycleID());
            if (oldBC != null)
            {
                hist.setOldBillCycleDay(oldBC.getDayOfMonth());
            }
            else
            {
                hist.setOldBillCycleDay(-1);
            }
            
            BillCycle newBC = HomeSupportHelper.get(ctx).findBean(ctx, BillCycle.class, hist.getNewBillCycleID());
            if (newBC != null)
            {
                hist.setNewBillCycleDay(newBC.getDayOfMonth());
            }
            else
            {
                hist.setNewBillCycleDay(-1);
            }
        }
    }
}
