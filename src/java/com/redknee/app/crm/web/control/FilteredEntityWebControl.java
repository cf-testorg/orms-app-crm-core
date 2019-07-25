/*

 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily 
 * available. Additionally, source code is, by its very nature, confidential 
 * information and inextricably contains trade secrets and other information 
 * proprietary, valuable and sensitive to Redknee, no unauthorised use, 
 * disclosure, manipulation or otherwise is permitted, and may only be used 
 * in accordance with the terms of the licence agreement entered into with 
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.web.control;

import java.io.PrintWriter;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.False;
import com.redknee.framework.xhome.elang.Or;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.entity.ByClassPredicate;
import com.redknee.framework.xhome.entity.EntityInfoHome;
import com.redknee.framework.xhome.entity.EntityWebControl;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

/**
 * Applies a filter to the EntityInfoHome before rendering the EntityWebControl.  This allows us to do things like license entities.
 *
 * @author Aaron Gourley
 * @since 7.4.16
 */
public class FilteredEntityWebControl extends ProxyWebControl
{
    protected final Predicate filter_;
    protected final Predicate allowToChoosePredicate_;
    protected final EntityOneRowSelectionWebControl localDelegate_;

    public FilteredEntityWebControl(Predicate predicate)
    {
        localDelegate_ = new EntityOneRowSelectionWebControl(predicate);
        setDelegate(localDelegate_);
        filter_ = predicate;
        allowToChoosePredicate_ = True.instance();
    }

    public FilteredEntityWebControl(Predicate predicate, Class cls, boolean sameLine)
    {
        localDelegate_ = new EntityOneRowSelectionWebControl(cls, sameLine);
        setDelegate(localDelegate_);
        filter_ = predicate;
        allowToChoosePredicate_ = True.instance();
    }

    public FilteredEntityWebControl(Predicate predicate, Class cls, boolean sameLine, boolean allowToChoose)
    {
        localDelegate_ = (EntityOneRowSelectionWebControl) new EntityOneRowSelectionWebControl(cls, sameLine).setAllowToChoose(allowToChoose);
        setDelegate(localDelegate_);
        filter_ = predicate;
        allowToChoosePredicate_ = allowToChoose?True.instance():False.instance();
    }

    public FilteredEntityWebControl(Predicate predicate, Class cls, boolean sameLine, Predicate allowToChoose)
    {
        localDelegate_ = new EntityOneRowSelectionWebControl(cls, sameLine);
        setDelegate(localDelegate_);
        filter_ = predicate;
        allowToChoosePredicate_ = allowToChoose;
    }

    public FilteredEntityWebControl(Predicate predicate, Class cls, String role)
    {
        localDelegate_ = new EntityOneRowSelectionWebControl(cls, role);
        setDelegate(localDelegate_);
        filter_ = predicate;
        allowToChoosePredicate_ = True.instance();
    }

    public FilteredEntityWebControl(Predicate predicate, Class cls)
    {
        localDelegate_ = new EntityOneRowSelectionWebControl(cls);
        setDelegate(localDelegate_);
        filter_ = predicate;
        allowToChoosePredicate_ = True.instance();
    }

    public FilteredEntityWebControl(Predicate predicate, Class[] clss, boolean sameLine)
    {
        localDelegate_ = new EntityOneRowSelectionWebControl(clss, sameLine);
        setDelegate(localDelegate_);
        filter_ = predicate;
        allowToChoosePredicate_ = True.instance();
    }

    public FilteredEntityWebControl(Predicate predicate, Class[] clss)
    {
        localDelegate_ = new EntityOneRowSelectionWebControl(clss);
        setDelegate(localDelegate_);
        filter_ = predicate;
        allowToChoosePredicate_ = True.instance();
    }

    @Override
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        final Context subCtx = ctx.createSubContext();

        final Home entityHome = (Home) subCtx.get(EntityInfoHome.class);
        Predicate condition = filter_;
        if (obj != null)
        {
            Or filter = new Or().add(filter_);

            // Add this object's class to the filter so that the existing value in the bean is shown.
            // In this case, the predicate was true when the bean was created but now may be false.
            // e.g. a license was disabled
            filter.add(new ByClassPredicate(obj.getClass()));

            condition = filter;
        }
        subCtx.put(EntityInfoHome.class, entityHome.where(subCtx, condition));
        
        localDelegate_.setAllowToChoose(allowToChoosePredicate_.f(ctx, obj));

        super.toWeb(subCtx, out, name, obj);
    }
    
    public Class [] getEntityClass()
    {
      return ((EntityWebControl)getDelegate()).getEntityClass();
    }

    public String getRole()
    {
      return ((EntityWebControl)getDelegate()).getRole();
    }

    public boolean getAllowToChoose()
    {
      return ((EntityWebControl)getDelegate()).getAllowToChoose();
    }

    public FilteredEntityWebControl setAllowToChoose(boolean allowToChoose)
    {
        ((EntityWebControl)getDelegate()).setAllowToChoose(allowToChoose);
        return this;
    }

    public Object getWhere()
    {
        return ((EntityWebControl)getDelegate()).getWhere();
    }

    public Predicate getPredicate()
    {
        return ((EntityWebControl)getDelegate()).getPredicate();
    }

    public WebControl webControl(Context ctx, Object obj)
    {
        return ((EntityWebControl)getDelegate()).webControl(ctx, obj);
    }

    /**
     * Returns the real WebControl, ignoring the ReadOnly, Final and XTestIgnore web controls in front
     */
    public WebControl getWebControl(Context ctx, Object obj)
    {
        return ((EntityWebControl)getDelegate()).getWebControl(ctx, obj);
    }

    public FilteredEntityWebControl setSameLine(boolean sameLine)
    {
        ((EntityWebControl)getDelegate()).setSameLine(sameLine);
        return this;
    }

    public boolean isSameLine()
    {
        return ((EntityWebControl)getDelegate()).isSameLine();
    }

    public boolean getSameLine()
    {
        return ((EntityWebControl)getDelegate()).getSameLine();
    }

    public FilteredEntityWebControl setNumOfBlanks(int numOfBlanks)
    {
        ((EntityWebControl)getDelegate()).setNumOfBlanks(numOfBlanks);
        return this;
    }

    public int getNumOfBlanks()
    {
       return ((EntityWebControl)getDelegate()).getNumOfBlanks();
    }
}
