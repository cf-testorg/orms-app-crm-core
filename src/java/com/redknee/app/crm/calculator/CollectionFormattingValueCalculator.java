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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.MinorLogMsg;

/**
 * Value calculator for a repeating section.
 * 
 * @author cindy.wong@redknee.com
 * @since 8.3
 */
public class CollectionFormattingValueCalculator extends
        AbstractCollectionFormattingValueCalculator
{

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAdvanced(Context ctx)
    {
        String separator =getSeparator().toString();
        if (separator.isEmpty())
        {
            separator = System.getProperty("line.separator");
        }
        
        if (!(getDelegate() instanceof CollectionValueCalculator))
        {
            new MinorLogMsg(
                    this,
                    "Delegate of CollectionFormattingValueCalculator must be CollectionValueCalculator",
                    null).log(ctx);
            return null;
        }
        Collection collection = (Collection) getDelegate()
                .getValueAdvanced(ctx);

        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Object obj : collection)
        {
            sb.append(obj.toString());
            if (i < collection.size() || isSeparatorAfterLastElement())
            {
                sb.append(separator);
            }
            i++;
        }
        return sb.toString();
    }
}
