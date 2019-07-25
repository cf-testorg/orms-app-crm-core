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
package com.redknee.app.crm.billing.message;

import java.util.List;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.msp.SpidAware;

import com.redknee.app.crm.bean.IdentifierAware;
import com.redknee.app.crm.support.messages.MessageConfigurationSupport;

/**
 * This interface is used to support Multi-language Billing Message.  
 * There are several beans in CRM that can configure Billing Messages and they should all 
 * support messages in Multiple Languages.
 * 
 * This interface will allow them to use GenericBillingMessageTableWebControl in the bean display
 * to configure the BillingMessages at the same time as the bean itself.
 * 
 * @author angie.li@redknee.com
 *
 */
public interface BillingMessageAware extends SpidAware, IdentifierAware
{
    public List getBillingMessages();
    public void setBillingMessages(List list);
    public MessageConfigurationSupport getConfigurationSupport(Context ctx);
    void saveBillingMessages(Context ctx);
}
