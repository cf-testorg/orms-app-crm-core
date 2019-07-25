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
package com.redknee.app.crm.delivery.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.redknee.framework.msg.EmailBody;
import com.redknee.framework.msg.EmailConfig;
import com.redknee.framework.msg.HtmlMailContentHandler;
import com.redknee.framework.msg.MailContentHandler;
import com.redknee.framework.msg.Msg;
import com.redknee.framework.msg.SMTPDeliveryMsgBox;
import com.redknee.framework.msg.TextMailContentHandler;
import com.redknee.framework.xhome.beans.XCloneable;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.MajorLogMsg;

import com.redknee.app.crm.notification.NotificationResultCallback;
import com.sun.mail.smtp.SMTPAddressFailedException;


/**
 * TODO: When upgraded to FW 6, this method can throw exception, hence we can implement
 * the retry a little more gracefully.  The caller will be able to decide how to handle failures.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class SMTPDeliveryCallbackMsgBox extends SMTPDeliveryMsgBox
{
    public static final Object ATTACHMENT_CTX_KEY = SMTPDeliveryCallbackMsgBox.class.getName() + "_ATTACHMENTS";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void send(Context ctx, Msg msg)
    {

        NotificationResultCallback callback = (NotificationResultCallback) ctx.get(NotificationResultCallback.class, ctx.get(EmailResultCallback.class));
        if(callback==null)
        {
            new InfoLogMsg(this,"cannot find callback object.  caller will not be notified of success/failure.",null).log(ctx);
        }

        try
        {
        	
        	/**
        	 * http://jira01/jira/browse/BSS-2147
        	 * Dunning Letters in HTML code after each BSS Restart
        	 * 
        	 * verified context template content type with actual framework set content type
        	 */
			List bodyList 		= (ArrayList) msg.getBody();
			EmailBody emailBody = (EmailBody) bodyList.get(0);

			MailContentHandler mailContentHandler 		= emailBody.getContentHandler();
			MailContentHandler ctxMailContentHandler 	= (MailContentHandler) ctx.get(MailContentHandler.class, null);

			if (mailContentHandler != null && ctxMailContentHandler != null	&& mailContentHandler instanceof TextMailContentHandler) 
			{
				String ctxContectType = null;
				String contectType = null;

				//get the framework set content handlet
				if (mailContentHandler instanceof HtmlMailContentHandler) 
				{
					contectType = ((HtmlMailContentHandler) mailContentHandler).getContentType();
					
				} else if (mailContentHandler instanceof TextMailContentHandler) 
				{
					contectType = ((TextMailContentHandler) mailContentHandler).getContentType();
				}

				//get the content type which is set on context : ctx.get(MailContentHandler.class, null);
				if (ctxMailContentHandler instanceof HtmlMailContentHandler) 
				{
					ctxContectType = ((HtmlMailContentHandler) ctxMailContentHandler).getContentType();
				} else if (ctxMailContentHandler instanceof TextMailContentHandler) 
				{
					ctxContectType = ((TextMailContentHandler) ctxMailContentHandler).getContentType();
				}
				
				//if framework content type is not match with context content type then set the context content type in email body
				if (ctxMailContentHandler != null && ctxContectType != null	&& !ctxContectType.equalsIgnoreCase(contectType)) 
				{

					TextMailContentHandler textMailContentHandler = new TextMailContentHandler();
					textMailContentHandler.setContentType(ctxContectType);
					mailContentHandler = textMailContentHandler;
					//set the context type in email body
					emailBody.setContentHandler(mailContentHandler);
					bodyList.set(0, emailBody);
					msg.setBody(bodyList);
				}
			}

            Multipart mp = new MimeMultipart();

            Map<String, DataSource> attachmentMap = (Map<String, DataSource>) ctx.get(ATTACHMENT_CTX_KEY);
            if (attachmentMap == null)
            {
                attachmentMap = new HashMap<String, DataSource>();   
            }
            
            for (Map.Entry<String, DataSource> attachment : attachmentMap.entrySet())
            {
                MimeBodyPart mbp = new MimeBodyPart();
                mbp.setFileName(attachment.getKey());
                mbp.setDataHandler(
                        new DataHandler(
                                attachment.getValue()));

                mp.addBodyPart(mbp);

            }
            Context subCtx = ctx.createSubContext();
            SMTPDeliveryMsgBox.setMultipartkey(subCtx,mp);            
            
            super.send(subCtx, msg);
            

            if (callback != null)
            {
                callback.reportSuccess(ctx);
            }
        }
        catch (MessagingException e)
        {
            new MajorLogMsg(this, e.getMessage() + "\nmsg ["+msg+"]", e).log(ctx);

            // FW 6 throws an exception here.  This class should be deleted when we move to FW 6.
            if (callback != null)
            {
                boolean recoverable = true;
                
                // Address errors are not recoverable
                recoverable &= !(e.getCause() instanceof SMTPAddressFailedException);
                
                callback.reportFailure(ctx, recoverable, e);
            }
        }
        catch (Exception e) //AddressException, AgentException
        {
            new MajorLogMsg(this, e.getMessage() + "\nmsg ["+msg+"]", e).log(ctx);

            // FW 6 throws an exception here.  This class should be deleted when we move to FW 6.
            if (callback != null)
            {
                callback.reportFailure(ctx, false, e);
            }
        }
    }

    public static class Factory extends ConstantMsgBoxFactory implements XCloneable
    {
        protected static MsgBoxFactory instance_ = null;
        public static MsgBoxFactory instance()
        {
            if (instance_ == null)
            {
                instance_ = new Factory();
            }
            return instance_;
        }
        
        public Factory()
        {
            super(new SMTPDeliveryCallbackMsgBox());
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
}
