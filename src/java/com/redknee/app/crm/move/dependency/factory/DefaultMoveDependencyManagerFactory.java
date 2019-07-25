/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily 
 * available. Additionally, source code is, by its very nature, confidential 
 * information and inextricably contains trade secrets and other information 
 * proprietary, valuable and sensitive to Redknee, no unauthorised use, 
 * disclosure, manipulation or otherwise is permitted, and may only be used 
 * in accordance with the terms of the licence agreement entered into with 
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.move.dependency.factory;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.PMLogMsg;

import com.redknee.app.crm.move.MoveDependencyManager;
import com.redknee.app.crm.move.MoveRequest;
import com.redknee.app.crm.move.dependency.CompoundMoveDependencyManager;
import com.redknee.app.crm.move.dependency.EmptyMoveDependencyManager;
import com.redknee.app.crm.move.dependency.LoggingMoveDependencyManager;
import com.redknee.app.crm.move.dependency.NullMoveDependencyManager;
import com.redknee.app.crm.move.dependency.PMMoveDependencyManager;
import com.redknee.app.crm.move.request.CompoundMoveRequest;


/**
 * This is the core version of the dependency manager factory.  It will likely need to be over-ridden
 * by the application using the feature.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class DefaultMoveDependencyManagerFactory implements MoveDependencyManagerFactory
{    
    private static MoveDependencyManagerFactory instance_ = null;
    public static MoveDependencyManagerFactory instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultMoveDependencyManagerFactory();
        }
        return instance_;
    }
    
    protected DefaultMoveDependencyManagerFactory()
    {
    }

    /**
     * {@inheritDoc}
     */
    public final MoveDependencyManager getInstance(Context ctx, MoveRequest request)
    {
        if (request == null)
        {
            return NullMoveDependencyManager.instance();
        }

        MoveDependencyManager dependencyManager = getRequestSpecificInstance(ctx, request);

        if (dependencyManager != null)
        {
            dependencyManager = new LoggingMoveDependencyManager(ctx, dependencyManager, new PMMoveDependencyManager(ctx, dependencyManager));
        }
        
        return dependencyManager;
    }

    /**
     * This method is designed to be overridden by the application that is aware of MoveRequest types
     * specific to its feature set.
     * 
     * The extending class should not wrap its dependency manager pipeline with anything that applies
     * to all pipelines.  The default getInstance() method does this.
     */
    protected MoveDependencyManager getRequestSpecificInstance(Context ctx, MoveRequest request)
    {
        MoveDependencyManager dependencyManager = new EmptyMoveDependencyManager(request);

        if (request instanceof CompoundMoveRequest)
        {
            PMLogMsg pm = new PMLogMsg(DefaultMoveDependencyManagerFactory.class.getName(), "CompoundMoveDependencyManagerCreation");
            dependencyManager = new CompoundMoveDependencyManager(ctx, (CompoundMoveRequest)request);
            pm.log(ctx);
        }
        return dependencyManager;
    }
}
