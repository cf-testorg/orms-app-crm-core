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
package com.redknee.app.crm.move.processor;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveProcessor;
import com.redknee.app.crm.move.MoveRequest;


/**
 * This move processor should be used to restrict calls to move in situations
 * where we want to protect the system from changes.
 * 
 * E.g. During validation, we don't want something to 'accidentally' call move.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class ReadOnlyMoveProcessor<MR extends MoveRequest> extends MoveProcessorProxy<MR>
{
    public ReadOnlyMoveProcessor(String reason)
    {
        super();
        reason_ = reason;
    }

    public ReadOnlyMoveProcessor(MoveProcessor<MR> delegate, String reason)
    {
        super(delegate);
        reason_ = reason;
    }

    @Override
    public void move(Context ctx) throws MoveException
    {
        String reason = reason_;
        if (reason == null || reason.trim().length() == 0)
        {
            reason = "Unspecified reason";
        }
        throw new MoveException(getRequest(), "Move request not allowed: " + reason);
    }
    
    private String reason_ = null;
}
