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

import com.redknee.app.crm.factory.DynamicValueFacetFactory;


/**
 * This facet factory can map notification template or notification message
 * classes to message generators.
 * 
 * If requesting a message generator for a template, the resulting generator
 * can be assumed to generate messages from that template.  If requesting a
 * message generator for a notification message, the resulting generator can
 * be assumed to generate messages of that type.  In the latter case, it is
 * possible that the generator returned can not handle a particular template
 * type and this is a limitation beyond the control of this functionality.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class MessageGeneratorFacetFactory extends DynamicValueFacetFactory<MessageGenerator>
{
    public MessageGeneratorFacetFactory()
    {
        super(MessageGenerator.class);
    }
}