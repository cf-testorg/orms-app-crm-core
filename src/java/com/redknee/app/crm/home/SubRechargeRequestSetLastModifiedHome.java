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

package com.redknee.app.crm.home;


import java.util.Date;

import com.redknee.app.crm.bean.SubscriberRechargeRequest;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;

/**
 * 
 * Sets ID and identifier for this SubscriberRechargeRequest using an
 * IdentifierSequence.
 * 
 * @author bdhavalshankh
 * @since 9.9
 *
 */
public class SubRechargeRequestSetLastModifiedHome extends HomeProxy 
{
    private static final long startValue = 100000;
    
    public SubRechargeRequestSetLastModifiedHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }

    @Override
    public Object store(Context ctx, Object obj) throws HomeException,
            HomeInternalException 
    {
        SubscriberRechargeRequest req = (SubscriberRechargeRequest) obj;
        req.setLastModified(new Date());
        
        return super.store(ctx, req);
    }
}
