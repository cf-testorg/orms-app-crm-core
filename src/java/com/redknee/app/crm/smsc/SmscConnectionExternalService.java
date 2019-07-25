/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily 
 * available. Additionally, source code is, by its very nature, confidential 
 * information and inextricably contains trade secrets and other information 
 * proprietary, valuable and sensitive to Redknee, no unauthorised use, 
 * disclosure, manipulation or otherwise is permitted, and may only be used 
 * in accordance with the terms of the licence agreement entered into with 
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.smsc;

import com.redknee.app.crm.client.ConnectionStatus;
import com.redknee.app.crm.client.RemoteServiceStatus;
import com.redknee.app.crm.support.SystemStatusSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.service.oam.LogServiceFactory;
import com.redknee.service.smsc.SmscConnection;
import com.redknee.service.smsc.SmscProperty;
import com.redknee.service.smsc.helper.SmscWorkerAction;


/**
 * External service extension to SMSC connection so that it can show up in the system status GUI
 * 
 * @author Aaron Gourley
 * @since 7.5
 */
public class SmscConnectionExternalService extends SmscConnection implements RemoteServiceStatus
{
    public SmscConnectionExternalService(Context ctx, SmscProperty smscProp)
    {
        this((LogServiceFactory)ctx.get(LogServiceFactory.class), smscProp, new SmscWorkerAction());
    }
    
    public SmscConnectionExternalService(LogServiceFactory logFactory, SmscProperty smscProp, SmscWorkerAction action)
    {
        super(logFactory, smscProp, action);
    }


    public SmscConnectionExternalService(LogServiceFactory logFactory, SmscProperty smscProp)
    {
        super(logFactory, smscProp);
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    /**
     * @deprecated Method SmscConnection is deprecated
     */
    public SmscConnectionExternalService(SmscProperty smscProp, SmscWorkerAction action)
    {
        super(smscProp, action);
    }


    @SuppressWarnings("deprecation")
    @Deprecated
    /**
     * @deprecated Method SmscConnection is deprecated
     */
    public SmscConnectionExternalService(SmscProperty smscProp)
    {
        super(smscProp);
    }


    /* (non-Javadoc)
     * @see com.redknee.app.crm.client.ExternalService#getRemoteInfo()
     */
    public String getRemoteInfo()
    {
        SmscProperty property = this.getProperty();
        if( property != null )
        {
            return property.getHost() + ":" + property.getPort();
        }
        return "";
    }


    /* (non-Javadoc)
     * @see com.redknee.app.crm.client.ExternalService#getServiceDescription()
     */
    public String getDescription()
    {
        StringBuilder sb = new StringBuilder("SMSC client connection");
        SmscProperty property = this.getProperty();
        if( property != null )
        {
            sb.append(" (connection id=").append(property.getId()).append(")");
        }
        return sb.toString();
    }


    /* (non-Javadoc)
     * @see com.redknee.app.crm.client.ExternalService#getServiceName()
     */
    public String getName()
    {
        StringBuilder sb = new StringBuilder("SmscConnection");
        SmscProperty property = this.getProperty();
        if( property != null )
        {
            sb.append("_").append(property.getId());
        }
        return sb.toString();
    }


    /* (non-Javadoc)
     * @see com.redknee.app.crm.client.ExternalService#isServiceAlive()
     */
    public boolean isAlive()
    {
        return isConnected();
    }

    @Override
    public ConnectionStatus[] getConnectionStatus()
    {
        return SystemStatusSupportHelper.get().generateConnectionStatus(getRemoteInfo(), isAlive());
    }

    @Override
    public String getServiceStatus()
    {
        return SystemStatusSupportHelper.get().generateServiceStatusString(isAlive());
    }

}
