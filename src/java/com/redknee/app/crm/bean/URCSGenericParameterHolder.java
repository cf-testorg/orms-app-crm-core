package com.redknee.app.crm.bean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.redknee.product.bundle.manager.provision.common.param.Parameter;

/**
 * Parameter Holder Object, which can hold input and output parameter map of URCS Generic Parameters.
 * @author sgaidhani
 * @since 9.2
 *
 */
public class URCSGenericParameterHolder {

	public void addInputParameter(Parameter inParam)
	{
		if(inParam != null)
		{
			inputParameterMap.put(inParam.parameterID, inParam);
		}
	}
	
	public void addOutputParameter(Parameter outParam)
	{
		if(outParam != null)
		{
			outputParameterMap.put(outParam.parameterID, outParam);
		}
	}
	
	public Parameter[] getInputParameterArray() 
	{
		Collection<Parameter> collection = inputParameterMap.values();
		Parameter[] inputParams = collection.toArray(new Parameter[0]);

		return inputParams;
	}
	
	public Parameter getOutputParameter(short parameterId) 
	{
		return outputParameterMap.get(parameterId);
	}
	
	public Map<Short, Parameter> getInputParameterMap() {
		return inputParameterMap;
	}

	public Map<Short, Parameter> getOutputParameterMap() {
		return outputParameterMap;
	}
	
	/**
	 * Never allow this map to be set to null. This way we can avoid lots of null checks.
	 * @param outputParameterMap
	 */
	public void setOutputParameterMap(Map<Short, Parameter> outputParameterMap) 
	{
		if(outputParameterMap != null)
		{
			this.outputParameterMap = outputParameterMap;
		}
	}
	
	/**
	 * Never allow this map to be set to null. This way we can avoid lots of null checks.
	 * @param inputParameterMap
	 */
	public void setInputParameterMap(Map<Short, Parameter> inputParameterMap) 
	{
		if(inputParameterMap != null)
		{
			this.inputParameterMap = inputParameterMap;
		}
	}

	// Never allow these maps to be null.
	private Map<Short, Parameter> inputParameterMap = new HashMap<Short, Parameter>();;
	private Map<Short, Parameter> outputParameterMap = new HashMap<Short, Parameter>();;
	
}
