package com.redknee.app.crm.bean.webcontrol;

import java.io.PrintWriter;

import com.redknee.app.crm.bundle.BundleCategoryAssociationTableWebControl;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.WebControl;


public class CustomBundleCategoryAssociationTableWebControl extends BundleCategoryAssociationTableWebControl
{
    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getRateWebControl()
    {
        return CustomBundleCategoryAssociationWebControl.CUSTOM_RATE_WC;
    }


    @Override
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        Context subCtx = ctx.createSubContext();
        subCtx.put(com.redknee.framework.xhome.web.Constants.TABLEWEBCONTROL_REORDER_KEY, false);
        super.toWeb(subCtx, out, name, obj);
    }
    
}
