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

package com.redknee.app.crm.home.core;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.SortingHome;

import com.redknee.app.crm.bean.AutoDepositReleaseCriteria;
import com.redknee.app.crm.bean.IdentifierEnum;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.sequenceId.IdentifierSettingHome;
import com.redknee.app.crm.support.IdentifierSequenceSupportHelper;
import com.redknee.app.crm.support.StorageSupportHelper;

/**
 * Provides a class from which to create the pipeline of Home decorators that process a AutoDepositReleaseCriteria
 * travelling between the application and the given delegate.
 *
 * @author cindy.wong@redknee.com
 */
public class CoreAutoDepositReleaseCriteriaHomePipelineFactory implements PipelineFactory
{
    /**
     * {@inheritDoc}
     */
    public Home createPipeline(final Context context, @SuppressWarnings("unused")
    final Context serverContext) throws HomeException
    {
        Home home = StorageSupportHelper.get(context).createHome(context, AutoDepositReleaseCriteria.class, "AutoDepositReleaseCriteria");

        home = addDecorators(context, home);

        home = new IdentifierSettingHome(context, home, IdentifierEnum.AUTO_DEPOSIT_RELEASE_CRITERIA_ID, null);

        IdentifierSequenceSupportHelper.get(context).ensureNextIdIsLargeEnough(context, IdentifierEnum.AUTO_DEPOSIT_RELEASE_CRITERIA_ID,
            home);

        return home;
    }

    /**
     * Add decorators to AutoDepositReleaseCriteriaHome.
     *
     * @param context
     *            The operating context.
     * @param rawHome
     *            The home to decorate.
     * @return The decorated home.
     */
    protected Home addDecorators(final Context context, final Home rawHome)
    {
        Home home = new SortingHome(rawHome);

        return home;
    }
}
