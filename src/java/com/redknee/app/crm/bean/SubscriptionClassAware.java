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
package com.redknee.app.crm.bean;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.account.SubscriptionClass;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public interface SubscriptionClassAware
{
    public long getSubscriptionClass();
    
    public void setSubscriptionClass(long classId);

    /**
     * Returns the SubscriptionClass bean for this object.
     * 
     * @param ctx IN The operating context.
     * @return The SubscriptionType if one exists for the set identifier; null otherwise.
     */
    public SubscriptionClass getSubscriptionClass(Context ctx);
}
