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

import java.util.Collection;
import java.util.Iterator;

import com.redknee.app.crm.bean.AccountOverPaymentHistory;
import com.redknee.app.crm.bean.AccountOverPaymentHistoryHome;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.app.crm.bean.AccountOverPaymentHistoryXInfo;

/**
 * @author alok.sohani@redknee.com
 *
 */
public class UpdateLatestAccountOverPaymentHistoryHome extends HomeProxy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UpdateLatestAccountOverPaymentHistoryHome(Home delegate)
    {
        super(delegate);
    }
	
	public UpdateLatestAccountOverPaymentHistoryHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }
	
	public Object create(Context ctx, Object bean) throws HomeException
    {
    	AccountOverPaymentHistory accountOverPaymentHistory = (AccountOverPaymentHistory) bean;
    	
    	if(LogSupport.isDebugEnabled(ctx))
		{
			LogSupport.debug(ctx, this, "UpdateLatestAccountOverPaymentHistoryHome : Update latest record as not latest and proceed.");
		}    	
    	  	
    	final Predicate filter =new And().add(new EQ(com.redknee.app.crm.bean.AccountOverPaymentHistoryXInfo.BAN,accountOverPaymentHistory.getBan()))
    			                         .add(new EQ(com.redknee.app.crm.bean.AccountOverPaymentHistoryXInfo.LATEST,true));
   	
    	final Collection<AccountOverPaymentHistory> accountOverPaymentHistoryList = ((Home) ctx.get(AccountOverPaymentHistoryHome.class)).select(ctx,filter);
    	AccountOverPaymentHistory accountOverPaymentHistoryTemp=null;
    	
    	for (Iterator<AccountOverPaymentHistory> accountOverPaymentHistoryListIter = accountOverPaymentHistoryList.iterator(); accountOverPaymentHistoryListIter.hasNext();)
        {
    		accountOverPaymentHistoryTemp=accountOverPaymentHistoryListIter.next();
    		accountOverPaymentHistoryTemp.setLatest(false);
    		if(LogSupport.isDebugEnabled(ctx))
    		{
    			LogSupport.debug(ctx, this, "UpdateLatestAccountOverPaymentHistoryHome : Updated entry with BAN = "+accountOverPaymentHistoryTemp.getBan()+" and timestamp = "+accountOverPaymentHistoryTemp.getTimestamp()+" as not latest." );
    		}  
    		super.store(ctx,accountOverPaymentHistoryTemp);
        }
    	
     	return super.create(ctx, bean);
    }
}
