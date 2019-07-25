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
 * Indicates that an exception occurred while making an OCG client call.
 *
 * @author jimmy.ng@redknee.com
 */
public class AppOcgClientException
    extends ClientException
{
    /**
     * Creates a new AppOcgClientException.
     *
     * @param message The message to display.
     * @param throwable The throwable that was the initiating cause of this
     * exception.
     * @param resultCode The result code (if any) returned by the client that
     * prompted this exception.
     */
    public AppOcgClientException(
        final String message,
        final Throwable throwable,
        final short resultCode)
    {
        super(message, throwable, resultCode);
    }


    /**
     * Creates a new AppOcgClientException.
     *
     * @param message The message to display.
     * @param resultCode The result code (if any) returned by the client that
     * prompted this exception.
     */
    public AppOcgClientException(final String message, final short resultCode)
    {
        super(message, resultCode);
    }

    /**
     * Creates a new AppOcgClientException.
     *
     * @param message The message to display.
     * @param throwable The throwable that was the initiating cause of this
     * exception.
     */
    public AppOcgClientException(final String message, final Throwable throwable)
    {
        super(message, throwable);
    }    

    /**
     * Creates a new AppOcgClientException.
     *
     * @param message The message to display.
     */
    public AppOcgClientException(final String message)
    {
        super(message);
    }

} // class
