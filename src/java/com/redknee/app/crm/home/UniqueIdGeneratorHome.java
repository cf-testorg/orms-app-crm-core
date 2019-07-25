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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.xdb.sequence.Sequence;

/**
 * Unique Id Generator Home
 *
 * @author mangaraj.sahoo@redknee.com
 */
public class UniqueIdGeneratorHome extends HomeProxy
{
    private static final long serialVersionUID = 1L;
    
    private Sequence idSequence_;
    

/**
 * @deprecated
 * @param ctx
 * @param delegate
 * @param tableName
 * @param uniqueIdColumnName
 * @param sequenceName
 */
    public UniqueIdGeneratorHome(final Context ctx, final Home delegate, 
            final String tableName, final String uniqueIdColumnName, 
            final String sequenceName)
    {
        super(ctx, delegate);
        idSequence_ = new CachedSequence(ctx, sequenceName);
    }

    public UniqueIdGeneratorHome(final Context ctx, final Home delegate, 
            final String sequenceName)
    {
        super(ctx, delegate);
        idSequence_ = new CachedSequence(ctx, sequenceName);
    }


    /**
     * {@inheritDoc}
     */
    public void removeAll(final Context ctx, final Object where)
    throws HomeException, HomeInternalException, UnsupportedOperationException
    {
        super.removeAll(ctx, where);

        if (where == True.instance())
        {
            idSequence_.resetSequence(ctx);
        }
    }
    
    public Sequence getSequence()
    {
        return idSequence_;
    }
}
