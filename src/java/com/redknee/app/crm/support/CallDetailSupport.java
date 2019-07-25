package com.redknee.app.crm.support;

import java.util.Collection;
import java.util.Date;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.bean.calldetail.BillingCategoryEnum;
import com.redknee.app.crm.bean.calldetail.CallDetail;
import com.redknee.app.crm.bean.calldetail.CallTypeEnum;


public interface CallDetailSupport extends Support
{
    /**
     * Different types of roaming calls.
     */
    public static final CallTypeEnum[] ROAMING_TYPES =
    {
        CallTypeEnum.ROAMING_MO, 
        CallTypeEnum.ROAMING_MT, 
        CallTypeEnum.ROAMING_SMS, 
        CallTypeEnum.ROAMING_TAX,
    };

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
    public Home getCallDetails(final Context context, 
                                      final Object predicate,
                                      final Date start, 
                                      final Date end, 
                                      final Date lastInvoiceDate);


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
    public Collection<CallDetail> getCallDetailsForAccount(
                                                final Context context,
                                                final String accountIdentifier, 
                                                final Date start, 
                                                final Date end, 
                                                final Date previousInvoiceDate);




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
    public Collection<CallDetail> getCallDetailsForSubscriberID(
                                                final Context context,
                                                final String subscriberID, 
                                                final Date start, 
                                                final Date end, 
                                                final Date previousInvoiceDate);

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
     * @return A home containing all the call details of the subscriber
     *         satisfying the criteria.
     */
    public Home getCallDetailsForSubscriberIDHome(
                                            final Context context, 
                                            final String subscriberID,
                                            final Date start, 
                                            final Date end, 
                                            final Date previousInvoiceDate);


    /**
     * Gets the CallDetails for the given subscriber within the given period
     * without a Last Invoice. Used for prepaid subscribers that have no invoice
     * date.
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
    public Collection<CallDetail> getCallDetailsInRangeForSubscriberID(
                                            final Context context,
                                            final String subscriberID, 
                                            final Date startDate, 
                                            final Date endDate);


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
    public Home getCallDetailsByBillingCategory(
                            final Context context, 
                            final String subscriberID,
                            final Date start, 
                            final Date end, 
                            final Date previousInvoiceDate, 
                            final BillingCategoryEnum category,
                            final boolean isVPNacct, 
                            final String ban);
    
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
    public Home getCallDetailsByBillingCategory(
                            final Context context, 
                            final String subscriberID,
                            final Date start, 
                            final Date end, 
                            final Date previousInvoiceDate, 
                            final BillingCategoryEnum category,
                            final boolean isVPNacct, 
                            final String ban,
                            final boolean includeDomesticIncomingCalls,
                            final boolean includeInternationalIncomingCalls
                            );


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
    public Home getCallDetailsByCallType(
                                final Context context, 
                                final String subscriberID, 
                                final Date start,
                                final Date end, 
                                final Date previousInvoiceDate, 
                                final CallTypeEnum type, 
                                final boolean isVPNacct,
                                final String ban);


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
    public Home getCallDetailsByUsageType(
                                final Context context, 
                                final String subscriberID, 
                                final Date start,
                                final Date end, 
                                final Date previousInvoiceDate, 
                                final Long type,
                                final And additionalElang, 
                                final boolean isVPNacct, 
                                final String ban);


    /**
     * Get the Call Detail by the Call Detail ID.
     *
     * @param context
     *            The operating context.
     * @param id
     *            Call detail ID.
     * @return The call detail with the provided identifier.
     */
    public CallDetail getCallDetail(
                                    final Context context, 
                                    final String id);
    
    public Date getEarliestDate(final Context ctx, final String ban, final String subscriberId) throws HomeException;

    /**
     * Finds out whether a CallDetail (with the given charged MSISDN,
     * transaction date, and call type) already exists in the CallDetail table.
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
    public boolean isCallDetailFound(
                                    final Context context, 
                                    final String chargedMSISDN,
                                    final Date transactionDate, 
                                    final CallTypeEnum callType);



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
     * @return Returns <code>true</code> if an interval is included in another
     *         interval, <code>false</code> otherwise.
     */
    public boolean includes(final Context ctx,
                                   final Date start,
                                   final Date end,
                                   final Date intervalStart,
                                   final Date intervalEnd);

    /**
     * Whether the call detail record is a roaming call.
     *
     * @param ctx
     *            The operating context.
     * @param cdr
     *            Call detail record.
     * @return Returns <code>true</code> if the call detail record is for a
     *         roaming call, <code>false</code> otherwise.
     */
    public boolean isRoamingCDR(final Context ctx, final CallDetail cdr);
    
    
    public Object getValidTimeWindowFilter(final Date billingDate,
            final Date previousBillingDate,
            final Date previousInvoiceDate);
    
    /**
     * Return an Elang object that is suitable for finding a particular 
     * subscriber.
     * 
     * @param subId 
     *                  The subscriber's id
     * @param isVpnAcct 
     *                  Whether the account is a VPN account
     * @param vpnBan 
     *                  The BAN to be used for a VPN account
     * @return Elang object to be passed into a Home
     */
    public Object getSubIDandVPNClause(String subId, 
                                               boolean isVpnAcct, 
                                               String vpnBan);
    

    public void debugMsg(Class className, CallDetail cdr, String msg, Context ctx);
}
