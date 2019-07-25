package com.redknee.app.crm.calculator;

import java.util.List;

import com.redknee.framework.xhome.context.Context;

/**
 * 
 * @author kabhay
 *
 */
public class ListValueCalculator extends AbstractListValueCalculator 
{


	@Override
	public Object getValueAdvanced(Context ctx) 
	{
		
		ValueCalculator delegate = getDelegate();
		
		List<ValueCalculator> calcList = getListOfValueCalculators();
		Object finalResult = "";
		if(calcList != null)
		{
			/*
			 * TODO : Enhance to iterate in a particular order
			 */
			for(ValueCalculator calc : calcList)
			{
				Object temp = calc.getValueAdvanced(ctx).toString();
				if(temp != null)
				{
					finalResult  = finalResult.toString() + temp.toString();
				}
				 
			}
			
		}
		
		if(delegate != null)
		{
			Object temp = delegate.getValueAdvanced(ctx).toString();
			if(temp != null)
			{
				finalResult = temp.toString() + finalResult.toString();
			}
			
		}
		
		return finalResult;
	}

	
}
