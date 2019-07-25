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
package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.EnumIndexOptionalWebControl;
import com.redknee.framework.xhome.webcontrol.EnumIndexWebControl;
import com.redknee.framework.xhome.webcontrol.FinalWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.SubscriberStateEnum;
import com.redknee.app.crm.delivery.email.CRMEmailTemplateWebControl;
import com.redknee.app.crm.delivery.email.EmailTemplateSubscriptionStateProxyWebControl;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CustomCRMEmailTemplateWebControl extends CRMEmailTemplateWebControl
{

    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getPreviousStateWebControl()
    {
        return CUSTOM_PREVIOUS_STATE_WC;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getNewStateWebControl()
    {
        return CUSTOM_NEW_STATE_WC;
    }
    
    public static final WebControl CUSTOM_PREVIOUS_STATE_WC = new FinalWebControl(new EmailTemplateSubscriptionStateProxyWebControl(new EnumIndexOptionalWebControl(SubscriberStateEnum.COLLECTION, "--Any--")));
    public static final WebControl CUSTOM_NEW_STATE_WC = new FinalWebControl(new EmailTemplateSubscriptionStateProxyWebControl(new EnumIndexWebControl(SubscriberStateEnum.COLLECTION)));
}
