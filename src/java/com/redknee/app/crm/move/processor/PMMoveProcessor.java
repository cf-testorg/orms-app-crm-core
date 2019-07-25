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
import com.redknee.framework.xlog.log.PMLogMsg;

import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveProcessor;
import com.redknee.app.crm.move.MoveRequest;


/**
 * Writes success or error PMs depending on the result of delegate execution.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class PMMoveProcessor<MR extends MoveRequest> extends MoveProcessorProxy<MR>
{
    public PMMoveProcessor(MoveProcessor<MR> delegate)
    {
        super(delegate);
        moduleName_ = getDelegate().getClass().getName();
    }

    public PMMoveProcessor(String moduleName, MoveProcessor<MR> delegate)
    {
        super(delegate);
        moduleName_  = moduleName;
    }

    @Override
    public void move(Context ctx) throws MoveException
    {
        PMLogMsg pmSuccess = new PMLogMsg(moduleName_, "move()");
        PMLogMsg pmError = new PMLogMsg(moduleName_, "move()-MoveException");
        try
        {
            super.move(ctx);
            pmSuccess.log(ctx);
        }
        catch(MoveException e)
        {
            pmError.log(ctx);
            throw e;
        }
    }

    @Override
    public Context setUp(Context ctx) throws MoveException
    {
        PMLogMsg pmSuccess = new PMLogMsg(moduleName_, "setUp()");
        PMLogMsg pmError = new PMLogMsg(moduleName_, "setUp()-MoveException");
        try
        {
            Context moveCtx = super.setUp(ctx);
            pmSuccess.log(ctx);
            return moveCtx;
        }
        catch(MoveException e)
        {
            pmError.log(ctx);
            throw e;
        }
    }

    @Override
    public void tearDown(Context ctx) throws MoveException
    {
        PMLogMsg pmSuccess = new PMLogMsg(moduleName_, "tearDown()");
        PMLogMsg pmError = new PMLogMsg(moduleName_, "tearDown()-MoveException");
        try
        {
            super.tearDown(ctx);
            pmSuccess.log(ctx);
        }
        catch(MoveException e)
        {
            pmError.log(ctx);
            throw e;
        }
    }

    @Override
    public void validate(Context ctx) throws IllegalStateException
    {
        PMLogMsg pmSuccess = new PMLogMsg(moduleName_, "validate()");
        PMLogMsg pmError = new PMLogMsg(moduleName_, "validate()-IllegalStateException");
        try
        {
            super.validate(ctx);
            pmSuccess.log(ctx);
        }
        catch(IllegalStateException e)
        {
            pmError.log(ctx);
            throw e;
        }
    }

    private String moduleName_ = null;
}
