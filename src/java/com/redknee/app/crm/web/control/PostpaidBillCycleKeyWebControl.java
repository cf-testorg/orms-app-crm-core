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
package com.redknee.app.crm.web.control;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.LT;
import com.redknee.framework.xhome.home.Home;

import com.redknee.app.crm.CoreCrmConstants;
import com.redknee.app.crm.bean.BillCycleKeyWebControl;
import com.redknee.app.crm.bean.BillCycleXInfo;

/**
 * This class adds customized behaviour to the BillCycleKeyWebControl.
 *
 * @author Jimmy Ng
 */
public class PostpaidBillCycleKeyWebControl extends BillCycleKeyWebControl
{
	public PostpaidBillCycleKeyWebControl()
	{
		super();
	}

	public PostpaidBillCycleKeyWebControl(boolean autoPreview)
	{
		super(autoPreview);
	}

	public PostpaidBillCycleKeyWebControl(int listSize)
	{
		super(listSize);
	}

	public PostpaidBillCycleKeyWebControl(int listSize, boolean autoPreview)
	{
		super(listSize, autoPreview);
	}

	public PostpaidBillCycleKeyWebControl(int listSize, boolean autoPreview, boolean isOptional)
	{
		super(listSize, autoPreview, isOptional);
	}

	public PostpaidBillCycleKeyWebControl(int listSize, boolean autoPreview, boolean isOptional, boolean allowCustom)
	{
		super(listSize, autoPreview, isOptional, allowCustom);
	}

	public PostpaidBillCycleKeyWebControl(int listSize, boolean autoPreview, Object optionalValue)
	{
		super(listSize, autoPreview, optionalValue);
	}

	public PostpaidBillCycleKeyWebControl(int listSize, boolean autoPreview, Object optionalValue, boolean allowCustom)
	{
		super(listSize, autoPreview, optionalValue, allowCustom);
	}

	/**
	 * INHERIT
	 */
	@Override
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
	{
        Context subCtx = ctx.createSubContext();
        
        HttpServletRequest req = (HttpServletRequest) subCtx.get(HttpServletRequest.class);
        HttpSession session = req.getSession();
        
		final int mode = subCtx.getInt("MODE", DISPLAY_MODE);
		
		Home billCycleHome = (Home) subCtx.get(getHomeKey());
		
		// TT8051700004 - Filter out the auto-bill cycles for postpaid accounts
		billCycleHome = billCycleHome.where(subCtx, new LT(BillCycleXInfo.BILL_CYCLE_ID, CoreCrmConstants.AUTO_BILL_CYCLE_START_ID));

        subCtx.put(getHomeKey(), billCycleHome);
		super.toWeb(subCtx, out, name, obj);
	}
}
