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
package com.redknee.app.crm.support;

import java.util.HashSet;
import java.util.Set;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.client.ConnectionState;
import com.redknee.app.crm.client.ConnectionStatus;
import com.redknee.app.crm.client.RemoteServiceStatus;
import com.redknee.service.corba.CorbaClientProperty;


/**
 * Support method for registering external services.
 * 
 * @author Gary Anderson
 * @author Aaron Gourley
 * 
 * Support clustered corba/rmi client
 * @author rchen
 * @since July 21, 2009 
 */
public class DefaultSystemStatusSupport implements SystemStatusSupport
{
    protected static SystemStatusSupport instance_ = null;
    public static SystemStatusSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultSystemStatusSupport();
        }
        return instance_;
    }

    protected DefaultSystemStatusSupport()
    {
    }
    
    /**
     * This was deprecated, and was kept to support some existent code.
     * especially for EcpRatePlanClient and SmscConnectionExternalService
     * 
     * @param context The operating context.
     * @param key The Context key by which the RemoteServiceStatus can be looked-up.
     */
    @Deprecated
    public void registerExternalService(final Context context, final Object key)
    {
        if (key instanceof RemoteServiceStatus)
        {
            RemoteServiceStatus service = (RemoteServiceStatus)key;
            services.add(service);
        }
        else
        {
            keys.add(key);
        }
    }
    
    public void registerServiceStatus(RemoteServiceStatus status)
    {
        services.add(status);
    }
    
    public Set<RemoteServiceStatus> getServices()
    {
        return services;
    }
    
    
    /**
     * Help existent CORBA/RMI client support RemoteServiceStatus interface
     * @param isAlive
     * @return
     */
    public String generateServiceStatusString(boolean isAlive)
    {
        return (isAlive?"Alive":"Dead");
    }
    
    /**
     * Help existent CORBA/RMI client support RemoteServiceStatus interface
     * @param host
     * @param port
     * @param isAlive
     * @return
     */
    public ConnectionStatus[] generateConnectionStatus(String host, int port, boolean isAlive)
    {
        return new ConnectionStatus[]{new ConnectionStatus(host, port, isAlive)};
    }
    
    /**
     * Help existent CORBA/RMI client support RemoteServiceStatus interface
     * @param host
     * @param port
     * @param isAlive
     * @return
     */
    public ConnectionStatus[] generateConnectionStatus(String host, int port, ConnectionState state)
    {
        return new ConnectionStatus[]{new ConnectionStatus(host, port, state)};
    }

    /**
     * Help existent CORBA/RMI client support RemoteServiceStatus interface
     * @param remoteInfo
     * @param isAlive
     * @return
     */
    public ConnectionStatus[] generateConnectionStatus(String remoteInfo, boolean isAlive)
    {
        return new ConnectionStatus[]{new ConnectionStatus(remoteInfo, isAlive)};
    }

    /**
     * Help existent CORBA/RMI client support RemoteServiceStatus interface
     * @param remoteInfo
     * @param isAlive
     * @return
     */
    public ConnectionStatus[] generateConnectionStatus(String remoteInfo, ConnectionState state)
    {
        return new ConnectionStatus[]{new ConnectionStatus(remoteInfo, state)};
    }

    /**
     * Help existent CORBA/RMI client support RemoteServiceStatus interface
     * @param clientProperty
     * @param isAlive
     * @return
     */
    public ConnectionStatus[] generateConnectionStatus(CorbaClientProperty clientProperty, boolean isAlive)
    {
        if (clientProperty!=null)
        {
            return generateConnectionStatus(clientProperty.getNameServiceHost(), 
                    clientProperty.getNameServicePort(), isAlive);
        }
        else
        {
            return generateConnectionStatus("Unknown", isAlive);
        }
        
    }

    /**
     * Help existent CORBA/RMI client support RemoteServiceStatus interface
     * @param clientProperty
     * @param isAlive
     * @return
     */
    public ConnectionStatus[] generateConnectionStatus(CorbaClientProperty clientProperty, ConnectionState state)
    {
        if (clientProperty!=null)
        {
            return generateConnectionStatus(clientProperty.getNameServiceHost(), 
                    clientProperty.getNameServicePort(), state);
        }
        else
        {
            return generateConnectionStatus("Unknown", state);
        }
        
    }

    public Set<Object> getKeys()
    {
        return keys;
    }

    private static Set<RemoteServiceStatus> services = new HashSet<RemoteServiceStatus>();
    private static Set<Object> keys = new HashSet<Object>();
}
