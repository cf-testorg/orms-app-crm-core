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
package com.redknee.app.crm.extension;

import com.redknee.framework.xhome.context.Context;


/**
 * Standard proxy implementation
 *
 * @author Aaron Gourley
 * @since 7.4.16
 */
public class ExtensionProxy implements Extension
{
    public ExtensionProxy()
    {
        this(NullExtension.instance());
    }
    
    public ExtensionProxy(Extension delegate)
    {
        setDelegate(delegate);
    }

    
    /**
     * @{inheritDoc}
     */
    public String getDescription(Context ctx)
    {
        return getDelegate().getDescription(ctx);
    }

    
    /**
     * @{inheritDoc}
     */
    public String getName(Context ctx)
    {
        return getDelegate().getName(ctx);
    }


    /**
     * @{inheritDoc}
     */
    public String getSummary(Context ctx)
    {
        return getDelegate().getSummary(ctx);
    }
    

    /**
     * {@inheritDoc}
     */
    public Object ID()
    {
        return getDelegate().ID();
    }

    public void setDelegate(Extension delegate)
    {
        delegate_ = delegate;
    }


    public Extension getDelegate()
    {
        return delegate_;
    }

    protected Extension delegate_;
}
