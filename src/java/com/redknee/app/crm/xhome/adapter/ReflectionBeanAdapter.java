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

import java.util.Map;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;


/**
 * This adapter will not only perform XBeans.copy() to copy all FW properties, but will also copy
 * non-model related fields from one bean to another.  This is useful when adapting between a
 * GUI-layer model and a core-layer concrete class.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class ReflectionBeanAdapter<UNADAPT_TYPE extends AbstractBean, ADAPT_TYPE extends AbstractBean> extends WriteOnlyReflectionBeanAdapter<UNADAPT_TYPE, ADAPT_TYPE>
{
    public ReflectionBeanAdapter(
            Context ctx,
            Class<UNADAPT_TYPE> sourceClass, 
            Map<String, PropertyInfo> sourceFieldToDestPropertyMap,
            Class<ADAPT_TYPE> destinationClass)
    {
        this(ctx, sourceClass, sourceFieldToDestPropertyMap, null, destinationClass);
    }
    
    public ReflectionBeanAdapter(
            Context ctx,
            Class<UNADAPT_TYPE> sourceClass, 
            Map<String, PropertyInfo> sourceFieldToDestPropertyMap,
            Map<String, String> sourceFieldToDestFieldMap,
            Class<ADAPT_TYPE> destinationClass)
    {
        this(ctx, sourceClass, sourceFieldToDestPropertyMap, sourceFieldToDestFieldMap, destinationClass, null);
    }
    
    public ReflectionBeanAdapter(
            Context ctx,
            Class<UNADAPT_TYPE> sourceClass, 
            Class<ADAPT_TYPE> destinationClass,
            Map<PropertyInfo, String> sourcePropertyToDestFieldMap)
    {
        this(ctx, sourceClass, null, null, destinationClass, sourcePropertyToDestFieldMap);
    }
    
    public ReflectionBeanAdapter(
            Context ctx,
            Class<UNADAPT_TYPE> sourceClass, 
            Class<ADAPT_TYPE> destinationClass,
            Map<PropertyInfo, String> sourcePropertyToDestFieldMap,
            Map<String, String> sourceFieldToDestFieldMap)
    {
        this(ctx, sourceClass, null, sourceFieldToDestFieldMap, destinationClass, sourcePropertyToDestFieldMap);
    }
    
    public ReflectionBeanAdapter(
            Context ctx,
            Class<UNADAPT_TYPE> sourceClass, 
            Map<String, PropertyInfo> sourceFieldToDestPropertyMap,
            Map<String, String> sourceFieldToDestFieldMap,
            Class<ADAPT_TYPE> destinationClass,
            Map<PropertyInfo, String> sourcePropertyToDestFieldMap)
    {
        super(ctx, sourceClass, sourceFieldToDestPropertyMap, sourceFieldToDestFieldMap, destinationClass, sourcePropertyToDestFieldMap);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object adapt(Context ctx, Object bean) throws HomeException
    {
        final Object resultBean = super.adapt(ctx, bean);
        copyProperties(ctx, bean, resultBean, true);
        return resultBean;
    }
}
