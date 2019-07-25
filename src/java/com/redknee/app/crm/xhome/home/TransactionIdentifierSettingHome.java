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
package com.redknee.app.crm.xhome.home;

import com.redknee.app.crm.bean.Transaction;
import com.redknee.app.crm.home.UniqueIdGeneratorHome;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;


/**
 * Sets the identifier of Transactions to a unique identifier pulled from the pool of
 * available identifiers.
 *
 * @author gary.anderson@redknee.com
 */
public class TransactionIdentifierSettingHome extends UniqueIdGeneratorHome
{

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;
    

    public TransactionIdentifierSettingHome(final Context ctx, final Home delegate, final String sequenceName)
    {
        super(ctx, delegate, sequenceName);

    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(final Context ctx, final Object obj) throws HomeException
    {
        final Transaction transaction = (Transaction)obj;

        transaction.setReceiptNum(this.getSequence().nextValue(ctx));

        return super.create(ctx,transaction);
    }
} // class
