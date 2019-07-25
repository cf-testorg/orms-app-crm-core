package com.redknee.app.crm.service;

import java.util.Date;

import com.redknee.app.crm.support.CalendarSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;


/**
 * 
 * @author sbanerjee
 *
 */
public class DefaultHandler
    implements ServicePeriodHandler, ChargingCycleHandler
{

    /**
     * 
     */
    private static ChargingCycleHandler handler;

    @Override
    public Date calculateCycleEndDate(Context context, Date billingDate,
            int billingCycleDay, int spid, String subscriberId, Object item)
            throws HomeException
    {
        throw new HomeException("Operation Not Supported.");
    }

    @Override
    public Date calculateCycleStartDate(Context context, Date billingDate,
            int billingCycleDay, int spid, String subscriberId, Object item)
            throws HomeException
    {
        throw new HomeException("Operation Not Supported.");
    }

    @Override
    public double calculateRate(final Context context, final Date startDate, final Date billingDate,
            final int billingCycleDay, final int spid, final String subscriberId, final Object item)
            throws HomeException
    {
        throw new HomeException("Operation Not Supported.");
    }

    @Override
    public double calculateRate(Context context, Date billingDate,
            int billingCycleDay, int spid, String subscriberId, Object item)
            throws HomeException
    {
        throw new HomeException("Operation Not Supported.");
    }

    @Override
    public double calculateRefundRate(Context context, Date billingDate,
            int billingCycleDay, int spid, String subscriberId, Object item)
            throws HomeException
    {
        throw new HomeException("Operation Not Supported.");
    }

    @Override
    public Date calculateCycleEndDate(Context context, Date billingDate,
            int billingCycleDay, int spid)
    {
        throw new UnsupportedOperationException("Operation Not Supported.");
    }

    @Override
    public Date calculateCycleStartDate(Context context, Date billingDate,
            int billingCycleDay, int spid)
    {
        throw new UnsupportedOperationException("Operation Not Supported.");
    }

    @Override
    public double calculateRate(Context context, Date billingDate,
            int billingCycleDay, int spid)
    {
        throw new UnsupportedOperationException("Operation Not Supported.");
    }

    public static ChargingCycleHandler instance()
    {
        if (handler==null)
        {
            handler = new DefaultHandler();
        }
        return handler;
    }
}
