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

import java.util.Map;

import javax.activation.DataSource;

import com.redknee.framework.core.bean.Script;
import com.redknee.framework.msg.MailContentHandler;
import com.redknee.framework.xhome.context.Context;

/**
 * An email notification message contains a textual representation of an email message's
 * subject and body. It also contains attachment data and a content handler used for
 * formatting the message according to the desired content type (e.g. HTML, Plain Text,
 * etc). The content handler is optional as the delivery service will have a default one
 * configured.
 * 
 * With respect to subject and body of message, the email message differs from the SMS
 * message in that template parameters can exist in the message body. The contents of
 * getSubstitutionProperties() will be used when sending the email message to substitute
 * some variables. That being said, there is nothing preventing the generator of the
 * message from pre-processing the message and manually performing substitution.
 * 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public interface EmailNotificationMessage extends NotificationMessage
{
    public String getFromAddress();
    public void setFromAddress(String from);
    
    public String getReplyToAddress();
    public void setReplyToAddress(String replyTo);
    
    public String getSubject();
    public void setSubject(String subject);
    
    public String getBody();
    public void setBody(String body);
    
    public Map<String, DataSource> getAttachments(Context ctx);
    public void addAttachment(String name, Script script);
    
    public MailContentHandler getContentHandler();
    public void setContentHandler(MailContentHandler handler);
}
