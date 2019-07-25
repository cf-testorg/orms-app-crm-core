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
package com.redknee.app.crm.support;

import java.util.Collection;

import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xhome.xenum.EnumCollection;

import com.redknee.app.crm.state.FinalStateAware;
import com.redknee.app.crm.state.StateAware;


/**
 * This is basically a copy of SubscriberStateSupport, since Subscriber Provisioning home
 * needs to be designed and re-architected, so just copy for now!
 *
 * @author joe.chen@redknee.com
 */
public interface EnumStateSupport extends Support
{
    /**
     * Return collection of AbstractEnum of states in array.
     *
     * @param ec
     *            Enum collection of states.
     * @param enumIndices
     *            Array of enum indices.
     * @return Collection of enum.
     */
    public Collection<? extends Enum> toAbstractEnumCollection(final EnumCollection ec, final int[] enumIndices);


    /**
     * Converting AbstractEnum collection to a sorted EnumCollection.
     *
     * @param abstractEnums
     *            Collection of abstract enum.
     * @return Enum collection.
     */
    public EnumCollection toEnumCollection(final Collection<? extends Enum> abstractEnums);


    /**
     * Determines whether the state owner is leaving a particular state.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param newStateOwner
     *            New state owner.
     * @param oldState
     *            Leaving state.
     * @return Returns <code>true</code> if the state owner is leaving a particular
     *         state.
     */
    public boolean isLeavingState(final StateAware oldStateOwner, final StateAware newStateOwner,
        final Enum oldState);


    /**
     * Determines whether the state owner is entering a particular state.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param newStateOwner
     *            New state owner.
     * @param newState
     *            New state.
     * @return Returns <code>true</code> if the state owner is entering a particular
     *         state.
     */
    public boolean isEnteringState(final StateAware oldStateOwner, final StateAware newStateOwner,
        final Enum newState);


    /**
     * Determines whether the state owner is entering one of the specified states.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param newStateOwner
     *            New state owner.
     * @param states
     *            Collection of entering states. This collection can contain the index of
     *            the enum or the enum themselves. (TODO Confusing, no?)
     * @return Returns true if the state owner is entering one of the specified states.
     */
    public boolean isEnteringState(final StateAware oldStateOwner, final StateAware newStateOwner,
        final Collection<? extends Enum> states);
    
    public boolean isEnteringState(final StateAware oldStateOwner, final StateAware newStateOwner,
            Enum... states);


