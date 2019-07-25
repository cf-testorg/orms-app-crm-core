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
package com.redknee.app.crm.support;

import java.util.Collection;
import java.util.Date;

import com.redknee.app.crm.bean.GSMPackageHome;
import com.redknee.app.crm.bean.GSMPackageXInfo;
import com.redknee.app.crm.bean.TDMAPackageHome;
import com.redknee.app.crm.bean.TDMAPackageXInfo;
import com.redknee.app.crm.numbermgn.GenericPackage;
import com.redknee.app.crm.numbermgn.HistoryEventSupport;
import com.redknee.app.crm.numbermgn.ImsiMgmtHistory;
import com.redknee.app.crm.numbermgn.ImsiMgmtHistoryHome;
import com.redknee.app.crm.numbermgn.ImsiMgmtHistoryXInfo;
import com.redknee.app.crm.numbermgn.LeastRecentVisitor;
import com.redknee.app.crm.numbermgn.MsisdnMgmtHistory;
import com.redknee.app.crm.numbermgn.MsisdnMgmtHistoryHome;
import com.redknee.app.crm.numbermgn.MsisdnMgmtHistoryXInfo;
import com.redknee.app.crm.technology.TechnologyEnum;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.GT;
import com.redknee.framework.xhome.elang.GTE;
import com.redknee.framework.xhome.elang.LTE;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;


public class DefaultNumberMgmtHistorySupport implements NumberMgmtHistorySupport
{

    protected static NumberMgmtHistorySupport instance_ = null;
    
