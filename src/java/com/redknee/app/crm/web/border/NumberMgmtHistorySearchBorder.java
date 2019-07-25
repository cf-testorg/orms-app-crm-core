package com.redknee.app.crm.web.border;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgentProxy;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;

import com.redknee.app.crm.bean.search.NumberMgmtHistorySearch;
import com.redknee.app.crm.bean.search.NumberMgmtHistorySearchWebControl;
import com.redknee.app.crm.bean.search.NumberMgmtHistorySearchXInfo;
import com.redknee.app.crm.numbermgn.NumberMgmtHistoryXInfo;

public class NumberMgmtHistorySearchBorder
   extends SearchBorder
{
   public NumberMgmtHistorySearchBorder(Context ctx, Class beanType)
   {
      super(ctx, beanType, new NumberMgmtHistorySearchWebControl());

      addAgent(new SelectSearchAgent(NumberMgmtHistoryXInfo.TERMINAL_ID, NumberMgmtHistorySearchXInfo.TERMINAL_ID));

      addAgent(
            new ContextAgentProxy()
            {
               @Override
            public void execute(Context ctx)
                  throws AgentException
               {
                  NumberMgmtHistorySearch criteria = (NumberMgmtHistorySearch)getCriteria(ctx);

                  if (criteria.getEvent() > 0)
                  {
                     doSelect(
                        ctx,
                        new EQ(NumberMgmtHistoryXInfo.EVENT, Long.valueOf(criteria.getEvent())));
                  }
                  delegate(ctx);
               }
            }
      );
   }
}
