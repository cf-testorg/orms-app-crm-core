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

import com.redknee.app.crm.bean.externalapp.ExternalAppEnum;


/**
 * 
 *
 * @author Marcio Marques
 * @since 9_1_2
 */
public class ExtensionAssociationException extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ExtensionAssociationException(ExternalAppEnum externalApp, String message, String description, int resultCode, Throwable cause, boolean extensionAssociated)
    {
        super(message, cause);
        extensionAssociated_ = extensionAssociated;
        externalApp_ = externalApp;
        description_ = description;
        resultCode_ = resultCode;
    }

    public ExtensionAssociationException(ExternalAppEnum externalApp, String message, int resultCode, Throwable cause, boolean extensionAssociated)
    {
        this(externalApp, message, null, resultCode, cause, extensionAssociated);
    }

    public ExtensionAssociationException(ExternalAppEnum externalApp, String message, String description, int resultCode, Throwable cause)
    {
        this(externalApp, message, description, resultCode, cause, false);
    }

    public ExtensionAssociationException(ExternalAppEnum externalApp, String message, String description, int resultCode, boolean extensionAssociated)
    {
        this(externalApp, message, description, resultCode, null, extensionAssociated);
    }

    public ExtensionAssociationException(ExternalAppEnum externalApp, String message, String description, int resultCode)
    {
        this(externalApp, message, description, resultCode, null);
    }

    public ExtensionAssociationException(ExternalAppEnum externalApp, String message, int resultCode, Throwable cause)
    {
        this(externalApp, message, null, resultCode, cause);
    }

    public ExtensionAssociationException(ExternalAppEnum externalApp, String message, int resultCode, boolean extensionAssociated)
    {
        this(externalApp, message, null, resultCode, extensionAssociated);
    }

    public ExtensionAssociationException(ExternalAppEnum externalApp, String message, int resultCode)
    {
        this(externalApp, message, null, resultCode);
    }

    public boolean wasExtensionAssociated()
    {
        return extensionAssociated_;
    }
    
    public int getResultCode()
    {
        return resultCode_;
    }
    
    public ExternalAppEnum getExternalApp()
    {
        return externalApp_;
    }
    
    public String getDescription()
    {
        return description_;
    }

    protected String description_;
    protected boolean extensionAssociated_;
    protected int resultCode_;
    protected ExternalAppEnum externalApp_;


}
