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
package com.redknee.app.crm.notification.template;

import com.redknee.framework.xhome.context.Context;


/**
 * This template is designed for extension, but may also be used on its own. It is a
 * template that contains the message as a string. The message may contain strings that
 * are intended for dynamic substitution, which would be processed by the message
 * generator. It is up to sub-classes and message generators to give the template meaning
 * if required.
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class SimpleNotificationTemplate extends AbstractSimpleNotificationTemplate
{

    /**
     * {@inheritDoc}
     */
    public String getTemplateMessage(Context ctx)
    {
        return getMessage();
    }

}