    public static NumberMgmtHistorySupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultNumberMgmtHistorySupport();
        }
        return instance_;
    }

    protected DefaultNumberMgmtHistorySupport()
    {
    }
    
    /**
     * A reverse-lookup on MSISDN history.  Get the MSISDN of a given subscriber on a specified date.
     * 
     * @param ctx
     * @param subId
     * @param date
     * @return
     * @throws UnsupportedOperationException
     * @throws HomeException
     */
    public String lookupMsisdnBySubscriberIdFromMsisdnHistory(final Context ctx, final String subId, final Date date)
        throws UnsupportedOperationException, HomeException
    {
        Home msisdnHistoryHome = (Home) ctx.get(MsisdnMgmtHistoryHome.class);
        if (msisdnHistoryHome == null)
        {
            throw new HomeException("Could not get IMSI History home from context.");
        }
        
        msisdnHistoryHome = msisdnHistoryHome.where(
                ctx,
                new And()
                    .add(new EQ(MsisdnMgmtHistoryXInfo.SUBSCRIBER_ID, subId))
                    .add(new EQ(MsisdnMgmtHistoryXInfo.EVENT, Long.valueOf(HistoryEventSupport.SUBID_MOD))));
                
        if( date != null )
        {        
            // Lookup MSISDN History Table.  Return record with (Date >= MSISDN.StartDate AND Date <= MSISDN.EndDate)
            Collection c = msisdnHistoryHome.select(
                    ctx,
                    new And()
                    .add(new LTE(MsisdnMgmtHistoryXInfo.TIMESTAMP, date))
                    .add(new GT(MsisdnMgmtHistoryXInfo.END_TIMESTAMP, date)));

            if( c != null )
            {
                if( c.size() > 1 )
                {
                    new MinorLogMsg(NumberMgmtHistorySupport.class, 
                            c.size() + " MSISDNs owned by subscriber " + subId
                            + date
                            + ".  There should only be one.", null).log(ctx);
                }
                for( Object obj : c )
                {
                    if( obj instanceof MsisdnMgmtHistory )
                    {
                        MsisdnMgmtHistory history = (MsisdnMgmtHistory)obj;
                        new DebugLogMsg(NumberMgmtHistorySupport.class, "Returning MSISDN " + history.getTerminalId() + " for subscriber ID " + subId + " on " + date, null).log(ctx);
                        return history.getTerminalId();   
                    }
                }
            }
        }

        new DebugLogMsg(NumberMgmtHistorySupport.class, "No MSISDN found for subscriber " + subId + (date != null ? " on " + date : ""), null).log(ctx);
        return null;
    }
    
    /**
     * Get the subscriber that owned the IMSI on the given date
     * 
     * @param ctx
     * @param imsi
     * @param date
     * @return
     * @throws UnsupportedOperationException
     * @throws HomeException
     */
    public String lookupSubscriberIDFromImsiHistory(final Context ctx, final String imsi, final Date date)
        throws UnsupportedOperationException, HomeException
    {
        // HLD OID 38588, 38589
        // Look up IMSI table.  If no IMSI found, return error.
        GenericPackage gsmPackage = lookupPackageForIMSIOrMIN(ctx, TechnologyEnum.GSM, imsi);
        if( gsmPackage == null )
        {
            new MinorLogMsg(NumberMgmtHistorySupport.class, "No package found for IMSI " + imsi + " on " + date, null).log(ctx);
            return null;
        }
        
        Home imsiHistoryHome = (Home) ctx.get(ImsiMgmtHistoryHome.class);
        if (imsiHistoryHome == null)
        {
            throw new HomeException("Could not get IMSI History home from context.");
        }
        
        imsiHistoryHome = imsiHistoryHome.where(
                ctx,
                new And()
                    .add(new EQ(ImsiMgmtHistoryXInfo.TERMINAL_ID, imsi))
                    .add(new EQ(ImsiMgmtHistoryXInfo.EVENT, Long.valueOf(HistoryEventSupport.SUBID_MOD))));
        
        // See if the latest IMSI record is the right one
        // It will be if no date is given or the given date is later than the latest IMSI record's start date
        Collection c = imsiHistoryHome.select(
                ctx,
                new And().add(new LTE(ImsiMgmtHistoryXInfo.TIMESTAMP, date)).add(
                        (new GT(ImsiMgmtHistoryXInfo.END_TIMESTAMP, date))));
        if( c != null && c.size() > 0 )
        {
            if( c.size() > 1 )
            {
                new MinorLogMsg(NumberMgmtHistorySupport.class, 
                        c.size() + " subscribers own IMSI " + imsi
                        + (date != null ? " on " + date : "")
                        + ".  There should only be one.", null).log(ctx);
            }
            for( Object obj : c )
            {
                if( obj instanceof ImsiMgmtHistory )
                {
                    ImsiMgmtHistory history = (ImsiMgmtHistory)obj;
                    if( date == null 
                            || history.getTimestamp().getTime() <= date.getTime() )
                    {
                        // HLD OID 38590, 38592, 38593
                        // If current IMSI.SubID != null AND (Date = null OR Date >= IMSI.StartDate)
                        // Then return current IMSI.SubID
                        String subId = history.getSubscriberId();
                        new DebugLogMsg(NumberMgmtHistorySupport.class, "Returning subscriber ID " + subId + " as owner of IMSI " + imsi + " on " + date, null).log(ctx);
                        return subId;   
                    }
                }
            }   
        }
        
        if( date != null )
        {        
            // HLD OID 38595, 38596, 38597
            // Lookup IMSI History Table.  Return record with (Date >= IMSI.StartDate AND Date <= IMSI.EndDate)
            c = imsiHistoryHome.select(
                    ctx,
                    new And()
                    .add(new LTE(ImsiMgmtHistoryXInfo.TIMESTAMP, Long.valueOf(date.getTime())))
                    .add(new GT(ImsiMgmtHistoryXInfo.END_TIMESTAMP, Long.valueOf(date.getTime()))));

            if( c != null )
            {
                if( c.size() > 1 )
                {
                    new MinorLogMsg(NumberMgmtHistorySupport.class, 
                            c.size() + " subscribers own IMSI " + imsi
                            + date
                            + ".  There should only be one.", null).log(ctx);
                }
                for( Object obj : c )
                {
                    if( obj instanceof ImsiMgmtHistory )
                    {
                        String subId = ((ImsiMgmtHistory)obj).getSubscriberId();
                        new DebugLogMsg(NumberMgmtHistorySupport.class, "Returning subscriber ID " + subId + " as owner of IMSI " + imsi + " on " + date, null).log(ctx);
                        return subId;
                    }
                }
            }
        }

        new DebugLogMsg(NumberMgmtHistorySupport.class, "No subscriber owns IMSI " + imsi + (date != null ? " on " + date : ""), null).log(ctx);
        return null;
    }

    private GenericPackage lookupPackageForIMSIOrMIN(
            final Context context,
            final TechnologyEnum technology,
            final String imsiOrMin)
            throws HomeException
        {
            final Home home;
            final EQ where;
            
            if (TechnologyEnum.GSM == technology)
            {
                home = (Home)context.get(GSMPackageHome.class);
                where = new EQ(GSMPackageXInfo.IMSI, imsiOrMin);
            }
            else if (TechnologyEnum.TDMA == technology || TechnologyEnum.CDMA == technology)
            {
                home = (Home)context.get(TDMAPackageHome.class);
                where = new EQ(TDMAPackageXInfo.MIN, imsiOrMin);
            }
            else
            {
                throw new IllegalStateException("Unexpected technology type: " + technology);
            }

            final GenericPackage card = (GenericPackage)home.find(context, where);
            
            return card;
        }
    
    
    /**
     * Get the subscriber that owned the IMSI on date.  If no subscriber owned the MSISDN, get
     * the first subscriber that owned the IMSI after the given date. 
     * 
     * @param ctx
     * @param imsi
     * @param date
     * @return
     * @throws UnsupportedOperationException
     * @throws HomeException
     */
    public String lookupBestSubscriberIDFromImsiHistory(final Context ctx, final String imsi, final Date date)
        throws UnsupportedOperationException, HomeException
    {
        // HLD OID 38588, 38589
        // Look up IMSI table.  If no IMSI found, return error.
        GenericPackage gsmPackage = lookupPackageForIMSIOrMIN(ctx, TechnologyEnum.GSM, imsi);
        if( gsmPackage == null )
        {
            return null;
        }
        
        String subscriberId = lookupSubscriberIDFromImsiHistory(ctx, imsi, date);
        if( subscriberId != null )
        {
            return subscriberId;
        }
        
        // HLD OID 38598, 38599
        // If no record is found in the range, the following is returned:
        // - If Date < IMSI.StartDate of earliest IMSI history record, return the earliest IMSI.SubId 
        Home imsiHistoryHome = (Home) ctx.get(ImsiMgmtHistoryHome.class);
        imsiHistoryHome = imsiHistoryHome.where(
                ctx, 
                new And()
                    .add(new EQ(ImsiMgmtHistoryXInfo.TERMINAL_ID, imsi))
                    .add(new EQ(ImsiMgmtHistoryXInfo.EVENT, Long.valueOf(HistoryEventSupport.SUBID_MOD)))
                    .add(new GTE(ImsiMgmtHistoryXInfo.TIMESTAMP, Long.valueOf(date.getTime()))));

        // Assuming the date is earlier than any period in the ImsiMgmtHistory,
        // attempt to match with the earliest period instead
        final LeastRecentVisitor leastRecentVisitor = (LeastRecentVisitor) imsiHistoryHome.forEach(ctx,
                new LeastRecentVisitor(ImsiMgmtHistoryXInfo.TIMESTAMP));
        ImsiMgmtHistory history = (ImsiMgmtHistory) leastRecentVisitor.getValue();
        if (history != null)
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(NumberMgmtHistorySupport.class, "Return least recent IMSI history " + history + " for date " + date, null).log(ctx);
            }
            return history.getSubscriberId();
        }
        
        return null;
    }
}
