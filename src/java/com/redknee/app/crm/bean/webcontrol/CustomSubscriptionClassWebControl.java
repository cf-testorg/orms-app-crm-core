package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.FinalWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.account.SubscriptionClassWebControl;
import com.redknee.app.crm.technology.TechnologyOnlyEnumWebControl;

public class CustomSubscriptionClassWebControl extends SubscriptionClassWebControl
{
    @Override
    public WebControl getTechnologyTypeWebControl()
    {
        return CUSTOM_TECHNOLOGY_TYPE_WC;
    }
    
    public static final WebControl CUSTOM_TECHNOLOGY_TYPE_WC = new FinalWebControl(new TechnologyOnlyEnumWebControl(false, true, true));
}
