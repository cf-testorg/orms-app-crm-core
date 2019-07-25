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
package com.redknee.app.crm.bean.core;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.SubscriptionTypeAware;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class MsisdnMgmtHistory extends com.redknee.app.crm.numbermgn.MsisdnMgmtHistory implements SubscriptionTypeAware
{
    /**
     * {@inheritDoc}
     */
    public SubscriptionType getSubscriptionType(Context ctx)
    {
        return SubscriptionType.getSubscriptionType(ctx, getSubscriptionType());
    }
}
