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

import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;

/**
 * 
 * 
 * @author Marcio Marques
 * @since 9.1.1
 */
public class SelectNumberSearchAgent extends SelectSearchAgent
{
	public SelectNumberSearchAgent(PropertyInfo pInfo, PropertyInfo searchInfo, Number ignoredValue)
	{
	    super(pInfo, searchInfo);
	    ignoredValue_ = ignoredValue;
	}


	@Override
    public void execute(Context ctx)
		throws AgentException
	{
         Number value = (Number) getSearchCriteria(ctx);
         
         if ( value != null && !ignoredValue_.toString().equals(value.toString()) )
         {
            SearchBorder.doSelect(ctx, getComparator(getPropertyInfo(), value));
         }
         delegate(ctx);
	}

	private Number ignoredValue_ = null;
}
