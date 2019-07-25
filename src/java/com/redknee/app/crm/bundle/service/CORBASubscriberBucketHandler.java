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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redknee.app.crm.bundle.BMReturnCodeMsgMapping;
import com.redknee.app.crm.bundle.Balance;
import com.redknee.app.crm.bundle.StatusEnum;
import com.redknee.app.crm.bundle.SubscriberBucket;
import com.redknee.app.crm.bundle.SubscriberBucketHome;
import com.redknee.app.crm.bundle.SubscriberBucketTransientHome;
import com.redknee.app.crm.bundle.SubscriberBucketsAndBalances;
import com.redknee.app.crm.bundle.UnitTypeEnum;
import com.redknee.app.crm.bundle.exception.BucketAlreadyExistsException;
import com.redknee.app.crm.bundle.exception.BucketDoesNotExistsException;
import com.redknee.app.crm.bundle.exception.BundleManagerException;
import com.redknee.app.crm.bundle.service.adapters.SubscriberBucketV4CrmSubscriberBucketAdapter;
import com.redknee.app.crm.bundle.service.adapters.SubscriberBucketV4ToBalanceAdapter;
import com.redknee.app.crm.bundle.service.adapters.SubscriberBucketsAndCategorySummaryAdapter;
import com.redknee.app.crm.client.BMSubscriberBucketCorbaClient;
import com.redknee.app.crm.client.SubscriberBucketsAndCategorySummaryHolder;
import com.redknee.framework.core.locale.Currency;
import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.support.IdentitySupport;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.product.bundle.manager.provision.v5_0.bucket.StatusType;
import com.redknee.product.bundle.manager.provision.v5_0.bucket.SubscriberBucketRetrieval;
import com.redknee.product.bundle.manager.provision.common.param.Parameter;
import com.redknee.product.bundle.manager.provision.common.param.ParameterID;
import com.redknee.product.bundle.manager.provision.common.param.ParameterValue;

/**
 * Handles all services for subscriber buckets in CRM from the CORBA perspective
 * @author @redknee.com
 *
 */
public class CORBASubscriberBucketHandler implements CRMSubscriberBucketProfile
{

    /**
     * {@inheritDoc}
     */
    public List getBalances(Context ctx, String msisdn, int subscriptionType)
            throws BucketDoesNotExistsException, BundleManagerException
    {
        List<Balance> balances = new ArrayList<Balance>();
        BMSubscriberBucketCorbaClient client = getBucketCorbaClient(ctx);
        if (client != null)
        {
            try
            {
                Collection<SubscriberBucketRetrieval> rawBalances = client.getSubscriberBalances(msisdn, subscriptionType);
                if (rawBalances != null)
                {
                    Iterator<SubscriberBucketRetrieval> iter = rawBalances.iterator();
                    while (iter.hasNext())
                    {
                        SubscriberBucketRetrieval bal = iter.next();
                        // Convert from BM balance object to CRM balance object
                        Balance balance = new SubscriberBucketV4ToBalanceAdapter(bal);
                        balances.add(balance);
                    }
                }
                else
                {
                    throw new BucketDoesNotExistsException("MSISDN " + msisdn);
                }
            }
            catch (Throwable t)
            {
                String msg = "CORBASubscriberBucketProfile:getBalances() failed. Msisdn = " + msisdn
                        + t.getMessage();
                LogSupport.major(ctx, this, msg);
                throw new BundleManagerException(msg, t);
            }
        }
        else
        {
            new InfoLogMsg(this,
                    "CORBASubscriberBucketProfile:getBalances() failed. ProductBMClient is null.", null).log(ctx);
        }
        return balances;
    }

