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

import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.notification.message.NotificationMessage;
import com.redknee.app.crm.notification.template.NotificationTemplate;


/**
 * The message generator generates notification messages from templates. The
 * implementation uses the template to determine the contents and type of message. All
 * implementation-specific parameters are to be assumed to be passed in the Context.
 * BeanLoaderSupport can also be used in the implementation.
 * 
 * It is assumed that the resulting message contains no template parameters, therefore if
 * it is generated from a template that had dynamic parameters (such as %BAN%) then the
 * generator of the message is responsible for performing this substitution.
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public interface MessageGenerator
{

    public NotificationMessage generate(Context ctx, NotificationTemplate template, KeyValueFeatureEnum... features) throws MessageGenerationException;
}
