package com.redknee.app.crm.bean.webcontrol;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletRequest;

import com.redknee.app.crm.extension.service.BlackberryServiceExtensionTableWebControl;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.service.blackberry.model.ServiceMappingRow;
import com.redknee.service.blackberry.model.ServiceMappingRowTableWebControl;



public class CustomBlackberryServiceExtensionTableWebControl extends BlackberryServiceExtensionTableWebControl
{
    @Override
    public WebControl getBlackberryServicesWebControl()
    {
        return CustomBlackberryServiceExtensionWebControl.CUSTOM_BLACKBERRY_SERVICE_WC;
    }
}