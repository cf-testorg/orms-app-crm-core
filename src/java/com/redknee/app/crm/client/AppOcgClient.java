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

package com.redknee.app.crm.client;

import static com.redknee.app.crm.client.CorbaClientTrapIdDef.OCG_PROV_SVC_DOWN;
import static com.redknee.app.crm.client.CorbaClientTrapIdDef.OCG_PROV_SVC_UP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.IntHolder;
import org.omg.CORBA.LongHolder;

import com.redknee.app.crm.account.BANAware;
import com.redknee.app.crm.bean.SubscriberTypeAware;
import com.redknee.app.crm.bean.SubscriberTypeEnum;
import com.redknee.app.crm.support.BeanLoaderSupport;
import com.redknee.app.crm.support.BeanLoaderSupportHelper;
import com.redknee.framework.core.locale.Currency;
import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;
import com.redknee.framework.xlog.log.PMLogMsg;
import com.redknee.product.s2100.ErrorCode;
import com.redknee.product.s2100.oasis.param.Parameter;
import com.redknee.product.s2100.oasis.param.ParameterID;
import com.redknee.product.s2100.oasis.param.ParameterSetHolder;
import com.redknee.product.s2100.oasis.param.ParameterValue;
import com.redknee.product.s2100.oasis.v90.PrepaidAccountService;
import com.redknee.util.partitioning.xhome.support.MsisdnAware;


/**
 * Support clustered CORBA client
 * Refactored reusable elements to an abstract class AbstractCrmClient<T>
 * 
 * @author ray.chen@redknee.com
 * @since June 25, 2009 
 */
public class AppOcgClient extends AbstractCrmClient<PrepaidAccountService>
{
    private static final String SERVICE_NAME = "AppOcgClient";
    private static final String SERVICE_DESCRIPTION = "CORBA client for OCG services (Proxy:PrepaidAccountService)";

    private static final String PM_MODULE = AppOcgClient.class.getName();
    
    public static final short AVAILABLE = 0 ;
    public static final short ACTIVE = 1 ;


    public AppOcgClient(final Context ctx)
    {
        super(ctx, SERVICE_NAME, SERVICE_DESCRIPTION, PrepaidAccountService.class, OCG_PROV_SVC_DOWN, OCG_PROV_SVC_UP);
    }
    
    /**
     * A static method for updating ABM expiryDate for the given subscriber.
     *
     * @param context The operating context.
     * @param subscriber The given subscriber.
     * @param expiryDateExtension The number of days to extend.
     * @param walletId IN the subscription type to update the expiry on.
     *                    This field is the BMID in the <code>SubscriptionType</code>.
     * @param newExpDate The subscribers new expiry date
     */
    public <T extends BANAware & MsisdnAware & SubscriberTypeAware> void updateExpiryDateForSubscriber(
        final Context context,
        final T subscriber,
        final short expiryDateExtension,
        final long walletId,
        StringBuilder newExpDate)
        throws HomeException, AppOcgClientException
    {
        final AppOcgClient ocgClient =
            (AppOcgClient) context.get(AppOcgClient.class);
        if (ocgClient == null)
        {
            throw new HomeException(
                "System Error: AppOcgClient not found in context");
        }
        
        String erReference="AppCrm-"+subscriber.getMsisdn();
        
        String currencyCode = getCurrencyCode(context, subscriber);

        /*
         * Need to think about this some more, if we should update the expiry
         * date if the extension day is 0.  It will be 0 for postpaid->prepaid conversion
         * or from the GUI, the extension day is 0.  Likely, it should not
         * update if extension is 0.
         */
        
        boolean updExp = true;
        
        final short result = (short) ocgClient.extendExpiryDate(
        		subscriber.getMsisdn(),
                subscriber.getSubscriberType(),
                currencyCode,
                updExp,
                expiryDateExtension,
                erReference,
                walletId,
                newExpDate);

        if (result != 0)
        {
            throw new AppOcgClientException(
                "Failed to update expiry date due to OCG error",
                result);
        }
    }


