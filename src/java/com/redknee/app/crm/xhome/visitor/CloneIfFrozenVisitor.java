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
package com.redknee.app.crm.xhome.visitor;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.CloneingVisitor;
import com.redknee.framework.xhome.visitor.Visitor;


/**
 * CloneingVisitor that only clones the bean if it is frozen.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.5
 */
public class CloneIfFrozenVisitor extends CloneingVisitor
{
    public CloneIfFrozenVisitor(Visitor delegate)
    {
        super(delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(Context ctx, Object bean) throws AgentException, AbortVisitException
    {
        if (!(bean instanceof AbstractBean)
                || ((AbstractBean)bean).isFrozen())
        {
            super.visit(ctx, bean);
        }
        else
        {
            getDelegate().visit(ctx, bean);
        }
    }

}
