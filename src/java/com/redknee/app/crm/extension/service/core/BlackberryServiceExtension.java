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
package com.redknee.app.crm.extension.service.core;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.bean.Service;
import com.redknee.app.crm.bean.ServiceTypeEnum;
import com.redknee.app.crm.extension.DependencyValidatableExtension;
import com.redknee.app.crm.extension.ExtensionAware;
import com.redknee.app.crm.extension.FinalExtension;
import com.redknee.app.crm.extension.MandatoryExtension;
import com.redknee.app.crm.extension.TypeDependentExtension;
import com.redknee.app.crm.extension.service.AbstractServiceExtension;
import com.redknee.app.crm.extension.service.BlackberryServiceExtensionXInfo;
import com.redknee.app.crm.license.LicenseAware;
import com.redknee.app.crm.support.BeanLoaderSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.app.crm.support.LicensingSupportHelper;
import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.xenum.AbstractEnum;

/**
 * Extension that contains BlackBerry services names that can be provisioned to a BlackBerry
 *
 * @author Marcio Marques
 * @since 8.5.0
 */
public class BlackberryServiceExtension extends com.redknee.app.crm.extension.service.BlackberryServiceExtension
        implements
            FinalExtension,
            DependencyValidatableExtension,
            MandatoryExtension, 
            TypeDependentExtension,
            LicenseAware
{
    
    public static final long serialVersionUID = 1;

    public BlackberryServiceExtension()
    {
        super();
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
    
    public void validateDependency(Context ctx) throws IllegalStateException
    {
        CompoundIllegalStateException cise = new CompoundIllegalStateException();
        
        // Validate whether or not this extension is allowed to be contained within the parent bean.
        ExtensionAware parentBean = this.getParentBean(ctx);
        if (parentBean instanceof Service)
        {
            Service service = (Service) parentBean;
            if (!ServiceTypeEnum.BLACKBERRY.equals(service.getType()))
            {
                cise.thrown(new IllegalArgumentException(this.getName(ctx) + " extension only allowed for " + ServiceTypeEnum.BLACKBERRY + " services."));
            }
        }
        
        if (getBlackberryServices().size()==0)
        {
            cise.thrown(new IllegalPropertyArgumentException(BlackberryServiceExtensionXInfo.BLACKBERRY_SERVICES,
                    ServiceTypeEnum.BLACKBERRY + " services must have at least one BlackBerry Service Name option choosen."));
        }
        
        cise.throwAll();
    }
    
    @Override
    public boolean isLicensed(Context ctx)
    {
        return LicensingSupportHelper.get(ctx).isLicensed(ctx, CoreCrmLicenseConstants.BLACKBERRY_LICENSE);
    }

    @Override
    public boolean isValidForType(AbstractEnum auxiliaryServiceType)
    {
        return ServiceTypeEnum.BLACKBERRY.equals(auxiliaryServiceType);
    }
}
