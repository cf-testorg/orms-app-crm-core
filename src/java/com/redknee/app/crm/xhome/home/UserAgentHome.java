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

/*
 * @author jchen
 * Created on Dec 15, 2005
 */
package com.redknee.app.crm.xhome.home;

import com.redknee.framework.xhome.auth.bean.User;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;

import com.redknee.app.crm.CoreCrmConstants;
import com.redknee.app.crm.bean.UserAgentAware;

/**
 * @author jchen
 *
 * A util home should be chained as local to set Principle/UserAgent
 * to the UserAgentAware object.
 * 
 * Currently, it only supports create method, for store mehtod, it should not change the original userAgent
 */
public class UserAgentHome extends HomeProxy
{

	public UserAgentHome(Context ctx, Home delegate)
	{
		super(ctx, delegate);
	}
	
	/* (non-Javadoc)
	 * @see com.redknee.framework.xhome.home.HomeSPI#create(com.redknee.framework.xhome.context.Context, java.lang.Object)
	 */
	@Override
    public Object create(Context ctx, Object obj) throws HomeException
	{
		if (obj instanceof UserAgentAware)
		{
		    setUserAgent(ctx, (UserAgentAware)obj);
		}
		return super.create(ctx, obj);
	}
	
	/**
	 * Be sure to passing correct context,otherwise principle.class could be wrong
	 *  will not be correct
	 * @param ctx
	 * @param usrAware
	 */
	void setUserAgent(Context ctx, UserAgentAware usrAware)
	{
	    if (usrAware != null)
	    {
	        String csr = usrAware.getAgent();
	        if (csr == null || csr.trim().length() == 0 || CoreCrmConstants.SYSTEM_AGENT.equals(csr))
	        {
	            final User principal = (User) ctx.get(java.security.Principal.class, new User());
	            String localAgent = principal.getId();
	            if (principal.getId().trim().length() == 0)
	            {
	                localAgent = CoreCrmConstants.SYSTEM_AGENT; 
	            }
	            usrAware.setAgent(localAgent);
	        }
	    }
	}
}
