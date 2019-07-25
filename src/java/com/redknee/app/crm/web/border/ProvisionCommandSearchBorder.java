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
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;

import com.redknee.app.crm.bean.ProvisionCommand;
import com.redknee.app.crm.bean.ProvisionCommandXInfo;
import com.redknee.app.crm.bean.search.ProvisionCommandSearch;
import com.redknee.app.crm.bean.search.ProvisionCommandSearchWebControl;
import com.redknee.app.crm.bean.search.ProvisionCommandSearchXInfo;

/**
 * @author jke
 */
public class ProvisionCommandSearchBorder extends SearchBorder
{
    public ProvisionCommandSearchBorder(Context ctx)
    {
       super(ctx, ProvisionCommand.class, new ProvisionCommandSearchWebControl());


         // spid
         addAgent(new ContextAgentProxy()
               {
                  @Override
                public void execute(Context ctx)
                     throws AgentException
                  {
                      ProvisionCommandSearch criteria = (ProvisionCommandSearch)getCriteria(ctx);

                     if (criteria.getSpid() > -1)
                     {
                        doSelect(
                           ctx,
                           new EQ(ProvisionCommandXInfo.SPID, Integer.valueOf(criteria.getSpid())));
                     }
                     delegate(ctx);
                  }
               }
         );
         
         // name
         addAgent(new WildcardSelectSearchAgent(ProvisionCommandXInfo.NAME, ProvisionCommandSearchXInfo.NAME, true));        
    }
}
