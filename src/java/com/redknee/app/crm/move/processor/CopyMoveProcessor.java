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
import com.redknee.app.crm.move.processor.strategy.CopyMoveStrategy;
import com.redknee.app.crm.move.processor.strategy.NullCopyMoveStrategy;


/**
 * Class defining the strategy of create/remove style move operations.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class CopyMoveProcessor<MR extends MoveRequest> extends MoveProcessorProxy<MR>
{
    public CopyMoveProcessor(MR request,
            CopyMoveStrategy<MR> strategy)
    {
        this(new DependencyMoveProcessor<MR>(request), strategy);
    }
    
    public CopyMoveProcessor(MoveProcessor<MR> delegate,
            CopyMoveStrategy<MR> strategy)
    {
        super(delegate);
        strategy_ = strategy;
        if (strategy_ == null)
        {
            strategy_ = NullCopyMoveStrategy.instance();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Context setUp(Context ctx) throws MoveException
    {
        Context moveCtx = super.setUp(ctx);
        
        strategy_.initialize(moveCtx, this.getRequest());
        
        return moveCtx;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void validate(Context ctx) throws IllegalStateException
    {
        strategy_.validate(ctx, this.getRequest());
        
        super.validate(ctx);
    }
    
    @Override
    public final void move(Context ctx) throws MoveException
    {
        strategy_.createNewEntity(ctx, this.getRequest());
        super.move(ctx);
        strategy_.removeOldEntity(ctx, this.getRequest());
    }
    
    @Override
    public String toString()
    {
       return getClass().getName() + "[" + strategy_ + "](" + getDelegate() + ")";
    }
    
    protected CopyMoveStrategy<MR> strategy_ = null;
}
