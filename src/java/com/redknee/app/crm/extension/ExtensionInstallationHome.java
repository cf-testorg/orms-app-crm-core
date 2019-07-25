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
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;

import com.redknee.app.crm.support.FrameworkSupportHelper;

/**
 * 
 *
 * @author Aaron Gourley
 * @since 
 */
public class ExtensionInstallationHome extends HomeProxy
{
    public ExtensionInstallationHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }

    public ExtensionInstallationHome(Home delegate)
    {
        super(delegate);
    }

    @Override
    public Object create(Context ctx, Object obj) throws HomeException
    {
        if (obj instanceof MovableExtension
                && ctx.getBoolean(MovableExtension.MOVE_IN_PROGRESS_CTX_KEY, false))
        {
            // Skip extension's normal install logic.  Rely on move logic to call move() for this extension.
        }
        else if( obj instanceof InstallableExtension )
        {
            InstallableExtension extension = (InstallableExtension)obj;
            try
            {
                extension.install(ctx);
            }
            catch (ExtensionInstallationException e)
            {
                if( e.wasExtensionUpdated() )
                {
                    HomeException he =  new HomeException("Partial failure installing " + extension.getName(ctx) + " extension. " + e.getMessage(), e);
                    FrameworkSupportHelper.get(ctx).notifyExceptionListener(ctx, he);
                }
                else
                {
                    HomeException he = new HomeException("Failed to install " + extension.getName(ctx) + " extension. " + e.getMessage(), e);
                    FrameworkSupportHelper.get(ctx).notifyExceptionListener(ctx, he);
                    throw he;
                }
            }
        }
        return super.create(ctx, obj);
    }

    @Override
    public void remove(Context ctx, Object obj) throws HomeException
    {
        if (obj instanceof MovableExtension
                && ctx.getBoolean(MovableExtension.MOVE_IN_PROGRESS_CTX_KEY, false))
        {
            // Skip extension's normal uninstall logic.  Rely on move logic to call move() for this extension.
        }
        else if( obj instanceof InstallableExtension )
        {
            InstallableExtension extension = (InstallableExtension)obj;
            try
            {
                extension.uninstall(ctx);
            }
            catch (ExtensionInstallationException e)
            {
                HomeException he;
                if( e.wasExtensionUpdated() )
                {
                    he =  new HomeException("Partial failure uninstalling " + extension.getName(ctx) + " extension. " + e.getMessage(), e);
                }
                else
                {
                    he =  new HomeException("Failed to uninstall " + extension.getName(ctx) + " extension. " + e.getMessage(), e);   
                }

                if (e.isFatal())
                {
                    throw he;
                }
                else
                {
                    FrameworkSupportHelper.get(ctx).notifyExceptionListener(ctx, he);
                }
            }
        }
        super.remove(ctx, obj);
    }

    @Override
    public Object store(Context ctx, Object obj) throws HomeException
    {
        if (obj instanceof MovableExtension
                && ctx.getBoolean(MovableExtension.MOVE_IN_PROGRESS_CTX_KEY, false))
        {
            // Skip extension's normal install/update/uninstall logic.  Rely on move logic to call move() for this extension.
        }
        else if( obj instanceof InstallableExtension )
        {   
            InstallableExtension extension = (InstallableExtension)obj;
            try
            {
                extension.update(ctx);
            }
            catch (ExtensionInstallationException e)
            {
                HomeException he;
                
                if( e.wasExtensionUpdated() )
                {
                    he = new HomeException("Partial failure updating " + extension.getName(ctx) + " extension. " + e.getMessage(), e);
                }
                else
                {
                    he =  new HomeException("Failed to update " + extension.getName(ctx) + " extension. " + e.getMessage(), e); 
                }
                
                if (e.isFatal())
                {
                    throw he;
                }
                else
                {
                    FrameworkSupportHelper.get(ctx).notifyExceptionListener(ctx, he);
                }
            }
        }
        return super.store(ctx, obj);
    }

}
