/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily
 * available. Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee. No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used in
 * accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */

package com.redknee.app.crm.calculator;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.support.StringUtil;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.delivery.email.EmailRepeatingTemplate;
import com.redknee.app.crm.delivery.email.EmailRepeatingTemplateHome;
import com.redknee.app.crm.delivery.email.EmailRepeatingTemplateXInfo;
import com.redknee.app.crm.delivery.email.RepeatingTemplateTypeEnum;
import com.redknee.app.crm.support.KeyValueSupportHelper;

/**
 * Value calculator for a repeating section.
 * 
 * @author cindy.wong@redknee.com
 * @since 8.3
 */
public class CollectionValueCalculator extends
        AbstractCollectionValueCalculator
{

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAdvanced(Context ctx)
    {
        RepeatingTemplateTypeEnum repeatingTemplateType = RepeatingTemplateTypeEnum.get((short) getTemplateType());
        Object obj = ctx.get(repeatingTemplateType, Collections.EMPTY_LIST);
        Collection collection = null;
        if (obj instanceof Collection)
        {
            collection = (Collection) obj;
        }
        else if (obj instanceof Map)
        {
            collection = ((Map) obj).values();
        }

        List results = new LinkedList();
        Home home = (Home) ctx.get(EmailRepeatingTemplateHome.class);
        EmailRepeatingTemplate template = null;
        try
        {
            template = (EmailRepeatingTemplate) home.find(ctx, new EQ(
                    EmailRepeatingTemplateXInfo.TEMPLATE_TYPE,
                    getTemplateType()));
        }
        catch (HomeException exception)
        {
            new MinorLogMsg(this,
                    "Exception caught when looking up EmailRepeatingTemplate",
                    exception).log(ctx);
            return null;
        }

        if (template == null)
        {
            new MinorLogMsg(this, "Cannot find  EmailRepeatingTemplate", null)
                    .log(ctx);
            return null;

        }

        if (collection != null)
        {
            for (Object elem : collection)
            {
                Context subCtx = ctx.createSubContext();
                
                // Remove the list from the context in case a sub-template contains
                // another CollectionValueCalculator for the same type
                subCtx.put(repeatingTemplateType, null);
                
                // Put the object in the context for downstream value calculators
                subCtx.put(elem.getClass(), elem);
                
                Map<String, Object> map = KeyValueSupportHelper.get(subCtx).getCalculatedKeyValueMap(subCtx, KeyValueFeatureEnum.GENERIC);
                
                String message = template.getRepeatingSection();
                for (String key : map.keySet())
                {
                    if (map.get(key)!=null)
                    {
                        message = StringUtil.stringReplace(message, key , map.get(key).toString());
                    }
                }
                results.add(message);
            }
        }
        return results;
    }
}
