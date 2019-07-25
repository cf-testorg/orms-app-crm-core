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

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Date;

import com.redknee.framework.license.LicenseMgr;
import com.redknee.framework.xhome.auth.bean.User;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.GTE;
import com.redknee.framework.xhome.elang.LTE;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.msp.SpidAware;
import com.redknee.framework.xhome.visitor.CountingVisitor;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.CoreCrmConstants;
import com.redknee.app.crm.account.BANAware;
import com.redknee.app.crm.bean.AdjustmentTypeActionEnum;
import com.redknee.app.crm.bean.AdjustmentTypeEnum;
import com.redknee.app.crm.bean.PayeeEnum;
import com.redknee.app.crm.bean.core.AdjustmentType;
import com.redknee.app.crm.bean.core.Transaction;
import com.redknee.app.crm.bean.payment.PaymentPlan;
import com.redknee.app.crm.bean.payment.PaymentPlanActionEnum;
import com.redknee.app.crm.bean.payment.PaymentPlanHistory;
import com.redknee.app.crm.bean.payment.PaymentPlanHistoryHome;
import com.redknee.app.crm.bean.payment.PaymentPlanHistoryXInfo;
import com.redknee.app.crm.bean.payment.PaymentPlanHome;
import com.redknee.app.crm.bean.payment.PaymentPlanXInfo;


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
public class DefaultPaymentPlanSupport implements PaymentPlanSupport
{
    protected static PaymentPlanSupport instance_ = null;
    public static PaymentPlanSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultPaymentPlanSupport();
        }
        return instance_;
    }

    protected DefaultPaymentPlanSupport()
    {
    }


    /**
     * {@inheritDoc}
     */
    public boolean isEnabled(final Context ctx)
    {
        final Object offlineFlag = ctx.get(DISABLE_LICENSE_KEY);
        if (offlineFlag == null)
        {
            final LicenseMgr lMgr = (LicenseMgr) ctx.get(LicenseMgr.class);
            return lMgr.isLicensed(ctx, PAYMENT_PLAN_LICENSE_KEY);
        }
        else
        {
            // Skip License Lookup and enable Payment Plan.
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isHistoryEnabled(final Context ctx)
    {
        final LicenseMgr lMgr = (LicenseMgr) ctx.get(LicenseMgr.class);
        return !lMgr.isLicensed(ctx, PAYMENT_PLAN_HISTORY_DISABLE);
    }

    /**
     * {@inheritDoc}
     */
    public PaymentPlan getPaymentPlan(final Context ctx, final long paymentPlanID)
    {
        PaymentPlan paymentPlan = null;
        try
        {
            final Home paymentPlanHome = (Home) ctx.get(PaymentPlanHome.class);
            paymentPlan = (PaymentPlan) paymentPlanHome.find(ctx, new EQ(PaymentPlanXInfo.ID, Long
                .valueOf(paymentPlanID)));
        }
        catch (final HomeException hEx)
        {
            LogSupport.minor(ctx, DefaultPaymentPlanSupport.class, "Exception caught when looking up payment plan "
                + paymentPlanID, hEx);
        }
        return paymentPlan;

    }


    /**
     * {@inheritDoc}
     */
    public boolean isValidPaymentPlan(final Context ctx, final long paymentPlanID)
    {
        final PaymentPlan paymentplan = getPaymentPlan(ctx, paymentPlanID);

        return paymentplan != null;
    }


    /**
     * {@inheritDoc}
     */
    public String getPaymentPlanName(final Context ctx, final long paymentPlanID)
    {
        final PaymentPlan paymentplan = getPaymentPlan(ctx, paymentPlanID);

        if (paymentplan != null)
        {
            return paymentplan.getName();
        }
        return "";
    }


    /**
     * {@inheritDoc}
     */
    public int getPaymentPlanNumberOfPayments(final Context ctx, final long paymentPlanID)
    {
        final PaymentPlan paymentplan = getPaymentPlan(ctx, paymentPlanID);

        if (paymentplan != null)
        {
            return paymentplan.getNumOfPayments();
        }
        return PaymentPlan.DEFAULT_NUMOFPAYMENTS;
    }


    /**
     * {@inheritDoc}
     */
    public double getPaymentPlanCreditLimitLoweringFactor(final Context ctx, final long paymentPlanID)
        throws HomeException
    {
        final PaymentPlan paymentplan = getPaymentPlan(ctx, paymentPlanID);
        // percentage
        double clAdjustment = paymentplan.getCreditLimitDecrease();

        // A decrease by x% means (1-x/100) as a factor
        clAdjustment = 1 - clAdjustment * 0.01;
        return clAdjustment;
    }


    /**
     * {@inheritDoc}
     */
    public <T extends BANAware & SpidAware> Transaction createPaymentPlanLoanAdjustment(final Context ctx, final long amount,
        final T accountRef, final Date billingDate, final AdjustmentTypeActionEnum action) throws HomeException
    {
        final AdjustmentType paymentPlanChargeType = AdjustmentTypeSupportHelper.get(ctx).getAdjustmentType(ctx,
            AdjustmentTypeEnum.PaymentPlanLoanAdjustment);
        final String glCode = paymentPlanChargeType.getGLCodeForSPID(ctx, accountRef.getSpid());

        /* Make an account level "charge" for this. */
        /*
         * Creates a transaction that will charge subscribers the remaining Payment Plan
         * Loan Balance.
         */
        final Transaction transaction;
        try
        {
            transaction = (Transaction) XBeans.instantiate(Transaction.class, ctx);
        }
        catch (Exception exception)
        {
            throw new HomeException("Cannot instantiate transaction bean", exception);
        }

        transaction.setAcctNum(accountRef.getBAN());
        transaction.setAmount(amount);
        transaction.setTaxPaid(0);
        transaction.setAdjustmentType(paymentPlanChargeType.getCode());
        transaction.setAction(action);
        transaction.setGLCode(glCode);
        transaction.setReceiveDate(billingDate);
        transaction.setPayee(PayeeEnum.Account);
        transaction.setAgent(((User) ctx.get(Principal.class, new User())).getId());
        transaction.setSpid(accountRef.getSpid());

        if (LogSupport.isDebugEnabled(ctx))
        {
            LogSupport.debug(ctx, DefaultPaymentPlanSupport.class, "Creating Payment Plan loan adjustment transaction "
                + transaction);
        }

        final Home home = (Home) ctx.get(CoreCrmConstants.FULL_TRANSACTION_HOME);
        if (home == null)
        {
            throw new HomeException("System error: no TransactionHome found in context.");
        }
        return (Transaction) home.create(ctx, transaction);
    }

    /**
     * {@inheritDoc}
     */
    public long countPaymentPlanEnrollments(Context context,
            final String accountID,
            final Date startOfInterval, 
            final Date endOfInterval)
        throws HomeException
    {
        Home historyHome = (Home) context.get(PaymentPlanHistoryHome.class);
        And predicate = new And();
        predicate.add(new EQ(PaymentPlanHistoryXInfo.ACCOUNT_ID, accountID));
        predicate.add(new EQ(PaymentPlanHistoryXInfo.ACTION, PaymentPlanActionEnum.ENROLL));
        predicate.add(new GTE(PaymentPlanHistoryXInfo.RECORD_DATE, startOfInterval));
        predicate.add(new LTE(PaymentPlanHistoryXInfo.RECORD_DATE, endOfInterval));
        historyHome = historyHome.where(context, predicate);
        
        CountingVisitor visitor = new CountingVisitor();
        historyHome.forEach(visitor);
        return visitor.getCount();
    }

    /**
     * {@inheritDoc}
     */
    public Collection<PaymentPlanHistory> getLastEnrollments(Context context, 
            final String accountId, 
            final int num,
            final Date endOfInterval)
        throws HomeException
    {
        And filter = new And();
        
        filter.add(new EQ(PaymentPlanHistoryXInfo.ACCOUNT_ID, accountId));
        filter.add(new EQ(PaymentPlanHistoryXInfo.ACTION, PaymentPlanActionEnum.ENROLL));
        filter.add(new LTE(PaymentPlanHistoryXInfo.RECORD_DATE, endOfInterval));

        // Selections should already be in sorted order by Date since the Home is wrapped in a Sorting Home
        return HomeSupportHelper.get(context).getBeans(context, PaymentPlanHistory.class, filter, num);
    }
}
