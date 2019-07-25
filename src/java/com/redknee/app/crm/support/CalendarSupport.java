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

import com.redknee.framework.xhome.context.Context;


/**
 * A set of utility method for use with Calendar (and Date) objects.
 *
 * @author kason.wong@redknee.com
 * @author gary.anderson@redknee.com
 * @author aaron.gourley@redknee.com
 */
public interface CalendarSupport extends Support
{
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
    public String formatDate(final Context context, final Date date);


    /**
     * Gets a copy of the given date with the time-of-day fields cleared in a specific timezone.
     *
     * @param date
     *            The date for which we want a copy with the time-of-day fields cleared.
     * @param timezone
     *            The timezone in which we want the date with no time of day.
     * @return A copy of the given date with the time-of-day fields cleared.
     */
    public Date convertDateWithNoTimeOfDayToTimeZone(final Date date, final String timezone);
    
    /**
     * Gets a copy of the given date with the time-of-day fields cleared.
     *
     * @param date
     *            The date for which we want a copy with the time-of-day fields cleared.
     * @param timezone
     *            The timezone in which we want the date with no time of day.
     * @return A copy of the given date with the time-of-day fields cleared.
     */
    public Date getDateWithNoTimeOfDay(final Date date, final String timezone);
    
    /**
     * Gets a copy of the given date with the time-of-day fields cleared.
     *
     * @param date
     *            The date for which we want a copy with the time-of-day fields cleared.
     * @return A copy of the given date with the time-of-day fields cleared.
     */
    public Date getDateWithNoTimeOfDay(final Date date);


    /**
     * Clears time-of-day fields in a given Calendar.
     *
     * @param calendar
     *            Calendar for which we want clear the time-of-day fields.
     * @return The same calendar object received with the time-of-day fields cleared.
     *         Same value returned to allow chained calls.
     */
    public Calendar clearTimeOfDay(final Calendar calendar);

    /**
     * Updates the time-of-day fields in a given Calendar to the last second of the day.
     * @param calendar
     * @param timezone
     * @return The same Calendar object as received with the time-of-day desirable set.
     *         Same value returned to allow chained calls
     */
    public Calendar getDateWithLastSecondofDay(final Calendar calendar, final String timezone);

    /**
     * Updates the time-of-day fields in a given Calendar to the last second of the day.
     * @param calendar
     * @return The same Calendar object as received with the time-of-day desirable set.
     *         Same value returned to allow chained calls
     */
    public Calendar getDateWithLastSecondofDay(final Calendar calendar);


    /**
     * Gets a copy of the given date with the time-of-day fields cleared.
     *
     * @param date
     *            The date for which we want a copy with the time-of-day fields cleared.
     * @param timezone
     * @return A copy of the given date with the time-of-day fields cleared.
     */
    public Date getDateWithLastSecondofDay(final Date date, final String timezone);

    public Date getDateWithLastSecondofDay(final Date date);

    /**
     * Gets the day after the given date (time-of-day is cleared).
     *
     * @param day
     *            The day for which we want the next day.
     * @return The day after the given date.
     */
    public Date getDayAfter(final Date day);

    public Date getDaysAfter(final Date day, final int daysAfter);


    /**
     * Gets the day before the given date (time-of-day is cleared).
     *
     * @param day
     *            The day for which we want the day before.
     * @return The day before the given date.
     */
    public Date getDayBefore(final Date day);


    public Date getDaysBefore(final Date day, final int daysBefore);


    /**
     * Gets the months after the given date (time-of-day is cleared).
     *
     * @param date
     * @param months
     *            The number month for which we want the month(s) after.
     * @return The day after the given date.
     */
    public Date getMonthsAfter(final Date date, final int monthsAfter);

    /**
     * Gets the months before the given date (time-of-day is cleared).
     *
     * @param date
     * @param months
     *            The number month for which we want the month(s) before.
     * @return The day before the given date.
     */
    public Date getMonthsBefore(final Date date, final int monthsBefore);

    
    /**
     * Sets the calendar to a day before the given date.
     *
     * @param calendar
     *            The calendar which we want to set to the day before.
     * @return The same calendar object, modified to a day before the given date.
     */
    public Calendar setDayBefore(final Calendar calendar);

