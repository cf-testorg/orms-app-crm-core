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
package com.redknee.app.crm.bundle.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.redknee.app.crm.bean.core.BundleProfile;
import com.redknee.app.crm.bundle.BMBundleCategoryAssociation;
import com.redknee.app.crm.bundle.BMBundleCategoryAssociationHome;
import com.redknee.app.crm.bundle.BMBundleCategoryAssociationXInfo;
import com.redknee.app.crm.bundle.BundleProfileHome;
import com.redknee.app.crm.bundle.BundleProfileXInfo;
import com.redknee.app.crm.bundle.BundleSegmentEnum;
import com.redknee.app.crm.bundle.GroupChargingTypeEnum;
import com.redknee.app.crm.bundle.QuotaTypeEnum;
import com.redknee.app.crm.bundle.RecurrenceOptions;
import com.redknee.app.crm.bundle.RecurrenceTypeEnum;
import com.redknee.app.crm.bundle.exception.BundleAlreadyExistsException;
import com.redknee.app.crm.bundle.exception.BundleDoesNotExistsException;
import com.redknee.app.crm.bundle.exception.BundleManagerException;
import com.redknee.app.crm.bundle.exception.SubscriberProfileDoesNotExistException;
import com.redknee.app.crm.client.BMSubscriberBucketCorbaClient;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.In;
import com.redknee.framework.xhome.elang.NEQ;
import com.redknee.framework.xhome.elang.Not;
import com.redknee.framework.xhome.elang.Or;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.product.bundle.manager.provision.common.param.ActivationSchemeType;
import com.redknee.product.bundle.manager.provision.common.param.Parameter;
import com.redknee.product.bundle.manager.provision.common.param.ParameterID;
import com.redknee.product.bundle.manager.provision.common.param.ParameterValue;

/**
 * Handles all services for bundles in CRM from the CORBA perspective
 * @author ling.tang@redknee.com
 *
 */
public class CORBABundleProfileHandler implements CRMBundleProfile
{

    /**
     * {@inheritDoc}
     */
    public Collection<BundleProfile> getBundleByCategory(Context ctx, long categoryId) throws BundleManagerException
    {
        Home bundleHome = getBundleProfileHome(ctx);
        Collection<BundleProfile>  result = null;
        Set<Long> categoryIds = new HashSet<Long>();
        categoryIds.add(Long.valueOf(categoryId));
        try
        {
            result = bundleHome.select(ctx, new In(BundleProfileXInfo.BUNDLE_ID, getBundleIdsByCategoryIds(ctx, categoryIds)));
        }
        catch (HomeException e)
        {
            String message = "HomeException while getting bundles by category id " + categoryId + ": " + e.getMessage();
            LogSupport.major(ctx, this, message);
            throw new BundleManagerException(message, e);
        }
        return result;
    }

    public Set<Long> getBundleIdsByCategoryIds(Context ctx, Set<Long> categoryIds) throws BundleManagerException
    {
        Home assocHome = (Home) ctx.get(BMBundleCategoryAssociationHome.class);
        Collection assocColl = null;
        Set<Long> bundleIds = new HashSet<Long>();
        try
        {
            assocColl = assocHome.select(ctx, new In(BMBundleCategoryAssociationXInfo.CATEGORY_ID, categoryIds));
            if (assocColl != null)
            {
                final Iterator<BMBundleCategoryAssociation> iter = assocColl.iterator();
                BMBundleCategoryAssociation assoc = null;
                while (iter.hasNext())
                {
                    assoc = iter.next();
                    bundleIds.add(Long.valueOf(assoc.getBundleId()));
                }
            }
        }
        catch (final HomeException e)
        {
            String message = "HomeException while getting bundle ids by category ids: " + e.getMessage();
            LogSupport.major(ctx, this, message);
            throw new BundleManagerException(message, e);
        }
        
        return bundleIds;

    }

