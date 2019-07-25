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
 * An exception used to report errors that occur throughout the move process.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class MoveException extends MoveWarningException
{
    public MoveException(MoveRequest request, String msg)
    {
        super(request, msg);
    }

    
    public MoveException(MoveRequest request, String msg, Throwable cause)
    {
        super(request, msg, cause);
    }
}
