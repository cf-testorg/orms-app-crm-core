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

import java.util.Calendar;
import java.util.Date;

import com.redknee.app.crm.CommonTime;
import com.redknee.framework.xhome.context.Context;
/**
 * @author bdhavalshankh
 */
public final class NoTimeOfDayCalendarSupport implements CalendarSupport
{
    protected static CalendarSupport instance_ = null;
    public static CalendarSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new NoTimeOfDayCalendarSupport();
        }
        return instance_;
    }

    protected NoTimeOfDayCalendarSupport()
    {
    }


    /**
     * Return the number of days from the given "from" date to the given "to" date considering timezone.
     *
     * @param from
     *            The given "from" date.
     * @param to
     *            The given "to" date.
     * @return The number of days between the two given dates.
     */
    public long getNumberOfDaysBetween(final Date from, final Date to)
    {
        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(from);
        
        Calendar toCal = Calendar.getInstance();
        toCal.setTime(to);
        final long difference = CalendarSupportHelper.get().getDateWithNoTimeOfDay(to, toCal.getTimeZone().toString()).
                getTime() - CalendarSupportHelper.get().getDateWithNoTimeOfDay(from, fromCal.getTimeZone().toString()).
                getTime();

        // no need to round the result because it is an integer division
        return difference / CommonTime.MILLIS_IN_DAY;
    }

    @Override
    public String formatDate(Context context, Date date)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date convertDateWithNoTimeOfDayToTimeZone(Date date, String timezone)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getDateWithNoTimeOfDay(Date date, String timezone)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getDateWithNoTimeOfDay(Date date)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Calendar clearTimeOfDay(Calendar calendar)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Calendar getDateWithLastSecondofDay(Calendar calendar, String timezone)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Calendar getDateWithLastSecondofDay(Calendar calendar)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getDateWithLastSecondofDay(Date date, String timezone)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getDateWithLastSecondofDay(Date date)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getDayAfter(Date day)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getDaysAfter(Date day, int daysAfter)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getDayBefore(Date day)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getDaysBefore(Date day, int daysBefore)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getMonthsAfter(Date date, int monthsAfter)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getMonthsBefore(Date date, int monthsBefore)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Calendar setDayBefore(Calendar calendar)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Calendar setDaysBefore(Calendar calendar, int daysBefore)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Calendar setDayAfter(Calendar calendar)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getEndOfDay(Date date)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getHoursAfter(Date day, int hoursAfter)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumberOfDaysInMonth(int month, int year)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDayOfWeek(int day)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDayOfMonth(int day)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date findFirstDayOfWeek(Date date)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date findFirstDayOfMonth(Date date)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date findPreviousFirstDayOfWeek(Date date)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date findPreviousFirstDayOfMonth(Date date)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date findNextFirstDayOfWeek(Date date)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date findNextFirstDayOfMonth(Date date)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date findDateMonthsAfter(int monthNum, Date startDate)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date findDateYearsAfter(int yearNum, Date startDate)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date findDateMinAfter(int mins, Date startDate)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date findDatesAfter(int dayNum)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date findDateDaysAfter(int dayNum, Date startDate, String timezone)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date findDateDaysAfter(int dayNum, Date startDate)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date findDateDaysBefore(int dayNum, Date startDate)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Calendar findDateDaysBefore(int dayNum, Calendar calendar)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getDayOfMonth(Date date)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int findBillingDayOfMonth(Date date)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getRunningDate(Context ctx)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Calendar dateToCalendar(Date date)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date calendarToDate(Calendar cal)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date findDateSecsAfter(int secs, Date startDate)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date findDateSecsBefore(int secs, Date startDate)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date calculateBillCycleEndDate(final Context context, final Date fromDate, final int billingCycleDay)
    {
        throw new UnsupportedOperationException();
    }

}
