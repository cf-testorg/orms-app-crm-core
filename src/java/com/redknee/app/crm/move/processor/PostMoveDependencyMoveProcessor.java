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


/**
 * This move processor recursively calls MoveManager.validate() and MoveManager.move()
 * for each of the calculated dependencies for the given request.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class PostMoveDependencyMoveProcessor<MR extends MoveRequest> extends DependencyMoveProcessor<MR>
{
    public PostMoveDependencyMoveProcessor(MR request)
    {
        super(request);
    }
    
    public PostMoveDependencyMoveProcessor(MoveProcessor<MR> delegate)
    {
        super(delegate);
    }
    
    
    @Override
    public void move(Context ctx) throws MoveException
    {   
    	getDelegate().move(ctx);
    	
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
    }
}
