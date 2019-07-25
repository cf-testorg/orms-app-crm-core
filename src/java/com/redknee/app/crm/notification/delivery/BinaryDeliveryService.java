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
package com.redknee.app.crm.notification.delivery;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.InfoLogMsg;

import com.redknee.app.crm.io.FileInputStreamFactory;
import com.redknee.app.crm.io.InputStreamFactory;
import com.redknee.app.crm.notification.LoggingNotificationResultCallback;
import com.redknee.app.crm.notification.NotificationResultCallback;
import com.redknee.app.crm.notification.NullNotificationResultCallback;
import com.redknee.app.crm.notification.RecipientInfo;
import com.redknee.app.crm.notification.message.BinaryNotificationMessage;
import com.redknee.app.crm.notification.message.NotificationMessage;

/**
 * This service delivers binary notification messages to an OutputStream. It proxies data
 * from an InputStream that is contained in the message.
 * 
 * Limitation: Binary messages passed to a scheduled liaison need to be able to have their
 * InputStream and OutputStream reconstructed at delivery time if the streams are not
 * capable of being saved to the ScheduledNotification home.
 * 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class BinaryDeliveryService extends AbstractBinaryDeliveryService
{
    /**
     * {@inheritDoc}
     */
    public void sendMessage(Context ctx, RecipientInfo recipient, NotificationMessage msg, NotificationResultCallback callback)
    {
        if (callback == null)
        {
            callback = new LoggingNotificationResultCallback(NullNotificationResultCallback.instance());
        }
        
        callback.reportAttempt(ctx);
        try
        {
            if (msg instanceof BinaryNotificationMessage)
            {
                BinaryNotificationMessage binMsg = (BinaryNotificationMessage) msg;

                String fileToDelete = null;
                InputStreamFactory streamGenerator = binMsg.getInputStreamGenerator();
                if (streamGenerator instanceof FileInputStreamFactory
                        && ((FileInputStreamFactory) streamGenerator).isTempFile())
                {
                    fileToDelete = ((FileInputStreamFactory) streamGenerator).getFilename();
                }
                
                InputStream inputStream = binMsg.getInputStream(ctx);
                OutputStream outputStream = recipient.getPostTo(ctx);

                if (inputStream != null && outputStream != null)
                {
                    send(ctx, inputStream, outputStream);
                    
                    if (fileToDelete != null)
                    {
                        try
                        {
                            new File(fileToDelete).delete();
                        }
                        catch (Exception e)
                        {
                            new InfoLogMsg(this, "Unable to delete temporary notification message file: " + fileToDelete + "(" + e.getMessage() + ")", null).log(ctx); 
                        }
                    }
                    
                    callback.reportSuccess(ctx);
                }
                else if (inputStream == null)
                {
                    callback.reportFailure(ctx, false, "Binary message contains no input stream.  No data will be delivered.");
                }
                else if (outputStream == null)
                {
                    callback.reportFailure(ctx, false, "Recipient information contains no output stream.  No data will be delivered.");
                }
            }
            else
            {
                String msgType = (msg != null ? msg.getClass().getName() : null);
                callback.reportFailure(ctx, false, "Message of type " + msgType + " not supported by " + this.getClass().getName());
            }
        }
        catch (MessageDeliveryException e)
        {
            callback.reportFailure(ctx, e.isRecoverable(), e);            
        }
        catch (Exception e)
        {
            callback.reportFailure(ctx, false, "Unexpected exception occurred while delivering message: " + e.getMessage(), e);
        }
    }

    protected void send(Context ctx, InputStream in, OutputStream out) throws MessageDeliveryException
    {
        // Wrap whatever input/output streams we get with buffered ones
        // The penalty for extra layer of buffering is much less than using an unbuffered stream
        in = new BufferedInputStream(in);
        out = new BufferedOutputStream(out);
        
        // Since we know we're using buffered streams, we can copy one byte at a time.
        // Leave it up to the buffered stream implementation to optimize buffer sizes.
        try
        {
            for (int nextByte = in.read(); nextByte != -1; nextByte = in.read())
            {
                out.write(nextByte);
            }
        }
        catch (IOException e)
        {
            throw new MessageDeliveryException("Error reading or writing notification data: " + e.getMessage(), false, e);
        }
        finally
        {
            closeOutputStream(ctx, out);
        }
    }

    protected void closeOutputStream(Context ctx, OutputStream outputStream)
    {
        if (outputStream != null)
        {
            try
            {
                outputStream.flush();
            }
            catch (IOException e)
            {
                new InfoLogMsg(this, "Error flushing output stream after message delivery.", e).log(ctx);
            }
            finally
            {
                try
                {
                    outputStream.close();
                }
                catch (IOException e)
                {
                    new InfoLogMsg(this, "Error closing output stream after message delivery.", e).log(ctx);
                }
            }
        }
    }
}
