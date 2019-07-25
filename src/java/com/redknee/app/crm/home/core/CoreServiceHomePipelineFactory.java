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

import java.io.IOException;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.ContextualizingHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.home.ValidatingHome;
import com.redknee.framework.xhome.msp.SpidAwareHome;

import com.redknee.app.crm.bean.Service;
import com.redknee.app.crm.extension.validator.SingleInstanceExtensionsValidator;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.technology.TechnologyAwareHome;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;

/**
 * Creates the service home decorators and put is in the context.
 * @author arturo.medina@redknee.com
 *
 */
public class CoreServiceHomePipelineFactory implements PipelineFactory
{
    /**
     * {@inheritDoc}
     */
    public Home createPipeline(final Context ctx, final Context serverCtx)
        throws HomeException, IOException, AgentException
    {
        Home serviceHome = StorageSupportHelper.get(ctx).createHome(ctx, Service.class, "SERVICE");
        
        serviceHome = new AuditJournalHome(ctx, serviceHome);
        
        // Install a home to adapt between business logic bean and data bean
        serviceHome = new AdapterHome(
                ctx, 
                serviceHome, 
                new ExtendedBeanAdapter<com.redknee.app.crm.bean.Service, com.redknee.app.crm.bean.core.Service>(
                        com.redknee.app.crm.bean.Service.class, 
                        com.redknee.app.crm.bean.core.Service.class));

        // Contextualize on the local side of the clustering home since the context field is transient
        serviceHome = new ContextualizingHome(ctx, serviceHome);
        
        serviceHome = new SortingHome(serviceHome);

        // Contextualize on the local side of the clustering home since the context field is transient
        serviceHome = new ContextualizingHome(ctx, serviceHome);
        
        serviceHome = new TechnologyAwareHome(ctx, serviceHome);
        
        serviceHome = new ValidatingHome(serviceHome, new SingleInstanceExtensionsValidator());
        
        serviceHome = new SpidAwareHome(ctx, serviceHome);
        
        return serviceHome;
    }
}