    /**
     * {@inheritDoc}
     */
    public BundleProfile getBundleByAdjustmentType(Context ctx, int adjustmentId) throws
            BundleManagerException
    {
        Or adjustmentTypesQuery = new Or();
        adjustmentTypesQuery.add(new EQ(BundleProfileXInfo.AUXILIARY_ADJUSTMENT_TYPE, Integer.valueOf(adjustmentId)));
        adjustmentTypesQuery.add(new EQ(BundleProfileXInfo.ADJUSTMENT_TYPE, Integer.valueOf(adjustmentId)));
        Home bundleHome = getBundleProfileHome(ctx);
        try
        {
            return (BundleProfile) bundleHome.find(ctx, adjustmentTypesQuery);
        }
        catch (HomeException e)
        {
            throw new BundleManagerException("Home Exception while finding the bundle by adjustment type " + adjustmentId);
        }

    }


    /**
     * {@inheritDoc}
     */
    public Home getBundlesByGroupScheme(Context ctx, GroupChargingTypeEnum groupScheme) throws BundleManagerException
    {
        EQ eq = new EQ(BundleProfileXInfo.GROUP_CHARGING_SCHEME, groupScheme);
        Home bundleHome = getBundleProfileHome(ctx);
        bundleHome = bundleHome.where(ctx, eq);
        return bundleHome;
    }


    /**
     * {@inheritDoc}
     */
    public Home getBundlesByGroupScheme(Context ctx, GroupChargingTypeEnum groupScheme, Collection exclusiveIds,
            boolean inclusive) throws BundleManagerException
    {
        And predicate = new And();
        Set exclusiveSet = new HashSet(exclusiveIds);
        if (inclusive)
        {
            predicate.add(new EQ(BundleProfileXInfo.GROUP_CHARGING_SCHEME, groupScheme));
            if (exclusiveIds != null && exclusiveIds.size() > 0)
            {
                predicate.add(new In(BundleProfileXInfo.BUNDLE_ID, exclusiveSet));
            }
        }
        else
        {
            predicate.add(new NEQ(BundleProfileXInfo.GROUP_CHARGING_SCHEME, groupScheme));
            if (exclusiveIds != null && exclusiveIds.size() > 0)
            {
                predicate.add(new Not(new In(BundleProfileXInfo.BUNDLE_ID, exclusiveSet)));
            }
        }
        Home bundleHome = getBundleProfileHome(ctx);
        bundleHome = bundleHome.where(ctx, predicate);
        return bundleHome;
    }


    /**
     * {@inheritDoc}
     */
    public Home getBundlesPointBundlesByQuotaScheme(Context ctx, QuotaTypeEnum quotaScheme)
            throws BundleManagerException
    {
        Home bundleHome = getBundleProfileHome(ctx);
        bundleHome = bundleHome.where(ctx, new EQ(BundleProfileXInfo.QUOTA_SCHEME, quotaScheme));
        return bundleHome;
    }


    /**
     * {@inheritDoc}
     */
    public Home getBundlesBySegment(Context ctx, BundleSegmentEnum segment, Collection includedBundles,
            boolean onlyAuxiliary) throws BundleDoesNotExistsException, BundleManagerException
    {
        And predicate = new And();
        predicate.add(new EQ(BundleProfileXInfo.SEGMENT, segment));
        if (includedBundles != null && includedBundles.size() > 0)
        {
            Set bundleSet = new HashSet(includedBundles);
            predicate.add(new In(BundleProfileXInfo.BUNDLE_ID, bundleSet));
        }
        if (onlyAuxiliary)
        {
            predicate.add(new EQ(BundleProfileXInfo.AUXILIARY, Boolean.TRUE));
        }
        Home bundleHome = getBundleProfileHome(ctx);
        bundleHome = bundleHome.where(ctx, predicate);
        return bundleHome;
    }


    /**
     * {@inheritDoc}
     */
    public Home getInvoiceCategoryBundles(Context ctx, int spid, Set unavailableBundles, Set categories)
            throws BundleManagerException
    {
        And predicate = new And();
        predicate.add(new EQ(BundleProfileXInfo.SPID, Integer.valueOf(spid)));
        if (unavailableBundles != null && unavailableBundles.size() > 0)
        {
            predicate.add(new Not(new In(BundleProfileXInfo.BUNDLE_ID, unavailableBundles)));
        }
        if (categories != null && categories.size() > 0)
        {
            predicate.add(new In(BundleProfileXInfo.BUNDLE_CATEGORY_IDS, categories));
        }
        Home bundleHome = getBundleProfileHome(ctx);
        bundleHome = bundleHome.where(ctx, predicate);
        return bundleHome;
    }


