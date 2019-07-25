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
package com.redknee.app.crm.move;


/**
 * An exception 
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class MoveWarningException extends Exception
{
    public MoveWarningException(MoveRequest request, String msg)
    {
        super(msg);
        request_ = request;
    }

    
    public MoveWarningException(MoveRequest request, String msg, Throwable cause)
    {
        super(msg, cause);
        request_ = request;
    }


    /**
     * @{inheritDoc}
     */
    @Override
    public String getMessage()
    {
        return getMessage(true);
    }
    

    public String getMessage(boolean includeRequestInfo)
    {
        StringBuilder message = new StringBuilder();
        if (super.getMessage() != null)
        {
            message = new StringBuilder(super.getMessage());
        }
        if (request_ != null && includeRequestInfo)
        {
            message.append(" [Request ID=" + request_.ID() + "]");
        }
        return message.toString();
    }
    

    private MoveRequest request_ = null;
}
