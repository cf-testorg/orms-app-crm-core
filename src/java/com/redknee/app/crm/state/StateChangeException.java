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
package com.redknee.app.crm.state;

import com.redknee.framework.xhome.home.HomeException;

/**
 * @author jchen
 */
public class StateChangeException extends HomeException {


	/**
	 * @param arg0
	 */
	public StateChangeException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public StateChangeException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public StateChangeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
