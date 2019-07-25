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
 * 
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class MovableExtensionProxy extends InstallableExtensionProxy implements MovableExtension
{
    public MovableExtensionProxy()
    {
        this(NullMovableExtension.instance());
    }
    
    public MovableExtensionProxy(MovableExtension delegate)
    {
        setDelegate(delegate);
    }
    

    /**
     * @{inheritDoc}
     */
    public void move(Context ctx, Object newContainer) throws ExtensionInstallationException
    {
        getDelegate().move(ctx, newContainer);
    }

    
    public void setDelegate(MovableExtension delegate)
    {
        super.setDelegate(delegate);
    }


    @Override
    public MovableExtension getDelegate()
    {
        return delegate_;
    }

    protected MovableExtension delegate_;

    
}
