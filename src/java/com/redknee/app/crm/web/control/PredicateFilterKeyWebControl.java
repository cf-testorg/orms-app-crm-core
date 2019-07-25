/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.web.control;

import java.io.PrintWriter;

import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.Or;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.webcontrol.AbstractKeyWebControl;
import com.redknee.framework.xhome.webcontrol.KeyWebControlProxy;


/**
 * A Proxy-Key control that selects keys based on a Predicate
 * Optionally, one can keeo the existing value of the key reagardless of the filter.
 * 
 * @author simar.singh@redknee.com
 * 
 */
public class PredicateFilterKeyWebControl extends KeyWebControlProxy
{

    /**
     * @param delegate - A Key-Web-Control chain; terminates with a Concrete Bean/Home key web-control
     * @param predicate - Predicate that will filter the selection of keys 
     */
    public PredicateFilterKeyWebControl(AbstractKeyWebControl delegate, Predicate predicate)
    {
        this(delegate,predicate,false);
    }
    
    /**
     * 
     * @param delegate - A Key-Web-Control chain; terminates with a Concrete Bean/Home key web-control
     * @param predicate - Predicate that will filter the selection of keys 
     * @param keepExisting - Consider's existing value as a valid selection regardless of the reference relation; particularly useful in default-value and migration.
     */
    public PredicateFilterKeyWebControl(AbstractKeyWebControl delegate, Predicate predicate, boolean keepExisting)
    {
        super(delegate);
        predicate_ = predicate;
        keepExisting_ = keepExisting;
    }

    private final boolean keepExisting_;

    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        Home home = getHome(ctx);
        if (home == null)
        {
            out.print("<font color=\"red\">Sytem Error: No Home supplied in Context under key '" + getHomeKey()
                    + "'.</font>");
            return;
        }
        Predicate condition = predicate_;
        if (keepExisting_)
        {
            XInfo beanXinfo = (XInfo) XBeans.getInstanceOf(ctx, getIdentitySupport().toBean(obj), XInfo.class);
            condition = new Or().add(condition).add(new EQ(beanXinfo.getID(), obj));
        }
        Home whereHome = home.where(ctx, condition);
        super.toWeb(ctx.createSubContext().put(getHomeKey(), whereHome), out, name, obj);
    }

    private final Predicate predicate_;
}