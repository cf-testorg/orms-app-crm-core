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
package com.redknee.app.crm.configshare;

import java.util.ArrayList;
import java.util.Collection;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.context.Context;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class NullConfigChangeRequestTranslator implements ConfigChangeRequestTranslator
{
    private static ConfigChangeRequestTranslator instance_ = null;
    public static ConfigChangeRequestTranslator instance()
    {
        if (instance_ == null)
        {
            instance_ = new NullConfigChangeRequestTranslator();
        }
        return instance_;
    }

    protected NullConfigChangeRequestTranslator()
    {
    }

    /**
     * {@inheritDoc}
     */
    public Collection<? extends AbstractBean> translate(Context ctx, ConfigChangeRequest request) throws ConfigChangeRequestTranslationException
    {
        return new ArrayList<AbstractBean>();
    }

}
