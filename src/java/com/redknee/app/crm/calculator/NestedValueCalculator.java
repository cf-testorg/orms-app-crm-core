package com.redknee.app.crm.calculator;

import com.redknee.framework.xhome.context.Context;

/**
 * 
 * @author kabhay
 *
 */
public class NestedValueCalculator extends AbstractNestedValueCalculator
{

	@Override
	public Object getValueAdvanced(Context ctx) 
	{
		
		ValueCalculator delegate = getDelegate();
		
		ValueCalculator inner = getInnerNestedValueCalculator();
		Object finalResult = "";
		
		if(inner != null)
		{
			Object temp = inner.getValueAdvanced(ctx).toString();
			if(temp != null)
			{
				finalResult = finalResult + inner.getValueAdvanced(ctx).toString();
			}
			
		}
		
		if(delegate != null)
		{
			Object temp = delegate.getValueAdvanced(ctx);
			if(temp != null)
			{
				finalResult = temp.toString() + finalResult.toString();
			}
		}
		 
		return finalResult;
	}

}
