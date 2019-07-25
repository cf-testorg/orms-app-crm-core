package com.redknee.app.crm.calculator;

import com.redknee.framework.xhome.context.Context;

/**
 * This class will copy the value of one context key to another context key.
 * If the invoker itself creates subContext, then the value will be placed in subContext and automatically wiped out after the call.
 * @author sgaidhani
 *
 */
public class ContextKeyCopyingValueCalculator extends AbstractContextKeyCopyingValueCalculator
implements ValueCalculator{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object getValueAdvanced(Context ctx) {
		
		Object contextObjectValue = ctx.get(getContextKey());
		
		ctx.put(getContextNewKey(), contextObjectValue);
		
		return super.getValueAdvanced(ctx);
	}
	
	

}
