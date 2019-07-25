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
package com.redknee.app.crm.notification.generator;

import com.redknee.framework.xhome.beans.XCloneable;
import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.notification.message.NotificationMessage;
import com.redknee.app.crm.notification.template.NotificationTemplate;


/**
 * This message generator does nothing.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class NullMessageGenerator implements MessageGenerator, XCloneable
{
    protected static MessageGenerator instance_ = null;

    public static MessageGenerator instance()
    {
        if (instance_ == null)
        {
            instance_ = new NullMessageGenerator();
        }
        return instance_;
    }
    
    public NullMessageGenerator()
    {
    }

    /**
     * {@inheritDoc}
     */
    public NotificationMessage generate(Context ctx, NotificationTemplate template, KeyValueFeatureEnum... features) throws MessageGenerationException
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
