/*
 * Created on 2005-1-12
 */
package com.redknee.app.crm.support;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;


/**
 * @author jchen
 */
public interface MultiDbSupport extends Support
{
    public final static String ORACLE_DATE_FORMAT_PATTERN    = "yyyy-MM-dd HH:mm:ss";
       
    public final static String DATE_FORMAT_PATTERN           = "yyyy.MM.dd HH:mm:ss:SSS Z";
       
    public static final int UNKNOWN=0;
    public static final int ORACLE=1;
    public static final int MYSQL=2;   
    public static final int SQLSERVER = 3; 
    
    public SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd kk:mm:ss");
    
	/**
	 * Gets system Database Server Type
	 * @param ctx
	 * @return
	 */
	public int getDbsType(Context ctx);
	

	/**
	 * Finds the table name of a home. the home is searched in the context by the key passed in. If
	 * The home is not found, the default name is returned.
	 * 
	 * @param ctx
	 * @param key
     * @param defaultName
	 * @return
	 */
	public String getTableName(Context ctx, Class key,String defaultName);

	/**
	 * Finds the table name of a home. the home is searched in the context by the key passed in. If
	 * The home is not found, the default name is returned.
	 * 
	 * @param ctx
	 * @param home
	 * @return
	 */
	public String getTableName(Context ctx, Home home);
	

}
