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

import com.redknee.app.crm.bean.AdjustmentType;
import com.redknee.app.crm.bean.AdjustmentTypeStateEnum;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.WhereHome;

/**
 * @author amedina
 *
 * Filters the home to return only non-deprecated Adjustment Types
 */
public class AdjustmentTypeDeprecateAwareHome extends WhereHome
{
    /**
     * Only constructor
     * @param ctx
     * @param delegate
     */
    public AdjustmentTypeDeprecateAwareHome(Context ctx, Home delegate)
    {
        super(ctx,delegate);
    }



    public Object getWhere(Context ctx)
    {
        return new Predicate()
        {
            public boolean f(Context ctx, Object obj)
            {
                AdjustmentType type = (AdjustmentType) obj;
                if(type==null || type.getState()==null)
                {
                    return false;
                }

                return type.getState().getIndex() == AdjustmentTypeStateEnum.ACTIVE_INDEX;
            }

          };
       }

}
