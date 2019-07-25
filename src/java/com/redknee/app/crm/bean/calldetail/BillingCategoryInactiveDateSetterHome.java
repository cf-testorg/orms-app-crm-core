/**
 * Copyright (c) 2010 Redknee, Inc. and its subsidiaries. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of REDKNEE.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with REDKNEE.
 *
 * REDKNEE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. REDKNEE SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR
 * ITS DERIVATIVES.
 */
 
package com.redknee.app.crm.bean.calldetail;

import java.util.Date;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;

import com.redknee.app.crm.bean.BillingOptionMapping;
import com.redknee.app.crm.bean.BillingOptionMappingXInfo;
import com.redknee.app.crm.support.HomeSupportHelper;

public class BillingCategoryInactiveDateSetterHome extends HomeProxy {

	public BillingCategoryInactiveDateSetterHome() {
	}

	public BillingCategoryInactiveDateSetterHome(Context ctx) {
		super(ctx);
	}

	public BillingCategoryInactiveDateSetterHome(Home delegate) {
		super(delegate);
	}

	public BillingCategoryInactiveDateSetterHome(Context ctx, Home delegate) {
		super(ctx, delegate);
	}
	
	/**
	 * write to data store
	 */
	@Override
	public Object store(Context ctx, Object obj) throws HomeException
	{
		
		BillingCategory beanNew = (BillingCategory) obj;
		
		if( BillingCategoryStateEnum.INACTIVE.equals(beanNew.getStatus()) ) 
		{
			BillingCategory beanOld = (BillingCategory) find( ctx, beanNew.getId() );
			
			if( BillingCategoryStateEnum.ACTIVE.equals( beanOld.getStatus() ) )
			{
			    if(HomeSupportHelper.get(ctx).hasBeans(ctx, 
			            BillingOptionMapping.class, 
			            new EQ(BillingOptionMappingXInfo.BILLING_CATEGORY, beanOld.getId())))
			    {
			        throw new HomeException("There are active BillingOptionMapping using the BillingCategory. " +
			        		"Please change BillingCategory or remove the BillingOptionMapping.");
			    } 
			    else 
			    {
			        beanNew.setInactivedDate(new Date());
			    }
			}
		}
		
		return super.store(ctx, beanNew);
	}

}
