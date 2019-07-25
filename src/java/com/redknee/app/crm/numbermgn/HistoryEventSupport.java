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
package com.redknee.app.crm.numbermgn;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAwareSupport;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;


/**
 * Support class for History events.
 *
 * @author candy.wong@redknee.com
 */
public class HistoryEventSupport extends ContextAwareSupport
{

    /**
     * Feature modification event ID.
     */
    public static final long FEATURE_MOD = 1000L;

    /**
     * Package migration event ID.
     */
    public static final long PACKAGE_MIGRATION = 1001L;

    /**
     * Customer swap event ID.
     */
    public static final long CUSTOMER_SWAP = 1002L;

    /**
     * Subscriber name modification event ID.
     */
    public static final long SUBNAME_MOD = 1003L;

    /**
     * Subscriber address modification event ID.
     */
    public static final long SUBADDR_MOD = 1004L;

    /**
     * Subscriber ID modification event ID.
     */
    public static final long SUBID_MOD = 1005L;

    /**
     * Subscriber type modification event ID.
     */
    public static final long SUBTYPE_MOD = 1006L;


    /**
     * Account ID modification event ID.
     */
    public static final long BAN_MOD = 1007L;
    
    /**
     * Create a new instance of <code>HistoryEventSupport</code>.
     *
     * @param ctx
     *            The operating context.
     */
    public HistoryEventSupport(final Context ctx)
    {
        setContext(ctx);
    }


    /**
     * Returns the feature modification event.
     *
     * @param ctx
     *            The operating context.
     * @return The feature modification event object.
     * @throws HomeException
     *             Thrown if there are problems retrieving the event.
     */
    public HistoryEvent getFeatureModificationEvent(final Context ctx) throws HomeException
    {
        return getEvent(ctx, Long.valueOf(FEATURE_MOD));
    }


    /**
     * Returns the package migration event.
     *
     * @param ctx
     *            The operating context.
     * @return The package migration event object.
     * @throws HomeException
     *             Thrown if there are problems retrieving the event.
     */
    public HistoryEvent getPackageMigrationEvent(final Context ctx) throws HomeException
    {
        return getEvent(ctx, Long.valueOf(PACKAGE_MIGRATION));
    }


    /**
     * Returns the customer swap event.
     *
     * @param ctx
     *            The operating context.
     * @return The custom swap event object.
     * @throws HomeException
     *             Thrown if there are problems retrieving the event.
     */
    public HistoryEvent getCustomerSwapEvent(final Context ctx) throws HomeException
    {
        return getEvent(ctx, Long.valueOf(CUSTOMER_SWAP));
    }


    /**
     * Returns the subscriber name modification event.
     *
     * @param ctx
     *            The operating context.
     * @return The subscriber name modification object.
     * @throws HomeException
     *             Thrown if there are problems retrieving the event.
     */
    public HistoryEvent getSubNameModificationEvent(final Context ctx) throws HomeException
    {
        return getEvent(ctx, Long.valueOf(SUBNAME_MOD));
    }


    /**
     * Returns the subscriber address modification event.
     *
     * @param ctx
     *            The operating context.
     * @return The subscriber address modification object.
     * @throws HomeException
     *             Thrown if there are problems retrieving the event.
     */
    public HistoryEvent getSubAddrModificationEvent(final Context ctx) throws HomeException
    {
        return getEvent(ctx, Long.valueOf(SUBADDR_MOD));
    }


    /**
     * Returns the subscriber ID modification event.
     *
     * @param ctx
     *            The operating context.
     * @return The subscriber ID modification object.
     * @throws HomeException
     *             Thrown if there are problems retrieving the event.
     */
    public HistoryEvent getSubIdModificationEvent(final Context ctx) throws HomeException
    {
        return getEvent(ctx, Long.valueOf(SUBID_MOD));
    }


    /**
     * Returns the subscriber type modification event.
     *
     * @param ctx
     *            The operating context.
     * @return The subscriber type modification object.
     * @throws HomeException
     *             Thrown if there are problems retrieving the event.
     */
    public HistoryEvent getSubTypeModificationEvent(final Context ctx) throws HomeException
    {
        return getEvent(ctx, Long.valueOf(SUBTYPE_MOD));
    }

    /**
     * Returns the subscriber type modification event.
     *
     * @param ctx
     *            The operating context.
     * @return The subscriber type modification object.
     * @throws HomeException
     *             Thrown if there are problems retrieving the event.
     */
    public HistoryEvent getBANModificationEvent(final Context ctx) throws HomeException
    {
        return getEvent(ctx, Long.valueOf(BAN_MOD));
    }
    
    /**
     * Returns the requested event.
     *
     * @param ctx
     *            The operating context.
     * @param criteria
     *            The criteria to search for.
     * @return The requested object.
     * @throws HomeException
     *             Thrown if there are problems retrieving the event.
     */
    public HistoryEvent getEvent(final Context ctx, final Object criteria) throws HomeException
    {
        return (HistoryEvent) getHome(ctx).find(ctx, criteria);
    }


    /**
     * Returns the history event home.
     *
     * @param ctx
     *            The operating context.
     * @return Home of history event.
     */
    public Home getHome(final Context ctx)
    {
        return (Home) ctx.get(HistoryEventHome.class);
    }
}
