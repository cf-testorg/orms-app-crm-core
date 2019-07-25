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

package com.redknee.app.crm.web.border;

import com.redknee.framework.license.LicenseMgr;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgentProxy;
import com.redknee.framework.xhome.elang.Order;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.web.search.LimitSearchAgent;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xhome.xdb.ByDate;
import com.redknee.framework.xhome.xdb.Min;

import com.redknee.app.crm.bean.calldetail.CallDetail;
import com.redknee.app.crm.bean.calldetail.CallDetailXInfo;
import com.redknee.app.crm.bean.search.CallDetailSearch;
import com.redknee.app.crm.bean.search.CallDetailSearchTypeEnum;
import com.redknee.app.crm.bean.search.CallDetailSearchWebControl;
import com.redknee.app.crm.bean.search.CallDetailSearchXInfo;
import com.redknee.app.crm.filter.ByChargingSource;
import com.redknee.app.crm.filter.BySecondaryBalanceCharged;
/**
 * An Custom SearchBorder for Call Details information.
 *
 * This will be generated from an XGen template in the future but for now
 * I'm still experimenting with the design.  Also, some common helper classes
 * will be created for each Search type.
 *
 * Add this Border before the WebController, not as one of either its
 * Summary or Detail borders.
 *
 * @author paul.sperneac@redknee.com
 **/
public class CallDetailSearchBorder extends SearchBorder
{
	Context ctx = null;
    public CallDetailSearchBorder(final Context context, final WebControl webControl)
    {
        super(context, CallDetail.class, webControl);
        this.ctx = context;
        registerSearchAgents();
        
    }
    public CallDetailSearchBorder(final Context context)
    {
        super(context, CallDetail.class, new CallDetailSearchWebControl());
        this.ctx = context;
        registerSearchAgents();
    }


    public CallDetailSearchBorder(final Context context, final Object homeKey)
    {
        super(context, homeKey, CallDetail.class, new CallDetailSearchWebControl());
        this.ctx = context;
        registerSearchAgents();
    }

    private void registerSearchAgents()
    {
        addAgent(new SelectSearchAgent(CallDetailXInfo.SUBSCRIBER_ID, CallDetailSearchXInfo.SUBSCRIBER_ID));

        addAgent(new SelectSearchAgent(CallDetailXInfo.CHARGED_MSISDN, CallDetailSearchXInfo.CHARGED_MSISDN));

        addAgent(new LimitSearchAgent(CallDetailSearchXInfo.LIMIT));
        
        addAgent(new ContextAgentProxy()
        {
            @Override
            public void execute(final Context ctx) throws AgentException
            {
                final CallDetailSearch criteria = (CallDetailSearch) getCriteria(ctx);
                Order order = new Order();
                
                if (criteria.getSearchBy() == CallDetailSearchTypeEnum.TRANSACTION)
                {
                    order.add(CallDetailXInfo.TRAN_DATE, false);
                }
                else
                {
                    order.add(CallDetailXInfo.POSTED_DATE, false);
                }

                doSelect(ctx, order);

                delegate(ctx);
            }
        }
        );        
        
        addAgent(new ContextAgentProxy()
        {
            @Override
            public void execute(final Context ctx) throws AgentException
            {
                final CallDetailSearch criteria = (CallDetailSearch) getCriteria(ctx);
                doSelect(ctx, getPostedDateLogic(ctx, criteria));

                delegate(ctx);
            }
        }
        );
        //addAgent(new SelectSearchAgent(CallDetailXInfo.SECONDARY_BALANCE_INDICATOR, CallDetailSearchXInfo.SECONDARY_BALANCE_INDICATOR));
        //addAgent(new SelectSearchAgent(CallDetailXInfo.SECONDARY_BALANCE_CHARGED_AMOUNT, CallDetailSearchXInfo.SECONDARY_BALANCE_CHARGED_AMOUNT));
        LicenseMgr licenseManager = (LicenseMgr) ctx.get(LicenseMgr.class);
		
        if (licenseManager.isLicensed(ctx,"Prepaid Airtime Secondary Balance") ||
        		licenseManager.isLicensed(ctx,"Postpaid Airtime Secondary Balance")) {
	        addAgent(new ContextAgentProxy()
	        {
	            @Override
	            public void execute(final Context ctx) throws AgentException
	            {
	                final CallDetailSearch criteria = (CallDetailSearch) getCriteria(ctx);
	                ByChargingSource byChargingSource = new ByChargingSource(criteria.getSecondaryBalanceIndicator()); 
	                doSelect(ctx, byChargingSource);
	
	                delegate(ctx);
	            }
	        }
	        );
	        addAgent(new ContextAgentProxy()
	        {
	            @Override
	            public void execute(final Context ctx) throws AgentException
	            {
	                final CallDetailSearch criteria = (CallDetailSearch) getCriteria(ctx);
	                Min min = null;
	                BySecondaryBalanceCharged bySecondaryBalanceCharged = new BySecondaryBalanceCharged(criteria.getSecondaryBalanceChargedAmount()); 
	                doSelect(ctx, bySecondaryBalanceCharged);
	
	                delegate(ctx);
	            }
	        }
	        );
        }
    }

    /**
     * Creates the Logic object coresponding to the postedDate part of the search.
     * @param ctx
     * @param criteria
     * @return the Logic object
     */
    protected Object getPostedDateLogic(final Context ctx, final CallDetailSearch criteria)
    {
        ByDate dt = null;

        if (criteria.getSearchBy() == CallDetailSearchTypeEnum.TRANSACTION)
        {
            dt = new ByDate("tranDate");
        }
        else
        {
            dt = new ByDate("postedDate");
        }

        boolean bdt = false;
        if (criteria.getStartDate() != null)
        {
            dt.setStartInclusive(criteria.getStartDate().getTime());
            bdt = true;
        }

        if (criteria.getEndDate() != null)
        {
            dt.setEndInclusive(criteria.getEndDate().getTime());
            bdt = true;
        }

        return bdt ? dt : True.instance();
    }

}

