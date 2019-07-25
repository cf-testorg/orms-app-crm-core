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
package com.redknee.app.crm.support;

import com.redknee.framework.xhome.context.Context;


/**
 * This class should implement static versions of all {@link com.redknee.app.crm.support.PaymentPlanSupport} methods.
 *
 * The only change that should be made to method signatures in the interface is
 * to add the context to facilitate implementation class retrieval. 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public final class PaymentPlanSupportHelper extends SupportHelper
{
    private PaymentPlanSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    @Deprecated
    public static PaymentPlanSupport get()
    {
        return get(PaymentPlanSupport.class, DefaultPaymentPlanSupport.instance());
    }
    
    public static PaymentPlanSupport get(Context ctx)
    {
        PaymentPlanSupport instance = get(ctx, PaymentPlanSupport.class, DefaultPaymentPlanSupport.instance());
        return instance;
    }
    
    public static PaymentPlanSupport set(Context ctx, PaymentPlanSupport instance)
    {
        return register(ctx, PaymentPlanSupport.class, instance);
    }
}