    /**
     * {@inheritDoc}
     */
    public Home getOneTimeBundles(Context ctx, Collection bundleIds) throws BundleDoesNotExistsException,
            BundleManagerException
    {
        Set bundleIdsSet = new HashSet(bundleIds);
        And predicate = new And();
        if (bundleIdsSet.size() > 0)
        {
            predicate.add(new In(BundleProfileXInfo.BUNDLE_ID, bundleIdsSet));
        }
        Or oneTimeCheck = new Or();
        oneTimeCheck.add(new EQ(BundleProfileXInfo.RECURRENCE_SCHEME, RecurrenceTypeEnum.ONE_OFF_FIXED_INTERVAL));
        oneTimeCheck.add(new EQ(BundleProfileXInfo.RECURRENCE_SCHEME, RecurrenceTypeEnum.ONE_OFF_FIXED_DATE_RANGE));
        predicate.add(oneTimeCheck);
        Home bundleHome = getBundleProfileHome(ctx);
        bundleHome = bundleHome.where(ctx, predicate);
        return bundleHome;
    }


    /**
     * {@inheritDoc}
     */
    public Home getNonOneTimeBundles(Context ctx, Collection bundleIds) throws BundleDoesNotExistsException,
            BundleManagerException
    {
        Set bundleIdsSet = new HashSet(bundleIds);
        And predicate = new And();
        if (bundleIdsSet.size() > 0)
        {
            predicate.add(new In(BundleProfileXInfo.BUNDLE_ID, bundleIdsSet));
        }
        Or oneTimeCheck = new Or();
        oneTimeCheck.add(new EQ(BundleProfileXInfo.RECURRENCE_SCHEME, RecurrenceTypeEnum.ONE_OFF_FIXED_INTERVAL));
        oneTimeCheck.add(new EQ(BundleProfileXInfo.RECURRENCE_SCHEME, RecurrenceTypeEnum.ONE_OFF_FIXED_DATE_RANGE));
        Not notOneTimeCheck = new Not(oneTimeCheck);
        predicate.add(notOneTimeCheck);
        Home bundleHome = getBundleProfileHome(ctx);
        bundleHome = bundleHome.where(ctx, predicate);
        return bundleHome;
    }
    /**
     * {@inheritDoc}
     */
    public void createBundle(Context ctx, BundleProfile profile) throws BundleAlreadyExistsException,
            BundleManagerException
    {
        Home bundleHome = getBundleProfileHome(ctx);
        try
        {
            BundleProfile bundle = null;
            try
            {
                bundle = getBundleProfile(ctx, profile.getSpid(), profile.getBundleId());
            }
            catch (BundleDoesNotExistsException e)
            {
            }
            if (bundle == null)
            {
                bundleHome.create(ctx, profile);
            }
            else
            {
                throw new BundleAlreadyExistsException("Bundle ID " + profile.getBundleId());
            }
        }
        catch (HomeException e)
        {
            String msg = "Home exception while creating bundle profile " + profile + " on bundle manager: "
                    + e.getMessage();
            LogSupport.major(ctx, this, msg);
            throw new BundleManagerException(msg, e);
        }
    }


    /**
     * {@inheritDoc}
     */
    public BundleProfile getBundleProfile(Context ctx, int spId, long bundleId) throws BundleDoesNotExistsException,
            BundleManagerException
    {   
        Home bundleHome = getBundleProfileHome(ctx);
        BundleProfile profile = null;
        try
        {
            profile = (BundleProfile) bundleHome.find(ctx, Long.valueOf(bundleId));
        }
        catch (HomeException e)
        {
            String msg = "Home exception while getting bundle profile with bundle ID" + bundleId + " on bundle manager: "
                    + e.getMessage();
            LogSupport.major(ctx, this, msg);
            throw new BundleManagerException(msg, e);
        }
        if (profile == null)
        {
            throw new BundleDoesNotExistsException("Bundle ID " + bundleId );
        }
        if(spId != profile.getSpid())
        {
            throw new BundleDoesNotExistsException("Bundle ID " + bundleId + " Spid " + spId);
        }            
        return profile;
    }


