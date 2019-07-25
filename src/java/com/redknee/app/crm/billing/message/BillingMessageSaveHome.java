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
package com.redknee.app.crm.billing.message;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.app.crm.contract.SubscriptionContractTerm;

/**
 * Trigger the save of the Billing Message from the BillingMessageAware Bean.
 * First version handles only SpidBillingMessage.
 *
 * @author victor.stratan@redknee.com
 */
public class BillingMessageSaveHome extends HomeProxy
{
    /**
     * For serialization.
     */
    private static final long serialVersionUID = 1L;

    public BillingMessageSaveHome(final Home delegate)
    {
        super(delegate);
    }

    @Override
    public Object create(final Context ctx, final Object obj) throws HomeException
    {
        if(obj instanceof BillingMessageAware)
        {
            final BillingMessageAware billingMessageAware = (BillingMessageAware) obj;
            billingMessageAware.saveBillingMessages(ctx);
            return super.create(ctx, obj);
        }
        else
        {
            return super.create(ctx, obj);
        }
        
    }

    @Override
    public Object store(final Context ctx, final Object obj) throws HomeException
    {
        if(obj instanceof BillingMessageAware)
        {
            final BillingMessageAware billingMessageAware = (BillingMessageAware) obj;
            billingMessageAware.saveBillingMessages(ctx);
            return super.store(ctx, obj);
        }
        else
        {
            return super.store(ctx, obj);
        }
    }

    @Override
    public void remove(final Context ctx, final Object obj) throws HomeException
    {
        super.remove(ctx, obj);
    }
}