    /**
     * Determines whether the state owner is transitioning from one particular state to
     * another particular state.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param newStateOwner
     *            New state owner.
     * @param oldState
     *            Leaving state.
     * @param newState
     *            Entering state.
     * @return Returns <code>true</code> if the state owner is transitioning from
     *         <code>oldState</code> to <code>newState</code>.
     */
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final Enum oldState, final Enum newState);


    /**
     * Determines whether the state owner is transitioning from one of the specified
     * states to another particular state.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param newStateOwner
     *            New state owner.
     * @param oldStates
     *            Collection of leaving states. This collection can contain the index of
     *            the enum or the enum themselves. (TODO Confusing, no?)
     * @param newState
     *            Entering state.
     * @return Returns <code>true</code> if the state owner is transitioning from one of
     *         <code>oldStates</code> to <code>newState</code>.
     */
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final Collection<? extends Enum> oldStates, final Enum newState);


    /**
     * Determines whether the state owner is transitioning from a particular state to one
     * of the specified states.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param newStateOwner
     *            New state owner.
     * @param oldState
     *            Leaving state.
     * @param newStates
     *            Collection of entering states. This collection can contain the index of
     *            the enum or the enum themselves. (TODO Confusing, no?)
     * @return Returns <code>true</code> if the state owner is transitioning from
     *         <code>oldState</code> to one of <code>newStates</code>.
     */
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final Enum oldState, final Collection<? extends Enum> newStates);


    /**
     * Determines whether the state owner is transitioning from one of the specified
     * states to one of another group of specified states.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param newStateOwner
     *            New state owner.
     * @param oldStates
     *            Collection of leaving states. This collection can contain the index of
     *            the enum or the enum themselves. (TODO Confusing, no?)
     * @param newStates
     *            Collection of entering states. This collection can contain the index of
     *            the enum or the enum themselves. (TODO Confusing, no?)
     * @return Returns <code>true</code> if the state owner is transitioning from one of
     *         <code>oldStates</code> to one of <code>newStates</code>.
     */
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final Collection<? extends Enum> oldStates, final Collection<? extends Enum> newStates);


    /**
     * Determines if a state owner is in one of the provided states.
     *
     * @param stateOwner
     *            State owner.
     * @param states
     *            Collection of states. This collection can contain the index of the enum
     *            or the enum themselves. (TODO Confusing, no?)
     * @return Returns <code>true</code> if the state owner is in one of the states.
     */
    public boolean isOneOfStates(final StateAware stateOwner, final Collection<? extends Enum> states);
    
    public boolean isOneOfStates(final StateAware stateOwner, Enum... states);


    /**
     * Determines if a state owner is not in one of the provided states. The inverse of
     * {@link #isOneOfStates(StateAware, Collection)}
     *
     * @param stateOwner
     *            State owner.
     * @param states
     *            Collection of states. This collection can contain the index of the enum
     *            or the enum themselves. (TODO Confusing, no?)
     * @return Returns <code>true</code> if the state owner is in one of the states.
     */
    public boolean isNotOneOfStates(final StateAware stateOwner, final Collection<? extends Enum> states);
    
    public boolean isNotOneOfStates(final StateAware stateOwner, Enum... states);


    /**
     * Determines if the provided state is in one of the provided states.
     *
     * @param state
     *            State.
     * @param states
     *            Collection of states. This collection can contain the index of the enum
     *            or the enum themselves. (TODO Confusing, no?)
     * @return Returns <code>true</code> if the provided state is in one of the states.
     */
    public boolean isOneOfStates(final Enum state, final Collection<? extends Enum> states);

    public boolean isOneOfStates(final Enum state, Enum... states);


    /**
     * Determines if the provided state is not one of the provided states. Inverse of
     * {@link #isOneOfStates(Enum, Collection)}.
     *
     * @param state
     *            State.
     * @param states
     *            Collection of states. This collection can contain the index of the enum
     *            or the enum themselves. (TODO Confusing, no?)
     * @return Returns <code>true</code> if the provided state is not one of the states.
     */
    public boolean isNotOneOfStates(final Enum state, final Collection<? extends Enum> states);

    public boolean isNotOneOfStates(final Enum state, Enum... states);


    /**
     * Determines if the state owner is in the provided state.
     *
     * @param stateOwner
     *            State owner.
     * @param state
     *            State.
     * @return Return <code>true</code> if the state owner is in the provided state.
     */
    public boolean stateEquals(final StateAware stateOwner, final Enum state);


    /**
     * Determines if the state of the two state owners are the same.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param newStateOwner
     *            New state owner.
     * @return Returns <code>true</code> if the two state owners have the same state.
     */
    public boolean stateEquals(final StateAware oldStateOwner, final StateAware newStateOwner);
    
    
    public boolean isFinalStateViolation(final FinalStateAware oldStateOwner, final FinalStateAware newStateOwner);


    /**
     * Determines whether the state owner is leaving a particular state. Synonym of
     * {@link #stateEquals(StateAware, int)}.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param oldState
     *            Leaving state.
     * @return Returns <code>true</code> if the state owner is leaving a particular
     *         state.
     * @deprecated Use {@link #stateEquals(StateAware,Enum)} instead.
     */
    @Deprecated
    public boolean isLeavingState(final StateAware oldStateOwner, final int oldState);


    /**
     * Determines whether the state owner is leaving a particular state.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param newStateOwner
     *            New state owner.
     * @param oldState
     *            Leaving state.
     * @return Returns <code>true</code> if the state owner is leaving a particular
     *         state.
     * 
     * @deprecated Use {@link #isLeavingState(StateAware,StateAware,Enum)} instead.
     */
    @Deprecated
    public boolean isLeavingState(final StateAware oldStateOwner, final StateAware newStateOwner,
        final int oldState);


    /**
     * Determines whether the state owner is leaving one of the specified states. Synonym
     * of {@link #isOneOfStates(StateAware, int[])}.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param oldStates
     *            Leaving states.
     * @return Returns <code>true</code> if the state owner is leaving one of the
     *         specified states.
     * @deprecated Use {@link #isOneOfStates(StateAware, Collection)} instead.
     */
    @Deprecated
    public boolean isLeavingState(final StateAware oldStateOwner, final int[] oldStates);


    /**
     * Determines whether the state owner is entering a particular state. Synonym of
     * {@link #stateEquals(StateAware, int)}.
     *
     * @param newStateOwner
     *            New state owner.
     * @param newState
     *            New state.
     * @return Returns <code>true</code> if the state owner is entering a particular
     *         state.
     * @deprecated Call {@link #stateEquals(StateAware,Enum)} instead.
     */
    @Deprecated
    public boolean isEnteringState(final StateAware newStateOwner, final int newState);


    /**
     * Determines whether the state owner is entering a particular state.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param newStateOwner
     *            New state owner.
     * @param newState
     *            New state.
     * @return Returns <code>true</code> if the state owner is entering a particular
     *         state.
     * 
     * @deprecated Use {@link #isEnteringState(StateAware,StateAware,Enum)} instead.
     */
    @Deprecated
    public boolean isEnteringState(final StateAware oldStateOwner, final StateAware newStateOwner,
        final int newState);


    /**
     * Determines whether the state owner is entering one of the specified states. Synonym
     * of {@link #isOneOfStates(StateAware, int[])}.
     *
     * @param newStateOwner
     *            New state owner.
     * @param newStates
     *            Entering states.
     * @return Returns true if the state owner is entering one of the specified states.
     * 
     * @deprecated Use {@link #isOneOfStates(StateAware, Collection)} instead.
     */
    @Deprecated
    public boolean isEnteringState(final StateAware newStateOwner, final int[] newStates);


    /**
     * Determines whether the state owner is entering one of the specified states.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param newStateOwner
     *            New state owner.
     * @param states
     *            Entering states.
     * @return Returns true if the state owner is entering one of the specified states.
     * 
     * @deprecated Use {@link #isEnteringState(StateAware,StateAware,Collection)}
     */
    @Deprecated
    public boolean isEnteringState(final StateAware oldStateOwner, final StateAware newStateOwner,
        final int[] states);


    /**
     * Determines whether the state owner is transitioning from one particular state to
     * another particular state.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param newStateOwner
     *            New state owner.
     * @param oldState
     *            Leaving state.
     * @param newState
     *            Entering state.
     * @return Returns <code>true</code> if the state owner is transitioning from
     *         <code>oldState</code> to <code>newState</code>.
     * 
     * @deprecated Use {@link #isTransition(StateAware,StateAware,Enum,Enum)}
     */
    @Deprecated
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final int oldState, final int newState);


    /**
     * Determines whether the state owner is transitioning from one of the specified
     * states to another particular state.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param newStateOwner
     *            New state owner.
     * @param oldStates
     *            Leaving states.
     * @param newState
     *            Entering state.
     * @return Returns <code>true</code> if the state owner is transitioning from one of
     *         <code>oldStates</code> to <code>newState</code>.
     * 
     * @deprecated Use {@link #isTransition(StateAware,StateAware,Collection,Enum)}
     */
    @Deprecated
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final int[] oldStates, final int newState);


    /**
     * Determines whether the state owner is transitioning from a particular state to one
     * of the specified states.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param newStateOwner
     *            New state owner.
     * @param oldState
     *            Leaving state.
     * @param newStates
     *            Entering states.
     * @return Returns <code>true</code> if the state owner is transitioning from
     *         <code>oldState</code> to one of <code>newStates</code>.
     * 
     * @deprecated Use {@link #isTransition(StateAware,StateAware,Enum,Collection)}
     */
    @Deprecated
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final int oldState, final int[] newStates);


    /**
     * Determines whether the state owner is transitioning from a particular state to one
     * of the specified states.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param newStateOwner
     *            New state owner.
     * @param oldState
     *            Leaving state.
     * @param newStates
     *            Collection of entering states. This collection can contain the index of
     *            the enum or the enum themselves. (TODO Confusing, no?)
     * @return Returns <code>true</code> if the state owner is transitioning from
     *         <code>oldState</code> to one of <code>newStates</code>.
     * 
     * @deprecated Use {@link #isTransition(StateAware,StateAware,Enum,Collection)}
     */
    @Deprecated
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final int oldState, final Collection<? extends Enum> newStates);


    /**
     * Determines whether the state owner is transitioning from one of the specified
     * states to one of another group of specified states.
     *
     * @param oldStateOwner
     *            Previous state owner.
     * @param newStateOwner
     *            New state owner.
     * @param oldStates
     *            Leaving states.
     * @param newStates
     *            Entering states.
     * @return Returns <code>true</code> if the state owner is transitioning from one of
     *         <code>oldStates</code> to one of <code>newStates</code>.
     * 
     * @deprecated Use {@link #isTransition(StateAware,StateAware,Collection,Collection)}
     */
    @Deprecated
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final int[] oldStates, final int[] newStates);


    /**
     * Determines if a state owner is in one of the provided states.
     *
     * @param stateOwner
     *            State owner.
     * @param states
     *            States.
     * @return Returns <code>true</code> if the state owner is in one of the states.
     * 
     * @deprecated Use {@link #isOneOfStates(StateAware,Collection)}
     */
    @Deprecated
    public boolean isOneOfStates(final StateAware stateOwner, final int[] states);


    /**
     * Determines if the state owner is not in one of the provided states. The inverse of
     * {@link #isOneOfStates(StateAware, int[])}.
     *
     * @param stateOwner
     *            State owner.
     * @param states
     *            States.
     * @return Returns <code>true</code> if the state owner is not in one of the states.
     * 
     * @deprecated Use {@link #isNotOneOfStates(StateAware,Collection)}
     */
    @Deprecated
    public boolean isNotOneOfStates(final StateAware stateOwner, final int[] states);


    /**
     * Determines if the provided state is in the group of provided states.
     *
     * @param state
     *            State.
     * @param states
     *            Group of states.
     * @return Returns <code>true</code> if the provided state is in the provided group
     *         of states.
     * 
     * @deprecated Use {@link #isOneOfStates(Enum,Collection)}
     */
    @Deprecated
    public boolean isOneOfStates(final int state, final int[] states);


    /**
     * Determines if the provided state is not in the group of provided states. The
     * inverse of {@link #isOneOfStates(Enum, Collection)}.
     *
     * @param state
     *            State.
     * @param states
     *            Group of states.
     * @return Returns <code>true</code> if the provided state is not in the provided
     *         group of states.
     * 
     * @deprecated Use {@link #isNotOneOfStates(Enum,Collection)}
     */
    @Deprecated
    public boolean isNotOneOfStates(final int state, final int[] states);


    /**
     * Determines if the provided state is in one of the provided states.
     *
     * @param state
     *            State.
     * @param states
     *            Collection of states. This collection can contain the index of the enum
     *            or the enum themselves. (TODO Confusing, no?)
     * @return Returns <code>true</code> if the provided state is in one of the states.
     * 
     * @deprecated Use {@link #isOneOfStates(Enum,Collection)}
     */
    @Deprecated
    public boolean isOneOfStates(final int state, final Collection states);


    /**
     * Determines if the provided state is not one of the provided states. Inverse of
     * {@link #isOneOfStates(int, Collection)}.
     *
     * @param state
     *            State.
     * @param states
     *            Collection of states. This collection can contain the index of the enum
     *            or the enum themselves. (TODO Confusing, no?)
     * @return Returns <code>true</code> if the provided state is not one of the states.
     * 
     * @deprecated Use {@link #isNotOneOfStates(Enum,Collection)}
     */
    @Deprecated
    public boolean isNotOneOfStates(final int state, final Collection states);


    /**
     * Determines if the state owner is in the provided state.
     *
     * @param stateOwner
     *            State owner.
     * @param state
     *            State.
     * @return Return <code>true</code> if the state owner is in the provided state.
     * 
     * @deprecated Use {@link #stateEquals(StateAware,Enum)}
     */
    @Deprecated
    public boolean stateEquals(final StateAware stateOwner, final int state);

}
