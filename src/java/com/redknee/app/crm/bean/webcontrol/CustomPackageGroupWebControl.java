package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.EnumWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.PackageGroupWebControl;
import com.redknee.app.crm.filter.TechnologyTypeEnumPredicate;


public class CustomPackageGroupWebControl extends PackageGroupWebControl
{
    @Override
    public WebControl getTechnologyWebControl()
    {
        return CUSTOM_TECHNOLOGY_WC;
    }
    
    public static final WebControl CUSTOM_TECHNOLOGY_WC = ((EnumWebControl) PackageGroupWebControl.technology_wc).setPredicate(new TechnologyTypeEnumPredicate());
}
