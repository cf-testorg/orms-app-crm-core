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
package com.redknee.app.crm.bean.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redknee.app.crm.bean.AdjustmentInfo;
import com.redknee.app.crm.bean.ServiceXInfo;
import com.redknee.app.crm.bean.SubscriptionTypeAware;
import com.redknee.app.crm.bean.TypeAware;
import com.redknee.app.crm.bean.service.ExternalAppMapping;
import com.redknee.app.crm.extension.Extension;
import com.redknee.app.crm.extension.ExtensionAware;
import com.redknee.app.crm.extension.ExtensionHolder;
import com.redknee.app.crm.extension.ExtensionLoadingAdapter;
import com.redknee.app.crm.extension.ExtensionSpidAdapter;
import com.redknee.app.crm.extension.service.ServiceExtension;
import com.redknee.app.crm.extension.service.ServiceExtensionXInfo;
import com.redknee.app.crm.home.core.CoreAdjustmentTypeHomePipelineFactory;
import com.redknee.app.crm.support.ExtensionSupportHelper;
import com.redknee.app.crm.support.ExternalAppMappingSupportHelper;
import com.redknee.app.crm.xhome.beans.xi.NonModelPropertyInfo;
import com.redknee.framework.xhome.beans.XCloneable;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xhome.context.ContextLocator;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.xenum.AbstractEnum;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;


/**
 * Concrete service class.
 *
 * @author candy.wong@redknee.com
 */
public class Service extends com.redknee.app.crm.bean.Service implements SubscriptionTypeAware, ExtensionAware, ContextAware, TypeAware
{
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;
    
    
    public SubscriptionType getSubscriptionType(Context ctx)
    {
        if (this.getSubscriptionType() != cachedSubTypeID_)
        {
            synchronized(CACHED_SUBSCRIPTION_TYPE_LOCK)
            {
                try
                {
                    cachedSubTypeObj_ = SubscriptionType.getSubscriptionTypeWithException(ctx, this.getSubscriptionType());
                    cachedSubTypeID_ = this.getSubscriptionType();
                }
                catch (HomeException e)
                {
                    String msg = "Unable to retreive SubscriptionType ID=" + getSubscriptionType();
                    new DebugLogMsg(this, msg, e).log(ctx);
                    new MinorLogMsg(this, msg + ": " + e.getMessage(), null).log(ctx);
                }
            }
        }

        return cachedSubTypeObj_;
    }
    
    
    public PropertyInfo getExtensionHolderProperty()
    {
        return EXTENSIONS_PROPERTY;
    }
    
    
    public Collection<Extension> getExtensions()
    {
        Collection<ExtensionHolder> holders = (Collection<ExtensionHolder>) getExtensionHolderProperty().get(this);
        return ExtensionSupportHelper.get(getContext()).unwrapExtensions(holders);
    }
    
    public List<ExtensionHolder> getExtensionHolders()
    {
        return serviceExtensions_;
    }
    
    
    /**
     * Lazy loading extensions.
     * {@inheritDoc}
     */
    public List getServiceExtensions()
    {
        synchronized (this)
        {
            if (getExtensionHolders() == null)
            {
                final Context ctx = getContext();
                try
                {
                    // To avoid deadlock, use a service "with extensions loaded" along with extension loading adapter.
                    Service serviceCopy = (Service) this.clone();
                    serviceCopy.setServiceExtensions(new ArrayList());
                    
                    serviceCopy = (Service) new ExtensionLoadingAdapter<ServiceExtension>(ServiceExtension.class, ServiceExtensionXInfo.SERVICE_ID).adapt(ctx, serviceCopy);
                    serviceCopy = (Service) new ExtensionSpidAdapter().adapt(ctx, serviceCopy);
                    
                    this.setServiceExtensions(serviceCopy.getServiceExtensions());
                }
                catch (Exception e)
                {
                    LogSupport.minor(ctx, this, "Exception occurred loading extensions. Extensions NOT loaded.");
                    LogSupport.debug(ctx, this, "Exception occurred loading extensions. Extensions NOT loaded.", e);
                }
            }
        }
        
        return getExtensionHolders();
    }
    
