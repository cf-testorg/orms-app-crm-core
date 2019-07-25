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
import com.redknee.framework.xhome.context.ContextAgents;
import com.redknee.framework.xhome.context.ContextAgentProxy;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.web.search.SearchBorder;

import com.redknee.app.crm.bean.BillingOptionMapping;
import com.redknee.app.crm.bean.BillingOptionMappingXInfo;
import com.redknee.app.crm.bean.search.BillingOptionMappingSearch;
import com.redknee.app.crm.bean.search.BillingOptionMappingSearchWebControl;


/**
 * Provides search functionality for the Call Categorization.
 *
 * @author candy
 */
public class BillingOptionMappingSearchBorder
   extends SearchBorder
{
   /**
    * Creates a new BillingOptionMappingSearchBorder.
    *
    * @param context The operating context.
    */
   public BillingOptionMappingSearchBorder(Context context)
   {
      super(context, BillingOptionMapping.class, new BillingOptionMappingSearchWebControl());

      addAgent(new ContextAgentProxy()
            {
               public void execute(Context ctx)
                  throws AgentException
               {
                  BillingOptionMappingSearch criteria = (BillingOptionMappingSearch)getCriteria(ctx);

                  if (criteria.getRuleId() != -1 &&
                      null != doFind(ctx, new EQ(BillingOptionMappingXInfo.IDENTIFIER, Long.valueOf(criteria.getRuleId()))))
                  {
                     ContextAgents.doReturn(ctx);
                  }
                  delegate(ctx);
               }
            }
      );

      addAgent(new ContextAgentProxy()
            {
               public void execute(Context ctx)
                  throws AgentException
               {
                  BillingOptionMappingSearch criteria = (BillingOptionMappingSearch)getCriteria(ctx);

                  if (criteria.getSpid() != -1)
                  {
                     doSelect(
                        ctx, 
                        new EQ(BillingOptionMappingXInfo.SPID, Integer.valueOf(criteria.getSpid())));
                  }
                  delegate(ctx);
               }
            }
      );

      addAgent(new ContextAgentProxy()
            {
               public void execute(Context ctx)
                  throws AgentException
               {
                  BillingOptionMappingSearch criteria = (BillingOptionMappingSearch)getCriteria(ctx);

                  if (criteria.getZoneIdentifier() != -1)
                  {
                     doSelect(
                        ctx, 
                        new EQ(BillingOptionMappingXInfo.ZONE_IDENTIFIER, Long.valueOf(criteria.getZoneIdentifier())));
                  }
                  delegate(ctx);
               }
            }
      );
   }
} // class
