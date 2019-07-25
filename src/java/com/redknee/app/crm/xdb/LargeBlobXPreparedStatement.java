/*
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
package com.redknee.app.crm.xdb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.xdb.DefaultXPreparedStatement;


/**
 * Hack hack hack...
 * 
 * @author danny.ng@redknee.com
 * @since May 15, 2006
 *
 */
public class LargeBlobXPreparedStatement extends DefaultXPreparedStatement
{
    public LargeBlobXPreparedStatement(Context ctx, PreparedStatement ps)
    {
        super(ctx, ps);
    }

    
    public void setString(String value) throws SQLException
    {
        /*
         * This line below is the what is used in DefaultXPreparedStatement after Kevin's fix
         * unfortunately, it depends on Java 1.5. 
         */
        //getStatement().setClob(nextIndex(), new javax.sql.rowset.serial.SerialClob(c));
        
        /*
         * We use this line below instead of the above; it is the code found within the JDBC Home.
         */
        getStatement().setCharacterStream(nextIndex(), new java.io.StringReader(value), value.length());
        /*
        CLOB clob = oracle.sql.CLOB.empty_lob();
        int columnNum = nextIndex();
        clob.setString(columnNum, value);
        getStatement().setClob(columnNum, clob);
        */
        
        
        
        
    }
    
    
    public void setBlob(String value) throws SQLException
    {
        /*
         * This line below is the what is used in DefaultXPreparedStatement after Kevin's fix
         * unfortunately, it depends on Java 1.5. 
         */
        //getStatement().setClob(nextIndex(), new javax.sql.rowset.serial.SerialClob(c));
        
        /*
         * We use this line below instead of the above; it is the code found within the JDBC Home.
         */
        getStatement().setCharacterStream(nextIndex(), new java.io.StringReader(value), value.length());
    }
}
