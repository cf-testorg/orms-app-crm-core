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
package com.redknee.app.crm.state;

import com.redknee.app.crm.bean.SubscriberStateEnum;

/**
 * @author arturo.medina@redknee.com
 * 
 * Ported from CRM 7_4. Part of the Subscriber State Service Update provisioning module.
 * @since CRM 8.2
 * @author angie.li@redknee.com
 */
public class StateChange
{

    /**
     * @return the stateFrom_
     */
    public SubscriberStateEnum from()
    {
        return stateFrom_;
    }
    
    /**
     * @param stateFrom the stateFrom_ to set
     */
    public void setFrom(SubscriberStateEnum stateFrom)
    {
        stateFrom_ = stateFrom;
    }
    
    /**
     * @return the stateTo_
     */
    public SubscriberStateEnum to()
    {
        return stateTo_;
    }
    
    /**
     * @param stateTo the stateTo to set
     */
    public void setTo(SubscriberStateEnum stateTo)
    {
        stateTo_ = stateTo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((stateFrom_ == null) ? 0 : stateFrom_.getDescription().hashCode());
        result = prime * result
                + ((stateTo_ == null) ? 0 : stateTo_.getDescription().hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StateChange other = (StateChange) obj;
        if (stateFrom_ == null)
        {
            if (other.stateFrom_ != null)
                return false;
        }
        else if (!stateFrom_.equals(other.stateFrom_))
            return false;
        if (stateTo_ == null)
        {
            if (other.stateTo_ != null)
                return false;
        }
        else if (!stateTo_.equals(other.stateTo_))
            return false;
        return true;
    }

    private SubscriberStateEnum stateFrom_;
    private SubscriberStateEnum stateTo_;
}
