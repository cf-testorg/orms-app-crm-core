/*
 * @author jchen
 * Created on Jan 5, 2006
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
package com.redknee.app.crm.xhome.home;

import java.security.Principal;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.xhome.auth.PrincipalAware;

/**
 * This class will set current principal to the bean object, and passing local principal to remote RMI server context
 * Make sure this home ouside of RMI client homes, and inside of RMI server homes
 *
 * @author jchen
 */
public class SetPrincipalHome extends AdapterHome
{
    public SetPrincipalHome(Home delegate)
    {
        super();
        setDelegate(delegate);
        setAdapter(new SetPrincipalAdapter());
    }

    @Override
    public Object removePredicateFromWhere(Context ctx, Object where)
    {
        return where;
    }
}

class SetPrincipalAdapter implements Adapter
{
    /**
     * @see com.redknee.framework.xhome.home.Adapter#adapt(com.redknee.framework.xhome.context.Context, java.lang.Object)
     */
    public Object adapt(Context ctx, Object obj) throws HomeException
    {
        if (obj instanceof PrincipalAware)
        {
            PrincipalAware ppa = (PrincipalAware) obj;

            if (ppa.getPrincipal() == null)
            {
                Principal ppInCtx = (Principal) ctx.get(Principal.class);
                if (ppInCtx != null)
                {
                    ppa.setPrincipal(ppInCtx);
                }
            }
        }
        return obj;
    }

    /**
     * @see com.redknee.framework.xhome.home.Adapter#unAdapt(com.redknee.framework.xhome.context.Context, java.lang.Object)
     */
    public Object unAdapt(Context ctx, Object obj) throws HomeException
    {
        return adapt(ctx, obj);
    }
}
