package com.redknee.app.crm.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.GTE;
import com.redknee.framework.xhome.elang.LT;
import com.redknee.framework.xhome.elang.LTE;
import com.redknee.framework.xhome.elang.NEQ;
import com.redknee.framework.xhome.elang.Or;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.InfoLogMsg;

import com.redknee.app.crm.bean.SubscriberTypeEnum;
import com.redknee.app.crm.bean.calldetail.BillingCategoryEnum;
import com.redknee.app.crm.bean.calldetail.CallDetail;
import com.redknee.app.crm.bean.calldetail.CallDetailHome;
import com.redknee.app.crm.bean.calldetail.CallDetailXInfo;
import com.redknee.app.crm.bean.calldetail.CallTypeEnum;
import com.redknee.util.snippet.log.Logger;


public class DefaultCallDetailSupport implements CallDetailSupport
{

    protected static CallDetailSupport instance_ = null;


    public static CallDetailSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultCallDetailSupport();
        }
        return instance_;
    }


    protected DefaultCallDetailSupport()
    {
    }


    /**
     * Gets the CallDetails for the given predicate within the given period.
     * 
     * @param context
     *            The operating context.
     * @param predicate
     *            The predicate to satisfy, in addition to the date constraint.
     * @param start
     *            The start date of the period (inclusive).
     * @param end
     *            The end date of the period (exclusive).
     * @param lastInvoiceDate
     *            The last invoice date.
     * @return A collection of CallDetails.
     */
    public Home getCallDetails(final Context context, final Object predicate, final Date start, final Date end,
            final Date lastInvoiceDate)
    {
        And condition = new And().add(predicate).add(getValidTimeWindowFilter(end, start, lastInvoiceDate));
        final Home callDetailHome = (Home) context.get(CallDetailHome.class);
        return callDetailHome.where(context, condition);
    }


    /**
     * Gets the CallDetails for the given account within the given period.
     * 
     * @param context
     *            The operating context.
     * @param accountIdentifier
     *            The Account identifier.
     * @param start
     *            The start date of the period (inclusive).
     * @param end
     *            The end date of the period (exclusive).
     * @param previousInvoiceDate
     *            The previous invoice date.
     * @return A collection of CallDetails.
     * @deprecated Use getCallDetailsForAccountHome instead
     */
    @Deprecated
    public  Collection<CallDetail> getCallDetailsForAccount(final Context context,
            final String accountIdentifier, final Date start, final Date end, final Date previousInvoiceDate)
    {
        try
        {
            final Home home = getCallDetailsForAccountHome(context, accountIdentifier, start, end, previousInvoiceDate);
            return home.selectAll();
        }
        catch (final Exception e)
        {
            Logger.major(context, CallDetailSupport.class, "getCallDetailsForAccount: Exception " + e.getMessage(),
                    e);
            return new ArrayList<CallDetail>();
        }
    }


    /**
     * Get a home containing the call details of an account.
     * 
     * @param context
     *            The operating context.
     * @param accountIdentifier
     *            The account to look for.
     * @param start
     *            Start date.
     * @param end
     *            End date.
     * @param previousInvoiceDate
     *            Previous invoice date.
     * @return A home containing all the call details of the account satisfying the
     *         criteria.
     */
    private  Home getCallDetailsForAccountHome(final Context context, final String accountIdentifier,
            final Date start, final Date end, final Date previousInvoiceDate)
    {
        EQ where = new EQ(CallDetailXInfo.BAN, accountIdentifier);
        return getCallDetails(context, where, start, end, previousInvoiceDate);
    }


    /**
     * Gets the CallDetails for the given subscriber within the given period.
     * 
     * @param context
     *            The operating context.
     * @param subscriberID
     *            The subscriber's ID.
     * @param start
     *            The start date of the period (inclusive).
     * @param end
     *            The end date of the period (exclusive).
     * @param previousInvoiceDate
     *            Previous invoice date.
     * @return A collection of CallDetails.
     * @deprecated Use getCallDetailsForSubscriberIDHome Instead
     */
    @Deprecated
    public  Collection<CallDetail> getCallDetailsForSubscriberID(final Context context,
            final String subscriberID, final Date start, final Date end, final Date previousInvoiceDate)
    {
        try
        {
            final Home home = getCallDetailsForSubscriberIDHome(context, subscriberID, start, end, previousInvoiceDate);
            return home.selectAll();
        }
        catch (final Exception e)
        {
            Logger.major(context, CallDetailSupport.class, "getCallDetailsForSubscriberID: Exception "
                    + e.getMessage(), e);
            return new ArrayList<CallDetail>();
        }
    }


    /**
     * Get a home containing the call details of a subscriber.
     * 
     * @param context
     *            The operating context.
     * @param subscriberID
     *            Subscriber identifier.
     * @param start
     *            Start date.
     * @param end
     *            End date.
     * @param previousInvoiceDate
     *            Previous invoice date.
     * @return A home containing all the call details of the subscriber satisfying the
     *         criteria.
     */
    public  Home getCallDetailsForSubscriberIDHome(final Context context, final String subscriberID,
            final Date start, final Date end, final Date previousInvoiceDate)
    {
        Object where = getSubIDandVPNClause(subscriberID, false, "");
        return getCallDetails(context, where, start, end, previousInvoiceDate);
    }


    /**
     * Gets the CallDetails for the given subscriber within the given period without a
     * Last Invoice. Used for prepaid subscribers that have no invoice date.
     * 
     * @param context
     *            The operating context.
     * @param subscriberID
     *            Subscriber identifier.
     * @param startDate
     *            Start date.
     * @param endDate
     *            End date.
     * @return A collection of call details satisfying the criteria.
     */
    public  Collection<CallDetail> getCallDetailsInRangeForSubscriberID(final Context context,
            final String subscriberID, final Date startDate, final Date endDate)
    {
        final And where = new And();
        where.add(new EQ(CallDetailXInfo.SUBSCRIBER_ID, subscriberID));
        where.add(new EQ(CallDetailXInfo.VPN__DISCOUNT__ON, 0));
        where.add(new GTE(CallDetailXInfo.POSTED_DATE, startDate));
        where.add(new LTE(CallDetailXInfo.POSTED_DATE, endDate));

        final Home callDetailHome = (Home) context.get(CallDetailHome.class);
        try
        {
            return callDetailHome.select(context, where);
        }
        catch (final Exception e)
        {
            Logger.major(context, CallDetailSupport.class.getName(), "getCallDetailsInRangeForSubscriberID: Exception "
                    + e.getMessage(), e);
            return new ArrayList<CallDetail>();
        }
    }


    /**
     * Filters the the Call Details by Billing Options.
     * 
     * @param context
     *            The operating context.
     * @param subscriberID
     *            Subscriber identifier.
     * @param start
     *            Start date.
     * @param end
     *            End date.
     * @param previousInvoiceDate
     *            Previous invoice date.
     * @param category
     *            Billing category.
     * @param isVPNacct
     *            Whether this is a VPN account.
     * @param ban
     *            Account identifier.
     * @return A home containing all the call details satisfying the criteria.
     */
    @Deprecated //Use the other method instead with additional flags.
    public  Home getCallDetailsByBillingCategory(final Context context, final String subscriberID,
            final Date start, final Date end, final Date previousInvoiceDate, final BillingCategoryEnum category,
            final boolean isVPNacct, final String ban)
    {
        final And where = new And();
        where.add(getSubIDandVPNClause(subscriberID, isVPNacct, ban));
        where.add(new EQ(CallDetailXInfo.SUBSCRIBER_TYPE, SubscriberTypeEnum.POSTPAID));
        where.add(new EQ(CallDetailXInfo.BILLING_CATEGORY, category));
        if (category == BillingCategoryEnum.DOMESTIC || category == BillingCategoryEnum.INTERNATIONAL)
        {
            where.add(new NEQ(CallDetailXInfo.CALL_TYPE, CallTypeEnum.TERM));
        }
        where.add(new NEQ(CallDetailXInfo.CALL_TYPE, CallTypeEnum.DROPPED_CALL));
        return getCallDetails(context, where, start, end, previousInvoiceDate);
    }
    
    /**
     * Filters the the Call Details by Billing Options.
     * 
     * @param context
     *            The operating context.
     * @param subscriberID
     *            Subscriber identifier.
     * @param start
     *            Start date.
     * @param end
     *            End date.
     * @param previousInvoiceDate
     *            Previous invoice date.
     * @param category
     *            Billing category.
     * @param isVPNacct
     *            Whether this is a VPN account.
     * @param ban
     *            Account identifier.
     * @param includeDomesticIncomingCalls
     * 				whether to fetch domestic incoming calls or not.
     * @param includeInternationalIncomingCalls
     *				whether to fetch international incoming calls or not.
     * @return A home containing all the call details satisfying the criteria.
     */
    public  Home getCallDetailsByBillingCategory(final Context context, final String subscriberID,
            final Date start, final Date end, final Date previousInvoiceDate, final BillingCategoryEnum category,
            final boolean isVPNacct, final String ban, boolean includeDomesticIncomingCalls, boolean includeInternationalIncomingCalls)
    {
        final And where = new And();
        where.add(getSubIDandVPNClause(subscriberID, isVPNacct, ban));
        where.add(new EQ(CallDetailXInfo.SUBSCRIBER_TYPE, SubscriberTypeEnum.POSTPAID));
        where.add(new EQ(CallDetailXInfo.BILLING_CATEGORY, category));
        if (!includeDomesticIncomingCalls && category == BillingCategoryEnum.DOMESTIC )
        {
            where.add(new NEQ(CallDetailXInfo.CALL_TYPE, CallTypeEnum.TERM));
        }
        if (!includeInternationalIncomingCalls && category == BillingCategoryEnum.INTERNATIONAL)
        {
            where.add(new NEQ(CallDetailXInfo.CALL_TYPE, CallTypeEnum.TERM));
        }
        where.add(new NEQ(CallDetailXInfo.CALL_TYPE, CallTypeEnum.DROPPED_CALL));
        return getCallDetails(context, where, start, end, previousInvoiceDate);
    }


    /**
     * Filters the the Call Details by Call type.
     * 
     * @param context
     *            The operating context.
     * @param subscriberID
     *            Subscriber identifier.
     * @param start
     *            Start date.
     * @param end
     *            End date.
     * @param previousInvoiceDate
     *            Previous invoice date.
     * @param type
     *            Call type.
     * @param isVPNacct
     *            Whether this is a VPN account.
     * @param ban
     *            Account identifier.
     * @return A home containing all the call details satisfying the criteria.
     */
    public  Home getCallDetailsByCallType(final Context context, final String subscriberID, final Date start,
            final Date end, final Date previousInvoiceDate, final CallTypeEnum type, final boolean isVPNacct,
            final String ban)
    {
        final And where = new And();
        where.add(getSubIDandVPNClause(subscriberID, isVPNacct, ban));
        where.add(new EQ(CallDetailXInfo.SUBSCRIBER_TYPE, SubscriberTypeEnum.POSTPAID));
        where.add(new EQ(CallDetailXInfo.CALL_TYPE, type));
        return getCallDetails(context, where, start, end, previousInvoiceDate);
    }


    /**
     * Filters the the Call Details by Usage type.
     * 
     * @param context
     *            The operating context.
     * @param subscriberID
     *            Subscriber identifier.
     * @param start
     *            Start date.
     * @param end
     *            End date.
     * @param previousInvoiceDate
     *            Previous invoice date.
     * @param type
     *            Usage type.
     * @param additionalElang
     *            Additional condition.
     * @param isVPNacct
     *            Whether this is VPN account.
     * @param ban
     *            Account identifier.
     * @return A home containing all the call details satisfying the criteria.
     */
    public  Home getCallDetailsByUsageType(final Context context, final String subscriberID, final Date start,
            final Date end, final Date previousInvoiceDate, final Long type, final And additionalElang,
            final boolean isVPNacct, final String ban)
    {
        And where;
        if (additionalElang != null)
        {
            where = additionalElang;
        }
        else
        {
            where = new And();
        }
        where.add(getSubIDandVPNClause(subscriberID, isVPNacct, ban));
        where.add(new EQ(CallDetailXInfo.SUBSCRIBER_TYPE, SubscriberTypeEnum.POSTPAID));
        return getCallDetails(context, where, start, end, previousInvoiceDate);
    }


    /**
     * Get the Call Detail by the Call Detail ID.
     * 
     * @param context
     *            The operating context.
     * @param id
     *            Call detail ID.
     * @return The call detail with the provided identifier.
     */
    public  CallDetail getCallDetail(final Context context, final String id)
    {
        final Home callDetailHome = (Home) context.get(CallDetailHome.class);
        try
        {
            return (CallDetail) callDetailHome.find(context, Long.valueOf(id));
        }
        catch (final HomeException exception)
        {
            final String sql = " id = " + id;
            Logger.major(context, CallDetailSupport.class, "Failed getting Call Detail records for SQL: " + sql,
                    exception);
        }
        return null;
    }


    public Date getEarliestDate(final Context ctx, final String ban, final String subscriberId) throws HomeException
    {
        Date date = null;
        final And condition = new And();
        boolean haveToCallMin = false;
        if (ban != null && ban.trim().length() > 0)
        {
            condition.add(new EQ(CallDetailXInfo.BAN, ban));
            haveToCallMin = true;
        }
        if (subscriberId != null && subscriberId.trim().length() > 0)
        {
            condition.add(new EQ(CallDetailXInfo.SUBSCRIBER_ID, subscriberId));
            haveToCallMin = true;
        }
        // amedina : If BAN and Subscriber ID are both null don't do anything
        if (haveToCallMin)
        {
            final Object value = HomeSupportHelper.get(ctx).min(ctx, CallDetailXInfo.POSTED_DATE, condition);
            if (value != null && (value instanceof Double))
            {
                date = new Date(((Number) value).longValue());
            }
            else
            {
                new InfoLogMsg("CallDetailSupport", "Either there is no calldetail or value is not a Double ", null)
                        .log(ctx);
            }
        }
        if (Logger.isDebugEnabled(ctx))
        {
            Logger.debug(ctx, CallDetailSupport.class, "Earliest Call Detail Date for BAN [" + ban + "] and Sub ["
                    + subscriberId + "] is " + date);
        }
        return date;
    }


    /**
     * Finds out whether a CallDetail (with the given charged MSISDN, transaction date,
     * and call type) already exists in the CallDetail table.
     * 
     * @param context
     *            The operating context.
     * @param chargedMSISDN
     *            The given charged MSISDN to match.
     * @param transactionDate
     *            The given transaction date to match.
     * @param callType
     *            The given call type to match.
     * @return boolean True if such a CallDetail is found; false otherwise.
     * @deprecated
     */
    @Deprecated
    public  boolean isCallDetailFound(final Context context, final String chargedMSISDN,
            final Date transactionDate, final CallTypeEnum callType)
    {
        final Home callDetailHome = (Home) context.get(CallDetailHome.class);
        if (callDetailHome == null)
        {
            return false;
        }
        final Collection<CallDetail> callDetails;
        And clause = new And().add(new EQ(CallDetailXInfo.CHARGED_MSISDN, chargedMSISDN)).add(
                new EQ(CallDetailXInfo.TRAN_DATE, transactionDate)).add(new EQ(CallDetailXInfo.CALL_TYPE, callType));
        try
        {
            callDetails = callDetailHome.select(context, clause);
        }
        catch (final HomeException exception)
        {
            return false;
        }
        return !callDetails.isEmpty();
    }




    /**
     * Checks if an interval is included in another interval.
     * 
     * @param ctx
     *            The operating context.
     * @param start
     *            Start date.
     * @param end
     *            End date.
     * @param intervalStart
     *            Interval start date.
     * @param intervalEnd
     *            Interval end date.
     * @return Returns <code>true</code> if an interval is included in another interval,
     *         <code>false</code> otherwise.
     */
    public  boolean includes(final Context ctx, final Date start, final Date end, final Date intervalStart,
            final Date intervalEnd)
    {
        return !start.after(intervalStart) && !end.before(intervalEnd);
    }


    /**
     * Whether the call detail record is a roaming call.
     * 
     * @param ctx
     *            The operating context.
     * @param cdr
     *            Call detail record.
     * @return Returns <code>true</code> if the call detail record is for a roaming call,
     *         <code>false</code> otherwise.
     */
    public  boolean isRoamingCDR(final Context ctx, final CallDetail cdr)
    {
        for (int i = 0; i < ROAMING_TYPES.length; ++i)
        {
            if (ROAMING_TYPES[i].equals(cdr.getCallType()))
            {
                return true;
            }
        }
        return false;
    }


    public  Object getValidTimeWindowFilter(final Date billingDate, final Date previousBillingDate,
            final Date previousInvoiceDate)
    {
        Or timeWindowCondition = new Or();
        And transCondition = new And();
        transCondition.add(new LT(CallDetailXInfo.TRAN_DATE, billingDate));
        transCondition.add(new GTE(CallDetailXInfo.TRAN_DATE, previousBillingDate));
        And postedDateCondition = new And();
        postedDateCondition.add(new GTE(CallDetailXInfo.POSTED_DATE, previousInvoiceDate));
        postedDateCondition.add(new LT(CallDetailXInfo.TRAN_DATE, previousBillingDate));
        timeWindowCondition.add(transCondition);
        timeWindowCondition.add(postedDateCondition);
        return timeWindowCondition;
    }


    /**
     * Return an Elang object that is suitable for finding a particular subscriber.
     * 
     * @param subId
     *            The subscriber's id
     * @param isVpnAcct
     *            Whether the account is a VPN account
     * @param vpnBan
     *            The BAN to be used for a VPN account
     * @return Elang object to be passed into a Home
     */
    public  Object getSubIDandVPNClause(String subId, boolean isVpnAcct, String vpnBan)
    {
        if (isVpnAcct)
        {
            return new And().add(new EQ(CallDetailXInfo.VPN__DISCOUNT__ON, 1)).add(
                    new EQ(CallDetailXInfo.SUBSCRIBER_ID, subId)).add(new EQ(CallDetailXInfo.VPN__BAN, vpnBan));
        }
        else
        {
            return new And().add(new EQ(CallDetailXInfo.VPN__DISCOUNT__ON, 0)).add(
                    new EQ(CallDetailXInfo.SUBSCRIBER_ID, subId));
        }
    }


    public void debugMsg(Class className, CallDetail cdr, String msg, Context ctx)
    {
        if (Logger.isDebugEnabled())
        {
            StringBuilder initialMsg = new StringBuilder(" [ ");
            if (cdr != null)
            {
                initialMsg.append(" ID=> ");
                initialMsg.append(cdr.getId());
                if (cdr.getSubscriberID() != null)
                {
                    initialMsg.append(" SUBID=> ");
                    initialMsg.append(cdr.getSubscriberID());
                }
                if (cdr.getOrigMSISDN() != null)
                {
                    initialMsg.append(" OrigMsisdn=> ");
                    initialMsg.append(cdr.getOrigMSISDN());
                }
            }
            else
            {
                initialMsg.append(" cdr = NULL ");
            }
            initialMsg.append("]");
            Logger.debug(ctx, className.getName(), initialMsg.toString() + " :: " + msg, null);
        }
    }
}
