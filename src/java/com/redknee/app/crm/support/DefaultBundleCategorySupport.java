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
package com.redknee.app.crm.support;

import com.redknee.framework.license.LicenseMgr;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bundle.BundleCategoryXInfo;
import com.redknee.app.crm.bundle.license.BMLicenseConstants;
import com.redknee.product.bundle.manager.api.v21.BundleCategoryApiXInfo;

public class DefaultBundleCategorySupport implements BundleCategorySupport
{
    protected static BundleCategorySupport instance_ = null;
    public static BundleCategorySupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultBundleCategorySupport();
        }
        return instance_;
    }

    protected DefaultBundleCategorySupport()
    {
    }

    
    /**
     * {@inheritDoc}
     */
    public PropertyInfo getBundleCategoryIdXInfo(Context ctx)
    {
        PropertyInfo catId = BundleCategoryApiXInfo.CATEGORY_ID;
        
        LicenseMgr manager = (LicenseMgr) ctx.get(LicenseMgr.class);
        if (manager.isLicensed(ctx, BMLicenseConstants.BUNDLE_MANAGER_27))
        {
            catId = BundleCategoryXInfo.CATEGORY_ID;
        }
        
        return catId;
    }

    
    /**
     * {@inheritDoc}
     */
    public PropertyInfo getBundleCategoryNameXInfo(Context ctx)
    {
        PropertyInfo name = BundleCategoryApiXInfo.NAME;
        
        LicenseMgr manager = (LicenseMgr) ctx.get(LicenseMgr.class);
        if (manager.isLicensed(ctx, BMLicenseConstants.BUNDLE_MANAGER_27))
        {
            name = BundleCategoryXInfo.NAME;
        }
        
        return name;
        
    }

    
    /**
     * {@inheritDoc}
     */
    public PropertyInfo getBundleCategorySPIDXInfo(Context ctx)
    {
        PropertyInfo spid = BundleCategoryApiXInfo.SPID;
        
        LicenseMgr manager = (LicenseMgr) ctx.get(LicenseMgr.class);
        if (manager.isLicensed(ctx, BMLicenseConstants.BUNDLE_MANAGER_27))
        {
            spid = BundleCategoryXInfo.SPID;
        }
        
        return spid;
        
    }


}
