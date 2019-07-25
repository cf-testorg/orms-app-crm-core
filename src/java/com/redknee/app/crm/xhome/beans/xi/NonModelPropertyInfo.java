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
package com.redknee.app.crm.xhome.beans.xi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.FacetMgr;
import com.redknee.framework.xhome.beans.xi.AbstractPropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.io.XOutput;

/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class NonModelPropertyInfo extends AbstractPropertyInfo
{
    public NonModelPropertyInfo(Context ctx, Class<? extends AbstractBean> clazz, String fieldName)
    {
        this(clazz, fieldName, (XInfo) ((FacetMgr)ctx.get(FacetMgr.class)).getInstanceOf(ctx, clazz, XInfo.class));
    }

    public NonModelPropertyInfo(Class clazz, String fieldName, XInfo xinfo)
    {
        super(fieldName);
        class_ = clazz;
        xinfo_ = xinfo;
        Class superClass = clazz;

        while (superClass!=null && field_==null)
        {
            try
            {
                field_ = superClass.getDeclaredField(fieldName);
            }
            catch (Throwable t)
            {
            }
            superClass = superClass.getSuperclass();
        }
        
        if (field_ == null)
        {
            throw new IllegalArgumentException("Invalid field '" + fieldName + "' for class '" + clazz.getName() + "'.");
        }

        final StringBuilder getterName = new StringBuilder(field_.getName());
        getterName.setCharAt(0, Character.toUpperCase(field_.getName().charAt(0)));
        if (getterName.charAt(getterName.length()-1) == '_')
        {
            getterName.deleteCharAt(getterName.length()-1);
        }

        final StringBuilder setterName = new StringBuilder(getterName.toString());

        final String getter = getterName.insert(0, "get").toString();

        superClass = clazz;

        while (superClass!=null && getter_==null)
        {
            try
            {
                getter_ = superClass.getMethod(getter);
                if (!field_.getType().isAssignableFrom(getter_.getReturnType()))
                {
                    getter_ = null;
                }
            }
            catch (Throwable t)
            {
            }
            superClass = superClass.getSuperclass();
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object get(Object bean)
    {            
        if (getter_ != null)
        {
            if (!getter_.isAccessible())
            {
                getter_.setAccessible(true);
            }
            try
            {
                return getter_.invoke(bean);
            }
            catch (Throwable t)
            {
                // NOP - fallback to field access
            }
        }

        if (!field_.isAccessible())
        {
            field_.setAccessible(true);
        }
        try
        {
            return field_.get(bean);
        }
        catch (Throwable t)
        {
            throw new IllegalArgumentException("Error getting " + field_.getName() + " field value: " + t.getMessage(), t);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void set(Object bean, Object value) throws IllegalArgumentException
    {        
        if (!field_.isAccessible())
        {
            field_.setAccessible(true);
        }
        try
        {
            field_.set(bean, value);
        }
        catch (Throwable t)
        {
            throw new IllegalArgumentException("Error setting " + field_.getName() + " field value: " + t.getMessage(), t);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getLabel()
    {
        return "";
    }


    @Override
    public Class getType()
    {
        if (getter_ != null)
        {
            return getter_.getReturnType();
        }
        
        return field_.getType();
    }


    @Override
    public XInfo getXInfo()
    {
        return xinfo_;
    }


    @Override
    public Class getBeanClass()
    {
        return class_;
    }


    @Override
    public boolean isPersistent()
    {
        return false;
    }


    @Override
    public boolean isMobile()
    {
        return false;
    }

    @Override
    public boolean isXtestIgnore()
    {
        return true;
    }

    @Override
    public Object getInstanceOf(Context ctx, Class targetType)
    {
        FacetMgr fMgr = (FacetMgr) ctx.get(FacetMgr.class);
        return fMgr.getInstanceOf(ctx, class_, targetType);
    }


    @Override
    public XOutput output(XOutput out, Object obj)
    {
        return out.output(get(obj));
    }

    protected Field field_ = null;
    protected Method getter_ = null;

    protected Class class_;
    protected XInfo xinfo_;
}
