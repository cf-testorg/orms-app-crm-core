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

import static com.redknee.app.crm.client.CorbaClientTrapIdDef.UNDEFINED_ENTRY_ID;

import java.util.concurrent.atomic.AtomicReference;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.EntryLogMsg;

import com.redknee.app.urcs.client.agent.ConnectionStatusHub;
import com.redknee.service.proxy.ConnectionInfo;
import com.redknee.service.proxy.ConnectionListener;

import com.redknee.app.crm.support.SystemStatusSupportHelper;

/**
 * @author rchen
 * @param <T> T must be the type of the remote interface, PromotionProvision, for example
 */
public final class RemoteServiceStatusImpl<T> implements ConnectionListener, RemoteServiceStatus
{
    private final Context                          _ctx;
    private final String                           _name;
    private final String                           _desc;
    private final Class<T>                         _serviceKey;
    private final int                              _trapId;
    private final int                              _clearId;
    private final ConnectionStatusHub              _connectionHub;
    private final AtomicReference<ConnectionState> _status = new AtomicReference<ConnectionState>(ConnectionState.UNINITIALIZED);
    

    public RemoteServiceStatusImpl(Context ctx, String name, String desc, Class<T> serviceType, int trapId, int clearId)
    {
        _ctx = ctx;
        _name = name;
        _desc = desc;
        _serviceKey = serviceType;
        _trapId = trapId;
        _clearId = clearId;

        //final T client = (T) ctx.get(_serviceKey);
        //ConnectionState initState = (client == null) ? ConnectionState.UNINITIALIZED : ConnectionState.UP;
        //_status.set(initState);
        
        _connectionHub = (ConnectionStatusHub) ctx.get(ConnectionStatusHub.class);
        if (_connectionHub != null)
        {
            _connectionHub.addConnectionListener(_serviceKey, this);
        }
        
        SystemStatusSupportHelper.get(ctx).registerServiceStatus(this);
    }
    
    public RemoteServiceStatusImpl(Context ctx, String name, String desc, Class<T> serviceType)
    {
        this(ctx, name, desc, serviceType, UNDEFINED_ENTRY_ID, UNDEFINED_ENTRY_ID);
    }


    @Override
    public String getDescription()
    {
        return _desc;
    }


    @Override
    public String getName()
    {
        return _name;
    }


    @Override
    public String getServiceStatus()
    {
        return _status.get().toString();
    }


    @Override
    public void linkdown()
    {
        // To avoid raising alarm repeatedly
        if (_status.get() != ConnectionState.DOWN)
        {
            _status.set(ConnectionState.DOWN);
            if (_trapId!=UNDEFINED_ENTRY_ID)
            {
                new EntryLogMsg(_trapId, this, getName(), "", new String[]{"",""}, null).log(_ctx);
            }
        }
    }


    @Override
    public void linkup()
    {
        // To avoid raising alarm repeatedly
        if (_status.get() != ConnectionState.UP)
        {
            _status.set(ConnectionState.UP);
            if (_clearId!=UNDEFINED_ENTRY_ID)
            {
                new EntryLogMsg(_clearId, this, getName(), "", new String[]{"",""}, null).log(_ctx);
            }
        }
    }


    @Override
    public ConnectionStatus[] getConnectionStatus()
    {
        ConnectionInfo[] infos = _connectionHub.getConnectionInfo(_serviceKey);

        int size = infos.length;

        if (size > 0)
        {
            ConnectionStatus[] ret = new ConnectionStatus[size];

            for (int i = 0; i < size; i++)
            {
                ConnectionInfo info = infos[i];
                ConnectionState state = (info.isConnected) ? ConnectionState.UP : ConnectionState.DOWN;
                ConnectionStatus conn = new ConnectionStatus(info.hostname, info.port, state);
                ret[i] = conn;
            }

            return ret;
        }
        else
        {
            return new ConnectionStatus[] { new ConnectionStatus("Unknown", ConnectionState.UNINITIALIZED) };
        }
    }


    @Override
    public boolean isAlive()
    {
        return ConnectionState.UP == _status.get();
    }
}
