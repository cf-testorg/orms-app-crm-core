package com.redknee.app.crm.bean.webcontrol;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.OrHome;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.msp.Spid;
import com.redknee.framework.xhome.msp.SpidHome;
import com.redknee.framework.xhome.msp.SpidKeyWebControl;
import com.redknee.framework.xhome.msp.SpidTransientHome;
import com.redknee.framework.xhome.webcontrol.FinalWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.ProvisionCommandWebControl;
import com.redknee.app.crm.technology.TechnologyOnlyEnumWebControl;


public class CustomProvisionCommandWebControl extends ProvisionCommandWebControl
{
    @Override
    public WebControl getSpidWebControl()
    {
        return CUSTOM_SPID_WC;
    }


    @Override
    public WebControl getTechnologyWebControl()
    {
        return CUSTOM_TECHNOLOGY_WC;
    }
    
    public static final WebControl CUSTOM_SPID_WC = new FinalWebControl(new SpidKeyWebControl()
    {

        // Create a Dummy Global SPID in the SpidHome
        @Override
        public Context wrapContext(Context parentCtx)
        {
            Context ctx = parentCtx.createSubContext();
            Home thome = new SpidTransientHome(ctx);
            try
            {
                Spid tempSpid = new Spid();
                tempSpid.setId(9999);
                tempSpid.setName("All Service Providers");
                thome.create(tempSpid);
            }
            catch (HomeException e)
            {
                // Cannot happen on transient home
            }
            ctx.put(SpidHome.class, new SortingHome(new OrHome((Home) ctx.get(SpidHome.class), thome)));
            return ctx;
        }


        @Override
        public void fromWeb(Context ctx, Object obj, ServletRequest req, String name)
        {
            super.fromWeb(wrapContext(ctx), obj, req, name);
        }


        @Override
        public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
        {
            super.toWeb(wrapContext(ctx), out, name, obj);
        }
    });
    
    public static final WebControl CUSTOM_TECHNOLOGY_WC = new FinalWebControl(new TechnologyOnlyEnumWebControl(true));
}
