package com.redknee.app.crm.calculator;

import java.util.ArrayList;
import java.util.Collection;

import com.redknee.framework.xhome.context.Context;

public class EmptyStringValueCalculator extends AbstractEmptyStringValueCalculator{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmptyStringValueCalculator()
    {
        super();
    }

	@Override
	public Object getValueAdvanced(Context ctx) {
		return "";
	}
	
	/**
     * {@inheritDoc}
     */
    @Override
    public Collection<Object> getDependentContextKeys(Context ctx)
    {
        return new ArrayList<Object>();
    }
	
	

}
