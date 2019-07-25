/*
 * Copyright (c) 1999-2003 REDKNEE.com. All Rights Reserved.
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
package com.redknee.app.crm.xhome.home;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;

import com.redknee.app.crm.bean.core.Transaction;
import com.redknee.app.crm.support.CoreTransactionSupportHelper;

/**
 * this home uses AdjustmentType value in newly generated Transaction bean
 * to retrieve GL code from AdjustmentTypeHome for it.
 *
 * @author lzou
 * @date   Oct.14, 2003
 */
public class GLCodeObtainHome 
    extends HomeProxy 
{
	public GLCodeObtainHome(Home delegate)
	{
		super(delegate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    public Object create(Context ctx, Object obj) throws HomeException
	{
		Transaction trans = (Transaction)obj;
		
        
		if (trans == null)
		{
            throw new HomeException("Cannot create Tranasction.  Object is null");
        }

		CoreTransactionSupportHelper.get(ctx).addTransactionGLCode(ctx, trans);

		return super.create(ctx,trans);
	}
}
