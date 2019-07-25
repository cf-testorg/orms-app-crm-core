/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily 
 * available. Additionally, source code is, by its very nature, confidential 
 * information and inextricably contains trade secrets and other information 
 * proprietary, valuable and sensitive to Redknee, no unauthorised use, 
 * disclosure, manipulation or otherwise is permitted, and may only be used 
 * in accordance with the terms of the licence agreement entered into with 
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.move.processor;

import java.util.Date;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.move.MoveConstants;
import com.redknee.app.crm.move.MoveException;
import com.redknee.app.crm.move.MoveProcessor;
import com.redknee.app.crm.move.MoveRequest;


/**
 * Puts the start time of the move in the move context.  This is necessary when refunding subscriptions
 * after the MSISDN has been deassociated from them and claimed by the new account.  The transaction
 * home needs to be able to retrieve the subscription that was associated with a given MSISDN at the time
 * that the move started.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class StartTimeRecordingProcessor<MR extends MoveRequest> extends MoveProcessorProxy<MR>
{

    public StartTimeRecordingProcessor(MoveProcessor<MR> delegate)
    {
        super(delegate);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Context setUp(Context ctx) throws MoveException
    {
        Date startTime = new Date();
        return super.setUp(ctx).put(MoveConstants.MOVE_START_TIME_CTX_KEY, startTime);
    }

}
