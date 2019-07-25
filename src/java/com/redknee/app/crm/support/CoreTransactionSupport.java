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

import java.util.Collection;
import java.util.Date;

import com.redknee.app.crm.bean.SubscriberTypeEnum;
import com.redknee.app.crm.bean.core.SubscriptionType;
import com.redknee.app.crm.bean.core.Transaction;
import com.redknee.app.crm.xhome.home.OcgTransactionException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.holder.LongHolder;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;


/**
 * Provides support methods for dealing with Transactions.
 *
 * @author gary.anderson@redknee.com
 */
public interface CoreTransactionSupport extends Support
{
    /**
     * Returns the user currently logged in.
     *
     * @param context
     *            The operating context.
     * @return The user currently logged in.
     */
    public String getCsrIdentifier(final Context context);


    /**
     * Returns the Transaction home with the predicate and the date range applied to it.
     * If predicate is null, then sql must be "".
     *
     * @param context
     *            The operating context.
     * @param predicate
     *            The predicate to satisfy, in addition to the date constraint.
     * @param start
     *            The start date of the period (inclusive).
     * @param end
     *            The end date of the period (exclusive).
     * @return The transaction home.
     */
    public Home getTransactionsHome(final Context context, final Predicate predicate, final Date start, final Date end);


    /**
     * Gets the Transactions for the given predicate within the given period.
     *
     * @param context
     *            The operating context.
     * @param predicate
     *            The predicate to satisfy, in addition to the date constraint.
     * @param start
     *            The start date of the period (inclusive).
     * @param end
     *            The end date of the period (exclusive).
     * @return A collection of Transactions.
     */
    public Collection<Transaction> getTransactions(final Context context, final Predicate predicate, final Date start, final Date end);


    /**
     * Gets the Transactions for the given account within the given period.
     *
     * @param context
     *            The operating context.
     * @param accountIdentifier
     *            The Account identifier.
     * @param start
     *            The start date of the period (inclusive).
     * @param end
     *            The end date of the period (exclusive).
     * @return A collection of Transactions.
     */
    public Collection<Transaction> getTransactionsForAccount(final Context context,
            final String accountIdentifier, final Date start, final Date end);


    /**
     * Gets the Transactions for the given account within the given period.
     *
     * @param context
     *            The operating context.
     * @param accountIdentifier
     *            The Account identifier.
     * @param start
     *            The start date of the period (inclusive).
     * @param end
     *            The end date of the period (inclusive).
     * @return A collection of Transactions.
     */
    public Collection getTransactionsForAccountInclusive(final Context context, final String accountIdentifier,
            final Date start, final Date end);


    /**
     * Gets the Transactions for the given subscriber within the given period.
     *
     * @param context
     *            The operating context.
     * @param subscriberID
     *            The subscriber identifier.
     * @param start
     *            The start date of the period (inclusive).
     * @param end
     *            The end date of the period (exclusive).
     * @return A collection of Transactions.
     */
    public Collection<Transaction> getTransactionsForSubscriberID(final Context context,
            final String subscriberID, final Date start, final Date end);


    /**
     * Returns the transaction with the provided receipt number.
     *
     * @param ctx
     *            The operating context.
     * @param receiptNum
     *            Receipt number of the transaction.
     * @return The transaction with the provided receipt number.
     */
    public Transaction getTransaction(final Context ctx, final String receiptNum);


    /**
     * Determines if the Transaction is a Deposit.
     *
     * @param context
     *            The operating context.
     * @param transaction
     *            The given transaction.
     * @return True if the Transaction is a Deposit; False otherwise.
     */
    public boolean isDeposit(final Context context, final Transaction transaction);


    /**
     * Determines if the Transaction is a Payment.
     *
     * @param context
     *            The operating context.
     * @param transaction
     *            The given transaction.
     * @return True if the Transaction is a Payment; False otherwise.
     */
    public boolean isPayment(final Context context, final Transaction transaction);


    /**
     * Determines if the Transaction is a StandardPayment.
     *
     * @param context
     *            The operating context.
     * @param transaction
     *            The given transaction.
     * @return True if the Transaction is a StandardPayment; False otherwise.
     */
    public boolean isStandardPayment(final Context context, final Transaction transaction);