    /***
     * This method is used to request Balance and Activate Subscriber 
     * @param msisdn
     * @param subType
     * @param currencyType
     * @param sendExpiry
     * @param activationFlag = false for requesting balance and true to Activate Subscriber
     * @param erReference
     * @param walletID IN this determines which wallet to retrieve the balance from.
     *                    This field can be found in the <code>SubscriptionType</code> as the BMID.
     * @param outputBalance
     * @param outputParams
     * @return
     */
    public int requestBalance2(final String msisdn, final SubscriberTypeEnum subType, final String currencyType,
            final boolean sendExpiry, final boolean activationFlag, final String erReference, final long walletID,
            final LongHolder outputBalance, final LongHolder outputOverdraftBalance,
            final LongHolder outputOverdraftDate, final ParameterSetHolder outputParams)
    {
		final PMLogMsg pmLogMsg = new PMLogMsg(PM_MODULE, "requestBalance()");
		
		int result = -1;
        final PrepaidAccountService service = getService();
		
		if (service != null)
		{
			//	 initialize inputParamSet and outputParamSet
            final Parameter[] inputParams = getParameterSet2(subType, activationFlag, walletID);
			//ParameterSetHolder outputParams = new ParameterSetHolder();
			
            final org.omg.CORBA.StringHolder expiry = new org.omg.CORBA.StringHolder();
			
			try 
			{
                result = service.requestBalance(msisdn, currencyType, sendExpiry, inputParams, erReference, expiry,
                        outputBalance, outputParams);
				new InfoLogMsg(this, "balance is " + outputBalance.value, null).log(getContext());
				
                ParameterValue parameterValue = getParameterValue(ParameterID.OD_ACCOUNT_BALANCE, outputParams);
                if( parameterValue != null )
                {
                    outputOverdraftBalance.value = parameterValue.longValue();
                    new InfoLogMsg(this, "overdraft balance is " + outputOverdraftBalance.value, null)
                            .log(getContext());
                }

                parameterValue = getParameterValue(ParameterID.OD_DATE, outputParams);
                if( parameterValue != null )
                {
                    outputOverdraftDate.value = parameterValue.longValue();
                    new InfoLogMsg(this, "overdraft date is " + outputOverdraftDate.value, null).log(getContext());   
                }
			}
			catch (final org.omg.CORBA.COMM_FAILURE commFail)
			{
                result = ErrorCode.SERVICE_NOT_AVAILABLE;
			}
			catch (final Exception e)
			{
				new MinorLogMsg(this, "Fail to request balance: " + e, e).log(getContext());
                result = ErrorCode.SERVICE_NOT_AVAILABLE;   //DZ: added to be consistent
			}
	}
	
	pmLogMsg.log(getContext());
	
	return result;
	}
    
    /**
     * This method is used to set parameter flag for Activation Flag
     * 
     * @param subType
     * @param activationFlag
     * @param walletId
     * @return
     */
    
    private Parameter[] getParameterSet2(final SubscriberTypeEnum subType, final boolean activationFlag,
            final long walletId)
	{
        // DZ passing subtype via parameter set
		if(subType==null)
        {
            return new Parameter[0];
        }
		//fix for TT 9050600006
        final Parameter[] inputParams = new Parameter[3];
		inputParams[0] = new Parameter();
		ParameterValue pv = new ParameterValue();
		pv.intValue(subType.getIndex());

		inputParams[0].value = pv;
		inputParams[0].parameterID = ParameterID.ACCOUNT_TYPE;

		pv = new ParameterValue();
		pv.booleanValue(activationFlag);

		inputParams[1] = new Parameter();
		inputParams[1].value = pv;
		inputParams[1].parameterID = ParameterID.ACTIVATE_FLAG;
		
        pv = new ParameterValue();
        pv.intValue((int)walletId);

        inputParams[2] = new Parameter();
        inputParams[2].value = pv;
        inputParams[2].parameterID = ParameterID.SUBSCRIPTION_TYPE;

        return inputParams;
	}
    
    /**
     * Credits profile on OCG
     * 
     * @param msisdn
     * @param subType
     * @param amount
     * @param currencyType
     * @param updExp
     * @param extension
     * @param erReference
     * @param walletId IN this determines which wallet to credit. This field is the BMID
     *            in the <code>SubscriptionType</code>.
     * @param newExpDate The new expiry date if the expiry date was updated
     * @param balance OUT balance + credit limit from the OCG profile.
     * @return Result code
     */
    public int requestCredit (String msisdn,
            final SubscriberTypeEnum subType,
            final long amount,
            final String currencyType,
            boolean updExp,
            final short extension,
            final String erReference,
            final long walletId,
            final StringBuilder newExpDate,
            final LongHolder balance)
    {
    	return requestCredit(msisdn, subType, amount, currencyType, updExp, extension,erReference, walletId, newExpDate, balance, null, null);
    	
    }
    
