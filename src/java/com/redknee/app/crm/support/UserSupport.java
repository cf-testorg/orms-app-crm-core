/*
 * Copyright (c) 2012, Redknee Inc. and its subsidiaries. All Rights Reserved.
 *
 * This code is a protected work and subject to domestic and international copyright law(s). 
 * A complete listing of authors of this work is readily available. Additionally, source
 * code is, by its very nature, confidential information and inextricably contains trade
 * secrets and other information proprietary, valuable and sensitive to Redknee. No unauthorized
 * use, disclosure, manipulation or otherwise is permitted, and may only be used in accordance
 * with the terms of the license agreement entered into with Redknee Inc. and/or its subsidiaries.
 */
package com.redknee.app.crm.support;

import java.security.Principal;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.msp.MSP;
import com.redknee.framework.xhome.msp.Spid;
import com.redknee.framework.xhome.session.Session;
import com.redknee.framework.xlog.log.LogSupport;


/**
 * @author mangaraj.sahoo@redknee.com
 * @since 9.4
 */
public class UserSupport
{
	/**
     * Default system user name to be used by the system triggered processes.
     */
    public static final String SYSTEM_USERNAME = "System";
    
    public static int getSpid(final Context ctx) throws Exception
    {
        Spid spid = MSP.getSpid(ctx);
        if (spid == null)
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                LogSupport.debug(ctx, UserSupport.class.getSimpleName(),
                        "Unable to determine SPID from user.");
            }
            throw new Exception("Unable to determine SPID from user.");
        }
        return spid.getId();
    }
    
    /**
     * @param ctx the Context object
     * @return the user name associated with the session context
     */
    public static String getUserName(final Context ctx)
    {
    	String userId = null;
    	final Context session = Session.getSession(ctx);
    	Principal principal = null;
        if (session != null)
        {
            principal = (Principal) session.get(Principal.class);
        }
        if (principal == null)
        {
            principal = (Principal) ctx.get(Principal.class);
        }
    	if(principal != null)
    	{
    		userId = principal.getName();
    	}
    	return userId;
    }
}
