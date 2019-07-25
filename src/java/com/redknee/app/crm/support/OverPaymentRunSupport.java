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
import java.util.Date;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeSPI;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.app.crm.bean.AdjustmentTypeEnum;
import com.redknee.app.crm.bean.GroupTypeEnum;
import com.redknee.app.crm.bean.OverPaymentRunStatusEnum;
import com.redknee.app.crm.bean.OverPaymentRunXInfo;
import com.redknee.app.crm.bean.Transaction;
import com.redknee.app.crm.bean.OverPaymentRun;
import com.redknee.app.crm.bean.OverPaymentRunHome;

/**
 * @author alok.sohani
 * @since 9.7.2
 */
public class OverPaymentRunSupport implements Support {
	
	protected static OverPaymentRunSupport instance_ = null;
	
    public static OverPaymentRunSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new OverPaymentRunSupport();
        }
        return instance_;
    }

    protected OverPaymentRunSupport()
    {
    }   
  
    public static void createOverPaymentRunRecord(final Context context,final int Spid,final int billCycleID) throws HomeInternalException, HomeException
    {
    	if(LogSupport.isDebugEnabled(context))
		{
			LogSupport.debug(context, OverPaymentRunSupport.class, "OverPaymentRunSupport.createOverPaymentRunRecord : Started ");
		}    
		Home home = (Home) context.get(OverPaymentRunHome.class);		
    	   	
		OverPaymentRun overPaymentRun=new OverPaymentRun();
   
		overPaymentRun.setSpid(Spid);
		overPaymentRun.setBillCycleId(billCycleID);	
		
		home.create(context,overPaymentRun);
		
		if(LogSupport.isDebugEnabled(context))
		{
			LogSupport.debug(context, OverPaymentRunSupport.class, "OverPaymentRunSupport.createOverPaymentRunRecord : End ");
		}   
    }


    public static void updateOverPaymentRunRecord(Context context,OverPaymentRun beanToUpdate) throws HomeInternalException, HomeException
    {
    	if(LogSupport.isDebugEnabled(context))
		{
			LogSupport.debug(context, OverPaymentRunSupport.class, "OverPaymentRunSupport.updateOverPaymentRunRecord : Started ");
		} 
    	Home home = getHome(context);
    	home.store(context,beanToUpdate);
    	if(LogSupport.isDebugEnabled(context))
		{
			LogSupport.debug(context, OverPaymentRunSupport.class, "OverPaymentRunSupport.updateOverPaymentRunRecord : End ");
		} 
    }
    
    public static OverPaymentRunStatusEnum getStatusBasedOnProcessedResult(long noFailed)
    {
    	if(noFailed > 0)
    	{
    		return OverPaymentRunStatusEnum.FAILED;
    	}else 
    	{
    		return OverPaymentRunStatusEnum.COMPLETED;
    	}
    }
    
    public static long getStartTimeForLastSuccessfulRun(Context context,int spid,int billCycleId) throws HomeException
    {
    	Home home = getHome(context);
    	And filter = new And();    	
    	filter.add(new EQ( OverPaymentRunXInfo.SPID, spid) );
    	filter.add(new EQ( OverPaymentRunXInfo.BILL_CYCLE_ID, billCycleId) );
    	filter.add(new EQ( OverPaymentRunXInfo.STATE, OverPaymentRunStatusEnum.COMPLETED));
    	
    	Object maxValue = HomeSupportHelper.get(context).max(context,OverPaymentRunXInfo.START_DATE_TIME,home,filter);
    	if (maxValue instanceof Number)
    	{
    		return ((Number) maxValue).longValue();
    	}else
    		return (new Date(0l)).getTime();
    }
    
    private static Home getHome(Context context)
    {
    	return (Home) context.get(OverPaymentRunHome.class);
    }
}