    /**
     * 
     * Checks if the incoming Map has OCG ParameterID Subscription Type
     * already present.
     * 
     * @param inParamMap
     * @return
     */
    private boolean isSubscriptionTypeAlreadyPresent(Map<Short,Parameter> inParamMap)
    {
    	if(inParamMap == null)
    	{
    		return false;
    	}
    	else
    	{
    		return inParamMap.get(ParameterID.SUBSCRIPTION_TYPE) != null;
    	}
    }
    
    /**
     * Credits profile on OCG
     * 
     * @param msisdn
     * @param subType
     * @param amount
     * @param currencyType
     * @param updExp
     * @param extension
     * @param erReference
     * @param walletId IN this determines which wallet to credit. This field is the BMID
     *            in the <code>SubscriptionType</code>.
     * @param newExpDate The new expiry date if the expiry date was updated
     * @param balance OUT balance + credit limit from the OCG profile.
     * @param ocgInputParamMap<Short, Parameter> Generic input Param map to be sent to OCG
     * @param ocgOutputParamMap<Short, Parameter> Generic output Param map from OCG
     * @return Result code
     */
    public int requestCredit (String msisdn,
            final SubscriberTypeEnum subType,
            final long amount,
            final String currencyType,
            boolean updExp,
            final short extension,
            final String erReference,
            final long walletId,
            final StringBuilder newExpDate,
            final LongHolder balance,
            final Map<Short, Parameter> ocgInputParamMap,
            final Map<Short, Parameter> ocgOutputParamMap )
    {
        final PMLogMsg pmLogMsg = new PMLogMsg(PM_MODULE, "requestCredit()");
        
        int result = -1;
        
        final PrepaidAccountService service = getService();
        final Parameter[] inputParams = getParameterSet(subType, true, walletId, 
        		isSubscriptionTypeAlreadyPresent(ocgInputParamMap));
        
        final Parameter[] inputParamsFromContext = getParameterArrayFromMap(ocgInputParamMap);
        final Parameter[] inputParamsForOcg = mergeArraysSafe(inputParams,inputParamsFromContext);
        
        final ParameterSetHolder outputParams = new ParameterSetHolder();

        if (service != null)
        {
            try 
            {
                // no need to extend
                if(extension==0)
                {
                    updExp=false;
                }
                
                result = service.requestCredit(msisdn, amount, currencyType, updExp, extension, inputParamsForOcg,
                        erReference, balance, outputParams);
                
                populateOcgOutputParameterMap(ocgOutputParamMap, outputParams);
                
                // If the caller needs the new expiry date, set it.
                if (newExpDate != null)
                {
                    // Look for the new expiry date in the output parameters
                    final ParameterValue parameterValue = getParameterValue(ParameterID.NEW_EXPIRY,
                            outputParams);
                    if( parameterValue != null )
                    {
                        newExpDate.append(parameterValue.stringValue());
                    }
                }

                // Calculating clawback to show correct balance for credit operation.
                final ParameterValue amountCreditparameterValue = getParameterValue(
                        ParameterID.SCP_AMOUNT_CREDITED, outputParams);
                if (amountCreditparameterValue!=null)
                {
                    final long clawback = amount - amountCreditparameterValue.longValue();
                    balance.value = balance.value + clawback;
                }
            }
            catch (final org.omg.CORBA.COMM_FAILURE commFail)
            {
                result = ErrorCode.SERVICE_NOT_AVAILABLE;
            }
            catch (final Exception e)
            {
                new MinorLogMsg(this, "Fail to increment balance: " + e, null).log(getContext());
                result = ErrorCode.SERVICE_NOT_AVAILABLE; //DZ: added to be consistent
            }
        }
        else
        {
            // connection not available
            result = ErrorCode.SERVICE_NOT_AVAILABLE;
        }

        pmLogMsg.log(getContext());
        
        return result;
    }
    
	

