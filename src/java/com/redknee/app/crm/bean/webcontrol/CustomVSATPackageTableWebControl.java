package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.VSATPackageTableWebControl;


public class CustomVSATPackageTableWebControl extends VSATPackageTableWebControl
{
    @Override
    public WebControl getStateWebControl()
    {
        return CustomVSATPackageWebControl.CUSTOM_STATE_WC;
    }
}
