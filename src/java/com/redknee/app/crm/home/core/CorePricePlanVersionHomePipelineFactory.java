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

import com.redknee.app.crm.bean.core.PricePlanVersion;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.home.PricePlanVersionChargeCalculatorHome;
import com.redknee.app.crm.xdb.LargeBlobCustomPricePlanVersionXDBHome;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.LRUCachingHome;
import com.redknee.framework.xhome.home.SortingHome;

/**
 * Creates the home pipeline for {@link PricePlanVersion}.
 *
 * @author cindy.wong@redknee.com
 */
public class CorePricePlanVersionHomePipelineFactory implements PipelineFactory
{
    /**
     * {@inheritDoc}
     */
    public Home createPipeline(final Context context, final Context serverContext)
    {
        Home home = new LargeBlobCustomPricePlanVersionXDBHome(context, "PRICEPLANVERSION");

        home = new AuditJournalHome(context, home);
        
        // Install a home to adapt between business logic bean and data bean
        home = new AdapterHome(
                context, 
                home, 
                new ExtendedBeanAdapter<com.redknee.app.crm.bean.PricePlanVersion, com.redknee.app.crm.bean.core.PricePlanVersion>(
                        com.redknee.app.crm.bean.PricePlanVersion.class, 
                        com.redknee.app.crm.bean.core.PricePlanVersion.class));
        
        home = new LRUCachingHome(context, PricePlanVersion.class, true, home);
        
        home = new PricePlanVersionChargeCalculatorHome(context, home);

        home = new SortingHome(home);
        
        return home;
    }
}
