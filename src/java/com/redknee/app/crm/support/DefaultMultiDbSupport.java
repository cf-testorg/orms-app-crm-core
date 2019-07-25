/*
 * Created on 2005-1-12
 */
package com.redknee.app.crm.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.support.DateUtil;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.ValueHoldingVisitor;
import com.redknee.framework.xhome.xdb.AbstractXDBHome;
import com.redknee.framework.xhome.xdb.XDB;
import com.redknee.framework.xhome.xdb.XQL;
import com.redknee.framework.xhome.xdb.mssql.MsSQLXQL;
import com.redknee.framework.xhome.xdb.mysql.MySQLXQL;
import com.redknee.framework.xhome.xdb.oracle.OracleXQL;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;


/**
 * @author jchen
 */
public class DefaultMultiDbSupport implements MultiDbSupport
{
    protected static MultiDbSupport instance_ = null;
    public static MultiDbSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultMultiDbSupport();
        }
        return instance_;
    }

    protected DefaultMultiDbSupport()
    {
    }
    
    
    /**
     * {@inheritDoc}
     */
	public int getDbsType(Context ctx)
	{
        XQL xql=(XQL) ctx.get(XQL.class); 
        
        if ( xql.getDatabaseType().equals(MsSQLXQL.DATABASE_TYPE) )
        {
            return SQLSERVER; 
        } else if ( xql.getDatabaseType().equals(OracleXQL.DATABASE_TYPE) )
        {
            return ORACLE;
        } else if (xql.getDatabaseType().equals(MySQLXQL.DATABASE_TYPE))
        {
            return MYSQL;
        } else 
        {
            return UNKNOWN; 
        }
	}
    
     /**
     * {@inheritDoc}
     */
	public String getTableName(Context ctx, Class key,String defaultName)
	{
		Home home=(Home) ctx.get(key);
		
		if(home==null)
		{
			return defaultName;
		}
		
		try
		{
			Object ret=home.cmd(ctx,AbstractXDBHome.TABLE_NAME);
			if(ret!=null)
			{
				return (String)ret;
			}
		}
		catch (HomeException e)
		{
			if(LogSupport.isDebugEnabled(ctx))
			{
				new DebugLogMsg(DefaultMultiDbSupport.class.getName(),e.getMessage(),e).log(ctx);
			}
		}
		
		return defaultName;
	}
    
    
    /**
     * {@inheritDoc}
     */
	public String getTableName(Context ctx, Home home)
	{
		try
		{
			Object ret=home.cmd(ctx,AbstractXDBHome.TABLE_NAME);
			if(ret!=null)
			{
				return (String)ret;
			}
		}
		catch (HomeException e)
		{
			if(LogSupport.isDebugEnabled(ctx))
			{
				new DebugLogMsg(DefaultMultiDbSupport.class.getName(),e.getMessage(),e).log(ctx);
			}
		}
		
		return null;
	}
    
}
