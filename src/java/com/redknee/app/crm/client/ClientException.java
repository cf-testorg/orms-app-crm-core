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

import com.redknee.app.crm.support.ExternalAppSupport;


/**
 * Indicates that an exception occurred while making a client call.
 *
 * @author jimmy.ng@redknee.com
 */
public class ClientException
    extends Exception
{
    /**
     * Used to indicate that no result code was set in this exception.
     */
    public static final short NO_RESULT = ExternalAppSupport.UNKNOWN;


    /**
     * Creates a new ClientException.
     *
     * @param message The message to display.
     * @param throwable The throwable that was the initiating cause of this
     * exception.
     * @param resultCode The result code (if any) returned by the client that
     * prompted this exception.
     */
    public ClientException(
        final String message,
        final Throwable throwable,
        final short resultCode)
    {
        super(getResultEncodedMessage(message, resultCode), throwable);
        resultCode_ = resultCode;
    }


    /**
     * Creates a new ClientException.
     *
     * @param message The message to display.
     * @param resultCode The result code (if any) returned by the client that
     * prompted this exception.
     */
    public ClientException(final String message, final short resultCode)
    {
        this(message, null, resultCode);
    }


    /**
     * Creates a new ClientException.
     *
     * @param message The message to display.
     * @param throwable The throwable that was the initiating cause of this
     * exception.
     * @param resultCode The result code (if any) returned by the client that
     * prompted this exception.
     */
    public ClientException(
        final String message,
        final Throwable throwable)
    {
        this(message, throwable, NO_RESULT);
    }


    /**
     * Creates a new ClientException.
     *
     * @param message The message to display.
     * @param resultCode The result code (if any) returned by the client that
     * prompted this exception.
     */
    public ClientException(final String message)
    {
        this(message, null, NO_RESULT);
    }
    /**
     * Gets the result code of the client operation if one is available;
     * NO_RESULT otherwise.
     *
     * @return The result code.
     */
    public short getResultCode()
    {
        return resultCode_;
    }

    /**
     * Gets a new message with the given result code encoded into it.  If the
     * given result code is NO_RESULT then the returned message is simply the
     * message passed in.
     *
     * @param message The exception message.
     * @param resultCode The result code to add to the message.
     *
     * @return A new message with the given result code encoded into it.
     */
    private static String getResultEncodedMessage(final String message, final short resultCode)
    {
        final String encodedMessage;
        
        if (resultCode == NO_RESULT)
        {
            encodedMessage = message;
        }
        else
        {
            encodedMessage =
                message + " Result code: " + resultCode + ".";
        }

        return encodedMessage;
    }
    
    /**
     * The result code of the client operation if one is available;
     * NO_RESULT otherwise.
     */
    private final short resultCode_;

} // class
