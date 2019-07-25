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
package com.redknee.app.crm.filter;

import com.redknee.app.crm.access.control.PermissionAware;
import com.redknee.framework.auth.AuthMgr;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.visitor.AbortVisitException;

/**
 * Decide if the bean is permitted by the authentication in context
 * 
 * @author simar.singh@redknee.com
 */
public class PermissionAwarePredicate<BEAN extends PermissionAware> implements Predicate
{

    private static final long serialVersionUID = 1L;

    public PermissionAwarePredicate(Class<BEAN> beanclass)
    {
        
    }
    
    public PermissionAwarePredicate()
    {
        
    }
    @SuppressWarnings("unchecked")
    public boolean f(Context ctx, Object obj) throws AbortVisitException 
    {
       return  hasPermission(ctx,((BEAN)obj).getPermission());
    }

    private boolean hasPermission(Context ctx, String permssion)
    {
        AuthMgr authMgr = new AuthMgr(ctx);
        return authMgr.check(permssion);
    }
}
