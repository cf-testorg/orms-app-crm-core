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
package com.redknee.app.crm.factory.core;

import java.util.Date;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextFactory;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.bean.PayeeEnum;
import com.redknee.app.crm.bean.SubscriberTypeEnum;
import com.redknee.app.crm.bean.core.Transaction;
import com.redknee.app.crm.factory.CoreBeanAdaptingContextFactory;
import com.redknee.app.crm.support.LicensingSupportHelper;


/**
 * Creates default Transaction beans.
 *
 * @author gary.anderson@redknee.com
 */
public
class TransactionFactory
    extends CoreBeanAdaptingContextFactory<com.redknee.app.crm.bean.Transaction, Transaction>
{
    public TransactionFactory(ContextFactory delegate)
    {
        super(com.redknee.app.crm.bean.Transaction.class, 
                Transaction.class, 
                delegate);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(final Context ctx)
    {
        Transaction txn     = (Transaction) super.create(ctx);
        Date        now     = new Date();
        
        if (LicensingSupportHelper.get(ctx).isLicensed(ctx, CoreCrmLicenseConstants.PREPAID_LICENSE_KEY))
        {
            txn.setSubscriberType(SubscriberTypeEnum.PREPAID);
        }
        else if ( LicensingSupportHelper.get(ctx).isLicensed(ctx, CoreCrmLicenseConstants.POSTPAID_LICENSE_KEY))
        {
            txn.setSubscriberType(SubscriberTypeEnum.POSTPAID);
        } 
        
        if (ctx.getBoolean(ACCOUNT_PAYMENT_TRANSACTION, false))
        {
            txn.setPayee(PayeeEnum.Account);
        }

        txn.setReceiveDate(now);
        txn.setTransDate(now);

        return txn;
    }
    
    public static String ACCOUNT_PAYMENT_TRANSACTION = "ACCOUNT PAYMENT TRANSACTION";

} // class
