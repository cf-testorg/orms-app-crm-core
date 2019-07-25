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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.notification.message.BasicSmsNotification;
import com.redknee.app.crm.notification.message.NotificationMessage;
import com.redknee.app.crm.notification.message.SmsNotificationMessage;
import com.redknee.app.crm.notification.template.NotificationTemplate;
import com.redknee.app.crm.notification.template.SmsNotificationTemplate;


/**
 * This generator translates a SimpleNotificationTemplate into a BasicSmsNotification. If
 * the template is not an instance of SmsNotificationTemplate or no 'from' string is
 * available in the template, then it will assume a default value stored in the generator
 * itself.
 * 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class SimpleSmsGenerator extends AbstractSimpleSmsGenerator
{
    /**
     * {@inheritDoc}
     */
    public NotificationMessage generate(Context ctx, NotificationTemplate template, KeyValueFeatureEnum... features) throws MessageGenerationException
    {
        SmsNotificationMessage message = instantiateMessage(ctx, template, features);
        
        setMessageSender(ctx, template, message, features);

        setMessageContent(ctx, template, message, features);

        return message;
    }

    protected SmsNotificationMessage instantiateMessage(Context ctx, NotificationTemplate template, KeyValueFeatureEnum... features)
            throws MessageGenerationException
    {
        return instantiateMessage(ctx, SmsNotificationMessage.class, BasicSmsNotification.class, template);
    }

    protected void setMessageSender(Context ctx, NotificationTemplate template,
            SmsNotificationMessage message, KeyValueFeatureEnum... features)
    {
        try
        {
            String from = getDefaultFrom();
            if (template instanceof SmsNotificationTemplate)
            {
                SmsNotificationTemplate smsTemplate = (SmsNotificationTemplate) template;
                if (smsTemplate.getFrom() != null && smsTemplate.getFrom().trim().length() > 0)
                {
                    from = smsTemplate.getFrom();
                }
            }
            message.setFrom(from);
        }
        catch (Exception e)
        {
            new MinorLogMsg(this, "Error setting source address in SMS message.  Message will be passed to delivery service with no sender information.", e).log(ctx);
        }
    }

    protected void setMessageContent(Context ctx, NotificationTemplate template, SmsNotificationMessage message, KeyValueFeatureEnum... features)
            throws MessageGenerationException
    {
        try
        {
            String content = template.getTemplateMessage(ctx);

            content = replaceVariables(ctx, content, features);
            
            message.setMessage(content);
        }
        catch (Exception e)
        {
            throw new MessageGenerationException("Error generating SMS message content.", e);
        }
    }

}
