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

import java.util.Date;

import com.redknee.app.crm.bean.IdentifierEnum;
import com.redknee.app.crm.bean.IdentifierSequence;
import com.redknee.app.crm.bean.IdentifierSequenceHome;
import com.redknee.app.crm.sequenceId.IncrementIdentifierCmd;
import com.redknee.app.crm.sequenceId.MaxIdentifierVisitor;
import com.redknee.app.crm.sequenceId.RollOverNotofiable;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

/**
 * @author d.zhang@redknee.com
 */
public class DefaultIdentifierSequenceSupport implements IdentifierSequenceSupport
{
    protected static IdentifierSequenceSupport instance_ = null;
    public static IdentifierSequenceSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultIdentifierSequenceSupport();
        }
        return instance_;
    }

    protected DefaultIdentifierSequenceSupport()
    {
    }

    /**
     * Gets the IdentifierSequenceHome from the context.
     *
     * @param context The operating context.
     * @return The IdentifierSequenceHome.
     * @throws HomeException Thrown if the IdentifierSequenceHome cannot be
     *                       found in the context.
     */
    private Home getIdentifierSequenceHome(final Context context)
        throws HomeException
    {
        final Home home = (Home) context.get(IdentifierSequenceHome.class);

        if (home == null)
        {
            throw new HomeException("IdentifierSequenceHome is not in the context!");
        }

        return home;
    }


    /**
     * Gets the IdentifierSequence for the given service provider and sequence
     * type.
     *
     * @param context The operating context.
     * @param spid    The identifier of the service provider.
     * @param type    The type of the identifier sequence.
     */
    public IdentifierSequence getIdentifierSequence(
            final Context context,
            final int spid,
            final String type)
        throws HomeException
    {
        return getIdentifierSequence(
                context,
                getIdentifierSequenceName(spid, type));
    }

    /**
     * Gets the IdentifierSequence for the given sequence descriptor.
     *
     * @param context    The operating context.
     * @param descriptor The descriptor of the identifier sequence.
     */
    public IdentifierSequence getIdentifierSequence(
            final Context context,
            final String descriptor)
        throws HomeException
    {
        final Home home = getIdentifierSequenceHome(context);
        return (IdentifierSequence) home.find(context, descriptor);
    }


    public IdentifierSequence createIdentifierSequence(final Context ctx,
            final int spid, final String seqId, final long start, final long end)
        throws HomeException
    {
        return createIdentifierSequence(ctx, spid, seqId, start, end, start);
    }

    public IdentifierSequence createIdentifierSequence(final Context ctx,
            final int spid, final String seqId, final long start, final long end, final long next)
        throws HomeException
    {
        final Home home = getIdentifierSequenceHome(ctx);
        if (home == null)
        {
            throw new HomeException("Identifier Sequence Home is not on context!");
        }
        final IdentifierSequence id = new IdentifierSequence();
        id.setIdentifier(getIdentifierSequenceName(spid, seqId));
        id.setStartNum(start);
        id.setEndNum(end);
        id.setNextNum(next);
        id.setLastModified(new Date());

        return (IdentifierSequence) home.create(ctx, id);
    }


    /**
     * Creates a new IdentifierSequence with the given values if one does not
     * already exist.
     *
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
            final String identifier,
            final long startValue,
            final long endValue)
        throws HomeException
    {
        ensureSequenceExists(context, identifier, startValue, endValue, startValue);
    }


    /**
     * Creates a new IdentifierSequence with the given values if one does not
     * already exist.
     *
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
            final String identifier,
            final long startValue,
            final long endValue,
            final long nextValue)
        throws HomeException
    {
        ensureSequenceExists(context, identifier, startValue, endValue, nextValue, 1); 
    }


    /**
     * Creates a new IdentifierSequence with the given values if one does not
     * already exist.
     *
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
            final String identifier,
            final long startValue,
            final long endValue,
            final long nextValue,
            final long increment)
        throws HomeException
    {
        final Home home = getIdentifierSequenceHome(context);

        if (home == null)
        {
            throw new HomeException("System error: no IdentifierSequenceHome in context.");
        }

        if (home.find(context, identifier) == null)
        {
            final IdentifierSequence sequence = new IdentifierSequence();
            sequence.setIdentifier(identifier);
            sequence.setStartNum(startValue);
            sequence.setEndNum(endValue);
            sequence.setNextNum(nextValue);
            sequence.setIncrement(increment); 
            
            home.create(context, sequence);
        }
    }

    public void updateIdentifierSequence(final Context ctx, final int spid,
            final String seqId, final long start, final long end)
        throws HomeException
    {
        final Home home = getIdentifierSequenceHome(ctx);
        if (home == null)
        {
            throw new HomeException("Identifier Sequence Home is not on context!");
        }
        final IdentifierSequence identifierSequence = (IdentifierSequence)
                home.find(ctx, getIdentifierSequenceName(spid, seqId));
        if (identifierSequence != null)
        {
            // do some validation here
            if (start > identifierSequence.getNextNum())
            {
                throw new HomeException("Start [" + start + "] can't be larger than the current sequence value:"
                        + identifierSequence.getNextNum());
            }
            if (end < identifierSequence.getNextNum())
            {
                throw new HomeException("End [" + end + "] can't be smaller than the current sequence value:"
                        + identifierSequence.getNextNum());
            }
            identifierSequence.setStartNum(start);
            identifierSequence.setEndNum(end);
            identifierSequence.setLastModified(new Date());
            home.store(ctx, identifierSequence);
        }
        else
        {
            // not exist
            createIdentifierSequence(ctx, spid, seqId, start, end);
        }
    }

    public IdentifierSequence updateIdentifierSequence(
            final Context ctx,
            final String identifier,
            final long next)
        throws HomeException
    {
        final Home home = getIdentifierSequenceHome(ctx);
        if (home == null)
        {
            throw new HomeException("Identifier Sequence Home is not on context!");
        }
        final IdentifierSequence idSeq = (IdentifierSequence) home.find(ctx, identifier);
        if (idSeq != null)
        {
            idSeq.setNextNum(next);
            return (IdentifierSequence) home.store(ctx, idSeq);
        }
        return null;
    }


    public void removeIdentifierSequence(final Context ctx, final int spid, final String seqId)
        throws HomeException
    {
        final Home home = getIdentifierSequenceHome(ctx);
        if (home == null)
        {
            throw new HomeException("Identifier Sequence Home is not on context!");
        }
        final IdentifierSequence identifierSequence = (IdentifierSequence)
                home.find(ctx, getIdentifierSequenceName(spid, seqId));
        if (identifierSequence != null)
        {
            home.remove(ctx, identifierSequence);
        }
    }

    public String getIdentifierSequenceName(final int spid, final String seqId)
    {
        return seqId +  IdentifierSequenceSupport.identifierDelimit + spid;
    }

    public String getSequenceId(final String seqIdentifier)
    {
        return seqIdentifier.substring(0, seqIdentifier.indexOf(IdentifierSequenceSupport.identifierDelimit));
    }

    public String getSpid(final String seqIdentifier)
    {
        return seqIdentifier.substring(1 + seqIdentifier.indexOf(IdentifierSequenceSupport.identifierDelimit));
    }


    public long getNextIdentifier(final Context ctx, final String identifier, final int spid,
            final RollOverNotofiable notifier)
        throws HomeException
    {
        return getNextIdentifier(ctx,
                getIdentifierSequenceName(spid, identifier), notifier);
    }

    /**
     * Gets the next identifier for the given sequence.
     *
     * @param context    The operating context.
     * @param identifier The identifier of the sequence.
     * @param notifier   The notifier used to signal roll-over.
     * @return The next available unique identifier for the sequence.
     */
    public long getNextIdentifier(final Context ctx, final String identifier,
            final RollOverNotofiable notifier) throws HomeException
    {
        final Home seqHome = getIdentifierSequenceHome(ctx);
        final IncrementIdentifierCmd cmd = new IncrementIdentifierCmd(identifier, notifier);
        return ((Long) (seqHome.cmd(ctx, cmd))).longValue();
    }
    
    
    public long peekNextIdentifier(
            final Context ctx,
            final String identifier)
        throws HomeException
    {
        final Home home = getIdentifierSequenceHome(ctx);
        synchronized (DefaultIdentifierSequenceSupport.class)
        {
            ensureSequenceExists(ctx, identifier, 1, Long.MAX_VALUE);

            final IdentifierSequence seq = (IdentifierSequence) home.find(ctx, identifier);
            if (seq != null)
            {
                return seq.getNextNum();
            }
            else
            {
                return 0;
            }
        }
    }

    /**
     * @param ctx
     * @param id
     * @throws HomeException
     */
    public void deleteIdentifierSequence(final Context ctx, final IdentifierSequence id)
        throws HomeException
    {
        final Home home = getIdentifierSequenceHome(ctx);
        if (home != null)
        {
            home.remove(ctx, id);
        }
    }

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
        throws HomeException
    {
        final Home home = getIdentifierSequenceHome(context);
        id.setStartNum(startingNumber);
        id.setNextNum(nextNumber);
        if (home != null)
        {
            home.store(context, id);
        }

    }


    /**
     * ensure the nextId value for the requird identifier seqence is larger than the
     * largest of the existing identifiers
     *
     * @param ctx        The operating context
     * @param identifier The identifier of the sequence.
     * @param home       The home that is interest in using the identifier sequence
     */
    public void ensureNextIdIsLargeEnough(final Context ctx, final String identifier, final Home home)
        throws HomeException
    {
        MaxIdentifierVisitor visitor = new MaxIdentifierVisitor();
        visitor = (MaxIdentifierVisitor) home.forEach(ctx, visitor);
        if (peekNextIdentifier(ctx, identifier) <= visitor.getMaxId())
        {
            updateIdentifierSequence(ctx, identifier, visitor.getMaxId() + 1);
        }
    }

    @Override
    public void ensureNextIdIsLargeEnough(Context ctx, IdentifierEnum identifier, Home home) throws HomeException
    {
        // TODO Auto-generated method stub
        ensureNextIdIsLargeEnough(ctx, identifier.getDescription(), home); 
    }

    @Override
    public void ensureSequenceExists(Context context, IdentifierEnum identifier, long startValue, long endValue,
            long nextValue) throws HomeException
    {
        // TODO Auto-generated method stub
        ensureSequenceExists(context, identifier.getDescription(), startValue, endValue, nextValue); 
    }

    @Override
    public void ensureSequenceExists(Context context, IdentifierEnum identifier, long startValue, long endValue)
            throws HomeException
    {
        // TODO Auto-generated method stub
        ensureSequenceExists(context, identifier.getDescription(), startValue, endValue); 
    }

    @Override
    public IdentifierSequence getIdentifierSequence(Context context, IdentifierEnum type) throws HomeException
    {
        // TODO Auto-generated method stub
        return getIdentifierSequence(context, type.getDescription());
    }

    @Override
    public IdentifierSequence getIdentifierSequence(Context context, int spid, IdentifierEnum type)
            throws HomeException
    {
        // TODO Auto-generated method stub
        return getIdentifierSequence(context, spid, type.getDescription());
    }

    @Override
    public long getNextIdentifier(Context ctx, IdentifierEnum identifier, int spid, RollOverNotofiable notifier)
            throws HomeException
    {
        // TODO Auto-generated method stub
        return getNextIdentifier(ctx, identifier.getDescription(), spid, notifier);
    }

    @Override
    public long getNextIdentifier(Context context, IdentifierEnum identifier, RollOverNotofiable notifier)
            throws HomeException
    {
        // TODO Auto-generated method stub
        return getNextIdentifier(context, identifier.getDescription(), notifier);
    }



    @Override
    public long peekNextIdentifier(Context ctx, IdentifierEnum identifier) throws HomeException
    {
        // TODO Auto-generated method stub
        return peekNextIdentifier(ctx, identifier.getDescription());
    }

    @Override
    public IdentifierSequence updateIdentifierSequence(Context ctx, IdentifierEnum identifier, long next)
            throws HomeException
    {
        // TODO Auto-generated method stub
        return updateIdentifierSequence(ctx, identifier.getDescription(), next);
    }
    
    
    
    
    

}
