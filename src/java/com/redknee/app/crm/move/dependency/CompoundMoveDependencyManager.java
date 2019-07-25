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

import java.util.ArrayList;
import java.util.Collection;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveRequest;
import com.redknee.app.crm.move.request.CompoundMoveRequest;


/**
 * This class returns each of the move requests contained in the given
 * compound move request as its dependencies.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class CompoundMoveDependencyManager extends AbstractMoveDependencyManager<CompoundMoveRequest>
{
    public CompoundMoveDependencyManager(Context ctx, CompoundMoveRequest srcRequest)
    {
        super(ctx, srcRequest);
    }
    
    
    /**
     * @{inheritDoc}
     */
    @Override
    protected Collection<? extends MoveRequest> getDependencyRequests(Context ctx, CompoundMoveRequest request) throws MoveException
    {
        Collection<MoveRequest> dependencies = new ArrayList<MoveRequest>();

        for(Object requestObj : request.getRequests())
        {
            if(requestObj instanceof MoveRequest)
            {
                dependencies.add((MoveRequest)requestObj);
            }
        }

        return dependencies;
    }
}
