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

package com.redknee.app.crm.filter;

import com.redknee.app.crm.bean.NotificationMethodEnum;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.visitor.AbortVisitException;

/**
 * Predicate to filter out Email notification methods.
 * 
 * @author cindy.wong@redknee.com
 * @since 8.3
 */
public class NotificationMethodNoEmailPredicate implements Predicate
{

    private static final long serialVersionUID = 1L;

    /**
     * @see com.redknee.framework.xhome.filter.Predicate#f(com.redknee.framework.xhome.context.Context,
     *      java.lang.Object)
     */
    @Override
    public boolean f(Context ctx, Object obj) throws AbortVisitException
    {
        NotificationMethodEnum e = (NotificationMethodEnum) obj;
        return !NotificationMethodEnum.BOTH.equals(e)
                && !NotificationMethodEnum.EMAIL.equals(e);
    }

}
