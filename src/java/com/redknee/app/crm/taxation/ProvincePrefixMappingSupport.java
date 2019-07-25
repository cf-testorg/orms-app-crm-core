package com.redknee.app.crm.taxation;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.ErIndexToProvinceConfig;
import com.redknee.app.crm.bean.ErIndexToProvinceConfigHome;
import com.redknee.app.crm.bean.ErIndexToProvinceConfigXInfo;
import com.redknee.app.crm.bean.EventTypeEnum;
import com.redknee.app.crm.bean.calldetail.CallDetail;
import com.redknee.app.crm.bean.calldetail.CallTypeEnum;


public class ProvincePrefixMappingSupport
{

    protected static ProvincePrefixMappingSupport instance_ = null;


    public static ProvincePrefixMappingSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new ProvincePrefixMappingSupport();
        }
        return instance_;
    }


    protected ProvincePrefixMappingSupport()
    {
    }


    /**
     * Gets the ER index to provinces mapping for the given call detail
     * 
     * @param context
     *            The operating context.
     * @param cd
     *            Call detail for which the configuration is to be returned
     * @param chargedParty
     *            Whether the call is of "O" - originating type or "T"- Terminating Type
     * 
     * @return the configuration
     */
    public static ErIndexToProvinceConfig getErToProvincesConfig(final Context ctx, CallDetail cd, String chargedParty)
    {
        CallTypeEnum callType = cd.getCallType();
        Home home = (Home) ctx.get(ErIndexToProvinceConfigHome.class);
        And condition = new And();
        condition.add(new EQ(ErIndexToProvinceConfigXInfo.SPID, cd.getSpid()));
        if (chargedParty != null && !chargedParty.trim().equals(""))
        {
            if (chargedParty.equalsIgnoreCase("O"))
            {
                if (callType.getIndex() == CallTypeEnum.ORIG_INDEX
                        || callType.getIndex() == CallTypeEnum.ROAMING_MO_INDEX)
                {
                    if(cd.getRedirectedAddress() != null && !cd.getRedirectedAddress().trim().equals(""))
                    {
                        condition.add(new EQ(ErIndexToProvinceConfigXInfo.EVENT_TYPE, EventTypeEnum.MO_FORWARD_VOICE));
                    }
                    else
                    {
                        condition.add(new EQ(ErIndexToProvinceConfigXInfo.EVENT_TYPE, EventTypeEnum.MO_VOICE));
                    }
                }
                else if (callType.getIndex() == CallTypeEnum.SMS_INDEX
                        || callType.getIndex() == CallTypeEnum.ROAMING_SMS_INDEX)
                {
                    condition.add(new EQ(ErIndexToProvinceConfigXInfo.EVENT_TYPE, EventTypeEnum.MO_SMS));
                }
            }
            else if (chargedParty.equalsIgnoreCase("T"))
            {
                if (callType.getIndex() == CallTypeEnum.TERM_INDEX
                        || callType.getIndex() == CallTypeEnum.ROAMING_MT_INDEX)
                {
                    condition.add(new EQ(ErIndexToProvinceConfigXInfo.EVENT_TYPE, EventTypeEnum.MT_VOICE));
                }
                else if (callType.getIndex() == CallTypeEnum.SMS_INDEX
                        || callType.getIndex() == CallTypeEnum.ROAMING_SMS_INDEX)
                {
                    condition.add(new EQ(ErIndexToProvinceConfigXInfo.EVENT_TYPE, EventTypeEnum.MT_SMS));
                }
            }
        }
        else if (callType.getIndex() == CallTypeEnum.MMS_INDEX)
        {
            condition.add(new EQ(ErIndexToProvinceConfigXInfo.EVENT_TYPE, EventTypeEnum.MMS));
        }
        else if (callType.getIndex() == CallTypeEnum.ADVANCED_EVENT_INDEX || callType.getIndex() == CallTypeEnum.DOWNLOAD_INDEX
                || callType.getIndex() == CallTypeEnum.WEB_INDEX || callType.getIndex() == CallTypeEnum.WAP_INDEX
                || callType.getIndex() ==  CallTypeEnum.SDR_INDEX)
                
        {
            condition.add(new EQ(ErIndexToProvinceConfigXInfo.EVENT_TYPE, EventTypeEnum.DATA));
        }
        
        ErIndexToProvinceConfig config = null;
        try
        {
            config = (ErIndexToProvinceConfig) home.find(ctx, condition);
        }
        catch (HomeException e)
        {
            LogSupport.minor(ctx, ProvincePrefixMappingSupport.class, "No configuration found for the supplied ER");
        }
        if (config != null)
        {
            return config;
        }
        return null;
    }
}
