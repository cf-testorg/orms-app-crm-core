package com.redknee.app.crm.service;

import java.util.Date;

import com.redknee.framework.xhome.context.Context;


public interface ChargingCycleHandler
{
    public double calculateRate(final Context context, final Date billingDate, final int billingCycleDay, final int spid);
    public Date calculateCycleStartDate(final Context context, final Date billingDate, final int billingCycleDay, final int spid);
    public Date calculateCycleEndDate(final Context context, final Date billingDate, final int billingCycleDay, final int spid);
    
}
