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
package com.redknee.app.crm.bundle.service;

import java.util.Collection;
import java.util.Map;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.core.BundleProfile;
import com.redknee.app.crm.bundle.exception.BundleAlreadyExistsException;
import com.redknee.app.crm.bundle.exception.BundleDoesNotExistsException;
import com.redknee.app.crm.bundle.exception.BundleManagerException;
import com.redknee.app.crm.bundle.exception.SubscriberProfileDoesNotExistException;

/**
 * Basic BM services requested by CRM
 * @author arturo.medina@redknee.com
 *
 */
public interface BundleManagerProfile
{

    /**
     * Requests BM a bundle creation with the mapped CRM profile
     * @param ctx
     * @param profile
     * @throws BundleAlreadyExistsException
     * @throws BundleManagerException
     */
    public void createBundle(Context ctx, BundleProfile profile)
        throws BundleAlreadyExistsException, BundleManagerException;
    
    /**
     * Retrieves a Bundle from BM and maps it in a CRM profile  
     * @param ctx
     * @param bundleId 
     * @return
     * @throws BundleDoesNotExistsException
     * @throws BundleManagerException
     */
    public BundleProfile getBundleProfile(Context ctx, int spId, long bundleId)
        throws BundleDoesNotExistsException, BundleManagerException;
    
    /**
     * Calls BM to switch bundles from a subscriber 
     * @param ctx
     * @param msisdn
     * @param spid
     * @param oldBundles
     * @param newBundles
     * @param options 
     * @return a Map of bundleIDs/amount charged to the subscriber at switching time 
     * @throws SubscriberProfileDoesNotExistException
     * @throws BundleManagerException
     */
    public Map<Long, Long> switchBundles(Context ctx, String msisdn, int spid, int subscriptionType, Collection oldBundles, Collection newBundles, Collection options)
        throws SubscriberProfileDoesNotExistException, BundleManagerException;
    
    /**
     * Removes a profile from BM
     * @param ctx
     * @param bundleId
     * @throws BundleDoesNotExistsException
     * @throws BundleManagerException
     */
    public void removeBundleProfile(Context ctx, int spId, long bundleId)
        throws BundleDoesNotExistsException, BundleManagerException;
    
    /**
     * Updates a bundle profile in BM
     * @param ctx
     * @param profile
     * @throws BundleDoesNotExistsException
     * @throws BundleManagerException
     */
    public void updateBundle(Context ctx, BundleProfile profile)
        throws BundleDoesNotExistsException, BundleManagerException;
    
}
