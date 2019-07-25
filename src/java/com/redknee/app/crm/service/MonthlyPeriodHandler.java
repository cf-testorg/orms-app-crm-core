package com.redknee.app.crm.service;

import java.util.Calendar;
import java.util.Date;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.CoreCrmConstants;
import com.redknee.app.crm.support.CalendarSupportHelper;


public class MonthlyPeriodHandler implements ServicePeriodHandler, ChargingCycleHandler
{
    public double calculateRate(final Context context, final Date billingDate, final int billingCycleDay, final int spid)
    {
        double result = 1.0;
        try
        {
            result = calculateRate(context, billingDate, billingCycleDay, spid, null, null);
        }
        catch (Throwable t)
        {
            // Ignored. Can't happen.
        }
        return result;
    }
    
    public Date calculateCycleStartDate(final Context context, final Date billingDate, final int billingCycleDay, final int spid)
    {
        Date result = null;
        try
        {
            result = calculateCycleStartDate(context, billingDate, billingCycleDay, spid, null, null);
        }
        catch (Throwable t)
        {
            // Ignored. Can't happen.
        }
        return result;    
    }    
    
    public Date calculateCycleEndDate(final Context context, final Date billingDate, final int billingCycleDay, final int spid)
    {
        Date result = null;
        try
        {
            result = calculateCycleEndDate(context, billingDate, billingCycleDay, spid, null, null);
        }
        catch (Throwable t)
        {
            // Ignored. Can't happen.
        }
        return result;    
    }    

    public double calculateRate(final Context context, final Date startDate, final Date billingDate, final int billingCycleDay, final int spid, final String subscriberId, final Object item) throws HomeException
    {
        double rate;
        Date start = CalendarSupportHelper.get(context).getDateWithNoTimeOfDay(startDate);
        Date billing = CalendarSupportHelper.get(context).getDateWithNoTimeOfDay(billingDate);

        if (start.before(billing))
        {
            // Calculate rate for first month.
            rate = calculateRate(context, start, billingCycleDay, spid, subscriberId, item);

            // Move start to end of billing cycle.
            start = calculateCycleEndDate(context, start, billingCycleDay, spid, subscriberId, item);

            // While start before billing date, charge for next cycle as well.
            while (start.before(billing))
            {
                start = CalendarSupportHelper.get(context).getDayAfter(start);
                rate += calculateRate(context, start, billingCycleDay, spid, subscriberId, item);
                start = calculateCycleEndDate(context, start, billingCycleDay, spid, subscriberId, item);
            }
        }
        else
        {
            rate = calculateRate(context, billingDate, billingCycleDay, spid, subscriberId, item);
        }
        
        return rate;
        
    }

    public double calculateRate(final Context context, final Date billingDate, final int billingCycleDay, final int spid, final String subscriberId, final Object item)
    {
        int remainingDays = 0;
        final Calendar billingDateCalendar = Calendar.getInstance();

        billingDateCalendar.setTime(billingDate);
        int billingMonth = billingDateCalendar.get(Calendar.MONTH);
        int daysInMonth = CalendarSupportHelper.get(context).getNumberOfDaysInMonth(billingMonth, billingDateCalendar.get(Calendar.YEAR));

        /*
         * if current day of month is less than billing day, it means we have not yet past
         * the current billing cycle. the remaining number of days in the billing cycle
         * can be calculated by (billingCycleDay - currentDayOfMonth)
         */
        if (billingDateCalendar.get(Calendar.DAY_OF_MONTH) < billingCycleDay)
        {
            remainingDays = billingCycleDay - billingDateCalendar.get(Calendar.DAY_OF_MONTH);
            if (billingMonth == Calendar.JANUARY)
            {
                billingMonth = Calendar.DECEMBER;
            }
            else
            {
                --billingMonth;
            }
            daysInMonth = CalendarSupportHelper.get(context).getNumberOfDaysInMonth(billingMonth, billingDateCalendar.get(Calendar.YEAR));
        }
        else if (billingDateCalendar.get(Calendar.DAY_OF_MONTH) == billingCycleDay)
        {
            remainingDays = daysInMonth;
        }

        /*
         * if current day of month is greater than the billing day, then we need to
         * consider the next billing cycle days left in month + days in next month
         * (billingDay)
         */
        else
        {
            remainingDays = daysInMonth - billingDateCalendar.get(Calendar.DAY_OF_MONTH) + billingCycleDay;
        }

        return remainingDays * 1.0 / daysInMonth;
    }
    
    public double calculateRefundRate(final Context context, final Date billingDate, final int billingCycleDay, final int spid, final String subscriberId, final Object item)
    {
        return -1.0 * calculateRate(context, billingDate, billingCycleDay, spid, subscriberId, item);
    }
    
    public Date calculateCycleStartDate(final Context context, final Date billingDate, final int billingCycleDay, final int spid, final String subscriberId, final Object item)
    {
        if (billingCycleDay < CoreCrmConstants.MIN_BILL_CYCLE_DAY || billingCycleDay > CoreCrmConstants.MAX_BILL_CYCLE_DAY)
        {
            throw new IllegalArgumentException("BillCycle day must be between 1 and 28");
        }

        final Calendar cycleStartCalendar = Calendar.getInstance();
        cycleStartCalendar.setTime(CalendarSupportHelper.get(context).getDateWithNoTimeOfDay(billingDate));
        cycleStartCalendar.set(Calendar.DAY_OF_MONTH, billingCycleDay);

        if (cycleStartCalendar.getTime().after(billingDate))
        {
            cycleStartCalendar.add(Calendar.MONTH, -1);
        }

        return CalendarSupportHelper.get(context).getDateWithNoTimeOfDay(cycleStartCalendar.getTime());
    }
    
    public Date calculateCycleEndDate(final Context context, final Date billingDate, final int billingCycleDay, final int spid, final String subscriberId, final Object item) throws HomeException
    {
        if (billingCycleDay < CoreCrmConstants.MIN_BILL_CYCLE_DAY || billingCycleDay > CoreCrmConstants.MAX_BILL_CYCLE_DAY)
        {
            throw new IllegalStateException("BillCycle day must be between 1 and 28");
        }
        
        final Calendar cal = Calendar.getInstance();
        cal.setTime(billingDate);

        final int dayOfBillingDate = cal.get(Calendar.DAY_OF_MONTH);
        if (dayOfBillingDate >= billingCycleDay)
        {
            cal.add(Calendar.MONTH, 1);
        }
        
        cal.set(Calendar.DAY_OF_MONTH, billingCycleDay);
        cal.add(Calendar.DAY_OF_MONTH, -1);

        return CalendarSupportHelper.get(context).getDateWithLastSecondofDay(cal.getTime());        
    }
    
    public static MonthlyPeriodHandler instance()
    {
        if (handler==null)
        {
            handler = new MonthlyPeriodHandler();
        }
        return handler;
    }
    
    private static MonthlyPeriodHandler handler = null;
    
    

        
}