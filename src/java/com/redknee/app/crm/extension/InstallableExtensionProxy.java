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
 * @since 
 */
public class InstallableExtensionProxy implements InstallableExtension
{
    public InstallableExtensionProxy()
    {
        this(NullInstallableExtension.instance());
    }
    
    public InstallableExtensionProxy(InstallableExtension delegate)
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
     * @{inheritDoc}
     */
    public void install(Context ctx) throws ExtensionInstallationException
    {
        getDelegate().install(ctx);
    }


    /**
     * @{inheritDoc}
     */
    public void uninstall(Context ctx) throws ExtensionInstallationException
    {
        getDelegate().uninstall(ctx);
    }


    /**
     * @{inheritDoc}
     */
    public void update(Context ctx) throws ExtensionInstallationException
    {
        getDelegate().update(ctx);
    }
    

    /**
     * {@inheritDoc}
     */
    public Object ID()
    {
        return getDelegate().ID();
    }
    
    public void setDelegate(InstallableExtension delegate)
    {
        delegate_ = delegate;
    }


    public InstallableExtension getDelegate()
    {
        return delegate_;
    }

    protected InstallableExtension delegate_;

    
}
