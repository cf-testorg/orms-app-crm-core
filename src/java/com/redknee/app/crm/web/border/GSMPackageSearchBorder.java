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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.search.FindSearchAgent;
import com.redknee.framework.xhome.web.search.SearchBorder;

import com.redknee.app.crm.bean.GSMPackage;
import com.redknee.app.crm.bean.GSMPackageXInfo;
import com.redknee.app.crm.bean.search.GSMPackageSearchWebControl;
import com.redknee.app.crm.bean.search.GSMPackageSearchXInfo;


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
public class GSMPackageSearchBorder
   extends SearchBorder
{
   
   public GSMPackageSearchBorder(final Context context)
   {
      super(context, GSMPackage.class, new GSMPackageSearchWebControl());
      
      // SerialNo
      addAgent(new FindSearchAgent(GSMPackageXInfo.SERIAL_NO, GSMPackageSearchXInfo.SERIAL_NO));
      
      // PackId
      addAgent(new FindSearchAgent(GSMPackageXInfo.PACK_ID, GSMPackageSearchXInfo.PACK_ID));
      
      // IMSI
      addAgent(new FindSearchAgent(GSMPackageXInfo.IMSI, GSMPackageSearchXInfo.IMSI));
   }
   
}

