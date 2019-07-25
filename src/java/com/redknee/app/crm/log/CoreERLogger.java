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
package com.redknee.app.crm.log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.ERLogMsg;
import com.redknee.app.crm.bean.Transaction;
import com.redknee.util.snippet.log.Logger;


/**
 * This class contains methods for sending out ERs. It also contains definition for the
 * general ER fields (ER IDs, descriptions).
 *
 * @author lanny.tse@redknee.com
 * @author larry.xia@redknee.com
 * @author paul.sperneac@redknee.com
 * @author angie.li@redknee.com
 * @author prasanna.kulkarni@redknee.com
 * @author cindy.wong@redknee.com
 */
public class CoreERLogger
{
    /** This ID defines the Redknee class of ERs. */
    protected static final int RECORD_CLASS       = 700;    
    /**
     * ER class 1100.
     */
    protected static final int RECORD_CLASS_1100 = 1100;
    
    /** ER ID for Subscriber Transaction. */
    protected static final int SUBSCRIBER_TRANSACTION_ERID = 1104;
    
    /** ER description of subscriber transaction ER. */
    protected static final String SUBSCRIBER_TRANSACTION_SERVICE_NAME = "Subscriber Transaction Event";

   /**
     * Redknee standard date-time format string for ER date and time field formatting.
     */
    public static final String DATE_FORMAT_STRING = "yyyy/MM/dd HH:mm:ss";

    /**
     * Channel Type Enumeration Index - from AppCrm ChannelTypeEnum.
     */
    public static final String CHANNEL_TYPE_ENUM_INDEX = "ChannelTypeEnumIndex";

    /**
     * Use the ThreadLocal. Better yet call formatERDateWithTime()
     * Redknee standard date-time format in ERs.
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING);

    public static final ThreadLocal<DateFormat> DATE_FORMAT_THREAD_LOCAL = new ThreadLocal<DateFormat>()
    {
        @Override
        protected DateFormat initialValue()
        {
            return new SimpleDateFormat(DATE_FORMAT_STRING);
        }
    };
    /**
     * Redknee standard date-only format string for ER date field formatting.
     */
    public static final String DATE_FORMAT_DAY_ONLY_STRING = "yyyy/MM/dd";

    /**
     * Use the ThreadLocal. Better yet call formatERDateDayOnly()
     * Redknee standard date-only format in ERs.
     */
    private static final DateFormat DATE_FORMAT_DAY_ONLY = new SimpleDateFormat(DATE_FORMAT_DAY_ONLY_STRING);

    /**
     * Redknee standard date-only format in ERs in a ThreadLocal.
     */
    public static final ThreadLocal<DateFormat> DATE_FORMAT_DAY_ONLY_THREAD_LOCAL = new ThreadLocal<DateFormat>()
    {
        @Override
        protected DateFormat initialValue()
        {
            return new SimpleDateFormat(DATE_FORMAT_DAY_ONLY_STRING);
        }
    };

    /**
     * Creates a new <code>ERLogger</code> instance. This method is made private to
     * prevent instantiation of utility class.
     */
    protected CoreERLogger()
    {
        // empty
    }
    
