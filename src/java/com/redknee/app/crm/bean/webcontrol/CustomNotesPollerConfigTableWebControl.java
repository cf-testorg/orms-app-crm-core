package com.redknee.app.crm.bean.webcontrol;

import com.redknee.app.crm.config.NotesPollerConfigTableWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;


public class CustomNotesPollerConfigTableWebControl extends NotesPollerConfigTableWebControl
{

    public WebControl getSubtype_defaultWebControl() { return CustomNotesPollerConfigWebControl.CUSTOM_SUBTYPE_WC; }

}
