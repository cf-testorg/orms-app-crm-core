/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily 
 * available. Additionally, source code is, by its very nature, confidential 
 * information and inextricably contains trade secrets and other information 
 * proprietary, valuable and sensitive to Redknee, no unauthorised use, 
 * disclosure, manipulation or otherwise is permitted, and may only be used 
 * in accordance with the terms of the licence agreement entered into with 
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.move.support;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.move.MoveDependencyManager;
import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveRequest;
import com.redknee.app.crm.move.dependency.NullMoveDependencyManager;
import com.redknee.app.crm.move.dependency.factory.DefaultMoveDependencyManagerFactory;
import com.redknee.app.crm.move.dependency.factory.MoveDependencyManagerFactory;


/**
 * Provides convenience methods to get move dependency managers and dependencies.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class MoveDependencySupport
{
    public static MoveDependencyManager getMoveDependencyManager(Context ctx, MoveRequest request) throws MoveException
    {
        MoveDependencyManagerFactory factory = (MoveDependencyManagerFactory)ctx.get(
                MoveDependencyManagerFactory.class, 
                DefaultMoveDependencyManagerFactory.instance());
        
        MoveDependencyManager mgr = factory.getInstance(ctx, request);
        if (mgr == null)
        {
            mgr = NullMoveDependencyManager.instance();
        }
        return mgr;
    }
}
