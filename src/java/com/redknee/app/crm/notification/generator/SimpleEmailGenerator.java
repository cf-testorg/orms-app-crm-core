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

import com.redknee.framework.msg.MailContentHandler;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.notification.message.BasicEmailNotification;
import com.redknee.app.crm.notification.message.EmailNotificationMessage;
import com.redknee.app.crm.notification.message.NotificationMessage;
import com.redknee.app.crm.notification.template.EmailNotificationTemplate;
import com.redknee.app.crm.notification.template.NotificationTemplate;


/**
 * This generator translates a SimpleNotificationTemplates into a BasicEmailNotification.
 * If the template is not an instance of EmailNotificationTemplate or if the 'from' or
 * 'reply-to' strings are not available in the template, then it will assume default
 * value(s) stored in the generator itself. The same is true for the content handlers. If
 * the subject is not provided for either reason, then the resulting email notification
 * will have no subject.
 * 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class SimpleEmailGenerator extends AbstractSimpleEmailGenerator
{
    /**
     * {@inheritDoc}
     */
    public NotificationMessage generate(Context ctx, NotificationTemplate template, KeyValueFeatureEnum... features) throws MessageGenerationException
    {
        EmailNotificationMessage message = instantiateMessage(ctx, template, features);
        
        setMessageSender(ctx, template, message, features);

        setMessageContent(ctx, template, message, features);
        
        addAttachments(ctx, template, message, features);

        return message;
    }

    protected EmailNotificationMessage instantiateMessage(Context ctx, NotificationTemplate template, KeyValueFeatureEnum... features) throws MessageGenerationException
    {
        return instantiateMessage(ctx, EmailNotificationMessage.class, BasicEmailNotification.class, template);
    }

    protected void setMessageSender(Context ctx, NotificationTemplate template, EmailNotificationMessage message, KeyValueFeatureEnum... features)
    {
        try
        {
            String from = getDefaultFrom();
            String replyTo = getDefaultReplyTo();
            MailContentHandler contentHandler = getDefaultContentHandler();
            if (template instanceof EmailNotificationTemplate)
            {
                EmailNotificationTemplate emailTemplate = (EmailNotificationTemplate) template;
                if (emailTemplate.getFrom() != null && emailTemplate.getFrom().trim().length() > 0)
                {
                    from = emailTemplate.getFrom();
                }
                if (emailTemplate.getReplyTo() != null && emailTemplate.getReplyTo().trim().length() > 0)
                {
                    replyTo = emailTemplate.getReplyTo();
                }
                if (emailTemplate.getContentHandler() != null)
                {
                    contentHandler = emailTemplate.getContentHandler();
                }
            }
            if (contentHandler != null)
            {
                message.setContentHandler(contentHandler);
            }
            message.setFromAddress(from);
            message.setReplyToAddress(replyTo);
        }
        catch (Exception e)
        {
            new MinorLogMsg(this, "Error setting source address in Email message.  Message will be passed to delivery service with no sender information.", e).log(ctx);
        }
    }

    protected void setMessageContent(Context ctx, NotificationTemplate template, EmailNotificationMessage message, KeyValueFeatureEnum... features) throws MessageGenerationException
    {
        try
        {
            if (template instanceof EmailNotificationTemplate)
            {
                EmailNotificationTemplate emailTemplate = (EmailNotificationTemplate) template;
                
                String subject = emailTemplate.getSubject();
                
                subject = replaceVariables(ctx, subject, features);
                
                message.setSubject(subject);
            }
            
            String body = template.getTemplateMessage(ctx);

            body = replaceVariables(ctx, body, features);
            
            message.setBody(body);
        }
        catch (Exception e)
        {
            throw new MessageGenerationException("Error generating Email message content.", e);
        }
    }


    protected void addAttachments(Context ctx, NotificationTemplate template, EmailNotificationMessage message, KeyValueFeatureEnum... features)
    {
        // Must be overridden by sub-classes if attachments are required.
    }
}
