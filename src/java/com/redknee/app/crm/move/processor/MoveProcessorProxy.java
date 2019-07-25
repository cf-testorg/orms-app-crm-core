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


/**
 * The usual proxy style implementation of the MoveProcessor interface.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class MoveProcessorProxy<MR extends MoveRequest> implements MoveProcessor<MR>
{
    public MoveProcessorProxy()
    {
    }

    public MoveProcessorProxy(MoveProcessor<MR> delegate)
    {
        setDelegate(delegate);
    }

    public Context setUp(Context ctx) throws MoveException
    {
        return getDelegate().setUp(ctx);
    }

    public void validate(Context ctx) throws IllegalStateException
    {
        getDelegate().validate(ctx);
    }

    public void move(Context ctx) throws MoveException
    {
        getDelegate().move(ctx);
    }

    public void tearDown(Context ctx) throws MoveException
    {
        getDelegate().tearDown(ctx);
    }
    
    public MR getRequest()
    {
        return getDelegate().getRequest();
    }

    public void setDelegate(MoveProcessor<MR> delegate)
    {
        this.delegate_ = delegate;
    }

    public MoveProcessor<MR> getDelegate()
    {
        if (delegate_ == null)
        {
            return NullMoveProcessor.instance();
        }
        return delegate_;
    }

    public final void addProxy(Context ctx, MoveProcessorProxy<MR> proxy)
    {
        proxy.setDelegate(getDelegate());

        setDelegate(proxy);  
    }


    public final void appendProxy(Context ctx, MoveProcessorProxy<MR> proxy)
    {
        if ( getDelegate() instanceof MoveProcessorProxy )
        {
            MoveProcessorProxy<MR> delegate = (MoveProcessorProxy<MR>) getDelegate();

            delegate.appendProxy(ctx, proxy);
        }
        else
        {
            addProxy(ctx, proxy);  
        }
    }


    public final MoveProcessor findDecorator(Class cls)
    {
        if ( cls.isInstance(this) )
        {
            return this;
        }

        if ( getDelegate() instanceof MoveProcessorProxy )
        {
            return ((MoveProcessorProxy) getDelegate()).findDecorator(cls);  
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

    private MoveProcessor<MR> delegate_ = null;
}
