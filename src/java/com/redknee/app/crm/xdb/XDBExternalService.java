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
package com.redknee.app.crm.xdb;

import com.redknee.app.crm.client.ConnectionStatus;
import com.redknee.app.crm.client.RemoteServiceStatus;
import com.redknee.app.crm.support.SystemStatusSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAwareSupport;
import com.redknee.framework.xhome.xdb.ConnectionStatusEnum;
import com.redknee.framework.xhome.xdb.XDBConfiguration;
import com.redknee.framework.xhome.xdb.XDBDriver;
import com.redknee.framework.xhome.xdb.XDBDriverProxy;
import com.redknee.framework.xhome.xdb.XDBMgr;
import com.redknee.framework.xhome.xdb.mssql.MsSQLDriver;
import com.redknee.framework.xhome.xdb.mysql.MySQLDriver;
import com.redknee.framework.xhome.xdb.oracle.OracleDriverManager;


/**
 * An external service for XDB connections.  This convenience class was created so that XDB related
 * entries can show up in the external services status page in the GUI.
 * 
 * @author Aaron Gourley
 * @since 7.5
 */
public class XDBExternalService extends ContextAwareSupport implements RemoteServiceStatus
{
    public XDBExternalService(Context ctx)
    {
        this(ctx, "default");
    }

    public XDBExternalService(Context ctx, String xdbConfigId)
    {
        setContext(ctx);
        xdbConfigId_ = xdbConfigId;
        
        XDBMgr xdbMgr = (XDBMgr)getContext().get(XDBMgr.class);
        xdbConfig_  = (XDBConfiguration) xdbMgr.getConfigurations().get(xdbConfigId_);
    }
    
    /* (non-Javadoc)
     * @see com.redknee.app.crm.client.ExternalService#getRemoteInfo()
     */
    public String getRemoteInfo()
    {
        if( xdbConfig_ != null )
        {
            XDBDriver driver = getBaseDriver();
            if( driver instanceof OracleDriverManager )
            {
                OracleDriverManager oracleDriver = ((OracleDriverManager)driver);
                return oracleDriver.getDatabaseName() + "@" + oracleDriver.getHostname() + ":" + oracleDriver.getPort();
            }
            if( driver instanceof MySQLDriver )
            {
                MySQLDriver mySqlDriver = ((MySQLDriver)driver);
                return mySqlDriver.getDatabaseName() + "@" + mySqlDriver.getHostname() + ":" + mySqlDriver.getPort();
            }
            if( driver instanceof MsSQLDriver )
            {
                MsSQLDriver msSqlDriver = ((MsSQLDriver)driver);
                return msSqlDriver.getDatabaseName() + "@" + msSqlDriver.getHostname() + ":" + msSqlDriver.getPort();
            }
        }
        return "";
    }

    /* (non-Javadoc)
     * @see com.redknee.app.crm.client.ExternalService#getServiceDescription()
     */
    public String getDescription()
    {
        return "Database client for " + xdbConfigId_ + " XDB connection";
    }

    /* (non-Javadoc)
     * @see com.redknee.app.crm.client.ExternalService#getServiceName()
     */
    public String getName()
    {
        return "XDB-" + xdbConfigId_;
    }

    /* (non-Javadoc)
     * @see com.redknee.app.crm.client.ExternalService#isServiceAlive()
     */
    public boolean isAlive()
    {
        if( xdbConfig_ != null )
        {
            XDBDriver driver = getBaseDriver();

            if( driver instanceof OracleDriverManager )
            {
                OracleDriverManager oracleDriver = ((OracleDriverManager)driver);
                return ConnectionStatusEnum.UP.equals(oracleDriver.getStatus());
            }
            else if( driver instanceof MySQLDriver )
            {
                MySQLDriver mySqlDriver = ((MySQLDriver)driver);
                return ConnectionStatusEnum.UP.equals(mySqlDriver.getStatus());
            }
            else if( driver instanceof MsSQLDriver )
            {
                MsSQLDriver msSqlDriver = ((MsSQLDriver)driver);
                return ConnectionStatusEnum.UP.equals(msSqlDriver.getStatus());
            }

        }
        return false;
    }

    private XDBDriver getBaseDriver()
    {
        XDBDriver driver = xdbConfig_.getDriver();
        while( driver instanceof XDBDriverProxy )
        {
            driver = ((XDBDriverProxy)driver).getDriver();
        }
        return driver;
    }

    private XDBConfiguration xdbConfig_ = null;
    private String xdbConfigId_ = null;
    @Override
    public ConnectionStatus[] getConnectionStatus()
    {
        return SystemStatusSupportHelper.get().generateConnectionStatus(this.getRemoteInfo(), isAlive());
    }

    @Override
    public String getServiceStatus()
    {
        return SystemStatusSupportHelper.get().generateServiceStatusString(isAlive());
    }
}
