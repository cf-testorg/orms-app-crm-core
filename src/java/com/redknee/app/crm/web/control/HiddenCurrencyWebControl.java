package com.redknee.app.crm.web.control;

import javax.servlet.ServletRequest;

import com.redknee.framework.core.web.XCurrencyWebControl;
import com.redknee.framework.xhome.context.Context;


public class HiddenCurrencyWebControl extends XCurrencyWebControl
{
    public HiddenCurrencyWebControl(boolean showCurrency)
    {
        super(showCurrency);
    }
    
    public Object fromWeb(Context ctx, ServletRequest req, String name)
    throws NumberFormatException, NullPointerException
    {
        try
        {
            return Long.valueOf(req.getParameter(name));
        }
        catch (NumberFormatException e)
        {
            return super.fromWeb(ctx, req, name);
        }
    }
}
