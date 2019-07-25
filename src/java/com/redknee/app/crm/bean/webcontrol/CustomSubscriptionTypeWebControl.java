package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.EnumIndexWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.account.SubscriptionTypeEnum;
import com.redknee.app.crm.bean.account.SubscriptionTypeWebControl;
import com.redknee.app.crm.filter.SubscriptionTypeEnumLicensePredicate;


public class CustomSubscriptionTypeWebControl extends SubscriptionTypeWebControl
{
    @Override
    public WebControl getTypeWebControl()
    {
        return CUSTOM_TYPE_WC;
    }
    
    public static final WebControl CUSTOM_TYPE_WC = (new EnumIndexWebControl(SubscriptionTypeEnum.COLLECTION)).setPredicate(new SubscriptionTypeEnumLicensePredicate());

}
