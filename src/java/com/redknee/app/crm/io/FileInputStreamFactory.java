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
package com.redknee.app.crm.io;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.MinorLogMsg;


/**
 * This factory will create an input stream for the configured filename.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class FileInputStreamFactory extends AbstractFileInputStreamFactory
{
    /**
     * {@inheritDoc}
     */
    public Object create(Context ctx)
    {
        return getInputStream(ctx);
    }

    /**
     * {@inheritDoc}
     */
    public InputStream getInputStream(Context ctx)
    {
        try
        {
            return new FileInputStream(getFilename());
        }
        catch (IOException e)
        {
            String msg = "Unable to create input stream for file '" + getFilename() + "': " + e.getMessage();
            new MinorLogMsg(this, msg, e).log(ctx);
            return new ByteArrayInputStream(msg.getBytes());
        }
    }
}
