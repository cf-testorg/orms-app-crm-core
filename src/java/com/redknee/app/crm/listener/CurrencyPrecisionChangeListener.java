package com.redknee.app.crm.listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import com.redknee.app.crm.bean.CurrencyPrecisionXInfo;
import com.redknee.framework.core.locale.Currency;
import com.redknee.framework.core.locale.CurrencyHome;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.beans.xi.XInfoSupport;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextLocator;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;


public class CurrencyPrecisionChangeListener implements PropertyChangeListener
{
    private static CurrencyPrecisionChangeListener instance_ = null;
    public static CurrencyPrecisionChangeListener instance()
    {
        if (instance_ == null)
        {
            instance_ = new CurrencyPrecisionChangeListener();
        }
        return instance_;
    }
    
    protected CurrencyPrecisionChangeListener()
    {
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        Context ctx = ContextLocator.locate();

        Object obj = event.getSource();
        if (obj != null)
        {
            XInfo xinfo = (XInfo) XBeans.getInstanceOf(ctx, obj.getClass(), XInfo.class);
            if (xinfo != null)
            {
                PropertyInfo property = XInfoSupport.getPropertyInfo(ctx, xinfo, event.getPropertyName());
                if (property != null && property.equals(CurrencyPrecisionXInfo.STORAGE_PRECISION))
                {
                    final int storagePrecision = (Integer) event.getNewValue();

                    Home home = (Home) ctx.get(CurrencyHome.class);
                    StringBuilder display = new StringBuilder();
                    display.append("0");
                    if (storagePrecision>0)
                    {
                        display.append(".");
                        for (int i = 0; i<storagePrecision; i++)
                        {
                            display.append("0");
                        }
                    }
                    String displayFormat = display.toString();
                    try
                    {
                        for (Currency currency : (Collection<Currency>) home.selectAll(ctx))
                        {
                            currency.setPrecision(storagePrecision);
                            currency.setFormat(displayFormat);
                            try
                            {
                                home.store(currency);
                            }
                            catch (HomeException e)
                            {
                                LogSupport.minor(ctx, this,
                                        "Error occured while updating the precision and format for the currency '"
                                                + currency.getCode() + "': " + e.getMessage(), e);
                            }
                        }
                    }
                    catch (HomeException e)
                    {
                        LogSupport.minor(ctx, this, "Error occured while trying to update the precision and format for all the currencies: " + e.getMessage(), e);
                    }
                }
            }
            else
            {
                new MinorLogMsg(this, "No XInfo found for class " + obj.getClass().getName() + ".  No change request sent.", null).log(ctx);
            }
        }
    }    
}
