/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
 
package com.redknee.app.crm.web.border;

import com.redknee.app.crm.bean.core.VSATPackage;
import com.redknee.app.crm.bean.search.VSATPackageSearchWebControl;
import com.redknee.app.crm.bean.search.VSATPackageSearchXInfo;
import com.redknee.app.crm.bean.VSATPackageXInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.search.FindSearchAgent;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;

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
 * @author     simar.singh@redknee.com
 **/
public class VSATPackageSearchBorder
   extends SearchBorder
{
   
   public VSATPackageSearchBorder(final Context context)
   {
      super(context, VSATPackage.class, new VSATPackageSearchWebControl());
      
      // VSAT ID
      addAgent(new SelectSearchAgent(VSATPackageXInfo.VSAT_ID, VSATPackageSearchXInfo.VSAT_ID));
      
      // PACK ID
      addAgent(new FindSearchAgent(VSATPackageXInfo.PACK_ID, VSATPackageSearchXInfo.PACK_ID));
      
      // PORT
      addAgent(new SelectSearchAgent(VSATPackageXInfo.PORT, VSATPackageSearchXInfo.PORT));
      
    // CHANNEL
      addAgent(new SelectSearchAgent(VSATPackageXInfo.CHANNEL, VSATPackageSearchXInfo.CHANNEL));
   }
   
}

