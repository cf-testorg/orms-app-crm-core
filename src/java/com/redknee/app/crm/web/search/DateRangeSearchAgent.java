/*
   DateRangeSearchAgent
  
   Author : Ah Lot Chan
   Date   : 10 July 2008
  
   This code is a protected work and subject to domestic and international
   copyright law(s). A complete listing of authors of this work is readily
   available. Additionally, source code is, by its very nature, confidential
   information and inextricably contains trade secrets and other information
   proprietary, valuable and sensitive to Redknee, no unauthorised use,
   disclosure, manipulation or otherwise is permitted, and may only be used
   in accordance with the terms of the licence agreement entered into with
   Redknee Inc. and/or its subsidiaries.
  
   Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.web.search;

import java.util.Date;

import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.GTE;
import com.redknee.framework.xhome.elang.LTE;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SearchFieldAgent;

/**
 * DateRangeSearchAgent is a searchagent who select bean with value for a partiular 'date' field  lies within a period
 * 
 * @author ahlot
 * @deprecated If CRM is using FW 6, use the class of the same name provided by Framework
 */
@Deprecated
public class DateRangeSearchAgent  	extends SearchFieldAgent
{
	protected PropertyInfo searchStartRangePInfo_;
	protected PropertyInfo searchEndRangePInfo_  ;

	@Deprecated
	public DateRangeSearchAgent(PropertyInfo searchfrom, PropertyInfo searchTo,PropertyInfo searchPInfo)
	{
		searchStartRangePInfo_=searchfrom;
		searchEndRangePInfo_=searchTo;
    setSearchPropertyInfo(searchPInfo);
	}


	@Override
    public void execute(Context ctx)
		throws AgentException
	{
		Object logSearch = SearchBorder.getCriteria(ctx);

		And and = null;

		Date startDate = (Date) searchStartRangePInfo_.get(logSearch);
		Date endDate   = (Date) searchEndRangePInfo_.get(logSearch);
		if (startDate !=null && endDate!= null)
		{
			and = new And();
			and.add(new GTE(getSearchPropertyInfo(), startDate));
			and.add(new LTE(getSearchPropertyInfo(), endDate));
			SearchBorder.doSelect(ctx, and);
		}

		delegate(ctx);
	}
}
