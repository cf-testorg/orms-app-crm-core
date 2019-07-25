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
package com.redknee.app.crm.support;

import com.redknee.app.crm.bean.TaxAuthority;
import com.redknee.app.crm.bean.TaxAuthorityHome;
import com.redknee.app.crm.bean.TaxAuthorityTypeEnum;
import com.redknee.app.crm.bean.TaxationMethodInfo;
import com.redknee.app.crm.bean.TaxationMethodInfoXInfo;
import com.redknee.app.crm.taxation.TaxationMethod;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;


public class TaxationMethodSupport
{
    /** 
     * @author bdhavalshankh
     * @since 9.5
     */
	 public static int resolveExternalTaxAuthority(Context ctx, Object obj) throws HomeException
	    {
	    	TaxAuthority ta = null;
	    	if(TaxAuthority.class.isInstance(obj))
	    	{
	    		ta = (TaxAuthority) obj;
	    	}
	    	else if(Integer.class.isInstance(obj))
	    	{
	    		Home home  = (Home) ctx.get(TaxAuthorityHome.class);
	    		ta = (TaxAuthority) home.find(ctx, obj);
	    	}
	    	else
	    	{
	    		throw new HomeException("Input object is neither tax-auth nor tax-auth-id");
	    	}
	        

	        if (ta.getTaxAuthorityType().getIndex() == TaxAuthorityTypeEnum.EXTERNAL_INDEX)
	        {
	            TaxationMethodInfo tmi = HomeSupportHelper.get(ctx).findBean(ctx, TaxationMethodInfo.class, new EQ(TaxationMethodInfoXInfo.ID, ta.getTaxationMethod()));
                if(tmi == null)
                {
                    throw new HomeException("Taxation Method Info for the Tax Authority "+ta.getIdentifier()+" is null . Please check if you have configured correct Taxation method for the tax authority");
                }
                else
                {
                    TaxationMethod tm = (TaxationMethod) ctx.get(tmi.getAdapter());
    	            if(tm != null)
    	            {
    	            	return tm.getTaxAuthority(ctx);
    	            }
    	            else
                    {
                       try
                       {
                           tm = (TaxationMethod) Class.forName(tmi.getAdapter()).getConstructor(new Class[]{}).newInstance(new Object[]{});
                           return tm.getTaxAuthority(ctx);
                       }
                       catch (Exception e)
                       {
                           throw new HomeException(e.getMessage(), e);
                       }
                    }
                }
	        }
	        else
	        {
	        	if(Integer.class.isInstance(obj))
	        	{
	        		return (Integer) obj;
	        	}
	        	else if(TaxAuthority.class.isInstance(obj))
	        	{
	        		return (int) ta.getIdentifier();
	        	}
	        }
	        return -1;
	    }
}
