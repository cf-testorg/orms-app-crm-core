package com.redknee.app.crm.service;

import java.util.Date;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;


public interface ServicePeriodHandler
{
    public double calculateRate(final Context context, final Date startDate, final Date billingDate, final int billingCycleDay, final int spid, final String subscriberId, final Object item) throws HomeException;
    public double calculateRate(final Context context, final Date billingDate, final int billingCycleDay, final int spid, final String subscriberId, final Object item) throws HomeException;
    public Date calculateCycleStartDate(final Context context, final Date billingDate, final int billingCycleDay, final int spid, final String subscriberId, final Object item) throws HomeException;
    public Date calculateCycleEndDate(final Context context, final Date billingDate, final int billingCycleDay, final int spid, final String subscriberId, final Object item) throws HomeException;
    public double calculateRefundRate(final Context context, final Date billingDate, final int billingCycleDay, final int spid, final String subscriberId, final Object item) throws HomeException;
    
}
