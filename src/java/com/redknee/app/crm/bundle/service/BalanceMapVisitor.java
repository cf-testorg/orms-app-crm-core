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
package com.redknee.app.crm.bundle.service;

import java.util.List;

import com.redknee.app.crm.bundle.SubscriberBucket;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.Visitor;

/**
 * For every subscriber bucket it will add in the list the bucket balances
 * @author arturo.medina@redknee.com
 *
 */
public class BalanceMapVisitor implements Visitor
{
    
    /**
     * Accepts the list to add such balances
     * @param balances
     */
    public BalanceMapVisitor(List balances)
    {
        setBalanceList(balances);
    }


    /**
     * {@inheritDoc}
     */
    public void visit(Context ctx, Object obj) throws AgentException,
            AbortVisitException
    {
        if (obj instanceof SubscriberBucket)
        {
            SubscriberBucket bucket = (SubscriberBucket) obj;
            getBalanceList().add(bucket.getRegularBal());
        }
    }

    /**
     * Returns the list of balances
     * @return
     */
    private List getBalanceList()
    {
        return balanceList_;
    }


    /**
     * Sets the balance list
     * @param balances
     */
    private void setBalanceList(List balances)
    {
        balanceList_ = balances;
    }

    /**
     * the list to add balances
     */
    private List balanceList_;


    /**
     * 
     */
    private static final long serialVersionUID = -5742686272969446174L;
}