    /**
     * {@inheritDoc}
     */
    public SubscriberBucket getBucketById(Context ctx, long bucketId)
            throws BucketDoesNotExistsException, BundleManagerException
    {
        SubscriberBucket retObj = null;
        BMSubscriberBucketCorbaClient client = getBucketCorbaClient(ctx);
        if (client != null)
        {
            try
            {
                // NOT SUPPORTED
            }
            catch (Throwable t)
            {
                String msg = "CORBASubscriberBucketProfile:getBucketById() failed. BucketId = " + bucketId
                        + t.getMessage();
                LogSupport.major(ctx, this, msg);
                throw new BundleManagerException(msg, t);
            }
        }
        else
        {
            new InfoLogMsg(this,
                    "CORBASubscriberBucketProfile:getBucketById() failed. ProductBMClient is null.", null).log(ctx);
        }
        return retObj;
    }

    
    /**
     * {@inheritDoc}
     */
    public Home getBuckets(Context ctx, String msisdn, int subscriptionType)
            throws BundleManagerException
    {
        Home buckets = new SubscriberBucketTransientHome(ctx);
        BMSubscriberBucketCorbaClient client = getBucketCorbaClient(ctx);
        if (client != null)
        {
            try
            {
                Collection rawBalances = client.getSubscriberBalances(msisdn, subscriptionType);
                if (rawBalances != null)
                {
                    Iterator iter = rawBalances.iterator();
                    while (iter.hasNext())
                    {
                        SubscriberBucketRetrieval bal = (SubscriberBucketRetrieval) iter.next();
    
                        // Convert from BM bucket object to CRM bucket object
                        SubscriberBucket bucket = new SubscriberBucketV4CrmSubscriberBucketAdapter(bal);
                        bucket.setSubscriptionType(subscriptionType);
                        
                        buckets.create(ctx,bucket);
                    }
                }
            }
            catch (Throwable t)
            {
                String msg = "Unable to get subscriber bundles (buckets) on BM for MSISDN '" + msisdn + "': "
                        + t.getMessage();
                LogSupport.major(ctx, this, msg);
                throw new BundleManagerException(msg, t);
            }
        }
        else
        {
            new InfoLogMsg(this,
                    "CORBASubscriberBucketProfile:getBuckets() failed. ProductBMClient is null.", null).log(ctx);
        }
        
        return buckets;
    }


