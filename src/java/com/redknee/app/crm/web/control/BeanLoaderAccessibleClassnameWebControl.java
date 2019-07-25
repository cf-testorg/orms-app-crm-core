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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.holder.StringHolder;
import com.redknee.framework.xhome.holder.StringHolderIdentitySupport;
import com.redknee.framework.xhome.holder.StringHolderTransientHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.support.IdentitySupport;
import com.redknee.framework.xhome.webcontrol.AbstractKeyWebControl;

import com.redknee.app.crm.support.BeanLoaderSupport;
import com.redknee.app.crm.support.BeanLoaderSupportHelper;
import com.redknee.util.snippet.log.Logger;


/**
 * 
 *
 * @author victor.stratan@redknee.com
 * @since 
 */
public class BeanLoaderAccessibleClassnameWebControl extends AbstractKeyWebControl
{
    public BeanLoaderAccessibleClassnameWebControl()
    {
        this(false);
    }
    
    public BeanLoaderAccessibleClassnameWebControl(final Class... baseCaseClasses)
    {
        this(false, baseCaseClasses);
    }


    public BeanLoaderAccessibleClassnameWebControl(final boolean autoPreview)
    {
        this(autoPreview, new Class[]{});
    }


    public BeanLoaderAccessibleClassnameWebControl(final boolean autoPreview, final Class... baseCaseClasses)
    {
        super(autoPreview);
        baseCaseClasses_ = baseCaseClasses;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDesc(Context ctx, Object obj)
    {
        final StringHolder bean = (StringHolder) obj;

        return bean.getValue();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object getHomeKey()
    {
        return null;
    }


    @Override
    public Home getHome(Context ctx)
    {
        synchronized (this)
        {
            if (home_ == null)
            {
                home_ = new StringHolderTransientHome(ctx);
                final Map map;

                BeanLoaderSupport beanLoaderSupport = BeanLoaderSupportHelper.get(ctx);
                if (baseCaseClasses_ != null)
                {
                    map = new HashMap();
                    for (Class baseCaseClass : baseCaseClasses_)
                    {
                        Map newMap = beanLoaderSupport.getBeanLoaderMap(ctx, baseCaseClass);
                        beanLoaderSupport.mergeBeanLoaderMaps(map, newMap);
                    }
                }
                else
                {
                    map = beanLoaderSupport.getBeanLoaderMap(ctx);
                }
                
                if (map != null)
                {
                    for (Iterator<Class> it = map.keySet().iterator(); it.hasNext();)
                    {
                        final Class clazz = it.next();
                        final StringHolder holder = new StringHolder();
                        holder.setValue(clazz.getName());
                        try
                        {
                            home_.create(ctx, holder);
                        }
                        catch (HomeException e)
                        {
                            Logger.minor(ctx, this, "Error while collecting the accessible Beans: " + e.getMessage(), e);
                        }
                    }
                }
            }
        }

        return home_;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public IdentitySupport getIdentitySupport()
    {
        return StringHolderIdentitySupport.instance();
    }

    protected Class[] baseCaseClasses_;
    protected StringHolderTransientHome home_;
}
