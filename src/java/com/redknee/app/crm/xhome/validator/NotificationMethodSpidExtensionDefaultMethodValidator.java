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

package com.redknee.app.crm.xhome.validator;

import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.NotificationMethodEnum;
import com.redknee.app.crm.bean.NotificationMethodProperty;
import com.redknee.app.crm.bean.NotificationMethodPropertyXInfo;
import com.redknee.app.crm.extension.spid.NotificationMethodSpidExtension;
import com.redknee.app.crm.support.FrameworkSupportHelper;

/**
 * Validator for per-subscription type default method.
 * 
 * @author cindy.wong@redknee.com
 * @since 8.3
 */
public class NotificationMethodSpidExtensionDefaultMethodValidator implements
        Validator
{

    /**
     * @see com.redknee.framework.xhome.beans.Validator#validate(com.redknee.framework.xhome.context.Context,
     *      java.lang.Object)
     */
    @Override
    public void validate(Context ctx, Object obj) throws IllegalStateException
    {
        NotificationMethodSpidExtension ext = (NotificationMethodSpidExtension) obj;
        for (Object key : ext.getNotificationMethods().keySet())
        {
            NotificationMethodProperty prop = (NotificationMethodProperty) ext
                    .getNotificationMethods().get(key);
            NotificationMethodEnum defaultMethod = NotificationMethodEnum
                    .get((short) prop.getDefaultMethod());
            if (!prop.getEmailAllowed()
                    && (NotificationMethodEnum.BOTH.equals(defaultMethod) || NotificationMethodEnum.EMAIL
                            .equals(defaultMethod)))
            {
                FrameworkSupportHelper.get(ctx).notifyExceptionListener(ctx, new IllegalPropertyArgumentException(
                        NotificationMethodPropertyXInfo.DEFAULT_METHOD,
                        "E-mail is not allowed for subscription type "
                                + prop.getSubscriptionType()));
            }

            if (!prop.getSmsAllowed()
                    && (NotificationMethodEnum.BOTH.equals(defaultMethod) || NotificationMethodEnum.SMS
                            .equals(defaultMethod)))
            {
                FrameworkSupportHelper.get(ctx).notifyExceptionListener(ctx, new IllegalPropertyArgumentException(
                        NotificationMethodPropertyXInfo.DEFAULT_METHOD,
                        "SMS is not allowed for subscription type "
                                + prop.getSubscriptionType()));
            }
        }
    }

}
