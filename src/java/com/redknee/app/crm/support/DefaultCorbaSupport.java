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
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;

import com.redknee.app.crm.log.CRMUtilCorbaLog;
import com.redknee.service.corba.CorbaClientProperty;
import com.redknee.util.corba.ConnectionListener;
import com.redknee.util.corba.ConnectionUpException;
import com.redknee.util.corba.CorbaClientException;
import com.redknee.util.corba.CorbaClientProxy;

public class DefaultCorbaSupport implements CorbaSupport
{
    protected static CorbaSupport instance_ = null;
    public static CorbaSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultCorbaSupport();
        }
        return instance_;
    }

    protected DefaultCorbaSupport()
    {
    }
    
    /**
     * {@inheritDoc}
     */
	public CorbaClientProxy createProxy(final Context ctx,final CorbaClientProperty property) throws CorbaClientException
	{
		CorbaClientProxy proxy =  new CorbaClientProxy(property, new ConnectionListener()
			{

				public void connectionUp() throws ConnectionUpException
				{
					if(LogSupport.isDebugEnabled(ctx))
					{
						new DebugLogMsg(this, "Connected to " + property.getNameServiceContextName() + " on "
							+ property.getNameServiceHost() + ":" + property.getNameServicePort(), null)
							.log(ctx);
					}
				}

				public void connectionDown()
				{
					new MajorLogMsg(this, "Failed connection while connected to " + property.getNameServiceContextName() + " on "
						+ property.getNameServiceHost() + ":" + property.getNameServicePort(), null)
						.log(ctx);
				}

			});
		
		proxy.setLogService( new CRMUtilCorbaLog(ctx, property.getNameServiceContextName() + ":" + property.getNameServiceHost())); 
		return proxy; 
	}

    /**
     * {@inheritDoc}
     */
	public CorbaClientProxy createProxy(final Context ctx,final CorbaClientProperty property,ConnectionListener listener) throws CorbaClientException
	{
		CorbaClientProxy proxy = new CorbaClientProxy(property, listener);
		proxy.setLogService( new CRMUtilCorbaLog(ctx, property.getNameServiceContextName() + ":" + property.getNameServiceHost())); 
		return proxy; 
		
	}
}
