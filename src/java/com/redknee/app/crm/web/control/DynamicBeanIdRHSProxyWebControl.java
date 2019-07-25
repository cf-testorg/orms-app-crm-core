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

import com.redknee.framework.xhome.beans.AbstractID;
import com.redknee.framework.xhome.beans.FacetMgr;
import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.entity.EntityInfo;
import com.redknee.framework.xhome.entity.EntityInfoHome;
import com.redknee.framework.xhome.entity.EntityInfoTransientHome;
import com.redknee.framework.xhome.entity.RHSFacetMgr;
import com.redknee.framework.xhome.entity.RHSProxyWebControl;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xlog.log.LogSupport;


/**
 * 
 * Copied back from framework, this is required to support Composite keys
 * for Config Share.
 *
 * @author aaron.gourley@redknee.com
 * @author <a href='mailto:ameya.bhurke@redknee.com'>Ameya Bhurke</a>
 * @since 
 */
public class DynamicBeanIdRHSProxyWebControl extends RHSProxyWebControl
{

    private PropertyInfo prop_;

    public DynamicBeanIdRHSProxyWebControl(PropertyInfo beanClassProperty, WebControl delegate)
    {
        super(delegate);
        prop_ = beanClassProperty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Context wrapContext(Context ctx)
    {
        Object obj = ctx.get(AbstractWebControl.BEAN);
        
        LogSupport.info(ctx, this, "Bean Class: " + obj.getClass());
        
        if (prop_.getBeanClass().isInstance(obj))
        {
            try
            {
                final Class beanClass;
                Object beanClassObj = prop_.get(obj);
                if (beanClassObj instanceof Class)
                {
                    beanClass = (Class) beanClassObj;
                }
                else if (beanClassObj instanceof XInfo)
                {
                    beanClass = ((XInfo)beanClassObj).getBeanClass();
                }
                else if (beanClassObj instanceof PropertyInfo)
                {
                    beanClass = ((PropertyInfo)beanClassObj).getBeanClass();
                }
                else if (beanClassObj instanceof String)
                {
                    beanClass = Class.forName((String)beanClassObj);
                }
                else
                {
                    beanClass = null;
                }

                if (Identifiable.class.isAssignableFrom(beanClass))
                {
                    Object prototype = XBeans.instantiate(beanClass, ctx);
                    Object id = ((Identifiable)prototype).ID();
                    
                    ctx = ctx.createSubContext();

                    Home       h    = new SortingHome(new EntityInfoTransientHome(ctx));
                    EntityInfo info = new EntityInfo();

                    ctx.put(EntityInfoHome.class, h);

                    info.setName(VALUE_LABEL);
                    
                    String idClassName = id.getClass().getName();
                    FacetMgr fMgr = (FacetMgr) ctx.get(FacetMgr.class);
                    
                    
                    if(id instanceof AbstractID)
                    {
                        info.setClassName(beanClass.getName());
                        info.setPrototype(prototype); 
                        ctx.put(FacetMgr.class, new RHSFacetMgr(fMgr, beanClass, (WebControl) fMgr.getInstanceOf(ctx, beanClass, WebControl.class)));
                    }
                    else
                    {
                    	info.setClassName(idClassName);
                    	info.setPrototype(id);
                        ctx.put(FacetMgr.class, new RHSFacetMgr(fMgr, id.getClass(), (WebControl) fMgr.getInstanceOf(ctx, id.getClass(), WebControl.class)));                    	
                    }                    

                    

                    h.create(info);
                }         
            }
            catch(Exception e)
            {
                
            }
        }
        
        return ctx;
    }

}
