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
package com.redknee.app.crm.move.dependency;

import java.util.Collection;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xlog.log.InfoLogMsg;

import com.redknee.app.crm.move.MoveDependencyManager;
import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveRequest;


/**
 * Writes INFO level log messages showing the result of each method.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class LoggingMoveDependencyManager extends MoveDependencyManagerProxy implements ContextAware
{

    public LoggingMoveDependencyManager(Context ctx)
    {
        this(ctx, (Object)null);
    }
    
    public LoggingMoveDependencyManager(Context ctx, MoveDependencyManager delegate)
    {
        this(ctx, null, delegate);
    }

    public LoggingMoveDependencyManager(Context ctx, Object logSource)
    {
        this(ctx, logSource, NullMoveDependencyManager.instance());
    }
    
    public LoggingMoveDependencyManager(Context ctx, Object logSource, MoveDependencyManager delegate)
    {
        super(delegate);
        setContext(ctx);
        if (logSource != null)
        {
            logSource_ = logSource;
        }
        else
        {
            logSource_ = getDelegate();
        }
    }

    @Override
    public MoveRequest getSourceRequest()
    {
        MoveRequest sourceRequest = super.getSourceRequest();
        new InfoLogMsg(logSource_, "Source request: " + sourceRequest, null).log(getContext());
        return sourceRequest;
    }

    @Override
    public Collection<? extends MoveRequest> getDependencyRequests() throws MoveException
    {
        Collection<? extends MoveRequest> dependencyRequests = super.getDependencyRequests();
        new InfoLogMsg(logSource_, "Dependencies for request [" + super.getSourceRequest() + "] are: " + dependencyRequests, null).log(getContext());
        return dependencyRequests;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Context getContext()
    {
        return ctx_;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void setContext(Context context)
    {
        ctx_ = context;
    }
    
    private Context ctx_ = null;

    private final Object logSource_;
}
