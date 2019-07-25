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

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.FacetMgr;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.3
 */
public class FacetRedirectingWebControl extends ProxyWebControl implements ContextAware
{
    public FacetRedirectingWebControl(Context ctx, Class<? extends AbstractBean> beanClass)
    {
        setContext(ctx);
        beanClass_ = beanClass;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getDelegate(Context ctx)
    {
        FacetMgr fMgr = (FacetMgr) ctx.get(FacetMgr.class);
        
        return (WebControl) fMgr.getInstanceOf(ctx, beanClass_, getWebControlType());
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getDelegate()
    {
        return getDelegate(getContext());
    }
    
    protected Class<? extends WebControl> getWebControlType()
    {
        return WebControl.class;
    }


    /**
     * {@inheritDoc}
     */
    public Context getContext()
    {
        return ctx_;
    }


    /**
     * {@inheritDoc}
     */
    public void setContext(Context ctx)
    {
        ctx_ = ctx;
    }


    protected Context ctx_;
    protected Class<? extends AbstractBean> beanClass_;
}