	/**
     * Request a Debit to the balance in the OCG profile for the given subscriber.
     * 
     * @param msisdn IN subscriber identifier 
     * @param subType IN prepaid or postpaid
     * @param amount IN amount being debited
     * @param currencyType IN currency type
     * @param balFlag 
     * @param erReference
     * @param walletId IN this determines which wallet to debit. This field is the BMID in
     *            the <code>SubscriptionType</code>.
     * @param balance OUT balance + credit limit from the OCG profile
     * @return
     */
    public int requestDebit (String msisdn,
            final SubscriberTypeEnum subType,
            final long amount,
            final String currencyType,
            final boolean balFlag,
            final String erReference,
            final long walletId,
            final LongHolder balance)
    {
        return requestDebit(msisdn, subType, amount, currencyType, balFlag, erReference, walletId, balance, null, null);
    }

    
    /**
     * Resets overdraft balance and returns the reset value in the overdraftBalance long holder.
     * @param msisdn
     * @param subType
     * @param amount
     * @param currencyType
     * @param balFlag
     * @param erReference
     * @param walletId
     * @param overdraftBalance
     * @return
     */
    public int resetOverdraftBalance(String msisdn,
            SubscriberTypeEnum subType,
            long amount,
            String currencyType,
            boolean balFlag,
            String erReference,
            final long walletId,
            LongHolder overdraftBalance)
    {
        LongHolder balance = new LongHolder();
        return requestDebit(msisdn, subType, amount, currencyType, balFlag, erReference, walletId, balance, overdraftBalance, true);
    }
    
    public int requestDebit (String msisdn,
            final SubscriberTypeEnum subType,
            final long amount,
            final String currencyType,
            final boolean balFlag,
            final String erReference,
            final long walletId,
            final LongHolder balance,
            LongHolder overdraftBalance,
            Boolean resetOverdraft)
    {
    	return requestDebit(msisdn, subType, amount, currencyType, balFlag, erReference, walletId, balance, overdraftBalance, resetOverdraft, null, null);
    }
    
    /**
     * Request a Debit to the balance in the OCG profile for the given subscriber.
     * 
     * @param msisdn IN subscriber identifier 
     * @param subType IN prepaid or postpaid
     * @param amount IN amount being debited
     * @param currencyType IN currency type
     * @param balFlag 
     * @param erReference
     * @param walletId IN this determines which wallet to debit. This field is the BMID in
     *            the <code>SubscriptionType</code>.
     * @param balance OUT balance + credit limit from the OCG profile
     * @param overdraftBalance OUT overdraft balance
     * @param resetOverdraft 
     * @return
     */
    public int requestDebit (String msisdn,
            final SubscriberTypeEnum subType,
            final long amount,
            final String currencyType,
            final boolean balFlag,
            final String erReference,
            final long walletId,
            final LongHolder balance,
            LongHolder overdraftBalance,
            Boolean resetOverdraft,
            final Map<Short, Parameter> ocgInputParamMap,
            final Map<Short, Parameter> ocgOutputParamMap)
    {
        final PMLogMsg pmLogMsg = new PMLogMsg(PM_MODULE, "requestDebit()");
        
        int result = -1;
        final PrepaidAccountService service = getService();
        
        //DZ passing subtype vis parameter set
        final Parameter[] inputParams = getParameterSet(subType, true, walletId, null, resetOverdraft, isSubscriptionTypeAlreadyPresent(ocgInputParamMap));
        
        final Parameter[] inputParamsFromContext = getParameterArrayFromMap(ocgInputParamMap);
        final Parameter[] inputParamsForOcg = mergeArraysSafe(inputParams,inputParamsFromContext);
        
        final ParameterSetHolder outputParams = new ParameterSetHolder();

        final LongHolder shortfall = new LongHolder();

        if (service != null)
        {
            try 
            {
                result = service.requestDebit(msisdn, amount, currencyType, balFlag, inputParamsForOcg, erReference, balance,
                        shortfall, outputParams);
                
                populateOcgOutputParameterMap(ocgOutputParamMap, outputParams);

                // If reseting overdraft, return the overdraft reset value instead of the account balance.
                if (resetOverdraft!=null && resetOverdraft)
                {
                    ParameterValue overdraftParameterValue = getParameterValue(ParameterID.RESET_OD_ACCOUNT_BALANCE, outputParams);
                    if (overdraftParameterValue != null)
                    {
                        overdraftBalance.value = overdraftParameterValue.longValue();
                    }
                    else
                    {
                        overdraftBalance.value = 0;
                    }
                }
            }
            catch (final org.omg.CORBA.COMM_FAILURE commFail)
            {
                result = ErrorCode.SERVICE_NOT_AVAILABLE;
            }
            catch (final Exception e)
            {
                new MinorLogMsg(this, "Fail to increment balance: " + e, null).log(getContext());
                result = ErrorCode.SERVICE_NOT_AVAILABLE; //DZ: added to be consistent
            }
        }
        else
        {
            // connection not available
            result = ErrorCode.SERVICE_NOT_AVAILABLE;
        }

        pmLogMsg.log(getContext());
        
        return result;
    }