    /**
     * {@inheritDoc}
     */
    public void removeBundleProfile(Context ctx, int spId, long bundleId) throws BundleDoesNotExistsException,
            BundleManagerException
    {
        Home bundleHome = getBundleProfileHome(ctx);
        try
        {
            BundleProfile bundleProfile = getBundleProfile(ctx, spId, bundleId);
            if (bundleProfile != null)
            {
                bundleHome.remove(ctx, bundleProfile);
            }
            else
            {
                throw new BundleDoesNotExistsException("SPID: " + spId +", Bundle ID: " + bundleId);
            }
        }
        catch (HomeException e)
        {
            String msg = "Home exception while removing bundle id " + bundleId + " on bundle manager: "
                    + e.getMessage();
            LogSupport.major(ctx, this, msg);
            throw new BundleManagerException(msg, e);
        }
    }


    /**
     * {@inheritDoc}
     */
    public Map<Long, Long> switchBundles(Context ctx, String msisdn, int spid, int subscriptionType, Collection oldBundles, Collection newBundles,
            Collection options) throws SubscriberProfileDoesNotExistException, BundleManagerException
    {
        final BMSubscriberBucketCorbaClient client = getBucketCorbaClient(ctx);
        Map<Long, Long> result = new HashMap<Long, Long>();
        try
        {
            // Parse lists of bundle ids
            long[] oldBundleIds = new long[oldBundles.size()];
            Iterator iter = oldBundles.iterator();
            int index = 0;
            while (iter.hasNext())
            {
                oldBundleIds[index] = ((Long) iter.next()).longValue();
                index += 1;
            }
            
            long[] newBundleIds = new long[newBundles.size()];
            iter = newBundles.iterator();
            index = 0;
            while (iter.hasNext())
            {
                newBundleIds[index] = ((Long) iter.next()).longValue();
                index += 1;
            }            
            
            // Parse the recurrence options
            iter = options.iterator();
            Parameter[][] inParamSet = new Parameter[options.size()][4];
            Parameter filler = new Parameter();
            filler.value = new ParameterValue();
            filler.value.intValue(0);
            index = 0;
            while (iter.hasNext())
            { 
            	
                RecurrenceOptions option = (RecurrenceOptions) iter.next();
                ParameterValue paramValue = new ParameterValue();
                long bundleId =   option.getBundleId();
                paramValue.longValue(option.getBundleId());
                inParamSet[index][0] = new Parameter(ParameterID.IN_BUNDLE_ID, paramValue);
                
                if (option.getExpiryTimePresent())
                {
                    ParameterValue paramValue2 = new ParameterValue();
                    paramValue2.longValue(option.getExpiryTime());
                    inParamSet[index][1] = new Parameter(ParameterID.IN_BUCKET_EXPIRY_TIME, paramValue2);               
                }
                else
                {
                    inParamSet[index][1] = filler;
                }
                
                if (option.getBillingDatePresent())
                {
                    ParameterValue paramValue3 = new ParameterValue();
                    if (option.getBillingDate() == -1)
                    {
                        // this is to accomodate for the NEW way of specifying the FIRST_CALL_ACTIVATION activation
                        // idealy this should be instead of [2] IN_BILL_CYCLE_DATE param. for now BM does not accept it
                        ParameterValue paramValue2 = new ParameterValue();
                        paramValue2.intValue(ActivationSchemeType.FIRST_CALL_ACTIVATION);
                        inParamSet[index][1] = new Parameter(ParameterID.IN_ACTIVATION_SCHEME, paramValue3);
                    }

                    // this is to accomodate for the old way of specifying the FIRST_CALL_ACTIVATION activation
                    paramValue3.intValue(option.getBillingDate());
                    inParamSet[index][2] = new Parameter(ParameterID.IN_BILL_CYCLE_DATE, paramValue3);
                }
                else
                {
                    inParamSet[index][2] = filler;
                    
                }
                
                Home xdbHome = (Home) ctx.get(com.redknee.app.crm.bundle.BundleProfileXDBHome.class);
        		boolean prorateBundleQuota = false;
        		if(xdbHome != null)
        		{
        			try
        			{
        				com.redknee.app.crm.bundle.BundleProfile profile = (com.redknee.app.crm.bundle.BundleProfile) xdbHome.find(ctx, bundleId);
        				if(profile != null)
        				{
        					prorateBundleQuota = profile.getProrateBundleQuota();
        				}
        				
        			}catch(com.redknee.framework.xhome.home.HomeException he)
        			{
        				LogSupport.minor(ctx, BMSubscriberBucketCorbaClient.class, "Unable to fetch bundle Profile DB for BundleID :" + bundleId);
        			}
        		}
        		if (LogSupport.isDebugEnabled(ctx))
                {
                    new DebugLogMsg(this, "Sending prorateBundleQuota as : " + prorateBundleQuota, null);
                }
        		
        		paramValue = new ParameterValue();
                paramValue.booleanValue((prorateBundleQuota));
                inParamSet[index][3] = new Parameter(ParameterID.IN_PRORATE_LIMIT, paramValue);
                
                index += 1;
            }
            
            result = client.switchBundles(msisdn, spid, subscriptionType, oldBundleIds, newBundleIds, inParamSet);    
            if (result == null)
            {
                String msg = "Exception encountered switching bundles on bundle manager";
                LogSupport.major(ctx, this, "Result was null when calling client.switchBundles() for [msisdn=" + msisdn + ", spid=" + spid + "]");
                throw new BundleManagerException(msg); 
            }
        }
        catch (BundleManagerException bme)
        {
            // Already logged.  Rethrow for GUI.
            throw bme;
        }
        catch (Throwable t)
        {
            String msg = "Exception encountered switching bundles on bundle manager: "
                    + t.getMessage();
            LogSupport.major(ctx, this, msg, t);
            throw new BundleManagerException(msg, t);           
        }
        return result;
    }


