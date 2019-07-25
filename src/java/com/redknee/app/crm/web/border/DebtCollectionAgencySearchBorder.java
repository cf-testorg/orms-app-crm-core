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

import com.redknee.app.crm.bean.ChargingTemplateSearch;
import com.redknee.app.crm.bean.ChargingTemplateXInfo;
import com.redknee.app.crm.bean.DebtCollectionAgency;
import com.redknee.app.crm.bean.DebtCollectionAgencyXInfo;
import com.redknee.app.crm.bean.search.DebtCollectionAgencySearch;
import com.redknee.app.crm.bean.search.DebtCollectionAgencySearchWebControl;
import com.redknee.app.crm.bean.search.DebtCollectionAgencySearchXInfo;

/**
 * @author Marcio Marques
 */
public class DebtCollectionAgencySearchBorder extends SearchBorder
{
   public DebtCollectionAgencySearchBorder(Context ctx)
   {
      super(ctx,DebtCollectionAgency.class,new DebtCollectionAgencySearchWebControl());

      addAgent(new ContextAgentProxy()
            {
               @Override
            public void execute(Context ctx)
                  throws AgentException
               {
                   DebtCollectionAgencySearch criteria = (DebtCollectionAgencySearch)getCriteria(ctx);
                  if (criteria.getId() > -1 &&
                      null != doFind(ctx, new EQ(DebtCollectionAgencyXInfo.ID, Long.valueOf(criteria.getId()))))
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
             DebtCollectionAgencySearch criteria = (DebtCollectionAgencySearch)getCriteria(ctx);

            if (criteria.getSpid() != -1)
            {
               doSelect(
                  ctx, 
                  new EQ(DebtCollectionAgencySearchXInfo.SPID, Integer.valueOf(criteria.getSpid())));
            }
            delegate(ctx);
         }
      }
);

      addAgent(new WildcardSelectSearchAgent(DebtCollectionAgencyXInfo.NAME, DebtCollectionAgencySearchXInfo.NAME, true));
   }
}
