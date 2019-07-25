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
package com.redknee.app.crm.xhome.adapter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.FacetMgr;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.beans.xi.XInfoSupport;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.InfoLogMsg;

import com.redknee.app.crm.xhome.beans.xi.NonModelPropertyInfo;


/**
 * This adapter will not only perform XBeans.copy() to copy all FW properties, but will also copy
 * non-model related fields from one bean to another.  This is useful when adapting between a
 * GUI-layer model and a core-layer concrete class.
 * 
 * Note: Non-model related fields are only adapted on write with this adapter.
 *       Use the {@link com.redknee.app.crm.xhome.adapter.ReflectionBeanAdapter} to ensure that this
 *       happens for reads and writes.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class WriteOnlyReflectionBeanAdapter<UNADAPT_TYPE extends AbstractBean, ADAPT_TYPE extends AbstractBean> extends BeanAdapter<UNADAPT_TYPE, ADAPT_TYPE>
{
    public WriteOnlyReflectionBeanAdapter(
            Context ctx,
            Class<UNADAPT_TYPE> sourceClass, 
            Map<String, PropertyInfo> sourceFieldToDestPropertyMap,
            Class<ADAPT_TYPE> destinationClass)
    {
        this(ctx, sourceClass, sourceFieldToDestPropertyMap, null, destinationClass);
    }
    
    public WriteOnlyReflectionBeanAdapter(
            Context ctx,
            Class<UNADAPT_TYPE> sourceClass, 
            Map<String, PropertyInfo> sourceFieldToDestPropertyMap,
            Map<String, String> sourceFieldToDestFieldMap,
            Class<ADAPT_TYPE> destinationClass)
    {
        this(ctx, sourceClass, sourceFieldToDestPropertyMap, sourceFieldToDestFieldMap, destinationClass, null);
    }
    
    public WriteOnlyReflectionBeanAdapter(
            Context ctx,
            Class<UNADAPT_TYPE> sourceClass, 
            Class<ADAPT_TYPE> destinationClass,
            Map<PropertyInfo, String> sourcePropertyToDestFieldMap)
    {
        this(ctx, sourceClass, null, null, destinationClass, sourcePropertyToDestFieldMap);
    }
    
    public WriteOnlyReflectionBeanAdapter(
            Context ctx,
            Class<UNADAPT_TYPE> sourceClass, 
            Class<ADAPT_TYPE> destinationClass,
            Map<PropertyInfo, String> sourcePropertyToDestFieldMap,
            Map<String, String> sourceFieldToDestFieldMap)
    {
        this(ctx, sourceClass, null, sourceFieldToDestFieldMap, destinationClass, sourcePropertyToDestFieldMap);
    }
    
    public WriteOnlyReflectionBeanAdapter(
            Context ctx,
            Class<UNADAPT_TYPE> sourceClass, 
            Map<String, PropertyInfo> sourceFieldToDestPropertyMap,
            Map<String, String> sourceFieldToDestFieldMap,
            Class<ADAPT_TYPE> destinationClass,
            Map<PropertyInfo, String> sourcePropertyToDestFieldMap)
    {
        super(sourceClass, destinationClass);

        FacetMgr fMgr = (FacetMgr) ctx.get(FacetMgr.class);
        XInfo srcXInfo = (XInfo) fMgr.getInstanceOf(ctx, sourceClass, XInfo.class);
        XInfo destXInfo = (XInfo) fMgr.getInstanceOf(ctx, destinationClass, XInfo.class);
        
        if (sourceFieldToDestPropertyMap != null)
        {
            for (Map.Entry<String, PropertyInfo> entry : sourceFieldToDestPropertyMap.entrySet())
            {
                final String fieldName = entry.getKey();
                final PropertyInfo sourceProperty = getPropertyInfoForField(ctx, sourceClass, fieldName, srcXInfo);
                if (sourceProperty != null)
                {
                    propertyMap_.put(sourceProperty, entry.getValue());
                }
                else
                {
                    new InfoLogMsg(this, "Could not find a suitable PropertyInfo instance for field: " + sourceClass.getName() + "." + fieldName, null).log(ctx);
                }
            }
        }

        if (sourcePropertyToDestFieldMap != null)
        {
            for (Map.Entry<PropertyInfo, String> entry : sourcePropertyToDestFieldMap.entrySet())
            {
                final String fieldName = entry.getValue();
                final PropertyInfo destProperty = getPropertyInfoForField(ctx, destinationClass, fieldName, destXInfo);
                if (destProperty != null)
                {
                    propertyMap_.put(entry.getKey(), destProperty);
                }
                else
                {
                    new InfoLogMsg(this, "Could not find a suitable PropertyInfo instance for field: " + destinationClass.getName() + "." + fieldName, null).log(ctx);
                }
            }
        }
        
        if (sourceFieldToDestFieldMap != null)
        {
            for (Map.Entry<String, String> entry : sourceFieldToDestFieldMap.entrySet())
            {
                final String srcFieldName = entry.getKey();
                final String destFieldName = entry.getValue();
                
                final PropertyInfo sourceProperty = getPropertyInfoForField(ctx, sourceClass, srcFieldName, srcXInfo);
                if (sourceProperty != null)
                {
                    final PropertyInfo destProperty = getPropertyInfoForField(ctx, destinationClass, destFieldName, destXInfo);
                    if (destProperty != null)
                    {
                        propertyMap_.put(sourceProperty, destProperty);
                    }
                    else
                    {
                        new InfoLogMsg(this, "Could not find a suitable PropertyInfo instance for field: " + destinationClass.getName() + "." + destFieldName, null).log(ctx);
                    }
                }
                else
                {
                    new InfoLogMsg(this, "Could not find a suitable PropertyInfo instance for field: " + sourceClass.getName() + "." + srcFieldName, null).log(ctx);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object unAdapt(Context ctx, Object bean) throws HomeException
    {
        final Object resultBean = super.unAdapt(ctx, bean);
        copyProperties(ctx, bean, resultBean, false);
        return resultBean;
    }

    protected void copyProperties(
            final Context ctx, 
            final Object bean, 
            final Object resultBean,
            boolean isAdapt)
    {
        if (bean != null
                && resultBean != null
                && bean != resultBean)
        {
            for (Map.Entry<PropertyInfo, PropertyInfo> mapping : propertyMap_.entrySet())
            {
                final PropertyInfo srcProperty;
                final PropertyInfo destProperty;
                if (isAdapt)
                {
                    srcProperty = mapping.getKey();
                    destProperty = mapping.getValue();
                }
                else
                {
                    srcProperty = mapping.getValue();
                    destProperty = mapping.getKey();
                }
                
                if (srcProperty != null && destProperty != null)
                {
                    if (destProperty.getType().isAssignableFrom(srcProperty.getType()))
                    {
                        Object sourceValue = null;
                        try
                        {
                            sourceValue = srcProperty.get(bean);
                        }
                        catch (Throwable t)
                        {
                            new InfoLogMsg(this, "Error getting value for " + bean.getClass().getName() + "." + srcProperty.getName() + "().  This field will not be copied to the instance of " + resultBean.getClass().getName() + ".", t).log(ctx);
                            continue;
                        }

                        try
                        {
                            destProperty.set(resultBean, sourceValue);
                        }
                        catch (Throwable t)
                        {
                            new InfoLogMsg(this, "Error setting property " + resultBean.getClass().getName() + "." + destProperty.getName() + "  This field will not be copied from the instance of " + bean.getClass().getName() + ".  Value=" + String.valueOf(sourceValue), t).log(ctx);
                        }
                    }
                    else
                    {
                        new InfoLogMsg(this, "Error setting property " + resultBean.getClass().getName() + "." + destProperty.getName() + "  This field is not type compatiblie with " + bean.getClass().getName() + "." + srcProperty.getName(), null).log(ctx);
                    }
                }
            }
        }
    }

    private PropertyInfo getPropertyInfoForField(Context ctx, Class<? extends AbstractBean> clazz, final String fieldName,
            XInfo xinfo)
    {
        PropertyInfo property = null;
        
        if (xinfo != null)
        {
            // See if there is a valid property first
            String propertyName = fieldName;
            if (propertyName.endsWith("_"))
            {
                // FW fields are named with an "_" (as are most RK fields) and FW property names do not have the "_".
                propertyName = propertyName.substring(0, propertyName.length()-1);
            }
            property = XInfoSupport.getPropertyInfo(ctx, xinfo, propertyName);
        }

        if (property == null)
        {
            // No valid property in the XInfo.  Make one.
            Field field = null;
            Throwable error = null;
            Class iteratingClass = clazz;

            while (iteratingClass!=null && field==null)
            {
                try
                {
                    field = iteratingClass.getDeclaredField(fieldName);
                }
                catch (Throwable t)
                {
                    error = t;
                }
                iteratingClass = clazz.getSuperclass();
            }

            if (field == null)
            {
                new InfoLogMsg(this, "Unable to find " + clazz.getName() + "." + fieldName + ".  Adapter will not process this field.", error).log(ctx);
            }
            else
            {
                property = new NonModelPropertyInfo(ctx, clazz, fieldName);
            }
        }
        
        return property;
    }

    protected final Map<PropertyInfo, PropertyInfo> propertyMap_ = new HashMap<PropertyInfo, PropertyInfo>();
}
