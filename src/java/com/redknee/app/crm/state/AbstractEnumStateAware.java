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

import java.util.Map;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xhome.xenum.EnumCollection;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public abstract class AbstractEnumStateAware implements EnumStateAware
{
    /**
     * Gets the state handler for the given state owner.
     *
     * @param context
     *            The operating context.
     * @param stateOwner
     *            The state owner for which to get the state handler.
     * @return The state handler for the given state type.
     */
    public EnumState getState(final Context context, final StateAware stateOwner)
    {
        Enum abstractState;
        if (stateOwner == null)
        {
            abstractState = null;
        }
        else
        {
            abstractState = stateOwner.getAbstractState();
        }
        return getState(context, abstractState);
    }


    /**
     * Find the state object corresponding to the state enum provided.
     *
     * @param type
     *            Enum state.
     * @param states
     *            Map of (State enum, state object).
     * @return The state object.
     */
    protected EnumState findStateInstance(final Enum type, final Map states)
    {
        return (EnumState) states.get(type);
    }
    
    
    /**
     * Return the state enum collection.
     *
     * @return States enum collection.
     */
    protected abstract EnumCollection getEnumCollection();

}
