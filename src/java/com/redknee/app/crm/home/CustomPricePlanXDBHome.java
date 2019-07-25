/*
 * Copyright (c) 2007, REDKNEE.com. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of REDKNEE.com.
 * ("Confidential Information"). You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement you entered
 * into with REDKNEE.com.
 * 
 * REDKNEE.COM MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT.
 * REDKNEE.COM SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.redknee.app.crm.home;

import java.sql.SQLException;

import com.redknee.app.crm.bean.PricePlan;
import com.redknee.app.crm.bean.PricePlanXDBHome;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.xdb.AutoKeySet;
import com.redknee.framework.xhome.xdb.XDB;
import com.redknee.framework.xhome.xdb.XPreparedStatement;
import com.redknee.framework.xhome.xdb.XQL;
import com.redknee.framework.xhome.xdb.XStatement;
import com.redknee.framework.xhome.xdb.mssql.InsertWithoutIdentityXStatement;


/**
 * @author bpandey
 * 
 */
public class CustomPricePlanXDBHome extends PricePlanXDBHome
{

    /**
     * @param ctx
     * @param cls
     * @param tableName
     */
    public CustomPricePlanXDBHome(Context ctx, Class cls, String tableName)
    {
        super(ctx, cls, tableName);
    }


    public Object create(Context ctx, Object obj) throws HomeException
    {
        XDB xdb = (XDB) ctx.get(XDB.class);
        // clone so that updated sequence numbers don't leak back into 'obj'
        try
        {
            obj = ((com.redknee.framework.xhome.beans.XCloneable) obj).clone();
        }
        catch (CloneNotSupportedException e)
        {
        }
        final PricePlan bean = (PricePlan) obj;
        // for each AUTOINC field update then call super.
        XQL xql = (XQL) ctx.get(XQL.class);
        if (xql.isSuportIdentity())
        {
            if (bean.getId() > 1)
            {
                final String tb_ = getTableName();
                int count = xdb.execute(ctx, new InsertWithoutIdentityXStatement()
                {

                    public String getTableName()
                    {
                        return tb_;
                    }


                    public String createStatement(Context ctx)
                    {
                        return "INSERT INTO "
                                + getTableName()
                                + " ( id, name, spid, subscriptionType, technology, pricePlanType, pricePlanFunction, PricePlanGroup, subscriptionLevel, voiceRatePlan, SMSRatePlan, DataRatePlan, expiryExtention, expiryExtensionMode, currentVersionChargeCycle, currentVersionCharge, currentVersion, nextVersion, enabled, billingMessage, applyContractDurationCriteria, minContractDuration, maxContractDuration, contractDurationUnits, sendswitchnotification) VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ?  )";
                    }


                    public void set(Context ctx, XPreparedStatement ps) throws SQLException
                    {
                        ps.setLong(bean.getId());
                        ps.setString(bean.getName());
                        ps.setInt(bean.getSpid());
                        ps.setLong(bean.getSubscriptionType());
                        ps.setShort(bean.getTechnology().getIndex());
                        ps.setShort(bean.getPricePlanType().getIndex());
                        ps.setShort(bean.getPricePlanFunction().getIndex());
                        ps.setLong(bean.getPricePlanGroup());
                        ps.setLong(bean.getSubscriptionLevel());
                        ps.setString(bean.getVoiceRatePlan());
                        ps.setString(bean.getSMSRatePlan());
                        ps.setString(bean.getDataRatePlan());
                        ps.setInt(bean.getExpiryExtention());
                        ps.setShort(bean.getExpiryExtensionMode().getIndex());
                        ps.setShort(bean.getCurrentVersionChargeCycle().getIndex());
                        ps.setLong(bean.getCurrentVersionCharge());
                        ps.setInt(bean.getCurrentVersion());
                        ps.setInt(bean.getNextVersion());
                        ps.setString(bean.getEnabled() ? "y" : "n");
                        ps.setString(bean.getBillingMessage());
                        ps.setString(bean.getApplyContractDurationCriteria() ? "y" : "n");
                        ps.setLong(bean.getMinContractDuration());
                        ps.setLong(bean.getMaxContractDuration());
                        ps.setShort(bean.getContractDurationUnits().getIndex());
                        ps.setString(bean.getSendswitchnotification()? "y" : "n");
                    }
                });
            }
            else
            {
                AutoKeySet resultSet = xdb.executeInsert(ctx, new XStatement()
                {

                    public String createStatement(Context ctx)
                    {
                        return "INSERT INTO "
                                + getTableName()
                                + " ( name, spid, subscriptionType, technology, pricePlanType, pricePlanFunction, PricePlanGroup, subscriptionLevel, voiceRatePlan, SMSRatePlan, DataRatePlan, expiryExtention, expiryExtensionMode, currentVersionChargeCycle, currentVersionCharge, currentVersion, nextVersion, enabled, billingMessage, applyContractDurationCriteria, minContractDuration, maxContractDuration, contractDurationUnits, sendswitchnotification ) VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ?  )";
                    }


                    public void set(Context ctx, XPreparedStatement ps) throws SQLException
                    {
                        ps.setString(bean.getName());
                        ps.setInt(bean.getSpid());
                        ps.setLong(bean.getSubscriptionType());
                        ps.setShort(bean.getTechnology().getIndex());
                        ps.setShort(bean.getPricePlanType().getIndex());
                        ps.setShort(bean.getPricePlanFunction().getIndex());
                        ps.setLong(bean.getPricePlanGroup());
                        ps.setLong(bean.getSubscriptionLevel());
                        ps.setString(bean.getVoiceRatePlan());
                        ps.setString(bean.getSMSRatePlan());
                        ps.setString(bean.getDataRatePlan());
                        ps.setInt(bean.getExpiryExtention());
                        ps.setShort(bean.getExpiryExtensionMode().getIndex());
                        ps.setShort(bean.getCurrentVersionChargeCycle().getIndex());
                        ps.setLong(bean.getCurrentVersionCharge());
                        ps.setInt(bean.getCurrentVersion());
                        ps.setInt(bean.getNextVersion());
                        ps.setString(bean.getEnabled() ? "y" : "n");
                        ps.setString(bean.getBillingMessage());
                        ps.setString(bean.getApplyContractDurationCriteria() ? "y" : "n");
                        ps.setLong(bean.getMinContractDuration());
                        ps.setLong(bean.getMaxContractDuration());
                        ps.setShort(bean.getContractDurationUnits().getIndex());
                        ps.setString(bean.getSendswitchnotification()? "y" : "n");
                    }
                });
                int autoKeyCount = 0;
                // for each AUTOINC field update then call super.
                bean.setId(resultSet.getLong(autoKeyCount++));
            }
        }
        else
        {
            if (bean.getId() == 1)
                bean.setId(Math.max(1, xql.nextSequence(ctx, getTableName(), "id")));
            int count = xdb.execute(ctx, new XStatement()
            {

                public String createStatement(Context ctx)
                {
                    return "INSERT INTO "
                            + getTableName()
                            + " ( id, name, spid, subscriptionType, technology, pricePlanType, pricePlanFunction, PricePlanGroup, subscriptionLevel, voiceRatePlan, SMSRatePlan, DataRatePlan, expiryExtention, expiryExtensionMode, currentVersionChargeCycle, currentVersionCharge, currentVersion, nextVersion, enabled, billingMessage, applyContractDurationCriteria, minContractDuration, maxContractDuration, contractDurationUnits, sendswitchnotification) VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ?  )";
                }


                public void set(Context ctx, XPreparedStatement ps) throws SQLException
                {
                    ps.setLong(bean.getId());
                    ps.setString(bean.getName());
                    ps.setInt(bean.getSpid());
                    ps.setLong(bean.getSubscriptionType());
                    ps.setShort(bean.getTechnology().getIndex());
                    ps.setShort(bean.getPricePlanType().getIndex());
                    ps.setShort(bean.getPricePlanFunction().getIndex());
                    ps.setLong(bean.getPricePlanGroup());
                    ps.setLong(bean.getSubscriptionLevel());
                    ps.setString(bean.getVoiceRatePlan());
                    ps.setString(bean.getSMSRatePlan());
                    ps.setString(bean.getDataRatePlan());
                    ps.setInt(bean.getExpiryExtention());
                    ps.setShort(bean.getExpiryExtensionMode().getIndex());
                    ps.setShort(bean.getCurrentVersionChargeCycle().getIndex());
                    ps.setLong(bean.getCurrentVersionCharge());
                    ps.setInt(bean.getCurrentVersion());
                    ps.setInt(bean.getNextVersion());
                    ps.setString(bean.getEnabled() ? "y" : "n");
                    ps.setString(bean.getBillingMessage());
                    ps.setString(bean.getApplyContractDurationCriteria() ? "y" : "n");
                    ps.setLong(bean.getMinContractDuration());
                    ps.setLong(bean.getMaxContractDuration());
                    ps.setShort(bean.getContractDurationUnits().getIndex());
                    ps.setString(bean.getSendswitchnotification()? "y" : "n");
                }
            });
            if (count != 1)
            {
                throw new HomeException("XDB Error on create.");
            }
        }
        return bean;
    }
}