    /**
     * Determines if the Transaction is a Discount.
     *
     * @param ctx
     *            The operating context.
     * @param t
     *            The given transaction.
     * @return True if the transaction is a discount; false otherwise.
     */
    public boolean isDiscount(final Context ctx, final Transaction t);


    /**
     * Determines if the Transaction is a PaymentReversal.
     *
     * @param context
     *            The operating context.
     * @param transaction
     *            The given transaction.
     * @return True if the Transaction is a PaymentReversal; False otherwise.
     * @throws HomeException
     *             Thrown if there are problems determining whether this transaction is a
     *             payment reversal.
     */
    public boolean isPaymentReversal(final Context context, final Transaction transaction) throws HomeException;


    /**
     * Determines if the adjustment type of the provided transaction can be either credit
     * or debit.
     *
     * @param ctx
     *            The operating context.
     * @param trans
     *            The given transaction.
     * @return True if the adjustment type of the transaction can be either credit or
     *         debit; false otherwise.
     * @throws HomeException
     *             Thrown if there are problems looking up the transaction.
     */
    public boolean isAdjustTypeEither(final Context ctx, final Transaction trans) throws HomeException;


    /**
     * Determines if the Transaction is a BalanceTransfer.
     *
     * @param context
     *            The operating context.
     * @param transaction
     *            The given transaction.
     * @return True if the Transaction is a BalanceTransfer; False otherwise.
     */
    public boolean isBalanceTransfer(final Context context, final Transaction transaction);


    /**
     * Determines if the Transaction is a BalanceTransferDebit.
     *
     * @param context
     *            The operating context.
     * @param transaction
     *            The given transaction.
     * @return True if the Transaction is a BalanceTransferDebit; False otherwise.
     */
    public boolean isBalanceTransferDebit(final Context context, final Transaction transaction);
    

    /**
     * Checks if a transaction is a deposit release.
     *
     * @param context
     *            The operating context
     * @param transaction
     *            The transaction in question
     * @return <code>TRUE</code> if this is a deposit release transaction,
     *         <code>FALSE</code> otherwise
     */
    public boolean isDepositRelease(final Context context, final Transaction transaction);


    /**
     * Checks if a transaction is a payment converted from deposit.
     *
     * @param context
     *            The operating context
     * @param transaction
     *            The transaction in question
     * @return <code>TRUE</code> if this transaction is a payment converted from
     *         deposit, <code>FALSE</code> otherwise
     */
    public boolean isPaymentConvertedFromDeposit(final Context context, final Transaction transaction);


    /**
     * Checks if a transaction is a interest payment.
     *
     * @param context
     *            The operating context
     * @param transaction
     *            The transaction in question
     * @return <code>TRUE</code> if this is a interest payment transaction,
     *         <code>FALSE</code> otherwise
     */
    public boolean isInterestPayment(final Context context, final Transaction transaction);


    /**
     * Checks if a transaction is a payment plan transaction. Includes all adjustment
     * types under this category, Payment Plan Credit/Adjustment/Reversal/Allocation.
     *
     * @param ctx
     *            The operating context
     * @param trans
     *            The transaction in question
     * @return <code>TRUE</code> if this is a payment plan transaction,
     *         <code>FALSE</code> otherwise
     */
    public boolean isPaymentPlanLoanCategory(final Context ctx, final Transaction trans);


    /**
     * Determines if the Transaction is a payment plan loan credit.
     *
     * @param ctx
     *            The operating context.
     * @param trans
     *            The given transaction.
     * @return True if the transaction is a payment plan loan credit; false otherwise.
     */
    public boolean isPaymentPlanLoanCredit(final Context ctx, final Transaction trans);


    /**
     * Determines if the Transaction is a payment plan loan adjustment.
     *
     * @param ctx
     *            The operating context.
     * @param trans
     *            The given transaction.
     * @return True if the transaction is a payment plan loan adjustment; false otherwise.
     */
    public boolean isPaymentPlanLoanAdjustment(final Context ctx, final Transaction trans);


