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

import java.util.Collection;
import java.util.Map;

import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.Wildcard;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class ConfigChangeRequestRoutingRule extends AbstractConfigChangeRequestRoutingRule
{

    /**
     * {@inheritDoc}
     */
    public int match(Context ctx, Object bean)
    {
        if (bean instanceof ConfigChangeRequest)
        {
            ConfigChangeRequest request = (ConfigChangeRequest) bean;
            ConfigChangeRequest filterRequest = this.getFilterBean();
            
            PropertyInfo[] wildcardProperties = new PropertyInfo[]
                                                                 {
                    ConfigChangeRequestXInfo.IDENTIFIER,
                    ConfigChangeRequestXInfo.BEAN_CLASS,
                    ConfigChangeRequestXInfo.BEAN_ID,
                    ConfigChangeRequestXInfo.MODIFIED_APP_NAME
                                                                 };
            for (PropertyInfo property : wildcardProperties)
            {
                final Object filterValue = property.get(filterRequest);
                if (filterValue instanceof String && ((String) filterValue).length() > 0)
                {
                    if (!new Wildcard(property, filterValue).f(ctx, request))
                    {
                        return NO_MATCH;
                    }
                }
            }

            final Map<Object, IndividualUpdate> filterUpdateMap = (Map<Object, IndividualUpdate>) ConfigChangeRequestXInfo.UPDATE_REQUEST.get(filterRequest);
            if (filterUpdateMap != null && filterUpdateMap.size() > 0)
            {
                final Collection<IndividualUpdate> updates = request.getUpdateRequest().values();
                
                final Collection<IndividualUpdate> filterUpdates = filterUpdateMap.values();
                for (IndividualUpdate filterUpdate : filterUpdates)
                {
                    for (IndividualUpdate update : updates)
                    {
                        if (new Wildcard(IndividualUpdateXInfo.FIELD_NAME, filterUpdate.getFieldName()).f(ctx, update))
                        {
                            String oldFilterValue = filterUpdate.getOldValue();
                            if (oldFilterValue != null && oldFilterValue.length() > 0)
                            {
                                if (!new Wildcard(IndividualUpdateXInfo.OLD_VALUE, oldFilterValue).f(ctx, update))
                                {
                                    return NO_MATCH;
                                }
                            }

                            String newFilterValue = filterUpdate.getNewValue();
                            if (newFilterValue != null && newFilterValue.length() > 0)
                            {
                                if (!new Wildcard(IndividualUpdateXInfo.NEW_VALUE, newFilterValue).f(ctx, update))
                                {
                                    return NO_MATCH;
                                }
                            }
                            break;
                        }
                    }
                }
            }
            
            return super.getMatchStrength();
        }
        return NO_MATCH;
    }

}
