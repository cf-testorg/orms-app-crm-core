package com.redknee.app.crm.bean.webcontrol;

import com.redknee.app.crm.bundle.BundleCategoryAssociationWebControl;
import com.redknee.app.crm.bundle.web.MapUnitWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;


public class CustomBundleCategoryAssociationWebControl extends BundleCategoryAssociationWebControl
{
    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getRateWebControl()
    {
        return CUSTOM_RATE_WC;
    }
    
    public static final WebControl CUSTOM_RATE_WC = new MapUnitWebControl(11, " units per", true);

}
