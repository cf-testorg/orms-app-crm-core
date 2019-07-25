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
package com.redknee.app.crm;

/**
 * This interface defines date and time related constants
 * 
 * @author victor.stratan@redknee.com
 */
public interface CommonTime
{
    public static long MILLIS_IN_HOUR = 60L * 60 * 1000;
    public static long MILLIS_IN_DAY = 24L * MILLIS_IN_HOUR;

    public static int YEARS_IN_FUTURE = 20;

    public static final String RUNNING_DATE = "RunningDate";
}
