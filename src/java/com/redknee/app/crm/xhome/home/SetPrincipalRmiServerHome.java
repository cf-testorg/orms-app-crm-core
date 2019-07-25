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
 * Created on Jan 5, 2006
 */
package com.redknee.app.crm.xhome.home;

import java.security.Principal;

import com.redknee.framework.xhome.auth.bean.User;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.msp.Spid;

import com.redknee.app.crm.xhome.auth.PrincipalAware;

/**
 * @author jchen
 *
 *This class coordinate with SetPrincipalHome, it sets Principal objects in RMI server context, 
 *the pricipal object which is passed from Bean (PrincipalAware).
 *
 *Install this class in BAS node, inside of RMI server sub-pipeline
 */
public class SetPrincipalRmiServerHome extends HomeProxy
{
    public SetPrincipalRmiServerHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }
    
	public SetPrincipalRmiServerHome(Home delegate)
	{
		super(delegate);	
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx, Object obj) throws HomeException
    {
        ctx = preparePrincipal(ctx, obj);
        return super.create(ctx, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Context ctx, Object bean) throws HomeException
    {
        ctx = preparePrincipal(ctx, bean);
        super.remove(ctx, bean);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object store(Context ctx, Object obj) throws HomeException
    {
        ctx = preparePrincipal(ctx, obj);
        return super.store(ctx, obj);
    }
    
    /**
     * put principal from object into context
     * @param ctx
     * @param obj
     * @return
     */
    protected Context preparePrincipal(Context ctx, Object obj)
    {
        if (obj instanceof PrincipalAware)
	    {
	        PrincipalAware ppa = (PrincipalAware)obj;
	        Principal ppInCtx = (Principal)ctx.get(Principal.class);
            
	        if (ppa.getPrincipal() != null && ctx.get(Principal.class) == null)
	        {
	            ctx = ctx.createSubContext();
	            //System.out.println("preparePrincipal, " + ppa.getPrincipal());
	            ctx.put(Principal.class, ppa.getPrincipal());
	            //we do not test if these two principle are equal or not
	            
	            //tt6021330610, SpidAware is looking for spid.class, if there is principal
	            if (ppa.getPrincipal() instanceof User)
	            {
		            User user = (User) ppa.getPrincipal();
		            if (user.getSpid() > 0)
		            {
		                Spid spid = new Spid();
		                spid.setSpid(user.getSpid());
		                ctx.put(Spid.class, spid);
		            }
	            }
	        }	        
	    }
        return ctx;
    }
}
