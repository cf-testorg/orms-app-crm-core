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

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeChangeListener;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.home.NotifyingHome;
import com.redknee.framework.xhome.home.NotifyingHomeItem;
import com.redknee.framework.xhome.visitor.RemoveAllVisitor;

import com.redknee.app.crm.support.ConfigChangeRequestSupport;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class ConfigSharingHome extends NotifyingHome
{
    public ConfigSharingHome(Context ctx, Home delegate)
    {
        this(ctx,
                (SharedConfigChangeListener) ctx.get(SharedConfigChangeListener.class, SharedConfigChangeListener.instance()),
                delegate);
    }

    public ConfigSharingHome(Context ctx, SharedConfigChangeListener listener, Home delegate)
    {
        super(delegate);
        setContext(ctx);
        
        super.addHomeChangeListener(listener);
        
        // Prevent super.store() from putting the old bean in the context.  We will handle that internally in this class.
        super.setSendBothObjectsInStore(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx, Object obj) throws HomeException
    {
        Context sCtx = ensureOldBeanInContext(ctx, obj);
        return super.create(sCtx, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object store(Context ctx, Object obj) throws HomeException
    {
        Context sCtx = ensureOldBeanInContext(ctx, obj);
        return super.store(sCtx, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Context ctx, Object bean) throws HomeException
    {
        boolean configChangeInProgress = ConfigChangeRequestSupportHelper.get(ctx).getConfigSharingBean(ctx, bean.getClass()) != null;
        
        Context sCtx = ensureOldBeanInContext(ctx, bean);
        getDelegate().remove(sCtx, bean);
        if (!configChangeInProgress)
        {
            fire(sCtx, bean, HomeOperationEnum.REMOVE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAll(Context ctx, Object where) throws HomeException
    {
        // Prevent the HomeChangeListener from having to explicitly deal with removeAll()
        forEach(ctx, new RemoveAllVisitor(this), where);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object cmd(Context ctx, Object cmdObj) throws HomeException
    {
        // The associated HomeChangeListener will not process cmd()
        return getDelegate(ctx).cmd(ctx, cmdObj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drop(Context ctx) throws HomeException
    {
        // The associated HomeChangeListener will not process drop()
        getDelegate(ctx).drop(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fire(Context ctx, Object source, HomeOperationEnum operation)
    {
        Object newBean = source;
        
        while (newBean instanceof NotifyingHomeItem)
        {
            newBean = ((NotifyingHomeItem)newBean).getNewObject();
        }
        Object oldBean =null;
        // If it is CREATE operation we should NOT have old bean in the DB.  However, sometimes default primary id (i.e. 1) might have entity with that id causes us send wrong oldBean
        // Hence, dont' send anything on create operation.
        if (operation != HomeOperationEnum.CREATE)
        {
            oldBean = ConfigChangeRequestSupportHelper.get(ctx).getConfigSharingBean(ctx, newBean.getClass());
        }
        
        NotifyingHomeItem item = new NotifyingHomeItem(oldBean, newBean);
        super.fire(ctx, item, operation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addHomeChangeListener(HomeChangeListener listener)
    {
        // NOP - not supported
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeHomeChangeListener(HomeChangeListener listener)
    {
        // NOP - not supported
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSendBothObjectsInStore(boolean flag)
    {
        // NOP - not supported
    }

    protected Context ensureOldBeanInContext(Context ctx, Object obj)
    {
        Context sCtx = ctx;
        if (obj instanceof Identifiable)
        {
            final ConfigChangeRequestSupport configChangeRequestSupport = ConfigChangeRequestSupportHelper.get(ctx);
            
            Object oldBean = configChangeRequestSupport.getConfigSharingBean(ctx, obj.getClass());
            if (oldBean == null)
            {
                try
                {
                    //Find the bean using top level home to allow cache hits
                    final Class<AbstractBean> cls = (Class<AbstractBean>)obj.getClass();
                    oldBean = HomeSupportHelper.get(sCtx).findBean(sCtx, cls, ((Identifiable) obj).ID());
                    if (oldBean != null)
                    {
                        sCtx = configChangeRequestSupport.getConfigSharingContext(ctx, oldBean);
                    }
                }
                catch (HomeException e)
                {
                    // Ignore this.  The entire bean will be updated.
                }
            }
        }
        return sCtx;
    }
}
