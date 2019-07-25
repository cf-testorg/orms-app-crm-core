package com.redknee.app.crm.bean.webcontrol;

import com.redknee.app.crm.config.NotesPollerConfigWebControl;
import com.redknee.app.crm.web.control.NoteSubTypeWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;


public class CustomNotesPollerConfigWebControl extends NotesPollerConfigWebControl
{

    public WebControl getSubtype_defaultWebControl() { return CUSTOM_SUBTYPE_WC; }

    
    public static final WebControl CUSTOM_SUBTYPE_WC = new NoteSubTypeWebControl();
}
