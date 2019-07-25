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
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.MinorLogMsg;


/**
 * On create, if it fails then attempt a store instead. 
 * 
 * WARNING: this home should be installed as close to XDBHome as possible
 *
 * @author bdhavalshankh
 * @since 9.5.0
 */
public class CreateOrStoreCT2RuleHome extends HomeProxy
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CreateOrStoreCT2RuleHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx, Object obj) throws HomeException
    {
        
        Exception createExp = null;
        Object tempObj = null;
        try
        {
            tempObj = super.create(ctx, obj);
        }
        catch (Exception e)
        {
            createExp = e;
            boolean storeSuccess = false;
            
            try
            {
                tempObj = super.store(ctx, obj);
                storeSuccess = true;
            }
            finally
            {
                if (!storeSuccess)
                {
                    new MinorLogMsg(this, "Original creation exception: " + createExp, createExp).log(ctx);
                }
            }
        }
        return tempObj;
    }

    
}
