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

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.Or;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.webcontrol.AbstractKeyWebControl;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.KeyWebControlProxy;


/**
 * A Proxy-Key control that selects keys based on property match (equality join)
 * Used where an Outer Bean encapsulates Inner Bean's property (reference = model-property)
 * 
 * @author simar.singh@redknee.com
 * 
 */
public class PropertyMatchKeyWebControl extends KeyWebControlProxy
{

    /**
     * Match SPID for selection for Keys Disable filtering when parent key is invalid.
     * Chose if we want the existing value to be kept regardless of the filter.
     */
    protected final boolean keepExistingVlaue_;
    protected final PropertyInfo outerBeanProperty_;
    protected final PropertyInfo innerBeanProperty_;


    /**
     * @param delegate - A Key-Web-Control chain; terminates with a Concrete Bean/Home key web-control
     * @param innerBeanProperty - Inner/Encapsulated Bean's property referenced by Outer Bean's property
     * @param outerBeanProperty - Outer/Encapsulating Bean's property that holds reference to Inner Bean's property
     */
    public PropertyMatchKeyWebControl(AbstractKeyWebControl delegate, PropertyInfo innerBeanProperty, PropertyInfo outerBeanProperty)
    {
        this(delegate, innerBeanProperty,outerBeanProperty, false);
    }

    /**
     * @param delegate - A Key-Web-Control chain; terminates with a Concrete Bean/Home key web-control
     * @param innerBeanProperty - Inner/Encapsulated Bean's property referenced by Outer Bean's property
     * @param outerBeanProperty - Outer/Encapsulating Bean's property that holds reference to Inner Bean's property
     * @param keepExistingValue - Consider's existing value as a valid selection regardless of the reference relation; particularly useful in default-value and migration. 
     */
    public PropertyMatchKeyWebControl(AbstractKeyWebControl delegate, PropertyInfo innerBeanProperty,
            PropertyInfo outerBeanProperty, boolean keepExistingValue)
    {
        super(delegate);
        keepExistingVlaue_ = keepExistingValue;
        outerBeanProperty_ = outerBeanProperty;
        innerBeanProperty_ = innerBeanProperty;
    }

    
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        Home home = getHome(ctx);
        if (home == null)
        {
            out.print("<font color=\"red\">Sytem Error: No Home supplied in Context under key '" + getHomeKey()
                    + "'.</font>");
            return;
        }
        AbstractBean parent = (AbstractBean) ctx.get(AbstractWebControl.BEAN);
        Predicate condition = new EQ(innerBeanProperty_, outerBeanProperty_.get(parent));
        if(keepExistingVlaue_)
        {
            condition = new Or().add(condition).add(new EQ(innerBeanProperty_.getXInfo().getID(),obj));
        }
        Home whereHome = home.where(ctx,condition);
        super.toWeb(ctx.createSubContext().put(getHomeKey(), whereHome), out, name, obj);
    }
}