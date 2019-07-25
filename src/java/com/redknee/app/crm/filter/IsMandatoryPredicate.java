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
package com.redknee.app.crm.filter;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;

/**
 * A Predicate that returns true the object is a mandatory fee.
 *
 * @author victor.stratan@redknee.com
 */
public final class IsMandatoryPredicate implements Predicate
{
    /**
     *
     * {@inheritDoc}
     */
    public boolean f(final Context ctx, final Object object)
    {
        if (object instanceof MandatoryFeeAware)
        {
            final MandatoryFeeAware fee = (MandatoryFeeAware) object;
            return fee.isMandatory();
        }
        
        return false;
    }
}