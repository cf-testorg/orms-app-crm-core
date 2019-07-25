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
 * The usual null style implementation of the MoveProcessor interface.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class NullMoveProcessor implements MoveProcessor
{
    private static MoveProcessor instance_ = null;
    public static MoveProcessor instance()
    {
        if (instance_ == null)
        {
            instance_ = new NullMoveProcessor();
        }
        return instance_;
    }
    
    private NullMoveProcessor()
    {
        
    }

    public MoveRequest getRequest()
    {
        return null;
    }

    public void move(Context ctx) throws MoveException
    {
    }

    public Context setUp(Context ctx) throws MoveException
    {
        return ctx;
    }

    public void tearDown(Context ctx) throws MoveException
    {
    }

    public void validate(Context ctx) throws IllegalStateException
    {
    }
    
    @Override
    public String toString()
    {
        return this.getClass().getName();
    }

}
