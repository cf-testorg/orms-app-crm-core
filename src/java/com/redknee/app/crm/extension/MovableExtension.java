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
 * This interface is to be used by extensions that have some dependency on external services or data
 * AND require some special provisioning in order to properly move itself from one container to another.
 * It provides a means to move the extension's features.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public interface MovableExtension extends InstallableExtension
{
    Object MOVED_SUBSCRIBER_CTX_KEY = "PPSMSupporteeSubExtension.MovedSubscriber";

    Object MOVE_IN_PROGRESS_CTX_KEY = "MovableExtension.IsMoveInProgress";

    /**
     * Move must be called on a Movable Extension when it's features/functionality should be applied to a new container
     * entity.  The extension is responsible for updating its own external provisioning, updating up any other dependent
     * data, or anything else that must be done to move the extension.
     * 
     * @param ctx The operating Context
     * @throws ExtensionInstallationException On error, this exception is thrown.  If the exception's wasExtensionUpdated() flag is set, then the extension itself was modified during installation and should be stored accordingly.
     */
    public void move(Context ctx, Object newContainer) throws ExtensionInstallationException;
}
