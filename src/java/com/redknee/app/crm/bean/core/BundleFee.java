package com.redknee.app.crm.bean.core;


import java.util.Date;

import com.redknee.app.crm.bean.ServicePeriodEnum;
import com.redknee.app.crm.bundle.DurationTypeEnum;
import com.redknee.app.crm.bundle.RecurrenceTypeEnum;
import com.redknee.app.crm.bundle.service.CRMBundleProfile;
import com.redknee.app.crm.support.CalendarSupportHelper;
import com.redknee.app.crm.support.UserSupport;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;


public class BundleFee extends com.redknee.app.crm.bundle.BundleFee implements ContextAware
{
    private BundleProfile bundleProfile_ = null;
    Context context_;

    
    public void setBundleProfile(BundleProfile bundleProfile)
    {
        bundleProfile_ = bundleProfile;
    }
    
    @Override
    public ServicePeriodEnum getServicePeriod()
    {
        if ( getContext() == null || getId()<=0 )
        {
           return super.getServicePeriod();
        }
        else
        {
            try
            {
               if (this.getBundleProfile(getContext()).getChargingRecurrenceScheme().equals(ServicePeriodEnum.ONE_TIME))
               {
                   return ServicePeriodEnum.ONE_TIME;
               }
               else if (this.getBundleProfile(getContext()).getChargingRecurrenceScheme().equals(ServicePeriodEnum.MONTHLY))
               {
                   return ServicePeriodEnum.MONTHLY;
               }else if (this.getBundleProfile(getContext()).getChargingRecurrenceScheme().equals(ServicePeriodEnum.MULTIDAY))
               {
                   return ServicePeriodEnum.MULTIDAY;
               }else if (this.getBundleProfile(getContext()).getChargingRecurrenceScheme().equals(ServicePeriodEnum.MULTIMONTHLY))
               {
                   return ServicePeriodEnum.MULTIMONTHLY;
               }else if (this.getBundleProfile(getContext()).getChargingRecurrenceScheme().equals(ServicePeriodEnum.DAILY))
               {
                   return ServicePeriodEnum.DAILY;
               }else if (this.getBundleProfile(getContext()).getChargingRecurrenceScheme().equals(ServicePeriodEnum.WEEKLY))
               {
                   return ServicePeriodEnum.WEEKLY;
               }
               
            }
            catch (Throwable t)
            {
            }
    
            return super.getServicePeriod();
        }
    }
    
    public Date getEndDate()
    {
        if ( getContext() == null || getId()<=0 )
        {
           return super.getEndDate();
        }
        else
        {
            try
            {
               if (this.getBundleProfile(getContext()).getRecurrenceScheme().isOneTime())
               {
                   Date originalDate = super.getEndDate();
                   if(this.getBundleProfile(getContext()).isRepurchasable() && getRepurchaseHappened() 
                           && originalDate!=null && originalDate.after(this.getStartDate()))
                   {
                       return originalDate;
                   }
                   
                   if (this.getBundleProfile(getContext()).getRecurrenceScheme().equals(RecurrenceTypeEnum.ONE_OFF_FIXED_DATE_RANGE))
                   {
                       return (this.getBundleProfile(getContext()).getEndDate());
                   }
                   else
                   {
                       if (this.getBundleProfile(getContext()).getInterval() == DurationTypeEnum.DAY_INDEX)
                       {
                    	   if(this.getBundleProfile(getContext()).getChargingRecurrenceScheme() == ServicePeriodEnum.ONE_TIME)
                    	   {
                    		   int durationInHours = this.getBundleProfile(getContext()).getValidity() * 24;  // hours in the days
                    		   Date endDate = CalendarSupportHelper.get(getContext()).getHoursAfter(this.getStartDate(), durationInHours);
                    		   return endDate;
                    	   }
                    	   else
                    	   {
                    		   return CalendarSupportHelper.get(getContext()).findDateSecsBefore(1, CalendarSupportHelper.get(getContext()).getDateWithNoTimeOfDay(CalendarSupportHelper.get(getContext()).findDateDaysAfter(this.getBundleProfile(getContext()).getValidity(), this.getStartDate())));
                    	   }
                       }
                       /*
                        * TT#13100302028 : Bundles which are ONE_TIME and have expiry as BCD have incorrect EndDate populated in BundleAuxServ table. 
                        * This condition is being added because we don't want to return below end date for one time 
                        * bundles having duration type as BCD. Because, They should end before next Bill cycle date.
                        * Skipping the return of end date for one time - BCD bundle from here and 
                        * end date will be set in SubscriberSetBundleEndDateHome.java . 
                        */
                       else if(this.getBundleProfile(getContext()).getInterval() == DurationTypeEnum.MONTH_INDEX)
                       {
                    	   if(this.getBundleProfile(getContext()).isAuxiliary() 
                    			   && this.getBundleProfile(getContext()).getChargingRecurrenceScheme() == ServicePeriodEnum.ONE_TIME)
                    	   {
                    		   return CalendarSupportHelper.get(getContext()).findDateMonthsAfter(this.getBundleProfile(getContext()).getValidity(), this.getStartDate());
                    	   }
                    	   else
                    	   {
                    		   return CalendarSupportHelper.get(getContext()).findDateSecsBefore(1, CalendarSupportHelper.get(getContext()).getDateWithNoTimeOfDay(CalendarSupportHelper.get(getContext()).findDateMonthsAfter(this.getBundleProfile(getContext()).getValidity(), this.getStartDate())));
                    	   }
                       }
                       else
                       {
                           if(LogSupport.isDebugEnabled(getContext()))
                           {
                               LogSupport.debug(getContext(), this, "Not setting end date of one time bundle from BundleFee..." +
                               		"End date will updated from SubscriberSetBundleEndDateHome.java  ");
                           }
                       }
                   }
               }
               else
               {
                   return super.getEndDate();
               }
            }
            catch (Throwable t)
            {
            }
    
            return super.getEndDate();
        }
    }

