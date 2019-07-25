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
package com.redknee.app.crm.delivery.email;

import com.redknee.framework.xhome.context.Context;


/**
 * Basic proxy implementation
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class EmailResultCallbackProxy implements EmailResultCallback
{
    public EmailResultCallbackProxy()
    {
    }
    
    public EmailResultCallbackProxy(EmailResultCallback delegate)
    {
        setDelegate(delegate);
    }

    /**
     * {@inheritDoc}
     */
    public void reportAttempt(Context ctx)
    {
        getDelegate().reportAttempt(ctx);
    }


    /**
     * {@inheritDoc}
     */
    public void reportFailure(Context ctx, boolean recoverable, String msg)
    {
        getDelegate().reportFailure(ctx, recoverable, msg);
    }


    /**
     * {@inheritDoc}
     */
    public void reportFailure(Context ctx, boolean recoverable, Exception cause)
    {
        getDelegate().reportFailure(ctx, recoverable, cause);
    }


    /**
     * {@inheritDoc}
     */
    public void reportFailure(Context ctx, boolean recoverable, String msg, Exception cause)
    {
        getDelegate().reportFailure(ctx, recoverable, msg, cause);
    }


    /**
     * {@inheritDoc}
     */
    public void reportSuccess(Context ctx)
    {
        getDelegate().reportSuccess(ctx);
    }


    public void setDelegate(EmailResultCallback delegate)
    {
        this.delegate_ = delegate;
    }

    public EmailResultCallback getDelegate()
    {
        if (delegate_ == null)
        {
            return NullEmailResultCallback.instance();
        }
        return delegate_;
    }

    public final void addProxy(Context ctx, EmailResultCallbackProxy proxy)
    {
        proxy.setDelegate(getDelegate());

        setDelegate(proxy);  
    }


    public final void appendProxy(Context ctx, EmailResultCallbackProxy proxy)
    {
        if ( getDelegate() instanceof EmailResultCallbackProxy )
        {
            EmailResultCallbackProxy delegate = (EmailResultCallbackProxy) getDelegate();

            delegate.appendProxy(ctx, proxy);
        }
        else
        {
            addProxy(ctx, proxy);  
        }
    }


    public final EmailResultCallback findDecorator(Class cls)
    {
        if ( cls.isInstance(this) )
        {
            return this;
        }

        if ( getDelegate() instanceof EmailResultCallbackProxy )
        {
            return ((EmailResultCallbackProxy) getDelegate()).findDecorator(cls);  
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

    private EmailResultCallback delegate_ = null;
}
