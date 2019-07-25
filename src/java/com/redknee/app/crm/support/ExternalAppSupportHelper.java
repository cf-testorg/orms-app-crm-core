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
 * This class should implement static versions of all {@link com.redknee.app.crm.support.ExternalAppSupportHelper} methods.
 * 
 * The only change that should be made to method signatures in the interface is
 * to add the context to facilitate implementation class retrieval. 
 *
 * @author Marcio Marques
 * @since 9.1.3
 */
public class ExternalAppSupportHelper extends SupportHelper
{
    private ExternalAppSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static ExternalAppSupport get()
    {
        return get(ExternalAppSupport.class, DefaultExternalAppSupport.instance());
    }
    
    public static ExternalAppSupport get(Context ctx)
    {
        ExternalAppSupport instance = get(ctx, ExternalAppSupport.class, DefaultExternalAppSupport.instance());
        return instance;
    }
    
    public static ExternalAppSupport set(Context ctx, ExternalAppSupport instance)
    {
        return register(ctx, ExternalAppSupport.class, instance);
    }
}
