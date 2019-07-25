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
package com.redknee.app.crm.notification.generator;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.KeyConfiguration;
import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.calculator.ConstantValueCalculator;
import com.redknee.app.crm.calculator.ToStringValueCalculator;
import com.redknee.app.crm.calculator.ValueCalculator;
import com.redknee.app.crm.notification.message.NotificationMessage;
import com.redknee.app.crm.notification.template.NotificationTemplate;
import com.redknee.app.crm.support.KeyValueSupportHelper;


/**
 * This class contains functions that are useful to all types of message generators.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public abstract class AbstractMessageGenerator extends AbstractBean implements MessageGenerator
{
    private static final ConstantValueCalculator NO_VALUE_CALCULATOR = new ConstantValueCalculator("");
    
    protected <T extends NotificationMessage> T instantiateMessage(
            Context ctx, 
            Class<T> type, 
            Class<? extends T> defaultValueType, 
            NotificationTemplate template) throws MessageGenerationException
    {
        if (type == null)
        {
            throw new NullPointerException("type parameter required");
        }

        T message = null;

        if (template != null)
        {
            message = (T) XBeans.getInstanceOf(ctx, template, type);
        }

        if (message == null)
        {
            try
            {
                message = (T) XBeans.instantiate(defaultValueType, ctx);
            }
            catch (Exception e)
            {
                throw new MessageGenerationException("Error instatiating new " + defaultValueType.getName(), e);
            }
        }
        
        return message;
    }
    
    protected Map<String, Object> generateKeyValuePairs(Context ctx, Set<String> keys, KeyValueFeatureEnum... features)
    {
        Map<String, Object> pairs = new LinkedHashMap<String, Object>();
        
        if (keys == null || keys.size() == 0)
        {
            return pairs;
        }

        Map<String, ValueCalculator> calcMap = new HashMap<String, ValueCalculator>();
        Collection<KeyConfiguration> keyConfs = KeyValueSupportHelper.get(ctx).getConfiguredKeys(ctx, true, features);
        if (keys != null)
        {
            for (KeyConfiguration key : keyConfs)
            {
                if (keys.contains(key.getKey()))
                {
                    calcMap.put(key.getKey(), key.getValueCalculator());
                }
            }
        }

        for (String key : keys)
        {
            ValueCalculator calc = calcMap.get(key);
            
            if (calc != null)
            {
                pairs.put(key, calc.getValue(ctx));
            }
        }
        
        if (LogSupport.isDebugEnabled(ctx))
        {
            LogSupport.debug(ctx, this, "Generated Key-Value Pairs: " + pairs);
        }
        
        return pairs;
    }
    
    protected String replaceVariables(Context ctx, String text, KeyValueFeatureEnum... features)
    {
        StringBuilder sb = new StringBuilder(text);
        
        if (features == null || features.length == 0)
        {
            // If no features are passed in, then assume no variable replacement is required.
            features = new KeyValueFeatureEnum[] {};
        }
        
        Collection<KeyConfiguration> keys = KeyValueSupportHelper.get(ctx).getConfiguredKeys(ctx, true, features);
        if (keys != null)
        {
            for (KeyConfiguration key : keys)
            {
                String keyword = key.getKey();

                ValueCalculator valueCalculator = key.getValueCalculator();
                if (valueCalculator == null)
                {
                    valueCalculator = NO_VALUE_CALCULATOR;
                }
                else
                {
                    valueCalculator = new ToStringValueCalculator(valueCalculator);
                }
                
                String value = (String) valueCalculator.getValue(ctx);
                
                for (int start = sb.indexOf(keyword); start >= 0; start = sb.indexOf(keyword, start))
                {
                    int end = start + keyword.length();
                    sb.replace(start, end, value);
                }
            }
        }
        
        return sb.toString();
    }

}
