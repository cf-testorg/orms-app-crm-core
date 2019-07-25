package com.redknee.app.crm.calculator;

import com.redknee.app.crm.bean.KeyConfiguration;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.app.crm.support.KeyValueSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;

/**
 * 
 * @author kabhay
 *
 */
public class InternalKeyValueCalculator extends AbstractInternalKeyValueCalculator 
{

	@Override
	public Object getValueAdvanced(Context ctx) 
	{
	
		Object returnObj = "";
		String internalKey = getInternalKey();
		try {
			KeyConfiguration keyConfig = HomeSupportHelper.get(ctx).findBean(ctx, KeyConfiguration.class, internalKey);
			
			if(keyConfig != null)
			{
				ValueCalculator calc = keyConfig.getValueCalculator();
				if(calc != null)
				{
					returnObj = calc.getValueAdvanced(ctx);
				}
			}
		}catch (HomeException e) 
		{
		
			e.printStackTrace();
		}
		
		return returnObj;
	}
}
