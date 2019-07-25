package com.redknee.app.crm.filter;

import com.redknee.app.crm.bean.AdjustmentType;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;

public class SystemAdjustmentTypePredicate implements Predicate 
{
	public SystemAdjustmentTypePredicate()
	{
	}
	
	/* (non-Javadoc)
	 * @see com.redknee.framework.xhome.filter.Predicate#f(com.redknee.framework.xhome.context.Context, java.lang.Object)
	 */
	public boolean f(Context arg0, Object obj)
	{
		AdjustmentType adj = (AdjustmentType)obj;
		return adj.isSystem();
	}

}

