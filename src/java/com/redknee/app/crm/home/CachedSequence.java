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
package com.redknee.app.crm.home;

import com.redknee.app.crm.support.MultiDbSupport;
import com.redknee.app.crm.support.MultiDbSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.xdb.sequence.OracleSequence;
import com.redknee.framework.xhome.xdb.sequence.Sequence;

/**
 * Cached Sequence Id Generator
 *
 * @author mangaraj.sahoo@redknee.com
 */
public class CachedSequence implements Sequence
{

    private Sequence idSequence_;
    
    private static final int MAX_INCREMENT = 200;
    private long increment = MAX_INCREMENT; 

    /**
     * This has to be set to MAX_INCREMENT because we must get the next 
     * sequence number stored in the database.
     */ 
    private long nextSeq_ = MAX_INCREMENT;

    /**
     * See if block in nextValue method.
     * Set to MAX_INCREMENT so that we don't need to get value during initialization.
     */ 
    private long maxSeq_ = MAX_INCREMENT;

    /**
     * Constructor with sequenceName as one of the parameters
     * 
     * @param ctx
     * @param delegate
     * @param sequenceName
     */
    public CachedSequence(final Context ctx, final String sequenceName)
    {
        this(ctx, sequenceName, MAX_INCREMENT);
    }
    
    public CachedSequence(final Context ctx, final String sequenceName, final int MAX_INCREMENT)
    {
        if (MultiDbSupportHelper.get(ctx).getDbsType(ctx) == MultiDbSupport.ORACLE)
        { 
            idSequence_ = new OracleSequence(ctx, sequenceName, 0, MAX_INCREMENT);
        }else 
        {
            idSequence_ = new CrmIdentifierSequence(ctx, sequenceName, MAX_INCREMENT); 
            this.increment = ((CrmIdentifierSequence)idSequence_).getIncrement(); 
        }
    }
    

    /**
     * @see com.redknee.framework.xhome.xdb.sequence.Sequence#nextValue(com.redknee.framework.xhome.context.Context)
     */
    public synchronized long nextValue(final Context ctx)
    {
        if (nextSeq_ >= maxSeq_)
        {
            nextSeq_ = idSequence_.nextValue(ctx);
            maxSeq_ = nextSeq_ + this.increment;
            return nextSeq_++;
        }
        else
        {
            //Return the current value, then increment it
            return nextSeq_++;
        }
    }


    /**
     * @see com.redknee.framework.xhome.xdb.sequence.Sequence#resetSequence(com.redknee.framework.xhome.context.Context)
     */
    public void resetSequence(final Context ctx)
    {
            nextSeq_ = 0;
            maxSeq_ = this.increment;
            idSequence_.resetSequence(ctx);
    }
    
    
    /**
     * @see com.redknee.framework.xhome.xdb.sequence.Sequence#dropSequence(com.redknee.framework.xhome.context.Context)
     */
    public void dropSequence(Context ctx)
    {
        idSequence_.dropSequence(ctx);
    }
}
