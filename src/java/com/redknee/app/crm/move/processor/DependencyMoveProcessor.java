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

import com.redknee.app.crm.move.MoveDependencyManager;
import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveManager;
import com.redknee.app.crm.move.MoveProcessor;
import com.redknee.app.crm.move.MoveRequest;
import com.redknee.app.crm.move.MoveWarningException;
import com.redknee.app.crm.move.support.MoveDependencySupport;


/**
 * This move processor recursively calls MoveManager.validate() and MoveManager.move()
 * for each of the calculated dependencies for the given request.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class DependencyMoveProcessor<MR extends MoveRequest> extends MoveProcessorProxy<MR>
{
    public DependencyMoveProcessor(MR request)
    {
        this(new DefaultMoveProcessor<MR>(request));
    }
    
    public DependencyMoveProcessor(MoveProcessor<MR> delegate)
    {
        super(delegate);
    }
    
    @Override
    public Context setUp(Context ctx) throws MoveException
    {
        Context moveCtx = super.setUp(ctx);        
        dependencyMgr_ = MoveDependencySupport.getMoveDependencyManager(moveCtx, getRequest());
        return moveCtx;
    }

    @Override
    public void validate(Context ctx) throws IllegalStateException
    {
        /*
         * Strategy here is to validate the rest of the move every step of the way.
         * 
         * This recursive validation might not be necessary, but if it works and it isn't very expensive
         * then it doesn't hurt to validate more frequently to minimize the chance that we proceed when
         * the system is in a bad state.
         */
        if (dependencyMgr_ != null
                && !this.getRequest().hasErrors(ctx))
        {
            try
            {
                for (MoveRequest dependency : dependencyMgr_.getDependencyRequests())
                {
                    MoveManager mm = (MoveManager)ctx.get(MoveManager.class);
                    if (mm == null)
                    {
                        mm = new MoveManager();
                    }
                    mm.validate(ctx, dependency);
                    for (Throwable error : dependency.getErrors(ctx))
                    {
                        this.getRequest().reportError(ctx, error);
                    }
                    for (MoveWarningException warning : dependency.getWarnings(ctx))
                    {
                        this.getRequest().reportWarning(ctx, warning);
                    }
                }
            }
            catch (MoveException e)
            {
                throw new IllegalStateException("Error setting up move scenario prior to validation: " + e.getMessage(), e);
            } 
        }
        
        super.validate(ctx);
    }

    @Override
    public void move(Context ctx) throws MoveException
    {   
        if (dependencyMgr_ != null
                && !this.getRequest().hasErrors(ctx))
        {
            for (MoveRequest dependency : dependencyMgr_.getDependencyRequests())
            {
                MoveManager mm = (MoveManager)ctx.get(MoveManager.class);
                if (mm == null)
                {
                    mm = new MoveManager();
                }
                mm.move(ctx, dependency);
                for (Throwable error : dependency.getErrors(ctx))
                {
                    this.getRequest().reportError(ctx, error);
                }
                for (MoveWarningException warning : dependency.getWarnings(ctx))
                {
                    this.getRequest().reportWarning(ctx, warning);
                }
            } 
        }

        super.move(ctx);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void tearDown(Context ctx) throws MoveException
    {
        dependencyMgr_ = null;
        super.tearDown(ctx);
    }
    
    protected MoveDependencyManager dependencyMgr_ = null;
}
