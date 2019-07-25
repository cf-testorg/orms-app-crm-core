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

import com.redknee.app.crm.bean.SubscriberTypeEnum;
import com.redknee.app.crm.bean.Transaction;
import com.redknee.app.crm.log.CoreERLogger;
import com.redknee.app.crm.tps.pipe.TPSPipeConstant;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;

/**
 * @author amedina
 * 
 * Logs ER everytime a subscriber level transaction is successfully 
 * written to the database. This includes all manual and recurring service 
 * charges, debits/credits, anything that makes it into the subscriber transaction table.
 */
public class CoreTransactionERLogHome extends HomeProxy 
{

    public CoreTransactionERLogHome(
    		Context ctx,
            final Home delegate)
            throws HomeException
    {
            super(delegate);
    }

    @Override
    public Object create(Context ctx, Object obj)
    throws HomeException
	{
		Transaction loggedTxn = (Transaction) super.create(ctx,obj);

        if ( loggedTxn.getSubscriberType() == SubscriberTypeEnum.PREPAID )
        {
            CoreERLogger.createTransactionEr(ctx, loggedTxn, loggedTxn.getAmount(), Long.valueOf(loggedTxn.getBalance()), TPSPipeConstant.RESULT_CODE_SUCCESS);
        }
        else
        {
            CoreERLogger.createTransactionEr(ctx, loggedTxn, loggedTxn.getAmount(), null, TPSPipeConstant.RESULT_CODE_SUCCESS);             
        }

        return loggedTxn;
	}

}
