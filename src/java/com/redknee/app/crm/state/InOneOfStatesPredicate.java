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
package com.redknee.app.crm.state;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xlog.log.InfoLogMsg;

import com.redknee.app.crm.support.EnumStateSupportHelper;

/**
 * @author jchen
 * @author agourley
 * 
 * Filtering if a StateAware object is one of predefined states,
 * 
 */
public class InOneOfStatesPredicate<T extends Enum> implements Predicate
{
    public InOneOfStatesPredicate(T... states)
    {
        this(Arrays.asList(states));
    }
    
    public InOneOfStatesPredicate(Collection<T> states)
    {
        this(new HashSet<T>(states));
    }
    
    public InOneOfStatesPredicate(Set<T> states)
    {
        states_ = Collections.unmodifiableSet(states);
        isPossible_ = (states_ != null && states_.size() > 0);
        statesIndices_ = null;
    }

    /**
     * @deprecated Use {@link #InOneOfStatesPredicate(Enum...), #InOneOfStatesPredicate(Collection), or #InOneOfStatesPredicate(Set)}
     */
    @Deprecated
    public InOneOfStatesPredicate(int stateIndex)
    {
        this(new int[] { stateIndex });
    }

    /**
     * @deprecated Use {@link #InOneOfStatesPredicate(Enum...), #InOneOfStatesPredicate(Collection), or #InOneOfStatesPredicate(Set)}
     */
    @Deprecated
    public InOneOfStatesPredicate(int stateIndex1, int stateIndex2)
    {
        this(new int[] { stateIndex1,  stateIndex2});
    }

    /**
     * @deprecated Use {@link #InOneOfStatesPredicate(Enum...), #InOneOfStatesPredicate(Collection), or #InOneOfStatesPredicate(Set)}
     */
    @Deprecated
    public InOneOfStatesPredicate(int stateIndex1, int stateIndex2, int stateIndex3)
    {
        this(new int[] { stateIndex1,  stateIndex2, stateIndex3});
    }

    /**
     * @deprecated Use {@link #InOneOfStatesPredicate(Enum...), #InOneOfStatesPredicate(Collection), or #InOneOfStatesPredicate(Set)}
     */
    @Deprecated
    public InOneOfStatesPredicate(int[] stateIndices)
    {
        statesIndices_ = stateIndices;
        isPossible_ = (statesIndices_ != null && statesIndices_.length > 0);
        states_ = null;
    }
	
	/* (non-Javadoc)
	 * @see com.redknee.framework.xhome.filter.Predicate#f(com.redknee.framework.xhome.context.Context, java.lang.Object)
	 */
	public boolean f(Context ctx, Object obj) 
	{
	    boolean ret = true;
	    if (isPossible_)
	    {
	        Enum state = null;
	        if (obj instanceof StateAware)
	        {
	            StateAware stObj = (StateAware)obj;
	            state = stObj.getAbstractState();
	        }
	        else if (obj instanceof Enum)
	        {
	            state = (Enum)obj;
	        }
	        if (state != null)
	        {
	            if (EnumStateSupportHelper.get(ctx).isOneOfStates(state, states_)
	                    || EnumStateSupportHelper.get(ctx).isOneOfStates(state.getIndex(), statesIndices_))
	            {
	                ret = true;
	            }
	            else
	            {
	                ret = false;
	            }
	        }
	        else
	        {
	            new InfoLogMsg(this, "Invalid object passed into state checking predicate " + obj, null).log(ctx);
	        }
	    }
	    return ret;
	}

	protected final boolean isPossible_;
    protected final int[] statesIndices_;
	protected final Set<T> states_;
}
