package com.redknee.app.crm.bean;

import com.redknee.framework.xhome.context.Context;


public class CurrencyPrecision extends AbstractCurrencyPrecision
{
    private Context context_;
    
    
    public Context getContext()
    {
        return context_;
    }

    
    public void setContext(Context context)
    {
        context_ = context;
    }

}
