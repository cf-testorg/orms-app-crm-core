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

import com.redknee.app.crm.bean.DiscountClass;
import com.redknee.app.crm.bean.DiscountClassXInfo;
import com.redknee.app.crm.bean.search.DiscountClassSearch;
import com.redknee.app.crm.bean.search.DiscountClassSearchWebControl;
import com.redknee.app.crm.bean.search.DiscountClassSearchXInfo;

/**
 * @author psperneac
 * @since May 1, 2005 9:25:02 PM
 */
public class DiscountClassSearchBorder extends SearchBorder
{
   public DiscountClassSearchBorder(Context ctx)
   {
      super(ctx,DiscountClass.class,new DiscountClassSearchWebControl());

      addAgent(new ContextAgentProxy()
            {
               @Override
            public void execute(Context ctx)
                  throws AgentException
               {
                  DiscountClassSearch criteria = (DiscountClassSearch)getCriteria(ctx);
                  if (criteria.getId() > -1 &&
                      null != doFind(ctx, new EQ(DiscountClassXInfo.ID, Integer.valueOf(criteria.getId()))))
                  {
                     ContextAgents.doReturn(ctx);
                  }
                  delegate(ctx);
               }
            }
      );

      addAgent(new WildcardSelectSearchAgent(DiscountClassXInfo.NAME, DiscountClassSearchXInfo.NAME, true));
   }
}
