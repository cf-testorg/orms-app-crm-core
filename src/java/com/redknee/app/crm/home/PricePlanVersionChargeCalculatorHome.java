package com.redknee.app.crm.home;

import java.util.Collection;

import com.redknee.app.crm.bean.ChargingCycleEnum;
import com.redknee.app.crm.bean.PricePlan;
import com.redknee.app.crm.bean.ServiceFee2;
import com.redknee.app.crm.bean.ServicePeriodEnum;
import com.redknee.app.crm.bean.ServicePreferenceEnum;
import com.redknee.app.crm.bean.core.PricePlanVersion;
import com.redknee.app.crm.bundle.BundleFee;
import com.redknee.app.crm.support.FrameworkSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.LogSupport;

public class PricePlanVersionChargeCalculatorHome extends HomeProxy
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PricePlanVersionChargeCalculatorHome(Context ctx, Home home)
    {
        super(ctx, home);
    }
    
    @Override
    public Object create(Context ctx, Object obj) throws HomeException, HomeInternalException
    {
        PricePlanVersion ppv = (PricePlanVersion) obj;
        ppv.setCharge(calculateCharge(ppv));
        Object ret = super.create(ctx, ppv);
        updatePricePlan(ctx, ppv);
        return ret;
    }

    @Override
    public Object store(Context ctx, Object obj) throws HomeException, HomeInternalException
    {
        PricePlanVersion ppv = (PricePlanVersion) obj;
        ppv.setCharge(calculateCharge(ppv));
        Object ret = super.store(ctx, ppv);
        updatePricePlan(ctx, ppv);
        return ret;
    }
    
    private void updatePricePlan(Context ctx, PricePlanVersion ppv)
    {
        Context subCtx = ctx.createSubContext();
        PricePlan plan = null;
        FrameworkSupportHelper.get(subCtx).initExceptionListener(subCtx, this);
        try
        {
            plan = ppv.getPricePlan(subCtx);
            if (plan!=null)
            {
                if (plan.getCurrentVersion() == ppv.getVersion())
                {
                    plan.setCurrentVersionCharge(ppv.getCharge());
                    plan.setCurrentVersionChargeCycle(ppv.getChargeCycle());
                    HomeSupportHelper.get(subCtx).storeBean(subCtx, plan);
                }
            }
            else
            {
                LogSupport.minor(subCtx, this, "Unable to retrieve price plan " +  ppv.getId() + ".");
            }
        }
        catch (HomeException e)
        {
            LogSupport.minor(subCtx, this, "Unable to update price plan current charge for price plan " + ppv.getId(), e);
            FrameworkSupportHelper.get(subCtx).notifyExceptionListener(subCtx, new HomeException("Unable to update price plan current charge", e));
        }
        FrameworkSupportHelper.get(subCtx).printCapturedExceptionsAsWarnings(subCtx, this);
    }
    
    private long calculateCharge(PricePlanVersion ppv)
    {
        long charge = 0;
        
        for (ServiceFee2 fee : (Collection<ServiceFee2>) ppv.getServicePackageVersion().getServiceFees().values())
        {
            if ((ServicePreferenceEnum.MANDATORY.equals(fee.getServicePreference())
                    || ServicePreferenceEnum.DEFAULT.equals(fee.getServicePreference())) && fee.getFee()!=0)
            {
                charge += calculateItemCharge(fee.getFee(), fee.getServicePeriod(), fee.getRecurrenceInterval(), ppv.getChargeCycle());
            }
        }

        for (BundleFee fee : (Collection<BundleFee>) ppv.getServicePackageVersion().getBundleFees().values())
        {
            if ((ServicePreferenceEnum.MANDATORY.equals(fee.getServicePreference())
                    || ServicePreferenceEnum.DEFAULT.equals(fee.getServicePreference())) && fee.getFee()!=0)
            {
                charge += calculateItemCharge(fee.getFee(), fee.getServicePeriod(), 1, ppv.getChargeCycle());
            }
        }
        
        return charge;
        
    }
    
    private long calculateItemCharge(long fee, ServicePeriodEnum servicePeriod, long recurrenceInterval, ChargingCycleEnum cycle)
    {
        long result = 0;
        switch (cycle.getIndex())
        {
            case ChargingCycleEnum.MONTHLY_INDEX:
                result = calculateItemMonthlyCharge(fee, servicePeriod, recurrenceInterval);
                break;
            case ChargingCycleEnum.DAILY_INDEX:
                result = calculateItemDailyCharge(fee, servicePeriod, recurrenceInterval);
                break;
            case ServicePeriodEnum.WEEKLY_INDEX:
                result = calculateItemWeeklyCharge(fee, servicePeriod, recurrenceInterval);
                break;
        }
        return result;
    }
    
    private long calculateItemMonthlyCharge(long fee, ServicePeriodEnum servicePeriod, long recurrenceInterval)
    {
        long result = fee;
        switch (servicePeriod.getIndex())
        {
            case ServicePeriodEnum.ANNUAL_INDEX:
                result = result/12;
                break;
            case ServicePeriodEnum.DAILY_INDEX:
                result = result*30;
                break;
            case ServicePeriodEnum.MULTIMONTHLY_INDEX:
                result = result/recurrenceInterval;
                break;
            case ServicePeriodEnum.MULTIDAY_INDEX:
                result = result*30/recurrenceInterval;
                break;
            case ServicePeriodEnum.WEEKLY_INDEX:
                result = result * 4;
                break;
            case ServicePeriodEnum.ONE_TIME_INDEX:
                result = 0;
                break;
            case ServicePeriodEnum.MONTHLY_INDEX:
                break;
        }
        return result;
    }

    private long calculateItemDailyCharge(long fee, ServicePeriodEnum servicePeriod, long recurrenceInterval)
    {
        long result = fee;
        switch (servicePeriod.getIndex())
        {
            case ServicePeriodEnum.ANNUAL_INDEX:
                result = result / 365;
                break;
            case ServicePeriodEnum.DAILY_INDEX:
                break;
            case ServicePeriodEnum.MULTIMONTHLY_INDEX:
                result = (result/30) / recurrenceInterval;
                break;
            case ServicePeriodEnum.MULTIDAY_INDEX:
                result = result / recurrenceInterval;
                break;
            case ServicePeriodEnum.WEEKLY_INDEX:
                result = result / 7;
                break;
            case ServicePeriodEnum.ONE_TIME_INDEX:
                result = 0;
                break;
            case ServicePeriodEnum.MONTHLY_INDEX:
                result = result / 30;
                break;
        }
        return result;
    }

    private long calculateItemWeeklyCharge(long fee, ServicePeriodEnum servicePeriod, long recurrenceInterval)
    {
        long result = fee;
        switch (servicePeriod.getIndex())
        {
            case ServicePeriodEnum.ANNUAL_INDEX:
                result = result / 56;
                break;
            case ServicePeriodEnum.DAILY_INDEX:
                result = result * 7;
                break;
            case ServicePeriodEnum.MULTIMONTHLY_INDEX:
                result = result / 4 / recurrenceInterval;
                break;
            case ServicePeriodEnum.MULTIDAY_INDEX:
                result = result * 7 / recurrenceInterval;
                break;
            case ServicePeriodEnum.WEEKLY_INDEX:
                break;
            case ServicePeriodEnum.ONE_TIME_INDEX:
                result = 0;
                break;
            case ServicePeriodEnum.MONTHLY_INDEX:
                result = result / 4;
                break;
        }
        return result;
    }

}
