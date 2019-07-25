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
 * This class eases installation/retrieval of the AdjustmentTypeSupport interface 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public final class AdjustmentTypeSupportHelper extends SupportHelper
{
    private AdjustmentTypeSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static AdjustmentTypeSupport get()
    {
        return get(AdjustmentTypeSupport.class, DefaultAdjustmentTypeSupport.instance());
    }
    
    public static AdjustmentTypeSupport get(Context ctx)
    {
        AdjustmentTypeSupport instance = get(ctx, AdjustmentTypeSupport.class, DefaultAdjustmentTypeSupport.instance());
        return instance;
    }
    
    public static AdjustmentTypeSupport set(Context ctx, AdjustmentTypeSupport instance)
    {
        return register(ctx, AdjustmentTypeSupport.class, instance);
    }
} // class