    /**
     * {@inheritDoc}
     */
    public void createBucket(Context ctx, String msisdn, int subscriptionType, long bundleId)
            throws BucketAlreadyExistsException, BundleManagerException
    {
        short retInt = -1;
        BMSubscriberBucketCorbaClient client = getBucketCorbaClient(ctx);
        if (client != null)
        {
            try
            {
                SubscriberBucket bucket = null;
                try
                {
                     bucket = getBucket(ctx, msisdn, subscriptionType, bundleId);
                }
                catch (BucketDoesNotExistsException e)
                {                    
                }
                
                if (bucket == null)
                {
                    retInt = client.createBucket(msisdn, subscriptionType, bundleId);
                    if (retInt != 0)
                    {
                        String msg = "CORBASubscriberBucketProfile:createBucket() failed: Result code = " + retInt + ". Msisdn = " + msisdn;
                        LogSupport.major(ctx, this, msg);
                        throw new BundleManagerException(BMReturnCodeMsgMapping.getBucketMessage(retInt) + " (Code=" + retInt + ").", null);
                    }
                }
                else
                {
                    String msg = "Bundle (bucket) " + bundleId + " for subscription with MSISDN='" + msisdn + "' already exists on BM";
                    throw new BucketAlreadyExistsException(msg);
                }
            }
            catch (BundleManagerException bme)
            {
                throw bme;
            }
            catch (Throwable t)
            {
                String msg = "CORBASubscriberBucketProfile:createBucket() failed: Result code = " + retInt + ". Msisdn = " + msisdn
                        + t.getMessage();
                LogSupport.major(ctx, this, msg);
                throw new BundleManagerException("Error creating bucket for bundle " + bundleId + " : "
                        + t.getMessage(), t);
            }
        }
        else
        {
            new InfoLogMsg(this,
                    "CORBASubscriberBucketProfile:createBucket() failed. ProductBMClient is null.", null).log(ctx);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void createBuckets(Context ctx, Collection subs, Collection buckets)
            throws BucketAlreadyExistsException, BundleManagerException
    {
        BMSubscriberBucketCorbaClient client = getBucketCorbaClient(ctx);
        if (client != null)
        {
            try
            {
                String[] msisdns = new String[subs.size()];
                long[] bundleIds = new long[buckets.size()];
                int[] subscriptionTypes = new int[buckets.size()];

                Iterator iter = subs.iterator();
                int index = 0;
                while(iter.hasNext())
                {
                    String msisdn= (String) iter.next();
                    msisdns[index] = msisdn;
                    index += 1;
                }

                iter = buckets.iterator();
                index = 0;
                while (iter.hasNext())
                {
                    SubscriberBucket bucket = (SubscriberBucket) iter.next();
                    bundleIds[index] = bucket.getBundleId();
                    subscriptionTypes[index] = bucket.getSubscriptionType();
                    index += 1;
                }

                short result[] = client.createBuckets(msisdns, subscriptionTypes, bundleIds);
            }
            catch (Throwable t)
            {
                String msg = "CORBASubscriberBucketProfile:createBuckets() failed: "
                        + t.getMessage();
                LogSupport.major(ctx, this, msg);
                throw new BundleManagerException(msg, null);
            }
        }
        else
        {
            new InfoLogMsg(this,
                    "CORBASubscriberBucketProfile:createBuckets() failed. ProductBMClient is null.", null).log(ctx);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void createBucket(Context ctx, SubscriberBucket bucket)
            throws BucketAlreadyExistsException, BundleManagerException
    {
        Home bucketHome = getSubscriberBucketHome(ctx);
        try
        {
            SubscriberBucket subBucket = null;
            try
            {
                subBucket = getBucket(ctx, bucket.getMsisdn(), bucket.getSubscriptionType(), bucket.getBundleId());
            }
            catch(BucketDoesNotExistsException e)
            {               
            }
            if (subBucket == null)
            {
                bucketHome.create(ctx, bucket);
            }
            else
            {
                throw new BucketAlreadyExistsException("Bundle (bucket) " + bucket.getBundleId() + " is already provisioned on BM for subscription with MSISDN='" + bucket.getMsisdn()+"'");
            }
        }
        catch (HomeException e)
        {
            String msg = "Unable to provision bundle (bucket) '" + bucket.getBundleId() + "' on BM to subscription with MSISDN='" + bucket.getMsisdn()+"': " + e.getMessage();
            LogSupport.major(ctx, this, msg);
            throw new BundleManagerException(msg, e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public int decreaseBalanceLimit(Context ctx, String msisdn, int spid,
            int subscriptionType, long bundleId, long amount) throws BundleManagerException
         {
    	Parameter[] inParamSet = new Parameter[0];
    	Map<Short, Parameter> outParamMap = new HashMap<Short, Parameter>();
    	return 	decreaseBalanceLimit(ctx, msisdn, spid, subscriptionType, bundleId, amount,inParamSet, outParamMap);

         }

    /**
     * {@inheritDoc}
     */
    public int decreaseBalanceLimit(Context ctx, String msisdn, int spid,
            int subscriptionType, long bundleId, long amount, Parameter[] inParamSet, Map<Short, Parameter> outParameterMap) 
            		throws BundleManagerException
    {
        int retInt = -1;
        BMSubscriberBucketCorbaClient client = getBucketCorbaClient(ctx);
        if (client != null)
        {
            try
            {
                retInt = client.decreaseBalanceLimit(msisdn, spid, subscriptionType, bundleId, amount, inParamSet, outParameterMap);
                if (retInt != 0)
                {
                    String msg = "CORBASubscriberBucketProfile:decreaseBalanceLimit() failed: Result code = " + retInt 
                            + ". Msisdn=" + msisdn
                            + "Spid=" + spid
                            + "BundleId=" + bundleId
                            + "amount=" + amount;
                    LogSupport.major(ctx, this, msg);
                    Currency defaultCurrency = (Currency) ctx.get(Currency.class, Currency.DEFAULT);
                    
                    throw new BundleManagerException("Unable to decrease bundle " + bundleId + " for the amount of "
                        + defaultCurrency.formatValue(amount) + " for subscription with MSISDN='" + msisdn + "' on BM: " + BMReturnCodeMsgMapping.getBucketMessage(retInt) + " (Code=" + retInt + ").", null,retInt);
                }
                
            }
            catch (BundleManagerException bme)
            {
                // Already logged.  Rethrow for GUI.
                throw bme;
            }
            catch (Throwable t)
            {
                String msg = "CORBASubscriberBucketProfile:decreaseBalanceLimit() failed: Result code = " + retInt 
                        + ". Msisdn=" + msisdn
                        + "Spid=" + spid
                        + "BundleId=" + bundleId
                        + "amount=" + amount
                        + ": " + t.getMessage();
                LogSupport.major(ctx, this, msg);
                Currency defaultCurrency = (Currency) ctx.get(Currency.class, Currency.DEFAULT);

                throw new BundleManagerException("Unable to decrease bundle " + bundleId + " for the amount of "
                        + defaultCurrency.formatValue(amount) + " for subscription with MSISDN='" + msisdn + "' on BM: " + BMReturnCodeMsgMapping.getBucketMessage(retInt) + " (Code=" + retInt + ").", null);
            }
        }
        else
        {
            new InfoLogMsg(this,
                    "CORBASubscriberBucketProfile:decreaseBalanceLimit() failed. ProductBMClient is null.", null).log(ctx);
        }
        
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public SubscriberBucket getBucket(Context ctx, String msisdn, int subscriptionType, long bundleId)
            throws BucketDoesNotExistsException, BundleManagerException
    {
        SubscriberBucket retObj = null;
        BMSubscriberBucketCorbaClient client = getBucketCorbaClient(ctx);
        if (client != null)
        {
            com.redknee.product.bundle.manager.provision.v5_0.bucket.SubscriberBucket bucket = null;
            try
            {
                bucket = client.getBucket(msisdn, subscriptionType, bundleId);
            }
            catch (BucketDoesNotExistsException e)
            {
                // Rethrow BucketDoesNotExistsException.  Bundle creation needs to ignore these exceptions.
                throw e;
            }
            catch (Throwable t)
            {
                String msg = "Unable to retrieve bundle (bucket) " + bundleId + " for subscription with MSISDN='" + msisdn + "' from BM: " + t.getMessage();
                LogSupport.major(ctx, this, msg);
                throw new BundleManagerException(msg, t);     
            }
            if (bucket == null)
            {
                throw new BucketDoesNotExistsException("MSISDN=" + msisdn + " BundleID=" + bundleId);
            }
            else
            {
                /*
                 * TODO: use adapter: com.redknee.app.crm.bundle.service.adapters.SubscriberBucketV4CrmSubscriberBucketAdapter 
                 */
                
                Balance balance = new Balance();                
                balance.setPersonalLimit(bucket.balanceLimit);
                balance.setPersonalUsed(bucket.balanceUsed);
                balance.setRolloverLimit(bucket.rolloverBalanceLimit);
                balance.setRolloverUsed(bucket.rolloverBalanceUsed);

                retObj = new SubscriberBucket();                
                retObj.setMsisdn(bucket.msisdn);
                retObj.setStatus(StatusEnum.get((short)bucket.status.value()));
                retObj.setSubscriptionType(subscriptionType);
                retObj.setBucketId(bucket.bucketId);
                retObj.setSpid(bucket.spid);
                retObj.setBundleId(bucket.bundleId);
                retObj.setProvisionTime(bucket.provisionTime);
                retObj.setActivationTime(bucket.activationTime);
                retObj.setExpiryTime(bucket.expiryTime);
                retObj.setRegularBal(balance);
                
                // retObj.setChargingPriority(bucket.chargingPriority);                
            }
        }
        else
        {
            new InfoLogMsg(this,
                    "CORBASubscriberBucketProfile:getBucket() failed. ProductBMClient is null.", null).log(ctx);
        }        
        return retObj;
    }
    
	@Override
	public int increaseBalanceLimit(Context ctx, String msisdn, int spid,
			int subscriptionType, long bundleId, long amount)
			throws BundleManagerException {
		Parameter[] inParamSet = new Parameter[0];
		Map<Short, Parameter> outParamMap = new HashMap<Short, Parameter>();
		return increaseBalanceLimit(ctx, msisdn, spid, subscriptionType, bundleId, amount,inParamSet, outParamMap);
		
	}

    /**
     * {@inheritDoc}
     */
    public int increaseBalanceLimit(Context ctx, String msisdn, int spid,
            int subscriptionType, long bundleId, long amount, Parameter[] inParamSet, Map<Short, Parameter> outParameterMap) 
            		throws BundleManagerException
    {
        int retInt = -1;
        BMSubscriberBucketCorbaClient client = getBucketCorbaClient(ctx);
        if (client != null)
        {
            try
            {
                retInt = client.increaseBalanceLimit(msisdn, spid, subscriptionType, bundleId, amount, inParamSet, outParameterMap);
                if (retInt != 0)
                {
                     
                    String msg = "CORBASubscriberBucketProfile:increaseBalanceLimit() failed: Result code = " + retInt 
                                + ". Msisdn=" + msisdn
                                + ", Spid=" + spid
                                + ", BundleId=" + bundleId
                                + ", Amount=" + amount;
                    LogSupport.major(ctx, this, msg);
                    
                    Currency defaultCurrency = (Currency) ctx.get(Currency.class, Currency.DEFAULT);
                    
                    throw new BundleManagerException("Unable to increase bundle " + bundleId + " for the amount of "
                        + defaultCurrency.formatValue(amount) + " for subscription with MSISDN='" + msisdn + "' on BM: "+ BMReturnCodeMsgMapping.getBucketMessage(retInt) + " (Code=" + retInt + ").", null, retInt);
                }
            }
            catch (BundleManagerException bme)
            {
                // Already logged.  Rethrow for GUI.
                throw bme;
            }
            catch (Throwable t)
            {
                String msg = "CORBASubscriberBucketProfile:increaseBalanceLimit() failed: Result code = " + retInt 
                            + ". Msisdn=" + msisdn
                            + ", Spid=" + spid
                            + ", BundleId=" + bundleId
                            + ", Amount=" + amount
                            + ": " + t.getMessage();
                LogSupport.major(ctx, this, msg);
                
                Currency defaultCurrency = (Currency) ctx.get(Currency.class, Currency.DEFAULT);
                
                throw new BundleManagerException("Unable to increase bundle " + bundleId + " for the amount of "
                        + defaultCurrency.formatValue(amount) + " for subscription with MSISDN='" + msisdn + "' on BM: "+ BMReturnCodeMsgMapping.getBucketMessage(retInt) + " (Code=" + retInt + ").", null);
            }
        }
        else
        {
            new InfoLogMsg(this,
                    "CORBASubscriberBucketProfile:increaseBalanceLimit() failed. ProductBMClient is null.", null).log(ctx);
        }
        return retInt;
    }

    /**
     * {@inheritDoc}
     */
    public long removeBucket(Context ctx, int spid, String msisdn, int subscriptionType, long bundleId)
            throws BucketDoesNotExistsException, BundleManagerException
    {
        long retInt = 0;
        BMSubscriberBucketCorbaClient client = getBucketCorbaClient(ctx);
        if (client != null)
        {
            // Check that the bucket exists
            getBucket(ctx, msisdn, subscriptionType, bundleId);

            try
            {
                retInt = client.removeSubscriberBucket(msisdn, spid, subscriptionType, bundleId);
            }
            catch (Throwable t)
            {
                String msg = "Unable to unprovision subscriber bundle (bucket) '" + bundleId + "' on BM from subscription with MSISDN='" + msisdn + "': " + t.getMessage();
                LogSupport.major(ctx, this, msg);
                throw new BundleManagerException(msg, t);
            }
        }
        else
        {
            new InfoLogMsg(this,
                    "CORBASubscriberBucketProfile:removeBucket() failed. ProductBMClient is null.", null).log(ctx);
        }
        return retInt;
    }

    /**
     * {@inheritDoc}
     */
    public Map removeBuckets(Context ctx, Collection msisdnList,
            Collection bundleList) throws BundleManagerException
    {
        Map retObj = new HashMap();
        BMSubscriberBucketCorbaClient client = getBucketCorbaClient(ctx);
        if (client != null)
        {
            try
            {
                String[] msisdns = new String[msisdnList.size()];
                long[] bundleIds = new long[bundleList.size()];
                int[] subscriptionTypes = new int[bundleList.size()];

                Iterator iter = msisdnList.iterator();
                int index = 0;
                while(iter.hasNext())
                {
                    // We expect this to be a bean of type Subscriber, but since
                    // Subscriber has not been moved to AppCrmCore as of time that
                    // this class was moved, must awkwardly get the identifier out
                    // of the bean generically.
                    Object bean = iter.next();
                    if (bean instanceof Identifiable)
                    {
                        Identifiable idAware = (Identifiable) bean;
                        Object id = idAware.ID();
                        
                        IdentitySupport idSupport = (IdentitySupport) XBeans.getInstanceOf(ctx, bean.getClass(), IdentitySupport.class);
                        if (idSupport != null)
                        {
                            msisdns[index] = idSupport.toStringID(id);
                        }
                        else
                        {
                            msisdns[index] = String.valueOf(id);
                        }
                    }
                    index += 1;
                }

                iter = bundleList.iterator();
                index = 0;
                while (iter.hasNext())
                {
                    SubscriberBucket bucket = (SubscriberBucket) iter.next();
                    bundleIds[index] = bucket.getBundleId();
                    subscriptionTypes[index] = bucket.getSubscriptionType();
                    index += 1;
                }

                retObj = client.removeBuckets(msisdns, subscriptionTypes, bundleIds);
            }
            catch (Throwable t)
            {
                String msg = "CORBASubscriberBucketProfile:remoteBuckets() failed."
                            + t.getMessage();
                throw new BundleManagerException("Error removing  bundles: " + t.getMessage(), t);
            }
        }
        else
        {
            new InfoLogMsg(this,
                    "CORBASubscriberBucketProfile:removeBuckets() failed. ProductBMClient is null.", null).log(ctx);
        }
        return retObj;
    }

    /**
     * {@inheritDoc}
     */
    public void updateBucket(Context ctx, String msisdn, int spid,
            int subscriptionType, long bundleId, boolean active, boolean prorate)
            throws BucketDoesNotExistsException, BundleManagerException
    {
        updateBucketStatus(ctx, msisdn, spid, subscriptionType, bundleId, active, prorate);
    }

    /**
     * {@inheritDoc}
     */
    public void updateBucketStatus(Context ctx, String msisdn, int spid,
            int subscriptionType, long bundleId, boolean status, boolean prorated)
            throws BucketDoesNotExistsException, BundleManagerException
    {
        int retInt = -1;
        BMSubscriberBucketCorbaClient client = getBucketCorbaClient(ctx);
        if (client != null)
        {
            getBucket(ctx, msisdn, subscriptionType, bundleId);
            
            try
            {
                Parameter[] inParamSet = new Parameter[3];

                Long runningDate = (Long) ctx.get(CURRENT_DATE);
                if(runningDate != null)
                {
                	inParamSet = new Parameter[4];
                	ParameterValue paramValue = new ParameterValue();
                    paramValue.longValue(runningDate);
                    inParamSet[3] = new Parameter(ParameterID.OVERRIDE_LR_TIME, paramValue);
                }
                	
                ParameterValue paramValue = new ParameterValue();
                paramValue.intValue(spid);
                inParamSet[0] = new Parameter(ParameterID.SPID, paramValue);
                
                ParameterValue paramValue2 = new ParameterValue();
                paramValue2.longValue(bundleId);
                inParamSet[1] = new Parameter(ParameterID.IN_BUNDLE_ID, paramValue2);
                
                ParameterValue paramValue3 = new ParameterValue();
                paramValue3.booleanValue(prorated);
                inParamSet[2] = new Parameter(ParameterID.IN_PRORATE_LIMIT, paramValue3);

                StatusType bucketStatus = StatusType.from_int(status ? StatusType._ACTIVE : StatusType._SUSPENDED);
                retInt = client.updateBucketActive(msisdn, subscriptionType, bucketStatus, inParamSet);
                if (retInt != 0)
                {
                    String msg = "CORBASubscriberBucketProfile:updateBucketStatus() failed: Result code = " + retInt + ".";
                    LogSupport.major(ctx, this, msg);

                    throw new BundleManagerException("Error updating bundle " + bundleId + ": " + BMReturnCodeMsgMapping.getBucketMessage(retInt) + " (Code=" + retInt + ").", null);
                }
            }
            catch (BundleManagerException bme)
            {
                // Already logged.  Rethrow for GUI.
                throw bme;
            }
            catch (Throwable t)
            { 
                String msg = "CORBASubscriberBucketProfile:updateBucketStatus() failed: Result code = " + retInt + "."
                            + t.getMessage();
                LogSupport.major(ctx, this, msg);
                throw new BundleManagerException("Error updating bundle " + bundleId + ": " + t.getMessage(), t);
            }
        }
        else
        {
            new InfoLogMsg(this,
                    "CORBASubscriberBucketProfile:updateBucketStatus() failed. ProductBMClient is null.", null).log(ctx);
        }
    }

    private Home getSubscriberBucketHome(Context ctx)
    {
        return (Home) ctx.get(SubscriberBucketHome.class);
    }
    
    private BMSubscriberBucketCorbaClient getBucketCorbaClient(Context ctx)
    {
        return (BMSubscriberBucketCorbaClient) ctx.get(BMSubscriberBucketCorbaClient.class);
    }

    @Override
    public SubscriberBucketsAndBalances getBucketsWithCategorySummary(
            Context ctx, String msisdn) throws BundleManagerException
    {
        SubscriberBucketsAndBalances ret = new SubscriberBucketsAndBalances();
        BMSubscriberBucketCorbaClient client = getBucketCorbaClient(ctx);
        if (client != null)
        {
            try
            {
                SubscriberBucketsAndCategorySummaryHolder bucketsWithSummary = client.getSubscriberBalancesWithSummary(msisdn);
                return new SubscriberBucketsAndCategorySummaryAdapter(bucketsWithSummary);
            }
            catch (Throwable t)
            {
                String msg = MessageFormat.format(
                        "Unable to get subscriber bundles (with Balances Summary) on BM for MSISDN: {0}",
                        new Object[]{msisdn});
                LogSupport.major(ctx, this, msg, t);
                throw new BundleManagerException(msg, t);
            }
        }
        else
        {
            new InfoLogMsg(this,
                    "CORBASubscriberBucketProfile:getBucketsWithCategorySummary() failed. ProductBMClient (service) is down.", null).log(ctx);
        }

        return ret;
    }
    
    public static final String CURRENT_DATE = "CURRENT_DATE";
}

