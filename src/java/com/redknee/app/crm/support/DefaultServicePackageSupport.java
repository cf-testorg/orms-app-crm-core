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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.bean.ServicePackageHome;
import com.redknee.app.crm.bean.ServicePackageVersionHome;
import com.redknee.app.crm.bean.ServicePackageVersionXInfo;
import com.redknee.app.crm.bean.core.ServicePackage;
import com.redknee.app.crm.bean.core.ServicePackageVersion;


/**
 * Support class for service packages.
 *
 * @author larry.xia@redknee.com
 */
public class DefaultServicePackageSupport implements ServicePackageSupport
{
    protected static ServicePackageSupport instance_ = null;


    public static ServicePackageSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultServicePackageSupport();
        }
        return instance_;
    }


    protected DefaultServicePackageSupport()
    {
    }
    
    /**
     * {@inheritDoc}
     */
    public ServicePackage getServicePackage(final Context ctx, final int id) throws HomeException
    {
        return HomeSupportHelper.get(ctx).findBean(ctx, ServicePackage.class, Integer.valueOf(id));
    }


    /**
     * {@inheritDoc}
     */
    public ServicePackageVersion getServicePackageVersion(final Context context, final int id, final int version)
        throws HomeException
    {
        final And condition = new And();
        condition.add(new EQ(ServicePackageVersionXInfo.ID, id));
        condition.add(new EQ(ServicePackageVersionXInfo.VERSION, version));
        
        return HomeSupportHelper.get(context).findBean(context, ServicePackageVersion.class, condition);
    }


    /**
     * {@inheritDoc}
     */
    public ServicePackageVersion getCurrentVersion(final Context ctx, final int packageId) throws HomeException
    {
        final ServicePackage pack = HomeSupportHelper.get(ctx).findBean(ctx, ServicePackage.class, Integer.valueOf(packageId));
        ServicePackageVersion version = null;
        if (pack != null)
        {
            final And condition = new And();
            condition.add(new EQ(ServicePackageVersionXInfo.ID, Integer.valueOf(packageId)));
            condition.add(new EQ(ServicePackageVersionXInfo.VERSION, Integer.valueOf(pack.getCurrentVersion())));
            
            version = HomeSupportHelper.get(ctx).findBean(ctx, ServicePackageVersion.class, condition);
        }
        return version;
    }


    /**
     * {@inheritDoc}
     */
    public Home getServicePackageHome(final Context context) throws HomeException
    {
        final Home home = (Home) context.get(ServicePackageHome.class);
        if (home == null)
        {
            throw new HomeException("System error: service package home does not exist");
        }
        return home;
    }


    /**
     * {@inheritDoc}
     */
    public Home getServicePackageVersionHome(final Context context) throws HomeException
    {
        final Home home = (Home) context.get(ServicePackageVersionHome.class);
        if (home == null)
        {
            throw new HomeException("System error: service package version home does not exist");
        }
        return home;
    }
}