    public void setRepurchaseHappened(boolean repurchaseHappened)
            throws IllegalArgumentException
    {
        super.setRepurchaseHappened(repurchaseHappened);
    }
    
    public void setRepurchaseHappened(Boolean repurchaseHappened)
        throws IllegalArgumentException
    {
        super.setRepurchaseHappened(repurchaseHappened.booleanValue());
    }
    
    public BundleProfile getBundleProfile(Context context) throws Exception
    {
    	int spid = fetchSpidFromBundleProfileOrUser(context);
        return getBundleProfile(context, spid);
    }

    /**
     * TT#13020107017 fixed. This is a hack solution. As one user can only be linked to single spid, UserSpid is little risky.
     * Adding an extra XDB call to fetch the spid from BSS DB.
     * @param context
     * @return
     * @throws Exception
     */
	private int fetchSpidFromBundleProfileOrUser(Context context) throws Exception {
		final int DEFAULT_SPID = -1;
		int spid = DEFAULT_SPID;
		Home xdbHome = (Home) context.get(com.redknee.app.crm.bundle.BundleProfileXDBHome.class);
		
		if(xdbHome != null)
		{
			try
			{
				com.redknee.app.crm.bundle.BundleProfile profile = (com.redknee.app.crm.bundle.BundleProfile) xdbHome.find(context, this.getId());
				if(profile != null)
				{
					spid = profile.getSpid();
				}
			}catch(HomeException he)
			{
				LogSupport.minor(context, this, "Unable to fetch spid from DB for BundleID :" + this.getId());
			}
		}
		
		if(spid == DEFAULT_SPID)
		{
			spid = UserSupport.getSpid(context);
		}
		return spid;
	}
    
    public BundleProfile getBundleProfile(Context ctx, int spId) throws Exception
    {
        if (this.bundleProfile_ == null && ctx != null)
        {
            CRMBundleProfile bundleSvc = (CRMBundleProfile) ctx.get(CRMBundleProfile.class);
            bundleProfile_ = bundleSvc.getBundleProfile(ctx, spId, this.getId());
            if (this.bundleProfile_ == null)
            {
                LogSupport.minor(ctx, this, "BundleProfile [ID=" + getId() + ", SPID=" + spId +"] does not exist.");
            }
        }
        return bundleProfile_;
    }
    
    public Context getContext()
    {
        return context_;
    }

    
    public void setContext(Context context)
    {
        context_ = context;
    }
    
}
