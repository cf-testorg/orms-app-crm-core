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
 * If the calculated value is null, this will throw exception so.
 * @author sgaidhani
 * @since 9.4
 *
 */
public class MandatoryValueCalculator extends AbstractMandatoryValueCalculator{

	public MandatoryValueCalculator() {

		super();
	}


	@Override
	public Object getValueAdvanced(Context ctx)
	{
		Object calculateValue = getDelegate().getValueAdvanced(ctx);
		if(calculateValue == null ||
				(getThrowExceptionForEmptyString() == true && "".equals(calculateValue.toString().trim()))) 
		{
		    if(LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(DefaultValueCalculator.class.getName(), "Calculated value is null returning a default claculated value" + calculateValue, null);
            }
			throw new IllegalStateException(getErrorMessage());
		}

		if(LogSupport.isDebugEnabled(ctx))
        {
		    if(calculateValue.getClass().getName() != null) 
		    {
		        new DebugLogMsg(DefaultValueCalculator.class.getName(), "Returning a calculated value from valuecalculator " + calculateValue.getClass().getName(), null);
		    }
            
        }
		return calculateValue;
	}

}
