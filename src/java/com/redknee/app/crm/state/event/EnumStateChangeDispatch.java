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

import java.util.ArrayList;
import java.util.Collection;

import com.redknee.app.crm.state.StateAware;
import com.redknee.app.crm.state.StateChangeException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.DebugLogMsg;

/**
 * @author jchen
 */
public class EnumStateChangeDispatch
{	   
	   public void addHomeChangeListener(EnumStateChangeListener listener)
	   {
	      listeners_.add(listener);
	   }
	   
	   public void addHomeChangeListener(Collection listeners)
	   {
	      listeners_.addAll(listeners);
	   }
	   
	   public void removeHomeChangeListener(EnumStateChangeListener listener)
	   {
	      listeners_.remove(listener);
	   }
	   
	   /**
	    * Fire state changes to all listeners, and trying to rollback one by one in
	    * reverse order if encounters exception. 
	    * 
	    * If exception happened in the rollback, rollback will stops.
	    * If no error in rollback, the exception is the original exception in the listener.
	    * 
	    * @param parentCtx
	    * @param oldStateOwner
	    * @param newStateOwner
	    * @throws StateChangeException
	    */
	   public void fire(Context parentCtx, StateAware oldStateOwner, StateAware newStateOwner) throws HomeException
	   {
	   	//Context ctx = parentCtx.createSubContext();
	   	Context ctx = parentCtx; 
	   	if ( listeners_.isEmpty() )
	      {
	         return;
	      }
	      
	   	int size = listeners_.size();
	   	int cnt = 0;
	   	try
	   	{
	      for (cnt = 0; cnt < size; cnt++)
	      {
	        if (oldStateOwner == null)
	        	((EnumStateChangeListener)(listeners_.get(cnt))).onEnumStateCreate(ctx, newStateOwner);
	        else if (newStateOwner == null)
	        	((EnumStateChangeListener)(listeners_.get(cnt))).onEnumStateRemove(ctx, oldStateOwner);
	        else
	        	((EnumStateChangeListener)(listeners_.get(cnt))).onEnumStateChange(ctx, oldStateOwner, newStateOwner);
	      }
	   	}
	   	catch(Throwable exp)
	   	{
	   		try
	   		{
		   		for (int i = (cnt - 1); i > -1; i--)
		   		{
		   			if (oldStateOwner == null)
			        	((EnumStateChangeListener)(listeners_.get(cnt))).rollbackCreate(ctx, newStateOwner);
			        else if (newStateOwner == null)
			        	((EnumStateChangeListener)(listeners_.get(cnt))).rollbackRemove(ctx, oldStateOwner);
			        else
			        	((EnumStateChangeListener)(listeners_.get(cnt))).rollbackStore(ctx, oldStateOwner, newStateOwner);
		   		}
	   		}
	   		catch(Throwable exp2)
	   		{
	   			//TODO, we lose the original exp information, what do we do???
	   			new DebugLogMsg(
	   	                this,
	   	                "Original Exception of EnumStateListener error.  index=" + cnt,
	   	                exp).log(ctx);
	   			throw new StateChangeException("Rollback failed original error" + exp + ", rollback exp" + exp2 + "original error index=" + cnt, exp2);
	   		}
	   		throw new StateChangeException("Rollback sucessfully. original error =" + exp, exp);
	   	}
	   }
	   ArrayList listeners_ = new ArrayList();
}