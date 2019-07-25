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

import java.util.Collection;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.xenum.Enum;



/**
 * Interface for state object.
 *
 * @author joe.chen@redknee.com
 */

public interface EnumState
{

    /**
     * Gets the representative state type of this state handler.
     *
     * @return The representative state type of this state handler.
     */
    Enum getRepresentativeStateType();


    /**
     * Indicates whether or not this state handler supports transitioning to the given
     * state type.
     *
     * @param ctx
     *            The operating context.
     * @param oldStateOwner
     *            State owner.
     * @param type
     *            The state type to which the subscriber is transitioning.
     * @return True if the given transition is permitted; false otherwise.
     */
    boolean isStateTransitionPermitted(Context ctx, StateAware oldStateOwner, Enum type);


    /**
     * Indicates whether or not this state handler supports manual transitioning to the
     * given state type.
     *
     * @param ctx
     *            The operating context.
     * @param oldStateOwner
     *            State owner.
     * @param type
     *            The state type to which the subscriber is transitioning.
     * @return True if manual transition is permitted; false otherwise.
     */
    boolean isManualStateTransitionPermitted(Context ctx, StateAware oldStateOwner, Enum type);


    /**
     * Returns a collection of {@link Enum} states which a state owner is allowed to be
     * manually transitioned into.
     *
     * @param ctx
     *            The operating context.
     * @param oldStateOwner
     *            State owner.
     * @return A collection of {@link Enum} states which a state owner is allowed to be
     *         manually transitioned into.
     */
    Collection<? extends Enum> getStatesPermittedForManualTransition(Context ctx, StateAware oldStateOwner);


    /**
     * Returns a collection of {@link Enum} states which a state owner is allowed to be
     * transitioned into.
     *
     * @param ctx
     *            The operating context.
     * @param oldStateOwner
     *            State owner.
     * @return A collection of {@link Enum} states which a state owner is allowed to be
     *         transitioned into.
     */
    Collection<? extends Enum> getStatesPermittedForTransition(Context ctx, StateAware oldStateOwner);


    /**
     * Performs the work necessary when the state owner is switching to a new state.
     *
     * @param context
     *            The operating context.
     * @param curStateOwner
     *            The object that owns the current state.
     * @param nextStateOwner
     *            The object that owns the next state.
     */
    void transition(Context context, StateAware curStateOwner, StateAware nextStateOwner);


    /**
     * Performs the common work necessary when the state owner is leaving the current
     * state.
     *
     * @param context
     *            The operating context.
     * @param curStateOwner
     *            The state owner that is leaving the current state.
     * @exception StateChangeException
     *                Thrown if there is a problem leaving the current state.
     */
    void leaving(Context context, StateAware curStateOwner) throws StateChangeException;


    /**
     * Performs the common work necessary when the state owner is entering the current
     * state.
     *
     * @param context
     *            The operating context.
     * @param curStateOwner
     *            The state owner that is entering the current state.
     * @exception StateChangeException
     *                Thrown if there is a problem entering the current state.
     */
    void entering(Context context, StateAware curStateOwner) throws StateChangeException;

}
