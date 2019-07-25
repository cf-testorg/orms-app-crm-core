package com.redknee.app.crm.bean.core;


import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.Service;
import com.redknee.app.crm.bean.ServicePeriodEnum;
import com.redknee.app.crm.support.HomeSupportHelper;


public class ServiceFee2 extends com.redknee.app.crm.bean.ServiceFee2 implements ContextAware
{
    private Service service_ = null;
    
    public void setService(Service service)
    {
        service_ = service;
    }
    
    public Service getService(Context context) throws HomeException
    {
        if (this.service_ == null && context != null)
        {
            service_ = HomeSupportHelper.get(context).findBean(
                    context,
                    Service.class,
                    this.getServiceId());
            if (this.service_ == null)
            {
                LogSupport.minor(context, this, "Service " + this.getServiceId()
                        + " does not exist.");
            }
        }
        return service_;
    }
    
    @Override
    public ServicePeriodEnum getServicePeriod()
    {
        if ( getContext() == null || getServiceId()<=0 )
        {
           return super.getServicePeriod();
        }
        else
        {
            try
            {
               return this.getService(getContext()).getChargeScheme();
            }
            catch (Throwable t)
            {
            }
    
            return super.getServicePeriod();
        }
    }
    
    @Override
    public int getRecurrenceInterval()
    {
        if ( getContext() == null || getServiceId()<=0 )
        {
           return super.getRecurrenceInterval();
        }
        else
        {
            try
            {
               return this.getService(getContext()).getRecurrenceInterval();
            }
            catch (Throwable t)
            {
            }
    
            return super.getRecurrenceInterval();
        }
    }  
    
    Context context_;

    
    public Context getContext()
    {
        return context_;
    }

    
    public void setContext(Context context)
    {
        context_ = context;
    }
    
    
}
