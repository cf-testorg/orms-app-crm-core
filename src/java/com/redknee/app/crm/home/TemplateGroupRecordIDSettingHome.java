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

package com.redknee.app.crm.home;

import com.redknee.app.crm.bean.IdentifierEnum;
import com.redknee.app.crm.notification.template.TemplateGroupGlobalRecord;
import com.redknee.app.crm.support.IdentifierSequenceSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.LogSupport;

/**
 * 
 * Sets ID and identifier for this TemplateGroupGlobalRecord using an
 * IdentifierSequence.  
 *  
 * @author bdhavalshankh
 *
 */
public class TemplateGroupRecordIDSettingHome extends HomeProxy 
{
	private static final long startValue = 100000;
	
	public TemplateGroupRecordIDSettingHome(Context ctx, Home delegate)
	{
		super(ctx, delegate);
	}

	@Override
	public Object create(Context ctx, Object obj) throws HomeException,
			HomeInternalException 
	{
		
	    TemplateGroupGlobalRecord templateGrpRcrd = (TemplateGroupGlobalRecord) obj;
		
		IdentifierSequenceSupportHelper.get(ctx).ensureSequenceExists(ctx, IdentifierEnum.TEMPLATE_GROUP_RECORD_ID,
				startValue, Long.MAX_VALUE);
		
		long templateId = IdentifierSequenceSupportHelper.get(ctx).getNextIdentifier(
	            ctx,
	            IdentifierEnum.TEMPLATE_GROUP_RECORD_ID,
	            null);
		
		if(templateGrpRcrd.getId() == 0)
		{
			templateGrpRcrd.setId(templateId);

		}
		
		LogSupport.info(ctx, this, "Template Group Record ID set to: " + templateGrpRcrd.getId());
		
		return super.create(ctx, obj);
	}

	
}
