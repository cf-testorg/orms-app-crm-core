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
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.LT;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.PricePlan;
import com.redknee.app.crm.bean.PricePlanVersionID;
import com.redknee.app.crm.bean.PricePlanVersionXInfo;
import com.redknee.app.crm.bean.core.PricePlanVersion;
import com.redknee.app.crm.factory.ConstructorCallingBeanFactory;
import com.redknee.app.crm.support.BeanLoaderSupportHelper;
import com.redknee.app.crm.support.CalendarSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;


/**
 * Create PricePlanVersions the same as the nextVersion.
 *
 * @author gary.anderson@redknee.com
 */
public class PricePlanVersionFactory extends ConstructorCallingBeanFactory<PricePlanVersion>
{
    private static ContextFactory instance_ = null;
    public static ContextFactory instance()
    {
        if (instance_ == null)
        {
            instance_ = new PricePlanVersionFactory();
        }
        return instance_;
    }
    
    protected PricePlanVersionFactory()
    {
        super(PricePlanVersion.class);
    }
    
    /**
     * {@inheritDoc}
     *
     * Creates a new version of a price plan.  The new version is largely
     * dependent on the previous version (if one exists).
     */
    @Override
    public Object create(final Context context)
    {
        PricePlanVersion newVersion;

        final PricePlan plan = BeanLoaderSupportHelper.get(context).getBean(context, PricePlan.class);

        if (plan != null)
        {
            /*
             * [Cindy Wong]: As part of the change for TT#8111900049, price plan
             * version numbers are no longer guaranteed to be continuous.
             */
            try
            {
                newVersion = createNewVersion(context, plan);
            }
            catch (final HomeException exception)
            {
                newVersion = (PricePlanVersion) super.create(context);
                newVersion.setId(plan.getId());
                newVersion.setActivateDate(CalendarSupportHelper.get(context).getDateWithNoTimeOfDay(new Date()));

                new MinorLogMsg(
                    this,
                    "Failed to create new version based on previous version.",
                    exception).log(context);
            }
        }
        else
        {
            newVersion = (PricePlanVersion) super.create(context);
            newVersion.setActivateDate(CalendarSupportHelper.get(context).getDateWithNoTimeOfDay(new Date()));
        }

        return newVersion;
    }


    /**
     * Creates a new version of the given PricePlan based on the most recent
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
    private PricePlanVersion createNewVersion(
        final Context context,
        final PricePlan plan)
        throws HomeException
    {
        PricePlanVersion newVersion = null;
        
        final PricePlanVersion previousVersion = findHighestVersion(context, plan);
        if (previousVersion != null)
        {
            try
            {
                newVersion = (PricePlanVersion)previousVersion.deepClone();
            }
            catch (final CloneNotSupportedException exception)
            {
                newVersion = previousVersion;
                new MinorLogMsg(
                    PricePlanVersionFactory.class.getName(),
                    "Unable to clone previoius version.",
                    exception).log(context);
            }

            // Note that the update of version number here is simply for
            // decoration.  The PricePlanVersion home explicitly sets the
            // next version automatically upon creation.
            newVersion.setVersion(previousVersion.getVersion() + 1);
            newVersion.setActivation(null);

            // cleaning the description, new version needs new description
            newVersion.clearDescription();

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
        else
        {
            // Have to take into account the fact that there might not have been
            // a previous version.
            newVersion = (PricePlanVersion) super.create(context);
            newVersion.setId(plan.getId());
            newVersion.setActivateDate(CalendarSupportHelper.get(context).getDateWithNoTimeOfDay(new Date()));
        }

        return newVersion;
    }

    
    /**
     * Finds the price plan version of a given price plan with the highest
     * version number.
     * 
     * @param context
     *            The operating context.
     * @param plan
     *            The plan for which to look up the most recent price plan
     *            version.
     * @return The price plan version with the highest version number.
     * @throws HomeException
     *             Thrown if there are problems accessing price plan or price
     *             plan version home.
     */
    public static PricePlanVersion findHighestVersion(final Context context,
        final PricePlan plan) throws HomeException
    {
        /*
         * [Cindy Wong]: As part of the change for TT#8111900049, price plan
         * version numbers are no longer guaranteed to be continuous. This
         * method looks up the price plan with the highest version number.
         */
        And and = new And();
        and.add(new EQ(PricePlanVersionXInfo.ID, plan.getId()));
        and.add(new LT(PricePlanVersionXInfo.VERSION, Integer.MAX_VALUE));
        
        Object max = HomeSupportHelper.get(context).max(context, PricePlanVersionXInfo.VERSION, new EQ(PricePlanVersionXInfo.ID, plan.getId()));
        if (max instanceof Number)
        {
            int version = ((Number) max).intValue();
            return HomeSupportHelper.get(context).findBean(context, PricePlanVersion.class, 
                    new PricePlanVersionID(plan.getId(), version));
        }
        
        return null;
    }
} // class
