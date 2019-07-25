package com.redknee.app.crm.calculator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.redknee.framework.xhome.context.Context;

public class StructureValueCalculator extends AbstractStructureValueCalculator {
	
	List list = new ArrayList();
	
	@Override
	public Object getValueAdvanced(Context ctx) 
	{
		
		ValueCalculator delegate = getDelegate();
		
		List<StructureChildValueCalculator> calcList = getListOfValueCalculators();
		
		String finalResult = "";
		if(calcList != null)
		{
			for(StructureChildValueCalculator calc : calcList)
			{
				Object calcValue = calc.getValueAdvanced(ctx);
				Object temp = calcValue != null ? calcValue.toString() : "";
				boolean flag = calc.getSkipForEmptyString();
			
				if(flag && (temp == null || temp.equals("")))
				{							
					return null;
				}
							
				finalResult  = finalResult + temp.toString();
	
			}
			
		}
		return finalResult;
	}
	
	


}
