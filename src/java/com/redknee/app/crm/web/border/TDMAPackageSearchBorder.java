// INSPECTED: 26/09/2003 LZ

/*
 *  PackageSearchBorder
 *
 *  Author : Kevin Greer
 *  Date   : Sept 24, 2003
 *  
 *  Copyright (c) 2003, Redknee
 *  All rights reserved.
 */
 
package com.redknee.app.crm.web.border;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgentProxy;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.web.search.FindSearchAgent;
import com.redknee.framework.xhome.web.search.SearchBorder;

import com.redknee.app.crm.bean.TDMAPackage;
import com.redknee.app.crm.bean.TDMAPackageXInfo;
import com.redknee.app.crm.bean.search.TDMAPackageSearch;
import com.redknee.app.crm.bean.search.TDMAPackageSearchWebControl;
import com.redknee.app.crm.bean.search.TDMAPackageSearchXInfo;


/**
 * A Custom SearchBorder for Packages.
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
public class TDMAPackageSearchBorder
   extends SearchBorder
{
   
   public TDMAPackageSearchBorder(final Context context)
   {
      super(context, TDMAPackage.class, new TDMAPackageSearchWebControl());
      
      // SerialNo
      addAgent(new FindSearchAgent(TDMAPackageXInfo.SERIAL_NO, TDMAPackageSearchXInfo.SERIAL_NO));
      
      // PackId
      addAgent(new FindSearchAgent(TDMAPackageXInfo.PACK_ID, TDMAPackageSearchXInfo.PACK_ID));
      
      // IMSI
      addAgent(new FindSearchAgent(TDMAPackageXInfo.MIN, TDMAPackageSearchXInfo.IMSI));
      
      addAgent(new ContextAgentProxy()
              {
                 @Override
                public void execute(Context ctx)
                    throws AgentException
                 {
                	 TDMAPackageSearch criteria = (TDMAPackageSearch)getCriteria(ctx);
                	 
                	 if(criteria != null && criteria.getTechnology() != null)
                	 {
	                	 doSelect(
	                             ctx,
	                             new EQ(TDMAPackageXInfo.TECHNOLOGY, criteria.getTechnology()));
                	 }
	                 delegate(ctx);
                 }
              }
        ); 

   }
   
}

