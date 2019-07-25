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
package com.redknee.app.crm.xhome.adapter;

import java.util.HashMap;
import java.util.Map;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.bean.ServicePackageVersion;



/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class ServicePackageVersionBeanAdapter extends ExtendedBeanAdapter<com.redknee.app.crm.bean.ServicePackageVersion, com.redknee.app.crm.bean.core.ServicePackageVersion>
{

    private static final long serialVersionUID = 1L;

	/**
     * @param baseClass
     * @param concreteClass
     */
    public ServicePackageVersionBeanAdapter()
    {
        super(com.redknee.app.crm.bean.ServicePackageVersion.class, com.redknee.app.crm.bean.core.ServicePackageVersion.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object adapt(Context ctx, Object bean) throws HomeException
    {
        if (bean instanceof com.redknee.app.crm.bean.ServicePackageVersion)
        {
        	Context appctx = (Context)ctx.get("app");
        	com.redknee.app.crm.bean.ServicePackageVersion version = (ServicePackageVersion) bean;
            com.redknee.app.crm.bean.core.ServicePackageVersion coreVersion = (com.redknee.app.crm.bean.core.ServicePackageVersion) super.adapt(ctx, bean);
            
            // Adapt the service fee map contained within
            Map<Object, com.redknee.app.crm.bean.ServiceFee2> serviceFees = version.getServiceFees();
            Map<Object, com.redknee.app.crm.bean.core.ServiceFee2> coreServiceFees = new HashMap<Object, com.redknee.app.crm.bean.core.ServiceFee2>();
            for (com.redknee.app.crm.bean.ServiceFee2 fee : serviceFees.values())
            {
                com.redknee.app.crm.bean.core.ServiceFee2 coreFee = (com.redknee.app.crm.bean.core.ServiceFee2) new StaticContextBeanAdapter<com.redknee.app.crm.bean.ServiceFee2, com.redknee.app.crm.bean.core.ServiceFee2>(
                        com.redknee.app.crm.bean.ServiceFee2.class, 
                        com.redknee.app.crm.bean.core.ServiceFee2.class, appctx).adapt(ctx, fee);
                coreServiceFees.put(coreFee.ID(), coreFee);
            }
            coreVersion.setServiceFees(coreServiceFees);

            // Adapt the service fee map contained within
            Map<Object, com.redknee.app.crm.bundle.BundleFee> bundleFees = version.getBundleFees();
            Map<Object, com.redknee.app.crm.bean.core.BundleFee> coreBundleFees = new HashMap<Object, com.redknee.app.crm.bean.core.BundleFee>();
            for (com.redknee.app.crm.bundle.BundleFee fee : bundleFees.values())
            {
                com.redknee.app.crm.bean.core.BundleFee coreFee = (com.redknee.app.crm.bean.core.BundleFee) new StaticContextBeanAdapter<com.redknee.app.crm.bundle.BundleFee, com.redknee.app.crm.bean.core.BundleFee>(
                        com.redknee.app.crm.bundle.BundleFee.class, 
                        com.redknee.app.crm.bean.core.BundleFee.class, appctx).adapt(ctx, fee);
                coreBundleFees.put(coreFee.ID(), coreFee);
            }
            coreVersion.setBundleFees(coreBundleFees);
            
            return coreVersion;
        }
        
        return bean;
    }
    
    
/*
 */
}
