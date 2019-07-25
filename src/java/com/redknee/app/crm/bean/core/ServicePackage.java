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

import java.util.Iterator;
import java.util.Map;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextLocator;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.ChargingLevelEnum;

import com.redknee.app.crm.bean.ServiceFee2;
import com.redknee.app.crm.bean.ServicePackageVersion;
import com.redknee.app.crm.support.ServicePackageSupport;
import com.redknee.app.crm.support.ServicePackageSupportHelper;
import com.redknee.app.crm.xhome.adapter.ServicePackageVersionBeanAdapter;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class ServicePackage extends com.redknee.app.crm.bean.ServicePackage
{
    /**
     * {@inheritDoc}
     * 
     * @deprecated Use contextualized version of method.
     */
    @Deprecated
  
    public ServicePackageVersion getVersions()
    {
        return getVersions(ContextLocator.locate());
    }
   
    public com.redknee.app.crm.bean.core.ServicePackageVersion getVersions(Context ctx)
    {
    	 try
         {
 //ServicePackageVersion version = super.getVersions();
        ServicePackageVersion version = (ServicePackageVersion) super.versions(ctx).selectAll().iterator().next();

       
            // Adapt between business logic bean and data bean
            return (com.redknee.app.crm.bean.core.ServicePackageVersion) new ServicePackageVersionBeanAdapter().adapt(ctx, version);
        }
        catch (HomeException e)
        {
            new MinorLogMsg(this, e.getClass().getSimpleName() + " occurred in " + ServicePackage.class.getSimpleName() + ".getVersions(): " + e.getMessage(), e).log(ctx);
            return null;
        }
    }


    /**
     * Updates the total charge.
     *
     * @see com.redknee.app.crm.bean.AbstractServicePackage#getTotalCharge()
     */
    public void updateTotalCharge(final Context ctx)
    {
        long total = 0;
        if (getChargingLevel() == ChargingLevelEnum.ATTRIBUTES)
        {
            total += getTotalServiceFee(ctx);
            total += getTotalBundleFee(ctx);
        }
        else
        {
            total += getRecurringRecharge();
        }

        setTotalCharge(total);
    }


    /**
     * Returns the total service fee.
     *
     * @param ctx
     *            The operating context.
     * @return Total service fee.
     */
    private long getTotalServiceFee(final Context ctx)
    {
        long total = 0;

        final com.redknee.app.crm.bean.core.ServicePackageVersion version = getCurrentVersion(ctx);

        // for newly created packages, there are no versions
        if (version == null)
        {
            return total;
        }

        final Map services = version.getServiceFees(ctx);

        if (services != null && services.size() > 0)
        {
            for (final Iterator i = services.keySet().iterator(); i.hasNext();)
            {
                final ServiceFee2 fee = (ServiceFee2) services.get(i.next());
                total += fee.getFee();
            }
        }

        return total;
    }


    /**
     * Returns the total bundle fee.
     *
     * @param ctx
     *            The operating context.
     * @return Total bundle fee.
     */
    private long getTotalBundleFee(final Context ctx)
    {
        long total = 0;

        final com.redknee.app.crm.bean.core.ServicePackageVersion version = getCurrentVersion(ctx);

        // for newly created service packages, there is no version
        if (version == null)
        {
            return 0;
        }

        final Map bundles = version.getBundleFees(ctx);

        if (bundles != null && bundles.size() > 0)
        {
            for (final Iterator i = bundles.keySet().iterator(); i.hasNext();)
            {
                final BundleFee fee = (BundleFee) bundles.get(i.next());
                total += fee.getFee();
            }
        }

        return total;
    }


    /**
     * Returns the object ServicePackageVersion for the int servicePackageVersion held in
     * this object
     *
     * @param ctx
     *            The operating context.
     * @return package version found or null
     * @deprecated Use {@link ServicePackageSupport#getCurrentVersion(Context, int)}
     *             instead.
     */
    @Deprecated
    public com.redknee.app.crm.bean.core.ServicePackageVersion getCurrentVersion(final Context ctx)
    {
        com.redknee.app.crm.bean.core.ServicePackageVersion version = null;
        try
        {
            version = ServicePackageSupportHelper.get(ctx).getCurrentVersion(ctx, getId());
        }
        catch (final HomeException e)
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, e.getMessage(), e).log(ctx);
            }
        }

        return version;
    }


    /**
     * Retrieves a package from the ServicePackageHome.
     *
     * @param ctx
     *            The operating context.
     * @param packageId
     *            Package ID.
     * @return package found or null.
     * @deprecated Use {@link ServicePackageSupport#getServicePackage(Context, int)}
     *             instead.
     */
    @Deprecated
    public static ServicePackage findServicePackage(final Context ctx, final int packageId)
    {
        ServicePackage servicePackage = null;
        try
        {
            servicePackage = ServicePackageSupportHelper.get(ctx).getServicePackage(ctx, packageId);
        }
        catch (final HomeException e)
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(ServicePackage.class.getName(), e.getMessage(), e).log(ctx);
            }
        }

        return servicePackage;
    }
}
