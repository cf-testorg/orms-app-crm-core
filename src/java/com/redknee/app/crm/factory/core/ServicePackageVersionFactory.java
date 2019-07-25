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
package com.redknee.app.crm.factory.core;

import java.util.Date;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextFactory;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.ServicePackage;
import com.redknee.app.crm.bean.ServicePackageVersionID;
import com.redknee.app.crm.bean.core.ServicePackageVersion;
import com.redknee.app.crm.factory.ConstructorCallingBeanFactory;
import com.redknee.app.crm.support.BeanLoaderSupportHelper;
import com.redknee.app.crm.support.CalendarSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;

/**
 * Create ServicePackageVersions the same as the nextVersion.
 *
 * @author paul.sperneac@redknee.com
 */
public class ServicePackageVersionFactory extends ConstructorCallingBeanFactory<ServicePackageVersion>
{
    private static ContextFactory instance_ = null;
    public static ContextFactory instance()
    {
        if (instance_ == null)
        {
            instance_ = new ServicePackageVersionFactory();
        }
        return instance_;
    }
    
    protected ServicePackageVersionFactory()
    {
        super(ServicePackageVersion.class);
    }
    
    /**
     * Creates a new version of a service package.  The new version is largely
     * dependent on the previous version (if one exists).
     */
    @Override
    public Object create(final Context context)
    {
        ServicePackageVersion newVersion;

        final ServicePackage pack = BeanLoaderSupportHelper.get(context).getBean(context, ServicePackage.class);

        if (pack != null)
        {
            try
            {
                newVersion = createNewVersion(context, pack);
            }
            catch (final HomeException exception)
            {
                newVersion = (ServicePackageVersion) super.create(context);
                newVersion.setId(pack.getId());
                newVersion.setActivateDate(CalendarSupportHelper.get(context).getDateWithNoTimeOfDay(new Date()));

                new MinorLogMsg(
                    this,
                    "Failed to create new version based on previous version.",
                    exception).log(context);
            }
        }
        else
        {
            newVersion = (ServicePackageVersion) super.create(context);
            newVersion.setActivateDate(CalendarSupportHelper.get(context).getDateWithNoTimeOfDay(new Date()));
        }

        return newVersion;
    }


    /**
     * Creates a new version of the given ServicePackage based on the most recent
     * version created.  The new version is not created in the Home -- that must
     * be done manually.
     *
     * @param context The operating context.
     * @param plan The plan for which to create a new version.
     * @param key The version key used to look up the previous version.
     *
     * @return The new version.
     *
     * @exception HomeException Thrown if there are problems accessing Home data
     * in the context.
     */
    private ServicePackageVersion createNewVersion(
        final Context context,
        final ServicePackage pack)
        throws HomeException
    {
        final ServicePackageVersionID key =
            new ServicePackageVersionID(pack.getId(), pack.getNextVersion() - 1);

        final ServicePackageVersion previousVersion = HomeSupportHelper.get(context).findBean(context, ServicePackageVersion.class, key);
        ServicePackageVersion newVersion = null;

        if (previousVersion != null)
        {
            try
            {
                newVersion = (ServicePackageVersion)previousVersion.deepClone();

                // Note that the update of version number here is simply for
                // decoration.  The ServicePackageVersion home explicitly sets the
                // next version automatically upon creation.
                newVersion.setVersion(previousVersion.getVersion() + 1);
                newVersion.setActivation(null);

                final Date now = new Date();
                if (previousVersion.getActivation() == null)
                {
                    newVersion.setActivateDate(CalendarSupportHelper.get(context).getDayAfter(previousVersion.getActivateDate()));
                }
                else
                {
                    newVersion.setActivateDate(CalendarSupportHelper.get(context).getDateWithNoTimeOfDay(now));
                }
            }
            catch (final CloneNotSupportedException exception)
            {
                newVersion = previousVersion;
                new MinorLogMsg(
                    ServicePackageVersionFactory.class.getName(),
                    "Unable to clone previous version.",
                    exception).log(context);
            }
        }
        else
        {
            // Have to take into account the fact that there might not have been
            // a previous version.
            newVersion = (ServicePackageVersion) super.create(context);
            newVersion.setId(pack.getId());
            newVersion.setActivateDate(CalendarSupportHelper.get(context).getDateWithNoTimeOfDay(new Date()));
        }

        return newVersion;
    }

} // class
