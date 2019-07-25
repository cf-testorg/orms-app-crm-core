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

package com.redknee.app.crm.web.control;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.ReadOnlyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

/**
 * Makes the field read only if the supplied predicate is true.
 *
 * @author larry.xia@redknee.com
 */
public class ReadOnlyOnPredicateWebControl extends ReadOnlyWebControl
{
    /**
     * Reference to predicate that determines if output will be read-only.
     */
    private final Predicate predicate_;

    /**
     * Accepts the delegate and the predicate needed for operation.
     *
     * @param delegate the non read-only web control
     * @param predicate if this predicate evaluates to true the output will be read-only
     */
    public ReadOnlyOnPredicateWebControl(final WebControl delegate, final Predicate predicate)
    {
        super(delegate);
        this.predicate_ = predicate;
    }

    /**
     * Delegate to the ReadOnlyWebControl super class if the predicate is true or to the delegate otherwise.
     *
     * {@inheritDoc}
     */
    public void toWeb(final Context ctx, final PrintWriter out, final String name, final Object obj)
    {
        final Object bean = ctx.get(AbstractWebControl.BEAN);

        if (this.predicate_.f(ctx, bean))
        {
            // Read-Only
            super.toWeb(ctx, out, name, obj);
        }
        else
        {
            // Read-Write
            delegate_.toWeb(ctx, out, name, obj);
        }
    }

    /**
     * Always delegate to the web control that can retreive the value.
     *
     * @param ctx the operating context
     * @param req initial request
     * @param name name of the field
     * @return field value object
     */
    public synchronized Object fromWeb(final Context ctx, final ServletRequest req, final String name)
    {
        return delegate_.fromWeb(ctx, req, name);
    }
}
