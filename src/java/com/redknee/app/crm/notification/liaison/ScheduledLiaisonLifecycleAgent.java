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
package com.redknee.app.crm.notification.liaison;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.redknee.app.crm.notification.ScheduledNotification;
import com.redknee.app.crm.notification.ScheduledNotificationHome;
import com.redknee.app.crm.notification.ScheduledNotificationXDBHome;
import com.redknee.app.crm.notification.ScheduledNotificationXInfo;
import com.redknee.app.crm.support.CalendarSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.app.crm.support.MultiDbSupportHelper;
import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.lifecycle.LifecycleAgentControl;
import com.redknee.framework.lifecycle.LifecycleException;
import com.redknee.framework.lifecycle.LifecycleStateEnum;
import com.redknee.framework.lifecycle.RunnableLifecycleAgentSupport;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.LTE;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xhome.xdb.XDB;
import com.redknee.framework.xhome.xdb.XResultSet;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;

/**
 * Package-private lifecycle agent class for use by {@link ScheduledTaskNotificationLiaison).
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
class ScheduledLiaisonLifecycleAgent extends RunnableLifecycleAgentSupport
{
    public ScheduledLiaisonLifecycleAgent(Context ctx, String scheduleName, String agentId) throws AgentException
    {
        super(ctx, agentId);
        
        
        LogSupport.info(ctx, this, "Inside ScheduledLiaisonLifecycleAgent.");
        
        scheduleName_ = scheduleName;
        
        try
        {
            LifecycleAgentControl ctl = HomeSupportHelper.get(ctx).findBean(ctx, LifecycleAgentControl.class, agentId);
            if (ctl == null)
            {
                ctl = new LifecycleAgentControl();
                ctl.setAgentId(agentId);
                ctl.setInitialState(LifecycleStateEnum.INITIALIZE);
                
                // Don't start this agent with the lifecycle manager
                ctl.setDependent(false);
                
                ctl.setTrans(getTrans());
                
                ctl = HomeSupportHelper.get(ctx).createBean(ctx, ctl);
            }
        }
        catch (HomeException e)
        {
            throw new AgentException(e);
        }
        
        visitor_ = new ScheduledNotificationDeliveryVisitor(this);
        
        // Initialize the lifecycle agent with FW
        execute(ctx);
    }

    @Override
    public void doRun(Context ctx) throws LifecycleException
    {
    	 LogSupport.info(ctx, this, "Inside ScheduledLiaisonLifecycleAgent. Call doRun()..");
    	 
        And filter = new And();
        filter.add(new EQ(ScheduledNotificationXInfo.APP_NAME, CoreSupport.getApplication(ctx).getName()));
        filter.add(new EQ(ScheduledNotificationXInfo.LIAISON_SCHEDULE, scheduleName_));
        filter.add(new LTE(ScheduledNotificationXInfo.TIMESTAMP, CalendarSupportHelper.get(ctx).getRunningDate(ctx)));
        
        Context sCtx = HomeSupportHelper.get(ctx).getWhereContext(ctx, 
                ScheduledNotification.class, filter, 
                true, 
                ScheduledNotificationXInfo.TIMESTAMP, 
                ScheduledNotificationXInfo.ID);
        try
        {
        	List<Object> iDs = (List<Object>) getQueryDataList(sCtx, getQueryForScheduledNotification(sCtx));
        
        	if(!iDs.isEmpty())
			{
				Iterator<Object> iterator = iDs.listIterator();
				while(iterator.hasNext())
				{
					Object id = iterator.next();
					ScheduledNotification notification = HomeSupportHelper.get(sCtx).findBean(sCtx, ScheduledNotification.class, new EQ(ScheduledNotificationXInfo.ID, id));
					visitor_.visit(sCtx, notification);
				}
			}
			else
			{
				LogSupport.info(sCtx, this, "No Notification found for agent:" + getAgentId());
			}
        }
        catch (HomeException e)
        {
            new MajorLogMsg(this, "Error occurred in lifecycle agent [" + getAgentId()
                    + "] while executing scheduled notification(s)", e).log(sCtx);
        }
        catch (AbortVisitException e) 
        {
        	new MajorLogMsg(this, "Error occurred in lifecycle agent [" + getAgentId()
                    + "] while executing scheduled notification(s)", e).log(sCtx);
        }
        catch (AgentException e) 
        {
        	new MajorLogMsg(this, "Error occurred in lifecycle agent [" + getAgentId()
                    + "] while executing scheduled notification(s)", e).log(sCtx);
		}
    }
    
    /**
     * 
     * @param ctx
     * @return
     */
    public String getQueryForScheduledNotification(Context ctx)
    {
    	final String table = MultiDbSupportHelper.get(ctx).getTableName(ctx, ScheduledNotificationHome.class,
    			ScheduledNotificationXDBHome.DEFAULT_TABLE_NAME);
    	
    	StringBuilder query = new StringBuilder();
    	query.append("select ID from ").append(table);
    	query.append(" WHERE ").append(ScheduledNotificationXInfo.APP_NAME.getSQLName());
    	query.append("='").append(CoreSupport.getApplication(ctx).getName());
    	query.append("'");
    	query.append(" AND ").append(ScheduledNotificationXInfo.LIAISON_SCHEDULE.getSQLName());
    	query.append("='").append(scheduleName_);
    	query.append("'");
    	query.append(" AND ").append(ScheduledNotificationXInfo.TIMESTAMP.getSQLName());
    	query.append("<=").append(CalendarSupportHelper.get(ctx).getRunningDate(ctx).getTime());
    	
    	
    	LogSupport.info(ctx, this, "Notification Query:" + query.toString());
    	
    	return query.toString();
    }
    
    /**
     * Return Collection of executing query
     * @param ctx
     * @param sql
     * @return
     * @throws HomeException
     */
    public static Collection <Object> getQueryDataList(final Context ctx, String sql) throws HomeException
    {
    	final Collection <Object> queryList = new ArrayList<Object>();
    	
    	final XDB xdb = (XDB) ctx.get(XDB.class);
    	
    	try
    	{
    		xdb.forEach(ctx, 
    				new Visitor()
    		{
    			private static final long serialVersionUID = 1L;

    			public void visit(Context ctx, Object obj) throws AgentException, AbortVisitException 
    			{
    				try 
    				{
    					queryList.add(((XResultSet) obj).getInt(1));
    				} catch (SQLException e) 
    				{
    					LogSupport.minor(ctx, this, "Error getting while get date from Resultset. ", e);
    					throw new AgentException(e);
    				}
    			}
    		}
    		, sql);
    	}
    	catch(Exception ex)
    	{
    		LogSupport.minor(ctx,ScheduledLiaisonLifecycleAgent.class.getSimpleName() , "Error getting execute query. ", ex);
    		throw new HomeException(ex);
    	}
    	
    	return queryList;
    }
      

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getTrans()
    {
        return true;
    }

    protected final ScheduledNotificationDeliveryVisitor visitor_;

    private final String scheduleName_;
}