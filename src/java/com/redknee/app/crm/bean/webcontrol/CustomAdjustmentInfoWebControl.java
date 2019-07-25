package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.msp.SetSpidProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.AdjustmentInfoWebControl;
import com.redknee.app.crm.web.control.CustomizedGlcodeKeyWebControl;


public class CustomAdjustmentInfoWebControl extends AdjustmentInfoWebControl
{
    @Override
    public WebControl getGLCodeWebControl()
    {
        return CUSTOM_GL_CODE_WC;
    }
    
    public static final WebControl CUSTOM_GL_CODE_WC = new SetSpidProxyWebControl(new CustomizedGlcodeKeyWebControl());
}
