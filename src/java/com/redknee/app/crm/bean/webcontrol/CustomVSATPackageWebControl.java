package com.redknee.app.crm.bean.webcontrol;

import java.io.PrintWriter;

import com.redknee.app.crm.bean.PackageStateEnum;
import com.redknee.app.crm.bean.VSATPackageWebControl;
import com.redknee.app.crm.bean.VSATPackageXInfo;
import com.redknee.app.crm.numbermgn.GenericPackage;
import com.redknee.app.crm.support.WebControlSupportHelper;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.ReadOnlyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;


public class CustomVSATPackageWebControl extends VSATPackageWebControl
{

    @Override
    public WebControl getStateWebControl()
    {
        return CustomGSMPackageWebControl.CUSTOM_STATE_WC;
    }


    @Override
    public WebControl getDefaultResourceIDWebControl()
    {
        return CUSTOM_DEFAULT_RESOURCE_WC;
    }
    
    
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        GenericPackage pack = (GenericPackage) obj;
        if (ctx.getInt("MODE", DISPLAY_MODE) != CREATE_MODE && pack.getState() == PackageStateEnum.IN_USE)
        {
            WebControlSupportHelper.get(ctx).setPropertiesReadOnly(ctx, new PropertyInfo[]
            {
                VSATPackageXInfo.DEALER,
                VSATPackageXInfo.VSAT_ID,
                VSATPackageXInfo.CHANNEL,
                VSATPackageXInfo.PORT
            });
        }

        super.toWeb(ctx, out, name, obj);
    }
    
    public static final WebControl CUSTOM_STATE_WC = CustomGSMPackageWebControl.CUSTOM_STATE_WC;  
    
    public static final WebControl CUSTOM_DEFAULT_RESOURCE_WC = new ReadOnlyWebControl(
            new com.redknee.app.crm.resource.ResourceDeviceKeyWebControl(0, false, ""));

}
