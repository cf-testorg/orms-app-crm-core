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
 * This class should implement static versions of all {@link com.redknee.app.crm.support.DeploymentTypeSupport} methods.
 *
 * The only change that should be made to method signatures in the interface is
 * to add the context to facilitate implementation class retrieval. 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class DeploymentTypeSupportHelper extends SupportHelper
{
    private DeploymentTypeSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static DeploymentTypeSupport get()
    {
        return get(DeploymentTypeSupport.class, DefaultDeploymentTypeSupport.instance());
    }
    
    public static DeploymentTypeSupport get(Context ctx)
    {
        DeploymentTypeSupport instance = get(ctx, DeploymentTypeSupport.class, DefaultDeploymentTypeSupport.instance());
        return instance;
    }
    
    public static DeploymentTypeSupport set(Context ctx, DeploymentTypeSupport instance)
    {
        return register(ctx, DeploymentTypeSupport.class, instance);
    }
} // class
