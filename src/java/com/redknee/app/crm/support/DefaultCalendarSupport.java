/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s).  A complete listing of authors of this work is readily
 * available.  Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee.  No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the license agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.support;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.redknee.app.crm.CommonTime;
import com.redknee.app.crm.CoreCrmConstants;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.language.Lang;
import com.redknee.framework.xhome.language.MessageMgrSPI;
import com.redknee.framework.xhome.webcontrol.DateWebControl;

/**
 * @author kason.wong@redknee.com
 * @author gary.anderson@redknee.com
 */
public final class DefaultCalendarSupport implements CalendarSupport
{
    protected static CalendarSupport instance_ = null;
    public static CalendarSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultCalendarSupport();
        }
        return instance_;
    }

    protected DefaultCalendarSupport()
    {
    }


    /**
     * Formats the date (not including the time-of-day) using the format set in the
     * context for the DateWebControl.
     *
     * @param context
     *            The operating context.
     * @param date
     *            The date to format.
     * @return The given date formated as would a default DateWebControl.
     */
    public String formatDate(final Context context, final Date date)
    {
        final MessageMgrSPI mmgr = (MessageMgrSPI)context.get(MessageMgrSPI.class);

        final String format = mmgr.get(context, DateWebControl.MSG_MGR_KEY, DateWebControl.class,
                (Lang) context.get(Lang.class), null, null);

        if  (format == null)
        {
            return DateWebControl.DEFAULT_FORMAT.format(date);
        }
        else
        {
            return (new SimpleDateFormat(format)).format(date);
        }
    }


    public Date convertDateWithNoTimeOfDayToTimeZone(final Date date, final String timezone)
    {
        Calendar calendar = getCalendar(timezone);
        Calendar oldCal = Calendar.getInstance();
        
        oldCal.setTime(date);
        calendar.set(Calendar.YEAR, oldCal.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, oldCal.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, oldCal.get(Calendar.DAY_OF_MONTH));

        clearTimeOfDay(calendar);

        return calendar.getTime();
    }

    /**
     * Gets a copy of the given date with the time-of-day fields cleared.
     *
     * @param date
     *            The date for which we want a copy with the time-of-day fields cleared.
     * @return A copy of the given date with the time-of-day fields cleared.
     */
    public Date getDateWithNoTimeOfDay(final Date date, final String timezone)
    {
        Calendar calendar = getCalendar(timezone);
        calendar.setTime(date);
        clearTimeOfDay(calendar);

        return calendar.getTime();
    }

    /**
     * Gets a copy of the given date with the time-of-day fields cleared.
     *
     * @param date
     *            The date for which we want a copy with the time-of-day fields cleared.
     * @return A copy of the given date with the time-of-day fields cleared.
     */
    public Date getDateWithNoTimeOfDay(final Date date)
    {
        return getDateWithNoTimeOfDay(date, null);
    }


    /**
     * Clears time-of-day fields in a given Calendar.
     *
     * @param calendar
     *            Calendar for which we want clear the time-of-day fields.
     * @return The same calendar object received with the time-of-day fields cleared.
     *         Same value returned to allow chained calls.
     */
    public Calendar clearTimeOfDay(final Calendar calendar)
    {
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        return calendar;
    }

    /**
     * Updates the time-of-day fields in a given Calendar to the last second of the day.
     * @param calendar
     * @return The same Calendar object as received with the time-of-day desirable set.
     *         Same value returned to allow chained calls
     */
    public Calendar getDateWithLastSecondofDay(final Calendar calendar)
    {
        return getDateWithLastSecondofDay(calendar, null);
    }
    /**
     * Updates the time-of-day fields in a given Calendar to the last second of the day.
     * @param calendar
     * @return The same Calendar object as received with the time-of-day desirable set.
     *         Same value returned to allow chained calls
     */
    public Calendar getDateWithLastSecondofDay(final Calendar calendar, String timezone)
    {
        Calendar newCal = getCalendar(timezone);
        newCal.setTime(calendar.getTime());
        newCal.set(Calendar.MILLISECOND, 999);
        newCal.set(Calendar.SECOND, 59);
        newCal.set(Calendar.MINUTE, 59);
        newCal.set(Calendar.HOUR_OF_DAY, 23);

        return newCal;
    }


    /**
     * Gets a copy of the given date with the time-of-day fields cleared.
     *
     * @param date
     *            The date for which we want a copy with the time-of-day fields cleared.
     * @return A copy of the given date with the time-of-day fields cleared.
     */
    public Date getDateWithLastSecondofDay(final Date date, String timezone)
    {
        final Calendar calendar = getCalendar(timezone);
        calendar.setTime(date);
        return getDateWithLastSecondofDay(calendar, timezone).getTime();
    }


    public Date getDateWithLastSecondofDay(final Date date)
    {
        return getDateWithLastSecondofDay(date, null);
    }
    
    public Date getHoursAfter(final Date day, final int hoursAfter)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.add(Calendar.HOUR_OF_DAY, hoursAfter);

        return calendar.getTime();
    }


    /**
     * Gets the day after the given date (time-of-day is cleared).
     *
     * @param day
     *            The day for which we want the next day.
     * @return The day after the given date.
     */
    public Date getDayAfter(final Date day)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        return getDateWithNoTimeOfDay(calendar.getTime());
    }

    public Date getDaysAfter(final Date day, final int daysAfter)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.add(Calendar.DAY_OF_MONTH, daysAfter);

        return getDateWithNoTimeOfDay(calendar.getTime());
    }

    public Date getMonthsAfter(final Date date, final int monthsAfter)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthsAfter);
        return getDateWithNoTimeOfDay(calendar.getTime());
    }
    
    public Date getMonthsBefore(final Date date, final int monthsBefore)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -monthsBefore);
        return getDateWithNoTimeOfDay(calendar.getTime());
    }
    /**
     * Gets the day before the given date (time-of-day is cleared).
     *
     * @param day
     *            The day for which we want the day before.
     * @return The day before the given date.
     */
    public Date getDayBefore(final Date day)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        setDayBefore(calendar);

        return calendar.getTime();
    }


    public Date getDaysBefore(final Date day, final int daysBefore)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        setDaysBefore(calendar, daysBefore);

        return calendar.getTime();
    }


    /**
     * Sets the calendar to a day before the given date.
     *
     * @param calendar
     *            The calendar which we want to set to the day before.
     * @return The same calendar object, modified to a day before the given date.
     */
    public Calendar setDayBefore(final Calendar calendar)
    {
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        return clearTimeOfDay(calendar);
    }

    public Calendar setDaysBefore(final Calendar calendar, final int daysBefore)
    {
        calendar.add(Calendar.DAY_OF_MONTH, daysBefore * (-1));

        return clearTimeOfDay(calendar);
    }

    /**
     * Sets the calendar to a day after the given date.
     *
     * @param calendar
     *            The calendar which we want to set to the day before.
     * @return The same calendar object, modified to a day before the given date.
     */
    public Calendar setDayAfter(final Calendar calendar)
    {
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        return clearTimeOfDay(calendar);
    }

    /**
     * Gets the end (or last moment) of the day of the given date. Useful when you need an
     * inclusive date for the end of the day.
     *
     * @param date
     *            The date for which we want an end date.
     * @return The end (or last moment) of the given date.
     */
    public Date getEndOfDay(final Date date)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 23);

        return calendar.getTime();
    }


    /**
     * Determines the maximum number of days in the given month in the given year.
     *
     * @param month
     *            The given month.
     * @param year
     *            The given year.
     * @return int The maximum number of days that the given month could have.
     */
    public int getNumberOfDaysInMonth(final int month, final int year)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, 1);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    public boolean isDayOfWeek(final int day)
    {
        final Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        return day == cal.get(Calendar.DAY_OF_WEEK);
    }


    public boolean isDayOfMonth(final int day)
    {
        final Calendar cal = Calendar.getInstance();
        return day == cal.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * @param date
     * @return
     */
    public Date findFirstDayOfWeek(final Date date)
    {
        final Calendar newCal = new GregorianCalendar();
        newCal.setTime(date);

        newCal.setFirstDayOfWeek(Calendar.SUNDAY);

        newCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        newCal.set(Calendar.HOUR_OF_DAY, 0);
        newCal.set(Calendar.MINUTE, 0);
        newCal.set(Calendar.SECOND, 0);
        newCal.set(Calendar.MILLISECOND, 0);

        return newCal.getTime();
    }


    /**
     * @param date
     * @return date, first day of next month
     */
    public Date findFirstDayOfMonth(final Date date)
    {
        final Calendar newCal = new GregorianCalendar();
        newCal.setTime(date);

        newCal.set(Calendar.DAY_OF_MONTH, 1);
        newCal.set(Calendar.HOUR_OF_DAY, 0);
        newCal.set(Calendar.MINUTE, 0);
        newCal.set(Calendar.SECOND, 0);
        newCal.set(Calendar.MILLISECOND, 0);

        return newCal.getTime();
    }


    /**
     * @param date
     * @return date, first day of next week
     */
    public Date findPreviousFirstDayOfWeek(final Date date)
    {
        final Calendar newCal = new GregorianCalendar();
        newCal.setTime(date);

        newCal.setFirstDayOfWeek(Calendar.SUNDAY);

        newCal.add(Calendar.DAY_OF_YEAR, -7);
        newCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        newCal.set(Calendar.HOUR_OF_DAY, 0);
        newCal.set(Calendar.MINUTE, 0);
        newCal.set(Calendar.SECOND, 0);
        newCal.set(Calendar.MILLISECOND, 0);

        return newCal.getTime();
    }


    /**
     * @param date
     * @return date, first day of next month
     */
    public Date findPreviousFirstDayOfMonth(final Date date)
    {
        final Calendar newCal = new GregorianCalendar();
        newCal.setTime(date);

        newCal.set(Calendar.MONTH, newCal.get(Calendar.MONTH) - 1);
        newCal.set(Calendar.DAY_OF_MONTH, 1);
        newCal.set(Calendar.HOUR_OF_DAY, 0);
        newCal.set(Calendar.MINUTE, 0);
        newCal.set(Calendar.SECOND, 0);
        newCal.set(Calendar.MILLISECOND, 0);

        return newCal.getTime();
    }


    /**
     * @param date
     * @return date, first day of next week
     */
    public Date findNextFirstDayOfWeek(final Date date)
    {
        final Calendar newCal = new GregorianCalendar();
        newCal.setTime(date);

        newCal.setFirstDayOfWeek(Calendar.SUNDAY);

        newCal.add(Calendar.DAY_OF_YEAR, 7);
        newCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        newCal.set(Calendar.HOUR_OF_DAY, 0);
        newCal.set(Calendar.MINUTE, 0);
        newCal.set(Calendar.SECOND, 0);
        newCal.set(Calendar.MILLISECOND, 0);

        return newCal.getTime();
    }


    /**
     * @param date
     * @return date, first day of next month
     */
    public Date findNextFirstDayOfMonth(final Date date)
    {
        final Calendar newCal = new GregorianCalendar();
        newCal.setTime(date);

        newCal.set(Calendar.MONTH, newCal.get(Calendar.MONTH) + 1);
        newCal.set(Calendar.DAY_OF_MONTH, 1);
        newCal.set(Calendar.HOUR_OF_DAY, 0);
        newCal.set(Calendar.MINUTE, 0);
        newCal.set(Calendar.SECOND, 0);
        newCal.set(Calendar.MILLISECOND, 0);

        return newCal.getTime();
    }


    /**
     * @param monthNum
     * @param startDate
     * @return date, the date that given month after startDate
     */
    public Date findDateMonthsAfter(final int monthNum, final Date startDate)
    {
        final Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);

        startCal.add(Calendar.MONTH, monthNum);

        return startCal.getTime();
    }


     /**
     * @param yearNum
     * @param startDate
     * @return date, the date that given year after startDate
     */
    public Date findDateYearsAfter(final int yearNum, final Date startDate)
    {
        return ModelAppCrmBeanSupport.findDateYearsAfter(yearNum, startDate);
    }


    public Date findDateMinAfter(final int mins, final Date startDate)
    {
        final Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        startCal.add(Calendar.MINUTE, mins);

        return startCal.getTime();
    }


    public Date findDatesAfter(final int dayNum)
    {
        return findDateDaysAfter(dayNum, new Date());
    }


    /**
     * @param dayNum
     * @param startDate
     * @return date, the date that is given days after startDate
     */
    public Date findDateDaysAfter(final int dayNum, final Date startDate)
    {
        return findDateDaysAfter(dayNum, startDate, null);
    }
    
    private Calendar getCalendar(String timezone)
    {
        if (timezone!=null)
        {
            TimeZone t = TimeZone.getTimeZone(timezone);
            return Calendar.getInstance(t);
        }
        else
        {
            return Calendar.getInstance();
        }   
    }


    /**
     * @param dayNum
     * @param startDate
     * @return date, the date that is given days after startDate
     */
    public Date findDateDaysAfter(final int dayNum, final Date startDate, final String timezone)
    {
        Calendar startCal = getCalendar(timezone);
        Calendar newCal = getCalendar(timezone);
        
        startCal.setTime(startDate);
        //int startDay   = startCal.get(Calendar.DAY_OF_MONTH);
        final int startMonth = startCal.get(Calendar.MONTH);
        final int startYear = startCal.get(Calendar.YEAR);

        newCal.setTime(startDate);

        newCal.set(Calendar.YEAR, startYear);
        newCal.set(Calendar.MONTH, startMonth);
        newCal.set(Calendar.DAY_OF_MONTH, startCal.get(Calendar.DAY_OF_MONTH) + dayNum);
        newCal.set(Calendar.HOUR_OF_DAY, 0);
        newCal.set(Calendar.MINUTE, 0);
        newCal.set(Calendar.SECOND, 0);
        newCal.set(Calendar.MILLISECOND, 0);

        return newCal.getTime();
    }

    /**
     * Returns a Date that is dayNum days before the supplied Date.
     *
     * @param dayNum
     * @param startDate
     * @return date, the date that is given days after startDate
     */
    public Date findDateDaysBefore(final int dayNum, final Date startDate)
    {
        final Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        final int startDay = startCal.get(Calendar.DAY_OF_MONTH);
        final int startMonth = startCal.get(Calendar.MONTH);
        final int startYear = startCal.get(Calendar.YEAR);

        final Calendar newCal = new GregorianCalendar();
        newCal.setTime(startDate);

        newCal.set(Calendar.YEAR, startYear);
        newCal.set(Calendar.MONTH, startMonth);
        newCal.set(Calendar.DAY_OF_MONTH, startDay - dayNum);
        newCal.set(Calendar.HOUR_OF_DAY, 0);
        newCal.set(Calendar.MINUTE, 0);
        newCal.set(Calendar.SECOND, 0);
        newCal.set(Calendar.MILLISECOND, 0);

        return newCal.getTime();
    }


    /**
     * Modifies a Calendar so that it is dayNum days before the supplied Calendar.
     *
     * @param dayNum the number of days that the calendar has to be moved
     * @param calendar calendar object that will be altered
     * @return original calendar object, modified
     */
    public Calendar findDateDaysBefore(final int dayNum, final Calendar calendar)
    {
        calendar.add(Calendar.DAY_OF_MONTH, -dayNum);

        return calendar;
    }


    /**
     * Return the number of days from the given "from" date to the given "to" date.
     *
     * @param from
     *            The given "from" date.
     * @param to
     *            The given "to" date.
     * @return The number of days between the two given dates.
     */
    public long getNumberOfDaysBetween(final Date from, final Date to)
    {
        final long difference = getDateWithNoTimeOfDay(to).getTime() - getDateWithNoTimeOfDay(from).getTime();

        // no need to round the result because it is an integer division
        return difference / CommonTime.MILLIS_IN_DAY;
    }


    /**
     * Given a date, returns the day of month of the date.
     *
     * @param date a date.
     * @return The day of month of the date.
     */
    public int getDayOfMonth(final Date date)
    {
        // get day of month
        final Calendar dateCal = new GregorianCalendar();
        dateCal.setTime(date);
        return dateCal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Given a date, returns a valid billing day of month of the date.
     *
     * @param date a date.
     * @return The day of month of the date.
     */
    public int findBillingDayOfMonth(final Date date)
    {
        // get billing day of month
        int day = getDayOfMonth(date);
        if (day > 28)
        {
            day = 28;
        }
        return day;
    }

    public Date getRunningDate(final Context ctx)
    {
        Date runningDate = (Date) ctx.get(CommonTime.RUNNING_DATE);
        if (runningDate == null)
        {
            runningDate = new Date();
        }
        return runningDate;
    }


    public Calendar dateToCalendar(final Date date)
    {
        if (date != null)
        {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        }
        return null;
    }


    public Date calendarToDate(final Calendar cal)
    {
        if (cal != null)
        {
            return cal.getTime();
        }
        return null;
    }

    public Date findDateSecsAfter(final int secs, final Date startDate)
    {
        final Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        startCal.add(Calendar.SECOND, secs);
        return startCal.getTime();
    }

    public Date findDateSecsBefore(final int secs, final Date startDate)
    {
        return findDateSecsAfter(-secs, startDate);
    }
    
    public Date calculateBillCycleEndDate(final Context context, final Date fromDate, final int billingCycleDay)
    {
     
        if (billingCycleDay < CoreCrmConstants.MIN_BILL_CYCLE_DAY
                || billingCycleDay > CoreCrmConstants.MAX_BILL_CYCLE_DAY)
        {
            throw new IllegalStateException("BillCycle day must be between 1 and 28");
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        final int dayOfFromDate = cal.get(Calendar.DAY_OF_MONTH);
        if (dayOfFromDate >= billingCycleDay)
        {
            cal.add(Calendar.MONTH, 1);
        }
        cal.set(Calendar.DAY_OF_MONTH, billingCycleDay);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return CalendarSupportHelper.get(context).getDateWithLastSecondofDay(cal.getTime());
    }
}
