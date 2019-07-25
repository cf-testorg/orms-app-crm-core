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
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveProcessor;
import com.redknee.app.crm.move.MoveRequest;


/**
 * Writes appropriate log messages to track the enter/exit/error flow of each processor method.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class LoggingMoveProcessor<MR extends MoveRequest> extends MoveProcessorProxy<MR>
{

    public LoggingMoveProcessor()
    {
        this(NullMoveProcessor.instance());
    }

    public LoggingMoveProcessor(MoveProcessor<MR> delegate)
    {
        this(null, delegate);
    }

    public LoggingMoveProcessor(Object logSource)
    {
        this(logSource, NullMoveProcessor.instance());
    }

    public LoggingMoveProcessor(Object logSource, MoveProcessor<MR> delegate)
    {
        super(delegate);
        if (logSource != null)
        {
            logSource_ = logSource;
        }
        else
        {
            logSource_ = getDelegate();
        }
    }

    @Override
    public Context setUp(Context ctx) throws MoveException
    {
        new DebugLogMsg(logSource_, "Setting up for move request " + getRequest(), null).log(ctx);
        try
        {
            Context moveCtx = super.setUp(ctx);
            new InfoLogMsg(logSource_, "Set up successful for move request " + getRequest(), null).log(ctx);
            return moveCtx;
        }
        catch (MoveException e)
        {
            new InfoLogMsg(logSource_, "Set up failed for move request: " + getRequest() + " [Error: " + e.getMessage() + "]", e).log(ctx);
            throw e;
        }
    }

    @Override
    public void validate(Context ctx) throws IllegalStateException
    {
        new DebugLogMsg(logSource_, "Validating move request " + getRequest(), null).log(ctx);
        try
        {
            super.validate(ctx);
            new InfoLogMsg(logSource_, "Validation successful for move request " + getRequest(), null).log(ctx);
        }
        catch (IllegalStateException e)
        {
            new InfoLogMsg(logSource_, "Validating failed for move request: " + getRequest() + " [Error: " + e.getMessage() + "]", e).log(ctx);
            throw e;
        }
    }

    @Override
    public void move(Context ctx) throws MoveException
    {
        new DebugLogMsg(logSource_, "Executing move request " + getRequest(), null).log(ctx);
        try
        {
            super.move(ctx);
            new InfoLogMsg(logSource_, "Execution successful for move request " + getRequest(), null).log(ctx);
        }
        catch (MoveException e)
        {
            new MinorLogMsg(logSource_, "Execution failed for move request: " + getRequest() + " [Error: " + e.getMessage() + "]", e).log(ctx);
            throw e;
        }
    }

    @Override
    public void tearDown(Context ctx) throws MoveException
    {
        new DebugLogMsg(logSource_, "Tearing down move request " + getRequest(), null).log(ctx);
        try
        {
            super.tearDown(ctx);
            new InfoLogMsg(logSource_, "Tear down successful for move request " + getRequest(), null).log(ctx);
        }
        catch (MoveException e)
        {
            new InfoLogMsg(logSource_, "Tear down failed for move request: " + getRequest() + " [Error: " + e.getMessage() + "]", e).log(ctx);
            throw e;
        }
    }

    private final Object logSource_;
}
