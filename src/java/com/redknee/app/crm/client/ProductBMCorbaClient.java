/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.client;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAwareSupport;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.support.CorbaSupportHelper;
import com.redknee.app.crm.support.SystemStatusSupportHelper;
import com.redknee.service.corba.CorbaClientProperty;
import com.redknee.service.corba.CorbaClientPropertyHome;
import com.redknee.util.corba.ConnectionListener;
import com.redknee.util.corba.ConnectionUpException;


/**
 * Provides an interface for communicating with Bundle Manager using corba
 * 
 */
public class ProductBMCorbaClient extends ContextAwareSupport implements RemoteServiceStatus, ConnectionListener
{

    public ProductBMCorbaClient(Context ctx)
    {
        setContext(ctx);
        init();
    }


    protected void init()
    {
        if (corbaClientPropertyName != null && corbaClientPropertyName.length()>0)
            {
            try
            {
                isConnected_ = false;
                Home corbaClientPropertyHome = null;
                corbaClientPropertyHome = (Home) getContext().get(CorbaClientPropertyHome.class);
                if (corbaClientPropertyHome == null)
                {
                    new MinorLogMsg(this, "Corba client configuration does not exist", null).log(getContext());
                    throw new IllegalArgumentException("Corba client configuration does not exist");
                }
                BMProperty_ = (CorbaClientProperty) corbaClientPropertyHome.find(getContext(), corbaClientPropertyName);
                if (BMProperty_ == null)
                {
                    new MinorLogMsg(this, "Configuration error: can not find CorbaClientProperty " + corbaClientPropertyName, null)
                            .log(getContext());
                    throw new IllegalArgumentException(
                            "Configuration error: can not find CorbaClientProperty " + corbaClientPropertyName);
                }
                new InfoLogMsg(this, BMProperty_.toString(), null).log(getContext());
                corbaProxy_ = CorbaSupportHelper.get(getContext()).createProxy(getContext(), BMProperty_, this);
            }
            catch (Throwable e)
            {
                new MinorLogMsg(this, "Unable to load corba proxy for " + corbaClientPropertyName, e).log(getContext());
            }
        }
    }


    public synchronized void invalidate(Throwable t)
    {
        if (corbaProxy_ != null)
        {
            corbaProxy_.invalidate();
        }
    }


    public String getDescription()
    {
        return "CORBA client for Bundle Manager services.";
    }

    
    public String getName()
    {
        if (BMProperty_ != null)
        {
            return BMProperty_.getKey();
        }
        return corbaClientPropertyName;
    }


    public boolean isAlive()
    {
        return isConnected_;
    }


    public void connectionDown()
    {
        isConnected_ = false;
    }


    public void connectionUp() throws ConnectionUpException
    {
        isConnected_ = true;
    }

    protected boolean isConnected_;
    protected com.redknee.util.corba.CorbaClientProxy corbaProxy_;
    protected String corbaClientPropertyName;
    protected CorbaClientProperty BMProperty_;
    @Override
    public ConnectionStatus[] getConnectionStatus()
    {
        return SystemStatusSupportHelper.get().generateConnectionStatus(BMProperty_, isAlive());
    }


    @Override
    public String getServiceStatus()
    {
        return SystemStatusSupportHelper.get().generateServiceStatusString(isAlive());
    }
}
