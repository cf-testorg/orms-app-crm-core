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
package com.redknee.app.crm.web.control;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.visitor.AbortVisitException;

import com.redknee.app.crm.bean.KeyConfiguration;
import com.redknee.app.crm.bean.KeyConfigurationKeyWebControl;
import com.redknee.app.crm.calculator.AbstractValueCalculator;
import com.redknee.app.crm.calculator.ValueCalculator;
import com.redknee.app.crm.support.BeanLoaderSupport;
import com.redknee.app.crm.support.BeanLoaderSupportHelper;


/**
 * Custom key web control for showing only Key Configurations that are anticipated to be
 * supported by the use case.  This class looks at the contents of the bean loader that
 * would be used for the provided base case classes (or if no base case classes provided, 
 * then the current bean loader), looks at context keys that the implementation will be 
 * adding to the context prior to value calculation, and current contents of the Context.
 * Key Configurations that require any keys that do not satisfy any of those criteria are 
 * considered to be "not available for use" by that use case.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class BeanLoaderAccessibleKeyConfigurationKeyWebControl extends KeyConfigurationKeyWebControl
{
    protected Class[] baseCaseClasses_;
    protected Set anticipatedContextKeys_;

    public BeanLoaderAccessibleKeyConfigurationKeyWebControl(Object... anticipatedContextKeys)
    {
        this(null, anticipatedContextKeys);
    }

    public BeanLoaderAccessibleKeyConfigurationKeyWebControl(Class[] baseCaseClasses, Object... anticipatedContextKeys)
    {
        baseCaseClasses_ = baseCaseClasses;
        anticipatedContextKeys_ = new HashSet(Arrays.asList(anticipatedContextKeys));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Home getHome(Context ctx)
    {
        Home home = super.getHome(ctx);
        home = home.where(ctx, new AvailableKeyCheckingPredicate());
        return home;
    }
    
    protected Map getBeanLoaderMap(Context ctx)
    {
        synchronized(this)
        {
            if (beanLoaderMap_ == null)
            {
                BeanLoaderSupport beanLoaderSupport = BeanLoaderSupportHelper.get(ctx);
                
                if (baseCaseClasses_ != null)
                {
                    beanLoaderMap_ = new HashMap();
                    for (Class baseCaseClass : baseCaseClasses_)
                    {
                        Map newMap = beanLoaderSupport.getBeanLoaderMap(ctx, baseCaseClass);
                        beanLoaderSupport.mergeBeanLoaderMaps(beanLoaderMap_, newMap);
                    }
                }
                else
                {
                    beanLoaderMap_ = beanLoaderSupport.getBeanLoaderMap(ctx);
                }
            }
        }
        
        return beanLoaderMap_;
    }

    protected Map beanLoaderMap_ = null;

    private final class AvailableKeyCheckingPredicate implements Predicate
    {
        public boolean f(Context ctx, Object obj) throws AbortVisitException
        {
            if (obj instanceof KeyConfiguration)
            {
                KeyConfiguration keyConf = (KeyConfiguration) obj;
                
                ValueCalculator calc = keyConf.getValueCalculator();
                if (calc instanceof AbstractValueCalculator)
                {
                    Collection<Object> keys = ((AbstractValueCalculator) calc).getDependentContextKeys(ctx);
                    for (Object key : keys)
                    {
                        if (!isAccessible(ctx, key))
                        {
                            return false;
                        }
                    }
                }
            }
            
            return true;
        }


        private boolean isAccessible(Context ctx, Object key)
        {
            if (anticipatedContextKeys_.contains(key)
                    || getBeanLoaderMap(ctx).containsKey(key)
                    || ctx.has(key))
            {
                return true;
            }
            return false;
        }
    }
}
