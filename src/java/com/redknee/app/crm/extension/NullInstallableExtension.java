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
public class NullInstallableExtension extends NullExtension implements InstallableExtension
{
    private static InstallableExtension instance_ = null;
    public static InstallableExtension instance()
    {
        if( instance_ == null )
        {
            instance_ = new NullInstallableExtension();
        }
        return instance_;
    }

    /**
     * @{inheritDoc}
     */
    public void install(Context ctx) throws ExtensionInstallationException
    {
    }

    /**
     * @{inheritDoc}
     */
    public void uninstall(Context ctx) throws ExtensionInstallationException
    {
    }

    /**
     * @{inheritDoc}
     */
    public void update(Context ctx) throws ExtensionInstallationException
    {
    }

}
