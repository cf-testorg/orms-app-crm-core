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

import com.redknee.framework.xhome.beans.Freezable;
import com.redknee.framework.xhome.beans.XCloneable;
import com.redknee.framework.xhome.beans.XDeepCloneable;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;

import com.redknee.app.crm.move.MoveDependencyManager;
import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveRequest;


/**
 * 
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public abstract class AbstractMoveDependencyManager<T extends MoveRequest> implements MoveDependencyManager, ContextAware
{
    public AbstractMoveDependencyManager(Context ctx, T srcRequest)
    {
        setContext(ctx);
        srcRequest_ = srcRequest;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public final Collection<? extends MoveRequest> getDependencyRequests() throws MoveException
    {
        T request = getSourceRequest();
        
        if (isDependencyListStatic())
        {
            if (getStaticMoveException() != null)
            {
                throw getStaticMoveException();
            }
            return getDependencyRequests(getContext(), request);
        }
        
        if (isDirty(request))
        {
            // Recalculate dependencies
            dependencies_ = new ArrayList<MoveRequest>();
            dependencies_.addAll(getDependencyRequests(getContext(), request));

            setClean(request);
        }

        if (dependencies_ == null)
        {
            dependencies_ = new ArrayList<MoveRequest>();
        }
        return dependencies_;
    }
    
    /**
     * This method is designed to be over-ridden in the concrete class.  The interface's method is final because it
     * provides a template for dependency caching and clean/dirty state control.
     */
    protected abstract Collection<? extends MoveRequest> getDependencyRequests(Context ctx, T request) throws MoveException;

    protected boolean isDependencyListStatic()
    {
        return false;
    }

    protected MoveException getStaticMoveException()
    {
        return null;
    }

    private boolean isDirty(T request)
    {
        boolean dirty = true;
        
        if (lastRequestUsed_ != null && request != null)
        {
            if (lastRequestUsed_.equals(request))
            {
                dirty = false;
            }
        }
        
        return dirty;
    }
    
    
    private void setClean(T request)
    {
        try
        {
            if (request instanceof XDeepCloneable)
            {
                lastRequestUsed_ = (T) ((XDeepCloneable)request).deepClone();
            }
            else if (request instanceof XCloneable)
            {
                lastRequestUsed_ = (T) ((XCloneable)request).clone();
            }

            if (request instanceof Freezable)
            {
                ((Freezable)lastRequestUsed_).freeze();
            }
        }
        catch (CloneNotSupportedException e)
        {
            lastRequestUsed_ = null;
        }
    }

    /**
     * @{inheritDoc}
     */
    public T getSourceRequest()
    {
        return srcRequest_;
    }

    /**
     * @{inheritDoc}
     */
    public Context getContext()
    {
        return ctx_;
    }

    /**
     * @{inheritDoc}
     */
    public void setContext(Context ctx)
    {
        ctx_ = ctx;
    }

    
    private Collection<MoveRequest> dependencies_ = new ArrayList<MoveRequest>();
    private T srcRequest_;
    private T lastRequestUsed_;
    private Context ctx_ = null;
}
