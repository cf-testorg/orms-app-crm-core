package com.redknee.app.crm.web.border;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgentProxy;
import com.redknee.framework.xhome.context.ReturnContextAgent;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;

import com.redknee.app.crm.bean.AccountCategory;
import com.redknee.app.crm.bean.AccountCategoryXInfo;
import com.redknee.app.crm.bean.search.AccountTypeSearch;
import com.redknee.app.crm.bean.search.AccountTypeSearchWebControl;
import com.redknee.app.crm.bean.search.AccountTypeSearchXInfo;

public class AccountTypeSearchBorder<T extends AccountCategory>
   extends SearchBorder
{
	public AccountTypeSearchBorder(Context ctx, Class<T> cls)
   {
		super(ctx, cls, new AccountTypeSearchWebControl());

      // identifier
      addAgent(
         new ContextAgentProxy()
         {
            @Override
            public void execute(Context ctx)
               throws AgentException
            {
               AccountTypeSearch criteria = (AccountTypeSearch)getCriteria(ctx);
               long id = criteria.getIdentifier();

				if (id >= 0
				    && null != doFind(ctx, new EQ(
				        AccountCategoryXInfo.IDENTIFIER, Long.valueOf(id))))
               {
                  // no need to continue once we've found the account type
                  ReturnContextAgent.doReturn(ctx);
                  return;
               }
               delegate(ctx);
            }
         }
      );

		addAgent(new WildcardSelectSearchAgent(AccountCategoryXInfo.NAME,
		    AccountTypeSearchXInfo.NAME, true));
   }
}
