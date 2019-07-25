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
package com.redknee.app.crm.bundle.driver;

import com.redknee.app.crm.bundle.service.CRMBundleCategory;
import com.redknee.app.crm.bundle.service.CRMBundleProfile;
import com.redknee.app.crm.bundle.service.CRMSubscriberBucketProfile;

/**
 * Interface that returns the BM business services for use in CRM
 * @author arturo.medina@redknee.com
 *
 */
public interface BMRemoteService
{

    /**
     * Returns the bundle category service to install it in the main context
     * @return
     */
    public CRMBundleCategory getBundleCategoryService();
    
    
    /**
     * Returns the bundle profile service to install it in the main context
     * @return
     */
    public CRMBundleProfile getBundleProfileService();
    
    /**
     * Returns the subscriber bucket service to install it in the main context
     * @return
     */
    public CRMSubscriberBucketProfile getSubscriberBucketService();
    
}
