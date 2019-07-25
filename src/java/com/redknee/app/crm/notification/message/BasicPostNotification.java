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

import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextLocator;

import com.redknee.app.crm.io.FileInputStreamFactory;
import com.redknee.app.crm.io.InputStreamFactory;


/**
 * This notification contains a filename representing a file that contains
 * the notification data.  It is purely for convenience and can be used in
 * place of a basic binary notification containing an input stream factory
 * that creates a FileInputStream.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class BasicPostNotification extends AbstractBasicPostNotification
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

    /**
     * {@inheritDoc}
     */
    public InputStreamFactory getInputStreamGenerator()
    {
        FileInputStreamFactory factory = null;
        try
        {
            factory = (FileInputStreamFactory) XBeans.instantiate(FileInputStreamFactory.class, ContextLocator.locate());
        }
        catch (Exception e)
        {
            factory = new FileInputStreamFactory();
        }
        factory.setFilename(getFilename());
        return factory;
    }

    /**
     * {@inheritDoc}
     */
    public void setInputStreamGenerator(InputStreamFactory factory)
    {
        // NOP
        // Input stream is always created from filename
    }
}
