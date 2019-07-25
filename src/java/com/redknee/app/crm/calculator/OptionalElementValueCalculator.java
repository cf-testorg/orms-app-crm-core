package com.redknee.app.crm.calculator;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;


public class OptionalElementValueCalculator extends AbstractOptionalElementValueCalculator{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OptionalElementValueCalculator()
	{
		super();
	}

	@Override
	public Object getValueAdvanced(Context ctx)
	{
		Object calculatedValue = getDelegate().getValueAdvanced(ctx);
		if(calculatedValue == null || 
				(getSkipForEmptyString() == true && "".equals(calculatedValue.toString().trim())))
		{
		    if(LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(DefaultValueCalculator.class.getName(), "Calculated value is null returning a default claculated value " + calculatedValue, null);
            }
			return "";
		}

		if(LogSupport.isDebugEnabled(ctx))
        {
		    if(calculatedValue.getClass().getName() != null)
		    {
		        new DebugLogMsg(DefaultValueCalculator.class.getName(), "Returning a calculated value from valuecalculator " + calculatedValue.getClass().getName(), null);
		    }
        }
		return getPreElementContent() + calculatedValue + getPostElementContent();
	}
}
