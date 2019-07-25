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

/**
 * Payment Plan History Pipeline Creator 
 * 
 * @author ali
 */
import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.home.SortingHome;

import com.redknee.app.crm.bean.payment.PaymentPlanHistory;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.PaymentPlanSupportHelper;
import com.redknee.app.crm.support.StorageSupportHelper;

public class CorePaymentPlanHistoryHomePipelineFactory implements PipelineFactory
{

    public Home createPipeline(Context ctx, Context serverCtx)
            throws RemoteException, HomeException, IOException, AgentException 
    {
        Home historyHome = null; 
            
        if (PaymentPlanSupportHelper.get(ctx).isHistoryEnabled(ctx))
        {
            historyHome = StorageSupportHelper.get(ctx).createHome(ctx, PaymentPlanHistory.class, "PAYMENTPLANHISTORY");
            historyHome = new SortingHome(historyHome);
        }
        else
        {
            historyHome = new DisablePaymentPlanHistoryHome();
        }

        return historyHome;
    }
    
    private class DisablePaymentPlanHistoryHome extends HomeProxy
    {
        
        @Override
        public Object create(Context ctx, Object obj) throws HomeException
        {
            throw new HomeException("The License to Disable Payment Plan History is enabled.  " +
                    "Disable this license to use the Payment Plan History Feature.");
        }
        
        @Override
        public Object store(Context ctx, Object obj) throws HomeException
        {
            throw new HomeException("The License to Disable Payment Plan History is enabled.  " +
                    "Disable this license to use the Payment Plan History Feature.");
        }
        
        @Override
        public Object find(Context ctx, Object obj) throws HomeException
        {
            throw new HomeException("The License to Disable Payment Plan History is enabled.  " +
                    "Disable this license to use the Payment Plan History Feature.");
        }
        
        @Override
        public void remove(Context ctx, Object obj) throws HomeException
        {
            throw new HomeException("The License to Disable Payment Plan History is enabled.  " +
                    "Disable this license to use the Payment Plan History Feature.");
        }
        
        @Override
        public void removeAll(Context ctx, Object obj) throws HomeException
        {
            throw new HomeException("The License to Disable Payment Plan History is enabled.  " +
                    "Disable this license to use the Payment Plan History Feature.");
        }
    }
}
