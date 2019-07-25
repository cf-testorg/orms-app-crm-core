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
package com.redknee.app.crm.bundle;

/**
 * Exception thrown if the bundle profile API is invalid.
 *
 * @author larry.xia@redknee.com
 */
public class InvalidBundleApiException extends Exception
{

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;


    /**
     * Create a new instance of <code>InvalidBundleApiException</code>.
     *
     * @param msg
     *            Detailed error message.
     */
    public InvalidBundleApiException(final String msg)
    {
        super(msg);
    }

}
