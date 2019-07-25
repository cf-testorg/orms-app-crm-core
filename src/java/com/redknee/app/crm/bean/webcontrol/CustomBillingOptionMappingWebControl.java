package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.EnumWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xhome.xenum.Enum;

import com.redknee.app.crm.bean.BillingOptionMappingWebControl;
import com.redknee.app.crm.bean.MsisdnZonePrefixKeyWebControl;
import com.redknee.app.crm.bean.TaxAuthorityKeyWebControl;
import com.redknee.app.crm.bean.calldetail.CallTypeEnum;
import com.redknee.app.crm.xhome.CustomEnumCollection;


public class CustomBillingOptionMappingWebControl extends BillingOptionMappingWebControl
{

    @Override
    public WebControl getCallTypeWebControl()
    {
        return CUSTOM_CALL_TYPE_WC;
    }


    @Override
    public WebControl getZoneIdentifierWebControl()
    {
        return CUSTOM_ZONE_IDENTIFIER_WC;
    }


    @Override
    public WebControl getBillingCategoryWebControl()
    {
        return CUSTOM_BILLING_CATEGORY_WC;
    }


    @Override
    public WebControl getUsageTypeWebControl()
    {
        return CUSTOM_USAGE_TYPE_WC;
    }


    @Override
    public WebControl getTaxAuthorityWebControl()
    {
        return CUSTOM_TAX_AUTHORITY_WC;
    }


    @Override
    public WebControl getTaxAuthority2WebControl()
    {
        return CUSTOM_TAX_AUTHORITY_2_WC;
    }

    public static WebControl CUSTOM_ZONE_IDENTIFIER_WC = new MsisdnZonePrefixKeyWebControl();

    public static WebControl CUSTOM_BILLING_CATEGORY_WC = 
    	new com.redknee.app.crm.bean.calldetail.ActiveBillingCategoryKeyWebControl();

    public static WebControl CUSTOM_CALL_TYPE_WC = new EnumWebControl(
            new CustomEnumCollection(new Enum[]
                                              {
                    CallTypeEnum.ORIG, 
                    CallTypeEnum.TERM, 
                    CallTypeEnum.SMS,
                    /*
                     * CallTypeEnum.ROAMING_MO, 
                     * CallTypeEnum.ROAMING_MT, 
                     * CallTypeEnum.DROPPED_CALL,
                     */
                    CallTypeEnum.DOWNLOAD, 
                    CallTypeEnum.WEB, 
                    CallTypeEnum.WAP, 
                    CallTypeEnum.MMS, 
                    CallTypeEnum.ROAMING_SMS,
                    // CallTypeEnum.Vpn,
                    CallTypeEnum.ADVANCED_EVENT, 
                    CallTypeEnum.SDR,
                    CallTypeEnum.ROAMING_DOWNLOAD,
                    CallTypeEnum.ROAMING_WEB,
                    CallTypeEnum.ROAMING_WAP,
                    CallTypeEnum.ROAMING_ADVANCED_EVENT,
                    CallTypeEnum.ROAMING_SDR
                    
                                              }));
    
    public static WebControl CUSTOM_USAGE_TYPE_WC = new com.redknee.app.crm.bean.UsageTypeKeyWebControl();
    
    public static WebControl CUSTOM_TAX_AUTHORITY_WC = new com.redknee.framework.xhome.msp.SetSpidProxyWebControl(
            new TaxAuthorityKeyWebControl());
    
    public static WebControl CUSTOM_TAX_AUTHORITY_2_WC = new com.redknee.framework.xhome.msp.SetSpidProxyWebControl(
            new TaxAuthorityKeyWebControl());
}
