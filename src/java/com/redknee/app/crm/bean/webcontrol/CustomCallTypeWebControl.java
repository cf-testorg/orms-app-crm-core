package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.CallTypeWebControl;
import com.redknee.app.crm.web.control.CustomizedGlcodeKeyWebControl;


public class CustomCallTypeWebControl extends CallTypeWebControl
{
    @Override
    public WebControl getGLCodeWebControl()
    {
        return CUSTOM_GL_CODE_WC;
    }
 
    public static final WebControl CUSTOM_GL_CODE_WC = new CustomizedGlcodeKeyWebControl();
}