    /**
     * The PrepaidAccountService.requestBalance CORBA call to the OCG returns the
     * profile's balance as its Balance + Credit Limit.
     * NOTE that the outputBalance MUST be instantiated by the caller.
     * 
     * @param msisdn IN OCG profile identifier 
     * @param subType IN SubscriberTypeEnum prepaid or postpaid
     * @param currencyType IN currency type
     * @param sendExpiry IN true=return the expiry date on the OCG profile
     * @param erReference IN 
     * @param subscriptionType IN this determines which wallet to retrieve the balance
     *            from.
     * @param outputBalance OUT balance + credit limit from the OCG profile.
     * @return
     */
    public int requestBalance(final String msisdn,
            final SubscriberTypeEnum subType,
            final String currencyType,
            final boolean sendExpiry,
            final String erReference,
            final long subscriptionType,
            final LongHolder outputBalance,
            final LongHolder outputOverdraftBalance,
            final LongHolder outputOverdraftDate)
    {
        return requestBalance(msisdn, subType, currencyType, sendExpiry, erReference, subscriptionType, outputBalance,
                outputOverdraftBalance, outputOverdraftDate, null);
    }

    
    /**
     * The PrepaidAccountService.requestBalance CORBA call to the OCG returns the
     * profile's balance as its Balance + Credit Limit.
     * NOTE that the outputBalance MUST be instantiated by the caller.
     * 
     * @param msisdn IN OCG profile identifier 
     * @param subType IN SubscriberTypeEnum prepaid or postpaid
     * @param currencyType IN currency type
     * @param sendExpiry IN true=return the expiry date on the OCG profile
     * @param erReference IN 
     * @param subscriptionType IN this determines which wallet to retrieve the balance from.
     * @param outputBalance OUT balance + credit limit from the OCG profile.
     * @return
     */
    public int requestBalance(final String msisdn,
            final SubscriberTypeEnum subType,
            final String currencyType,
            final boolean sendExpiry,
            final String erReference,
            final long subscriptionType,
            final LongHolder outputBalance,
            final LongHolder outputOverdraftBalance,
            final LongHolder outputOverdraftDate,
            final Boolean ActivationFlag)
    {
        final PMLogMsg pmLogMsg = new PMLogMsg(PM_MODULE, "requestBalance()");
        
        int result = -1;
        final PrepaidAccountService service = getService();
     
        if (service != null)
        {
            // initialize inputParamSet and outputParamSet
            final Parameter[] inputParams = getParameterSet(subType, true, subscriptionType, ActivationFlag, null, false);
            final ParameterSetHolder outputParams = new ParameterSetHolder();

            final org.omg.CORBA.StringHolder expiry = new org.omg.CORBA.StringHolder();
            try 
            {
                result = service.requestBalance(msisdn, currencyType, sendExpiry, inputParams, erReference, expiry,
                        outputBalance, outputParams);

                ParameterValue parameterValue = getParameterValue(ParameterID.OD_ACCOUNT_BALANCE,
                        outputParams);
                if( parameterValue != null )
                {
                    outputOverdraftBalance.value = parameterValue.longValue();   
                }
                parameterValue = getParameterValue(ParameterID.OD_DATE, outputParams);
                if( parameterValue != null )
                {
                    outputOverdraftDate.value = parameterValue.longValue();   
                }
            }
            catch (final org.omg.CORBA.COMM_FAILURE commFail)
            {
                result = ErrorCode.SERVICE_NOT_AVAILABLE;
            }
            catch (final Exception e)
            {
                new MinorLogMsg(this, "Fail to request balance: " + e, e).log(getContext());
                result = ErrorCode.SERVICE_NOT_AVAILABLE; //DZ: added to be consistent
            }
        }

        pmLogMsg.log(getContext());
        
        return result;
    }

        
    public int activateSubscriber(final String msisdn,
            final SubscriberTypeEnum subType,
            final String currencyType,
            final boolean sendExpiry,
            final String erReference,
            final LongHolder outputBalance,
            final long subscriptionType)
    {
        // while activating nobody is interested in overdraft. What it is going to be? more than zero? Really?
        final LongHolder outputOverdraftBalance = new LongHolder();
        final LongHolder outputOverdraftDate = new LongHolder();
        return requestBalance(msisdn, subType, currencyType, sendExpiry, erReference, subscriptionType, outputBalance,
                outputOverdraftBalance, outputOverdraftDate, Boolean.TRUE);
    }

    	
    /**
     * Extends expiry date for profile on OCG
     * 
     * @param msisdn
     * @param subType
     * @param currencyType
     * @param updExp
     * @param extension
     * @param erReference
     * @param newExpDate The new expiry date if the expiry date was updated
     * @return Result code
     */
    public int extendExpiryDate(final String msisdn,
            final SubscriberTypeEnum subType,
            final String currencyType,
            final boolean updExp,
            final short extension,
            final String erReference,
            final long subscriptionType,
            final StringBuilder newExpDate)
    {
        final PMLogMsg pmLogMsg = new PMLogMsg(PM_MODULE, "extendExpiryDate()");
        
        int result = -1;
        
        final PrepaidAccountService service = getService();
        final Parameter[] inputParams = getParameterSet(subType, false, subscriptionType, false);
        final ParameterSetHolder outputParams = new ParameterSetHolder();
        final LongHolder balance = new LongHolder();
        
        if (service != null)
        {
            try 
            {
            	if ( LogSupport.isDebugEnabled(getContext()))
                {
                    new DebugLogMsg(this, "Updating the expiry date for : " + "updExp " + updExp + "extension "
                            + extension + "erReference " + erReference + "newExpDate " + newExpDate, null)
                            .log(getContext());
                }

                result = service.requestCredit(msisdn, 0, currencyType, updExp, extension, inputParams, erReference,
                        balance, outputParams);
            	
                // If the caller needs the new expiry date, set it.
                if (newExpDate != null)
                {
                    // Look for the new expiry date in the output parameters
                    final ParameterValue parameterValue = getParameterValue(ParameterID.NEW_EXPIRY,
                            outputParams);
                    if( parameterValue != null )
                    {
                        newExpDate.append(parameterValue.stringValue());
                    }
                }
            }
            catch (final org.omg.CORBA.COMM_FAILURE commFail)
            {
                result = ErrorCode.SERVICE_NOT_AVAILABLE;
            }
            catch (final Exception e)
            {
                new MinorLogMsg(this, "Fail to extend expiry date: " + e, null).log(getContext());
                result = ErrorCode.SERVICE_NOT_AVAILABLE; //DZ: added to be consistent
            }
        }
        else
        {
            // connection not available
            result = ErrorCode.SERVICE_NOT_AVAILABLE;
        }

        pmLogMsg.log(getContext());
        
        return result;
    }

