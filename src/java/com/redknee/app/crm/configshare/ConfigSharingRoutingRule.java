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
package com.redknee.app.crm.configshare;

import java.util.Map;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.Wildcard;
import com.redknee.framework.xlog.log.InfoLogMsg;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class ConfigSharingRoutingRule extends AbstractConfigSharingRoutingRule
{

    /**
     * {@inheritDoc}
     */
    public int match(Context ctx, Object bean)
    {
        Map beanIdentifiers = this.getBeanIdentifiers();
        if (beanIdentifiers != null && beanIdentifiers.size() > 0)
        {
            for (Object idObj : beanIdentifiers.values())
            {
                if (idObj instanceof SharedBeanIdentifier)
                {
                    SharedBeanIdentifier identifier = (SharedBeanIdentifier) idObj;
                    try
                    {
                        if (isInstance(ctx, bean, identifier.getBeanClass())
                                && new Wildcard(ConfigChangeRequestXInfo.BEAN_ID, identifier.getBeanIdentifier()).f(ctx, bean))
                        {
                            return getMatchStrength();
                        }
                    }
                    catch (Throwable t)
                    {
                        new InfoLogMsg(this, "Error executing routing rule for shared bean identifier [" + identifier + "]", t).log(ctx);
                    }
                }
            }
        }

        return NO_MATCH;
    }

    private boolean isInstance(Context ctx, Object bean, String ruleClassName)
    {
        boolean result = new EQ(ConfigChangeRequestXInfo.BEAN_CLASS, ruleClassName).f(ctx, bean);
        
        if (!result
                && bean instanceof ConfigChangeRequest)
        {
            ConfigChangeRequest request = (ConfigChangeRequest) bean;
            try
            {
                Class ruleClass = Class.forName(ruleClassName);
                Class requestClass = Class.forName(request.getBeanClass());
                if (ruleClass != null && requestClass != null)
                {
                    result = ruleClass.isAssignableFrom(requestClass);
                }
            }
            catch (ClassNotFoundException e)
            {
                // NOP
            }
        }
        
        return result;
    }

}
