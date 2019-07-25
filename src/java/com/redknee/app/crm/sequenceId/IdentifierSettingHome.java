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
package com.redknee.app.crm.sequenceId;

import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;

import com.redknee.app.crm.bean.IdentifierEnum;
import com.redknee.app.crm.support.IdentifierSequenceSupportHelper;


/**
 * Sets the unique identifiers used by beans that are SequenceIdentified.
 *
 * @author gary.anderson@redknee.com
 */
public class IdentifierSettingHome extends HomeProxy
{
    /**
     * Creates a new IdentiferSettingHome proxy.
     *
     * @param context The operating context.
     * @param delegate The Home to which we delegate.
     * @param sequenceIdentifier The identifier of the sequence to use when
     * generating unique identifiers.
     * @param rollOver The object notified when a roll-over occurs within the
     * sequence.
     */
    public IdentifierSettingHome(
        final Context context,
        final Home delegate,
        final IdentifierEnum sequenceIdentifier,
        final RollOverNotofiable rollOver)
    {
        super(delegate);
        setContext(context);
        sequenceIdentifier_ = sequenceIdentifier;
        rollOver_ = rollOver;
    }

    /**
     * {@inheritDoc}
     *
     * This Home automatically assigns a new, unique, identifier to the given
     * object.
     */
    @Override
    public Object create(final Context ctx, final Object bean)
        throws HomeException
    {
        final SequenceIdentified identified = (SequenceIdentified)bean;

        Object defaultIdValue = null;
        XInfo xinfo = (XInfo) XBeans.getInstanceOf(ctx, bean, XInfo.class);
        if (xinfo != null)
        {
            PropertyInfo idProperty = xinfo.getID();
            if (idProperty != null && idProperty.hasDefault())
            {
                defaultIdValue = idProperty.getDefault();
            }
        }
        
        if (!(defaultIdValue instanceof Number)
                || SafetyUtil.safeEquals(identified.getIdentifier(), ((Number)defaultIdValue).longValue()))
        {
            // Throws HomeException.
            final long identifier = getNextIdentifier(ctx);

            identified.setIdentifier(identifier);
        }

        return super.create(ctx, identified);
    }


    /**
     * Gets the next available identifier.
     *
     * @return The next available identifier.
     *
     * @exception HomeException Thrown if there is a problsm accessing the
     * sequence identifier information in the operating context.
     */
    private long getNextIdentifier(final Context ctx)
        throws HomeException
    {
        IdentifierSequenceSupportHelper.get(ctx).ensureSequenceExists(
            ctx,
            sequenceIdentifier_,
            1,
            Long.MAX_VALUE);

        return IdentifierSequenceSupportHelper.get(ctx).getNextIdentifier(
            ctx,
            sequenceIdentifier_,
            rollOver_);
    }

    /**
     * The identifier of the sequence to use when generating unique identifiers.
     */
    private final IdentifierEnum sequenceIdentifier_;

    /**
     * The object notified when a roll-over occurs within the sequence.
     */
    private final RollOverNotofiable rollOver_;

} // class
