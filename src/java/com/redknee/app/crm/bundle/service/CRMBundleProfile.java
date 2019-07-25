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
import java.util.Set;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;

import com.redknee.app.crm.bean.core.BundleProfile;
import com.redknee.app.crm.bundle.BundleSegmentEnum;
import com.redknee.app.crm.bundle.GroupChargingTypeEnum;
import com.redknee.app.crm.bundle.QuotaTypeEnum;
import com.redknee.app.crm.bundle.exception.BundleDoesNotExistsException;
import com.redknee.app.crm.bundle.exception.BundleManagerException;

/**
 * Handles CRM specific business logic to retrieve bundles
 * @author arturo.medina@redknee.com
 *
 */
public interface CRMBundleProfile extends BundleManagerProfile
{
    /**
     * Retrieves a set of Bundles from BM and mapped to CRM beans
     * The query will be based on the  QuotaTypeEnum from Bundle Manager
     * @param ctx
     * @param quotaScheme
     * @return
     * @throws BundleManagerException
     */
    public Home getBundlesPointBundlesByQuotaScheme(Context ctx, QuotaTypeEnum quotaScheme)
        throws BundleManagerException;
    
    /**
     * Retrieves a set Bundles based on the group charging scheme 
     * @param ctx
     * @param groupScheme
     * @return
     * @throws BundleManagerException
     */
    public Home getBundlesByGroupScheme(Context ctx, GroupChargingTypeEnum groupScheme)
        throws BundleManagerException;

    
    /**
     * Retrieves a set Bundles based on the group charging scheme 
     * @param ctx
     * @param groupScheme
     * @param ids 
     * @param inclusive if true the ids will be inclusive in the query, if false they will be exlcuded from the result
     * @return
     * @throws BundleManagerException
     */
    public Home getBundlesByGroupScheme(Context ctx, GroupChargingTypeEnum groupScheme, Collection ids, boolean inclusive)
        throws BundleManagerException;

    
   
    /**
     * Gets all bundles that fulfills a certain Group Charging Type
     * @param ctx
     * @param spid 
     * @param unavailableBundles List of all unavailable bundles 
     * @param categories list of all categories in the invoice bundle category
     * @return
     * @throws BundleManagerException
     */
    public Home getInvoiceCategoryBundles(Context ctx, int spid, Set unavailableBundles, Set categories)
        throws BundleManagerException;


    /**
     * Returns all one time bundles within a range of bundle ids, if empty it returns all of them
     * @param ctx
     * @param bundleIds
     * @return
     * @throws BundleDoesNotExistsException
     * @throws BundleManagerException
     */
    public Home getOneTimeBundles(Context ctx, Collection bundleIds)
        throws BundleDoesNotExistsException, BundleManagerException;
    
    /**
     * Returns all non one time bundles within a range of bundle ids, if empty it returns all of them
     * @param ctx
     * @param bundleIds
     * @return
     * @throws BundleDoesNotExistsException
     * @throws BundleManagerException
     */
    public Home getNonOneTimeBundles(final Context ctx, final Collection bundleIds) 
    throws BundleDoesNotExistsException, BundleManagerException;
    
    /**
     * Gets a Bundle by Category
     * @param ctx
     * @param categoryId
     * @return
     * @throws BundleManagerException
     */
    public Set<Long> getBundleIdsByCategoryIds(Context ctx, Set<Long> categoryIds) throws BundleManagerException;

    /**
     * Gets a Bundle by Category
     * @param ctx
     * @param categoryId
     * @return
     * @throws BundleManagerException
     */
    public Collection<BundleProfile> getBundleByCategory(Context ctx, long categoryId)
        throws BundleManagerException;

    /**
     * Returns a  bundle by a particular adjustment Type
     * The search will be based on adjustmentType and auxiliary Adjustment Type  
     * @param ctx
     * @param adjustmentId
     * @return
     * @throws BundleDoesNotExistsException
     * @throws BundleManagerException
     */
    public BundleProfile getBundleByAdjustmentType(Context ctx, int adjustmentId)
        throws BundleManagerException;


    /**
     * Returns a Home of bundles filtered by Segment.
     * If required, this method can be filtered by a group of bundles.
     * @param ctx
     * @param segment
     * @param includedBundles
     * @param onlyAuxiliary true if the home needs to return only auxiliary bundles
     * @return
     * @throws BundleDoesNotExistsException
     * @throws BundleManagerException
     */
    public Home getBundlesBySegment(Context ctx, BundleSegmentEnum segment, Collection includedBundles, boolean onlyAuxiliary)
        throws BundleDoesNotExistsException, BundleManagerException;

    /**
     * Retrieves a filtered home to select all bundles for a particular spid
     * @param ctx
     * @param spid
     * @return
     * @throws BundleDoesNotExistsException
     * @throws BundleManagerException
     */
    public Home getBundlesBySPID(Context ctx, int spid)
        throws BundleManagerException;
    
    /**
     * Retrieves a filtered home to select all auxiliary bundles for a particular spid
     * @param ctx
     * @param spid
     * @return
     * @throws BundleManagerException
     */
    public Home getAuxiliaryBundlesBySPID(Context ctx, int spid)
        throws BundleManagerException;
        
}
