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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.In;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.SubscriberTypeEnum;
import com.redknee.app.crm.bean.SubscriptionTypeAware;
import com.redknee.app.crm.bean.account.SubscriptionClassHome;
import com.redknee.app.crm.bean.account.SubscriptionClassXInfo;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.app.crm.technology.TechnologyEnum;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class SubscriptionClass extends com.redknee.app.crm.bean.account.SubscriptionClass implements SubscriptionTypeAware
{
    public SubscriptionType getSubscriptionType(final Context ctx)
    {
        return SubscriptionType.getSubscriptionType(ctx, getSubscriptionType());
    }
    

    public static SubscriptionClass getSubscriptionClass(final Context ctx, final long subscriptionClass)
    {
        SubscriptionClass classObj;
        try
        {
            classObj = getSubscriptionClassWithException(ctx, subscriptionClass);
        }
        catch (HomeException e)
        {
            LogSupport.minor(ctx, SubscriptionClass.class,
                    "Unable to retreive SubscriptionClass ID=" + subscriptionClass, e);
            classObj = null;
        }

        return classObj;
    }

    public static SubscriptionClass getSubscriptionClassWithException(final Context ctx, final long subscriptionClass)
        throws HomeException
    {
        return HomeSupportHelper.get(ctx).findBean(ctx, SubscriptionClass.class, subscriptionClass);
    }

    public static boolean isSubscriptionClassExisting(final Context ctx, final long subscriptionClass)
    {
        boolean result = false;
        try
        {
            result = HomeSupportHelper.get(ctx).hasBeans(ctx, SubscriptionClass.class, subscriptionClass);
        }
        catch (HomeException e)
        {
            LogSupport.minor(ctx, SubscriptionType.class,
                    "Unable to retreive SubscriptionClass ID=" + subscriptionClass, e);
        }
        return result;
    }

    public static SubscriptionClass getFirstSubscriptionClassObject(final Context ctx)
    {
        SubscriptionClass classObj;
        try
        {
            Collection<SubscriptionClass> beans = HomeSupportHelper.get(ctx).getBeans(ctx, SubscriptionClass.class, True.instance(), 1, true);
            if (beans != null && beans.size() > 0)
            {
                classObj = beans.iterator().next();
            }
            else
            {
                classObj = null;
            }
        }
        catch (HomeException e)
        {
            LogSupport.minor(ctx, SubscriptionClass.class,
                    "Unable to retreive first SubscriptionClass record", e);
            classObj = null;
        }

        return classObj;
    }

    public static SubscriptionType getSubscriptionTypeForClass(final Context ctx,
            final long subscriptionClassId, final long defaultId)
    {
        final SubscriptionType subType;
        if (subscriptionClassId != defaultId)
        {
            final SubscriptionClass subClass;
            subClass = SubscriptionClass.getSubscriptionClass(ctx, subscriptionClassId);
            if (subClass != null)
            {
                subType = subClass.getSubscriptionType(ctx);
            }
            else
            {
                subType = null;
            }
        }
        else
        {
            final SubscriptionClass subClass = SubscriptionClass.getFirstSubscriptionClassObject(ctx);
            subType = subClass.getSubscriptionType(ctx);
        }
        return subType;
    }

    public static TechnologyEnum getTechnologyForSubscriptionClass(final Context ctx, final long subscriptionClassId,
            final long defaultId)
    {
        final TechnologyEnum technology;
        if (subscriptionClassId != defaultId)
        {
            final SubscriptionClass subClass;
            subClass = SubscriptionClass.getSubscriptionClass(ctx, subscriptionClassId);
            if (subClass != null)
            {
                technology = TechnologyEnum.get((short) subClass.getTechnologyType());
            }
            else
            {
                technology = null;
            }
        }
        else
        {
            final SubscriptionClass subClass = SubscriptionClass.getFirstSubscriptionClassObject(ctx);
            technology = TechnologyEnum.get((short) subClass.getTechnologyType());
        }
        return technology;
    }
    
    public static Context filterHomeOnBillingType(final Context ctx, final SubscriberTypeEnum billingType)
    {
        return filterHomeOnBillingType(ctx, billingType, SubscriptionClassHome.class, false);
    }

    public static Context filterHomeOnBillingType(final Context ctx, final SubscriberTypeEnum billingType, Object homeKey,
            final boolean isConversion)
    {
        Context subCtx = ctx;
        if (!SubscriberTypeEnum.HYBRID.equals(billingType))
        {
            subCtx = ctx.createSubContext();
            final Home home = (Home) subCtx.get(homeKey);
            final Set<Integer> filteringSet = new HashSet<Integer>(2);

            if (isConversion)
            {
                if (billingType == SubscriberTypeEnum.POSTPAID)
                {
                    filteringSet.add(Integer.valueOf(SubscriberTypeEnum.PREPAID.getIndex()));
                }
                else if (billingType == SubscriberTypeEnum.PREPAID)
                {
                    filteringSet.add(Integer.valueOf(SubscriberTypeEnum.POSTPAID.getIndex()));
                }
            }
            else
            {
                filteringSet.add(Integer.valueOf(billingType.getIndex()));
                filteringSet.add(Integer.valueOf(SubscriberTypeEnum.HYBRID.getIndex()));
            }

            final Home alteredHome = home.where(ctx, new In(SubscriptionClassXInfo.SEGMENT_TYPE, filteringSet));
            subCtx.put(homeKey, alteredHome);
        }

        return subCtx;
    }
}
