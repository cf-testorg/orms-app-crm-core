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
package com.redknee.app.crm.bundle.home;

import com.redknee.app.crm.bundle.BMReturnCodeMsgMapping;
import com.redknee.app.crm.bundle.BundleCategory;
import com.redknee.app.crm.bundle.UnitTypeEnum;
import com.redknee.app.crm.client.BundleCategoryProvisionClient;
import com.redknee.app.crm.client.ProvisioningHomeException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode;
import com.redknee.product.bundle.manager.provision.v5_0.category.Category;
import com.redknee.product.bundle.manager.provision.v5_0.category.ChargingScheme;
import com.redknee.product.bundle.manager.provision.v5_0.category.ServiceClass;

/**
 * Stores and creates BM related information to BM via CORBA
 * @author @redknee.com
 *
 */
public class BMCORBACategoryInfoHome extends HomeProxy
{

    /**
     * 
     */
    private static final long serialVersionUID = -8766615573392241284L;

    /**
     * Accepts the delegate and the context
     * @param ctx
     * @param delegate
     */
    public BMCORBACategoryInfoHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx, Object bean)
        throws HomeException
    {
        BundleCategory bundleCategory= (BundleCategory) bean;
        
        Category adaptedBundleCategory = adaptCategory(bundleCategory, true);
        
        BundleCategoryProvisionClient client = (BundleCategoryProvisionClient) ctx.get(BundleCategoryProvisionClient.class);
        if (client == null)
        {
            new MajorLogMsg(this, "BundleCategoryProvisionClient not found in context", null).log(ctx);
            throw new HomeException("BundleCategoryProvisionClient not found in context.");
        }
        int retInt = -1;
        retInt = client.createCategory(adaptedBundleCategory);
        if (retInt != 0)
        {
            new MinorLogMsg(this, "BMCORBACategoryInfoHome:create() failed: Result code = " + retInt, null).log(ctx);
            throw new ProvisioningHomeException("Error creating bundle category on BM: "
                    + BMReturnCodeMsgMapping.getMessage(retInt) + " (Code=" + retInt +
                    ")", retInt);
        }
        
        // else if create was successful, continue to create bundle category in XDBHome on CRM
        super.create(ctx, bean);
        return bundleCategory;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object store(Context ctx, Object bean)
    throws HomeException
    {
        BundleCategory bundleCategory= (BundleCategory) bean;
        
        Category adaptedBundleCategory = adaptCategory(bundleCategory, false);
        
        BundleCategoryProvisionClient client = (BundleCategoryProvisionClient) ctx.get(BundleCategoryProvisionClient.class);
        if (client == null)
        {
            new MajorLogMsg(this, "BundleCategoryProvisionClient not found in context", null).log(ctx);
            throw new HomeException("BundleCategoryProvisionClient not found in context.");
        }
        int retInt = -1;
        retInt = client.updateCategory(adaptedBundleCategory.spId, adaptedBundleCategory.categoryId, adaptedBundleCategory);
        if (retInt != 0)
        {
            new MinorLogMsg(this, "BMCORBACategoryInfoHome:store() failed: Result code = " + retInt, null).log(ctx);
            throw new ProvisioningHomeException("Error updating bundle category on BM: "
                    + BMReturnCodeMsgMapping.getMessage(retInt) + " (Code=" + retInt +
                    ")", retInt);
        }
        
        // else if store was successful, continue to store bundle category in XDBHome on CRM
        super.store(ctx, bean);
        return bundleCategory;
    }

    @Override
    public void remove(Context ctx, Object bean)
    throws HomeException
    {
        BundleCategory bundleCategory= (BundleCategory) bean;
  
        BundleCategoryProvisionClient client = (BundleCategoryProvisionClient) ctx.get(BundleCategoryProvisionClient.class);
        if (client == null)
        {
            new MajorLogMsg(this, "BundleCategoryProvisionClient not found in context", null).log(ctx);
            throw new HomeException("BundleCategoryProvisionClient not found in context.");
        }
        int retInt = -1;
        retInt = client.removeCategory(bundleCategory.getSpid(), bundleCategory.getCategoryId());
        if (retInt != ErrorCode.SUCCESS && retInt != ErrorCode.CATEGORY_NOT_FOUND)
        {
            new MinorLogMsg(this, "BMCORBACategoryInfoHome:remove() failed: Result code = " + retInt, null).log(ctx);
            throw new ProvisioningHomeException("Error removing bundle category from BM: "
                    + BMReturnCodeMsgMapping.getMessage(retInt) + " (Code=" + retInt +
                    ")", retInt);
        }
        
        super.remove(ctx, bean);
    }
    
    /**
     * Adapts BundleCategory to BM bundle category
     * 
     * @param bundleCategory
     * @return
     */
    private Category adaptCategory(BundleCategory bundleCategory, boolean setDefaults)
    {
        Category adaptedBundleCategory = new Category();

        adaptedBundleCategory.categoryId = bundleCategory.getCategoryId();
        adaptedBundleCategory.spId = bundleCategory.getSpid();
        adaptedBundleCategory.name = bundleCategory.getName();
        ServiceClass serviceClass = null;
        UnitTypeEnum unitType = bundleCategory.getUnitType();
        if(unitType.equals(UnitTypeEnum.VOLUME_BYTES))
        {
            serviceClass = ServiceClass.DATA;
        }
        else if(unitType.equals(UnitTypeEnum.VOLUME_SECONDS))
        {
            serviceClass = ServiceClass.VOICE;
        }
        else if(unitType.equals(UnitTypeEnum.EVENT_SMS_MMS))
        {
            serviceClass = ServiceClass.MESSAGING;
        }
        else if(unitType.equals(UnitTypeEnum.POINTS))
        {
            serviceClass = ServiceClass.REWARD;
        }
        else if (unitType.equals(UnitTypeEnum.EVENT_GENERIC))
        {
            serviceClass = ServiceClass.DATA;
        }
        else if (unitType.equals(UnitTypeEnum.SECONDARY_BALANCE))
        {
        	serviceClass = ServiceClass.SECONDARY_BALANCE;
        }

        adaptedBundleCategory.serviceClass = serviceClass;
        
        // overwrite these values for now since CRM-BM view doesn't contain this data
        adaptedBundleCategory.chargingScheme = ChargingScheme.SINGLE_PRIORITY_EXPIRY_PROVISION;
        adaptedBundleCategory.enableAutoProvision = false;
        
        return adaptedBundleCategory;
    }
}
