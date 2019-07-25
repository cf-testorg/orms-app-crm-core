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

import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;


/**
 * Interface that should be implemented by beans that contain some lazy-loaded properties.  It
 * can be used by external code to perform bulk lazy-loading if required.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public interface LazyLoadBean
{
    /**
     * Lazy-load a specific property.  This method should be called to ensure that the given property
     * value is loaded.  This might be useful prior to freezing in some cases.  See description of
     * lazyLoadAllProperties() for some general examples.
     * 
     * @param ctx Operating Context
     * @return True on success, False on failure.  Logs should be output for all failures.
     */
    public boolean lazyLoad(Context ctx, PropertyInfo property);
    
    /**
     * Lazy-load all lazy-loadable data in the bean.  This method should be called before freezing
     * a bean IF AND ONLY IF this information is required by whoever is using the bean.
     * 
     * Two examples:
     * 
     * 1. In the move logic, a frozen copy of the original bean is stored in the request.  This
     *    copy is intended to represent the entire "pre-state" of the bean before the move started.
     *    In this case, it is desireable to have all of this information available in the frozen bean.
     * 
     * 2. Transient homes (e.g. caches) store frozen beans.  In this case, we DO NOT want to execute
     *    the lazy-load before freezing because then we will effectively end up storing stale data in
     *    the transient home.
     * 
     * @param ctx Operating Context
     * @return True on success, False on failure.  Logs should be output for all failures.
     */
    public boolean lazyLoadAllProperties(Context ctx);
}
