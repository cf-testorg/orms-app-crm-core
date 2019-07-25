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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.msp.SpidAware;

import com.redknee.app.crm.account.BANAware;
import com.redknee.app.crm.bean.AccountStateEnum;
import com.redknee.app.crm.bean.AdjustmentTypeActionEnum;
import com.redknee.app.crm.bean.core.Transaction;
import com.redknee.app.crm.bean.payment.PaymentPlan;
import com.redknee.app.crm.bean.payment.PaymentPlanHistory;


/**
 * This is a collection of utility methods for working with Payment Plan.
 *
 * The Payment Plan Feature requires the PAYMENT_PLAN_LICENSE_KEY to be installed in
 * the License Manager.
 * 
 * Once the Payment Plan feature is enabled, there is further configuration needed at the 
 * Service Provider level.
 * 
 * The Payment Plan has a history module that was added for CRM 7.7.  Since customers 
 * requested that version to be have no schema changes, we have disabled the Payment 
 * Plan History feature using a license: PAYMENT_PLAN_HISTORY_DISABLE.  This license
 * will disable the XDB installation of Payment Plan History Table, as well as 
 * bypass any queries into that table.  To enable Payment Plan History, we disable or 
 * delete the PAYMENT_PLAN_HISTORY_DISABLE license.  Once we move to a version that allows
 * schema changes we can deprecate the license PAYMENT_PLAN_HISTORY_DISABLE.
 *
 * @author angie.li@redknee.com
 */
public interface PaymentPlanSupport extends Support
{
    /**
     * Invalid payment plan ID.
     */
    public final long INVALID_PAYMENT_PLAN_ID = 0;

    /** License Manager key for enabling Payment Plan. * */
    public final String PAYMENT_PLAN_LICENSE_KEY = "Payment Plan";

    /** Flag to disable License Manager Lookup. For Unit Testing. */
    public final String DISABLE_LICENSE_KEY = "DisableLicenseLookup";
    /**  Flag to disable Payment Plan History Functionality **/
    public final String PAYMENT_PLAN_HISTORY_DISABLE = "Disable Payment Plan History";

    /**
     * The states from which we can change to Active once the account is enrolled in a
     * Payment Plan.
     */
    public final Collection<AccountStateEnum> STATES_TO_CHANGE_TO_ACTIVE =
        Collections.unmodifiableCollection(Arrays.asList(
                // AccountStateEnum.ACTIVE,
                AccountStateEnum.SUSPENDED, 
                AccountStateEnum.NON_PAYMENT_WARN,
                AccountStateEnum.NON_PAYMENT_SUSPENDED, 
                AccountStateEnum.PROMISE_TO_PAY,
                AccountStateEnum.IN_ARREARS, 
                AccountStateEnum.IN_COLLECTION));
    
    /**
     * Returns true if payment plan license is installed and enabled.
     *
     * @param ctx
     *            The operating context.
     * @return true if Payment Plan is enabled (with the License Manager). If the 
     * call is made "offline" then return True.
     */
    public boolean isEnabled(final Context ctx);

    /**
     * Returns if the Payment Plan History Feature has been enabled.
     * @param ctx
     * @return
     */
    public boolean isHistoryEnabled(final Context ctx);

    /**
     * Returns the payment plan with the provided identifier.
     *
     * @param ctx
     *            The operating context.
     * @param paymentPlanID
     *            Payment plan identifier.
     * @return The payment plan with the provided identifier, or <code>null</code> when
     *         none is found.
     */
    public PaymentPlan getPaymentPlan(final Context ctx, final long paymentPlanID);


    /**
     * Returns if Payment Plan is a valid Payment Plan.
     *
     * @param ctx
     *            the context used as locator
     * @param paymentPlanID
     *            payment plan ID used for look up
     * @return the result
     */
    public boolean isValidPaymentPlan(final Context ctx, final long paymentPlanID);


    /**
     * Returns the Payment Plan Name, or Blank String if not found.
     *
     * @param ctx
     *            the context used as locator
     * @param paymentPlanID
     *            payment plan ID used for look up
     * @return the result
     */
    public String getPaymentPlanName(final Context ctx, final long paymentPlanID);


    /**
     * Return the Payment Plan number of payments, or the Default if of not found.
     *
     * @param ctx
     *            The operating context.
     * @param paymentPlanID
     *            Payment plan ID.
     * @return Number of installments in the payment plan.
     */
    public int getPaymentPlanNumberOfPayments(final Context ctx, final long paymentPlanID);


    /**
     * Return the multiplier factor that will be applied to lower the credit limit of a
     * subscriber upon entering Payment Plan.
     *
     * @param ctx
     *            The operating context.
     * @param paymentPlanID
     *            Payment plan ID.
     * @return the multiplier factor that will be applied to lower the credit limit of a
     *         subscriber upon entering payment plan.
     * @throws HomeException
     *             when Payment Plan Home is not in the ctx.
     */
    public double getPaymentPlanCreditLimitLoweringFactor(final Context ctx, final long paymentPlanID)
        throws HomeException;


    /**
     * Create an account level Transaction for Payment Plan Loan Charge.
     *
     * @param ctx
     *            The operating context.
     * @param amount
     *            the amount of the charge
     * @param account
     *            the account to be charged
     * @param billingDate
     *            transaction date
     * @param action
     *            Adjustment action.
     * @return The created transaction.
     * @throws HomeException
     *             Thrown if there are problems creating the payment.
     */
    public <T extends BANAware & SpidAware> Transaction createPaymentPlanLoanAdjustment(final Context ctx, final long amount,
        final T accountRef, final Date billingDate, final AdjustmentTypeActionEnum action) throws HomeException;

    /**
     * Return the number of times the given account has enrolled in a Payment Plan within 
     * the given time frame (boundary dates inclusive).
     * @param context
     * @param accountID
     * @param startOfInterval
     * @param endOfInterval
     * @return
     * @throws HomeException
     */
    public long countPaymentPlanEnrollments(Context context,
            final String accountID,
            final Date startOfInterval, 
            final Date endOfInterval)
        throws HomeException;
    
    /**
     * Return the last X number of Payment Plan History "enrollment" records since the 
     * given date
     * @param num given number of enrollments to retrieve.
     * @param fromDate the record search period ends
     * @return The returned Array will have the Payment Plan History records ordered from most recent to furthest in the past.
     */
    public Collection<PaymentPlanHistory> getLastEnrollments(Context context, 
            final String accountId, 
            final int num,
            final Date endOfInterval)
        throws HomeException;
}
