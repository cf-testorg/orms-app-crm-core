/*
 * Created on Apr 21, 2005
 *
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
package com.redknee.app.crm.bean.calldetail;

import com.redknee.app.crm.bean.BillingOptionMapping;
import com.redknee.app.crm.bean.DayCategoryEnum;
import com.redknee.app.crm.bean.FriendAndFamilyTypeEnum;
import com.redknee.app.crm.support.DestinationZoneSupportHelper;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.Visitor;


/**
 * This visitor iterates over all call categorization rules and determines whether the
 * call detail matches the particular rule.
 *
 * @author paul.sperneac@redknee.com
 * @since Apr 21, 2005 12:41:51 PM
 */
public class CallCategoryRulePicker implements Visitor
{

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Call detail to be categorized.
     */
    protected CallDetail callDetail;

    /**
     * The call categorization rule which matches the call detail.
     */
    protected BillingOptionMapping savedRule;


    /**
     * Create a new instance of <code>CallCategoryRulePicker</code>.
     *
     * @param cdr
     *            Call detail record.
     */
    public CallCategoryRulePicker(final CallDetail cdr)
    {
        setCallDetail(cdr);
        setSavedRule(null);
    }


    /**
     * @see com.redknee.framework.xhome.visitor.Visitor#visit(com.redknee.framework.xhome.context.Context,
     *      Object)
     */
    public void visit(final Context ctx, final Object obj) throws AgentException, AbortVisitException
    {
        final BillingOptionMapping rule = (BillingOptionMapping) obj;
        final CallDetail cdr = getCallDetail();

        if (cdr.getSpid() != rule.getSpid())
        {
            return;
        }

        if (cdr.getCallType() != rule.getCallType())
        {
            return;
        }

        // Adding calling party location filer
        if (!isCallingPartyMatch(rule, cdr))
        {
            return;
        }

        if (!isBillingOptionMatch(rule, cdr))
        {
            return;
        }

        if (!isTimeBandTypeMatch(rule, cdr))
        {
            return;
        }

        if (!isDayCategoryMatch(rule, cdr))
        {
            return;
        }

        if (!isFnfTypeMatch(rule, cdr))
        {
            return;
        }

        final long zoneId = getDestinationZoneId(ctx, cdr);

        if (!isDestinationZoneMatch(rule, cdr, zoneId))
        {
            return;
        }

        if (!isVpnTypeMatch(rule, cdr))
        {
            return;
        }

        setSavedRule(rule);

        if (rule.getCallingPartyLocation().equals(cdr.getCallingPartyLocation()))
        {
            throw new AbortVisitException("found it");
        }
    }


    /**
     * Determines whether the VPN type of the call detail matches the call categorization
     * rule.
     *
     * @param rule
     *            Call categorization rule.
     * @param cdr
     *            Call detail record.
     * @return Whether the VPN type of the call detail matches the call categorization
     *         rule.
     */
    private boolean isVpnTypeMatch(final BillingOptionMapping rule, final CallDetail cdr)
    {
        boolean match = false;
        // VPN type is ignored for Advanced Event.
        if (cdr.getCallType() == CallTypeEnum.ADVANCED_EVENT)
        {
            match = true;
        }
        else if (rule.getVpnType() == VPNCallTypeEnum.NONE || cdr.getVpn_Call_type() == rule.getVpnType())
        {
            match = true;
        }
        return match;
    }


    /**
     * Determines whether the destination zone of the call detail matches the call
     * categorization rule.
     *
     * @param rule
     *            Call categorization rule.
     * @param cdr
     *            Call detail record.
     * @param zoneId
     *            Matching destination zone ID of the call detail.
     * @return Whether the destination zone of the call detail matches the call
     *         categorization rule.
     */
    private boolean isDestinationZoneMatch(final BillingOptionMapping rule, final CallDetail cdr, final long zoneId)
    {
        boolean match = false;
        // Destination zone is ignored for Advanced Event.
        if (cdr.getCallType() == CallTypeEnum.ADVANCED_EVENT)
        {
            match = true;
        }
        else if (cdr.getDestMSISDN() == null || cdr.getDestMSISDN().trim().length() <= 0)
        {
            match = true;
        }
        else if (zoneId == rule.getZoneIdentifier())
        {
            match = true;
        }
        return match;
    }


