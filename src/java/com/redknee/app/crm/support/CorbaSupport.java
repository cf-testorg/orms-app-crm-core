/*
 * Created on Feb 23, 2005
 *
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
package com.redknee.app.crm.support;

import com.redknee.framework.xhome.context.Context;

import com.redknee.service.corba.CorbaClientProperty;
import com.redknee.util.corba.ConnectionListener;
import com.redknee.util.corba.CorbaClientException;
import com.redknee.util.corba.CorbaClientProxy;

public interface CorbaSupport extends Support
{
	public CorbaClientProxy createProxy(final Context ctx,final CorbaClientProperty property) throws CorbaClientException;

	public CorbaClientProxy createProxy(final Context ctx,final CorbaClientProperty property,ConnectionListener listener) throws CorbaClientException;
}
