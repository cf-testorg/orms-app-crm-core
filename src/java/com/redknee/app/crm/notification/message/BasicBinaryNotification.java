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
package com.redknee.app.crm.notification.message;

import java.io.InputStream;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.io.InputStreamFactory;


/**
 * This notification simply contains an input stream generator, and that generator
 * is executed to retrieve the input stream that reads the notification data.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class BasicBinaryNotification extends AbstractBasicBinaryNotification
{
    /**
     * {@inheritDoc}
     */
    public InputStream getInputStream(Context ctx)
    {
        InputStreamFactory inFactory = getInputStreamGenerator();
        if (inFactory != null)
        {
            return inFactory.getInputStream(ctx);
        }
        return null;
    }
}