    /**
     * Returns the destination zone ID of the call detail.
     *
     * @param ctx
     *            The operating context.
     * @param cdr
     *            Call detail record.
     * @return The destination zone ID of the call detail.
     */
    private long getDestinationZoneId(final Context ctx, final CallDetail cdr)
    {
        long zoneId;
        if (cdr.getCallType() != CallTypeEnum.ADVANCED_EVENT && 
            (cdr.getCallType() == CallTypeEnum.SMS || cdr.getCallType() == CallTypeEnum.ROAMING_SMS)
            && cdr.getDestMSISDN() != null)
        {
            if (cdr.getDestMSISDN().length() < 10)
            {
                zoneId = DestinationZoneSupportHelper.get(ctx).getShortCodeDestinationZoneId(ctx, cdr.getDestMSISDN(), true);
            }
            else
            {
                zoneId = DestinationZoneSupportHelper.get(ctx).getShortCodeDestinationZoneId(ctx, cdr.getDestMSISDN(), false);
            }
        }
        else
        {
            // TT#11053118072
            // Always returning the destination zone which is NOT a short code in case it's not an SMS
            zoneId = DestinationZoneSupportHelper.get(ctx).getShortCodeDestinationZoneId(ctx, cdr.getDestMSISDN(), false);
        }
        return zoneId;
    }


    /**
     * Determines whether the FnF type of the call detail matches the call categorization
     * rule.
     *
     * @param rule
     *            Call categorization rule.
     * @param cdr
     *            Call detail record.
     * @return Whether the FnF type of the call detail matches the call categorization
     *         rule.
     */
    private boolean isFnfTypeMatch(final BillingOptionMapping rule, final CallDetail cdr)
    {
        boolean match = false;
        // FnF Type is ignored for Advanced Event.
        if (cdr.getCallType() == CallTypeEnum.ADVANCED_EVENT)
        {
            match = true;
        }
        else if (matchFnf(rule.getFnfType(), cdr.getApplSpecialChargeInd()))
        {
            match = true;
        }
        return match;
    }


    /**
     * Determines whether the Day Category of the call detail matches the call
     * categorization rule.
     *
     * @param rule
     *            Call categorization rule.
     * @param cdr
     *            Call detail record.
     * @return Whether the Day Category of the call detail matches the call categorization
     *         rule.
     */
    private boolean isDayCategoryMatch(final BillingOptionMapping rule, final CallDetail cdr)
    {
        boolean match = false;
        // Day Category is ignored for Advanced Event.
        if (cdr.getCallType() == CallTypeEnum.ADVANCED_EVENT)
        {
            match = true;
        }
        else if (rule.getDayCategory() == DayCategoryEnum.ANY || cdr.getDayCategory() == rule.getDayCategory())
        {
            match = true;
        }
        return match;
    }


    /**
     * Determines whether the Time Band type of the call detail matches the call
     * categorization rule.
     *
     * @param rule
     *            Call categorization rule.
     * @param cdr
     *            Call detail record.
     * @return Whether the Time Band type of the call detail matches the call
     *         categorization rule.
     */
    private boolean isTimeBandTypeMatch(final BillingOptionMapping rule, final CallDetail cdr)
    {
        boolean match = false;
        // Time Band is ignored for Advanced Event.
        if (cdr.getCallType() == CallTypeEnum.ADVANCED_EVENT)
        {
            match = true;
        }
        else if (ANY_TIME_BAND.equals(rule.getTimeBandType()) || cdr.getTimeBandType().equals(rule.getTimeBandType()))
        {
            match = true;
        }
        return match;
    }


