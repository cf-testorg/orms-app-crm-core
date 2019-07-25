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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.LongHolder;

import com.redknee.app.crm.CoreCrmConstants;
import com.redknee.app.crm.bean.AdjustmentInfo;
import com.redknee.app.crm.bean.AdjustmentType;
import com.redknee.app.crm.bean.AdjustmentTypeActionEnum;
import com.redknee.app.crm.bean.AdjustmentTypeEnum;
import com.redknee.app.crm.bean.IdentifierEnum;
import com.redknee.app.crm.bean.OcgGenericParameterHolder;
import com.redknee.app.crm.bean.SubscriberTypeEnum;
import com.redknee.app.crm.bean.TransactionHome;
import com.redknee.app.crm.bean.TransactionXInfo;
import com.redknee.app.crm.bean.core.SubscriptionType;
import com.redknee.app.crm.bean.core.Transaction;
import com.redknee.app.crm.client.AppOcgClient;
import com.redknee.app.crm.exception.codemapping.S2100ReturnCodeMsgMapping;
import com.redknee.app.crm.tps.pipe.TPSPipeConstant;
import com.redknee.app.crm.transaction.TransactionReceiveDateComparator;
import com.redknee.app.crm.xhome.home.OcgTransactionException;
import com.redknee.framework.core.locale.Currency;
import com.redknee.framework.core.locale.CurrencyHome;
import com.redknee.framework.license.LicenseMgr;
import com.redknee.framework.xhome.auth.bean.User;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.GTE;
import com.redknee.framework.xhome.elang.LT;
import com.redknee.framework.xhome.elang.LTE;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.language.MessageMgr;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;
import com.redknee.product.s2100.ErrorCode;
import com.redknee.product.s2100.oasis.param.Parameter;
import com.redknee.product.s2100.oasis.param.ParameterID;
import com.redknee.product.s2100.oasis.param.ParameterValue;

/**
 * Provides support methods for dealing with Transactions.
 *
 * @author gary.anderson@redknee.com
 */