    /**
     * Determines if the Transaction is a payment plan adjustment.
     *
     * @param ctx
     *            The operating context.
     * @param trans
     *            The given transaction.
     * @return True if the transaction is a payment plan adjustment; false otherwise.
     */
    public boolean isPaymentPlanAdjustment(final Context ctx, final Transaction trans);


    /**
     * Determines if the Transaction is a payment plan loan payment.
     *
     * @param ctx
     *            The operating context.
     * @param trans
     *            The given transaction.
     * @return True if the transaction is a payment plan loan payment; false otherwise.
     */
    public boolean isPaymentPlanLoanPayment(final Context ctx, final Transaction trans);


    /**
     * Determines if the Transaction is a payment plan loan reversal.
     *
     * @param ctx
     *            The operating context.
     * @param trans
     *            The given transaction.
     * @return True if the transaction is a payment plan loan reversal; false otherwise.
     */
    public boolean isPaymentPlanLoanReversal(final Context ctx, final Transaction trans);


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
    public boolean isLateFeeCategory(final Context ctx, final Transaction trans);

    
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
    public boolean isEarlyPaymentCategory(final Context ctx, final Transaction trans);


    /**
     * Determines if the Transaction is a reactivation fee debit.
     *
     * @param ctx
     *            The operating context.
     * @param trans
     *            The given transaction.
     * @return True if the transaction is a reactivation fee debit; false otherwise.
     */
    public boolean isReactivationFeeDebit(final Context ctx, final Transaction trans);


    /**
     * Determines if the Transaction is an initial balance credit.
     *
     * @param ctx
     *            The operating context.
     * @param trans
     *            The given transaction.
     * @return True if the transaction is an initial balance credit; false otherwise.
     */
    public boolean isInitialBalanceCredit(final Context ctx, final Transaction trans);


    /**
     * Determines if the Transaction is a Notes.
     *
     * @param ctx
     *            The operating context.
     * @param trans
     *            The given transaction.
     * @return True if the transaction is a Notes; false otherwise.
     */
    public boolean isNotes(final Context ctx, final Transaction trans);


    /**
     * Sets the GL code for this particular transaction.
     *
     * @param ctx
     *            The operating context.
     * @param trans
     *            The transaction to be set.
     * @throws HomeException
     *             Thrown if there are problems looking up the adjustment type.
     */
    public void addTransactionGLCode(final Context ctx, final Transaction trans) throws HomeException;


    /**
     * Retrieves the transactions for a subscriber done on the specified adjustment.
     *
     * @param context
     *            The operating context.
     * @param subscriberID
     *            The subscriber identifier.
     * @param type
     *            The adjustment type.
     * @param condition
     *            Any additional condition.
     * @return a Collection with Transaction objects
     */
    public Collection<Transaction> getTransactionsForSubAdjustment(final Context context,
            final String subscriberID, final int type, final Object condition);


    /**
     * Retrieves the transactions for an account done on the specified adjustment.
     *
     * @param context
     *            The operating context.
     * @param ban
     *            The account identifier.
     * @param type
     *            The adjustment type.
     * @param condition
     *            Any additional condition.
     * @return a Collection with Transaction objects
     */
    public Collection<Transaction> getTransactionsForAccountAdjustment(final Context context, final String ban,
            final int type, final Object condition);


    /**
     * Creates a transaction.
     *
     * @param ctx
     *            The operating context.
     * @param transaction
     *            The transaction to be created.
     * @return The created transaction.
     * @throws HomeException
     *             Thrown if there are problems creating the transaction.
     */
    public Transaction createTransaction(final Context ctx, final Transaction transaction) throws HomeException;


    /**
     * Creates a transaction.
     *
     * @param ctx
     *            The operating context.
     * @param transaction
     *            The transaction to be created.
     * @param useFullPipeline
     *            Whether the full pipeline is used. If set to false, then the transaction
     *            pipeline in current context is used.
     * @return The created transaction.
     * @throws HomeException
     *             Thrown if there are problems creating the transaction.
     */
    public Transaction createTransaction(final Context ctx, final Transaction transaction,
            final boolean useFullPipeline) throws HomeException;


