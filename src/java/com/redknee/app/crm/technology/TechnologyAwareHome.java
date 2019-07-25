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
package com.redknee.app.crm.technology;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.Or;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.home.AbstractClassAwareHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.WhereHome;

import com.redknee.util.snippet.log.Logger;
/**
 * Modeled closely on SpidWareHome in framework.
 *
 * @author r.attapattu@redknee.com
 */
public class TechnologyAwareHome extends WhereHome
{
    /**
     * Prevent serialization issues.
     */
    private static final long serialVersionUID = 1L;
    private Class beanClass_ = null;
    private Object beanClassMonitor_ = new Object();
    
    public TechnologyAwareHome(final Context ctx, final Home delegate)
    {
        super(ctx, delegate);
    }
    private Class getBeanClass(final Context ctx)
    {
        synchronized (beanClassMonitor_)
        {
            if (beanClass_ == null)
            {
                try
                {
                    beanClass_ = (Class) super.cmd(ctx, AbstractClassAwareHome.CLASS_CMD);
                }
                catch (HomeException e)
                {
                    Logger.crit(ctx, this, "COULD NOT DETERMINE HOME BEAN CLASS IN "
                            + this.getClass().getName(), e);
                }
            }
        }

        return beanClass_;
    }

    public Object getWhere(final Context ctx)
    {
        final TechnologyEnum tech = Technology.getBeanTechnology(ctx);

        if (tech != null && tech != TechnologyEnum.ANY && tech != TechnologyEnum.NO_TECH)
        {
            final Or condition = new Or();
            
            final Class beanClass = getBeanClass(ctx);
            if (TechnologyAware.class.isAssignableFrom(beanClass))
            {
                condition.add(new EQ(TechnologyAwareXInfo.TECHNOLOGY, tech));
                condition.add(new EQ(TechnologyAwareXInfo.TECHNOLOGY, TechnologyEnum.ANY));
            }
            else if (TechnologyIndexAware.class.isAssignableFrom(beanClass))
            {
                condition.add(new EQ(TechnologyIndexAwareXInfo.TECHNOLOGY, Integer.valueOf(tech.getIndex())));
                condition.add(new EQ(TechnologyIndexAwareXInfo.TECHNOLOGY, Integer.valueOf(TechnologyEnum.ANY.getIndex())));
            }
            return condition;
        }
        else
        {
            return True.instance();
        }
    }
}
