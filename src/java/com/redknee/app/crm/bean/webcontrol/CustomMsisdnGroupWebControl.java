package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.FinalWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.MsisdnGroupWebControl;
import com.redknee.app.crm.technology.TechnologyOnlyEnumWebControl;


public class CustomMsisdnGroupWebControl extends  MsisdnGroupWebControl
{
    @Override
    public WebControl getTechnologyWebControl()
    {
        return CUSTOM_TECHNOLOGY_WC;
    }
    
    public static final WebControl CUSTOM_TECHNOLOGY_WC = new FinalWebControl(new TechnologyOnlyEnumWebControl());
}
