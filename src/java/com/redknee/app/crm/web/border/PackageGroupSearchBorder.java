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
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;

import com.redknee.app.crm.bean.PackageGroup;
import com.redknee.app.crm.bean.PackageGroupXInfo;
import com.redknee.app.crm.bean.search.PackageGroupSearch;
import com.redknee.app.crm.bean.search.PackageGroupSearchWebControl;
import com.redknee.app.crm.bean.search.PackageGroupSearchXInfo;

/**
 * @author psperneac
 * @since May 1, 2005 7:05:16 PM
 */
public class PackageGroupSearchBorder extends SearchBorder
{
   public PackageGroupSearchBorder(Context ctx)
   {
      super(ctx,PackageGroup.class,new PackageGroupSearchWebControl());

      // spid
      addAgent(new ContextAgentProxy()
            {
               @Override
            public void execute(Context ctx)
                  throws AgentException
               {
                  PackageGroupSearch criteria = (PackageGroupSearch)getCriteria(ctx);
                  if (criteria.getSpid() != -1)
                  {
                     doSelect(
                        ctx,
                        new EQ(PackageGroupXInfo.SPID, Integer.valueOf(criteria.getSpid())));
                  }
                  delegate(ctx);
               }
            }
      );
      
      // name
      addAgent(new WildcardSelectSearchAgent(PackageGroupXInfo.NAME, PackageGroupSearchXInfo.NAME, true));
   }
}
