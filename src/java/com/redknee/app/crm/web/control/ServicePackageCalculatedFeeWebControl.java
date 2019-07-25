package com.redknee.app.crm.web.control;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.ViewModeEnum;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.ServicePackageFee;
import com.redknee.app.crm.bean.ServicePackageFeeTableWebControl;
import com.redknee.app.crm.bean.ServicePackageHome;
import com.redknee.app.crm.bean.core.ServicePackage;

public class ServicePackageCalculatedFeeWebControl extends ServicePackageFeeTableWebControl
{

    @Override
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        if (obj == null)
        {
            super.toWeb(ctx, out, name, obj);

            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "obj is null", null).log(ctx);
            }

            return;
        }

        List fees = (List) obj;
        Home home = (Home) ctx.get(ServicePackageHome.class);

        for (Iterator i = fees.iterator(); i.hasNext();)
        {
            ServicePackageFee fee = (ServicePackageFee) i.next();
            try
            {
                ServicePackage pack=(ServicePackage) home.find(ctx, Integer.valueOf(fee.getPackageId()));
                if(pack!=null)
                {
                    pack.updateTotalCharge(ctx);
                    fee.setFee(pack.getTotalCharge());
                }
            }
            catch (HomeException e)
            {
                if(LogSupport.isDebugEnabled(ctx))
                {
                    new DebugLogMsg(this,e.getMessage(),e).log(ctx);
                }
            }
        }

        AbstractWebControl.setMode(ctx, "ServicePackageFee.fee", ViewModeEnum.READ_ONLY);

        super.toWeb(ctx, out, name, obj);
    }

}
