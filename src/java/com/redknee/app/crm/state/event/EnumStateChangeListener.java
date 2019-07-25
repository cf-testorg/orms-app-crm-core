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
package com.redknee.app.crm.state.event;

import java.util.EventListener;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.state.StateAware;
import com.redknee.app.crm.state.StateChangeException;

/**
 * @author jchen
 *
 * Provides common interface with context enabled.
 * 
 * 
 * Interface also provides rollback method, because rollback might require a stateful object,
 * So, in general, it would be better to register and unregister listeners 
 * at run time within a short period
 */
public interface EnumStateChangeListener  extends EventListener 
{
	/**
	 * Create state refer to event source of state  owner profile creating 
	 * @param ctx
	 * @param newStateOwner
	 */
	public void onEnumStateCreate(Context ctx, StateAware newStateOwner) throws HomeException;
	public void rollbackCreate(Context ctx, StateAware newStateOwner);
	
	/**
	 * Remove state refers to event source of state owner profile removing
	 * @param ctx
	 * @param newStateOwner
	 */
	public void onEnumStateRemove(Context ctx, StateAware newStateOwner);
	public void rollbackRemove(Context ctx, StateAware newStateOwner);
	
	/**
	 * Will be fired by EnumStateChange event,
	 * Neither oldStateOwner nor newStateOwner will be Null, 
	 * they should be dispatched to create, remove method.
	 * @param ctx
	 * @param oldStateOwner
	 * @param newStateOwner
	 * @throws StateChangeException
	 */
	public void onEnumStateChange(Context ctx, StateAware oldStateOwner, StateAware newStateOwner) throws HomeException;
	
	/**
	 * 
	 * Called once one of event listener throwing exceptions to allowing rollback.
	 * @param ctx
	 * @param oldStateOwner
	 * @param newStateOwner
	 * @throws StateChangeException
	 */
	public void rollbackStore(Context ctx, StateAware oldStateOwner, StateAware newStateOwner) throws HomeException;
	
}
