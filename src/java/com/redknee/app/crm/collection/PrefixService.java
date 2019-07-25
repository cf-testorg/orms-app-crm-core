/*
 *  PrefixService
 *
 *  Author : Kevin Greer
 *  Date   : Nov 25, 2003
 *
 *  Copyright (c) Redknee, 2003
 *    - all rights reserved
 */
package com.redknee.app.crm.collection;

import com.redknee.app.crm.bean.BillingOptionMapping;
import com.redknee.framework.xhome.context.Context;

/** 
 * Given an MSISDN and a SPID, return a ZoneInfo based on prefix mappings defined in DestinationZoneHome. 
 */
public interface PrefixService
{
	//public BillingOptionMapping lookup(int spid, String msisdn, String billingOption);
	
	/**
	 * Returns the description for the zone in which the msisdn matches. If the matching prefix has a description
	 * it returns that one, if not it returns the zone description
	 * 
	 * @param ctx registry
	 * @param msisdn the msisdn to be matched
	 * @return the description of the zone. Empty string for no match.
	 */
	public String getZoneDescription(Context ctx,String msisdn);

	/**
	 * Returns the description for the zone in which the msisdn matches. If the matching prefix has a description
	 * it returns that one, if not it returns the zone description
	 * Method specific to SMS ERs
	 * @param ctx registry
	 * @param msisdn the msisdn to be matched
	 * @return the description of the zone. Empty string for no match.
	 */
	public String getShortCodeZoneDescription(Context ctx, String msisdn, boolean isShortCode);
	
	/**
	 * Returns the id of the zone in which the msisdn matches. 
	 * 
	 * @param ctx registry
	 * @param msisdn the msisdn to be matched
	 * @return the id of the zone or -1 for no match
	 */
	public long getZoneId(Context ctx,String msisdn);

	/**
	 * Returns the id of the zone in which the msisdn matches. 
	 * Method specific to SMS ERs
	 * @param ctx registry
	 * @param msisdn the msisdn to be matched
	 * @return the id of the zone or -1 for no match
	 */
    public long getShortCodeZoneId(Context ctx, String msisdn, boolean isShortCode);
}