    public Calendar setDaysBefore(final Calendar calendar, final int daysBefore);

    /**
     * Sets the calendar to a day after the given date.
     *
     * @param calendar
     *            The calendar which we want to set to the day before.
     * @return The same calendar object, modified to a day before the given date.
     */
    public Calendar setDayAfter(final Calendar calendar);

    /**
     * Gets the end (or last moment) of the day of the given date. Useful when you need an
     * inclusive date for the end of the day.
     *
     * @param date
     *            The date for which we want an end date.
     * @return The end (or last moment) of the given date.
     */
    public Date getEndOfDay(final Date date);

    /**
     * Returns date and time after number of hrs
     * @param day
     * @param hoursAfter
     * @return
     */
    public Date getHoursAfter(final Date day, final int hoursAfter);
    
    /**
     * Determines the maximum number of days in the given month in the given year.
     *
     * @param month
     *            The given month.
     * @param year
     *            The given year.
     * @return int The maximum number of days that the given month could have.
     */
    public int getNumberOfDaysInMonth(final int month, final int year);

    public boolean isDayOfWeek(final int day);

    public boolean isDayOfMonth(final int day);

    public Date findFirstDayOfWeek(final Date date);


    /**
     * @param date
     * @return date, first day of next month
     */
    public Date findFirstDayOfMonth(final Date date);


    /**
     * @param date
     * @return date, first day of next week
     */
    public Date findPreviousFirstDayOfWeek(final Date date);


    /**
     * @param date
     * @return date, first day of next month
     */
    public Date findPreviousFirstDayOfMonth(final Date date);


    /**
     * @param date
     * @return date, first day of next week
     */
    public Date findNextFirstDayOfWeek(final Date date);


    /**
     * @param date
     * @return date, first day of next month
     */
    public Date findNextFirstDayOfMonth(final Date date);


    /**
     * @param monthNum
     * @param startDate
     * @return date, the date that given month after startDate
     */
    public Date findDateMonthsAfter(final int monthNum, final Date startDate);


     /**
     * @param yearNum
     * @param startDate
     * @return date, the date that given year after startDate
     */
    public Date findDateYearsAfter(final int yearNum, final Date startDate);

    public Date findDateMinAfter(final int mins, final Date startDate);

    public Date findDatesAfter(final int dayNum);


    /**
     * @param dayNum
     * @param startDate
     * @return date, the date that is given days after startDate
     */
    public Date findDateDaysAfter(final int dayNum, final Date startDate, String timezone);

    /**
     * @param dayNum
     * @param startDate
     * @return date, the date that is given days after startDate
     */
    public Date findDateDaysAfter(final int dayNum, final Date startDate);


    /**
     * Returns a Date that is dayNum days before the supplied Date.
     *
     * @param dayNum
     * @param startDate
     * @return date, the date that is given days after startDate
     */
    public Date findDateDaysBefore(final int dayNum, final Date startDate);


    /**
     * Modifies a Calendar so that it is dayNum days before the supplied Calendar.
     *
     * @param dayNum the number of days that the calendar has to be moved
     * @param calendar calendar object that will be altered
     * @return original calendar object, modified
     */
    public Calendar findDateDaysBefore(final int dayNum, final Calendar calendar);


    /**
     * Return the number of days from the given "from" date to the given "to" date.
     *
     * @param from
     *            The given "from" date.
     * @param to
     *            The given "to" date.
     * @return The number of days between the two given dates.
     */
    public long getNumberOfDaysBetween(final Date from, final Date to);


    /**
     * Given a date, returns the day of month of the date.
     *
     * @param date a date.
     * @return The day of month of the date.
     */
    public int getDayOfMonth(final Date date);

    /**
     * Given a date, returns a valid billing day of month of the date.
     *
     * @param date a date.
     * @return The day of month of the date.
     */
    public int findBillingDayOfMonth(final Date date);

    public Date getRunningDate(final Context ctx);


    public Calendar dateToCalendar(final Date date);


    public Date calendarToDate(final Calendar cal);

    public Date findDateSecsAfter(final int secs, final Date startDate);
    public Date findDateSecsBefore(final int secs, final Date startDate);
    
    public Date calculateBillCycleEndDate(final Context context, final Date fromDate, final int billingCycleDay);
}
