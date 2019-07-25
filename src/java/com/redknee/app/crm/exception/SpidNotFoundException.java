/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s).  A complete listing of authors of this work is readily
 * available.  Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee.  No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the license agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.exception;

import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;


/**
 * This class can be used to throw Unchecked Exception if SPID doesn't exist on BSS
 * @author shailesh.makhijani
 * @since 9.6
 */
public class SpidNotFoundException extends IllegalPropertyArgumentException {

	/**
	 * @param propertyInfo
	 * @param msg
	 */
	public SpidNotFoundException(PropertyInfo propertyInfo, String msg) {
		super(propertyInfo, msg);
	}

	/**
	 * @param property
	 * @param msg
	 */
	public SpidNotFoundException(String property, String msg)
    {
        super(property, msg);
    }


	/**
	 * @param propertyInfo
	 * @param throwable
	 */
    public SpidNotFoundException(PropertyInfo propertyInfo, Throwable t)
    {
        super(propertyInfo, t);
    }


	private static final long serialVersionUID = 1L;

}
