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
package com.redknee.app.crm.factory;

import com.redknee.framework.xhome.beans.XCloneable;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextFactory;
import com.redknee.framework.xlog.log.MajorLogMsg;


/**
 * Returns a clone of an XCloneable object which it uses as a Prototype.  The
 * prototype is pulled from the context using the key upon which this factory
 * was initialized.
 *
 * @author gary.anderson@redknee.com
 */
public class PrototypeContextFactory
    implements ContextFactory
{
    /**
     * Creates a new factory for cloning the prototype referred to with the
     * given key.
     *
     * @param key The key used to locate the prototype in the context.
     */
    public PrototypeContextFactory(final Object key)
    {
        key_ = key;
    }

    /**
     * {@inheritDoc}
     */
    public Object create(final Context context)
    {
        try
        {
            final XCloneable proto = (XCloneable)context.get(key_);

            if (proto == null)
            {
                new MajorLogMsg(
                    this,
                    "Failed to locate prototype for " + key_,
                    null).log(context);

                return null;
            }

            return proto.clone();
        }
        catch (final CloneNotSupportedException exception)
        {
            new MajorLogMsg(
                this,
                "Failed to clone prototype for " + key_,
                exception).log(context);

            return null;
        }
    }


    /**
     * The key used to locate the prototype in the context.
     */
    private final Object key_;

} // class
