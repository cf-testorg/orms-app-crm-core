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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.bean.IdentifierEnum;
import com.redknee.app.crm.bean.IdentifierSequence;
import com.redknee.app.crm.sequenceId.RollOverNotofiable;

/**
 * @author d.zhang@redknee.com
 */
public interface IdentifierSequenceSupport extends Support
{
    public static final String identifierDelimit = ".";

   
    public IdentifierSequence getIdentifierSequence(
            final Context context,
            final int spid,
            final String type)
        throws HomeException;


    public IdentifierSequence getIdentifierSequence(
            final Context context,
            final String type)
        throws HomeException;
    

    public IdentifierSequence createIdentifierSequence(final Context ctx,
            final int spid, final String seqId, final long start, final long end)
        throws HomeException;

    public IdentifierSequence createIdentifierSequence(final Context ctx,
            final int spid, final String seqId, final long start, final long end, final long next)
        throws HomeException;



    
    public void ensureSequenceExists(
            final Context context,
            final String identifier,
            final long startValue,
            final long endValue)
        throws HomeException;



    public void ensureSequenceExists(
            final Context context,
            final String identifier,
            final long startValue,
            final long endValue,
            final long nextValue)
        throws HomeException;

    public void ensureSequenceExists(
            final Context context,
            final String identifier,
            final long startValue,
            final long endValue,
            final long nextValue,
            final long increment)
        throws HomeException;
    
    public void updateIdentifierSequence(final Context ctx, final int spid,
            final String seqId, final long start, final long end)
        throws HomeException;


    
    public IdentifierSequence updateIdentifierSequence(
            final Context ctx,
            final String identifier,
            final long next)
        throws HomeException;

    public void removeIdentifierSequence(final Context ctx, final int spid, final String seqId)
        throws HomeException;

    public String getIdentifierSequenceName(final int spid, final String seqId);

    public String getSequenceId(final String seqIdentifier);

    public String getSpid(final String seqIdentifier);
    
    public long getNextIdentifier(final Context ctx, final String identifier,
            final RollOverNotofiable notifier) throws HomeException;

    public long getNextIdentifier(final Context ctx, final String identifier, final int spid,
            final RollOverNotofiable notifier)
        throws HomeException;
    
    public long peekNextIdentifier(
            final Context ctx,
            final String identifier)
        throws HomeException;
    /**
     * @param ctx
     * @param id
     * @throws HomeException
     */
    public void deleteIdentifierSequence(final Context ctx, final IdentifierSequence id)
        throws HomeException;

    /**
     * @param context
     * @param id
     * @param startingNumber
     * @param nextNumber
     */
    public void updateIdentifierSequence(final Context context,
            final IdentifierSequence id,
            final long startingNumber,
            final long nextNumber)
        throws HomeException;


    public void ensureNextIdIsLargeEnough(final Context ctx, final String identifier, final Home home)
    throws HomeException;
    
    /**
     * @deprecated
     * @param ctx
     * @param identifier
     * @param spid
     * @param notifier
     * @return
     * @throws HomeException
     */
    public long getNextIdentifier(final Context ctx, final IdentifierEnum identifier, final int spid,
            final RollOverNotofiable notifier)
        throws HomeException;
    
    /**
     * ensure the nextId value for the requird identifier seqence is larger than the
     * largest of the existing identifiers
     *@deprecated
     * @param ctx        The operating context
     * @param identifier The identifier of the sequence.
     * @param home       The home that is interest in using the identifier sequence
     */
    public void ensureNextIdIsLargeEnough(final Context ctx, final IdentifierEnum identifier, final Home home)
        throws HomeException;
    
    /**
     * Gets the next identifier for the given sequence.
     *@deprecated
     * @param context    The operating context.
     * @param identifier The identifier of the sequence.
     * @param notifier   The notifier used to signal roll-over.
     * @return The next available unique identifier for the sequence.
     * @throws HomeException Thrown if there is a problem accessing home
     *                       information in the operating context.
     */
    public long getNextIdentifier(
            final Context context,
            final IdentifierEnum identifier,
            final RollOverNotofiable notifier)
        throws HomeException;
    
    /**
     * @deprecated
     * @param ctx
     * @param identifier
     * @return
     * @throws HomeException
     */
    
    public long peekNextIdentifier(
            final Context ctx,
            final IdentifierEnum identifier)
        throws HomeException;
    
    
    /**
     * @deprecated
     * @param ctx
     * @param identifier
     * @param next
     * @return
     * @throws HomeException
     */
    public IdentifierSequence updateIdentifierSequence(
            final Context ctx,
            final IdentifierEnum identifier,
            final long next)
        throws HomeException;
    
    /**
     * Creates a new IdentifierSequence with the given values if one does not
     * already exist.
     *@deprecated
     * @param context    The operating context.
     * @param identifier The identifier of the sequence.
     * @param startValue The starting value of the sequence (and the next
     *                   available identifier).
     * @param endValue   The ending value of the sequence.
     * @param nextValue  The next available value in the sequence.
     * @throws HomeException Thrown if there is a problem accessing home
     *                       information in the operating context.
     */
    public void ensureSequenceExists(
            final Context context,
            final IdentifierEnum identifier,
            final long startValue,
            final long endValue,
            final long nextValue)
        throws HomeException;
    
    
    
    /**
     * Creates a new IdentifierSequence with the given values if one does not
     * already exist.
     *@deprecated
     * @param context    The operating context.
     * @param identifier The identifier of the sequence.
     * @param startValue The starting value of the sequence (and the next
     *                   available identifier).
     * @param endValue   The ending value of the sequence.
     * @throws HomeException Thrown if there is a problem accessing home
     *                       information in the operating context.
     */
    public void ensureSequenceExists(
            final Context context,
            final IdentifierEnum identifier,
            final long startValue,
            final long endValue)
        throws HomeException;
    
    /**
     * Gets the IdentifierSequence for the given sequence type (sequence is
     * assumed to be non-service provider specific).
     *@deprecated
     * @param context The operating context.
     * @param type    The type of the identifier sequence.
     */
    public IdentifierSequence getIdentifierSequence(
            final Context context,
            final IdentifierEnum type)
        throws HomeException;
    
    /**
     * Gets the IdentifierSequence for the given service provider and sequence
     * type.
     *@deprecated 
     * @param context The operating context.
     * @param spid    The identifier of the service provider.
     * @param type    The type of the identifier sequence.
     */
    public IdentifierSequence getIdentifierSequence(
            final Context context,
            final int spid,
            final IdentifierEnum type)
        throws HomeException;
    

    
    
    
}
