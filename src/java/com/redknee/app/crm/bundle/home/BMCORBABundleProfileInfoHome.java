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

import com.redknee.app.crm.CoreCrmConstants;
import com.redknee.app.crm.bundle.BMReturnCodeMsgMapping;
import com.redknee.app.crm.bundle.exception.BundleManagerException;
import com.redknee.app.crm.client.BundleProfileProvisionClient;
import com.redknee.app.crm.client.ProvisioningHomeException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.BundleProfile;

/**
 * Stores and creates BM related bundle profile information via CORBA
 * @author @redknee.com
 *
 */
public class BMCORBABundleProfileInfoHome extends HomeProxy
{
    /**
     * 
     */
    private static final long serialVersionUID = -8766615573392241289L;    

    /**
     * Accepts the delegate and the context
     * @param ctx
     * @param delegate
     */
    public BMCORBABundleProfileInfoHome(Context ctx, Home delegate)
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
        com.redknee.app.crm.bean.core.BundleProfile bundleProfile = (com.redknee.app.crm.bean.core.BundleProfile) bean;
                
        BundleProfileProvisionClient client = (BundleProfileProvisionClient) ctx.get(BundleProfileProvisionClient.class);
        if (client == null)
        {
            new MajorLogMsg(this, "BMBundleProfileCorbaClient not found in context", null).log(ctx);
            throw new HomeException("BMBundleProfileCorbaClient not found in context");
        }
        BundleProfile retObj = null;
        try
        {
            retObj = client.createBundleProfile(bundleProfile);
            if (retObj != null)
            {
                bundleProfile.setBundleId(retObj.bundleId);
                if (bundleProfile.isFlex()
                        && bundleProfile.getNextBundleRef() == CoreCrmConstants.SELF_BUNDLE_ID)
                {
                    bundleProfile.setNextBundleRef(retObj.bundleId);
                }
            }
            else
            {
                new MinorLogMsg(this, "BMCORBABundleProfileInfoHome:create() failed", null).log(ctx);
                throw new ProvisioningHomeException("Error provisioning from Bundle Manager. Check logs for details",
                        null);
            }
        }
        catch (BundleManagerException e)
        {
            new MinorLogMsg(this, "BMCORBABundleProfileInfoHome:create() failed", e).log(ctx);
            throw new ProvisioningHomeException("Error creating bundle profile on BM: " + e.getMessage(), e);
        }
        
        // else if create was successful, continue to create bundle profile in XDBHome on CRM
        super.create(ctx, bundleProfile);
        return bundleProfile;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object store(Context ctx, Object bean)
    throws HomeException
    {
        com.redknee.app.crm.bean.core.BundleProfile bundleProfile = (com.redknee.app.crm.bean.core.BundleProfile) bean;


        BundleProfileProvisionClient client = (BundleProfileProvisionClient) ctx.get(BundleProfileProvisionClient.class);
        if (client == null)
        {
            new MajorLogMsg(this, "BMBundleProfileCorbaClient not found in context", null).log(ctx);
            throw new HomeException("BMBundleProfileCorbaClient not found in context");
        }
        int retInt = -1;
        try
        {
            retInt = client.updateBundleProfile(bundleProfile.getBundleId(), bundleProfile);
        }
        catch (BundleManagerException e)
        {
            throw new ProvisioningHomeException("Error updating bundle profile on BM: " + e.getMessage(), e);
        }
        if (retInt != 0)
        {
            new MinorLogMsg(this, "BMCORBABundleProfileInfoHome:store() failed: Result code = " + retInt, null).log(ctx);
            throw new ProvisioningHomeException("Error updating bundle profile on BM: " + 
                    BMReturnCodeMsgMapping.getMessage(retInt) + " (Code=" + retInt
                    + ")", retInt);
        }
        
        // else if store was successful, continue to store bundle profile in XDBHome on CRM
        super.store(ctx, bean);
        return bundleProfile;
    }

    @Override
    public void remove(Context ctx, Object bean)
    throws HomeException
    {
        com.redknee.app.crm.bean.core.BundleProfile bundleProfile = (com.redknee.app.crm.bean.core.BundleProfile) bean;

        BundleProfileProvisionClient client = (BundleProfileProvisionClient) ctx.get(BundleProfileProvisionClient.class);
        if (client == null)
        {
            new MajorLogMsg(this, "BMBundleProfileCorbaClient not found in context", null).log(ctx);
            throw new HomeException("ProductBMCorba client not found in context");
        }
        int retInt = -1;
        try
        {
            retInt = client.removeBundleProfile(bundleProfile.getSpid(), bundleProfile.getBundleId());
        }
        catch (BundleManagerException e)
        {
            throw new ProvisioningHomeException("Error removing bundle profile on BM: " + e.getMessage(), e);
        }
        if (retInt != 0)
        {
            new MinorLogMsg(this, "BMCORBABundleProfileInfoHome:remove() failed: Result code = "
                    + retInt, null).log(ctx);
            throw new ProvisioningHomeException("Error removing bundle profile from BM: " + BMReturnCodeMsgMapping.getMessage(retInt) + " (Code=" + retInt + ")", retInt);
        }

        super.remove(ctx, bean);
    }
    
}
