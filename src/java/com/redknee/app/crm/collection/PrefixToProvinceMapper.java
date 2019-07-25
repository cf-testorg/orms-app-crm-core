package com.redknee.app.crm.collection;

import com.redknee.framework.xhome.context.Context;

public interface PrefixToProvinceMapper
{
	
	/**
	 * Returns the code of a province to which identifier belongs. 
	 * The lookup will be based on longest prefix matching the identifier. 
	 * Returns empty string for no match found.
	 * 
	 * @param ctx Context
	 * @param identifier The identifier (MSISDN or MSC ID) to be matched against the prefixes
	 * @return the code of the province to which identifier belongs.
	 */
	public String getProvince(Context ctx, String identifier);

}


