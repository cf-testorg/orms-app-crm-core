package com.redknee.app.crm.bean.webcontrol;

import java.io.PrintWriter;

import com.redknee.app.crm.bean.PackageStateEnum;
import com.redknee.app.crm.bean.TDMAPackageWebControl;
import com.redknee.app.crm.bean.TDMAPackageXInfo;
import com.redknee.app.crm.numbermgn.GenericPackage;
import com.redknee.app.crm.support.WebControlSupportHelper;
import com.redknee.app.crm.technology.TechnologyEnum;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.EnumWebControl;
import com.redknee.framework.xhome.webcontrol.FinalWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xhome.xenum.EnumCollection;

public class CustomTDMAPackageWebControl extends TDMAPackageWebControl
{

    @Override
    public WebControl getStateWebControl()
    {
        return CustomGSMPackageWebControl.CUSTOM_STATE_WC;
    }
    
    public WebControl getDefaultResourceIDWebControl()
    {
        return CustomVSATPackageWebControl.CUSTOM_DEFAULT_RESOURCE_WC;
    }

    @Override
    public WebControl getTechnologyWebControl()
    {
        return TECH_ENUM_WEB_CONTROL;
    }
    
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        GenericPackage pack = (GenericPackage) obj;
        if (ctx.getInt("MODE", DISPLAY_MODE) != CREATE_MODE && pack.getState() == PackageStateEnum.IN_USE)
        {
            WebControlSupportHelper.get(ctx).setPropertiesReadOnly(ctx, new PropertyInfo[]
            {
                TDMAPackageXInfo.DEALER,
                TDMAPackageXInfo.MIN,
                TDMAPackageXInfo.ESN,
                TDMAPackageXInfo.SERIAL_NO,
                TDMAPackageXInfo.AUTH_KEY,
                TDMAPackageXInfo.SUBSIDY_KEY,
                TDMAPackageXInfo.MASS_SUBSIDY_KEY,
                TDMAPackageXInfo.SERVICE_LOGIN1,
                TDMAPackageXInfo.SERVICE_LOGIN2,
                TDMAPackageXInfo.SERVICE_PASSWORD1,
                TDMAPackageXInfo.SERVICE_PASSWORD2,
                TDMAPackageXInfo.CALLBACK_ID,
                TDMAPackageXInfo.RADIUS_PROFILE_NAME,
                TDMAPackageXInfo.PACKAGE_TYPE
            });
        }
        
        super.toWeb(ctx, out, name, obj);
    }

    private static final WebControl TECH_ENUM_WEB_CONTROL = new FinalWebControl(new EnumWebControl(
            new EnumCollection(new com.redknee.framework.xhome.xenum.Enum[]{TechnologyEnum.TDMA, TechnologyEnum.CDMA}),
            true));
}
