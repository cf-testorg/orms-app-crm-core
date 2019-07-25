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
package com.redknee.app.crm.bundle.exception;

/**
 * Thrown when trying create a subscriber profile that already exists
 * @author arturo.medina@redknee.com
 *
 */
public class SubscriberProfileAlreadyExistsException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 941962658015583183L;

    /**
     * Default constructor
     */
    public SubscriberProfileAlreadyExistsException()
    {
    }

    /**
     * Accepts a message
     * @param message
     */
    public SubscriberProfileAlreadyExistsException(String message)
    {
        super(message);
    }

    /**
     * Accepts a cause
     * @param cause
     */
    public SubscriberProfileAlreadyExistsException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Accepts a message and a stack trace
     * @param message
     * @param cause
     */
    public SubscriberProfileAlreadyExistsException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
