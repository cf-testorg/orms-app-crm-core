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
package com.redknee.app.crm.move.processor.strategy;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;

import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveRequest;


/**
 * Writes appropriate log messages to track the enter/exit/error flow of each processor method.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class LoggingCopyMoveStrategy<MR extends MoveRequest> extends CopyMoveStrategyProxy<MR>
{
    public LoggingCopyMoveStrategy(CopyMoveStrategy<MR> delegate)
    {
        super(delegate);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void initialize(Context ctx, MR request)
    {
        new DebugLogMsg(getDelegate(), "Initializing move request " + request, null).log(ctx);
        try
        {
            super.initialize(ctx, request);
            new InfoLogMsg(this, "Move request " + request + " initialized successfully.", null).log(ctx);
        }
        catch (IllegalStateException e)
        {
            new InfoLogMsg(this, "Exception initializing move request: " + e.getMessage(), e).log(ctx);
            throw e;
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void validate(Context ctx, MR request) throws IllegalStateException
    {
        new DebugLogMsg(getDelegate(), "Validating move request " + request, null).log(ctx);
        try
        {
            super.validate(ctx, request);
            new InfoLogMsg(this, "Move request " + request + " validated successfully.", null).log(ctx);
        }
        catch (IllegalStateException e)
        {
            new InfoLogMsg(this, "Exception validating move request: " + e.getMessage(), e).log(ctx);
            throw e;
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void createNewEntity(Context ctx, MR request) throws MoveException
    {
        new DebugLogMsg(getDelegate(), "Creating new entity for move request " + request, null).log(ctx);
        try
        {
            super.createNewEntity(ctx, request);
            new InfoLogMsg(this, "New entity for move request " + request + " created successfully.", null).log(ctx);
        }
        catch (MoveException e)
        {
            new InfoLogMsg(this, "Exception creating new entity for move request: " + e.getMessage(), e).log(ctx);
            throw e;
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void removeOldEntity(Context ctx, MR request) throws MoveException
    {
        new DebugLogMsg(getDelegate(), "Removing old entity for move request " + request, null).log(ctx);
        try
        {
            super.removeOldEntity(ctx, request);
            new InfoLogMsg(this, "Old entity for move request " + request + " removed successfully.", null).log(ctx);
        }
        catch (MoveException e)
        {
            new InfoLogMsg(this, "Exception removing old entity for move request: " + e.getMessage(), e).log(ctx);
            throw e;
        }
    }

}
