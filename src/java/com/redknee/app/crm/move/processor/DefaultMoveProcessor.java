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
package com.redknee.app.crm.move.processor;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveProcessor;
import com.redknee.app.crm.move.MoveRequest;


/**
 * Default move processor that doesn't do anything but store the request.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class DefaultMoveProcessor<MR extends MoveRequest> implements MoveProcessor<MR>
{
    public DefaultMoveProcessor(MR request)
    {
        request_ = request;
    }

    /**
     * @{inheritDoc}
     */
    public MR getRequest()
    {
        return request_;
    }

    /**
     * @{inheritDoc}
     */
    public Context setUp(Context ctx) throws MoveException
    {
        return ctx.createSubContext();
    }

    /**
     * @{inheritDoc}
     */
    public void validate(Context ctx) throws IllegalStateException
    {
    }

    /**
     * @{inheritDoc}
     */
    public void move(Context ctx) throws MoveException
    {
    }

    /**
     * @{inheritDoc}
     */
    public void tearDown(Context ctx) throws MoveException
    {
    }
    
    @Override
    public String toString()
    {
        return this.getClass().getName() + "[request=" + request_ + "]";
    }
    
    private final MR request_;
}
