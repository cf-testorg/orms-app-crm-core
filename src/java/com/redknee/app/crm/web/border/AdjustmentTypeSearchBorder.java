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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;

import com.redknee.app.crm.bean.AdjustmentType;
import com.redknee.app.crm.bean.AdjustmentTypeXInfo;
import com.redknee.app.crm.bean.search.AdjustmentTypeSearchWebControl;
import com.redknee.app.crm.bean.search.AdjustmentTypeSearchXInfo;

/**
 * @author amedina
 *
 * Search Border for Adjustment type by Name
 */
public class AdjustmentTypeSearchBorder extends SearchBorder 
{
	   public AdjustmentTypeSearchBorder(Context ctx)
	   {
	      super(ctx, AdjustmentType.class, new AdjustmentTypeSearchWebControl());

	      addAgent(new WildcardSelectSearchAgent(AdjustmentTypeXInfo.NAME, AdjustmentTypeSearchXInfo.NAME, true));
	   }
}
