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

import com.redknee.app.crm.move.MoveRequest;
import com.redknee.app.crm.move.request.factory.DefaultMoveRequestFactory;
import com.redknee.app.crm.move.request.factory.MoveRequestFactory;


/**
 * Provides convenience methods to get move requests.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class MoveRequestSupport
{
    public static MoveRequest getMoveRequest(Context ctx, Object bean)
    {
        MoveRequestFactory factory = (MoveRequestFactory)ctx.get(
                MoveRequestFactory.class, 
                DefaultMoveRequestFactory.instance());
        
        return factory.getInstance(ctx, bean);
    }

    public static MoveRequest getMoveRequest(Context ctx, Object bean, Class<? extends MoveRequest> preferredType)
    {
        MoveRequestFactory factory = (MoveRequestFactory)ctx.get(
                MoveRequestFactory.class, 
                DefaultMoveRequestFactory.instance());
        
        return factory.getInstance(ctx, bean, preferredType);
    }
}
