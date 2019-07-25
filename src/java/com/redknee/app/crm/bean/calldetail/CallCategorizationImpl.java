/*
 * Created on Apr 20, 2005
 *
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
package com.redknee.app.crm.bean.calldetail;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.OrderBy;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.OrderByHome;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.BillingOptionMapping;
import com.redknee.app.crm.bean.BillingOptionMappingHome;
import com.redknee.app.crm.bean.BillingOptionMappingXInfo;

/**
 * Service that finds the BillingOptionMapping rule that matches a 
 * CallDetail.
 * 
 * @author psperneac
 */
public class CallCategorizationImpl implements CallCategorization
{
	/**
	 * @see com.redknee.app.crm.bean.calldetail.CallCategorization#categorizeCall(com.redknee.framework.xhome.context.Context, com.redknee.app.crm.bean.calldetail.CallDetail)
	 */
	public BillingOptionMapping categorizeCall(Context ctx,CallDetail cdr)
	{
		if(ctx==null)
		{
			return null;
		}
		
		if(cdr==null)
		{
			return null;
		}
		
		Home home=(Home)ctx.get(BillingOptionMappingHome.class);
		if(home==null)
		{
			return null;
		}
		home = home.where(ctx, new EQ(BillingOptionMappingXInfo.SPID, Integer.valueOf(cdr.getSpid())));
        final OrderBy order = new OrderBy(BillingOptionMappingXInfo.PRIORITY, true);
        final Home orderedHome = new OrderByHome(ctx, order, home);

		// visits each rule and matches the cdr against it.
		CallCategoryRulePicker visitor=new CallCategoryRulePicker(cdr);
		try
		{
			orderedHome.forEach(ctx,visitor);
		}
		catch (HomeException e)
		{
			if(LogSupport.isDebugEnabled(ctx))
			{
				new DebugLogMsg(this,e.getMessage(),e).log(ctx);
			}
		}
		catch (AbortVisitException e)
		{
			
		}

		return visitor.getSavedRule();
	}
}
