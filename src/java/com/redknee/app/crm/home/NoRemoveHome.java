package com.redknee.app.crm.home;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.MajorLogMsg;

/**
 * Throw a UnsupportedOperationException whenever a remove action is
 * called in this pipeline.
 * @author angie.li
 *
 */
public class NoRemoveHome extends HomeProxy 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoRemoveHome(Home delegate)
	{
		super(delegate);
	}
	
	public void remove(Context ctx, Object obj)
		throws HomeException
	{
		UnsupportedOperationException e = new UnsupportedOperationException("The Delete action for this entity is disabled in the system.");
		new MajorLogMsg(this, 
				"Home.remove is disabled in the pipeline for Class " + obj.getClass().getName(), 
				e).log(ctx);

		throw e;
	}
}