    public void setServiceExtensions(List<ExtensionHolder> serviceExtensions)
    {
        serviceExtensions_ = serviceExtensions;
    }
    
    
    /**
     * Adding cloning functionality to clone added fields.
     *
     * @return the clone object
     * @throws CloneNotSupportedException should not be thrown
     */
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        Service clone = (Service) super.clone();
        clone.setContext(null);
        return cloneServiceExtensionList(clone);
    }

    private Service cloneServiceExtensionList(final Service clone) throws CloneNotSupportedException
    {
        if (serviceExtensions_ != null)
        {
            final List extentionList = new ArrayList(serviceExtensions_.size());
            clone.setServiceExtensions(extentionList);
            for (final Iterator it = serviceExtensions_.iterator(); it.hasNext();)
            {
                extentionList.add(safeClone((XCloneable) it.next()));
            }
        }
        return clone;
    }
    
    /**
     * Handler is a transient field dependent on the Service Type.
     * Default to "Generic".
     *
     * @return Type of service.
     */
    @Override
    public String getHandler()
    {
        //we need a lookup in the context here.
        ExternalAppMapping record = null;
        try
        {
            record = ExternalAppMappingSupportHelper.get(getContext()).getExternalAppMapping(getContext(), getType());
        }
        catch (Throwable e)
        {
            LogSupport.major(getContext(), this, "ExternalAppMapping record does not exist for Service Type="
                        + getType().getIndex() + ". Add this configuration.", e);
        }
        return (record != null ? record.getHandler() : "Generic");
    }
    

    public Context getContext()
    {
        return ctx_;
    }

    public void setContext(final Context context)
    {
        ctx_ = context;
    }

    /**
     * Updates the given Service with it's AdjustmenType information.
     * 
     * @param service The Service for which to fill in AdjustmentType details.
     */
    private void loadAdjustmentTypeInformation(final Context ctx)
    {
        try
        {
            if (this.getAdjustmentType()>=0)
            {
                final Home home = (Home) ctx.get(CoreAdjustmentTypeHomePipelineFactory.ADJUSTMENT_TYPE_SYSTEM_HOME);
                final AdjustmentType type = (AdjustmentType) home.find(ctx, Integer.valueOf(this.getAdjustmentType()));
        
                if (type == null)
                {
                    return;
                }
        
                this.setAdjustmentTypeName(type.getName());
                this.setAdjustmentTypeDesc(type.getDesc());
        
                final Map spidInformation = type.getAdjustmentSpidInfo();
                final Object key = Integer.valueOf(this.getSpid());
                final AdjustmentInfo information = (AdjustmentInfo) spidInformation.get(key);
        
                if (information != null)
                {
                    this.setAdjustmentGLCode(information.getGLCode());
                    this.setAdjustmentInvoiceDesc(information.getInvoiceDesc());
                    this.setTaxAuthority(information.getTaxAuthority());
                }
                else
                {
                    this.setAdjustmentGLCode("");
                    this.setAdjustmentInvoiceDesc("");
                }
            }
        }
        catch (HomeException e)
        {
            LogSupport.minor(ctx, this, "Unable to load adjustment type info for service " + this.getID() + ": " + e.getMessage(), e);
        }
    }
    
    @Override
    public String getAdjustmentTypeName()
    {
        return getAdjustmentTypeName(getContext());
    }

    public String getAdjustmentTypeName(Context ctx)
    {
        if (super.getAdjustmentTypeName()==null && ctx!=null)
        {
            loadAdjustmentTypeInformation(ctx);
        }
        return super.getAdjustmentTypeName();
    }

    @Override
    public String getAdjustmentTypeDesc()
    {
        return getAdjustmentTypeDesc(getContext());
    }

    public String getAdjustmentTypeDesc(Context ctx)
    {
        if (super.getAdjustmentTypeDesc()==null && ctx!=null)
        {
            loadAdjustmentTypeInformation(ctx);
        }
        return super.getAdjustmentTypeDesc();
    }

    @Override
    public String getAdjustmentInvoiceDesc()
    {
        return getAdjustmentInvoiceDesc(getContext());
    }

    public String getAdjustmentInvoiceDesc(Context ctx)
    {
        if (super.getAdjustmentInvoiceDesc()==null && ctx!=null)
        {
            loadAdjustmentTypeInformation(ctx);
        }
        return super.getAdjustmentInvoiceDesc();
    }

    @Override
    public String getAdjustmentGLCode()
    {
        return getAdjustmentGLCode(getContext());
    }

    public String getAdjustmentGLCode(Context ctx)
    {
        if (super.getAdjustmentGLCode()==null && ctx!=null)
        {
            loadAdjustmentTypeInformation(ctx);
        }
        return super.getAdjustmentGLCode();
    }

    @Override
    public int getTaxAuthority()
    {
        return getTaxAuthority(getContext());
    }

    public int getTaxAuthority(Context ctx)
    {
        if (super.getAdjustmentTypeName()==null && ctx!=null)
        {
            loadAdjustmentTypeInformation(ctx);
        }
        return super.getTaxAuthority();
    }

    @Override
    public int getTypeEnumIndex()
    {
        if (getType()!=null)
        {
            return getType().getIndex();
        }
        else
        {
            return -1;
        }
    }

    @Override
    public AbstractEnum getEnumType()
    {
        return getType();
    }
    
    public Collection<Class> getExtensionTypes()
    {
        final Context ctx = ContextLocator.locate();
        Set<Class<ServiceExtension>> extClasses = ExtensionSupportHelper.get(ctx).getRegisteredExtensions(ctx,
                ServiceExtension.class);
        
        Collection<Class> desiredClass = new ArrayList<Class>();
        for (Class<ServiceExtension> ext : extClasses)
        {
            desiredClass.add(ext);
        }
        return desiredClass;

    }

        
    private transient Context ctx_;

    private long cachedSubTypeID_;
    private SubscriptionType cachedSubTypeObj_;
    private static final Object CACHED_SUBSCRIPTION_TYPE_LOCK = new Object();

    public static final String SERVICE_EXTENSIONS_FIELD_NAME = "serviceExtensions_";
    public static final PropertyInfo EXTENSIONS_PROPERTY = new NonModelPropertyInfo(Service.class, SERVICE_EXTENSIONS_FIELD_NAME, ServiceXInfo.instance());
    protected List<ExtensionHolder> serviceExtensions_ = null;
}
