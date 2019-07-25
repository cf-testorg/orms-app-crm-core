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
package com.redknee.app.crm.configshare;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.MinorLogMsg;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class ConfigChangeWriteExceptionCleanupHome extends HomeProxy
{
    public ConfigChangeWriteExceptionCleanupHome(Context ctx, Home createHome)
    {
        super(ctx, createHome);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx, Object obj) throws HomeException
    {
        try
        {
            return super.create(ctx, obj);
        }
        catch (Throwable t)
        {
            throw cleanupException(ctx, obj, t);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Context ctx, Object obj) throws HomeException
    {
        try
        {
            super.remove(ctx, obj);
        }
        catch (Throwable t)
        {
            throw cleanupException(ctx, obj, t);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object store(Context ctx, Object obj) throws HomeException
    {
        try
        {
            return super.store(ctx, obj);
        }
        catch (Throwable t)
        {
            throw cleanupException(ctx, obj, t);
        }
    }


    private HomeException cleanupException(Context ctx, Object obj, Throwable t)
    {
        if (t instanceof HomeException)
        {
            return (HomeException) t;
        }
        
        // We must make sure that all exceptions that leave this home are of type HomeException
        // Otherwise, RMIHomeClient will convert them to HomeException, which may result in retrying
        // these operations that will most likely fail on retry.
        ConfigChangeRequest request = null;
        if (obj instanceof ConfigChangeRequest)
        {
            request = (ConfigChangeRequest) obj;
        }
        final String msg;
        if (request != null)
        {
            msg = "Unexpected exception occurred while processing from " + request.getModifiedAppName()
            + "'s source bean class " + request.getBeanClass();
        }
        else
        {
            msg = "Unexpected exception occurred while processing an non-ConfigChangeRequest object!";
        }
        new MinorLogMsg(this, msg, t).log(ctx);
        return new HomeException(msg, t);
    }

}