    /**
     * Returns the next available identifier.
     *
     * @param ctx
     *            The operating context.
     * @return The next available identifier.
     * @throws HomeException
     *             Thrown if there are problem retrieving the next available transaction
     *             identifier.
     */
    public long getNextIdentifier(final Context ctx) throws HomeException;


    /**
     * If the transaction is a payment coming from the TPS process returns the file name.
     *
     * @param ctx
     *            the operating context
     * @return the file name if exists an empty string otherwise
     */
    public String getTPSFileName(final Context ctx);


    /**
     * Looks up a transaction satisfying the provided criteria already exist.
     *
     * @param ctx
     *            The operating context.
     * @param filter
     *            The criteria to look up.
     * @return The duplicate of this transaction, or <code>null</code> if no duplicates
     *         exist.
     * @throws HomeException
     *             Thrown if there are problems looking up the transaction.
     */
    public Transaction findDuplicateTransaction(final Context ctx, final Predicate filter) throws HomeException;


    /**
     * Determines if a transaction satisfying the provided criteria already exist.
     *
     * @param ctx
     *            The operating context.
     * @param filter
     *            The criteria to look up.
     * @return Whether a transaction satisfying the provided criteria already exist.
     * @throws HomeException
     *             Thrown if there are problems looking up the transaction.
     */
    public boolean isDuplicateTransaction(final Context ctx, final Predicate filter) throws HomeException;


    /**
     * Determines if a transaction satisfying the provided criteria already exist.
     *
     * @param ctx
     *            The operating context.
     * @param accountNumber
     *            Account number of the transaction.
     * @param msisdn
     *            MSISDN of the transaction.
     * @param adjustmentType
     *            Adjustment type of the transaction.
     * @param amount
     *            The amount of the transaction.
     * @param dateTobeChecked
     *            The date of the transaction.
     * @return Whether a transaction satisfying the provided criteria already exist.
     * @throws HomeException
     *             Thrown if there are problems looking up the transaction.
     */
    public boolean isDuplicateTransaction(final Context ctx, final String accountNumber, final String msisdn,
            final int adjustmentType, final long amount, final Date dateTobeChecked) throws HomeException;

    /**
     * Forwards the transaction to OCG.
     *
     * @param ctx
     *            The operating context.
     * @param trans
     *            The transaction to be forwarded.
     * @param msisdn
     *            The MSISDN of the subscriber.
     * @param subType
     *            The subscriber paid type.
     * @param currency
     *            The currency used.
     * @param rollback
     *            Whether this is a rollback.
     * @param amount
     *            The amount charged.
     * @param srcObj
     *            The caller of this method.
     * @return OCG result code.
     * @throws HomeException
     *             Thrown if there are problems forwarding the transaction.
     */
    public int forwardTransactionToOcg(final Context ctx, final Transaction trans, final String msisdn, final SubscriberTypeEnum subType,
            final String currency, final boolean rollback, final long amount, final Object srcObj) throws HomeException, OcgTransactionException;


    /**
     * 
     * @param ctx
     * @param msisdn
     * @param amount
     * @param subscriptionType
     * @param currency
     * @param subType
     * @param srcObj
     * @param expiryExt
     * @param rollback
     * @return
     * @throws HomeException
     * @throws OcgTransactionException
     */
    public int forwardToOcg(final Context ctx, String msisdn, long amount, SubscriptionType subscriptionType,
            final String currency, final SubscriberTypeEnum subType, final Object srcObj, final short expiryExt,
            final boolean rollback) throws HomeException, OcgTransactionException;
    
    /**
     * 
     * @param ctx
     * @param msisdn
     * @param amount
     * @param subscriptionType
     * @param currency
     * @param subType
     * @param srcObj
     * @param expiryExt
     * @param rollback
     * @return
     * @throws HomeException
     * @throws OcgTransactionException
     */
    public int forwardToOcg(final Context ctx, String msisdn, long amount, SubscriptionType subscriptionType,
            final String currency, final SubscriberTypeEnum subType, final Object srcObj, final short expiryExt,
            final boolean rollback, final LongHolder balance) throws HomeException, OcgTransactionException;
} // class
