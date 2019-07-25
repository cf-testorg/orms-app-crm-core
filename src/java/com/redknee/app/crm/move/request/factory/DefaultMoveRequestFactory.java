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
package com.redknee.app.crm.move.request.factory;

import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.move.MoveRequest;


/**
 * This is the core version of the move request factory.  It will likely need to be over-ridden
 * by the application using the feature.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class DefaultMoveRequestFactory implements MoveRequestFactory
{
    private static MoveRequestFactory instance_ = null;
    public static MoveRequestFactory instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultMoveRequestFactory();
        }
        return instance_;
    }
    
    protected DefaultMoveRequestFactory()
    {
    }

    /**
     * @{inheritDoc}
     */
    public MoveRequest getInstance(Context ctx, Object bean)
    {
        Class<? extends MoveRequest> preferredType = null;
        if (bean != null)
        {
            preferredType = XBeans.getClass(ctx, bean.getClass(), MoveRequest.class);
        }

        if (preferredType == null)
        {
            preferredType = MoveRequest.class;
        }
        
        return getInstance(ctx, bean, preferredType);
    }

    /**
     * @{inheritDoc}
     */
    public MoveRequest getInstance(Context ctx, Object bean, Class<? extends MoveRequest> preferredType)
    {
        MoveRequest request = null;
        
        return request;
    }
}
