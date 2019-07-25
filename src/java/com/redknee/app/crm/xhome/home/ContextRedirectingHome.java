/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s).  A complete listing of authors of this work is readily
 * available.  Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee.  No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the license agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.xhome.home;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.home.NullHome;


/**
 * Home to delegate from one home pipeline to another at runtime rather than statically
 * linking 2 home pipelines.  The only exception to this is caused by calls to addProxy()
 * or appendProxy(), which may result in insertion of statically referenced delegates
 * into the pipeline.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class ContextRedirectingHome extends HomeProxy
{
    public ContextRedirectingHome(Context ctx, Object ctxKey)
    {
        super(ctx);
        setAlternateContextKey(ctxKey);
    }

    public Object getAlternateContextKey()
    {
        return ctxKey_;
    }

    public void setAlternateContextKey(Object ctxKey)
    {
        ctxKey_ = ctxKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Home getDelegate(Context ctx)
    {
        if (delegate_ == NullHome.instance())
        {
            return (Home) ctx.get(getAlternateContextKey());
        }
        return delegate_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        if (delegate_ == NullHome.instance())
        {
            final Object alternateContextKey = getAlternateContextKey();
            return getClass().getName() + "(Key=[" + (alternateContextKey instanceof Class ? ((Class)alternateContextKey).getName() : String.valueOf(alternateContextKey)) + "])";
        }
        else
        {
            return getClass().getName() + "(Static Reference=[" + delegate_ + "])";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addProxy(Context ctx, HomeProxy proxy)
    {
        proxy.setDelegate(new ContextRedirectingHome(ctx, getAlternateContextKey()));
        
        delegate_ = proxy;
    }

    /**
     * {@inheritDoc}
     */
    public void setDelegate(Home delegate)
    {
        throw new UnsupportedOperationException("setDelegate() method not supported for " + this.getClass().getName() + ".  Use setAlternateContextKey() instead.");
    }

    protected Object ctxKey_;
}
