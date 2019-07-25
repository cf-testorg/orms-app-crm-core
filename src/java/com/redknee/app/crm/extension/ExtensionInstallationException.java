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


/**
 * 
 *
 * @author Aaron Gourley
 * @since 
 */
public class ExtensionInstallationException extends Exception
{

    public ExtensionInstallationException(boolean extensionUpdated, boolean fatal)
    {
        super();
        extensionUpdated_ = extensionUpdated;
        fatal_ = fatal;
    }

    public ExtensionInstallationException(String message, Throwable cause, boolean extensionUpdated, boolean fatal)
    {
        super(message, cause);
        extensionUpdated_ = extensionUpdated;
        fatal_ = fatal;
    }

    public ExtensionInstallationException(String message, boolean extensionUpdated, boolean fatal)
    {
        super(message);
        extensionUpdated_ = extensionUpdated;
        fatal_ = fatal;
    }

    public ExtensionInstallationException(Throwable cause, boolean extensionUpdated, boolean fatal)
    {
        super(cause);
        extensionUpdated_ = extensionUpdated;
        fatal_ = fatal;
    }

    public ExtensionInstallationException(boolean extensionUpdated)
    {
        this(extensionUpdated, false);
    }

    public ExtensionInstallationException(String message, Throwable cause, boolean extensionUpdated)
    {
        this(message, cause, extensionUpdated, false);
    }

    public ExtensionInstallationException(String message, boolean extensionUpdated)
    {
        this(message, extensionUpdated, false);
    }

    public ExtensionInstallationException(Throwable cause, boolean extensionUpdated)
    {
        this(cause, extensionUpdated, false);
    }
    
    public void setExtensionUpdated(boolean updateState)
    {
        extensionUpdated_ = updateState;
    }
    
    public boolean wasExtensionUpdated()
    {
        return extensionUpdated_;
    }
    
    public boolean isFatal()
    {
        return fatal_;
    }

    
    public void setFatal(boolean fatal)
    {
        fatal_ = fatal;
    }

    protected boolean extensionUpdated_;

    protected boolean fatal_;

    
}
