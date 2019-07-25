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
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;

import com.redknee.app.crm.bean.DealerCode;
import com.redknee.app.crm.bean.DealerCodeXInfo;
import com.redknee.app.crm.bean.search.DealerCodeSearch;
import com.redknee.app.crm.bean.search.DealerCodeSearchWebControl;
import com.redknee.app.crm.bean.search.DealerCodeSearchXInfo;
import com.redknee.app.crm.web.search.PrePostWildcardSelectSearchAgent;

/**
 * @author psperneac
 * @since May 1, 2005 11:43:05 PM
 */
public class DealerCodeSearchBorder extends SearchBorder
{
   public DealerCodeSearchBorder(Context ctx)
   {
      super(ctx,DealerCode.class,new DealerCodeSearchWebControl());

      // dealer code
      addAgent(new SelectSearchAgent(DealerCodeXInfo.CODE, DealerCodeSearchXInfo.CODE, true));

      // spid
      addAgent(new ContextAgentProxy()
            {
               @Override
            public void execute(Context ctx)
                  throws AgentException
               {
                  DealerCodeSearch criteria = (DealerCodeSearch)getCriteria(ctx);

                  if (criteria.getSpid() > -1)
                  {
                     doSelect(
                        ctx,
                        new EQ(DealerCodeXInfo.SPID, Integer.valueOf(criteria.getSpid())));
                  }
                  delegate(ctx);
               }
            }
      );

      // description
      addAgent(new PrePostWildcardSelectSearchAgent(DealerCodeXInfo.DESC, DealerCodeSearchXInfo.DESC, true));
   }
}
