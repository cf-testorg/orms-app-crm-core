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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.DataSource;
import javax.mail.internet.InternetAddress;

import com.redknee.framework.msg.Email;
import com.redknee.framework.msg.EmailConfig;
import com.redknee.framework.msg.EmailTemplate;
import com.redknee.framework.msg.MailContentHandler;
import com.redknee.framework.msg.Msg;
import com.redknee.framework.msg.MsgBox;
import com.redknee.framework.xhome.auth.bean.User;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextFactory;
import com.redknee.framework.xhome.holder.StringHolder;
import com.redknee.framework.xlog.log.InfoLogMsg;

import com.redknee.app.crm.delivery.email.MsgBoxFactory;
import com.redknee.app.crm.delivery.email.SMTPDeliveryCallbackMsgBox;
import com.redknee.app.crm.notification.EmailAddresses;
import com.redknee.app.crm.notification.LoggingNotificationResultCallback;
import com.redknee.app.crm.notification.NotificationResultCallback;
import com.redknee.app.crm.notification.NullNotificationResultCallback;
import com.redknee.app.crm.notification.RecipientInfo;
import com.redknee.app.crm.notification.message.EmailNotificationMessage;
import com.redknee.app.crm.notification.message.NotificationMessage;


/**
 * This service delivers email notification messages to an SMTP server.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class EmailDeliveryService extends AbstractEmailDeliveryService
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
            if (msg instanceof EmailNotificationMessage)
            {
                EmailNotificationMessage eMsg = (EmailNotificationMessage) msg;

                String fromAddress = getFromAddress(ctx, eMsg);
                String replyToAddress = getReplyToAddress(ctx, eMsg);
                
                EmailAddresses addresses = recipient.getEmailTo();
                boolean validAddresses = (addresses != null);
                if (validAddresses)
                {
                    List toList = addresses.getTo();
                    List ccList = addresses.getCc();
                    List bccList = addresses.getBcc();
                    validAddresses &= 
                        (toList != null && toList.size() > 0)
                            || (ccList != null && ccList.size() > 0)
                            || (bccList != null && bccList.size() > 0);
                }
                
                MailContentHandler contentHandler = getMailContentHandler(ctx, eMsg);
                
                if (fromAddress != null
                        && contentHandler != null
                        && validAddresses)
                {
                    EmailTemplate template = new EmailTemplate();
                    template.setContentHandler(contentHandler);
                    template.setSubject(eMsg.getSubject());
                    template.setEmail(eMsg.getBody());

                    Map<String, DataSource> attachments = eMsg.getAttachments(ctx);
                    
                    sendEmailMessage(ctx, fromAddress, replyToAddress, addresses, template, attachments, callback);
                    
                    // Don't report success to the callback.  We must rely on the MsgBox implementation for this
                    // in FW 5.7 because FW's email delivery stuff doesn't throw exceptions on failure.  In FW 6
                    // this can be revisited.
                    // callback.reportSuccess(ctx);
                }
                else if (fromAddress == null)
                {
                    callback.reportFailure(ctx, false, "No 'from' address could be found in message or delivery service.  No message will be delivered.");
                }
                else if (contentHandler == null)
                {
                    callback.reportFailure(ctx, false, "No content handler could be found in message or delivery service.  No message will be delivered.");
                }
                else if (!validAddresses)
                {
                    callback.reportFailure(ctx, false, "Recipient information contains no To/CC/BCC addresses.  No message will be delivered.");
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

    protected void sendEmailMessage(Context ctx, 
            String from, String replyTo, 
            EmailAddresses address, 
            EmailTemplate template, 
            Map<String, DataSource> attachments, 
            NotificationResultCallback callback) throws MessageDeliveryException
    {
        ctx = ctx.createSubContext();
        
        // Install FW dependencies
        ctx.put(MailContentHandler.class, template.getContentHandler());
        ctx.put(EmailConfig.class, getSmtpConfig());
        
        MsgBox msgBox = getMsgBox(ctx);
        if (msgBox != null)
        {
            ctx.put(Email.APPLICATION_OUTBOX, msgBox);
        }

        if (attachments != null && attachments.size() > 0)
        {
            // Add a bean factory that initializes the attachment fields of new Msg instances
            // for MsgBox implementations that do not support multiple attachments.
            XBeans.putBeanFactory(ctx, Msg.class, new AttachmentInitializingMsgFactory(attachments));

            if (attachments.size() > 1)
            {
                // Add attachment map for MsgBox implementations that support multiple attachments
                ctx.put(SMTPDeliveryCallbackMsgBox.ATTACHMENT_CTX_KEY, attachments);
            }
        }
        
        // Add callback for MsgBox that may use it for success/failure reporting
        ctx.put(NotificationResultCallback.class, callback);

        List<String> to = unwrapAddressList(address.getTo());
        List<String> cc = unwrapAddressList(address.getCc());
        List<String> bcc = unwrapAddressList(address.getBcc());
        
        Email.sendEmail(ctx, 
                from, 
                to, cc, bcc, 
                template.getSubject(), template.getEmail());
    }

    protected List<String> unwrapAddressList(List<StringHolder> addressHolderList)
    {
        List<String> addresses = new ArrayList<String>();
        if (addressHolderList != null)
        {
            for (StringHolder holder : addressHolderList)
            {
                if (holder != null && holder.getValue() != null && holder.getValue().trim().length() > 0)
                {
                    addresses.add(holder.getValue());
                }
            }
        }
        return addresses;
    }

    protected String getFromAddress(Context ctx, EmailNotificationMessage eMsg)
    {
        String fromAddress = eMsg.getFromAddress();
        if (!isAddressValid(ctx, fromAddress))
        {
            fromAddress = getDefaultFrom();
        }
        if (!isAddressValid(ctx, fromAddress)
                && ctx.has(EmailConfig.class))
        {
            fromAddress = ((EmailConfig) ctx.get(EmailConfig.class)).getReplyToAddress();
        }
        if (!isAddressValid(ctx, fromAddress)
                && ctx.has(Principal.class))
        {
            fromAddress = ((User) ctx.get(Principal.class)).getEmail();
        }
        return fromAddress;
    }

    protected String getReplyToAddress(Context ctx, EmailNotificationMessage eMsg)
    {
        String replyToAddress = eMsg.getReplyToAddress();
        if (!isAddressValid(ctx, replyToAddress))
        {
            EmailConfig smtpConfig = getSmtpConfig();
            if (smtpConfig != null)
            {
                replyToAddress = smtpConfig.getReplyToAddress();
            }
        }
        return replyToAddress;
    }

    protected MailContentHandler getMailContentHandler(Context ctx, EmailNotificationMessage eMsg)
    {
        MailContentHandler contentHandler = eMsg.getContentHandler();
        if (contentHandler == null)
        {
            contentHandler = getDefaultContentHandler();
        }
        return contentHandler;
    }
    
    protected MsgBox getMsgBox(Context ctx)
    {
        MsgBox result = null;
        
        MsgBoxFactory generator = getMsgBoxGenerator();
        if (generator != null)
        {
            result = generator.getMessageBox(ctx);
        }
        
        return result;
    }

    protected boolean isAddressValid(Context ctx, String address)
    {
        if (address != null
                && address.length() > 0)
        {
            try
            {
                new InternetAddress(address).validate();
            }
            catch (Exception e)
            {
                new InfoLogMsg(this, "E-Mail address '" + address + "' invalid.", e).log(ctx);
                return false;
            }
        }
        else
        {
            return false;
        }
        
        return true;
    }

    private static final class AttachmentInitializingMsgFactory implements ContextFactory
    {
        private AttachmentInitializingMsgFactory(Map<String, DataSource> attachments)
        {
            this.attachments_ = attachments;
        }

        public Object create(Context ctx)
        {
            ctx = ctx.createSubContext();
            
            // Null out the bean factory to avoid stack overflow
            XBeans.putBeanFactory(ctx, Msg.class, null);
            
            Msg msg = Msg.instantiate(ctx);
            
            if (attachments_ != null && attachments_.size() > 0)
            {
                Entry<String, DataSource> attachment = attachments_.entrySet().iterator().next();
                msg.setAttachmentName(attachment.getKey());
                msg.setAttachmentSource(attachment.getValue());
            }
            
            return msg;
        }

        private final Map<String, DataSource> attachments_;
    }
}
