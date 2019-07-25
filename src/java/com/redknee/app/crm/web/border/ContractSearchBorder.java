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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.search.FindSearchAgent;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;

import com.redknee.app.crm.bean.payment.Contract;
import com.redknee.app.crm.bean.payment.ContractXInfo;
import com.redknee.app.crm.bean.search.ContractSearchWebControl;
import com.redknee.app.crm.bean.search.ContractSearchXInfo;

/**
 * @author albert
 * @since July 21, 2005 09:43:25 AM
 */
public class ContractSearchBorder extends SearchBorder
{
   public ContractSearchBorder(Context context)
   {
      super(context,Contract.class,new ContractSearchWebControl());

      addAgent(new FindSearchAgent(ContractXInfo.ID, ContractSearchXInfo.CONTRACT_ID).addIgnore(Long.valueOf(-1)));

      addAgent(new SelectSearchAgent(ContractXInfo.SPID, ContractSearchXInfo.SPID).addIgnore(Integer.valueOf(-1)));
      
      addAgent(new WildcardSelectSearchAgent(ContractXInfo.NAME, ContractSearchXInfo.NAME, true));
   }
}