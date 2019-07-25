package com.redknee.app.crm.bean.webcontrol;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.redknee.app.crm.bean.GSMPackageWebControl;
import com.redknee.app.crm.bean.GSMPackageXInfo;
import com.redknee.app.crm.bean.PackageStateEnum;
import com.redknee.app.crm.numbermgn.GenericPackage;
import com.redknee.app.crm.support.WebControlSupportHelper;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.EnumWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xhome.xenum.EnumCollection;


public class CustomGSMPackageWebControl extends GSMPackageWebControl
{
    @Override
    public WebControl getStateWebControl()
    {
        return CUSTOM_STATE_WC;
    }

    @Override
    public WebControl getDefaultResourceIDWebControl()
    {
        return CustomVSATPackageWebControl.CUSTOM_DEFAULT_RESOURCE_WC;
    }
    
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        GenericPackage pack = (GenericPackage) obj;
        if (ctx.getInt("MODE", DISPLAY_MODE) != CREATE_MODE && pack.getState() == PackageStateEnum.IN_USE)
        {
            WebControlSupportHelper.get(ctx).setPropertiesReadOnly(ctx, new PropertyInfo[]
            {
                GSMPackageXInfo.DEALER,
                GSMPackageXInfo.IMSI,
                GSMPackageXInfo.SERIAL_NO,
                GSMPackageXInfo.PIN1,
                GSMPackageXInfo.PUK1,
                GSMPackageXInfo.PIN2,
                GSMPackageXInfo.PUK2,
                GSMPackageXInfo.ADM1,
                GSMPackageXInfo.KI,
                GSMPackageXInfo.KAPPLI,
                GSMPackageXInfo.SERVICE_LOGIN1,
                GSMPackageXInfo.SERVICE_LOGIN2,
                GSMPackageXInfo.SERVICE_PASSWORD1,
                GSMPackageXInfo.SERVICE_PASSWORD2
            });
        }
        
        super.toWeb(ctx, out, name, obj);
    }


    public static final WebControl CUSTOM_STATE_WC = new EnumWebControl(PackageStateEnum.USER_TRANISTIONABLE_ENUM_COLL)
    {
        @Override
        public EnumCollection getEnumCollection(Context ctx)
        {
            GenericPackage myPackage = (GenericPackage) ctx.get(AbstractWebControl.BEAN);
            if (!PackageStateEnum.IN_USE.equals(myPackage.getState()))
            {
                List<Enum> stateList = new ArrayList<Enum>();
                
                for (Iterator i = enumc_.iterator(); i.hasNext(); )
                {
                    Enum e = (Enum) i.next();
                    if (!PackageStateEnum.IN_USE.equals(e))
                    {
                        stateList.add(e);
                    }   
                }
                
                Enum[] tempEnum = stateList.toArray(new Enum[0]);
                return new EnumCollection(tempEnum);
            }
            else
            {
                return enumc_;
            }
        }
    };



}
