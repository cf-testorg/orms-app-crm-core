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
package com.redknee.app.crm.move.dependency;

import java.util.Collection;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xlog.log.PMLogMsg;

import com.redknee.app.crm.move.MoveDependencyManager;
import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveRequest;


/**
 * Writes success or error PMs depending on the result of delegate execution.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class PMMoveDependencyManager extends MoveDependencyManagerProxy implements ContextAware
{
    public PMMoveDependencyManager(Context ctx, MoveDependencyManager delegate)
    {
        super(delegate);
        setContext(ctx);
        moduleName_ = getDelegate().getClass().getName();
    }

    public PMMoveDependencyManager(Context ctx, String moduleName, MoveDependencyManager delegate)
    {
        super(delegate);
        setContext(ctx);
        moduleName_  = moduleName;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Collection<? extends MoveRequest> getDependencyRequests() throws MoveException
    {
        PMLogMsg pm = new PMLogMsg(moduleName_, "getDependencyRequests()");
        PMLogMsg pmError = new PMLogMsg(moduleName_, "getDependencyRequests()-MoveException");
        try
        {
            Collection<? extends MoveRequest> requests = super.getDependencyRequests();
            pm.log(getContext());
            return requests;
        }
        catch( MoveException e )
        {
            pmError.log(getContext());
            throw e;
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public MoveRequest getSourceRequest()
    {
        PMLogMsg pm = new PMLogMsg(moduleName_, "getSourceRequest()");
        try
        {
            return super.getSourceRequest();   
        }
        finally
        {
            pm.log(getContext());
        }
    }
    /**
     * @{inheritDoc}
     */
    @Override
    public Context getContext()
    {
        return ctx_;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void setContext(Context ctx)
    {
        ctx_ = ctx;
    }

    private Context ctx_ = null;
    private String moduleName_ = null;
}
