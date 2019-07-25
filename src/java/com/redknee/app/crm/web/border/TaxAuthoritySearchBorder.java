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

import com.redknee.app.crm.bean.TaxAuthority;
import com.redknee.app.crm.bean.TaxAuthorityXInfo;
import com.redknee.app.crm.bean.search.TaxAuthoritySearch;
import com.redknee.app.crm.bean.search.TaxAuthoritySearchWebControl;
import com.redknee.app.crm.bean.search.TaxAuthoritySearchXInfo;
import com.redknee.app.crm.web.search.PrePostWildcardSelectSearchAgent;

/**
 * @author amedina
 *
 * Search Border for Tax Authority by Id and Name and Spid
 */
public class TaxAuthoritySearchBorder extends SearchBorder 
{
	   public TaxAuthoritySearchBorder(Context ctx)
	   {
	      super(ctx, TaxAuthority.class, new TaxAuthoritySearchWebControl());

	      // ID Type
	      addAgent(new ContextAgentProxy() {
	         @Override
            public void execute(Context ctx)
	            throws AgentException
	         {
	            TaxAuthoritySearch criteria = (TaxAuthoritySearch)getCriteria(ctx);
	     
               if (criteria.getTaxId() != -1)
	            {
                  SearchBorder.doSelect(
                     ctx, 
                     new EQ(TaxAuthorityXInfo.TAX_ID, Integer.valueOf(criteria.getTaxId())));
	            }
	            delegate(ctx);
	         }
	      });

	      addAgent(new ContextAgentProxy() {
	         @Override
            public void execute(Context ctx)
	            throws AgentException
	         {
	            TaxAuthoritySearch criteria = (TaxAuthoritySearch)getCriteria(ctx);

               if (criteria.getSpid() != -1)
               {
                  SearchBorder.doSelect(
                     ctx, 
                     new EQ(TaxAuthorityXInfo.SPID, Integer.valueOf(criteria.getSpid())));
	            
	            }
	            delegate(ctx);
	         }
	      });

	      
	      addAgent(new PrePostWildcardSelectSearchAgent(TaxAuthorityXInfo.TAX_AUTH_NAME, TaxAuthoritySearchXInfo.NAME));
	   }

}
