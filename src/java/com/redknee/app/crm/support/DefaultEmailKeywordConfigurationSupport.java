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

import com.redknee.app.crm.calculator.CurrencyFormattingValueCalculator;
import com.redknee.app.crm.calculator.DateFormattingValueCalculator;
import com.redknee.app.crm.calculator.PropertyBasedValueCalculator;
import com.redknee.app.crm.calculator.ToStringValueCalculator;
import com.redknee.app.crm.calculator.ValueCalculator;
import com.redknee.app.crm.delivery.email.DateFormattingConstantValueCalculator;
import com.redknee.app.crm.delivery.email.PropertyBasedConstantValueCalculator;
import com.redknee.app.crm.delivery.email.SpidCurrencyFormattingConstantValueCalculator;
import com.redknee.app.crm.delivery.email.ToStringConstantValueCalculator;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class DefaultEmailKeywordConfigurationSupport implements EmailKeywordConfigurationSupport
{
    protected static EmailKeywordConfigurationSupport instance_ = null;
    public static EmailKeywordConfigurationSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultEmailKeywordConfigurationSupport();
        }
        return instance_;
    }

    protected DefaultEmailKeywordConfigurationSupport()
    {
    }
    
    public ValueCalculator getNonDeprecatedValueCalculator(ValueCalculator calc)
    {
        ValueCalculator valueCalculator = calc;
        
        if (valueCalculator instanceof PropertyBasedConstantValueCalculator)
        {
            PropertyBasedConstantValueCalculator propCalc = (PropertyBasedConstantValueCalculator) valueCalculator;
            Class cls = null;
            try
            {
                cls = Class.forName(propCalc.getBeanClassName());
            }
            catch (ClassNotFoundException e)
            {
            }

            PropertyBasedValueCalculator newCalc = new PropertyBasedValueCalculator();
            newCalc.setBeanClassName(propCalc.getBeanClassName());
            newCalc.setProperty(propCalc.getProperty());
            valueCalculator = newCalc;
        }
        else if (valueCalculator instanceof DateFormattingConstantValueCalculator)
        {
            DateFormattingConstantValueCalculator dateFormattingCalc = (DateFormattingConstantValueCalculator) valueCalculator;
            
            DateFormattingValueCalculator newCalc = new DateFormattingValueCalculator();
            newCalc.setDelegate(getNonDeprecatedValueCalculator(dateFormattingCalc.getDelegate()));
            newCalc.setFormat(dateFormattingCalc.getFormat());
            valueCalculator = newCalc;
        }
        else if (valueCalculator instanceof SpidCurrencyFormattingConstantValueCalculator)
        {
            SpidCurrencyFormattingConstantValueCalculator currencyFormattingCalc = (SpidCurrencyFormattingConstantValueCalculator) valueCalculator;
            
            CurrencyFormattingValueCalculator newCalc = new CurrencyFormattingValueCalculator();
            newCalc.setDelegate(getNonDeprecatedValueCalculator(currencyFormattingCalc.getDelegate()));
            valueCalculator = newCalc;
        }
        else if (valueCalculator instanceof ToStringConstantValueCalculator)
        {
            ToStringConstantValueCalculator toStringCalc = (ToStringConstantValueCalculator) valueCalculator;
            
            ToStringValueCalculator newCalc = new ToStringValueCalculator();
            newCalc.setDelegate(getNonDeprecatedValueCalculator(toStringCalc.getDelegate()));
            valueCalculator = newCalc;
        }
        
        return valueCalculator;
    }
}
