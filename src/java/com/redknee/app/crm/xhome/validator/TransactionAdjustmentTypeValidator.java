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
package com.redknee.app.crm.xhome.validator;

import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.Transaction;
import com.redknee.app.crm.bean.TransactionXInfo;
import com.redknee.app.crm.support.AdjustmentTypeSupportHelper;


/**
 * Validates the Adjustment Type specified in a transaction is correct.
 * 
 * @author simar.singh@redknee.com
 * @since 22-April-08
 */
public class TransactionAdjustmentTypeValidator implements Validator
{

    /**
     * Create a new instance of <code>TransactionAdjustmentTypeValidator</code>.
     */
    protected TransactionAdjustmentTypeValidator()
    {
        // empty
    }


    /**
     * Returns an instance of <code>TransactionAdjustmentTypeValidator</code>.
     * 
     * @return An instance of <code>TransactionAdjustmentTypeValidator</code>.
     */
    public static TransactionAdjustmentTypeValidator instance()
    {
        if (instance == null)
        {
            instance = new TransactionAdjustmentTypeValidator();
        }
        return instance;
    }


    /**
     * {@inheritDoc}
     */
    public void validate(final Context context, final Object object) throws IllegalStateException
    {
        final CompoundIllegalStateException el = new CompoundIllegalStateException();
        if (object == null || !(object instanceof Transaction))
        {
            el.thrown(new IllegalArgumentException("A transaction must be supplied to this validator, it is the object to be validated"));
        }
        else
        {
            final Transaction transaction = (Transaction) object;
            final int adjustment = transaction.getAdjustmentType();
            try
            {
                if (null == AdjustmentTypeSupportHelper.get(context).getAdjustmentType(context, adjustment))
                {
                    el
                            .thrown(new IllegalPropertyArgumentException(
                                    TransactionXInfo.ADJUSTMENT_TYPE,
                                    "Adjustment type code ["
                                            + adjustment
                                            + "] could not be found. Please check the Adjustment Types allowed by the Service Provider"));
                }
            }
            catch (HomeException e)
            {
                new MinorLogMsg(this, "Error in fetching Adjustment Type code [" + adjustment + "].", e).log(context);
                el.thrown(e);
            }
        }
        el.throwAll();
    }

    /**
     * Singleton instance.
     */
    private static TransactionAdjustmentTypeValidator instance;
}
