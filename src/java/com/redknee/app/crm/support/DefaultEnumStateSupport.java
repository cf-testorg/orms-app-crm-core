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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xhome.xenum.EnumCollection;

import com.redknee.app.crm.state.EnumState;
import com.redknee.app.crm.state.FinalStateAware;
import com.redknee.app.crm.state.StateAware;


/**
 * This is basically a copy of SubscriberStateSupport, since Subscriber Provisioning home
 * needs to be designed and re-architected, so just copy for now!
 *
 * @author joe.chen@redknee.com
 */
public class DefaultEnumStateSupport implements EnumStateSupport
{
    protected static EnumStateSupport instance_ = null;
    public static EnumStateSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultEnumStateSupport();
        }
        return instance_;
    }

    protected DefaultEnumStateSupport()
    {
    }
    
    /**
     * {@inheritDoc}
     */
    public Collection<? extends Enum> toAbstractEnumCollection(final EnumCollection ec, final int[] enumIndices)
    {
        final List<Enum> allStates = new ArrayList<Enum>();
        if (enumIndices != null && enumIndices.length > 0)
        {
            for (final int element : enumIndices)
            {
                allStates.add(ec.get((short) element));
            }
        }
        return allStates;
    }


    /**
     * {@inheritDoc}
     */
    public EnumCollection toEnumCollection(final Collection<? extends Enum> abstractEnums)
    {
        final List<Enum> list = new ArrayList<Enum>(abstractEnums);
        Collections.sort(list);
        final Enum[] enums = new Enum[abstractEnums.size()];
        list.toArray(enums);
        return new EnumCollection(enums);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isLeavingState(final StateAware oldStateOwner, final StateAware newStateOwner,
        final Enum oldState)
    {
        return !stateEquals(oldStateOwner, newStateOwner) && stateEquals(oldStateOwner, oldState);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isEnteringState(final StateAware oldStateOwner, final StateAware newStateOwner,
        final Enum newState)
    {
        return !stateEquals(oldStateOwner, newStateOwner) && stateEquals(newStateOwner, newState);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isEnteringState(final StateAware oldStateOwner, final StateAware newStateOwner,
        final Collection<? extends Enum> states)
    {
        return !stateEquals(oldStateOwner, newStateOwner) && isOneOfStates(newStateOwner, states);
    }
    
    public boolean isEnteringState(final StateAware oldStateOwner, final StateAware newStateOwner,
            Enum... states)
    {
        return isEnteringState(oldStateOwner, newStateOwner, Arrays.asList(states));
    }


    /**
     * {@inheritDoc}
     */
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final Enum oldState, final Enum newState)
    {
        return !stateEquals(oldStateOwner, newStateOwner) && stateEquals(oldStateOwner, oldState)
            && stateEquals(newStateOwner, newState);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final Collection<? extends Enum> oldStates, final Enum newState)
    {
        return !stateEquals(oldStateOwner, newStateOwner) && isOneOfStates(oldStateOwner, oldStates)
            && stateEquals(newStateOwner, newState);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final Enum oldState, final Collection<? extends Enum> newStates)
    {
        return !stateEquals(oldStateOwner, newStateOwner) && stateEquals(oldStateOwner, oldState)
            && isOneOfStates(newStateOwner, newStates);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final Collection<? extends Enum> oldStates, final Collection<? extends Enum> newStates)
    {
        return !stateEquals(oldStateOwner, newStateOwner) && isOneOfStates(oldStateOwner, oldStates)
            && isOneOfStates(newStateOwner, newStates);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isOneOfStates(final StateAware stateOwner, final Collection<? extends Enum> states)
    {
        boolean inStates = false;
        if (stateOwner != null)
        {
            inStates = isOneOfStates(stateOwner.getAbstractState(), states);
        }
        return inStates;
    }

    
    /**
     * {@inheritDoc}
     */
    public boolean isOneOfStates(final StateAware stateOwner, Enum... states)
    {
        return isOneOfStates(stateOwner, Arrays.asList(states));
    }


    /**
     * {@inheritDoc}
     */
    public boolean isNotOneOfStates(final StateAware stateOwner, final Collection<? extends Enum> states)
    {
        return !isOneOfStates(stateOwner, states);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isNotOneOfStates(final StateAware stateOwner, Enum... states)
    {
        return !isOneOfStates(stateOwner, Arrays.asList(states));
    }


    /**
     * {@inheritDoc}
     */
    public boolean isOneOfStates(final Enum state, final Collection<? extends Enum> states)
    {
        boolean ret = false;
        if(states != null && states.size() > 0)
        {
            ret = states.contains(state);
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isOneOfStates(final Enum state, Enum... states)
    {
        return isOneOfStates(state, Arrays.asList(states));
    }


    /**
     * {@inheritDoc}
     */
    public boolean isNotOneOfStates(final Enum state, final Collection<? extends Enum> states)
    {
        return !isOneOfStates(state, states);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isNotOneOfStates(final Enum state, Enum... states)
    {
        return isNotOneOfStates(state, Arrays.asList(states));
    }


    /**
     * {@inheritDoc}
     */
    public boolean stateEquals(final StateAware stateOwner, final Enum state)
    {
        return isOneOfStates(stateOwner, state);
    }


    /**
     * {@inheritDoc}
     */
    public boolean stateEquals(final StateAware oldStateOwner, final StateAware newStateOwner)
    {
        boolean same = false;
        if (oldStateOwner != null && newStateOwner != null)
        {
            same = oldStateOwner.getAbstractState().getIndex() == newStateOwner.getAbstractState().getIndex();
        }
        else if (oldStateOwner == null && newStateOwner == null)
        {
            same = true;
        }
        return same;
    }
    

    /**
     * {@inheritDoc}
     */
    public boolean isFinalStateViolation(final FinalStateAware oldStateOwner, final FinalStateAware newStateOwner)
    {
        return oldStateOwner.isInFinalState() && !newStateOwner.isInFinalState();
    }


    /**
     * {@inheritDoc}
     */
    protected EnumState findStateInstance(final Enum type, final Map states)
    {
        return (EnumState) states.get(type);
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isLeavingState(final StateAware oldStateOwner, final int oldState)
    {
        return stateEquals(oldStateOwner, oldState);
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isLeavingState(final StateAware oldStateOwner, final StateAware newStateOwner,
        final int oldState)
    {
        return !stateEquals(oldStateOwner, newStateOwner) && stateEquals(oldStateOwner, oldState);
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isLeavingState(final StateAware oldStateOwner, final int[] oldStates)
    {
        return isOneOfStates(oldStateOwner, oldStates);
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isEnteringState(final StateAware newStateOwner, final int newState)
    {
        return stateEquals(newStateOwner, newState);
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isEnteringState(final StateAware oldStateOwner, final StateAware newStateOwner,
        final int newState)
    {
        return !stateEquals(oldStateOwner, newStateOwner) && stateEquals(newStateOwner, newState);
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isEnteringState(final StateAware newStateOwner, final int[] newStates)
    {
        return isOneOfStates(newStateOwner, newStates);
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isEnteringState(final StateAware oldStateOwner, final StateAware newStateOwner,
        final int[] states)
    {
        return !stateEquals(oldStateOwner, newStateOwner) && isOneOfStates(newStateOwner, states);
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final int oldState, final int newState)
    {
        return !stateEquals(oldStateOwner, newStateOwner) && stateEquals(oldStateOwner, oldState)
            && stateEquals(newStateOwner, newState);
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final int[] oldStates, final int newState)
    {
        return !stateEquals(oldStateOwner, newStateOwner) && isOneOfStates(oldStateOwner, oldStates)
            && stateEquals(newStateOwner, newState);
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final int oldState, final int[] newStates)
    {
        return !stateEquals(oldStateOwner, newStateOwner) && stateEquals(oldStateOwner, oldState)
            && isOneOfStates(newStateOwner, newStates);
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final int oldState, final Collection<? extends Enum> newStates)
    {
        return !stateEquals(oldStateOwner, newStateOwner) && stateEquals(oldStateOwner, oldState)
            && isOneOfStates(newStateOwner, newStates);
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isTransition(final StateAware oldStateOwner, final StateAware newStateOwner,
        final int[] oldStates, final int[] newStates)
    {
        return !stateEquals(oldStateOwner, newStateOwner) && isOneOfStates(oldStateOwner, oldStates)
            && isOneOfStates(newStateOwner, newStates);
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isOneOfStates(final StateAware stateOwner, final int[] states)
    {
        boolean inStates = false;
        if (stateOwner != null)
        {
            inStates = isOneOfStates(stateOwner.getAbstractState().getIndex(), states);
        }
        return inStates;
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isNotOneOfStates(final StateAware stateOwner, final int[] states)
    {
        return !isOneOfStates(stateOwner, states);
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isOneOfStates(final int state, final int[] states)
    {
        boolean inStates = false;

        if( states != null && states.length > 0 )
        {
            for (final int element : states)
            {
                if (state == element)
                {
                    inStates = true;
                    break;
                }
            }   
        }
        
        return inStates;
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isNotOneOfStates(final int state, final int[] states)
    {
        return !isOneOfStates(state, states);
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isOneOfStates(final int state, final Collection states)
    {
        boolean inStates = false;

        final Iterator it = states.iterator();
        while (it.hasNext())
        {
            final Object obj = it.next();
            if (obj instanceof Enum)
            {
                final Enum en = (Enum) obj;
                if (state == en.getIndex())
                {
                    inStates = true;
                    break;
                }
            }
            else if (obj instanceof Number)
            {
                final Number en = (Number) obj;
                if (state == en.intValue())
                {
                    inStates = true;
                    break;
                }
            }
        }
        return inStates;
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean isNotOneOfStates(final int state, final Collection states)
    {
        return !isOneOfStates(state, states);
    }


    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean stateEquals(final StateAware stateOwner, final int state)
    {
        return stateOwner != null && stateOwner.getAbstractState().getIndex() == state;
    }

}
