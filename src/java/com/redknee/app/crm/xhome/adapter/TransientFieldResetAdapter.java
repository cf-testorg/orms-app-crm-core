/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily 
 * available. Additionally, source code is, by its very nature, confidential 
 * information and inextricably contains trade secrets and other information 
 * proprietary, valuable and sensitive to Redknee, no unauthorised use, 
 * disclosure, manipulation or otherwise is permitted, and may only be used 
 * in accordance with the terms of the licence agreement entered into with 
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.xhome.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.CloneAdapter;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.xhome.beans.xi.NonModelPropertyInfo;


/**
 * This adapter resets transient properties (specific ones or all) for a bean
 * to their default values using reflection.  Reflection is used instead of
 * calling the property's setter to avoid issues where default values are
 * rejected by IllegalArgumentExceptions.
 * 
 * This adapter is useful used in conjunction with the TransientFieldResettingHome
 * outside of transient homes or cached homes, since they have a pitfall of potentially
 * storing stale data that is intented to be storage transient.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class TransientFieldResetAdapter implements Adapter
{
    public TransientFieldResetAdapter(Collection<PropertyInfo> propertiesToReset)
    {
        if (propertiesToReset != null && propertiesToReset.size() > 0)
        {
            propsToReset_  = new ArrayList<PropertyInfo>();
            for (PropertyInfo prop : propertiesToReset)
            {
                if (prop != null && !prop.isPersistent())
                {
                    propsToReset_.add(prop);
                }
            }
        }
    }
    
    public TransientFieldResetAdapter(PropertyInfo... propertiesToReset)
    {
        this(Arrays.asList(propertiesToReset));
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Object adapt(Context ctx, Object obj) throws HomeException
    {
        return CloneAdapter.instance().adapt(ctx, obj);
    }


    /**
     * @{inheritDoc}
     */
    @Override
    public Object unAdapt(Context ctx, Object obj) throws HomeException
    {
        Object bean = obj;
        
        if (bean != null)
        {
            final Collection<PropertyInfo> localPropsToReset;
            if (propsToReset_ == null)
            {
                XInfo xinfo = (XInfo) XBeans.getInstanceOf(ctx, bean, XInfo.class);
                if (xinfo != null)
                {
                    localPropsToReset = Collections.unmodifiableList(xinfo.getProperties(ctx));
                }
                else
                {
                    localPropsToReset = Collections.emptyList();
                }
            }
            else if (propsToReset_.size() > 0)
            {
                localPropsToReset = Collections.unmodifiableList(propsToReset_);
            }
            else
            {
                localPropsToReset = Collections.emptyList();
            }

            if (localPropsToReset.size() > 0)
            {
                bean = CloneAdapter.instance().unAdapt(ctx, bean);
                for (PropertyInfo property : localPropsToReset)
                {
                    if (property != null && !property.isPersistent())
                    {
                        setDefaultValue(ctx, property, bean);
                    }
                }
            }
        }
        
        return bean;
    }

    private void setDefaultValue(Context ctx, PropertyInfo property, Object bean) throws HomeException
    {
        // Set the default value by accessing the field directly to avoid problems setting values that violate assertions
        final Field field = getDeclaredField(bean.getClass(), property);
        if (field != null)
        {
            if (!field.isAccessible())
            {
                field.setAccessible(true);
            }
            try
            {
                field.set(bean, getDefaultValue(ctx, property));
            }
            catch(HomeException e)
            {
                throw e;
            }
            catch(Exception e)
            {
                throw new HomeException("Error setting value of " + property.getXInfo().getName() + "'s " + property.getName() + " field to its default value.", e);
            }
        }
    }

    private Object getDefaultValue(Context ctx, PropertyInfo property) throws HomeException, IllegalArgumentException, IllegalAccessException
    {
        Object defaultValue = null;
     
        if (property.hasDefault())
        {
            defaultValue = property.getDefault();   
        }
        else
        {
            XInfo xinfo = property.getXInfo();

            Object newBean = null;
            try
            {
                newBean = XBeans.instantiate(xinfo.getBeanClass(), ctx);
            }
            catch (Exception e)
            {
                throw new HomeException("Error instantiating bean of type " + xinfo.getBeanClass().getName(), e);
            }
            if (newBean == null)
            {
                throw new HomeException("Failed to instantiate bean of type " + xinfo.getBeanClass().getName());
            }

            defaultValue = getDefaultValueUsingReflection(newBean, property);
        }
        return defaultValue;
    }


    private Object getDefaultValueUsingReflection(Object bean, PropertyInfo property) throws IllegalArgumentException, IllegalAccessException
    {
        Object defaultValue = null;

        // Get the default value by accessing the field directly to avoid lazy-load style getters!
        Field field = getDeclaredField(bean.getClass(), property);
        if (field != null)
        {
            if (!field.isAccessible())
            {
                field.setAccessible(true);
            }
            defaultValue = field.get(bean);
        }

        return defaultValue;
    }

    private Field getDeclaredField(Class cls, PropertyInfo property) throws SecurityException
    {
        final StringBuilder fieldNameBuilder = new StringBuilder(property.getName());
        if (!(property instanceof NonModelPropertyInfo))
        {
            fieldNameBuilder.append("_");
        }

        return getDeclaredField(cls, fieldNameBuilder.toString());
    }
    
    private Field getDeclaredField(Class cls, String fieldName) throws SecurityException
    {
        Field field = null;
        try
        {
            field = cls.getDeclaredField(fieldName);
            if (field == null
                    && cls.getSuperclass() != null
                    && !Object.class.getName().equals(cls.getSuperclass().getName()))
            {
                field = getDeclaredField(cls.getSuperclass(), fieldName);
            }
        }
        catch (NoSuchFieldException e)
        {
            if (cls.getSuperclass() != null
                    && !Object.class.getName().equals(cls.getSuperclass().getName()))
            {
                field = getDeclaredField(cls.getSuperclass(), fieldName);
            }
        }
        return field;
    }

    private List<PropertyInfo> propsToReset_ = null;
}
