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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.redknee.framework.xhome.context.Context;


/**
 * This factory returns an output stream that does not follow the OutputStream
 * contract for the close() method.  It should only be used if the sender wants
 * the output stream to be left open by the delivery service.  If it is used, it
 * is the caller's responsibility to eventually close the output stream itself.
 * This can either be done by calling this factory's closeRawOutputStream() method
 * or by calling close on the actual output stream.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class ExplicitCloseOutputStreamFactory extends OutputStreamFactoryProxy
{
    public ExplicitCloseOutputStreamFactory()
    {
    }
    
    public ExplicitCloseOutputStreamFactory(OutputStreamFactory delegate)
    {
        setDelegate(delegate);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public OutputStream getOutputStream(Context ctx)
    {
        OutputStream out = super.getOutputStream(ctx);
        if (out == null)
        {
            return null;
        }
        
        return new NoCloseOutputStream(out);
    }
    
    public void closeRawOutputStream(Context ctx) throws IOException
    {
        OutputStream out = super.getOutputStream(ctx);
        if (out != null)
        {
            out.close();
        }
    }
    
    /**
     * This output stream does not follow the OutputStream contract for the close() method.
     * 
     * If it is used, it is the caller's responsibility to eventually close the output stream itself.
     *
     * @author aaron.gourley@redknee.com
     * @since 8.8/9.0
     */
    private static class NoCloseOutputStream extends FilterOutputStream
    {
        public NoCloseOutputStream(OutputStream out)
        {
            super(out);
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public void close() throws IOException
        {
            // NOP - The factory's closeRawOutputStream() method must be called to close the underlying stream.
        }
    }
}
