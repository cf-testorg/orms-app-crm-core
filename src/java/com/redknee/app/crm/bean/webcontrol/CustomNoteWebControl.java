package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.NoteWebControl;
import com.redknee.app.crm.web.control.NoteSubTypeWebControl;


public class CustomNoteWebControl extends NoteWebControl
{
    @Override
    public WebControl getSubTypeWebControl()
    {
        return CUSTOM_SUB_TYPE_WC;
    }
    
    public static WebControl CUSTOM_SUB_TYPE_WC = new NoteSubTypeWebControl();
}
