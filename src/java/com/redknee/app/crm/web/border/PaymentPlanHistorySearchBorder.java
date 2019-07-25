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

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgentProxy;
import com.redknee.framework.xhome.elang.GTE;
import com.redknee.framework.xhome.elang.LTE;
import com.redknee.framework.xhome.web.search.LimitSearchAgent;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;

import com.redknee.app.crm.bean.payment.PaymentPlanHistory;
import com.redknee.app.crm.bean.payment.PaymentPlanHistoryXInfo;
import com.redknee.app.crm.bean.search.PaymentPlanHistorySearch;
import com.redknee.app.crm.bean.search.PaymentPlanHistorySearchWebControl;
import com.redknee.app.crm.bean.search.PaymentPlanHistorySearchXInfo;

public class PaymentPlanHistorySearchBorder extends SearchBorder 
{

    public PaymentPlanHistorySearchBorder(Context context) 
    {
        super(context,PaymentPlanHistory.class,new PaymentPlanHistorySearchWebControl());

        addAgent(new SelectSearchAgent(PaymentPlanHistoryXInfo.ACCOUNT_ID, PaymentPlanHistorySearchXInfo.ACCOUNT_ID, true));

        // Date: Start of Period
        addAgent(new ContextAgentProxy() {
            @Override
            public void execute(Context ctx)
            throws AgentException
            {
                PaymentPlanHistorySearch criteria = (PaymentPlanHistorySearch) getCriteria(ctx);

                if ( criteria.getStartDate() != null )
                {
                    doSelect(ctx, new GTE(PaymentPlanHistoryXInfo.RECORD_DATE, criteria.getStartDate()));
                }

                delegate(ctx);
            }
        });
        
        // Date: End of Period
        addAgent(new ContextAgentProxy() {
            @Override
            public void execute(Context ctx)
            throws AgentException
            {
                PaymentPlanHistorySearch criteria = (PaymentPlanHistorySearch) getCriteria(ctx);

                if ( criteria.getEndDate() != null )
                {
                    doSelect(ctx, new LTE(PaymentPlanHistoryXInfo.RECORD_DATE, criteria.getEndDate()));
                }

                delegate(ctx);
            }
        });
        
        // Limit
        addAgent(new LimitSearchAgent(PaymentPlanHistorySearchXInfo.LIMIT));
    }

}
