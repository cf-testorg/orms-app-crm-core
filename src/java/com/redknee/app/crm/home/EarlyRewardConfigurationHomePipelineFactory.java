/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily
 * available. Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee. No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used in
 * accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */

package com.redknee.app.crm.home;

import com.redknee.app.crm.bean.EarlyRewardConfiguration;
import com.redknee.app.crm.bean.IdentifierEnum;
import com.redknee.app.crm.sequenceId.IdentifierSettingHome;
import com.redknee.app.crm.support.IdentifierSequenceSupportHelper;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.msp.SpidAwareHome;

/**
 * Home pipeline factory for late fee configuration.
 * 
 * @author cindy.wong@redknee.com
 * @since 8.4
 */
public class EarlyRewardConfigurationHomePipelineFactory implements
        PipelineFactory
{
    /**
     * {@inheritDoc}
     */
    public Home createPipeline(final Context context,
            @SuppressWarnings("unused") final Context serverContext)
            throws HomeException
    {
        Home home = StorageSupportHelper.get(context).createHome(context,
                EarlyRewardConfiguration.class, "EarlyRewardConfiguration");

        home = new SortingHome(context, home);

        home = new SpidAwareHome(context, home);

        home = new IdentifierSettingHome(context, home,
                IdentifierEnum.EARLY_REWARD_CONFIGURATION_ID, null);

        IdentifierSequenceSupportHelper.get(context).ensureNextIdIsLargeEnough(context,
                IdentifierEnum.EARLY_REWARD_CONFIGURATION_ID, home);

        return home;
    }
}
