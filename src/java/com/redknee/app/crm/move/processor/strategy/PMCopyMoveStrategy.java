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
import com.redknee.framework.xlog.log.PMLogMsg;

import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveRequest;


/**
 * Writes success or error PMs depending on the result of delegate execution.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class PMCopyMoveStrategy<MR extends MoveRequest> extends CopyMoveStrategyProxy<MR>
{
    public PMCopyMoveStrategy(CopyMoveStrategy<MR> delegate)
    {
        super(delegate);
        moduleName_ = getDelegate().getClass().getName();
    }

    public PMCopyMoveStrategy(String moduleName, CopyMoveStrategy<MR> delegate)
    {
        super(delegate);
        moduleName_  = moduleName;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void initialize(Context ctx, MR request)
    {
        PMLogMsg pm = new PMLogMsg(moduleName_, "initialize()");
        try
        {
            super.initialize(ctx, request);   
        }
        finally
        {
            pm.log(ctx);   
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void validate(Context ctx, MR request) throws IllegalStateException
    {
        PMLogMsg pmSuccess = new PMLogMsg(moduleName_, "validate()");
        PMLogMsg pmError = new PMLogMsg(moduleName_, "validate()-IllegalStateException");
        try
        {
            super.validate(ctx, request);
            pmSuccess.log(ctx);
        }
        catch(IllegalStateException e)
        {
            pmError.log(ctx);
            throw e;
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void createNewEntity(Context ctx, MR request) throws MoveException
    {
        PMLogMsg pmSuccess = new PMLogMsg(moduleName_, "createNewEntity()");
        PMLogMsg pmError = new PMLogMsg(moduleName_, "createNewEntity()-MoveException");
        try
        {
            super.createNewEntity(ctx, request);
            pmSuccess.log(ctx);
        }
        catch(MoveException e)
        {
            pmError.log(ctx);
            throw e;
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void removeOldEntity(Context ctx, MR request) throws MoveException
    {
        PMLogMsg pmSuccess = new PMLogMsg(moduleName_, "removeOldEntity()");
        PMLogMsg pmError = new PMLogMsg(moduleName_, "removeOldEntity()-MoveException");
        try
        {
            super.removeOldEntity(ctx, request);
            pmSuccess.log(ctx);
        }
        catch(MoveException e)
        {
            pmError.log(ctx);
            throw e;
        }
    }


    private String moduleName_ = null;
}
