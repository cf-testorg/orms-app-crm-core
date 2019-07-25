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

import com.redknee.framework.xhome.context.Context;

/**
 * @author psperneac
 * @since Apr 29, 2005 4:57:27 PM
 */
public interface DestinationZoneSupport extends Support
{
    /** Finds the zone id of a msisdn */
    public  long getDestinationZoneId(Context ctx, String destMSISDN);
 
    /** Finds the zone id of a msisdn */
    public long getShortCodeDestinationZoneId(Context ctx, String destMSISDN, boolean isShortCode);


    /** Finds the zone description of a msisdn */
    public String getDestinationZoneDescription(Context ctx, String destMSISDN);
 

    /** Finds the zone description of a msisdn */
    public String getShortCodeDestinationZoneDescription(Context ctx, String destMSISDN, boolean isShortCode);
 
}
