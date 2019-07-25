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

import java.util.HashMap;
import java.util.Map;

import javax.activation.DataSource;

import com.redknee.framework.core.bean.Script;
import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.support.FrameworkSupportHelper;



/**
 * This message contains a textual subject and body. The email notification also contains
 * optional 'from' and 'reply-to' e-mail addresses.
 * 
 * The notification can optionally contain attachments in the form of a map from strings
 * (attachment names) to Script instances (the script name must be the same as the
 * attachment name). The script must return an instance of DataSource, and this will most
 * commonly be an instance of FileDataSource).
 * 
 * The notification can optionally specify a content handler which would used by the
 * delivery service when sending the message. Common content handlers are
 * HtmlMailContentHandler and TextMailContentHandler.
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class BasicEmailNotification extends AbstractBasicEmailNotification
{
    /**
     * {@inheritDoc}
     */
    public Map<String, DataSource> getAttachments(Context ctx)
    {
        initAttachments(ctx);
        return attachmentMap_;
    }

    /**
     * {@inheritDoc}
     */
    public void addAttachment(String name, Script script)
    {
        script.setName(name);
        getAttachmentGenerators().put(name, script);
    }


    protected void initAttachments(Context ctx)
    {
        if (attachmentMap_ == null)
        {
            attachmentMap_ = new HashMap<String, DataSource>();
        }
        
        Map<String, Script> attachmentGenerators = getAttachmentGenerators();
        for (Script script : attachmentGenerators.values())
        {
            if (attachmentMap_.containsKey(script.getName()))
            {
                continue;
            }
            
            DataSource source = getDataSourceFromScript(ctx, script);
            if (source != null)
            {
                attachmentMap_.put(script.getName(), source);
            }
        }
    }
    
    protected DataSource getDataSourceFromScript(Context ctx, Script script)
    {
        return FrameworkSupportHelper.get(ctx).getObjectFromScript(ctx, DataSource.class, script);
    }

    protected transient Map<String, DataSource> attachmentMap_ = null;
}
