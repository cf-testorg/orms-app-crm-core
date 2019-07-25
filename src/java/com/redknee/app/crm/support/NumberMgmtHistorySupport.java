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

import java.util.Date;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;


/**
 * 
 * @author Aaron Gourley
 * @since 7.5
 */
public interface NumberMgmtHistorySupport extends Support
{
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
        throws UnsupportedOperationException, HomeException;
    
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
        throws UnsupportedOperationException, HomeException;

    
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
        throws UnsupportedOperationException, HomeException;
}
