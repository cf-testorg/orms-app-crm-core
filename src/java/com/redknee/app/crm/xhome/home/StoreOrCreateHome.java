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
package com.redknee.app.crm.xhome.home;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.BeanNotFoundHomeException;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.InfoLogMsg;


/**
 * On Create, attempt a Store first. If bean is not found, then continue with Create.
 * Since we do Store more often than Create, this should reduce the XDB exceptions thrown. 
 *
 *  WARNING: this home should be installed as close to XDBHome as possible
 *
 *  @author asim.mahmood@redknee.com
 */
public class StoreOrCreateHome extends HomeProxy
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public StoreOrCreateHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx, Object obj) throws HomeException
    {
        return storeOrCreate(ctx, obj);
    }

    @Override
    public Object store(Context ctx, Object obj) throws HomeException
    {
        return storeOrCreate(ctx, obj);
    }
 
    protected Object storeOrCreate(Context ctx, Object obj) throws HomeException
    {
        try
        {
            obj = super.store(ctx, obj);
        }
        catch(BeanNotFoundHomeException e)
        {
            new InfoLogMsg(this, "Ignore this UPDATE XDB log message, if present.", e).log(ctx);
            obj = super.create(ctx, obj);
        }
        return obj;
    }

}