    /**
     * Determines whether the Billing Option of the call detail matches the call
     * categorization rule.
     *
     * @param rule
     *            Call categorization rule.
     * @param cdr
     *            Call detail record.
     * @return Whether the Billing Option of the call detail matches the call
     *         categorization rule.
     */
    private boolean isBillingOptionMatch(final BillingOptionMapping rule, final CallDetail cdr)
    {
    	boolean match = false;
    	
    	String billingOptionRegex = rule.getBillingOption();
        if (billingOptionRegex == null)
        {
        	match = true;
        }
        else 
        {
	        String billingOptionInCdr = cdr.getBillingOption();
	        if (billingOptionInCdr != null)
	        {
	        	String regex = billingOptionRegex.replaceAll("\\*", "(.)*");
	        	match = billingOptionInCdr.trim().matches(regex.trim());
	        }
        }
        
        return match;
    }


    /**
     * Determines whether the Calling Party Location of the call detail matches the call
     * categorization rule.
     *
     * @param rule
     *            Call categorization rule.
     * @param cdr
     *            Call detail record.
     * @return Whether the Calling Party Location of the call detail matches the call
     *         categorization rule.
     */
    private boolean isCallingPartyMatch(final BillingOptionMapping rule, final CallDetail cdr)
    {
        boolean match = true;

        if(!cdr.getCallingPartyLocation().equals(rule.getCallingPartyLocation()))
        {
            if ( rule.getCallingPartyLocation().trim().length() > 0)
            {
                match = false;
            }
            else if (this.getSavedRule() != null)
            {
                match = false;
            }
        }
        return match;
    }


    /**
     * Returns whether FnF type matches.
     *
     * @param fnfType
     *            FnF type.
     * @param applSpecialChargeInd
     *            Which special charge was applied.
     * @return Whether the special charge types matches the FnF type.
     */
    private boolean matchFnf(final FriendAndFamilyTypeEnum fnfType,
        final AppliedSpecialChargeIndicatorEnum applSpecialChargeInd)
    {
        if (applSpecialChargeInd.getIndex() == AppliedSpecialChargeIndicatorEnum.NOT_AVAILABLE_INDEX
            && fnfType.getIndex() == FriendAndFamilyTypeEnum.NONE_INDEX)
        {
            return true;
        }

        if ((applSpecialChargeInd.getIndex() == AppliedSpecialChargeIndicatorEnum.PLP_RATE_PLAN_INDEX || applSpecialChargeInd
            .getIndex() == AppliedSpecialChargeIndicatorEnum.PLP_PERCENT_DISCOUNT_INDEX)
            && fnfType.getIndex() == FriendAndFamilyTypeEnum.PLP_INDEX)
        {
            return true;
        }

        if ((applSpecialChargeInd.getIndex() == AppliedSpecialChargeIndicatorEnum.CUG_RATE_PLAN_INDEX || applSpecialChargeInd
            .getIndex() == AppliedSpecialChargeIndicatorEnum.CUG_PERCENT_DISCOUNT_INDEX)
            && fnfType.getIndex() == FriendAndFamilyTypeEnum.CUG_INDEX)
        {
            return true;
        }

        return false;
    }


    /**
     * @return Returns the callDetail.
     */
    public CallDetail getCallDetail()
    {
        return callDetail;
    }


    /**
     * @param callDetail
     *            The callDetail to set.
     */
    public void setCallDetail(final CallDetail callDetail)
    {
        this.callDetail = callDetail;
    }


    /**
     * @return Returns the savedRule.
     */
    public BillingOptionMapping getSavedRule()
    {
        return savedRule;
    }


    /**
     * @param savedRule
     *            The savedRule to set.
     */
    public void setSavedRule(final BillingOptionMapping savedRule)
    {
        this.savedRule = savedRule;
    }
    
    private static String ANY_TIME_BAND = "ANY";

}
