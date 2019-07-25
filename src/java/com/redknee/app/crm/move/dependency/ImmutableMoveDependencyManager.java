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
import java.util.Collections;

import com.redknee.framework.xhome.beans.Freezable;
import com.redknee.framework.xhome.beans.XCloneable;
import com.redknee.framework.xhome.beans.XDeepCloneable;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.MajorLogMsg;

import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveRequest;


/**
 * After instantiation, this classes source request and dependency list can't be changed.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public abstract class ImmutableMoveDependencyManager extends AbstractMoveDependencyManager
{
    public ImmutableMoveDependencyManager(Context ctx, MoveRequest srcRequest)
    {
        super(ctx, (MoveRequest)getImmutableCopy(srcRequest));
        
        Collection<? extends MoveRequest> dependencies = null;
        MoveException exception = null;
        try
        {
            dependencies = Collections.unmodifiableCollection(initializeDependencies(ctx));
        }
        catch (MoveException e)
        {
            new MajorLogMsg(this, e.getMessage(), e).log(ctx);
            exception = e;
        }
        dependencies_ = dependencies;
        exception_ = exception;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public final MoveRequest getSourceRequest()
    {
        return super.getSourceRequest();
    }
    
    
    @Override
    protected boolean isDependencyListStatic()
    {
        return true;
    }

    @Override
    protected MoveException getStaticMoveException()
    {
        return exception_;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Collection<? extends MoveRequest> getDependencyRequests(Context ctx, MoveRequest request) throws MoveException
    {
        return dependencies_;
    }

    public abstract Collection<? extends MoveRequest> initializeDependencies(Context ctx) throws MoveException;

    private static Object getImmutableCopy(Object obj)
    {
        Object copy = obj;
        try
        {
            if (obj instanceof XDeepCloneable)
            {
                copy = ((XDeepCloneable)obj).deepClone();
            }
            else if (obj instanceof XCloneable)
            {
                copy = ((XCloneable)obj).clone();
            }
        }
        catch (CloneNotSupportedException e)
        {
        }
        if (copy instanceof Freezable)
        {
            ((Freezable) copy).freeze();
        }
        return copy;
    }
    
    private final MoveException exception_;
    private final Collection<? extends MoveRequest> dependencies_;
}
