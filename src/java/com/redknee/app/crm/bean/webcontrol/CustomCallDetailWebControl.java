package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.EnumWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xhome.webcontrol.XTestIgnoreWebControl;
import com.redknee.framework.xhome.xenum.Enum;

import com.redknee.app.crm.bean.calldetail.CallDetailWebControl;
import com.redknee.app.crm.bean.calldetail.CallTypeEnum;
import com.redknee.app.crm.web.control.TimeWebControl;
import com.redknee.app.crm.xhome.CustomEnumCollection;


public class CustomCallDetailWebControl extends CallDetailWebControl
{
    @Override
    public WebControl getCallTypeWebControl()
    {
        return CUSTOM_CALL_TYPE_WC;
    }


    @Override
    public WebControl getBillingCategoryWebControl()
    {
        return CUSTOM_BILLING_CATEGORY_WC;
    }


    @Override
    public WebControl getDurationWebControl()
    {
        return CUSTOM_DURATION_WC;
    }

    public static final WebControl CUSTOM_BILLING_CATEGORY_WC = 
    	new com.redknee.app.crm.bean.calldetail.ActiveBillingCategoryKeyWebControl();

    public static final WebControl CUSTOM_CALL_TYPE_WC = new EnumWebControl(
            new CustomEnumCollection(new Enum[]
                                              {
                    CallTypeEnum.ORIG, 
                    CallTypeEnum.TERM, 
                    CallTypeEnum.SMS, 
                    CallTypeEnum.ROAMING_MO, 
                    CallTypeEnum.ROAMING_MT,
                    CallTypeEnum.ROAMING_SMS, 
                    CallTypeEnum.DROPPED_CALL, 
                    CallTypeEnum.DOWNLOAD, 
                    CallTypeEnum.WEB,
                    CallTypeEnum.WAP, 
                    CallTypeEnum.MMS, 
                    CallTypeEnum.ADVANCED_EVENT, 
                    CallTypeEnum.SDR
                                              }));

    public static final WebControl CUSTOM_DURATION_WC = new XTestIgnoreWebControl(TimeWebControl.instance());
}
