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

import java.util.HashMap;
import java.util.Map;

import com.redknee.framework.xhome.beans.FacetFactory;
import com.redknee.framework.xhome.beans.FacetMgr;
import com.redknee.framework.xhome.beans.FacetMgrUtil;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextFactory;



/**
 * This facet factory returns instances of facets from a variety
 * of possible sources (in order of priority):
 * 
 * 1. An explicitly provided instance
 * 2. A {@link ContextFactory}
 * 3. By invoking {@link FacetMgrUtil#instantiate(Context, Class)
 * 
 * Note that it will recurse up the type hierarchy if no default is
 * registered for the specific type that is being looked for.  Implemented
 * interfaces will not be recursed over.
 * 
 * Usage Notes:
 * 
 * When registering this factory with the facet manager, the class passed to
 * {@link #DynamicValueDefaultFacetFactory(Class)} should be the same class
 * that is passed as the target class type to the {@link FacetMgr} during
 * registration.
 * 
 * For convenience, consider extending this class to initialize the template
 * parameter explicitly for the type of facet being managed.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class DynamicValueFacetFactory<T> implements FacetFactory
{
    protected DynamicValueFacetFactory(Class<T> type)
    {
        type_ = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getClass(Context ctx, Class beanType, Class targetType)
    {
        if (!type_.isAssignableFrom(targetType))
        {
            return null;
        }
        
        Class result = null;

        for (Class currentType = beanType; result == null && currentType != null; currentType = currentType.getSuperclass())
        {
            result = getClass(ctx, currentType);
        }
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Object getInstanceOf(Context ctx, Class beanType, Class targetType)
    {
        if (!type_.isAssignableFrom(targetType))
        {
            return null;
        }
        
        Object result = null;

        for (Class currentType = beanType; result == null && currentType != null; currentType = currentType.getSuperclass())
        {
            result = getInstance(ctx, currentType);
        }
        
        return result;
    }
    
    public synchronized void unregister(Class beanType)
    {
        register(beanType, (T) null);
        register(beanType, (Class<T>) null, null);
    }
    
    public synchronized void register(Class beanType, Class<? extends T> targetType)
    {
        register(beanType, targetType, ctxKeyMap_.get(beanType));
    }
    
    public synchronized void register(Class beanType, Class<? extends T> targetType, Object ctxKey)
    {
        if (targetType == null)
        {
            typeMap_.remove(beanType);
        }
        else
        {
            typeMap_.put(beanType, targetType);
        }
        
        if (ctxKey == null)
        {
            ctxKeyMap_.remove(beanType);
        }
        else
        {
            ctxKeyMap_.put(beanType, ctxKey);
        }
    }
    
    public synchronized void register(Class beanType, T targetInstance)
    {
        if (targetInstance == null)
        {
            T oldInstance = instanceMap_.remove(beanType);
            
            Class<? extends T> oldType = typeMap_.get(beanType);
            if (oldType != null && oldType.isInstance(oldInstance))
            {
                register(beanType, (Class<T>) null);
            }
        }
        else
        {
            instanceMap_.put(beanType, targetInstance);
            register(beanType, (Class<T>) targetInstance.getClass());
        }
    }

    protected Class<? extends T> getClass(Context ctx, Class beanType)
    {
        Class<? extends T> type = typeMap_.get(beanType);
        
        if (type == null)
        {
            // getInstance() will update type map if an instance is available.
            T instance = getInstance(ctx, beanType);
            if (instance != null)
            {
                type = (Class<T>) instance.getClass();
            }
        }
        
        return type;
    }

    protected T getInstance(Context ctx, Class beanType)
    {
        Class<? extends T> registeredType = typeMap_.get(beanType);
        
        T instance = instanceMap_.get(beanType);
        
        if (instance == null)
        {
            Object key = ctxKeyMap_.get(beanType);
            if (key != null)
            {
                Object obj = ctx.get(key);
                if (type_.isInstance(obj))
                {
                    instance = (T) obj;
                }
            }
        }
        
        if (instance == null)
        {
            instance = (T) FacetMgrUtil.instantiate(ctx, registeredType);
        }
        else if (registeredType == null)
        {
            registeredType = (Class<T>) instance.getClass();
            register(beanType, registeredType);
        }
        
        return instance;
    }

    protected Class<T> type_;
    
    protected Map<Class, Class<? extends T>> typeMap_ = new HashMap<Class, Class<? extends T>>();
    protected Map<Class, T> instanceMap_ = new HashMap<Class, T>();
    protected Map<Class, Object> ctxKeyMap_ = new HashMap<Class, Object>();
}
