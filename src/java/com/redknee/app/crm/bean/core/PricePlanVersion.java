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
package com.redknee.app.crm.bean.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextLocator;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.PricePlanSubTypeEnum;
import com.redknee.app.crm.bean.Service;
import com.redknee.app.crm.bean.ServicePackageVersion;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;
import com.redknee.app.crm.xhome.adapter.ServicePackageVersionBeanAdapter;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class PricePlanVersion extends com.redknee.app.crm.bean.PricePlanVersion
{
    /**
     * {@inheritDoc}
     * 
     * @deprecated Use contextualized version of method.
     */
    @Deprecated
    @Override
    public ServicePackageVersion getServicePackageVersion()
    {
        return getServicePackageVersion(ContextLocator.locate());
    }

    public com.redknee.app.crm.bean.core.ServicePackageVersion getServicePackageVersion(Context ctx)
    {
        ServicePackageVersion version = super.getServicePackageVersion();

        try
        {
            // Adapt between business logic bean and data bean
            return (com.redknee.app.crm.bean.core.ServicePackageVersion) new ServicePackageVersionBeanAdapter().adapt(ctx, version);
        }
        catch (HomeException e)
        {
            new MinorLogMsg(this, e.getClass().getSimpleName() + " occurred in " + PricePlan.class.getSimpleName() + ".getVersions(): " + e.getMessage(), e).log(ctx);
            return null;
        }
    }

    /**
     * Returns a Set of Keys to subscribed Services.
     */
    public Set<Long> getServices(Context ctx)
    {
        Set<Long> set = new HashSet<Long>();

        Map<Long, ServiceFee2> fees=getServiceFees(ctx);

        // TODO 2007-04-26 getServiceFees(ctx) is returning a Map where the keys are the service IDs, so just
        // return the key set, no need to copy in a separate set. If a copy is needed at least reuse the Long IDs
        if (fees != null)
        {
            for ( Iterator i = fees.values().iterator() ; i.hasNext() ; )
            {
                ServiceFee2 fee = (ServiceFee2) i.next();

                set.add(Long.valueOf(fee.getServiceId()));
            }
        }

        return set;
    }
    
    /**
     * We need to know the services that are not marked RP=true.
     * @param ctx
     * @return
     */
    public Set<Long> getServicesExcludingThoseMarkedRestrictProvisioning(Context ctx)
    {
        Set<Long> set = new HashSet<Long>();
        Map<Long, ServiceFee2> fees=getServiceFees(ctx);

        if (fees != null)
            for (ServiceFee2 fee : fees.values())
            {
                Service service = null;
                try
                {
                    service = fee.getService(ctx);
                } 
                catch (Exception e)
                {
                    if(LogSupport.isDebugEnabled(ctx))
                        LogSupport.debug(ctx, this, 
                                "Unexpected: could not find Service instance for the serviceFee: "+ fee, e);
                }
                
                if(service!=null && service.getRestrictProvisioning())
                    continue;
                
                set.add(Long.valueOf(fee.getServiceId()));
            }

        return set;
    }

    public Map<Long, ServiceFee2> getServiceFees(Context ctx)
    {
        ServicePackageVersion servicePackageVersion = getServicePackageVersion();
        if(servicePackageVersion==null)
        {
            return null;
        }
        
        com.redknee.app.crm.bean.core.ServicePackageVersion coreVersion = null; 
        if (servicePackageVersion instanceof com.redknee.app.crm.bean.core.ServicePackageVersion)
        {
            coreVersion = (com.redknee.app.crm.bean.core.ServicePackageVersion) servicePackageVersion; 
        }
        else
        {
            Adapter beanAdapter = new ExtendedBeanAdapter<ServicePackageVersion, com.redknee.app.crm.bean.core.ServicePackageVersion>(ServicePackageVersion.class, com.redknee.app.crm.bean.core.ServicePackageVersion.class);
            try
            {
                coreVersion = (com.redknee.app.crm.bean.core.ServicePackageVersion) beanAdapter.adapt(ctx, servicePackageVersion);
            }
            catch (HomeException e)
            {
                if (LogSupport.isDebugEnabled(ctx))
                {
                    new DebugLogMsg(this, e.getClass().getSimpleName() + " occurred in " + PricePlanVersion.class.getSimpleName() + ".getServiceFees(): " + e.getMessage(), e).log(ctx);
                }
            }
        }
        
        if (coreVersion != null)
        {
            return coreVersion.getServiceFees(ctx);
        }
        
        return Collections.emptyMap();
    }

    public void setServiceFees(Map<Long, ServiceFee2> fees)
    {
        if(getServicePackageVersion()==null)
        {
            return;
        }

        getServicePackageVersion().setServiceFees(fees);
    }
    
    
    public ServiceFee2 getPrimaryService(Context ctx)
    {
        Map serviceMap = getServicePackageVersion(ctx).getServiceFees();
        if(serviceMap != null && !serviceMap.isEmpty())
        {
            Collection<ServiceFee2> services = serviceMap.values();
            for (ServiceFee2 fee : services)
            {
                if(fee.isPrimary())
                {
                    return fee;
                }
            }
        }
        return null;
    }
    
    /**
     *  Returns the list of all Primary services 
     *  since there can be multiple primary/ membership services in PickNPay priceplan 
     * @throws HomeException 
     */
    public Collection<ServiceFee2> getPrimaryServicesList(Context ctx) throws HomeException
    {
    	Collection<ServiceFee2> primaryServicesList = new ArrayList<ServiceFee2>();
    	
    	PricePlan pricePlan = getPricePlan(ctx);
    	
    	//if(pricePlan.getPricePlanSubType().equals(PricePlanSubTypeEnum.PICKNPAY))
    	{
    		Map serviceMap = getServicePackageVersion(ctx).getServiceFees();
            if(serviceMap != null && !serviceMap.isEmpty())
            {
                Collection<ServiceFee2> services = serviceMap.values();
                for (ServiceFee2 fee : services)
                {
                    if(fee.isPrimary())
                    {
                    	primaryServicesList.add(fee);
                    }
                }
            }
    	}
        
        return primaryServicesList;
    }
    

    public void clearDescription()
    {
        this.description_ = "";
    }
    
    /**
     * May Return NULL if Price Plan cannot be retrieved.
     * @param ctx
     * @return
     */
    public synchronized PricePlan getPricePlan(Context ctx)
        throws HomeException
    {
        return pricePlan_ = HomeSupportHelper.get(ctx).findBean(ctx, PricePlan.class, this.getId());
    }
    
    
    /**
     * Returns whether this price plan has been activated.
     * 
     * @return Whether this price plan hsa been activated.
     */
    public boolean isActivated()
    {
        return getActivation() != null && getActivation().getTime() > 0;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        // XGen generated clone() does almost all that needs to be done, almost.
        final PricePlanVersion cln = (PricePlanVersion) super.clone();
        if( cln.pricePlan_ != null)
        {
            cln.pricePlan_.setContext(null);
        }

        return cln;
    }
    
    private PricePlan pricePlan_ = null;
}
