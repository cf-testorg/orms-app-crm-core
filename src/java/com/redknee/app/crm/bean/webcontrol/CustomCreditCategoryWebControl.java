package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.CreditCategoryWebControl;
import com.redknee.app.crm.billing.message.GenericBillingMessageTableWebControl;
import com.redknee.app.crm.web.control.CustomProvisioningCommandsWebControl;


public class CustomCreditCategoryWebControl extends CreditCategoryWebControl
{
    @Override
    public WebControl getWarningActionWebControl()
    {
        return CUSTOM_ACTION_WC;
    }

    @Override
    public WebControl getDunningActionWebControl()
    {
        return CUSTOM_ACTION_WC;
    }

    @Override
    public WebControl getInArrearsActionWebControl()
    {
        return CUSTOM_ACTION_WC;
    }
    @Override
    public WebControl getBillingMessagesWebControl()
    {
        return CUSTOM_BILLING_MESSAGES_WC;
    }


    public static final WebControl CUSTOM_ACTION_WC = new CustomProvisioningCommandsWebControl();
    public static final WebControl CUSTOM_BILLING_MESSAGES_WC = new GenericBillingMessageTableWebControl();
}
