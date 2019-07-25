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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.MinorLogMsg;


/**
 * This factory will create an output stream for the given filename.
 * 
 * If the file does not exist, the factory will attempt to create it (and parent directories if applicable)
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class FileOutputStreamFactory extends AbstractFileOutputStreamFactory
{
    /**
     * {@inheritDoc}
     */
    public Object create(Context ctx)
    {
        return getOutputStream(ctx);
    }

    /**
     * {@inheritDoc}
     */
    public OutputStream getOutputStream(Context ctx)
    {
        try
        {
            File file = new File(getFilename());
            if (!file.exists())
            {
                File parentFile = file.getParentFile();
                if (parentFile != null)
                {
                    parentFile.mkdirs();
                }
                file.createNewFile();
            }
            return new FileOutputStream(file);
        }
        catch (IOException e)
        {
            new MinorLogMsg(this, "Unable to create output stream for file '" + getFilename() + "': " + e.getMessage(), e).log(ctx);
            return null;
        }
    }
}
