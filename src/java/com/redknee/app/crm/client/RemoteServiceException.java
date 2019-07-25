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
package com.redknee.app.crm.client;

/**
 * @author victor.stratan@redknee.com
 */
public class RemoteServiceException extends Exception
{
    public RemoteServiceException(final int errorCode, final String message)
    {
        super(message);
        errorCode_ = errorCode;
    }

    public RemoteServiceException(final int errorCode, final String message, final Throwable cause)
    {
        super(message, cause);
        errorCode_ = errorCode;
    }

    /**
     * Gets the error code returned by URCS, if available.
     *
     * @return The error code returned by URCS, if available; -1 otherwise.
     */
    public int getErrorCode()
    {
        return errorCode_;
    }

    /**
     * The error code returned by URCS, if available.
     */
    private final int errorCode_;

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;
}
