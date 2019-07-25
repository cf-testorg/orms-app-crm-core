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
package com.redknee.app.crm.home.core;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.app.crm.adjustmenttype.ManualAdjustmentTypeCategoryValidator;
import com.redknee.app.crm.adjustmenttype.SystemAdjustmentTypeCategoryValidator;
import com.redknee.app.crm.bean.AdjustmentType;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;
import com.redknee.app.crm.xhome.home.AdjustmentTypeDeprecateAwareHome;
import com.redknee.app.crm.xhome.home.ConfigShareTotalCachingHome;
import com.redknee.app.crm.xhome.home.ContextRedirectingHome;
import com.redknee.app.crm.xhome.home.SetPrincipalRmiServerHome;
import com.redknee.app.crm.xhome.home.SystemAdjustmentTypeProtectionHome;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.CloneAdapter;
import com.redknee.framework.xhome.home.GenericTransientHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.ReadOnlyHome;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.home.ValidatingHome;
import com.redknee.framework.xhome.visitor.Visitor;

/**
 * Provides a class from which to create the pipeline of Home decorators that
 * process a Transaction travelling between the application and the given
 * delegate.
 * 
 * @author arturo.medina@redknee.com
 */
public class CoreAdjustmentTypeHomePipelineFactory implements PipelineFactory
{
    @Override
    public Home createPipeline(Context ctx, Context serverCtx)
            throws RemoteException, HomeException, IOException
    {
        Home systemHome = StorageSupportHelper.get(ctx).createHome(ctx, AdjustmentType.class, "ADJUSTMENTTYPE");

        systemHome = new AuditJournalHome(ctx, systemHome);

        // avoid to clone adapter home for read only interface.
        Home cacheHome = new GenericTransientHome(ctx, AdjustmentType.class);
        
        Home[] readOnlyOperationHomes = new Home[] {cacheHome, systemHome};
        for (int i=0; i<readOnlyOperationHomes.length; i++)
        {
            // Install a home to adapt between business logic bean and data bean
            readOnlyOperationHomes[i] = new AdapterHome(
                    ctx, 
                    readOnlyOperationHomes[i], 
                    new ExtendedBeanAdapter<com.redknee.app.crm.bean.AdjustmentType, com.redknee.app.crm.bean.core.AdjustmentType>(
                            com.redknee.app.crm.bean.AdjustmentType.class, 
                            com.redknee.app.crm.bean.core.AdjustmentType.class));
            readOnlyOperationHomes[i] = new SortingHome(ctx, readOnlyOperationHomes[i]);
            readOnlyOperationHomes[i] = new AdjustmentTypeDeprecateAwareHome(ctx, readOnlyOperationHomes[i]);
            readOnlyOperationHomes[i] = new SetPrincipalRmiServerHome(ctx, readOnlyOperationHomes[i]);
        }
        
        // Update references to adjustment type homes
        cacheHome = readOnlyOperationHomes[0];
        systemHome = readOnlyOperationHomes[1];
        
        // There is no need to clone on for each when performing reads for the purpose
        // of read-only access.  Therefore, do not clone on forEach in the read-only
        // portion of the pipeline.
        Home readOnlyHome = new AdapterHome(ctx, cacheHome, CloneAdapter.instance())
        {
            /** Unlike AdapterHome, we don't want to adapt on forEach. **/
            @Override
            public Visitor forEach(Context aCtx, Visitor visitor, Object where) throws HomeException
            {
                return getDelegate().forEach(aCtx, visitor, where);
            }

        };
        
        readOnlyHome = new ReadOnlyHome(ctx, readOnlyHome);
            
        // Store read-only home in the context
        ctx.put(ADJUSTMENT_TYPE_READ_ONLY_HOME, readOnlyHome);
        
        // Need to clone when inserting and removing data from the underlying cache
        // when not going through the read-only home.
        cacheHome = new AdapterHome(ctx, cacheHome, CloneAdapter.instance());
        
		systemHome = new ConfigShareTotalCachingHome(ctx, cacheHome, systemHome, false);
        
        // 'Validatable' validator
        systemHome = new ValidatingHome(systemHome);

        ctx.put(ADJUSTMENT_TYPE_SYSTEM_HOME, systemHome);

        Home adjustmentTypeHome = new ContextRedirectingHome(ctx, ADJUSTMENT_TYPE_SYSTEM_HOME);
        /*
         * [Cindy Wong] 2010-02-08: The following are skipped from system
         * home.
         */
        adjustmentTypeHome = new SystemAdjustmentTypeProtectionHome(ctx,
                adjustmentTypeHome);
        adjustmentTypeHome = new ValidatingHome(
                ManualAdjustmentTypeCategoryValidator.instance(),
                SystemAdjustmentTypeCategoryValidator.instance(),
                adjustmentTypeHome);

        return adjustmentTypeHome;
    }

    public static final String ADJUSTMENT_TYPE_READ_ONLY_HOME = "ADJUSTMENT_TYPE_READ_ONLY_HOME";

    /**
     * Context key for adjustment type home that can be used by system to create
     * adjustment types.
     */
    public static final String ADJUSTMENT_TYPE_SYSTEM_HOME = "ADJUSTMENT_TYPE_SYSTEM_HOME";
}
