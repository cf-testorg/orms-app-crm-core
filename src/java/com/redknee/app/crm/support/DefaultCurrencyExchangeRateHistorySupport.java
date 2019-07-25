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

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.LTE;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.CurrencyExchangeRateHistory;
import com.redknee.app.crm.bean.CurrencyExchangeRateHistoryXInfo;

/**
 * Provides utility functions for use with CurrencyExchangeRateHistory
 *
 * @author kumaran.sivasubramaniam@redknee.com
 */
public class DefaultCurrencyExchangeRateHistorySupport implements CurrencyExchangeRateHistorySupport
{
    public DefaultCurrencyExchangeRateHistorySupport()
    {
        currencyExchangeRateHistoryList_ = new HashMap<String, Map<Long, CurrencyExchangeRateHistory>>();
    }
    
    /**
     * {@inheritDoc}
     */
    public CurrencyExchangeRateHistory getLatestCurrencyExchangeRate(Context ctx, final String currencyId )
    {
        Date todayDate = new java.util.Date();
	
        return getCurrencyExchangeRateForCurrencyTypeAndInterval( ctx,
                todayDate,
                currencyId );
    }

    /**
     * {@inheritDoc}
     */
    public CurrencyExchangeRateHistory getCurrencyExchangeRateForCurrencyTypeAndInterval(
            Context ctx,
            final Date exchangeDate,
            final String currencyId )
    {
        CurrencyExchangeRateHistory result = null;
                
        final Date exchangeDateWithDateOnly = CalendarSupportHelper.get(ctx).getDateWithNoTimeOfDay(exchangeDate);

        Map<Long, CurrencyExchangeRateHistory> allExchangeRateForCurrencyId = currencyExchangeRateHistoryList_.get(currencyId);    
        if (allExchangeRateForCurrencyId == null  )
        {
            allExchangeRateForCurrencyId = new HashMap<Long, CurrencyExchangeRateHistory>();
            currencyExchangeRateHistoryList_.put(currencyId, allExchangeRateForCurrencyId);
        }

        result = allExchangeRateForCurrencyId.get(exchangeDateWithDateOnly.getTime());

        if (result == null)
        {
            LogSupport.info(ctx, this, " CurrencyExchangeRate was not found in cache for " + currencyId + " on " + exchangeDate, null);
            
            Collection<CurrencyExchangeRateHistory> rates = null;
            
            try
            {
                And filter = new And();
                filter.add(new EQ(CurrencyExchangeRateHistoryXInfo.CURRENCY_ID, currencyId));
                filter.add(new LTE(CurrencyExchangeRateHistoryXInfo.START_DATE, exchangeDateWithDateOnly));
                
                rates = HomeSupportHelper.get(ctx).getBeans(ctx, 
                        CurrencyExchangeRateHistory.class, filter, 
                        false /* Sort Descending by start date */,
                        CurrencyExchangeRateHistoryXInfo.START_DATE);
            }
            catch (HomeException e)
            {
                LogSupport.minor(ctx, this, "Home Exception : ", e);
            }
            
            if (rates != null && rates.size()>0)
            {
                result = rates.iterator().next();
                if ( result != null )
                {
                    // Add the exchange rate to the cache
                    allExchangeRateForCurrencyId.put(exchangeDateWithDateOnly.getTime(), result);
                }
            }
        }
        else
        {
            LogSupport.info(ctx,this, "Using exchange that was found in the cache " + result, null);
        }
        
        return result;
    }
    
    /**
     * Maintains a cache of all the exchange rate
     */
    private Map<String, Map<Long, CurrencyExchangeRateHistory>> currencyExchangeRateHistoryList_;
}