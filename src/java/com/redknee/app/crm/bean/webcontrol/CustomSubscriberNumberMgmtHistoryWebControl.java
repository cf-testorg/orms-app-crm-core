package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.DateTimeWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xhome.webcontrol.XTestIgnoreWebControl;

import com.redknee.app.crm.numbermgn.SubscriberNumberMgmtHistoryWebControl;
import com.redknee.app.crm.web.control.HideMaxDateWebControl;


public class CustomSubscriberNumberMgmtHistoryWebControl extends SubscriberNumberMgmtHistoryWebControl
{
    @Override
    public WebControl getEndTimestampWebControl()
    {
        return CUSTOM_END_TIMESTAMP_WC;
    }
    
    public static final WebControl CUSTOM_END_TIMESTAMP_WC = new XTestIgnoreWebControl(new HideMaxDateWebControl(DateTimeWebControl.instance()));

}
