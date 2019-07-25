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

import java.util.Collection;


/**
 * A move dependency manager is used to retrieve information about other move requests
 * that are related to the source request.  The source request must not be returned as
 * its own dependency, otherwise an infinite loop of move operations could occur.
 * 
 * For example, when moving an account we must also move all subscriptions within the
 * account.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public interface MoveDependencyManager
{
    public MoveRequest getSourceRequest();
    public Collection<? extends MoveRequest> getDependencyRequests() throws MoveException;
}
