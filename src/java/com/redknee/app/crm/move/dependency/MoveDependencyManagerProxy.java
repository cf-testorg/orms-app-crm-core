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

import com.redknee.app.crm.move.MoveDependencyManager;
import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveRequest;


/**
 * The usual proxy style implementation of the MoveDependencyManager interface.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class MoveDependencyManagerProxy implements MoveDependencyManager
{
    public MoveDependencyManagerProxy()
    {
    }

    public MoveDependencyManagerProxy(MoveDependencyManager delegate)
    {
        setDelegate(delegate);
    }
    
    @Override
    public MoveRequest getSourceRequest()
    {
        return getDelegate().getSourceRequest();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Collection<? extends MoveRequest> getDependencyRequests() throws MoveException
    {
        return getDelegate().getDependencyRequests();
    }

    public void setDelegate(MoveDependencyManager delegate)
    {
        this.delegate_ = delegate;
    }

    public MoveDependencyManager getDelegate()
    {
        if (delegate_ == null)
        {
            return NullMoveDependencyManager.instance();
        }
        return delegate_;
    }

    public final void addProxy(Context ctx, MoveDependencyManagerProxy proxy)
    {
        proxy.setDelegate(getDelegate());

        setDelegate(proxy);  
    }


    public final void appendProxy(Context ctx, MoveDependencyManagerProxy proxy)
    {
        if ( getDelegate() instanceof MoveDependencyManagerProxy )
        {
            MoveDependencyManagerProxy delegate = (MoveDependencyManagerProxy) getDelegate();

            delegate.appendProxy(ctx, proxy);
        }
        else
        {
            addProxy(ctx, proxy);  
        }
    }


    public final MoveDependencyManager findDecorator(Class cls)
    {
        if ( cls.isInstance(this) )
        {
            return this;
        }

        if ( getDelegate() instanceof MoveDependencyManagerProxy )
        {
            return ((MoveDependencyManagerProxy) getDelegate()).findDecorator(cls);  
        }

        if ( cls.isInstance(getDelegate()) )
        {
            return getDelegate();
        }

        return null;
    }


    public final boolean hasDecorator(Class cls)
    {
        return findDecorator(cls) != null;
    }
    
    @Override
    public String toString()
    {
       return getClass().getName() + "(" + delegate_ + ")";
    }

    private MoveDependencyManager delegate_ = null;

}