    /**
     * Generates the subscriber successful transaction ER.
     *
    * @param ctx
     *            The operating context.
    * @param txn
     *            The transaction.
     * @param amount
     *            Amount in the transaction.
    * @param result
     *            Result code.
    */
    
   
    public static void createTransactionEr(final Context ctx, final Transaction txn, final long amount,
        final Long balanceAmount, final int result) {

        int adjustmentType = txn.getAdjustmentType();
        String itemType = null;
        String serviceId = "";
        String svcName = "";
        String startDate = "";
        String endDate = "";
        String svcFee = "";

        final String[] fields = new String[25];
        fields[0] = txn.getBAN();
        fields[1] = txn.getMSISDN();
        fields[2] = String.valueOf(txn.getSubscriberType().getIndex());
        
        if (txn.getTransDate() != null) {
            fields[3] = formatERDateDayOnly(txn.getTransDate());
        } else {
            fields[3] = "";
        }
    
        fields[4] = txn.getGLCode();
        fields[5] = String.valueOf(amount);
        fields[6] = String.valueOf(adjustmentType);
        fields[7] = String.valueOf(txn.getReceiptNum());
        fields[8] = String.valueOf(result);

        if(balanceAmount != null) {
            fields[9] = String.valueOf(balanceAmount.longValue());
        } else {
            fields[9] = "";
        }

        fields[10] = itemType != null ? itemType : "";
        fields[11] = serviceId;
        fields[12] = svcName;
        fields[13] = startDate;
        fields[14] = endDate;
        fields[15] = svcFee;
        fields[16] = ""; //TODO : Need to add Subscription Type ID
        fields[17] = txn.getAgent() != null ? txn.getAgent() : "";
        fields[18] = "";
        fields[19] = "";
        fields[20] = "";
        fields[21] = "";
        fields[22] = "";

        if (ctx.get(CHANNEL_TYPE_ENUM_INDEX) != null) {
            fields[23] = String.valueOf(ctx.get(CHANNEL_TYPE_ENUM_INDEX));
        } else {
            fields[23] = "";
        }

        fields[24] = "";

        new ERLogMsg(SUBSCRIBER_TRANSACTION_ERID, RECORD_CLASS_1100, SUBSCRIBER_TRANSACTION_SERVICE_NAME,
            txn.getSpid(), fields).log(ctx);
    }

    /**
     * Formats the given date to conform to ER date format standard.
     * TT7090400016: Dates should conform to Redknee standard.
     *
     * @param date The date to be formatted.
     * @return Date formatted into a string.
     */
    public static String formatERDateDayOnly(final Date date)
    {
        if (date == null)
        {
            // the formatted string for a null date is an empty string
            return "";
        }

        DateFormat format = DATE_FORMAT_DAY_ONLY_THREAD_LOCAL.get();
        return format.format(date);
    }


    /**
     * Parses the given string in ER date format standard to Date.
     *
     * @param date The date to be formatted.
     * @return Date formatted into a string.
     * @throws ParseException thrown if string cannot be parsed in a Date
     */
    public static Date parseERDateDayOnly(final String dateString) throws ParseException
    {
        DateFormat format = DATE_FORMAT_DAY_ONLY_THREAD_LOCAL.get();
        return format.parse(dateString);
    }


    /**
     * Parses the given string in ER date format standard to Date.
     *
     * @param date The date to be formatted.
     * @return Date formatted into a string.
     * @throws ParseException thrown if string cannot be parsed in a Date
     */
    public static Date parseERDateDayOnlyNoException(final Context ctx, final String dateString)
    {
        try
        {
            return parseERDateDayOnly(dateString);
        }
        catch (ParseException e)
        {
            // debug level cause the caller is not interested in the exception
            Logger.debug(ctx, CoreERLogger.class, "Error occurred while parsing date: " + e.getMessage(), e);
            return null;
        }
    }


    /**
     * Formats the given date to conform to ER date format standard.
     * TT7090400016: Dates should conform to Redknee standard.
     *
     * @param date The date to be formatted.
     * @return Date formatted into a string.
     */
    public static String formatERDateWithTime(final Date date)
    {
        if (date == null)
        {
            // the formatted string for a null date is an empty string
            return "";
        }

        DateFormat format = DATE_FORMAT_THREAD_LOCAL.get();
        return format.format(date);
    }


    /**
     * Parses the given string in ER date format standard to Date.
     *
     * @param date The date to be formatted.
     * @return Date formatted into a string.
     * @throws ParseException thrown if string cannot be parsed in a Date
     */
    public static Date parseERDateWithTime(final String dateString) throws ParseException
    {
        DateFormat format = DATE_FORMAT_THREAD_LOCAL.get();
        return format.parse(dateString);
    }


    /**
     * Parses the given string in ER date format standard to Date.
     *
     * @param date The date to be formatted.
     * @return Date formatted into a string.
     * @throws ParseException thrown if string cannot be parsed in a Date
     */
    public static Date parseERDateWithTimeNoException(final Context ctx, final String dateString)
    {
        try
        {
            return parseERDateWithTime(dateString);
        }
        catch (ParseException e)
        {
            // debug level cause the caller is not interested in the exception
            Logger.debug(ctx, CoreERLogger.class, "Error occurred while parsing date: " + e.getMessage(), e);
            return null;
        }
    }
}
