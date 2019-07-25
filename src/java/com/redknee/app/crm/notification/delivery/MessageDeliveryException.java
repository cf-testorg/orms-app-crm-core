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
package com.redknee.app.crm.notification.delivery;


/**
 * This exception is intended to be used internally by delivery services.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class MessageDeliveryException extends Exception
{
    public MessageDeliveryException(String msg, int numRetriesAllowed)
    {
        super(msg);
        numRetriesAllowed_ = numRetriesAllowed;
    }
    
    public MessageDeliveryException(String msg, int numRetriesAllowed, Throwable cause)
    {
        super(msg, cause);
        numRetriesAllowed_ = numRetriesAllowed;
    }
    
    public MessageDeliveryException(String msg, boolean recoverable)
    {
        super(msg);
        initNumRetriesAllowed(recoverable, null);
    }
    
    public MessageDeliveryException(String msg, boolean recoverable, Throwable cause)
    {
        super(msg, cause);
        initNumRetriesAllowed(recoverable, cause);
    }

    public MessageDeliveryException(boolean recoverable, Throwable cause)
    {
        super(cause);
        initNumRetriesAllowed(recoverable, cause);
    }

    protected void initNumRetriesAllowed(boolean recoverable, Throwable cause)
    {
        if (recoverable && cause instanceof MessageDeliveryException)
        {
            int oldRetriesAllowed = ((MessageDeliveryException)cause).getNumRetriesAllowed();
            if (oldRetriesAllowed <= 0)
            {
                numRetriesAllowed_ = oldRetriesAllowed;
            }
            else
            {
                numRetriesAllowed_ = oldRetriesAllowed - 1;
            }
        }
        else
        {
            numRetriesAllowed_ = (recoverable ? -1 : 0);
        }
    }

    public int getNumRetriesAllowed()
    {
        return numRetriesAllowed_;
    }
    
    public boolean isRecoverable()
    {
        return numRetriesAllowed_ != 0;
    }

    protected int numRetriesAllowed_ = 0;
}
