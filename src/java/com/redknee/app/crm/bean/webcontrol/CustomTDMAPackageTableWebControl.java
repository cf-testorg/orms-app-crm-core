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
package com.redknee.app.crm.bean.webcontrol;

import java.io.PrintWriter;

import com.redknee.app.crm.bean.PackageStateEnum;
import com.redknee.app.crm.bean.TDMAPackageTableWebControl;
import com.redknee.app.crm.bean.TDMAPackageXInfo;
import com.redknee.app.crm.numbermgn.GenericPackage;
import com.redknee.app.crm.support.WebControlSupportHelper;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.WebControl;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CustomTDMAPackageTableWebControl extends TDMAPackageTableWebControl
{
    @Override
    public WebControl getStateWebControl()
    {
        return CustomGSMPackageWebControl.CUSTOM_STATE_WC;
    }
    
    
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
    	if(GenericPackage.class.isAssignableFrom(obj.getClass()))
    	{
	        GenericPackage pack = (GenericPackage) obj;
	        if (ctx.getInt("MODE", DISPLAY_MODE) != CREATE_MODE && pack.getState() == PackageStateEnum.IN_USE)
	        {
	            WebControlSupportHelper.get(ctx).setPropertiesReadOnly(ctx, new PropertyInfo[]
	            {
	                TDMAPackageXInfo.MIN,
	                TDMAPackageXInfo.SERIAL_NO
	            });
	        }
    	}
        super.toWeb(ctx, out, name, obj);
    }        

}
