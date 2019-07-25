package com.redknee.app.crm.bundle.web;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bundle.BalanceApplication;
import com.redknee.app.crm.bundle.BalanceApplicationHome;
import com.redknee.app.crm.bundle.ServiceBalanceLimit;
import com.redknee.app.crm.bundle.UnitTypeEnum;

/**
 * This webcontrol converts the input based on the UnitType associated with the selected
 * bundle category of the bundle profile.
 *
 * @author Candy Wong
 */
public class MapApplicationUnitWebControl
        extends MapUnitWebControl
{
    // If this key is set to true in the context, we disable the unit conversion
    public static final String DISABLE_UNIT_CONVERSION = "disable unit conversion";

    public MapApplicationUnitWebControl(int width)
    {
        super(width);
    }

    @Override
    public UnitTypeEnum getUnitType(Context ctx)
    {
        try
        {
            ServiceBalanceLimit limit = (ServiceBalanceLimit) ctx.get(AbstractWebControl.BEAN);
            Home appHome = (Home) ctx.get(BalanceApplicationHome.class);
            BalanceApplication balApp = (BalanceApplication) appHome.find(ctx, Long.valueOf(limit.getApplicationId()));
            if (balApp != null)
            {
                return balApp.getUnitType();
            }
            else
            {
                return null;
            }
        }
        catch (Throwable t)
        {
            new MinorLogMsg(this, "fail to look up unit type from application", t).log(ctx);
            return null;
        }
    }
}
