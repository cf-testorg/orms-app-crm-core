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
package com.redknee.app.crm.factory;

import com.redknee.framework.xhome.beans.FacetFactory;
import com.redknee.framework.xhome.beans.FacetMgr;
import com.redknee.framework.xhome.context.Context;


/**
 * This facet factory retrieves instances of the target type from the
 * context by using a context key.  This is useful when specific instances
 * of facets need to by dynamically retrieved to handle specific types of
 * beans.
 * 
 * If static instances of facets are sufficient, then don't use this class.
 * Instead, register that instance directly using the following method:
 * 
 * {@link FacetMgr#register(Context, Class, Class, Object)}
 * 
 * Usage Notes:
 * 
 * When registering this factory with the facet manager, the classes passed to
 * {@link #ContextRedirectingFacetFactory(Context, Class, Class, Object)}
 * should be the same classes that are passed as the bean class type and target
 * class type to the {@link FacetMgr} during registration.
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class ContextRedirectingFacetFactory implements FacetFactory
{
    public ContextRedirectingFacetFactory(Context ctx, Class beanType, Class targetType, Object ctxKey)
    {
        delegate_ = new DynamicValueFacetFactory(targetType);
        delegate_.register(beanType, null, ctxKey);
    }


    /**
     * {@inheritDoc}
     */
    public Class getClass(Context ctx, Class beanType, Class targetType)
    {
        Object instance = getInstanceOf(ctx, beanType, targetType);
        if (instance != null)
        {
            return instance.getClass();
        }
        return null;
    }


    /**
     * {@inheritDoc}
     */
    public Object getInstanceOf(Context ctx, Class beanType, Class targetType)
    {
        Object instance = delegate_.getInstanceOf(ctx, beanType, targetType);
        return instance;
    }
    
    private DynamicValueFacetFactory delegate_;
}
