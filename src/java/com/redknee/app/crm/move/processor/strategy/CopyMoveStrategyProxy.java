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

import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveRequest;


/**
 * 
 *
 * @author Aaron Gourley
 * @since 
 */
public class CopyMoveStrategyProxy<MR extends MoveRequest> implements CopyMoveStrategy<MR>
{
	public static final String COPY_SUCCESS = "Copy Passed | ";
	public static final String COPY_FAILED = "Copy Failed | ";
	public static final String COPY_NOT_APPLICABLE = "Nothing to Copy | ";
	public static final String MOVE_NOT_APPLICABLE = "Nothing to Move | ";
	
    public CopyMoveStrategyProxy()
    {
    }

    public CopyMoveStrategyProxy(CopyMoveStrategy<MR> delegate)
    {
        setDelegate(delegate);
    }

    /**
     * @{inheritDoc}
     */
    public void initialize(Context ctx, MR request)
    {
        getDelegate().initialize(ctx, request);
    }

    public void validate(Context ctx, MR request) throws IllegalStateException
    {
        getDelegate().validate(ctx, request);
    }

    public void createNewEntity(Context ctx, MR request) throws MoveException
    {
        getDelegate().createNewEntity(ctx, request);
    }

    public void removeOldEntity(Context ctx, MR request) throws MoveException
    {
        getDelegate().removeOldEntity(ctx, request);
    }

    public void setDelegate(CopyMoveStrategy<MR> delegate)
    {
        this.delegate_ = delegate;
    }

    public CopyMoveStrategy<MR> getDelegate()
    {
        if (delegate_ == null)
        {
            return NullCopyMoveStrategy.instance();
        }
        return delegate_;
    }

    public final void addProxy(Context ctx, CopyMoveStrategyProxy<MR> proxy)
    {
        proxy.setDelegate(getDelegate());

        setDelegate(proxy);  
    }


    public final void appendProxy(Context ctx, CopyMoveStrategyProxy<MR> proxy)
    {
        if ( getDelegate() instanceof CopyMoveStrategyProxy )
        {
            CopyMoveStrategyProxy<MR> delegate = (CopyMoveStrategyProxy<MR>) getDelegate();

            delegate.appendProxy(ctx, proxy);
        }
        else
        {
            addProxy(ctx, proxy);  
        }
    }


    public final CopyMoveStrategy findDecorator(Class cls)
    {
        if ( cls.isInstance(this) )
        {
            return this;
        }

        if ( getDelegate() instanceof CopyMoveStrategyProxy )
        {
            return ((CopyMoveStrategyProxy) getDelegate()).findDecorator(cls);  
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

    private CopyMoveStrategy<MR> delegate_ = null;
}
