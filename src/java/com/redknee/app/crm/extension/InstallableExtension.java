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
 * This interface is to be used by extensions that have some dependency on external services or data.
 * It provides a means to install, update, and uninstall the extension.
 *
 * @author Aaron Gourley
 * @since 7.4.16
 */
public interface InstallableExtension extends Extension
{
    /**
     * Install must be called on an Installable Extension the first time it is being provisioned.  The extension is responsible
     * for its own external provisioning, setting up any other dependent data, etc.
     * 
     * @param ctx The operating Context
     * @throws ExtensionInstallationException On error, this exception is thrown.  If the exception's wasExtensionUpdated() flag is set, then the extension itself was modified during installation and should be stored accordingly.
     */
    public void install(Context ctx) throws ExtensionInstallationException;

    /**
     * Update must be called on an Installable Extension whenever the extension itself is updated.  The extension is responsible
     * for updating external dependencies.  Multiple update calls made on the same extension instance containing the same data
     * should be handled gracefully.
     * 
     * @param ctx The operating Context
     * @throws ExtensionInstallationException On error, this exception is thrown.  If the exception's wasExtensionUpdated() flag is set, then the extension itself was modified during installation and should be stored accordingly.
     */
    public void update(Context ctx) throws ExtensionInstallationException;
    
    /**
     * Uninstall must be called on an Installable Extension when it is being deprovisioned or disabled.  Basically, if it is not
     * desirable to have the extension data provisioned with external dependencies, then it should be uninstalled.
     *  
     * @param ctx The operating Context
     * @throws ExtensionInstallationException On error, this exception is thrown.  If the exception's wasExtensionUpdated() flag is set, then the extension itself was modified during installation and should be stored accordingly.
     */
    public void uninstall(Context ctx) throws ExtensionInstallationException;
}
