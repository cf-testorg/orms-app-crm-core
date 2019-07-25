/*
 * Copyright (c) 2007, REDKNEE.com. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of REDKNEE.com.
 * ("Confidential Information"). You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement you entered
 * into with REDKNEE.com.
 * 
 * REDKNEE.COM MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT.
 * REDKNEE.COM SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.redknee.app.crm.calculator;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;


/**
 * The class can be used as a wrapper class. If the delegate value evaluated is null or empty,
 * it will evaluate the default key and use this.
 * @author sgaidhani
 * @since 9.4
 *
 */
public class DefaultValueCalculator extends AbstractDefaultValueCalculator {

	public DefaultValueCalculator() {
	      super();
	   }

	@Override
	public Object getValueAdvanced(Context ctx)
	{
		Object calculatedValue = getDelegate().getValueAdvanced(ctx);
	
		if(calculatedValue == null || 
				(getUseDefaultForEmptyString() == true && "".equals(calculatedValue.toString().trim())))
		{
		    if(LogSupport.isDebugEnabled(ctx))
	        {
	            new DebugLogMsg(DefaultValueCalculator.class.getName(), "Calculated value is null returning a default claculated value" + calculatedValue, null);
	        }
			return getDefaultValue().getValueAdvanced(ctx);
		}
		
		if(LogSupport.isDebugEnabled(ctx))
        {
		    if(calculatedValue.getClass().getName()!=null)
		    {
		        new DebugLogMsg(DefaultValueCalculator.class.getName(), "Returning a calculated value from valuecalculator " + calculatedValue.getClass().getName(), null);
		    }
        }
		return calculatedValue;
	}
	   

}
