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
package com.redknee.app.crm.extension.service.core;

import java.util.Map;

import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.beans.Validatable;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.PMLogMsg;

import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.bean.Service;
import com.redknee.app.crm.bean.ServiceTypeEnum;
import com.redknee.app.crm.bean.SubscriptionTypeAware;
import com.redknee.app.crm.bean.account.SubscriptionTypeEnum;
import com.redknee.app.crm.bean.core.SubscriptionType;
import com.redknee.app.crm.extension.ExtensionAware;
import com.redknee.app.crm.extension.service.AbstractServiceExtension;
import com.redknee.app.crm.support.AlcatelSSCSupportHelper;
import com.redknee.app.crm.support.BeanLoaderSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;



/**
 * Extension that contains key/value pairs that can be provisioned to an Alcatel SSC
 * for such broadband services
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class AlcatelSSCServiceExtension extends com.redknee.app.crm.extension.service.AlcatelSSCServiceExtension implements ContextAware, Validatable
{

    public AlcatelSSCServiceExtension()
    {
        super();
    }

    public AlcatelSSCServiceExtension(Context ctx)
    {
        super();
        setContext(ctx);
    }
    
    /**
     * Enhanced to use bean in context if available.
     * 
     * {@inheritDoc}
     */
    @Override
    public Service getService(Context ctx)
    {
        Service service = BeanLoaderSupportHelper.get(ctx).getBean(ctx, Service.class);
        if( service != null 
                && (AbstractServiceExtension.DEFAULT_SERVICEID == this.getServiceId()
                        || SafetyUtil.safeEquals(service.getID(), this.getServiceId())) )
        {
            return service;
        }
        
        if( AbstractServiceExtension.DEFAULT_SERVICEID == this.getServiceId() )
        {
            return null;
        }
        
        try
        {
            service = HomeSupportHelper.get(ctx).findBean(
                    ctx,
                    Service.class,
                    Long.valueOf(this.getServiceId()));
        }
        catch (HomeException e)
        {
        } 
        
        if( service != null && SafetyUtil.safeEquals(service.getID(), this.getServiceId()) )
        {
            return service;
        }
        
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void validate(Context ctx) throws IllegalStateException
    {
        CompoundIllegalStateException cise = new CompoundIllegalStateException();
        
        // Validate whether or not this extension is allowed to be contained within the parent bean.
        ExtensionAware parentBean = this.getParentBean(ctx);
        if (parentBean instanceof Service)
        {
            Service service = (Service) parentBean;
            if (!ServiceTypeEnum.ALCATEL_SSC.equals(service.getType()))
            {
                cise.thrown(new IllegalArgumentException(this.getName(ctx) + " extension only allowed for " + ServiceTypeEnum.ALCATEL_SSC + " services."));
            }
        }
        if (parentBean instanceof SubscriptionTypeAware)
        {
            SubscriptionTypeAware subTypeAware = (SubscriptionTypeAware) parentBean;
            SubscriptionType subscriptionType = subTypeAware.getSubscriptionType(ctx);
            if (subscriptionType == null
                    || !SubscriptionTypeEnum.BROADBAND.equals(subscriptionType.getTypeEnum()))
            {
                cise.thrown(new IllegalArgumentException(this.getName(ctx) + " extension only allowed for " + SubscriptionTypeEnum.BROADBAND + " subscription types."));
            }
        }

        // TODO: Validate extension contents (i.e. key/value pairs)
        
        cise.throwAll();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setKeyValuePairs(Map keyValuePairs) throws IllegalArgumentException
    {
        assertKeyValuePairs(keyValuePairs);
        assertBeanNotFrozen();
        
        PMLogMsg pm = new PMLogMsg("AlcatelSSC", "Service.initializeMap()");
        try
        {
            AlcatelSSCSupportHelper.get(getContext()).initializeMap(getContext(), KeyValueFeatureEnum.ALCATEL_SSC_SERVICE, keyValuePairs);
        }
        finally
        {
            if (getContext() != null)
            {
                pm.log(getContext());
            }
        }
        
        super.setKeyValuePairs(keyValuePairs);
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

    protected transient Context ctx_ = null;
}
