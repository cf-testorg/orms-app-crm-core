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

import com.redknee.app.crm.bean.BillCycle;
import com.redknee.app.crm.bean.BillCycleXInfo;
import com.redknee.app.crm.bean.search.BillCycleSearch;
import com.redknee.app.crm.bean.search.BillCycleSearchWebControl;
import com.redknee.app.crm.bean.search.BillCycleSearchXInfo;

/**
 * @author candy
 * @since May 24, 2005 10:06:25 PM
 */
public class BillCycleSearchBorder extends SearchBorder
{
   public BillCycleSearchBorder(Context context)
   {
      super(context,BillCycle.class,new BillCycleSearchWebControl());

      addAgent(new ContextAgentProxy()
            {
               @Override
            public void execute(Context ctx)
                  throws AgentException
               {
                  BillCycleSearch criteria = (BillCycleSearch)getCriteria(ctx);

                  if (criteria.getBillCycleID() != -1 &&
                      null != doFind(ctx, new EQ(BillCycleXInfo.BILL_CYCLE_ID, Integer.valueOf(criteria.getBillCycleID()))))
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
                  BillCycleSearch criteria = (BillCycleSearch)getCriteria(ctx);

                  if (criteria.getSpid() != -1)
                  {
                     doSelect(
                        ctx, 
                        new EQ(BillCycleXInfo.SPID, Integer.valueOf(criteria.getSpid())));
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
                  BillCycleSearch criteria = (BillCycleSearch)getCriteria(ctx);

                  if (criteria.getDayOfMonth() != -1)
                  {
                     doSelect(
                        ctx, 
                        new EQ(BillCycleXInfo.DAY_OF_MONTH, Integer.valueOf(criteria.getDayOfMonth())));
                  }
                  delegate(ctx);
               }
            }
      );

      addAgent(new WildcardSelectSearchAgent(BillCycleXInfo.DESCRIPTION, BillCycleSearchXInfo.DESCRIPTION, true));
   }
}
