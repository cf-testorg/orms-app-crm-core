package com.redknee.app.crm.client;

import com.redknee.app.crm.bundle.exception.BundleManagerException;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.BundleProfile;


public interface BundleProfileProvisionClient
{
    public BundleProfile createBundleProfile(com.redknee.app.crm.bean.core.BundleProfile bundle) throws BundleManagerException;
    public BundleProfile getBundleProfile(int spId, int bundleId) throws BundleManagerException;
    public short updateBundleProfile(long bundleId, com.redknee.app.crm.bean.core.BundleProfile bundle) throws BundleManagerException;
    public short removeBundleProfile(int spId, long bundleId) throws BundleManagerException;
}