    private <T> String getCurrencyCode(final Context context, final T subscriber) throws HomeException
    {
        // Use bean loader to find the currency for the subscriber
        BeanLoaderSupport beanLoaderSupport = BeanLoaderSupportHelper.get(context);

        // Prepare the subcontext
        // Put the subscriber (base case) into the context for the bean loader to use
        Context sCtx = context.createSubContext();
        Class<AbstractBean> subClass = (Class<AbstractBean>) subscriber.getClass();
        sCtx.put(subClass, subscriber);

        // Install the bean loader map
        Map<Class, Collection<PropertyInfo>> beanLoaderMap = beanLoaderSupport.getBeanLoaderMap(sCtx, subClass);
        beanLoaderSupport.setBeanLoaderMap(sCtx, beanLoaderMap);

        // Get the currency
        Currency currency = beanLoaderSupport.getBean(sCtx, Currency.class);
        if (currency == null)
        {
            throw new HomeException("System Error: Bean Loader is unable to locate currency object.");
        }

        String currencyCode = currency.getCode();
        return currencyCode;
    }

    private ParameterValue getParameterValue(short parameterId, ParameterSetHolder params)
    {
        if( params != null )
        {
            return getParameterValue(parameterId, params.value);
        }
        return null;
    }

    private ParameterValue getParameterValue(short parameterId, Parameter[] params)
    {
        if( params != null )
        {
            for( Parameter outParam : params )
            {
                if( outParam.parameterID == parameterId )
                {
                    return outParam.value;
                }
            }
        }
        return null;
    }

    
    private Parameter[] getParameterSet(final SubscriberTypeEnum subType,
            final boolean updateBalance,
            final long subscriptionType, boolean bypassSubscriptionType)
    {
    	return  getParameterSet(subType, updateBalance, subscriptionType, null, null, bypassSubscriptionType); 
    }


