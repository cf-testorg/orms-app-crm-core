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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.xenum.Enum;


/**
 * Provides the base class required of a SubscriberStateTransition processing object.
 *
 * @author joe.chen@redknee.com
 */
public abstract class EnumStateTransition
{

    /*
     * [Cindy Wong] 2008-03-13: This class is not referenced anywhere -- looks like an
     * unfinished attempt to handle state transition actions inside the EnumState
     * hierarchy instead of in the home decorators.
     */

    /**
     * Returns the enum state support.
     *
     * @return Enum state support.
     */
    public abstract EnumStateAware getEnumStateSupport();


    /**
     * Creates a new AccountStateTransition with specific start state type and end state
     * type.
     *
     * @param start
     *            The type of start state for which to create this handler.
     * @param end
     *            The type of end state for which to create this handler.
     */
    public EnumStateTransition(final Enum start, final Enum end)
    {
        startStateType_ = start;
        endStateType_ = end;
    }


    /**
     * Indicates whether or not state transitioning is permitted when the state owner is
     * switching to a new state.
     *
     * @param context
     *            The operating context.
     * @param current
     *            The current version of the state owner.
     * @param next
     *            The next version of the state owner.
     * @return Returns whether state transitioning is permitted.
     */
    public boolean isProcessingPermitted(final Context context, final StateAware current, final StateAware next)
    {
        boolean permitted = true;

        final boolean isCurrentStatePermitted = current.getAbstractState() == startStateType_;

        final boolean isNextStatePermitted = next.getAbstractState() == endStateType_;

        if (!isCurrentStatePermitted || !isNextStatePermitted)
        {
            permitted = false;
        }

        return permitted;
    }


    /**
     * Performs the work necessary when the account is switching to a new state.
     *
     * @param context
     *            The operating context.
     * @param current
     *            The current version of the account.
     * @param next
     *            The next version of the account.
     * @exception StateChangeException
     *                Thrown if there is a problem switching to the new state.
     */
    public void processing(final Context context, final StateAware current, final StateAware next)
        throws StateChangeException
    {
        if (!isProcessingPermitted(context, current, next))
        {
            throw new StateChangeException("StateAware State Transition do not match.  Expecting: "
                + startStateType_.getDescription() + "->" + endStateType_.getDescription() + "  Processing: "
                + current.getAbstractState().getDescription() + "->" + next.getAbstractState().getDescription());
        }

        // Leaving current state.
        final EnumState currentState = getEnumStateSupport().getState(context, current);
        currentState.leaving(context, current);

        // Special transition cases.
        performTransitionSpecificInstructions(context, current, next);

        // Entering next state.
        final EnumState nextState = getEnumStateSupport().getState(context, next);
        nextState.entering(context, next);
    }


    /**
     * Performs the transition specific work necessary when the account is switching to a
     * new state. Default is doing nothing.
     *
     * @param context
     *            The operating context.
     * @param current
     *            The current version of the account.
     * @param next
     *            The next version of the account.
     * @exception StateChangeException
     *                Thrown if there is a problem in state transitioning.
     */
    protected abstract void performTransitionSpecificInstructions(final Context context, final StateAware current,
        final StateAware next) throws StateChangeException;

    /**
     * Each StateAwareStateTransition will have a fixed start state type and a fixed end
     * state type, which will be initialized when the class is constructed.
     */
    private final Enum startStateType_;

    /**
     * End state.
     */
    private final Enum endStateType_;

} // interface
