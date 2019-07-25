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

import java.util.Collection;
import java.util.Map;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.AlcatelSSCProperty;
import com.redknee.app.crm.bean.KeyConfiguration;
import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.calculator.UserInputValueCalculator;
import com.redknee.app.crm.calculator.ValueCalculator;
import com.redknee.app.crm.calculator.ValueCalculatorProxy;


/**
 * Support class for stuff shared by the 3 Alcatel SSC extensions.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class DefaultAlcatelSSCSupport implements AlcatelSSCSupport
{
    protected static AlcatelSSCSupport instance_ = null;
    public static AlcatelSSCSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultAlcatelSSCSupport();
        }
        return instance_;
    }

    protected DefaultAlcatelSSCSupport()
    {
    }
    
    /**
     * {@inheritDoc}
     */
    public void initializeMap(Context ctx, KeyValueFeatureEnum feature, Map<String, AlcatelSSCProperty> keyValuePairs)
    {
        switch (feature.getIndex())
        {
        case KeyValueFeatureEnum.ALCATEL_SSC_SUBSCRIPTION_INDEX:
        case KeyValueFeatureEnum.ALCATEL_SSC_SERVICE_INDEX:
        case KeyValueFeatureEnum.ALCATEL_SSC_SPID_INDEX:
            break;
        default:
            // Nothing to do for non-Alcatel SSC key/value features
            return;
        }
        
        if (ctx != null && keyValuePairs != null)
        {
            Collection<KeyConfiguration> keys = KeyValueSupportHelper.get(ctx).getConfiguredKeys(ctx, false, feature);
            if (keys != null)
            {
                // Add any mandatory keys that are missing to the map
                for (KeyConfiguration keyConf : keys)
                {
                    String key = keyConf.getKey();
                    if (!keyValuePairs.containsKey(key))
                    {
                        ValueCalculator calc = keyConf.getValueCalculator();
                        if (calc instanceof ValueCalculatorProxy)
                        {
                            calc = ((ValueCalculatorProxy)calc).findDecorator(UserInputValueCalculator.class);
                        }
                        if (calc instanceof UserInputValueCalculator)
                        {
                            AlcatelSSCProperty property = new AlcatelSSCProperty();
                            property.setKey(key);
                            
                            String value = KeyValueSupportHelper.get(ctx).getUserInputValue(ctx, feature, keyConf);
                            property.setValue(value);
                            
                            keyValuePairs.put(key, property);
                        }
                    }
                }
            }

            for (Map.Entry<String, AlcatelSSCProperty> entry : keyValuePairs.entrySet())
            {
                AlcatelSSCProperty property = entry.getValue();
                if (property != null
                        && (property.getValue() == null
                                || AlcatelSSCProperty.DEFAULT_VALUE.equals(property.getValue())))
                {
                    String value = KeyValueSupportHelper.get(ctx).getUserInputValue(ctx, feature, entry.getKey());
                    property.setValue(value);
                }
            }
        }
    }
}
