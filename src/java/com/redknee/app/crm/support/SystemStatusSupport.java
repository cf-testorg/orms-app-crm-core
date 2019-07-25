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
public interface SystemStatusSupport extends Support
{
    /**
     * This was deprecated, and was kept to support some existent code.
     * especially for EcpRatePlanClient and SmscConnectionExternalService
     * 
     * @param context The operating context.
     * @param key The Context key by which the RemoteServiceStatus can be looked-up.
     * @deprecated use registerServiceStatus()
     */
    @Deprecated
    public void registerExternalService(final Context context, final Object key);
    
    public void registerServiceStatus(RemoteServiceStatus status);
    
    public Set<RemoteServiceStatus> getServices();
    
    
    /**
     * Help existent CORBA/RMI client support RemoteServiceStatus interface
     * @param isAlive
     * @return
     */
    public String generateServiceStatusString(boolean isAlive);
    
    /**
     * Help existent CORBA/RMI client support RemoteServiceStatus interface
     * @param host
     * @param port
     * @param isAlive
     * @return
     */
    public ConnectionStatus[] generateConnectionStatus(String host, int port, boolean isAlive);
    
    /**
     * Help existent CORBA/RMI client support RemoteServiceStatus interface
     * @param host
     * @param port
     * @param isAlive
     * @return
     */
    public ConnectionStatus[] generateConnectionStatus(String host, int port, ConnectionState state);

    /**
     * Help existent CORBA/RMI client support RemoteServiceStatus interface
     * @param remoteInfo
     * @param isAlive
     * @return
     */
    public ConnectionStatus[] generateConnectionStatus(String remoteInfo, boolean isAlive);

    /**
     * Help existent CORBA/RMI client support RemoteServiceStatus interface
     * @param remoteInfo
     * @param isAlive
     * @return
     */
    public ConnectionStatus[] generateConnectionStatus(String remoteInfo, ConnectionState state);

    /**
     * Help existent CORBA/RMI client support RemoteServiceStatus interface
     * @param clientProperty
     * @param isAlive
     * @return
     */
    public ConnectionStatus[] generateConnectionStatus(CorbaClientProperty clientProperty, boolean isAlive);

    /**
     * Help existent CORBA/RMI client support RemoteServiceStatus interface
     * @param clientProperty
     * @param isAlive
     * @return
     */
    public ConnectionStatus[] generateConnectionStatus(CorbaClientProperty clientProperty, ConnectionState state);

    public Set<Object> getKeys();
}
