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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;

import com.redknee.app.crm.bean.KeyConfiguration;
import com.redknee.app.crm.bean.KeyValueEntry;
import com.redknee.app.crm.bean.KeyValueEntryXInfo;
import com.redknee.app.crm.support.HomeSupportHelper;


/**
 * A value calculator that returns a user-input value.  Note that this calculator
 * needs an instance of com.redknee.app.crm.bean.KeyConfiguration in the context
 * to run properly.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class UserInputValueCalculator extends AbstractUserInputValueCalculator
{
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Object> getDependentContextKeys(Context ctx)
    {
        Collection dependencies = new ArrayList();
        dependencies.add(KeyConfiguration.class);
        
        return dependencies;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAdvanced(Context ctx)
    {
        Object value = null;
        
        KeyConfiguration keyConf = (KeyConfiguration) ctx.get(KeyConfiguration.class);

        Class beanClass = getBeanClass(ctx, keyConf);
        String beanId = getBeanId(ctx, keyConf);
        
        if (beanClass != null
                && beanId != null)
        {
            value = getValueEntry(ctx, beanClass, beanId);
        }

        if (value == null)
        {
            value = super.getValue();
        }
        if (value == null)
        {
            value = "";
        }
        
        return value;
    }

    protected String getValueEntry(Context ctx, Class beanClass, String beanId)
    {
        String value = null;

        KeyConfiguration keyConf = (KeyConfiguration) ctx.get(KeyConfiguration.class);
        String key = keyConf.getKey();
        
        new DebugLogMsg(this, "Looking for user defined value for key=" + key + ", " + beanClass.getName() + "-ID=" + beanId + "...", null).log(ctx);
        And filter = new And();
        filter.add(new EQ(KeyValueEntryXInfo.KEY, key));
        filter.add(new EQ(KeyValueEntryXInfo.BEAN_CLASS, beanClass.getName()));
        filter.add(new EQ(KeyValueEntryXInfo.BEAN_IDENTIFIER, beanId));
        
        try
        {
            KeyValueEntry inheritedValue = HomeSupportHelper.get(ctx).findBean(ctx, KeyValueEntry.class, filter);
            if (inheritedValue != null)
            {
                value = inheritedValue.getValue();
            }
        }
        catch (HomeException e)
        {
            String msg = "Error looking up key value entry";
            new InfoLogMsg(this, msg + ": " + e.getMessage(), null).log(ctx);
            new DebugLogMsg(this, msg, e).log(ctx);
        }
        
        return value;
    }

    protected Class getBeanClass(Context ctx, KeyConfiguration keyConf)
    {
        // Override in true base class
        return keyConf.getClass();
    }

    protected String getBeanId(Context ctx, KeyConfiguration keyConf)
    {
        // Override in true base class
        return keyConf.getKey();
    }
}
