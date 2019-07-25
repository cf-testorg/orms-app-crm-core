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
package com.redknee.app.crm.client;

import static com.redknee.app.crm.client.CorbaClientTrapIdDef.SUBSCRIBER_BUCKET_PROV_SVC_DOWN;
import static com.redknee.app.crm.client.CorbaClientTrapIdDef.SUBSCRIBER_BUCKET_PROV_SVC_UP;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.redknee.app.crm.bundle.BMReturnCodeMsgMapping;
import com.redknee.app.crm.bundle.exception.BucketDoesNotExistsException;
import com.redknee.app.crm.bundle.exception.BundleManagerException;
import com.redknee.app.crm.support.BundleProfileSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.product.bundle.manager.provision.v5_0.bucket.*;
import com.redknee.product.bundle.manager.provision.common.param.Parameter;

/**
 * Provides an interface for communicating with Bundle Manager bundle category corba services
 * 
 * Support clustered corba client
 * Refactored reusable elements to an abstract class AbstractCrmClient<T>
 * @author rchen
 * @since June 29, 2009 
 * 
 */
public class BMSubscriberBucketCorbaClient extends AbstractCrmClient<SubscriberBucketProvision>
{
    /**
     * Name of the CORBA client.
     */
    private static final String CLIENT_NAME = "SubscriberBucketProvisionClient";

    private static final Class<SubscriberBucketProvision> SERVICE_TYPE = SubscriberBucketProvision.class;

    /**
     * Service description.
     */
    private static final String SERVICE_DESCRIPTION = "CORBA client for Subscriber Bucket Provisioning services";

    public BMSubscriberBucketCorbaClient(final Context ctx)
    {
        // TODO change the trap IDs
        super(ctx, CLIENT_NAME, SERVICE_DESCRIPTION, SERVICE_TYPE,
                SUBSCRIBER_BUCKET_PROV_SVC_DOWN, SUBSCRIBER_BUCKET_PROV_SVC_UP);
    }

    public short createBucket(String msisdn, int subscriptionType, long bundleId)
    {
        Parameter[] inParamSet = new Parameter[0];
        return createBucket(msisdn, subscriptionType, bundleId, inParamSet);
    }


