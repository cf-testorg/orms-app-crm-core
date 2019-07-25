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
package com.redknee.app.crm.calculator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.redknee.framework.xhome.context.Context;


/**
 * This constant calculator retrieves the configured bean from the context
 * and gets its value using the configured property info.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class PropertyBasedValueCalculator extends AbstractPropertyBasedValueCalculator
{
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Object> getDependentContextKeys(Context ctx)
    {   
        final List keyCollection = new ArrayList();
        keyCollection.add(this.getProperty().getBeanClass());
        return keyCollection;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAdvanced(Context ctx)
    {
        Object value = null;

        Object bean = ctx.get(this.getProperty().getBeanClass());
        if (bean == null)
        {
            Class beanClass = this.getProperty().getBeanClass();
            
            final String beanClassName;
            if (beanClass != null)
            {
                beanClassName = beanClass.getName();
            }
            else
            {
                beanClassName = this.getBeanClassName();
                try
                {
                    beanClass = Class.forName(beanClassName);
                }
                catch (Throwable t)
                {
                }
            }
            
            if (beanClass != null)
            {
                bean = ctx.get(beanClass);
            }
            
            if (bean == null)
            {
                bean = ctx.get(beanClassName);
            }
        }

        if (this.getProperty().getBeanClass().isInstance(bean))
        {
            value = this.getProperty().get(bean);
        }

        return value;
    }
}
