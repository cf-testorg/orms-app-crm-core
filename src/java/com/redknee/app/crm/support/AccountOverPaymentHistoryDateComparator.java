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
package com.redknee.app.crm.support;

import java.io.Serializable;
import java.util.Comparator;

import com.redknee.app.crm.bean.AccountOverPaymentHistory;
import com.redknee.app.crm.bean.Transaction;
import com.redknee.app.crm.transaction.TransactionDateComparator;

/**
 * @author alok.sohani@redknee.com
 * @since 9_7_2
 *  
 */

public class AccountOverPaymentHistoryDateComparator implements Comparator, Serializable
{
   public int compare(Object arg0, Object arg1) {
       AccountOverPaymentHistory a = (AccountOverPaymentHistory) arg0;
       AccountOverPaymentHistory b = (AccountOverPaymentHistory) arg1;

        return b.getTimestamp().compareTo(a.getTimestamp());
    }

}
