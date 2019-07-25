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
 * This class is used to sort Transactions based on the transDate property of
 * the Transaction. Order can be specified through descending property. 
 */
public class TransactionDateComparator implements Comparator, Serializable
{

    public TransactionDateComparator(boolean descending)
    {
        descending_ = descending;
    }

    public int compare(Object arg0, Object arg1) {
        Transaction a = (Transaction) arg0;
        Transaction b = (Transaction) arg1;
        
        if (isDescending())
        {
            return b.getTransDate().compareTo(a.getTransDate());
        }
        else
        {
            return a.getTransDate().compareTo(b.getTransDate());
        }
    }

    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if(!(obj instanceof TransactionDateComparator))
        {
            return false;
        }
        
        return isDescending() == ((TransactionDateComparator)obj).isDescending();
    }

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
