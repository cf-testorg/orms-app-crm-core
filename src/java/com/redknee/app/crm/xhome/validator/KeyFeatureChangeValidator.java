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
package com.redknee.app.crm.xhome.validator;

import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.bean.KeyConfiguration;
import com.redknee.app.crm.bean.KeyConfigurationXInfo;
import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.calculator.UserInputValueCalculator;
import com.redknee.app.crm.calculator.ValueCalculator;
import com.redknee.app.crm.calculator.ValueCalculatorProxy;
import com.redknee.app.crm.support.HomeSupportHelper;


/**
 * This validator ensures that feature changes for key configurations will
 * not break the features that use them.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class KeyFeatureChangeValidator implements Validator
{

    /**
     * {@inheritDoc}
     */
    public void validate(Context ctx, Object obj) throws IllegalStateException
    {
        CompoundIllegalStateException cise = new CompoundIllegalStateException();
        
        if (obj instanceof KeyConfiguration)
        {
            KeyConfiguration keyConf = (KeyConfiguration) obj;
            
            try
            {
                final KeyConfiguration existingKeyConf = HomeSupportHelper.get(ctx).findBean(ctx, KeyConfiguration.class, keyConf.getKey());
                if (existingKeyConf != null
                        && !existingKeyConf.getFeature().equals(keyConf.getFeature()))
                {
                    ValueCalculator valueCalculator = keyConf.getValueCalculator();
                    boolean isUserInputCalculator = (valueCalculator instanceof UserInputValueCalculator);
                    if (!isUserInputCalculator
                            && valueCalculator instanceof ValueCalculatorProxy)
                    {
                        isUserInputCalculator = ((ValueCalculatorProxy)valueCalculator).hasDecorator(UserInputValueCalculator.class);
                    }
                    
                    if (isUserInputCalculator)
                    {
                        // User-input configurations are not allowed to change feature.
                        cise.thrown(new IllegalPropertyArgumentException(
                                KeyConfigurationXInfo.FEATURE, 
                                "Feature change to " + keyConf.getFeature()
                                + " not allowed for " + keyConf.getKey()
                                + " because it involves user input."));
                    }
                    else if (!KeyValueFeatureEnum.GENERIC.equals(keyConf.getFeature()))
                    {
                        // In general configurations are only allowed to change.
                        cise.thrown(new IllegalPropertyArgumentException(
                                KeyConfigurationXInfo.FEATURE, 
                                "Feature change to " + keyConf.getFeature()
                                + " not allowed for " + keyConf.getKey()
                                + ".  Valid feature types for this key: '"
                                + (!KeyValueFeatureEnum.GENERIC.equals(existingKeyConf.getFeature()) ? existingKeyConf.getFeature() + "', '" : "")
                                + KeyValueFeatureEnum.GENERIC + "'"));
                    }
                }
            }
            catch (HomeException e)
            {
                cise.thrown(e);
            }
        }
        
        cise.throwAll();
    }

}
