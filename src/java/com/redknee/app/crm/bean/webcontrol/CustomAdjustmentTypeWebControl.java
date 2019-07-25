package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.CheckBoxWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.AdjustmentTypeWebControl;
import com.redknee.app.crm.filter.SystemAdjustmentTypePredicate;
import com.redknee.app.crm.web.control.AdjustmentSpidInfoTableWebControl;
import com.redknee.app.crm.web.control.AdjustmentTypeFilterCategoryProxyWebControl;
import com.redknee.app.crm.web.control.AdjustmentTypeKeyTreeWebControl;
import com.redknee.app.crm.web.control.ReadOnlyOnPredicateWebControl;

public class CustomAdjustmentTypeWebControl extends AdjustmentTypeWebControl 
{
    @Override
    public WebControl getParentCodeWebControl()
    {
        return CUSTOM_PARENT_CODE_WC;
    }

    @Override
    public WebControl getCategoryWebControl()
    {
        return CUSTOM_CATEGORY_WC;
    }

    @Override
    public WebControl getAdjustmentSpidInfoWebControl()
    {
        return CUSTOM_ADJUSTMENT_SPID_INFO_WC;
    }
    
    public static final WebControl CUSTOM_PARENT_CODE_WC =  new AdjustmentTypeFilterCategoryProxyWebControl(new AdjustmentTypeKeyTreeWebControl(), true);
    public static final WebControl CUSTOM_CATEGORY_WC = new ReadOnlyOnPredicateWebControl(CheckBoxWebControl.instance(), new SystemAdjustmentTypePredicate());
    public static final WebControl CUSTOM_ADJUSTMENT_SPID_INFO_WC = new AdjustmentSpidInfoTableWebControl();

}
