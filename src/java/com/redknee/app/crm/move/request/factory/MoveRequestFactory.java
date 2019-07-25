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
package com.redknee.app.crm.move.request.factory;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.move.MoveRequest;


/**
 * This interface is provided primarily to support unit testing.  When running
 * unit tests it may not be desirable to actually return the production version
 * of the factory.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public interface MoveRequestFactory
{
    public MoveRequest getInstance(Context ctx, Object bean);
    
    public MoveRequest getInstance(Context ctx, Object bean, Class<? extends MoveRequest> preferredType);
}
