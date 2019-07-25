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
package com.redknee.app.crm.home.core;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Comparator;

import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NoSelectAllHome;
import com.redknee.framework.xhome.home.SortingHome;

import com.redknee.app.crm.bean.BillCycleHistory;
import com.redknee.app.crm.bean.BillCycleHistoryXDBHome;
import com.redknee.app.crm.home.NoRemoveHome;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.xhome.home.BillCycleHistoryDateSettingHome;
import com.redknee.app.crm.xhome.home.UserAgentHome;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 9.1
 */
public class CoreBillCycleHistoryHomePipelineFactory implements PipelineFactory
{
    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = StorageSupportHelper.get(ctx).createHome(ctx, BillCycleHistory.class, BillCycleHistoryXDBHome.DEFAULT_TABLE_NAME);
        
        home = new BillCycleHistoryDateSettingHome(ctx, home);
        
        home = new UserAgentHome(ctx, home);

        home = new NoRemoveHome(home);

        home = new SortingHome(ctx, home, new BillCycleHistoryComparator());
        
        home = new NoSelectAllHome(home);
        
        return home;
    }

    /**
     * Sort by date in descending order first, then by BAN.
     */
    private static final class BillCycleHistoryComparator implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2)
        {
            BillCycleHistory h1 = (BillCycleHistory) o1;
            BillCycleHistory h2 = (BillCycleHistory) o2;
            int result = SafetyUtil.safeCompare(
                    h2.getBillCycleChangeDate(),
                    h1.getBillCycleChangeDate());
            if (result == 0)
            {
                result = SafetyUtil.safeCompare(h1.getBAN(), h2.getBAN());
            }
            return result;
        }
    }
}
