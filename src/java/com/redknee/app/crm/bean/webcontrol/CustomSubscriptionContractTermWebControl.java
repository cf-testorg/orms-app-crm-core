/*
 * 
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.bean.webcontrol;

import com.redknee.app.crm.billing.message.GenericBillingMessageTableWebControl;
import com.redknee.app.crm.contract.SubscriptionContractTermWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;


/**
 * 
 * @author vijay.gote
 * @since 9.5
 */
public class CustomSubscriptionContractTermWebControl extends SubscriptionContractTermWebControl
{

    public WebControl getBillingMessagesWebControl()
    {
        return CUSTOM_BILLING_MESSAGE_WC;
    }

    public static final WebControl CUSTOM_BILLING_MESSAGE_WC = new GenericBillingMessageTableWebControl();
}
