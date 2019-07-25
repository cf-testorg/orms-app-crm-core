package com.redknee.app.crm.xdb;

/*
    LargeBlobCustomPricePlanVersionXDBHome
    
    Author : Kevin Greer (via xgen)
    Date   : Tue Apr 18 18:28:11 EDT 2006
    
    Copyright (c) Redknee Inc., 2004
        - all rights reserved
*/

import java.sql.SQLException;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.BeanNotFoundHomeException;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.xdb.AbstractXDBHome;
import com.redknee.framework.xhome.xdb.DefaultXPreparedStatement;
import com.redknee.framework.xhome.xdb.XDB;
import com.redknee.framework.xhome.xdb.XPreparedStatement;
import com.redknee.framework.xhome.xdb.XStatement;

import com.redknee.app.crm.bean.PricePlanVersion;
import com.redknee.app.crm.bean.PricePlanVersionHome;
import com.redknee.app.crm.bean.ServicePackageVersionXMLSupport;

public class LargeBlobCustomPricePlanVersionXDBHome
        extends AbstractXDBHome
        implements PricePlanVersionHome
{

    public final static String DEFAULT_TABLE_NAME = "XPricePlanVersion";


    public LargeBlobCustomPricePlanVersionXDBHome(Context ctx)
    {
        this(ctx, PricePlanVersion.class, DEFAULT_TABLE_NAME);
    }


    public LargeBlobCustomPricePlanVersionXDBHome(Context ctx, String tableName)
    {
        this(ctx, PricePlanVersion.class, tableName);
    }


    public LargeBlobCustomPricePlanVersionXDBHome(Context ctx, Class cls, String tableName)
    {
        super(ctx, cls, tableName);
    }


    public Object create(Context ctx, Object obj)
            throws HomeException
    {
        XDB xdb = (XDB) ctx.get(XDB.class);


        final PricePlanVersion bean = (PricePlanVersion) obj;


        int count = xdb.execute(ctx, new XStatement()
        {
            public String createStatement(Context ctx)
            {
                return "INSERT INTO " + getTableName() + " ( id, version, activation, activateDate, createdDate, deposit, creditLimit, defaultPerMinuteAirRate, overusageVoiceRate, overusageSmsRate, overusageDataRate, servicePackageVersion, description, chargecycle, charge ) VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ?, ?  )";
            }

            public void set(Context ctx, XPreparedStatement ps)
                    throws SQLException
            {
                if (ps instanceof DefaultXPreparedStatement)
                {
                    ((DefaultXPreparedStatement) ps).getStatement();

                    ps = new LargeBlobXPreparedStatement(ctx, ((DefaultXPreparedStatement) ps).getStatement());
                }

                ps.setLong(bean.getId());
                ps.setInt(bean.getVersion());
                ps.setDate(bean.getActivation());
                ps.setDate(bean.getActivateDate());
                ps.setDate(bean.getCreatedDate());
                ps.setLong(bean.getDeposit());
                ps.setLong(bean.getCreditLimit());
                ps.setLong(bean.getDefaultPerMinuteAirRate());
                ps.setLong(bean.getOverusageVoiceRate());
                ps.setLong(bean.getOverusageSmsRate());
                ps.setLong(bean.getOverusageDataRate());
                ps.setString(ServicePackageVersionXMLSupport.instance().toXMLString(bean.getServicePackageVersion()));
                ps.setString(bean.getDescription());
                ps.setLong(bean.getChargeCycle().getIndex());
                ps.setLong(bean.getCharge());
            }
        });

        if (count != 1)
        {
            throw new HomeException("XDB Error on create.");
        }

        return bean;
    }


    public Object store(Context ctx, Object obj)
            throws HomeException
    {
        XDB xdb = (XDB) ctx.get(XDB.class);

        final PricePlanVersion bean = (PricePlanVersion) obj;

        int count = xdb.execute(ctx, new XStatement()
        {
            public String createStatement(Context ctx)
            {
                return "UPDATE " + getTableName() + " SET activation = ?, activateDate = ?, createdDate = ?, deposit = ?, creditLimit = ?, defaultPerMinuteAirRate = ?, overusageVoiceRate = ?, overusageSmsRate = ?, overusageDataRate = ?, servicePackageVersion = ?, description = ?, chargeCycle = ?, charge = ? WHERE id = ? AND version = ? ";
            }

            public void set(Context ctx, XPreparedStatement ps)
                    throws SQLException
            {
                if (ps instanceof DefaultXPreparedStatement)
                {
                    ((DefaultXPreparedStatement) ps).getStatement();

                    ps = new LargeBlobXPreparedStatement(ctx, ((DefaultXPreparedStatement) ps).getStatement());
                }
                // SET
                ps.setDate(bean.getActivation());
                ps.setDate(bean.getActivateDate());
                ps.setDate(bean.getCreatedDate());
                ps.setLong(bean.getDeposit());
                ps.setLong(bean.getCreditLimit());
                ps.setLong(bean.getDefaultPerMinuteAirRate());
                ps.setLong(bean.getOverusageVoiceRate());
                ps.setLong(bean.getOverusageSmsRate());
                ps.setLong(bean.getOverusageDataRate());
                ps.setString(ServicePackageVersionXMLSupport.instance().toXMLString(bean.getServicePackageVersion()));
                ps.setString(bean.getDescription());
                ps.setLong(bean.getChargeCycle().getIndex());
                ps.setLong(bean.getCharge());

                // WHERE
                ps.setLong(bean.getId());
                ps.setInt(bean.getVersion());
            }
        });

        if (count != 1)
        {
            throw new BeanNotFoundHomeException();
        }

        return bean;
    }

}