    public void updateBundle(Context ctx, BundleProfile profile) throws BundleDoesNotExistsException,
            BundleManagerException
    {
        Home bundleHome = getBundleProfileHome(ctx);
        try
        {
            BundleProfile bundle = null;
            try
            {
                bundle = getBundleProfile(ctx, profile.getSpid(), profile.getBundleId());
            }
            catch (BundleDoesNotExistsException e)
            {
            }
            if (bundle != null)
            {
                bundleHome.store(ctx, profile);
            }
            else
            {
                throw new BundleDoesNotExistsException("Bundle ID " + profile.getBundleId());
            }
        }
        catch (HomeException e)
        {
            String msg = "Home exception while updating bundle profile " + profile + " on bundle manager: "
                    + e.getMessage();
            LogSupport.major(ctx, this, msg, e);
            throw new BundleManagerException(msg, e);
        }
    }


    private Home getBundleProfileHome(Context ctx)
    {
        return (Home) ctx.get(BundleProfileHome.class);
    }


    private BMSubscriberBucketCorbaClient getBucketCorbaClient(Context ctx)
    {
        return (BMSubscriberBucketCorbaClient) ctx.get(BMSubscriberBucketCorbaClient.class);
    }


    public Home getAuxiliaryBundlesBySPID(Context ctx, int spid)
            throws BundleManagerException
    {
        Home home = getBundleProfileHome(ctx);
        And predicate = new And();
        predicate.add(new EQ(BundleProfileXInfo.SPID, Integer.valueOf(spid)));
        predicate.add(new EQ(BundleProfileXInfo.AUXILIARY, Boolean.TRUE));
        home = home.where(ctx, predicate);
        return home;
    }


    public Home getBundlesBySPID(Context ctx, int spid)
            throws BundleManagerException
    {
        Home home = getBundleProfileHome(ctx);
        EQ eq = new EQ(BundleProfileXInfo.SPID, Integer.valueOf(spid));
        home = home.where(ctx, eq);
        return home;
    }
}