public class DefaultCoreTransactionSupport implements CoreTransactionSupport
{
    protected static CoreTransactionSupport instance_ = null;
    public static CoreTransactionSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultCoreTransactionSupport();
        }
        return instance_;
    }

    protected DefaultCoreTransactionSupport()
    {
    }

    /**
     * PPSM GL code.
     */
    public static final String GLCODE_PPSM = "default";


    /**
     * {@inheritDoc}
     */
    public String getCsrIdentifier(final Context context)
    {
        final User principal = (User) context.get(java.security.Principal.class, new User());
        return (principal.getId().trim().equals("") ? "System" : principal.getId());
    }


    /**
     * {@inheritDoc}
     */
    public Home getTransactionsHome(final Context context, final Predicate filter,
            final Date start, final Date end)
    {
        final And and = new And();
        {
            if (filter != null)
            {
                and.add(filter);
            }
            and.add(new LT(TransactionXInfo.RECEIVE_DATE, end));
            and.add(new GTE(TransactionXInfo.RECEIVE_DATE, start));
        }

        Context whereCtx = HomeSupportHelper.get(context).getWhereContext(context, Transaction.class, and);
        return (Home) whereCtx.get(TransactionHome.class);
    }


    /**
     * {@inheritDoc}
     */
    public Collection<Transaction> getTransactions(final Context context, final Predicate filter, final Date start, final Date end)
    {
        final Home transactionHome = getTransactionsHome(context, filter, start, end);

        final Collection<Transaction> transactions;
        try
        {
            transactions = transactionHome.selectAll(context);
        }
        catch (final HomeException exception)
        {
            new MajorLogMsg(this, "Failed getting Transaction records for predicate \"" + filter + "\" " + "with \"" + start + "\" <= date < \"" + end + "\".", exception).log(context);

            return new ArrayList<Transaction>();
        }

        return transactions;
    }


    /**
     * {@inheritDoc}
     */
    public Collection<Transaction> getTransactionsForAccount(final Context context,
            final String accountIdentifier, final Date start, final Date end)
    {
        return getTransactions(context, new EQ(TransactionXInfo.BAN, accountIdentifier), start, end);
    }


    /**
     * {@inheritDoc}
     */
    public Collection getTransactionsForAccountInclusive(final Context context, final String accountIdentifier,
            final Date start, final Date end)
    {
        Collection transactions = null;
        try
        {
            final And and = new And();

            and.add(new EQ(TransactionXInfo.BAN, accountIdentifier));
            and.add(new GTE(TransactionXInfo.RECEIVE_DATE, start));
            and.add(new LTE(TransactionXInfo.RECEIVE_DATE, end));

            transactions = HomeSupportHelper.get(context).getBeans(context, Transaction.class, and);
        }
        catch (final HomeException exception)
        {
            new MajorLogMsg(this, "Failed getting Transaction records for BAN \"" + accountIdentifier + "\" with \"" + start + "\" <= date < \"" + end + "\".", exception).log(context);
        }

        return transactions;
    }


    /**
     * {@inheritDoc}
     */
    public Collection<Transaction> getTransactionsForSubscriberID(final Context context,
            final String subscriberID, final Date start, final Date end)
    {
        return getTransactions(context, new EQ(TransactionXInfo.SUBSCRIBER_ID, subscriberID), start, end);
    }


    /**
     * {@inheritDoc}
     */
    public Transaction getTransaction(final Context ctx, final String receiptNum)
    {
        final Home home = (Home) ctx.get(TransactionHome.class);
        try
        {
            return (Transaction) home.find(ctx, Long.valueOf(receiptNum));
        }
        catch (final NumberFormatException exception)
        {
            new MajorLogMsg(this, "Failed to parse receipt number \"" + receiptNum + "\"", exception).log(ctx);
        }
        catch (final HomeException hEx)
        {
            new MajorLogMsg(this, "Failed getting Transaction records for receipt number \"" + receiptNum + "\"", hEx).log(ctx);
        }
        return null;
    }


    /**
     * {@inheritDoc}
     */
    public boolean isDeposit(final Context context, final Transaction transaction)
    {
        return AdjustmentTypeSupportHelper.get(context).isInCategory(context, transaction.getAdjustmentType(),
            AdjustmentTypeEnum.DepositPayments);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isPayment(final Context context, final Transaction transaction)
    {
        return AdjustmentTypeSupportHelper.get(context)
            .isInCategory(context, transaction.getAdjustmentType(), AdjustmentTypeEnum.Payments);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isStandardPayment(final Context context, final Transaction transaction)
    {
        return AdjustmentTypeSupportHelper.get(context).isInCategory(context, transaction.getAdjustmentType(),
            AdjustmentTypeEnum.StandardPayments);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isDiscount(final Context ctx, final Transaction t)
    {
        return AdjustmentTypeSupportHelper.get(ctx).isInCategory(ctx, t.getAdjustmentType(), AdjustmentTypeEnum.Discount);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isPaymentReversal(final Context context, final Transaction transaction) throws HomeException
    {
        if (isPayment(context, transaction))
        {
            if (isAdjustTypeEither(context, transaction) )
            {
                return transaction.getAmount() > 0;
            }
        }

        return AdjustmentTypeSupportHelper.get(context).isInCategory(context, transaction.getAdjustmentType(),
            AdjustmentTypeEnum.PaymentReversal); 
    }


    /**
     * {@inheritDoc}
     */
    public boolean isAdjustTypeEither(final Context ctx, final Transaction trans) throws HomeException
    {
        final AdjustmentType adjType = AdjustmentTypeSupportHelper.get(ctx).getAdjustmentType(ctx, trans.getAdjustmentType());

        if (adjType != null)
        {
            return adjType.getAction().equals(AdjustmentTypeActionEnum.EITHER);
        }

        throw new HomeException("Can not find adjsutment type " + trans.getAdjustmentType());
    }


    /**
     * {@inheritDoc}
     */
    public boolean isBalanceTransfer(final Context context, final Transaction transaction)
    {
        return AdjustmentTypeSupportHelper.get(context).isInCategory(context, transaction.getAdjustmentType(),
            AdjustmentTypeEnum.BalanceTransfer);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isBalanceTransferDebit(final Context context, final Transaction transaction)
    {
        return AdjustmentTypeSupportHelper.get(context).isInCategory(context, transaction.getAdjustmentType(),
            AdjustmentTypeEnum.BalanceTransferDebit);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isDepositRelease(final Context context, final Transaction transaction)
    {
        return AdjustmentTypeSupportHelper.get(context).isInCategory(context, transaction.getAdjustmentType(),
            AdjustmentTypeEnum.DepositReleaseCategory);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isPaymentConvertedFromDeposit(final Context context, final Transaction transaction)
    {
        return AdjustmentTypeSupportHelper.get(context).isInCategory(context, transaction.getAdjustmentType(),
            AdjustmentTypeEnum.PaymentConvertedFromDeposit);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isInterestPayment(final Context context, final Transaction transaction)
    {
        return AdjustmentTypeSupportHelper.get(context).isInCategory(context, transaction.getAdjustmentType(),
            AdjustmentTypeEnum.InterestPayment);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isPaymentPlanLoanCategory(final Context ctx, final Transaction trans)
    {
        return AdjustmentTypeSupportHelper.get(ctx).isInCategory(ctx, trans.getAdjustmentType(), AdjustmentTypeEnum.PaymentPlan);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isPaymentPlanLoanCredit(final Context ctx, final Transaction trans)
    {
        return AdjustmentTypeSupportHelper.get(ctx).isInCategory(ctx, trans.getAdjustmentType(),
            AdjustmentTypeEnum.PaymentPlanLoanCredit);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isPaymentPlanLoanAdjustment(final Context ctx, final Transaction trans)
    {
        return AdjustmentTypeSupportHelper.get(ctx).isInCategory(ctx, trans.getAdjustmentType(),
            AdjustmentTypeEnum.PaymentPlanLoanAdjustment);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isPaymentPlanAdjustment(final Context ctx, final Transaction trans)
    {
        return AdjustmentTypeSupportHelper.get(ctx).isInCategory(ctx, trans.getAdjustmentType(), AdjustmentTypeEnum.PaymentPlan);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isPaymentPlanLoanPayment(final Context ctx, final Transaction trans)
    {
        return AdjustmentTypeSupportHelper.get(ctx).isInCategory(ctx, trans.getAdjustmentType(),
            AdjustmentTypeEnum.PaymentPlanLoanPayment);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isPaymentPlanLoanReversal(final Context ctx, final Transaction trans)
    {
        return AdjustmentTypeSupportHelper.get(ctx).isInCategory(ctx, trans.getAdjustmentType(),
            AdjustmentTypeEnum.PaymentPlanLoanReversal);
    }
    

    /**
     * Checks if a transaction is a late fee transaction. 
     *
     * @param ctx
     *            The operating context
     * @param trans
     *            The transaction in question
     * @return <code>TRUE</code> if this is a payment plan transaction,
     *         <code>FALSE</code> otherwise
     */
    public boolean isLateFeeCategory(final Context ctx, final Transaction trans)
    {
        return AdjustmentTypeSupportHelper.get(ctx).isInCategory(ctx, trans.getAdjustmentType(), AdjustmentTypeEnum.LateFee);
    }

    /**
     * Checks if a transaction is an early payment transaction. Includes all adjustment
     * types under this category.
     *
     * @param ctx
     *            The operating context
     * @param trans
     *            The transaction in question
     * @return <code>TRUE</code> if this is a payment plan transaction,
     *         <code>FALSE</code> otherwise
     */
    public boolean isEarlyPaymentCategory(final Context ctx, final Transaction trans)
    {
        return AdjustmentTypeSupportHelper.get(ctx).isInCategory(ctx, trans.getAdjustmentType(), AdjustmentTypeEnum.EarlyReward);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isReactivationFeeDebit(final Context ctx, final Transaction trans)
    {
        return AdjustmentTypeSupportHelper.get(ctx).isInCategory(ctx, trans.getAdjustmentType(), AdjustmentTypeEnum.ReactivationFee);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isInitialBalanceCredit(final Context ctx, final Transaction trans)
    {
        return AdjustmentTypeSupportHelper.get(ctx).isInCategory(ctx, trans.getAdjustmentType(), AdjustmentTypeEnum.InitialBalance);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isNotes(final Context ctx, final Transaction trans)
    {
        return AdjustmentTypeSupportHelper.get(ctx).isInCategory(ctx, trans.getAdjustmentType(), AdjustmentTypeEnum.Notes);
    }


    /**
     * {@inheritDoc}
     */
    public void addTransactionGLCode(final Context ctx, final Transaction trans) throws HomeException
    {
        // Code Added: 29-11-05 by skrishnan
        final MessageMgr mmgr = new MessageMgr(ctx, DefaultCoreTransactionSupport.class);

        final AdjustmentType adjustmentType = AdjustmentTypeSupportHelper.get(ctx).getAdjustmentType(ctx, trans.getAdjustmentType());

        if (adjustmentType == null)
        {
            throw new HomeException("Cannot create Transaction.  No AdjustmentType \"" + trans.getAdjustmentType()
                + "\" found in home.");
        }

        final AdjustmentInfo info = (AdjustmentInfo) adjustmentType.getAdjustmentSpidInfo().get(
            Integer.valueOf(trans.getSpid()));

        if (info == null)
        {
            if (AdjustmentTypeSupportHelper.get(ctx).isInCategory(ctx, trans.getAdjustmentType(),
                AdjustmentTypeEnum.PostpaidSupportRedirectCharges))
            {
                trans.setGLCode(GLCODE_PPSM);
            }
            else
            {
                throw new HomeException(mmgr.get("Exception.TransactionSupport.1", "Cannot create Transaction.  "
                        + "No AdjustmentInfo found for AdjustmentType \"{0}\" and ServiceProvider \"{1}\".",
                        new String[]
                        {
                                String.valueOf(trans.getAdjustmentType()), String.valueOf(trans.getSpid()),
                        }));
            }
        }
        else
        {
            if (info.getGLCode() == null || info.getGLCode().equals(""))
            {
                throw new HomeException(mmgr.get("Exception.TransactionSupport.2", "Cannot create Transaction.  "
                        + "No GLCode found for AdjustmentType \"{0}\" and ServiceProvider \"{1}\".", new String[]
                        {
                                String.valueOf(trans.getAdjustmentType()), String.valueOf(trans.getSpid()),
                        }));
            }

            trans.setGLCode(info.getGLCode());
        }
    }


    /**
     * {@inheritDoc}
     */
    public Collection<Transaction> getTransactionsForSubAdjustment(final Context context,
            final String subscriberID, final int type, final Object condition)
    {
        final Home transactionHome = new SortingHome(context, (Home) context.get(TransactionHome.class),
            new TransactionReceiveDateComparator(false));

        final And where = new And();
        where.add(new EQ(TransactionXInfo.SUBSCRIBER_ID, subscriberID));
        where.add(new EQ(TransactionXInfo.ADJUSTMENT_TYPE, Integer.valueOf(type)));
        if (condition != null)
        {
            where.add(condition);
        }

        final Collection<Transaction> transactions;
        try
        {
            transactions = transactionHome.select(context, where);
        }
        catch (final HomeException exception)
        {
            new MajorLogMsg(this, "Failed getting Transaction records for SQL \"" + where + "\"", exception).log(context);

            return new ArrayList<Transaction>();
        }

        return transactions;
    }


    /**
     * {@inheritDoc}
     */
    public Collection<Transaction> getTransactionsForAccountAdjustment(final Context context, final String ban,
            final int type, final Object condition)
    {
        final Home transactionHome = new SortingHome(context, (Home) context.get(TransactionHome.class),
            new TransactionReceiveDateComparator(false));

        final And where = new And();
        where.add(new EQ(TransactionXInfo.BAN, ban));
        where.add(new EQ(TransactionXInfo.ADJUSTMENT_TYPE, Integer.valueOf(type)));
        if (condition != null)
        {
            where.add(condition);
        }

        final Collection<Transaction> transactions;
        try
        {
            transactions = transactionHome.select(context, where);
        }
        catch (final HomeException exception)
        {
            new MajorLogMsg(this, "Failed getting Transaction records for SQL \"" + where + "\"", exception).log(context);

            return new ArrayList<Transaction>();
        }

        return transactions;
    }



    /**
     * {@inheritDoc}
     */
    public Transaction createTransaction(final Context ctx, final Transaction transaction) throws HomeException
    {
        return createTransaction(ctx, transaction, true);
    }


    /**
     * {@inheritDoc}
     */
    public Transaction createTransaction(final Context ctx, final Transaction transaction,
            final boolean useFullPipeline) throws HomeException
    {

        /*
         * TT8053000025: When a transaction is created from GUI, a web border sticks in a
         * ValidatingAmountTransactionHome decorator around the Transaction home. When a
         * credit transaction is created from GUI and the subscriber has $0 services
         * suspended due to failed charges, this decorator incorrectly fails $0 recharges.
         * The fix is to use the original transaction pipeline when creating the recharge
         * transactions.
         */

        Home home;
        if (useFullPipeline)
        {
            home = (Home) ctx.get(CoreCrmConstants.FULL_TRANSACTION_HOME);
            // fall back
            if (home == null)
            {
                home = (Home) ctx.get(TransactionHome.class);
            }
        }
        else
        {
            home = (Home) ctx.get(TransactionHome.class);
        }

        if (home == null)
        {
            throw new HomeException("System Error: TransactionHome is not in the context!");
        }

        return (Transaction) home.create(ctx, transaction);
    }


    /**
     * {@inheritDoc}
     */
    public synchronized long getNextIdentifier(final Context ctx) throws HomeException
    {
        // TODO - 2004-08-04 - Should provide roll-over function. The defaults
        // should not require roll over for a very long time, but there is
        // nothing to prevent an admin from changing the next or end values.
        return IdentifierSequenceSupportHelper.get(ctx).getNextIdentifier(ctx, IdentifierEnum.TRANSACTION_ID, null);
    }


    /**
     * {@inheritDoc}
     */
    public String getTPSFileName(final Context ctx)
    {
        String fileName = "";
        final File tpsFile = (File) ctx.get(TPSPipeConstant.TPS_PIPE_TPS_FILE);

        if (tpsFile != null)
        {
            fileName = tpsFile.getName();
        }

        return fileName;
    }


    /**
     * {@inheritDoc}
     */
    public Transaction findDuplicateTransaction(final Context ctx, final Predicate filter) throws HomeException
    {
        final Home transactionHome = (Home) ctx.get(TransactionHome.class);

        return (Transaction) transactionHome.find(ctx, filter);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isDuplicateTransaction(final Context ctx, final Predicate filter) throws HomeException
    {
        return findDuplicateTransaction(ctx, filter) != null;
    }


    /**
     * {@inheritDoc}
     */
    public boolean isDuplicateTransaction(final Context ctx, final String accountNumber, final String msisdn,
            final int adjustmentType, final long amount, final Date dateTobeChecked) throws HomeException
    {
        final Date startOfToday = CalendarSupportHelper.get(ctx).getDateWithNoTimeOfDay(dateTobeChecked);
        final Date startOfTommarrow = CalendarSupportHelper.get(ctx).getDayAfter(dateTobeChecked);

        final And andPredicate = new And();
        andPredicate.add(new EQ(TransactionXInfo.BAN, accountNumber));
        andPredicate.add(new EQ(TransactionXInfo.MSISDN, msisdn));
        andPredicate.add(new GTE(TransactionXInfo.RECEIVE_DATE, startOfToday));
        andPredicate.add(new LT(TransactionXInfo.RECEIVE_DATE, startOfTommarrow));
        andPredicate.add(new EQ(TransactionXInfo.ADJUSTMENT_TYPE, Integer.valueOf(adjustmentType)));
        andPredicate.add(new EQ(TransactionXInfo.AMOUNT, Long.valueOf(amount)));

        return isDuplicateTransaction(ctx, andPredicate);
    }
    
    
    private String formatAmount(Context ctx, String currency, long amount)
    {
        Currency defaultCurrency = null;
        try
        {
            defaultCurrency = (Currency) ((Home) ctx.get(CurrencyHome.class)).find(currency);
            String txAmount = null;
            if (defaultCurrency != null)
            {
                txAmount = defaultCurrency.formatValue(amount);
                return txAmount;
            }           
        }
        catch (Exception e)
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "Failed to retrieve currency for " + currency, e);
            }
        }
        return String.valueOf(amount);
    }


    /**
     * {@inheritDoc}
     */
    public int forwardTransactionToOcg(final Context ctx, final Transaction trans, final String msisdn, final SubscriberTypeEnum subType,
            final String currency, final boolean rollback, final long amount, final Object srcObj)
        throws HomeException, OcgTransactionException
    {
        AdjustmentType type =  AdjustmentTypeSupportHelper.get(ctx).getAdjustmentType(ctx, trans.getAdjustmentType()); 
        if (!type.isChargeToOcg())
        {
            new DebugLogMsg(this, "skip charge to ABM by config" + type.getCode(), null).log(ctx);
            return 0;  
        }
        
        final AppOcgClient client = (AppOcgClient) ctx.get(ctx, AppOcgClient.class);
        if (client == null)
        {
            throw new HomeException("Create failed. Cannot find AppOcgClient in context.");
        }

        int result = 0;
        final String erReference = "AppCrm-" + trans.getReceiptNum();

        // Susbscriber's balance after the transaction
        final LongHolder balance = new LongHolder();

        long transactionAmount = amount;
        if (rollback)
        {
            transactionAmount = transactionAmount * -1;
        }

        SubscriptionType subscriptionType = SubscriptionType.getSubscriptionType(ctx, trans.getSubscriptionTypeId());
        if (null == subscriptionType)
        {
            throw new HomeException("Transactiion does not have a valid subscriptionType.");
        }

        
        if (transactionAmount > 0)
        {
            final boolean bBalFlag = 
                subType == SubscriberTypeEnum.PREPAID 
                || (subType == SubscriberTypeEnum.POSTPAID && !trans.getAllowCreditLimitOverride());

            OcgGenericParameterHolder paramHolder =  (OcgGenericParameterHolder)ctx.get(OcgGenericParameterHolder.class);
            
            Map<Short, Parameter> inputParameterMap = new HashMap<Short, Parameter>();
            /**
             * TT# 13112120056 We need to read extTransId value from OCG even if request is coming from DCRM
             */
            Map<Short, Parameter> outputParameterMap = new HashMap<Short, Parameter>();
            
            if(paramHolder != null)
            {
            	/**
            	 * This new parameter ParameterID.OVERRIDE_CREDITLIMIT was introduced in 
            	 * OCG ICD (ProductS2100.PrepaidAccountService), for TCP 9.3, for postpaid.
            	 */
            	if(subType == SubscriberTypeEnum.POSTPAID && !trans.getAllowCreditLimitOverride())
            	{
            	    Parameter param = getCrediLimitOverrideOCGParam(false);
                    paramHolder.addInputParameter(param);
            	}
            	
            	inputParameterMap = paramHolder.getInputParameterMap();
            	outputParameterMap = paramHolder.getOutputParameterMap();
            }
            else if(subType == SubscriberTypeEnum.POSTPAID && !trans.getAllowCreditLimitOverride())
            {
                Parameter param = getCrediLimitOverrideOCGParam(false);
                inputParameterMap = new HashMap<Short, Parameter>();
                inputParameterMap.put(param.parameterID, param);
            }
            
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(srcObj, "debit MSISDN[" + trans.getMSISDN() + "] amount[" + transactionAmount
                    + "] currency[" + currency + "] balFlag[" + bBalFlag + "] allowCreditLimitOverride["+ 
                    trans.getAllowCreditLimitOverride()+ "] erReference[" + erReference
                    + "] subscriptionType [" + subscriptionType + "] inputParameterMap["+ inputParameterMap+ 
                    "]", null)
                    .log(ctx);
            }
            
            Context context = (Context) ctx.get("app");
            int secondarySubscriptionType = context.getInt("SecondarySubscriptionTypeValue");

            if (type.getParent(ctx).getCode() == AdjustmentTypeEnum.SecondaryBalance_INDEX
                    && secondarySubscriptionType != 0)
            {
                Parameter infoParam = new Parameter();
                infoParam.parameterID = ParameterID.SUBSCRIPTION_TYPE;
                ParameterValue paramValue = new ParameterValue();
                paramValue.intValue(secondarySubscriptionType);
                infoParam.value = paramValue;

                if (inputParameterMap == null)
                {
                    inputParameterMap = new HashMap<Short, Parameter>();
                }
                inputParameterMap.put(infoParam.parameterID, infoParam);
            }
            context.remove("SecondarySubscriptionTypeValue");
   
            result = client.requestDebit(trans.getMSISDN(), subType, transactionAmount, currency, bBalFlag,
                erReference, subscriptionType.getId(), balance, null, null, inputParameterMap, outputParameterMap);
            
            fetchExternalTransactionId(ctx , trans, outputParameterMap, inputParameterMap);
            

            /*
             * Update the Running Balance for the Transaction. If the transaction is from
             * the VRA poller then the balance comes from the ER and has already been
             * updated.
             */
            if (!trans.getFromVRAPoller())
            {
                trans.setBalance(balance.value);
            }

            if (result != ErrorCode.NO_ERROR)
            {
                if (rollback)
                {
                    final String msg = "Fail to rollback OCG transaction " + trans.getReceiptNum() + " for MSISDN "
                        + trans.getMSISDN() + " , Amount " + transactionAmount + " , Result " + result + ". "
                        + S2100ReturnCodeMsgMapping.getMessage(result);
                    new MinorLogMsg(srcObj, msg, null).log(ctx);
                }
                else
                {                                      
                    String msg = "Failed to debit MSISDN " + trans.getMSISDN() + " for amount " + formatAmount(ctx, currency, transactionAmount)
                        + ". ";
                    /*
                     * Profile has expired. This error message needs to be clearer hence
                     * we handle this as a special case.
                     */
                    msg += S2100ReturnCodeMsgMapping.getMessage(result);

                    final OcgTransactionException exception = new OcgTransactionException(msg, result);
                    if (LogSupport.isDebugEnabled(ctx))
                    {
                        LogSupport.debug(ctx, srcObj, "forwardTransactionToOcg Error",
                            exception);
                    }
                    throw exception;
                }
            }
        }
        else if (transactionAmount < 0 || trans.getExpiryDaysExt() > 0)
        {
            final boolean bExtendBalance = subType == SubscriberTypeEnum.PREPAID;
            short nNumDaysExtendBalance = trans.getExpiryDaysExt();

            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(srcObj, "credit MSISDN[" + trans.getMSISDN() + "] amount[" + -transactionAmount
                    + "] currency[" + currency + "] bExtendBalance[" + bExtendBalance + "] nNumDaysToExtend["
                    + nNumDaysExtendBalance + "] erReference[" + erReference + "] subscriptionType ["
                    + subscriptionType + "]", null).log(ctx);
            }
            
            OcgGenericParameterHolder paramHolder =  (OcgGenericParameterHolder)ctx.get(OcgGenericParameterHolder.class);
            
            Map<Short, Parameter> inputParameterMap = new HashMap<Short, Parameter>();
            Map<Short, Parameter> outputParameterMap = new HashMap<Short, Parameter>();
            
            Parameter applicationId = null; 
            
            if(paramHolder != null)
            {
            	 inputParameterMap = paramHolder.getInputParameterMap();
            	 outputParameterMap = paramHolder.getOutputParameterMap();
            	 applicationId = inputParameterMap.get(ParameterID.APPLICATION_ID);
            }
            
            if(applicationId != null && (trans.getAdjustmentType() == AdjustmentTypeEnum.OneTimeCreditCardTopUp_INDEX 
            		|| trans.getAdjustmentType() == AdjustmentTypeEnum.RecurringCreditCardTopUp_INDEX
            		|| trans.getAdjustmentType() == AdjustmentTypeEnum.PaymentMethodVisa_INDEX
            		|| trans.getAdjustmentType() == AdjustmentTypeEnum.PaymentMethodAmex_INDEX
            		|| trans.getAdjustmentType() == AdjustmentTypeEnum.PaymentMethodMasterCard_INDEX
            		|| trans.getAdjustmentType() == AdjustmentTypeEnum.PaymentMethodCreditoFacilCondensa_INDEX
            		|| trans.getAdjustmentType() == AdjustmentTypeEnum.PaymentMethodDiners_INDEX))
            {
            	nNumDaysExtendBalance = -1;
            	if(LogSupport.isDebugEnabled(ctx))
            	{
                    LogSupport.debug(ctx, this,
                                    "Application Id "+ applicationId + " found in requestCredit() hence sending Days Extend Balance as -1 to use OCG ladder functionality");
            	}
            }

            if (isExitoMvneLicensed(ctx))
            {
            	// In case of EXITO two additional parameters - Adjustment type and External Transaction ID, received as part of DEBIT call to Payment gateway will be
            	// send as input parameter to the CREDIT call
            	Parameter adjustTypeParam = new Parameter();
            	ParameterValue adjustTypeParamValue = new ParameterValue();
            	adjustTypeParam.parameterID= ParameterID.ADJUSTMENT_TYPE;
            	adjustTypeParamValue.stringValue(String.valueOf(trans.getAdjustmentType()));
            	adjustTypeParam.value=adjustTypeParamValue;
            	inputParameterMap.put(ParameterID.ADJUSTMENT_TYPE, adjustTypeParam);
            }
            result = client.requestCredit(msisdn, subType, -transactionAmount, currency, bExtendBalance,
                    nNumDaysExtendBalance, erReference, subscriptionType.getId(), null, balance, inputParameterMap, outputParameterMap);
            
            fetchExternalTransactionId(ctx, trans, outputParameterMap, inputParameterMap);
            
            /*
             * Update the Running Balance for the Transaction with the value returned from
             * the requesCredit call.
             */
            trans.setBalance(balance.value);

            if (result != ErrorCode.NO_ERROR)
            {
                if (rollback)
                {
                    final String msg = "Fail to rollback OCG transaction " + trans.getReceiptNum() + " for MSISDN "
                            + trans.getMSISDN() + ", amount " + transactionAmount + ", result " + result + "."
                            + S2100ReturnCodeMsgMapping.getMessage(result);
                    new MinorLogMsg(srcObj, msg, null).log(ctx);
                }
                else
                {                    
                    final String msg = "Failed to credit MSISDN " + trans.getMSISDN() + " for amount "
                            + formatAmount(ctx, currency, -transactionAmount) + ". " + S2100ReturnCodeMsgMapping.getMessage(result);
                    final OcgTransactionException exception = new OcgTransactionException(msg, result);
                    if (LogSupport.isDebugEnabled(ctx))
                    {
                        LogSupport.debug(ctx, srcObj, "forwardTransactionToOcg Error",
                            exception);
                    }
                    throw exception;
                }
            }
        }
        else if (transactionAmount == 0)
        {
            /*
             * For all transactions that cost nothing (i.e. Activating a free service) the
             * running balance still has to be updated.
             */
            result = client.requestBalance(trans.getMSISDN(), subType, currency, false, erReference,
                    subscriptionType.getId(), balance, new LongHolder(), new LongHolder());

            // Update the Running Balance for the Transaction.
            trans.setBalance(balance.value);
        }
        return result;
    }

    /**
     * @param value
     * @return
     */
    private Parameter getCrediLimitOverrideOCGParam(final boolean value)
    {
        Parameter param = new Parameter();
        param.parameterID = ParameterID.OVERRIDE_CREDITLIMIT;
        ParameterValue paramValue = new ParameterValue();
        paramValue.booleanValue(value);
        param.value = paramValue;
        return param;
    }
    

    /**
     * In case OCG sends back this parameter, it needs to be set in the transaction bean.
     * @param trans
     * @param outputParameterMap
     */
	private void fetchExternalTransactionId(Context ctx, Transaction trans,
			Map<Short, Parameter> outputParameterMap, Map<Short, Parameter> inputParameterMap) 
	{
		boolean extTransIdSet = false;
		if (isExitoMvneLicensed(ctx))
		{
			if(outputParameterMap != null)
			{
				Parameter ocgGeneratedExtTransactionId = outputParameterMap.get(ParameterID.EXT_TRANSACTION_ID);
				if(ocgGeneratedExtTransactionId != null && ocgGeneratedExtTransactionId.value != null && !("".equals(ocgGeneratedExtTransactionId.value.stringValue())))
				{
					trans.setExtTransactionId(ocgGeneratedExtTransactionId.value.stringValue());
					extTransIdSet = true;
				}
			}
			
			if (!extTransIdSet && inputParameterMap != null)
			{
				Parameter ocgGeneratedExtTransactionId = inputParameterMap.get(ParameterID.EXT_TRANSACTION_ID);
				if(ocgGeneratedExtTransactionId != null && ocgGeneratedExtTransactionId.value != null && !("".equals(ocgGeneratedExtTransactionId.value.stringValue())))
				{
					trans.setExtTransactionId(ocgGeneratedExtTransactionId.value.stringValue());
				}
			}
		}

	}

	public int forwardToOcg(final Context ctx, String msisdn, long amount, SubscriptionType subscriptionType,
            final String currency, final SubscriberTypeEnum subType, final Object srcObj, final short expiryExt,
            final boolean rollback) throws HomeException, OcgTransactionException
    {
        // Susbscriber's balance after the transaction
        final com.redknee.framework.xhome.holder.LongHolder balance = new com.redknee.framework.xhome.holder.LongHolder();
        return forwardToOcg(ctx, msisdn, amount, subscriptionType, currency, subType, srcObj, expiryExt, rollback, balance);
        
    }

    public int forwardToOcg(final Context ctx, String msisdn, long amount, SubscriptionType subscriptionType,
            final String currency, final SubscriberTypeEnum subType, final Object srcObj, final short expiryExt,
            final boolean rollback, final com.redknee.framework.xhome.holder.LongHolder balance) throws HomeException, OcgTransactionException
    {
        final String erReference = "AppCrm-recur-" + msisdn;
        final AppOcgClient client = (AppOcgClient) ctx.get(ctx, AppOcgClient.class);
        if (client == null)
        {
            throw new HomeException("Create failed. Cannot find AppOcgClient in context.");
        }
        // Susbscriber's balance after the transaction
        final LongHolder corbaBalance = new LongHolder();
        int result = 0;
        if (amount > 0)
        {
            // bBalFlag should be always false, since Ecare only support post pay
            final boolean bBalFlag = subType == SubscriberTypeEnum.PREPAID;
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(srcObj, "debit (fwd) MSISDN [" + msisdn + "] amount[" + amount + "] currency[" + currency
                        + "] balFlag[" + bBalFlag + "] erReference[" + erReference + "] subscriptionType ["
                        + subscriptionType + "]", null).log(ctx);
            }
            result = client.requestDebit(msisdn, subType, amount, currency, bBalFlag, erReference,
                    subscriptionType.getId(), corbaBalance);
            if (result != ErrorCode.NO_ERROR)
            {
                if (rollback)
                {
                    final String msg = "Fail to rollback OCG  for MSISDN " + msisdn + " , Amount " + amount
                            + " , Result " + result + ". " + S2100ReturnCodeMsgMapping.getMessage(result);
                    new MinorLogMsg(srcObj, msg, null).log(ctx);
                }
                else
                {                    
                    String msg = "Failed to debit MSISDN " + msisdn + " for amount " + formatAmount(ctx, currency, amount) + ". ";
                    /*
                     * Profile has expired. This error message needs to be clearer hence
                     * we handle this as a special case.
                     */
                    msg += S2100ReturnCodeMsgMapping.getMessage(result);
                    final OcgTransactionException exception = new OcgTransactionException(msg, result);
                    if (LogSupport.isDebugEnabled(ctx))
                    {
                        LogSupport.debug(ctx, srcObj, "forwardTransactionToOcg Error", exception);
                    }
                    throw exception;
                }
            }
        }
        else if (amount < 0 || expiryExt > 0)
        {
            final boolean bExtendBalance = subType == SubscriberTypeEnum.PREPAID;
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(srcObj, "credit MSISDN[" + msisdn + "] amount[" + -amount + "] currency[" + currency
                        + "] bExtendBalance[" + bExtendBalance + "] nNumDaysToExtend[" + "] erReference[" + erReference
                        + "] subscriptionType [" + subscriptionType + "]", null).log(ctx);
            }
            result = client.requestCredit(msisdn, subType, -amount, currency, bExtendBalance, expiryExt, erReference,
                    subscriptionType.getId(), null, corbaBalance);
            if (result != ErrorCode.NO_ERROR)
            {
                if (rollback)
                {
                    final String msg = "Fail to rollback OCG  for MSISDN " + msisdn + ", amount " + amount
                            + ", result " + result + "." + S2100ReturnCodeMsgMapping.getMessage(result);
                    new MinorLogMsg(srcObj, msg, null).log(ctx);
                }
                else
                {                    
                    final String msg = "Failed to credit MSISDN " + msisdn + " for amount " + formatAmount(ctx, currency, -amount) + ". "
                            + S2100ReturnCodeMsgMapping.getMessage(result);
                    final OcgTransactionException exception = new OcgTransactionException(msg, result);
                    if (LogSupport.isDebugEnabled(ctx))
                    {
                        LogSupport.debug(ctx, srcObj, "forwardTransactionToOcg Error", exception);
                    }
                    throw exception;
                }
            }
        }
        else if (amount == 0)
        {
            /*
             * For all transactions that cost nothing (i.e. Activating a free service) the
             * running balance still has to be updated.
             */
            result = client.requestBalance(msisdn, subType, currency, false, erReference, subscriptionType.getId(),
                    corbaBalance, new LongHolder(), new LongHolder());
        }
        // Setting the balance.
        balance.setValue(corbaBalance.value);
        return result;
    }
   
    public boolean isExitoMvneLicensed(Context ctx) 
	{
		LicenseMgr lMgr = (LicenseMgr) ctx.get(LicenseMgr.class);
		return lMgr.isLicensed(ctx, "EXITO MVNE Deployment");     
	}
} // class
