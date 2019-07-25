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
package com.redknee.app.crm.calculator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.support.DateUtil;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.support.CalendarSupportHelper;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class DateFormattingValueCalculator extends AbstractDateFormattingValueCalculator
{

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAdvanced(Context ctx)
    {
        Object value = super.getValueAdvanced(ctx);
        SimpleDateFormat format = null;
        try
        {
            format = new SimpleDateFormat(this.getFormat());   
        }
        catch (Exception e)
        {
            new MinorLogMsg(this, 
                    "Invalid date format configured (" + this.getFormat()
                    + ").  Using default format (" + DateUtil.DATE_FORMAT_PATTERN + ") instead.", e).log(ctx);
            format = new SimpleDateFormat(DateUtil.DATE_FORMAT_PATTERN);
        }
        
        Date date = null;
        if (value instanceof Date)
        {
            date = (Date)value;
        }
        else if (value instanceof Calendar)
        {
            date = CalendarSupportHelper.get(ctx).calendarToDate((Calendar)value);
        }
        else if (value instanceof Long)
        {
            date = new Date((Long)value);
        }
        else if (value instanceof Number)
        {
            date = new Date(((Number)value).longValue());
        }
        
        if (date != null)
        {
            value = format.format(date);
        }
        
        return value;
    }

}
