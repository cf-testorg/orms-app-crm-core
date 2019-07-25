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

package com.redknee.app.crm.home;

import com.redknee.app.crm.bean.AccountCategory;
import com.redknee.app.crm.bean.IdentifierEnum;
import com.redknee.app.crm.support.IdentifierSequenceSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.LogSupport;

/**
 * 
 * Sets ID and identifier for this Account Category using an
 * IdentifierSequence.
 * 
 * @author bdhavalshankh
 *
 */
public class AccountCategoryIDSettingHome extends HomeProxy 
{
    private static final long startValue = 100000;
    
    public AccountCategoryIDSettingHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }

    @Override
    public Object create(Context ctx, Object obj) throws HomeException,
            HomeInternalException 
    {
        
        AccountCategory accountCat = (AccountCategory)obj;
        
        IdentifierSequenceSupportHelper.get(ctx).ensureSequenceExists(ctx, IdentifierEnum.ACCOUNTTYPE_ID.toString(),
                startValue, Long.MAX_VALUE);
        
        long accountCategoryID = IdentifierSequenceSupportHelper.get(ctx).getNextIdentifier(
                ctx,
                IdentifierEnum.ACCOUNTTYPE_ID.toString(),
                null);
        
        if(accountCat.getIdentifier() == 0)
        {
            accountCat.setIdentifier(accountCategoryID);
        }
        
        LogSupport.info(ctx, this, "Account Type ID set to: " + accountCat.getIdentifier());
        
        return super.create(ctx, obj);
    }

    
}
