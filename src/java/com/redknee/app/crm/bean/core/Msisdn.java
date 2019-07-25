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
package com.redknee.app.crm.bean.core;

import java.util.Date;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.GT;
import com.redknee.framework.xhome.elang.LTE;
import com.redknee.framework.xhome.elang.NEQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.MsisdnStateEnum;
import com.redknee.app.crm.numbermgn.HistoryEventSupport;
import com.redknee.app.crm.numbermgn.MsisdnMgmtHistory;
import com.redknee.app.crm.numbermgn.MsisdnMgmtHistoryHome;
import com.redknee.app.crm.numbermgn.MsisdnMgmtHistoryXInfo;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class Msisdn extends com.redknee.app.crm.bean.Msisdn implements ContextAware
{

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;


    /**
     * This is a temporary solution to migrate the subscriberId to MsisdnMgmtHistory.
     *
     * @return The subscriber ID.
     */
    public String getSubscriberID()
    {
        return getSubscriberID(getContext());
    }


    /**
     * Returns the subscriber ID of this MSISDN.
     *
     * @param ctx
     *            The operating context.
     * @return The subscriber associated with this MSISDN.
     */
    public String getSubscriberID(final Context ctx)
    {
        return getSubscriberID(ctx, null);
    }


    /**
     * Returns the IN subscription owning or last owned this MSISDN on the provided date.
     *
     * @param ctx
     *            The operating context.
     * @param date
     *            The date to look up.
     * @return The subscriber owning or last owned this MSISDN on the provided date.
     */
    public String getSubscriberID(final Context ctx, final Date date)
    {
        try
        {
            SubscriptionType subscriptionType = SubscriptionType.getINSubscriptionType(ctx);
            if(subscriptionType != null)
            {
                return getSubscriberID(ctx, subscriptionType.getId(), date);
            }
            else
            {
                new MajorLogMsg(this, "Unable to retrieve the IN SubscriptionType!  Verify that there is a IN SubscriptionType configured.  Returning null.", null).log(ctx);
            }
        }
        catch (HomeException e)
        {
            new MajorLogMsg(this, "Encountered a HomeException while trying to retrieve the default SubscriptionType.  Returning null.", e).log(ctx);
        }
        return null;
    }

    public String getSubscriberID(final Context ctx, long subscriptionTypeId)
    {
        return getSubscriberID(ctx, subscriptionTypeId, null);
    }

    /**
     * Returns the IN subscription owning or last owned this MSISDN on the provided date.
     *
     * @param ctx
     *            The operating context.
     * @param subscriptionTypeId
     *            The subscription type of the subscription to lookup.  Pass -1 to lookup all subscriptions.
     * @param date
     *            The date to look up.
     * @return The subscriber owning or last owned this MSISDN on the provided date.  Returns null if the MSISDN was never owned.
     */
    public String getSubscriberID(final Context ctx, long subscriptionTypeId, Date date)
    {
        /*
         * TODO [Cindy Wong] 2008-02-14: move this code into support class.
         */

        String result = "";
        /*
         * [Cindy Wong] 2008-02-14: AVAILABLE state should not short-circuit the date
         * logic. That piece of code has been removed.
         */
        if (date == null)
        {
            new DebugLogMsg(this, "Date is null. Will use current time to find the associated subscriberId for [msisdn=" + getMsisdn() + ",subscriptionTypeId=" + subscriptionTypeId + "]", null)
                    .log(ctx);
            date = new Date();
        }

        new DebugLogMsg(this, "Looking up the subscriber id from MsisdnMgmtHistory for [msisdn=" + getMsisdn() + ", subscriptionTypeId=" + subscriptionTypeId + "]...", null).log(ctx);
            
        Home historyHome = getMsisdnHistoryHome(ctx);

        final And condition = new And();
        condition.add(new EQ(MsisdnMgmtHistoryXInfo.TERMINAL_ID, getMsisdn()));
        condition.add(new EQ(MsisdnMgmtHistoryXInfo.EVENT, Long.valueOf(HistoryEventSupport.SUBID_MOD)));
        if(-1 != subscriptionTypeId)
        {
            condition.add(new EQ(MsisdnMgmtHistoryXInfo.SUBSCRIPTION_TYPE, subscriptionTypeId));
        }

        historyHome = historyHome.where(ctx, condition);
        try
        {
            final And and = new And();
            and.add(new LTE(MsisdnMgmtHistoryXInfo.TIMESTAMP, date));
            and.add(new GT(MsisdnMgmtHistoryXInfo.END_TIMESTAMP, date));

            MsisdnMgmtHistory history = (MsisdnMgmtHistory) historyHome.find(ctx, and);
            if (history != null)
            {
                new DebugLogMsg(this, "Found a record in MSISDN History " + history, null).log(ctx);
                result = history.getSubscriberId();
            }
            else
            {
                result = null;
            }
        }
        catch (final HomeException hEx)
        {
            new MinorLogMsg(this, "fail to look up subscriber id from MsisdnMgmtHistory", hEx).log(ctx);
            // noop
        }

        return result;
    }

    /**
     * Returns the IN subscription owning or last owned this MSISDN on the provided date.
     *
     * @param ctx
     *            The operating context.
     * @param subscriptionTypeId
     *            The subscription type of the subscription to lookup.  Pass -1 to lookup all subscriptions.
     * @param date
     *            The date to look up.
     * @return The subscriber owning or last owned this MSISDN on the provided date.  Returns null if the MSISDN was never owned.
     * 
     * If MSISDN is already own by one subscription type and other subscription type wants to aquire this msisdn then
     * this method doesn't allow it. 
     */
    public String getOthersubscriptionTypeSubscriberID(final Context ctx, long subscriptionTypeId, Date date)
    {

        String result = "";

        if (date == null)
        {
            new DebugLogMsg(this, "Date is null. Will use current time to find the associated subscriberId for [msisdn=" + getMsisdn() + ",subscriptionTypeId=" + subscriptionTypeId + "]", null)
                    .log(ctx);
            date = new Date();
        }

        new DebugLogMsg(this, "Looking up the subscriber id from MsisdnMgmtHistory for [msisdn=" + getMsisdn() + ", subscriptionTypeId=" + subscriptionTypeId + "]...", null).log(ctx);
            
        Home historyHome = getMsisdnHistoryHome(ctx);

        final And condition = new And();
        condition.add(new EQ(MsisdnMgmtHistoryXInfo.TERMINAL_ID, getMsisdn()));
        condition.add(new EQ(MsisdnMgmtHistoryXInfo.EVENT, Long.valueOf(HistoryEventSupport.SUBID_MOD)));
        if(-1 != subscriptionTypeId)
        {
            condition.add(new NEQ(MsisdnMgmtHistoryXInfo.SUBSCRIPTION_TYPE, subscriptionTypeId));
        }

        historyHome = historyHome.where(ctx, condition);
        try
        {
            final And and = new And();
            and.add(new LTE(MsisdnMgmtHistoryXInfo.TIMESTAMP, date));
            and.add(new GT(MsisdnMgmtHistoryXInfo.END_TIMESTAMP, date));

            MsisdnMgmtHistory history = (MsisdnMgmtHistory) historyHome.find(ctx, and);
            if (history != null)
            {
                new DebugLogMsg(this, "Found a record in MSISDN History " + history, null).log(ctx);
                result = history.getSubscriberId();
            }
            else
            {
                result = null;
            }
        }
        catch (final HomeException hEx)
        {
            new MinorLogMsg(this, "fail to look up subscriber id from MsisdnMgmtHistory", hEx).log(ctx);
            // noop
        }

        return result;
    }    

    /**
     * Claims the MSISDN.
     *
     * @param ctx
     *            The operating context.
     * @param ban
     *            The Account claiming the MSISDN.
     */
    public void claim(final Context ctx, final String ban)
    {
        setBAN(ban);
        setState(MsisdnStateEnum.IN_USE);
    }


    /**
     * Releases the MSISDN.
     */
    public void release()
    {
        setState(MsisdnStateEnum.HELD);
        setAMsisdn(false);
        setSubAuxSvcId(-1);
    }


    /**
     * Resets the MSISDN state.
     */
    public void reset()
    {
        setState(MsisdnStateEnum.AVAILABLE);
        setAMsisdn(false);
        setSubAuxSvcId(-1);
    }


    /**
     * Returns the history home.
     *
     * @param ctx
     *            The operating context.
     * @return MSISDN history home.
     */
    protected Home getMsisdnHistoryHome(final Context ctx)
    {
        return (Home) ctx.get(MsisdnMgmtHistoryHome.class);
    }


    /**
     * Returns history support class.
     *
     * @param ctx
     *            The operating context.
     * @return History support class.
     */
    protected HistoryEventSupport getHistoryEventSupport(final Context ctx)
    {
        return (HistoryEventSupport) ctx.get(HistoryEventSupport.class);
    }



    /**
     * {@inheritDoc}
     */
    public Context getContext()
    {
        return this.ctx_;
    }


    /**
     * {@inheritDoc}
     */
    public void setContext(final Context ctx)
    {
        this.ctx_ = ctx;
    }

    /**
     * The operating context.
     */
    protected transient Context ctx_;
}
