package com.redknee.app.crm.service;

import java.util.Date;

import com.redknee.app.crm.support.CalendarSupportHelper;
import com.redknee.framework.xhome.context.Context;


public class DailyPeriodHandler implements ServicePeriodHandler, ChargingCycleHandler
{
    public double calculateRate(final Context context, final Date billingDate, final int billingCycleDay, final int spid)
    {
        return 1.0;
    }

    public Date calculateCycleStartDate(final Context context, final Date billingDate, final int billingCycleDay, final int spid)
    {
        return CalendarSupportHelper.get(context).getDateWithNoTimeOfDay(billingDate);    
    }    
    
    public Date calculateCycleEndDate(final Context context, final Date billingDate, final int billingCycleDay, final int spid)
    {
        return CalendarSupportHelper.get(context).getDateWithLastSecondofDay(billingDate);   
    } 

    public double calculateRate(final Context context, final Date startDate, final Date billingDate, final int billingCycleDay, final int spid, final String subscriberId, final Object item)
    {
        Date start = CalendarSupportHelper.get(context).getDateWithNoTimeOfDay(startDate);
        Date billing = CalendarSupportHelper.get(context).getDateWithNoTimeOfDay(billingDate);

        if (start.before(billing))
        {
            return CalendarSupportHelper.get(context).getNumberOfDaysBetween(startDate, billingDate) + 1.0;
        }
        else
        {
            return calculateRate(context, billingDate, billingCycleDay, spid, subscriberId, item);
        }
    }

    public double calculateRate(final Context context, final Date billingDate, final int billingCycleDay, final int spid, final String subscriberId, final Object item)
    {
        return 1.0;
    }
    
    public double calculateRefundRate(final Context context, final Date billingDate, final int billingCycleDay, final int spid, final String subscriberId, final Object item)
    {
        return 0;
    }

    public Date calculateCycleStartDate(final Context context, final Date billingDate, final int billingCycleDay, final int spid, final String subscriberId, final Object item)
    {
        return CalendarSupportHelper.get(context).getDateWithNoTimeOfDay(billingDate);    
    }    
    
    public Date calculateCycleEndDate(final Context context, final Date billingDate, final int billingCycleDay, final int spid, final String subscriberId, final Object item)
    {
        return CalendarSupportHelper.get(context).getDateWithLastSecondofDay(billingDate);   
    } 
    
    public static DailyPeriodHandler instance()
    {
        if (handler==null)
        {
            handler = new DailyPeriodHandler();
        }
        return handler;
    }
    
    private static DailyPeriodHandler handler = null;    
}
