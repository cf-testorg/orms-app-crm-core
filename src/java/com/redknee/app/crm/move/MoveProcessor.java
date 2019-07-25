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

import com.redknee.framework.xhome.context.Context;


/**
 * Interface defining the stages of a move operation.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public interface MoveProcessor<MR extends MoveRequest>
{
    public Context setUp(Context ctx) throws MoveException;
    public void tearDown(Context ctx) throws MoveException;

    public void validate(Context ctx) throws IllegalStateException;

    public void move(Context ctx) throws MoveException;
    
    public MR getRequest();
}