    public short createBucket(String msisdn, int subscriptionType, long bundleId, Parameter[] inParamSet)
    {
        short retInt = -1;
        SubscriberBucketProvision service = getService();
        if (service == null)
        {
            new InfoLogMsg(this, "createBucket(): unable to get service", null).log(getContext());
            return retInt;
        }
        try
        {
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "createBucket(): Attempt to create bucket on bundle manager: msisdn["
                        + msisdn + "], bucketId[" + bundleId + "], inParamSet["
                        + BundleProfileSupportHelper.get(getContext()).paramSetToString(inParamSet) + "]", null).log(getContext());
            }
            BucketReturnParam result = service.createBucket(msisdn, bundleId, subscriptionType, inParamSet);
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "createBucket(): Result code = " + result.resultCode, null);
            }
            retInt = result.resultCode;
        }
        catch (org.omg.CORBA.COMM_FAILURE commFail)
        {
            logFailure("createBucket", msisdn, "", commFail);
            retInt = COMMUNICATION_FAILURE;
        }
        catch (Throwable t)
        {
            logFailure("createBucket", msisdn, "", t);
            retInt = COMMUNICATION_FAILURE;
        }
        return retInt;
    }


    public short[] createBuckets(String[] msisdns, int[] subscriptionTypes, long[] bundleIds)
    {
        Parameter[][] inParamSet = new Parameter[0][0];
        return createBuckets(msisdns, subscriptionTypes, bundleIds, inParamSet);
    }


    public short[] createBuckets(String[] msisdns, int[] subscriptionTypes, long[] bundleIds, Parameter[][] inParamSet)
    {
        short[] retInt;
        SubscriberBucketProvision service = getService();
        if (service == null)
        {
            new InfoLogMsg(this, "createBuckets(): unable to get service", null).log(getContext());
            return new short[msisdns.length];
        }
        try
        {
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "createBuckets(): Attempt to bulk create buckets on bundle manager: "
                        + "MSISDNS[" + Arrays.toString(msisdns) + "]"
                        + ", BundleIds[" + Arrays.toString(bundleIds) + "]"
                        + ", inParamSet[" + BundleProfileSupportHelper.get(getContext()).paramSetArrayToString(inParamSet) + "]",
                        null).log(getContext());
            }
            retInt = service.createBulkBuckets(msisdns, bundleIds, subscriptionTypes, inParamSet);
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "createBuckets(): Result code = " + Arrays.toString(retInt), null);
            }
        }
        catch (org.omg.CORBA.COMM_FAILURE commFail)
        {
            logFailure("createBuckets", Arrays.toString(msisdns), "", commFail);
            retInt = new short[msisdns.length];
        }
        catch (Throwable t)
        {
            logFailure("createBuckets", Arrays.toString(msisdns), "", t);
            retInt = new short[msisdns.length];
        }
        return retInt;
    }


    public SubscriberBucket getBucket(String msisdn, int subscriptionType, long bundleId) throws Throwable
    {
        Parameter[] inParamSet = new Parameter[0];
        return getBucket(msisdn, subscriptionType, bundleId, inParamSet);
    }


    public SubscriberBucket getBucket(String msisdn, int subscriptionType, long bundleId, Parameter[] inParamSet) throws Throwable
    {
        SubscriberBucketProvision service = getService();
        if (service == null)
        {
            new InfoLogMsg(this, "getBucket(): unable to get service", null).log(getContext());
            throw new Exception("Service Not Available!");
        }
        try
        {
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "getBucket(): Attempt to get bucket on bundle manager: MSISDN[" + msisdn + "]"
                        + ", BundleId[" + bundleId + "], inParamSet["
                        + BundleProfileSupportHelper.get(getContext()).paramSetToString(inParamSet) + "]", null).log(getContext());
            }
            BucketReturnParam result = service.getBucket(msisdn, bundleId, subscriptionType, inParamSet);
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "getBucket(): Result code = " + result.resultCode, null).log(getContext());
            }
            if (result.resultCode == 0)
            {
                return result.outSubscriberBucket;
            }
            else if (result.resultCode == com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.BUCKET_NOT_FOUND)
            {
                throw new BucketDoesNotExistsException(BMReturnCodeMsgMapping.getBucketMessage(result.resultCode) + " (Code=" + result.resultCode + ")");
            }
            else
            {
                throw new BundleManagerException(BMReturnCodeMsgMapping.getBucketMessage(result.resultCode) + " (Code=" + result.resultCode + ")");
            }
        }
        catch (Throwable commFail)
        {
            logFailure("getBucket", msisdn, "", commFail);
            throw commFail;
        }
    }


    public short updateBucketActive(String msisdn, int subscriptionType, StatusType status)
    {
        Parameter[] inParamSet = new Parameter[0];
        return updateBucketActive(msisdn, subscriptionType, status, inParamSet);
    }


    public short updateBucketActive(String msisdn, int subscriptionType, StatusType status, Parameter[] inParamSet)
    {
        short retInt = -1;
        SubscriberBucketProvision service = getService();
        if (service == null)
        {
            new InfoLogMsg(this, "updateBucketActive(): unable to get service", null).log(getContext());
            return retInt;
        }
        try
        {
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "updateBucketActive(): Attempt to update bucket status on bundle manager: "
                        + "MSISDN[" + msisdn + "], Status[" + status + "], inParamSet["
                        + BundleProfileSupportHelper.get(getContext()).paramSetToString(inParamSet) + "]", null).log(getContext());
            }
            BucketReturnParam result = service.updateBucketStatus(msisdn, status, subscriptionType, inParamSet);
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "updateBucketActive(): Result code = " + result.resultCode,
                        null).log(getContext());
            }
            retInt = result.resultCode;
        }
        catch (org.omg.CORBA.COMM_FAILURE commFail)
        {
            logFailure("updateBucketActive", msisdn, "", commFail);
            retInt = COMMUNICATION_FAILURE;
        }
        catch (Throwable t)
        {
            logFailure("updateBucketActive", msisdn, "", t);
            retInt = COMMUNICATION_FAILURE;
        }
        return retInt;
    }


    public short removeBucket(String msisdn, int subscriptionType, long bundleId)
    {
        Parameter[] inParamSet = new Parameter[0];
        return removeBucket(msisdn, subscriptionType, bundleId, inParamSet);
    }


    public short removeBucket(String msisdn, int subscriptionType, long bundleId, Parameter[] inParamSet)
    {
        short retInt = -1;
        SubscriberBucketProvision service = getService();
        if (service == null)
        {
            new InfoLogMsg(this, "removeBucket(): unable to get service", null).log(getContext());
            return retInt;
        }
        try
        {
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "removeBucket(): Attempt to remove bucket on bundle manager: MSISDN[" + msisdn
                        + "], BundleId[" + bundleId + "], inParamSet["
                        + BundleProfileSupportHelper.get(getContext()).paramSetToString(inParamSet) + "]", null).log(getContext());
            }
            BucketReturnParam result = service.removeBucket(msisdn, bundleId, subscriptionType, inParamSet);
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "removeBucket(): Result code = " + result.resultCode, null).log(getContext());
            }
            retInt = result.resultCode;
        }
        catch (org.omg.CORBA.COMM_FAILURE commFail)
        {
            logFailure("removeBucket", msisdn, "", commFail);
            retInt = COMMUNICATION_FAILURE;
        }
        catch (Throwable t)
        {
            logFailure("removeBucket", msisdn, "", t);
            retInt = COMMUNICATION_FAILURE;
        }
        return retInt;
    }


    public long removeSubscriberBucket(String msisdn, int spid, int subscriptionType, long bundleId)
    {
        // TODO return value is a mess !!!!
        long retOverusage = -1;
        SubscriberBucketProvision service = getService();
        if (service == null)
        {
            new InfoLogMsg(this, "removeSubscriberBucket(): unable to get service", null).log(getContext());
            return retOverusage;
        }
        try
        {
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "removeSubscriberBucket(): Attempt to remove bucket on bundle manager: "
                        + "MSISDN[" + msisdn + "], Spid[" + spid + "], BundleId[" + bundleId + "]",
                        null).log(getContext());
            }
            BucketServiceReturnParam result = service.removeSubscriberBucket(msisdn, spid, subscriptionType, bundleId);
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "removeSubscriberBucket(): Result code = " + result.resultCode,
                        null).log(getContext());
            }
            if (result.resultCode == 0)
            {
                if (result.overUsageMap.length>0)
                {
                    retOverusage = result.overUsageMap[0].overUsage;
                }
            }
            else
            {
                throw new BundleManagerException(BMReturnCodeMsgMapping.getBucketMessage(result.resultCode) + " (Code=" + result.resultCode + ")");
            }
        }
        catch (org.omg.CORBA.COMM_FAILURE commFail)
        {
            logFailure("removeSubscriberBucket", msisdn, "", commFail);
            retOverusage = COMMUNICATION_FAILURE;
        }
        catch (Throwable t)
        {
            logFailure("removeSubscriberBucket", msisdn, "", t);
            retOverusage = COMMUNICATION_FAILURE;
        }

        return retOverusage;
    }


    @SuppressWarnings("unchecked")
    public Map removeBuckets(String[] msisdns, int[] subscriptionTypes, long[] bundleIds) throws Throwable
    {
        Parameter[][] inParamSet = new Parameter[0][0];
        return removeBuckets(msisdns, subscriptionTypes, bundleIds, inParamSet);
    }


    @SuppressWarnings("unchecked")
    public Map removeBuckets(String[] msisdns, int[] subscriptionTypes, long[] bundleIds, Parameter[][] inParamSet) throws Throwable
    {
        Map retObj = new HashMap();
        SubscriberBucketProvision service = getService();
        if (service == null)
        {
            new InfoLogMsg(this, "removeBuckets(): unable to get service", null).log(getContext());
            throw new Exception("Service Not Available!");
        }
        try
        {
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "removeBuckets(): Attempt to bulk remove buckets on bundle manager: "
                        + "MSISDNs[" + Arrays.toString(msisdns) + "], BundleIds[" + Arrays.toString(bundleIds) + "]"
                        + ", inParamSet[" + BundleProfileSupportHelper.get(getContext()).paramSetArrayToString(inParamSet) + "]",
                        null).log(getContext());
            }
            short[] retInt = service.removeBulkBuckets(msisdns, bundleIds, subscriptionTypes, inParamSet);
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "removeBuckets(): Result code = " + Arrays.toString(retInt),
                        null).log(getContext());
            }

            // TODO: Corba interface needs to be updated to return over-usage amounts of the removed bucket
            // and be added to retObj of this function
        }
        catch (Throwable commFail)
        {
            logFailure("removeBuckets", Arrays.toString(msisdns), "", commFail);
            throw commFail;
        }
        return retObj;
    }


    public short increaseBalanceLimit(String msisdn, int spid, int subscriptionType, long bundleId, long amount)
    {
        Parameter[] inParamSet = new Parameter[0];
        Map<Short, Parameter> outParameterMap = new HashMap<Short, Parameter>();
        
        return increaseBalanceLimit(msisdn, spid, subscriptionType, bundleId, amount, inParamSet, outParameterMap);
    }


    public short increaseBalanceLimit(String msisdn, int spid, int subscriptionType, long bundleId, long amount, Parameter[] inParamSet, Map<Short, Parameter> outParameterMap)
    {
        short retInt = -1;
        SubscriberBucketProvision service = getService();
        if (service == null)
        {
            new InfoLogMsg(this, "increaseBalanceLimit(): unable to get service", null).log(getContext());
            return retInt;
        }
        try
        {
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "increaseBalanceLimit(): Attempt to increase balance limit on bundle manager: "
                        + "MSISDN[" + msisdn + "], Spid[" + spid +"], BundleId[" + bundleId +"], Amount[" + amount +"]"
                        + ", inParamSet[" + BundleProfileSupportHelper.get(getContext()).paramSetToString(inParamSet) +"]",
                        null).log(getContext());
            }
            BucketReturnParam result = service.increaseBalanceLimit(msisdn, spid, bundleId, subscriptionType, amount, inParamSet);
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "increaseBalanceLimit(): Result code = " + result.resultCode,
                        null).log(getContext());
            }
            retInt = result.resultCode;
            
            populateOutParamMap(result, outParameterMap);
        }
        catch (org.omg.CORBA.COMM_FAILURE commFail)
        {
            logFailure("increaseBalanceLimit", msisdn, "", commFail);
            retInt = COMMUNICATION_FAILURE;
        }
        catch (Throwable t)
        {
            logFailure("increaseBalanceLimit", msisdn, "", t);
            retInt = COMMUNICATION_FAILURE;
        }
        return retInt;
    }


    public short decreaseBalanceLimit(String msisdn, int spid, int subscriptionType, long bundleId, long amount)
    {
        Parameter[] inParamSet = new Parameter[0];
        Map<Short, Parameter> outParameterMap = new HashMap<Short, Parameter>();
        return decreaseBalanceLimit(msisdn, spid, subscriptionType, bundleId, amount, inParamSet, outParameterMap);
    }


    public short decreaseBalanceLimit(String msisdn, int spid, int subscriptionType, long bundleId, long amount, Parameter[] inParamSet, Map<Short, Parameter> outParameterMap)
    {
        short retInt = -1;
        SubscriberBucketProvision service = getService();
        if (service == null)
        {
            new InfoLogMsg(this, "decreaseBalanceLimit(): unable to get service", null).log(getContext());
            return retInt;
        }
        try
        {
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "decreaseBalanceLimit(): Attempt to decrease balance limit on bundle manager: "
                        + "MSISDN[" + msisdn +"], Spid[" + spid +"], BundleId[" + bundleId +"], Amount[" + amount +"]"
                        + ", inParamSet[" + BundleProfileSupportHelper.get(getContext()).paramSetToString(inParamSet) +"]",
                        null).log(getContext());
            }
            BucketReturnParam result = service.decreaseBalanceLimit(msisdn, spid, bundleId, subscriptionType, amount, inParamSet);
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "decreaseBalanceLimit(): Result code = " + result.resultCode,
                        null).log(getContext());
            }
            retInt = result.resultCode;
            
            populateOutParamMap(result, outParameterMap);
        }
        catch (org.omg.CORBA.COMM_FAILURE commFail)
        {
            logFailure("decreaseBalanceLimit", msisdn, "", commFail);
            retInt = COMMUNICATION_FAILURE;
        }
        catch (Throwable t)
        {
            logFailure("decreaseBalanceLimit", msisdn, "", t);
            retInt = COMMUNICATION_FAILURE;
        }
        return retInt;
    }

    public Collection<SubscriberBucketRetrieval> getSubscriberBalances(String msisdn, int subscriptionType) throws Throwable
    {
        Collection<SubscriberBucketRetrieval> retObj = null;
        SubscriberBucketProvision service = getService();
        if (service == null)
        {
            new InfoLogMsg(this, "getSubscriberBalances(): unable to get service", null).log(getContext());
            throw new Exception("Service Not Available!");
        }
        try
        {
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "getSubscriberBalances(): Attempt to get subscriber balance from bundle manager: "
                        + "MSISDN[" + msisdn +"]", null).log(getContext());
            }
            BucketRetrievalReturnParam result = service.getAllSubscriberBalances(msisdn, subscriptionType);
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "getSubscriberBalances(): Result code = " + result.resultCode,
                        null).log(getContext());
            }
            
            if (result.resultCode == 0)
            {
                if (result.bucketRetrievalCollection != null)
                {
                    retObj = Arrays.asList(result.bucketRetrievalCollection);
                }
            }
            else
            {
                throw new BundleManagerException(BMReturnCodeMsgMapping.getBucketMessage(result.resultCode) + " (Code=" + result.resultCode + ")");
            }
        }
        catch (Throwable commFail)
        {
            logFailure("getSubscriberBalances", msisdn, "", commFail);
            throw commFail;
        }
        return retObj;
    }

    
    public SubscriberBucketsAndCategorySummaryHolder getSubscriberBalancesWithSummary(String msisdn) throws Throwable
    {
        SubscriberBucketsAndCategorySummaryHolder ret = new SubscriberBucketsAndCategorySummaryHolder();
        SubscriberBucketProvision service = getService();
        if (service == null)
        {
            new InfoLogMsg(this, "getAllSubscriberBalancesWithSummary(): unable to get service", null).log(getContext());
            throw new Exception("Could not invoke getAllSubscriberBalancesWithSummary(); Service Not Available!");
        }
        
        try
        {
            if (LogSupport.isDebugEnabled(getContext()))
                LogSupport.debug(getContext(), this, MessageFormat.format(
                        "CORBA: method getAllSubscriberBalancesWithSummary(): Attempt to get subscriber balance for MSISDN: {0}",
                        new Object[]{msisdn}));
            
            BucketRetrievalWithSummaryReturnParamV2 result = service.getAllSubscriberBalancesWithSummaryV2(msisdn);
            
            if (LogSupport.isDebugEnabled(getContext()))
                LogSupport.debug(getContext(), this, MessageFormat.format(
                        "CORBA: method getAllSubscriberBalancesWithSummary(): ResultCode: {0}",
                        new Object[]{Integer.valueOf(result.resultCode)}));
            
            if (result.resultCode == 0)
            {
                if (result.bucketRetrievalCollection != null)
                {
                    ret.setBuckets(Arrays.asList(result.bucketRetrievalCollection));
                    ret.setCategorySummary(Arrays.asList(result.categorySummaryCollection));
                }
            }
            else
            {
                throw new BundleManagerException(BMReturnCodeMsgMapping.getBucketMessage(result.resultCode) + " (Code=" + result.resultCode + ")");
            }
        }
        catch (Throwable commFail)
        {
            logFailure("getAllSubscriberBalancesWithSummary", msisdn, "", commFail);
            throw commFail;
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public Map<Long, Long> switchBundles(String msisdn, int spid, int subscriptionType, long[] oldBundleIds, long[] newBundleIds, Parameter[][] inParamSet) throws Throwable
    {
        Map<Long, Long> retObj = null;
        SubscriberBucketProvision service = getService();
        
        if (service == null)
        {
            new InfoLogMsg(this, "switchBundles(): unable to get service", null).log(getContext());
            throw new Exception("Service not available");
        }
        try
        {            
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "switchBundles(): Attempt to switch bundles on bundle manager: "
                        + "MSISDN[" + msisdn + "], Spid[" + spid + "], SubscriptionType[" + subscriptionType + "]"
                        + ", OldBundleIds[" + Arrays.toString(oldBundleIds) + "]"
                        + ", NewBundleIds[" + Arrays.toString(newBundleIds) + "]"
                        + ", inParamSet[" + BundleProfileSupportHelper.get(getContext()).paramSetArrayToString(inParamSet) + "]",
                        null).log(getContext());
            }
            
            BucketServiceReturnParam result = service.switchBundles(msisdn, spid, subscriptionType, oldBundleIds, newBundleIds,
                    inParamSet);
            
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "switchBundles(): Result code = " + result.resultCode, null).log(getContext());
            }
            
            if (result.resultCode == 0)
            {
                OverUsage[] overUsage = result.overUsageMap;
                if (overUsage != null)
                {
                    retObj = new HashMap<Long, Long>();
                    for (int i = 0; i < overUsage.length; i += 1)
                    {
                        retObj.put(Long.valueOf(overUsage[i].bundleID), Long.valueOf(overUsage[i].overUsage));
                    }
                }
            }
            else
            {
                throw new BundleManagerException(BMReturnCodeMsgMapping.getBucketMessage(result.resultCode) + " (Code=" + result.resultCode + ")");
            }
        }
        catch (Throwable commFail)
        {
            logFailure("switchBundles2", msisdn, "", commFail);
            throw commFail;
        }
        return retObj;
    }


    public void changeBCD(String msisdn, int subscriptionType, long bundleId, int newBCD, boolean effectiveImmediately, Parameter[] inParamSet) throws Throwable
    {
        SubscriberBucketProvision service = getService();
        if (service == null)
        {
            new InfoLogMsg(this, "changeBCD(): unable to get service", null).log(getContext());
            throw new Exception("Service Not Available!");
        }
        try
        {
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "changeBCD(): Attempt to changeBCD on bundle manager: "
                        + "MSISDN[" + msisdn + "], BundleId[" + bundleId + "], NewBCD[" + newBCD + "]"
                        + ", EffectiveImmediately[" + effectiveImmediately + "]"
                        + ", inParamSet[" + BundleProfileSupportHelper.get(getContext()).paramSetToString(inParamSet) + "]",
                        null).log(getContext());
            }
            BucketReturnParam[] result = null;
            result = service.changeBCD(msisdn, bundleId, subscriptionType, newBCD, effectiveImmediately, inParamSet);
            if (LogSupport.isDebugEnabled(getContext()))
            {
                // this is an incomplete result code log
                new DebugLogMsg(this, "changeBCD(): Result code = " + result[0].resultCode, null).log(getContext());
            }
        }
        catch (Throwable commFail)
        {
            logFailure("changeBCD", msisdn, "", commFail);
            throw commFail;
        }
    }
    
    private void populateOutParamMap(BucketReturnParam result,
			Map<Short, Parameter> outParameterMap) 
    {
    	Parameter[] outputParams =  result.outParamSet;
		if(outParameterMap != null && outputParams != null)
		{
			for(Parameter param : outputParams)
			{
				if(param != null)
				{
					outParameterMap.put(param.parameterID, param);
				}
			}
		}
	}


    public String getServiceDescription()
    {
        return "CORBA client for Bundle Manager subscriber bucket services.";
    }
    
    private void logFailure(String operation, String msisdn ,String msg, Throwable t)
    {
        new MajorLogMsg(this, ": OPERATION [" + operation + "] failed for MSISDN [" + msisdn + "] : REASON [" + msg
                + "]", t).log(getContext());
    }

    protected SubscriberBucketProvision bucketService_;
}
