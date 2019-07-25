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
package com.redknee.app.crm.transaction;

import java.io.Serializable;
import java.util.Comparator;

import com.redknee.app.crm.bean.Transaction;

/**
 * @author victor.stratan@redknee.com
 *
 * This class is used to sort Transactions based on the Receive Date property of
 * the Transaction. Order can be specified through descending property. 
 */
public class TransactionReceiveDateComparator implements Comparator, Serializable
{
    private static final long serialVersionUID = 1L;

    public TransactionReceiveDateComparator(boolean descending)
    {
        descending_ = descending;
    }

    public int compare(Object arg0, Object arg1) {
        Transaction a = (Transaction) arg0;
        Transaction b = (Transaction) arg1;
        
        if (isDescending())
        {
            return b.getReceiveDate().compareTo(a.getReceiveDate());
        }
        else
        {
            return a.getReceiveDate().compareTo(b.getReceiveDate());
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if(!(obj instanceof TransactionReceiveDateComparator))
        {
            return false;
        }
        
        return isDescending() == ((TransactionReceiveDateComparator)obj).isDescending();
    }

    @Override
    public int hashCode()
    {
        return (descending_ ? 1 : 0);
    }

    public boolean isDescending()
    {
        return descending_;
    }

    public void setDescending_(boolean descending)
    {
        this.descending_ = descending;
    }

    protected boolean descending_ = false;
}
