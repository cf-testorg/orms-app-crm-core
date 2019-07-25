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

import java.util.Date;

import com.redknee.app.crm.bean.account.AccountConversionHistory;
import com.redknee.app.crm.move.MoveConstants;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;


/**
 *  @author bdhavalshankh
 *  @since 9.5.1
 */
public class AccountConversionHistFieldSettingHome extends HomeProxy
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AccountConversionHistFieldSettingHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx, Object obj) throws HomeException
    {
        AccountConversionHistory history = (AccountConversionHistory) obj;
        if(history.getNewGroupBan().contains(MoveConstants.DEFAULT_MOVE_PREFIX))
        {
            history.setNewGroupBan(null);
        }
       history.setCompletedOn(new Date()); 
       return super.create(ctx, history);
        
    }

    @Override
    public Object store(Context ctx, Object obj) throws HomeException
    {
        AccountConversionHistory history = (AccountConversionHistory) obj;
        if(history.getNewGroupBan().contains(MoveConstants.DEFAULT_MOVE_PREFIX))
        {
            history.setNewGroupBan(null);
        }
       history.setCompletedOn(new Date());
       return super.store(ctx, history);
    }
 
}