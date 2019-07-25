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
package com.redknee.app.crm.client;

/**
 * @author ray.chen@redknee.com
 */
public final class ConnectionStatus
{
    private final String _remoteInfo;
    private final ConnectionState _status;
    
    public ConnectionStatus(String remoteInfo, ConnectionState status)
    {
        _remoteInfo = remoteInfo;
        _status = status;
    }
    
    public ConnectionStatus(String remoteInfo, boolean isAlive)
    {
        this(remoteInfo, isAlive?ConnectionState.UP:ConnectionState.DOWN);
    }
    
    public ConnectionStatus(String host, int port)
    {
        this(host, port, ConnectionState.UNINITIALIZED);
    }
    
    public ConnectionStatus(String host, int port, boolean isAlive)
    {
        this(host, port, isAlive?ConnectionState.UP:ConnectionState.DOWN);
    }
    
    public ConnectionStatus(String host, int port, ConnectionState status)
    {
        this(String.format("%s:%d", host, port), status);
    }
    
    public String getRemoteInfo()
    {
        return _remoteInfo;
    }
    
    public boolean isAlive()
    {
        return ConnectionState.UP == _status;
    }
    
    public String getStatus()
    {
        return _status.toString();
    }
}

