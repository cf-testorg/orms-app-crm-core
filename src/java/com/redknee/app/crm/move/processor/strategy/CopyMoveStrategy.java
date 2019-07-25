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
package com.redknee.app.crm.move.processor.strategy;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveRequest;


/**
 * 
 *
 * @author Aaron Gourley
 * @since 
 */
public interface CopyMoveStrategy<MR extends MoveRequest>
{
    public void initialize(Context ctx, MR request);
    
    public void validate(Context ctx, MR request) throws IllegalStateException;

    public void createNewEntity(Context ctx, MR request) throws MoveException;
    public void removeOldEntity(Context ctx, MR request) throws MoveException;
}
