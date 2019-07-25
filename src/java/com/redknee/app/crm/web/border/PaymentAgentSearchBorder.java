/*
 * Created on May 31, 2005
 *
 * Copyright (c) 1999-2005 REDKNEE.com. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * REDKNEE.com. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with REDKNEE.com.
 *
 * REDKNEE.COM MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THE SOFTWARE, EITHCDR EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE IMPLIED WARRANTIES OF MCDRCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. REDKNEE.COM SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFCDRED BY LICENSEE AS A RESULT OF USING,
 * MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DCDRIVATIVES.
 */
package com.redknee.app.crm.web.border;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgentProxy;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;

import com.redknee.app.crm.bean.payment.PaymentAgent;
import com.redknee.app.crm.bean.payment.PaymentAgentXInfo;
import com.redknee.app.crm.bean.search.PaymentAgentSearch;
import com.redknee.app.crm.bean.search.PaymentAgentSearchWebControl;
import com.redknee.app.crm.bean.search.PaymentAgentSearchXInfo;

/**
 * @author jke
 *
 */
public class PaymentAgentSearchBorder extends SearchBorder
{
   public PaymentAgentSearchBorder(Context ctx)
   {
      super(ctx, PaymentAgent.class, new PaymentAgentSearchWebControl());


        // spid
        addAgent(new ContextAgentProxy()
              {
                 @Override
                public void execute(Context ctx)
                    throws AgentException
                 {
                 	PaymentAgentSearch criteria = (PaymentAgentSearch)getCriteria(ctx);

                    if (criteria.getSpid() > -1)
                    {
                       doSelect(
                          ctx,
                          new EQ(PaymentAgentXInfo.SPID, Integer.valueOf(criteria.getSpid())));
                    }
                    delegate(ctx);
                 }
              }
        );

    // name
    addAgent(new WildcardSelectSearchAgent(PaymentAgentXInfo.NAME, PaymentAgentSearchXInfo.NAME, true));
  }
}
