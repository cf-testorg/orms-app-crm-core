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
import java.util.Collections;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xhome.xenum.EnumCollection;

import com.redknee.app.crm.support.EnumStateSupportHelper;


/**
 * State transition support class.
 *
 * @author joe.chen@redknee.com
 */
public abstract class EnumStateTransitionSupport
{

    /**
     * Returns the {@link EnumStateSupportHelper} class for this state transition support class.
     *
     * @return The enum state support class for this state transition support class.
     */
    public abstract EnumStateAware getEnumStateSupport();


    /**
     * Return the iterator of all states to which manual state transition is possible
     * (i.e., all states to which state transition is permitted, plus the current state)
     * for the given state owner, or simply the iterator of all states if the state owner
     * is null.
     *
     * @param ctx
     *            The operating context.
     * @param stateOwner
     *            The given state owner.
     * @return The collection of all states possible for state transition.
     */
    public Collection<? extends Enum> getPossibleManualStateCollection(final Context ctx, final StateAware stateOwner, final Enum state)
    {

        Enum oldState = state;
        if (state == null && stateOwner != null)
        {
            oldState = stateOwner.getAbstractState();
        }
        final EnumState stateObject = getEnumStateSupport().getState(ctx, oldState);

        if (stateObject != null)
        {
            return stateObject.getStatesPermittedForManualTransition(ctx, stateOwner);
        }
        return Collections.emptySet();
    }


    /**
     * Return the iterator of all states to which manual state transition is possible
     * (i.e., all states to which state transition is permitted, plus the current state)
     * for the given state owner, or simply the iterator of all states if the state owner
     * is null.
     *
     * @param ctx
     *            The operating context.
     * @param stateOwner
     *            The given state owner.
     * @return The collection of all states possible for state transition.
     */
    public Collection<? extends Enum> getPossibleManualStateCollection(final Context ctx, final StateAware stateOwner)
    {
        Enum state = null;
        if (stateOwner != null)
        {
            state = stateOwner.getAbstractState();
        }
        return getPossibleManualStateCollection(ctx, stateOwner, state);
    }


    /**
     * Return the iterator of all states to which state transition is possible (i.e., all
     * states to which state transition is permitted, plus the current state) for the
     * given state owner, or simply the iterator of all states if the state owner is null.
     *
     * @param ctx
     *            The operating context.
     * @param stateOwner
     *            The given state owner.
     * @return The enum collection of all states possible for state transition.
     */
    public EnumCollection getPossibleManualStateEnumCollection(final Context ctx, final StateAware stateOwner)
    {
        return EnumStateSupportHelper.get(ctx).toEnumCollection(getPossibleManualStateCollection(ctx, stateOwner));
    }


    /**
     * Determines whether it is permissible to for any state owner of the supported
     * StateAware type to enter the provided state programmatically.
     *
     * @param context
     *            The operating context.
     * @param state
     *            State enum.
     * @return Returns <code>true</code> if it is a valid state for the supported
     *         StateAware type.
     */
    public boolean isStateValid(final Context context, final Enum state)
    {
        final EnumState targetState = getEnumStateSupport().getState(context, state);
        return targetState != null;
    }


    /**
     * Return the iterator of all states to which state transition is possible (i.e., all
     * states to which state transition is permitted, plus the current state) for the
     * given state owner, or simply the iterator of all states if the state owner is null.
     *
     * @param context
     *            The operating context.
     * @param stateOwner
     *            The given state owner.
     * @return The collection of all states possible for state transition.
     */
    public Collection getPossibleStateCollection(final Context context, final StateAware stateOwner)
    {
        Enum state = null;
        if (stateOwner != null)
        {
            state = stateOwner.getAbstractState();
        }
        return getPossibleStateCollection(context, stateOwner, state);
    }


    /**
     * Return the iterator of all states to which state transition is possible (i.e., all
     * states to which state transition is permitted, plus the current state) for the
     * given state owner, or simply the iterator of all states if the state owner is null.
     *
     * @param context
     *            The operating context.
     * @param stateOwner
     *            The given state owner.
     * @return The collection of all states possible for state transition.
     */
    public Collection getPossibleStateCollection(final Context context, final StateAware stateOwner, final Enum state)
    {
        Enum oldState = state;
        if (oldState == null && stateOwner != null)
        {
            oldState = stateOwner.getAbstractState();
        }
        final EnumState stateObject = getEnumStateSupport().getState(context, oldState);
        if (stateObject != null)
        {
            return stateObject.getStatesPermittedForTransition(context, stateOwner);
        }
        return Collections.emptySet();
    }


    /**
     * Determines if the state transition is allowed for the given state owner.
     *
     * @param context
     *            The operating context.
     * @param stateOwner
     *            State owner.
     * @param oldState
     *            Previous state of the state owner.
     * @param newState
     *            New state of the state owner.
     * @return Returns whether the state transition is allowed for the given state owner.
     */
    public boolean isStateTransitionAllowed(final Context context, final StateAware stateOwner, final Enum oldState,
        final Enum newState)
    {
        final Collection states = getPossibleStateCollection(context, stateOwner, oldState);
        boolean result = false;
        final EnumState startState = getEnumStateSupport().getState(context, oldState);
        if (startState != null && startState.isStateTransitionPermitted(context, stateOwner, newState))
        {
            result = true;
        }
        return result;
    }


    /**
     * Determines if the state transition is allowed for the given state owner.
     *
     * @param context
     *            The operating context.
     * @param stateOwner
     *            State owner.
     * @param newState
     *            New state of the state owner.
     * @return Returns whether the state transition is allowed for the given state owner.
     */
    public boolean isStateTransitionAllowed(final Context context, final StateAware stateOwner, final Enum newState)
    {
        boolean result = false;
        if (stateOwner != null)
        {
            result = isStateTransitionAllowed(context, stateOwner, stateOwner.getAbstractState(), newState);
        }
        return result;
    }


    /**
     * Determines if the manual state transition is allowed for the given state owner.
     *
     * @param context
     *            The operating context.
     * @param stateOwner
     *            State owner.
     * @param oldState
     *            Previous state of the state owner.
     * @param newState
     *            New state of the state owner.
     * @return Returns whether the manual state transition is allowed for the given state
     *         owner.
     */
    public boolean isManualStateTransitionAllowed(final Context context, final StateAware stateOwner,
        final Enum oldState, final Enum newState)
    {
        boolean result = false;
        final EnumState startState = getEnumStateSupport().getState(context, oldState);
        if (startState != null && startState.isManualStateTransitionPermitted(context, stateOwner, newState))
        {
            result = true;
        }
        return result;
    }


    /**
     * Determines if the manual state transition is allowed for the given state owner.
     *
     * @param context
     *            The operating context.
     * @param stateOwner
     *            State owner.
     * @param newState
     *            New state of the state owner.
     * @return Returns whether the manual state transition is allowed for the given state
     *         owner.
     */
    public boolean isManualStateTransitionAllowed(final Context context, final StateAware stateOwner,
        final Enum newState)
    {
        boolean result = false;
        if (stateOwner != null)
        {
            result = isManualStateTransitionAllowed(context, stateOwner, stateOwner.getAbstractState(), newState);
        }
        return result;
    }

} // class

