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

import com.redknee.app.crm.bean.TransactionMethod;
import com.redknee.app.crm.bean.TransactionMethodXInfo;
import com.redknee.app.crm.bean.search.TransactionMethodSearch;
import com.redknee.app.crm.bean.search.TransactionMethodSearchWebControl;
import com.redknee.app.crm.bean.search.TransactionMethodSearchXInfo;
import com.redknee.app.crm.web.search.PrePostWildcardSelectSearchAgent;

/**
 * @author amedina
 *
 * Search Border for Transaction Method by Name
 */
public class TransactionMethodSearchBorder extends SearchBorder 
{
	   public TransactionMethodSearchBorder(Context ctx)
	   {
	      super(ctx, TransactionMethod.class, new TransactionMethodSearchWebControl());

	      addAgent(new ContextAgentProxy() {
	         @Override
            public void execute(Context ctx)
	            throws AgentException
	         {
	            TransactionMethodSearch criteria = (TransactionMethodSearch)getCriteria(ctx);
	     
               if (criteria.getIdentifier() != -1)
               {
                  SearchBorder.doSelect(
                     ctx, 
                     new EQ(TransactionMethodXInfo.IDENTIFIER, Integer.valueOf(criteria.getIdentifier())));
	            }
	            delegate(ctx);
	         }
	      });

	      
	      addAgent(new PrePostWildcardSelectSearchAgent(TransactionMethodXInfo.NAME, TransactionMethodSearchXInfo.NAME, false));
	   }
}
