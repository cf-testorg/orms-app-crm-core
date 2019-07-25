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
package com.redknee.app.crm.web.border;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgentProxy;
import com.redknee.framework.xhome.context.ContextAgents;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;

import com.redknee.app.crm.bean.payment.PaymentPlan;
import com.redknee.app.crm.bean.payment.PaymentPlanXInfo;
import com.redknee.app.crm.bean.search.PaymentPlanSearch;
import com.redknee.app.crm.bean.search.PaymentPlanSearchWebControl;
import com.redknee.app.crm.bean.search.PaymentPlanSearchXInfo;

/**
 * @author albert
 * @since July 18, 2005 10:06:25 PM
 */
public class PaymentPlanSearchBorder extends SearchBorder
{
   public PaymentPlanSearchBorder(Context context)
   {
      super(context,PaymentPlan.class,new PaymentPlanSearchWebControl());

      addAgent(new ContextAgentProxy()
            {
               @Override
            public void execute(Context ctx)
                  throws AgentException
               {
                  PaymentPlanSearch criteria = (PaymentPlanSearch)getCriteria(ctx);

                  if (criteria.getPaymentPlanID() != -1 
                  		&& null != doFind(ctx, new EQ(PaymentPlanXInfo.ID, Long.valueOf(criteria.getPaymentPlanID()))))
                  {         	
                  	ContextAgents.doReturn(ctx);
                  }
                  delegate(ctx);
               }
            }
      );

      addAgent(new ContextAgentProxy()
            {
               @Override
            public void execute(Context ctx)
                  throws AgentException
               {
                  PaymentPlanSearch criteria = (PaymentPlanSearch)getCriteria(ctx);

                  if (criteria.getSpid() != -1)
                  {
                     doSelect(
                        ctx, 
                        new EQ(PaymentPlanXInfo.SPID, Integer.valueOf(criteria.getSpid())));
                  }
                  delegate(ctx);
               }
            }
      );
      

      addAgent(new WildcardSelectSearchAgent(PaymentPlanXInfo.NAME, PaymentPlanSearchXInfo.NAME, true));
   }
}