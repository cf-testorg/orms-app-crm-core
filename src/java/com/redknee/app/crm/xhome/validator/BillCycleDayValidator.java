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
package com.redknee.app.crm.xhome.validator;

import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.CoreCrmConstants;
import com.redknee.app.crm.bean.BillCycle;
import com.redknee.app.crm.bean.BillCycleXInfo;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 9.1
 */
public class BillCycleDayValidator implements Validator
{

    /**
     * {@inheritDoc}
     */
    public void validate(Context ctx, Object obj) throws IllegalStateException
    {
        CompoundIllegalStateException cise = new CompoundIllegalStateException();
        if (obj instanceof BillCycle)
        {
            BillCycle bc = (BillCycle) obj;
            
            if (bc.getDayOfMonth() == 0)
            {
                cise.thrown(new IllegalPropertyArgumentException(BillCycleXInfo.DAY_OF_MONTH, "Invalid bill cycle day"));
            }
            
            if (bc.getDayOfMonth() == CoreCrmConstants.SPECIAL_BILL_CYCLE_DAY
                    && (bc.getIdentifier() < CoreCrmConstants.AUTO_BILL_CYCLE_START_ID))
            {
                cise.thrown(new IllegalPropertyArgumentException(
                        BillCycleXInfo.DAY_OF_MONTH, 
                        "Bill Cycle day " + CoreCrmConstants.SPECIAL_BILL_CYCLE_DAY
                        + " is reserved for prepaid auto-bill cycles."));
            }
        }
        cise.throwAll();
    }

}
