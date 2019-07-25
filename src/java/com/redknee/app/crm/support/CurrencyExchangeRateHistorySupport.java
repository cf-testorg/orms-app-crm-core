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

import java.util.Date;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.CurrencyExchangeRateHistory;

/**
 * Provides utility functions for use with CurrencyExchangeRateHistory
 *
 * @author kumaran.sivasubramaniam@redknee.com
 */
public interface CurrencyExchangeRateHistorySupport extends Support
{
    
    /**
     * Gets the current exchange rate (current Time) for the currencyId
     * 
     * @param ctx
     * @param currencyId CurrencyType we want to find the exchange rate for
     */
    public CurrencyExchangeRateHistory getLatestCurrencyExchangeRate(Context ctx, final String currencyId);

/**
 * Gets appropriate exchangerate for both currencyId and exchangeDate 
 * @param ctx
 * @param exchangeDate Date which exchange rate is being calculated
 * @param currencyId Country code 
 * @return CurrencyExchangeRateHisotry
 */
    public CurrencyExchangeRateHistory getCurrencyExchangeRateForCurrencyTypeAndInterval(Context ctx, final Date exchangeDate, final String currencyId);
}