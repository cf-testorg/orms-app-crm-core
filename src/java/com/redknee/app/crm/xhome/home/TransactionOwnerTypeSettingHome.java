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
package com.redknee.app.crm.xhome.home;

import java.text.MessageFormat;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.AdjustmentType;
import com.redknee.app.crm.bean.Transaction;
import com.redknee.app.crm.support.AdjustmentTypeSupportHelper;


/**
 * Provides a decorator for the Transaction Home pipeline that, upon creation,
 * ensures an owner type is configured for the Transaction.  If null, the owner
 * type is set to that of the associated AdjustmentType.  Transactions created
 * through the web UI will likely have the owner type set automatically by the
 * web controls, but programmatically created transactions might not.
 *
 * @author gary.anderson@redknee.com
 */
public
class TransactionOwnerTypeSettingHome
    extends HomeProxy
{
    /**
     * Creates a new decorator for the given Home delegate.
     *
     * @param context The operating context.
     * @param delegate The Home to which this decorator delegates.
     */
    public TransactionOwnerTypeSettingHome(final Context context, final Home delegate)
    {
        super(context, delegate);
    }


    /**
     * {@inheritDoc}
     */
    public Object create(final Context context, final Object obj)
        throws HomeException
    {
        if (obj instanceof Transaction)
        {
            final Transaction transaction = (Transaction)obj;
            ensureOwnerTypeSet(context, transaction);
        }

        return super.create(context, obj);
    }


    /**
     * Ensures that an owner type is configured for the Transaction.  If null,
     * the owner type is set to that of the associated AdjustmentType.
     *
     * @param context The operating context.
     * @param transaction The transaction to check and configure.
     *
     * @exception HomeException Thrown if there are problems accessing the
     * AdjustmentType Home data in the context.
     */
    private void ensureOwnerTypeSet(
        final Context context,
        final Transaction transaction)
        throws HomeException
    {
        debug(context, "Ensuring Transaction {0} has an owner type.", transaction);

        if (transaction.getOwnerType() != null)
        {
            debug(context, "Transaction {0} already has owner type {1}.", transaction);
            return;
        }

        debug(context, "Transaction {0} has no owner type set.", transaction);

        setOwnerTypeFromAdjustmentType(context, transaction);

        debug(context, "Transaction {0} now has owner type {1}.", transaction);
    }


    /**
     *  Sets the owner type of the Transaction to that of the associated
     *  AdjustmentType.
     *
     * @param context The operating context.
     * @param transaction The transaction to check and configure.
     *
     * @exception HomeException Thrown if there are problems accessing the
     * AdjustmentType Home data in the context.
     */
    private void setOwnerTypeFromAdjustmentType(
        final Context context,
        final Transaction transaction)
        throws HomeException
    {
        debug(context, "Getting AdjustmentType {2} for Transaction {0}.", transaction);

        final AdjustmentType adjustmentType =
            AdjustmentTypeSupportHelper.get(context).getAdjustmentType(
                context,
                transaction.getAdjustmentType());

        debug(context, "AdjustmentType {0} found with owner type {1}.", adjustmentType);

        transaction.setOwnerType(adjustmentType.getOwnerType());
    }


    /**
     * Provides a convenient way to log a debug message if debug mode is enabled
     * for Transactions.  Message template parameters are:
     * 0=Transaction.receiptNum, 1=Transaction.ownerType,
     * 2=Transaction.adjustmentType.
     *
     * @param context The operating context.
     * @param template A message template suitable for the Java MessageFormat
     * class.
     * @param transaction The transaction for which to embed information in the
     * message.
     */
    private void debug(
        final Context context,
        final String template,
        final Transaction transaction)
    {
        if (!LogSupport.isDebugEnabled(context))
        {
            return;
        }

        final String ownerType;
        if (transaction.getOwnerType() == null)
        {
            ownerType = "Unspecified";
        }
        else
        {
            ownerType = transaction.getOwnerType().toString();
        }

        final String msg = MessageFormat.format(template, Long.toString(transaction.getReceiptNum()),
                ownerType, Integer.toString(transaction.getAdjustmentType()));
        new DebugLogMsg(this, msg, null).log(context);
    }


    /**
     * Provides a convenient way to log a debug message if debug mode is enabled
     * for AdjustmentTypes.  Message template parameters are:
     * 0=AdjustmentType.code, 1=AdjustmentType.ownerType.
     *
     * @param context The operating context.
     * @param template A message template suitable for the Java MessageFormat
     * class.
     * @param adjustmentType The adjustmentType for which to embed information in the
     * message.
     */
    private void debug(
        final Context context,
        final String template,
        final AdjustmentType adjustmentType)
    {
        if (!LogSupport.isDebugEnabled(context))
        {
            return;
        }

        final String ownerType;
        if (adjustmentType.getOwnerType() == null)
        {
            ownerType = "Unspecified";
        }
        else
        {
            ownerType = adjustmentType.getOwnerType().toString();
        }

        new DebugLogMsg(this, MessageFormat.format(template, Integer.toString(adjustmentType.getCode()), ownerType),
                null).log(context);
    }


} // class
