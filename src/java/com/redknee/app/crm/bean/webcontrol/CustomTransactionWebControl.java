package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.AdjustmentTypeEnum;
import com.redknee.app.crm.bean.TransactionWebControl;
import com.redknee.app.crm.web.control.AdjustmentCategoryFilterWebControl;


public class CustomTransactionWebControl extends TransactionWebControl
{
    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getServiceEndDateWebControl()
    {
        if (CUSTOM_SERVICE_END_DATE_WC == null)
        {
            CUSTOM_SERVICE_END_DATE_WC = new AdjustmentCategoryFilterWebControl(super.getServiceEndDateWebControl(), AdjustmentTypeEnum.RecurringCharges);
        }
        return CUSTOM_SERVICE_END_DATE_WC;
    }

    public static WebControl CUSTOM_SERVICE_END_DATE_WC;
}