    private Parameter[] getParameterSet(final SubscriberTypeEnum subType,
            final boolean updateBalance,
            final long subscriptionType,
            final Boolean activationFlag,
            final Boolean resetOverdraftBalance, boolean bypassSubscriptionType)
    {
        //    DZ passing subtype via parameter set
        if(subType==null)
        {
            return new Parameter[0];
        }
        
        Parameter[] inputParams;
        Parameter inParam;
        
        List<Parameter> paramList = new ArrayList<Parameter>();
        
        
        inParam = new Parameter();
        ParameterValue pv = new ParameterValue();
        pv.intValue(subType.getIndex());
        
        inParam.value=pv;
        inParam.parameterID=ParameterID.ACCOUNT_TYPE;
        
        paramList.add(inParam);
        
        inParam = new Parameter();
        pv = new ParameterValue();
        pv.booleanValue(updateBalance);
        inParam.value=pv;
        inParam.parameterID=com.redknee.product.s2100.oasis.param.ParameterID.UPDATE_BALANCE_FLAG;
        
        paramList.add(inParam);
        
        // this is done in case of createSubscriptionTransaction call for SecodaryBalance/PTUB.
        if(!bypassSubscriptionType)
        {
        	inParam = new Parameter();
	        pv = new ParameterValue();
	        pv.intValue((int)subscriptionType);
	        inParam.value = pv;
	        inParam.parameterID = com.redknee.product.s2100.oasis.param.ParameterID.SUBSCRIPTION_TYPE;
	        paramList.add(inParam);
        }
        
        //Always by pass monthly spend limit feature when CRM does debit
        inParam = new Parameter();
        pv = new ParameterValue();
        pv.booleanValue(true);
        inParam.value = pv;
        inParam.parameterID = com.redknee.product.s2100.oasis.param.ParameterID.BYPASS_MONTHLYSPENDLIMIT;
        
        paramList.add(inParam);
        
        if (activationFlag != null)
        {
        	pv = new ParameterValue();
    		pv.booleanValue(activationFlag.booleanValue());

    		inParam = new Parameter();
    		inParam.value = pv;
    		inParam.parameterID = ParameterID.ACTIVATE_FLAG;
    		
    		paramList.add(inParam);
        }

        if (resetOverdraftBalance != null)
        {

            pv = new ParameterValue();
            /**
             * TT #13030831048 Passing RESET_OD_ACCOUNT_BALANCE as long value in InParam because
             * OCG expects it as long.
             */
            pv.longValue(resetOverdraftBalance.booleanValue()? 1L : 0L);

            inParam = new Parameter();
            inParam.value = pv;
            inParam.parameterID = ParameterID.RESET_OD_ACCOUNT_BALANCE;
            
            paramList.add(inParam);
        }
        
        // Please refer java doc of the toArray(T[]) method to understand why the array size can be 1.
        inputParams = paramList.toArray(new Parameter[1]);

        return inputParams;
    }
    
    private Parameter[] getParameterArrayFromMap(Map<Short, Parameter> ocgInputParamMap) 
    {
    	Parameter[] inputParams = null;

    	if(ocgInputParamMap != null)
    	{
    		inputParams = ocgInputParamMap.values().toArray(new Parameter[0]);
    	}

    	return inputParams;
    }

	private Parameter[] mergeArraysSafe(Parameter[] arrayA,
			Parameter[] arrayB) 
	{
		int lengthA = (arrayA == null)? 0: arrayA.length;
		int lengthB = (arrayB == null)? 0 : arrayB.length;

		Parameter[] mergedArray = new Parameter[lengthA + lengthB]; 

		for(int i = 0; i < lengthA; i++)
		{
			mergedArray[i] = arrayA[i];
		}

		for(int i = 0; i < lengthB; i++)
		{
			mergedArray[lengthA + i] = arrayB[i];
		}
		
		return mergedArray;
	}
	
	private void populateOcgOutputParameterMap(Map<Short, Parameter> ocgOutputParamMap,
			ParameterSetHolder outputParams) 
	{
		if(ocgOutputParamMap != null && outputParams != null)
		{
			for(Parameter param : outputParams.value)
			{
				if(param != null)
				{
					ocgOutputParamMap.put(param.parameterID, param);
				}
			}
		}
	}
}
