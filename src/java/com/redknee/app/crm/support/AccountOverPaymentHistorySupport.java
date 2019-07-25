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

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.GT;
import com.redknee.framework.xhome.elang.LT;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeSPI;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.app.crm.bean.AccountOverPaymentHistoryHome;
import com.redknee.app.crm.bean.AccountOverPaymentHistoryXInfo;
import com.redknee.app.crm.bean.AccountOverPaymentHistory;
import com.redknee.app.crm.bean.AdjustmentTypeEnum;
import com.redknee.app.crm.bean.GroupTypeEnum;
import com.redknee.app.crm.bean.Transaction;


/**
 * @author alok.sohani
 * @since 9_7_2
 */
public class AccountOverPaymentHistorySupport implements Support {
	
	protected static AccountOverPaymentHistorySupport instance_ = null;
    public static AccountOverPaymentHistorySupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new AccountOverPaymentHistorySupport();
        }
        return instance_;
    }

    protected AccountOverPaymentHistorySupport()
    {
    }

    /**
     * Used at the time of Payment distribution
     *
     */      
	public static void createAccountOverPaymentHistoryTransaction(final Context context, final String BAN,final int Spid,final int billCycleID,final long paymentAmount,final int AdjustmentType,final long ReceiptNum,final long undistributedOverPayment) throws HomeInternalException, HomeException
    {
		if(LogSupport.isDebugEnabled(context))
		{
			LogSupport.debug(context, AccountOverPaymentHistorySupport.class, "AccountOverPaymentHistorySupport.createAccountOverPaymentHistoryTransaction : Started ");
		}    
		Home home = (Home) context.get(AccountOverPaymentHistoryHome.class);
		
    	final Predicate filter =new And().add(new EQ(com.redknee.app.crm.bean.AccountOverPaymentHistoryXInfo.BAN,BAN))
    									 .add(new EQ(com.redknee.app.crm.bean.AccountOverPaymentHistoryXInfo.LATEST,true));

    	AccountOverPaymentHistory accountOverPaymentHistoryOld = null;
    	
    	accountOverPaymentHistoryOld=(AccountOverPaymentHistory) ((Home) context.get(AccountOverPaymentHistoryHome.class)).find(context,filter);
    	
    	AccountOverPaymentHistory accountOverPaymentHistoryNew=new AccountOverPaymentHistory();
    	
    //	final long newOverpaymentAmount=undistributedOverPayment;
    	final long overpaymentDebitAmount=0L;
    	final long oldOverpaymentBalance;
    	final long newOverpaymentBalance;
    	final long distributedAmount = paymentAmount + (-1)*undistributedOverPayment;
    	
    	if (accountOverPaymentHistoryOld==null)   
    	{
    		oldOverpaymentBalance=0;
    	}
    	else
    	{
    		oldOverpaymentBalance=accountOverPaymentHistoryOld.getNewOverpaymentBalance();
    	}
    	
    	newOverpaymentBalance=undistributedOverPayment+overpaymentDebitAmount+oldOverpaymentBalance;
    	
    	accountOverPaymentHistoryNew.setBan(BAN);
    	accountOverPaymentHistoryNew.setSpid(Spid);
    	accountOverPaymentHistoryNew.setBillCycleID(billCycleID);
    	accountOverPaymentHistoryNew.setAdjustmentType(AdjustmentType);
    	accountOverPaymentHistoryNew.setPaymentAmount(paymentAmount);
    	accountOverPaymentHistoryNew.setDistributedAmount(distributedAmount);
    	accountOverPaymentHistoryNew.setNewOverpaymentAmount(undistributedOverPayment);    	
    	accountOverPaymentHistoryNew.setOverpaymentDebitAmount(overpaymentDebitAmount);
    	accountOverPaymentHistoryNew.setOldOverpaymentBalance(oldOverpaymentBalance);
    	accountOverPaymentHistoryNew.setNewOverpaymentBalance(newOverpaymentBalance);
    	accountOverPaymentHistoryNew.setPaymentReference(ReceiptNum);
    	accountOverPaymentHistoryNew.setLatest(true);
    	    	
		home.create(context,accountOverPaymentHistoryNew);
		if(LogSupport.isDebugEnabled(context))
		{
			LogSupport.debug(context, AccountOverPaymentHistorySupport.class, "AccountOverPaymentHistorySupport.createAccountOverPaymentHistoryTransaction : End ");
		}   
    }
	
	 /**
     * Used at the time of BSS task , after Invoice run
     * 
	 * @param context
	 * @param BAN
	 * @param Spid
	 * @param billCycleID
	 * @param AdjustmentType
	 * @param ReceiptNum
	 * @param undistributedOverPayment
	 * @throws HomeInternalException
	 * @throws HomeException
	 */
	public static void createAccountOverPaymentHistoryTransaction(final Context context, final String BAN,final int Spid,final int billCycleID,final int AdjustmentType,final long ReceiptNum,final long undistributedOverPayment) throws HomeInternalException, HomeException
    {
		if(LogSupport.isDebugEnabled(context))
		{
			LogSupport.debug(context, AccountOverPaymentHistorySupport.class, "AccountOverPaymentHistorySupport.createAccountOverPaymentHistoryTransaction run by system after Invoice Run  : Started ");
		}    
		Home home = (Home) context.get(AccountOverPaymentHistoryHome.class);
		
    	final Predicate filter =new And().add(new EQ(com.redknee.app.crm.bean.AccountOverPaymentHistoryXInfo.BAN,BAN))
    									 .add(new EQ(com.redknee.app.crm.bean.AccountOverPaymentHistoryXInfo.LATEST,true));

    	AccountOverPaymentHistory accountOverPaymentHistoryOld = null;
    	
    	accountOverPaymentHistoryOld=(AccountOverPaymentHistory) ((Home) context.get(AccountOverPaymentHistoryHome.class)).find(context,filter);
    	
    	AccountOverPaymentHistory accountOverPaymentHistoryNew=new AccountOverPaymentHistory();
    	
    	final long paymentAmount=accountOverPaymentHistoryOld.getNewOverpaymentBalance();
    	final long overpaymentDebitAmount = (-1)*accountOverPaymentHistoryOld.getNewOverpaymentBalance();
    	final long oldOverpaymentBalance=accountOverPaymentHistoryOld.getNewOverpaymentBalance();
    	final long newOverpaymentBalance;
    	final long distributedAmount = paymentAmount + (-1)*undistributedOverPayment;  
    	
    	newOverpaymentBalance=undistributedOverPayment+overpaymentDebitAmount+oldOverpaymentBalance;
    	
    	accountOverPaymentHistoryNew.setBan(BAN);
    	accountOverPaymentHistoryNew.setSpid(Spid);
    	accountOverPaymentHistoryNew.setBillCycleID(billCycleID);
    	accountOverPaymentHistoryNew.setAdjustmentType(AdjustmentType);
    	accountOverPaymentHistoryNew.setPaymentAmount(paymentAmount);
    	accountOverPaymentHistoryNew.setDistributedAmount(distributedAmount);
    	accountOverPaymentHistoryNew.setNewOverpaymentAmount(undistributedOverPayment);    	
    	accountOverPaymentHistoryNew.setOverpaymentDebitAmount(overpaymentDebitAmount);
    	accountOverPaymentHistoryNew.setOldOverpaymentBalance(oldOverpaymentBalance);
    	accountOverPaymentHistoryNew.setNewOverpaymentBalance(newOverpaymentBalance);
    	accountOverPaymentHistoryNew.setPaymentReference(ReceiptNum);
    	accountOverPaymentHistoryNew.setLatest(true);
    	    	
		home.create(context,accountOverPaymentHistoryNew);
		if(LogSupport.isDebugEnabled(context))
		{
			LogSupport.debug(context, AccountOverPaymentHistorySupport.class, "AccountOverPaymentHistorySupport.createAccountOverPaymentHistoryTransaction run by system after Invoice Run : End ");
		}   
    }
	
	public static Long getOverPaymentBalance(final Context context, final String BAN) throws HomeInternalException, HomeException
    {
		if(LogSupport.isDebugEnabled(context))
		{
			LogSupport.debug(context, AccountOverPaymentHistorySupport.class, "AccountOverPaymentHistorySupport.getOverPaymentBalance : Started ");
		}    
		Home home = (Home) context.get(AccountOverPaymentHistoryHome.class);
		
    	final Predicate filter =new And().add(new EQ(com.redknee.app.crm.bean.AccountOverPaymentHistoryXInfo.BAN,BAN))
    									 .add(new EQ(com.redknee.app.crm.bean.AccountOverPaymentHistoryXInfo.LATEST,true));

    	AccountOverPaymentHistory accountOverPaymentHistory = null;
    	
    	accountOverPaymentHistory=(AccountOverPaymentHistory) ((Home) context.get(AccountOverPaymentHistoryHome.class)).find(context,filter);    	
    	
		if(LogSupport.isDebugEnabled(context))
		{
			LogSupport.debug(context, AccountOverPaymentHistorySupport.class, "AccountOverPaymentHistorySupport.getOverPaymentBalance : End ");
		}   
		if (accountOverPaymentHistory==null)
		{
			return  null;
		}
		else
		{			
			return accountOverPaymentHistory.getNewOverpaymentBalance();
		}			 
    }
	
	public static long getOverPaymentBalanceForGivenWindow(final Context context, final String BAN,Date startDate,final Date endDate,int accountSpid,int accountBillCycleId) throws HomeInternalException, HomeException
    {
		if(LogSupport.isDebugEnabled(context))
		{
			LogSupport.debug(context, AccountOverPaymentHistorySupport.class, "AccountOverPaymentHistorySupport.getOverPaymentBalanceForGivenWindow : Started ");
		}    
		Home home = (Home) context.get(AccountOverPaymentHistoryHome.class);
		final Predicate filter;
		if (startDate.getTime()<0)
		{
		       filter =new And().add(new EQ(com.redknee.app.crm.bean.AccountOverPaymentHistoryXInfo.BAN,BAN))
                      .add(new LT(com.redknee.app.crm.bean.AccountOverPaymentHistoryXInfo.TIMESTAMP,endDate));
                      

		}else
		{
			long lastPaymentRunStartTime = OverPaymentRunSupport.getStartTimeForLastSuccessfulRun(context, accountSpid, accountBillCycleId);
			if(lastPaymentRunStartTime < startDate.getTime() )
			{
				startDate = new Date(lastPaymentRunStartTime);
			}
	        filter =new And().add(new EQ(com.redknee.app.crm.bean.AccountOverPaymentHistoryXInfo.BAN,BAN))
                .add(new GT(com.redknee.app.crm.bean.AccountOverPaymentHistoryXInfo.TIMESTAMP,startDate))
                .add(new LT(com.redknee.app.crm.bean.AccountOverPaymentHistoryXInfo.TIMESTAMP,endDate));
                    
		}

    	AccountOverPaymentHistory accountOverPaymentHistory = null;
    	if (filter!=null)
    	{
    	    List<AccountOverPaymentHistory> collection = (List<AccountOverPaymentHistory>) HomeSupportHelper.get(context).getBeans( context, AccountOverPaymentHistory.class, filter);
    	    
    	    Collections.sort(collection, new AccountOverPaymentHistoryDateComparator());
    	    if(collection != null 
    	            && collection.size() > 0)
    	    {   // We need only first record
    	        accountOverPaymentHistory=collection.get(0);
       	    }
    	}
		if(LogSupport.isDebugEnabled(context))
		{
			LogSupport.debug(context, AccountOverPaymentHistorySupport.class, "AccountOverPaymentHistorySupport.getOverPaymentBalanceForGivenWindow : End. Returning : accountOverPaymentHistory =  "+ accountOverPaymentHistory);
		}   
		if (accountOverPaymentHistory==null)
		{
			return 0;
		}
		else
		{
			return accountOverPaymentHistory.getNewOverpaymentBalance();
		}			 
    }
	

	
}
