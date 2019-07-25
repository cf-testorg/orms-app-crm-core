package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.BillCycleWebControl;
import com.redknee.app.crm.billing.message.GenericBillingMessageTableWebControl;


public class CustomBillCycleWebControl extends BillCycleWebControl
{
    @Override
    public WebControl getBillingMessagesWebControl()
    {
        return CUSTOM_BILLING_MESSAGE_WC;
    }
    
    public static final WebControl CUSTOM_BILLING_MESSAGE_WC = new GenericBillingMessageTableWebControl();
    
}
