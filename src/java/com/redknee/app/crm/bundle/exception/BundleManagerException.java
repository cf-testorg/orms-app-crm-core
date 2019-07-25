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
 * Thrown when a general Bundle Manager occurs
 * @author arturo.medina@redknee.com
 *
 */
public class BundleManagerException extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = 3205267502976479176L;

    /**
     * Default constructor
     */
    public BundleManagerException()
    {
    }

    /**
     * Accepts a message
     * @param message
     */
    public BundleManagerException(String message)
    {
        super(message);
    }

    /**
     * Accepts a cause
     * @param cause
     */
    public BundleManagerException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Accepts a message and a stack trace
     * @param message
     * @param cause
     */
    public BundleManagerException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    /**
     * 
     * @param message
     * @param cause
     * @param errorCode - Bundle Manager result code.
     */
    public BundleManagerException(String message, Throwable cause, int errorCode)
    {
        super(message, cause);
        this.errorCode_ = errorCode;
    }

	public int getErrorCode() {
		return errorCode_ ;
	}
	
	private int errorCode_ = -1;
}
