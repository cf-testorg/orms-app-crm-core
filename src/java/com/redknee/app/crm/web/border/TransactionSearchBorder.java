// INSPECTED: 26/09/2003 LZ


/*
 *  TransactionSearchBorder
 *
 *  Author : Kevin Greer
 *  Date   : Sept 24, 2003
 *
 *  Copyright (c) 2003, Redknee
 *  All rights reserved.
 */

package com.redknee.app.crm.web.border;

import java.util.Date;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgentProxy;
import com.redknee.framework.xhome.elang.GTE;
import com.redknee.framework.xhome.elang.LTE;
import com.redknee.framework.xhome.web.search.LimitSearchAgent;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;

import com.redknee.app.crm.bean.TransactionXInfo;
import com.redknee.app.crm.bean.core.Transaction;
import com.redknee.app.crm.bean.search.TransactionSearch;
import com.redknee.app.crm.bean.search.TransactionSearchWebControl;
import com.redknee.app.crm.bean.search.TransactionSearchXInfo;
import com.redknee.app.crm.support.CalendarSupportHelper;


/**
 * A Custom SearchBorder for Transactions.
 *
 * This will be generated from an XGen template in the future but for now
 * I'm still experimenting with the design.  Also, some common helper classes
 * will be created for each Search type.
 *
 * Add this Border before the WebController, not as one of either its
 * Summary or Detail borders.
 *
 * @author     kgreer
 **/
public class TransactionSearchBorder
   extends SearchBorder
{

   public TransactionSearchBorder(Context context)
   {
      super(context, Transaction.class, new TransactionSearchWebControl());

      // BAN
      addAgent(new SelectSearchAgent(TransactionXInfo.BAN, TransactionSearchXInfo.BAN));

      // MSISDN
      addAgent(new SelectSearchAgent(TransactionXInfo.MSISDN, TransactionSearchXInfo.MSISDN));

      // Subscriber ID
      addAgent(new SelectSearchAgent(TransactionXInfo.SUBSCRIBER_ID, TransactionSearchXInfo.SUBSCRIBER_ID));

      // startDate
      addAgent(new ContextAgentProxy() {
         @Override
        public void execute(Context ctx)
            throws AgentException
         {
            TransactionSearch criteria = (TransactionSearch) getCriteria(ctx);

            if ( criteria.getStartDate() != null )
            {
                Date startDate = CalendarSupportHelper.get(ctx).getDateWithNoTimeOfDay(criteria.getStartDate());
               doSelect(ctx, new GTE(TransactionXInfo.TRANS_DATE, startDate));
            }

            delegate(ctx);
         }
      });

      // endDate
      addAgent(new ContextAgentProxy() {
         @Override
        public void execute(Context ctx)
            throws AgentException
         {
            TransactionSearch criteria = (TransactionSearch) getCriteria(ctx);

            if ( criteria.getEndDate() != null )
            {
                Date endDate = CalendarSupportHelper.get(ctx).getDateWithLastSecondofDay(criteria.getEndDate());
               doSelect(ctx, new LTE(TransactionXInfo.TRANS_DATE, endDate));
            }

            delegate(ctx);
         }
      });

      // Limit
      addAgent(new LimitSearchAgent(TransactionSearchXInfo.LIMIT));

   }

}

