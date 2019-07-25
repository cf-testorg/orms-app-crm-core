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
package com.redknee.app.crm.notification;

import java.util.ArrayList;
import java.util.List;

import com.redknee.app.crm.bean.CreditNotificationEvent;
import com.redknee.app.crm.extension.creditcategory.NoticeScheduleCreditCategoryExtension;
import com.redknee.app.crm.notification.liaison.NotificationLiaison;
import com.redknee.app.crm.notification.liaison.ScheduledTaskNotificationLiaison;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.framework.lifecycle.LifecycleStateEnum;
import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.Or;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.MajorLogMsg;

/**
 * Package-private visitor for use by {@link AbstractCreditNoticeLifecycleAgent}.
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
class CreditNoticeScheduleVisitor<BEAN extends AbstractBean> implements Visitor
{
    protected final AbstractCreditNoticeLifecycleAgent<BEAN> agent_;

    CreditNoticeScheduleVisitor(AbstractCreditNoticeLifecycleAgent<BEAN> agent)
    {
        agent_ = agent;
    }

    public void visit(Context ctx, Object obj) throws AgentException, AbortVisitException
    {
        if (!LifecycleStateEnum.RUNNING.equals(agent_.getState()))
        {
            String msg = "Lifecycle agent " + agent_.getAgentId() + " no longer running.  Remaining credit notices will be sent next time it is run.";
            new InfoLogMsg(this, msg, null).log(ctx);
            throw new AbortVisitException(msg);
        }
        
        if (obj instanceof NoticeScheduleCreditCategoryExtension)
        {
            PropertyInfo prop = agent_.getCreditCategoryIdProperty();
            Class<BEAN> beanClass = prop.getBeanClass();
            
            NoticeScheduleCreditCategoryExtension noticeSched = (NoticeScheduleCreditCategoryExtension) obj;

            List<CreditNotificationEvent> events = new ArrayList<CreditNotificationEvent>(noticeSched.getEvents().values());
            agent_.cleanupNotificationEvents(events);
            
            if (events != null && events.size() > 0)
            {
                Object filter = new EQ(prop, noticeSched.getCreditCategory());
                
                Or customFilters = new Or();
                
                for (CreditNotificationEvent event : events)
                {
                    Object customFilter = agent_.getCustomFilter(ctx, event, noticeSched.getCreditCategory());
                    if (customFilter != null)
                    {
                        customFilters.add(customFilter);
                    }
                }
                
                if (customFilters.getList().size()>0)
                {
                    filter = new And().add(filter).add(customFilters);
                }

                Home home = null;
                try
                {
                    Context whereCtx = HomeSupportHelper.get(ctx).getWhereContext(ctx, beanClass, filter);
                    home = HomeSupportHelper.get(ctx).getHome(whereCtx, beanClass);
                }
                catch (HomeException e)
                {
                    new MajorLogMsg(this, "Error retrieving home for " + beanClass.getSimpleName()
                            + " in lifecycle agent [" + agent_.getAgentId()
                            + "].  Unable to deliver notices.", e).log(ctx);
                }
                
                if (home != null)
                {
                    Context sCtx = ctx.createSubContext();
                    
                    ScheduledTaskNotificationLiaison scheduledLiaison = new ScheduledTaskNotificationLiaison();
                    scheduledLiaison.setLiaisonSchedule(noticeSched.getSchedule());
                    sCtx.put(NotificationLiaison.class, agent_.wrapScheduledLiaison(sCtx, scheduledLiaison));
                    
                    try
                    {
                        home.forEach(sCtx, new CreditNoticeDeliveryVisitor<BEAN>(agent_, events));
                    }
                    catch (HomeException e)
                    {
                        new MajorLogMsg(this, "Error occurred in lifecycle agent [" + agent_.getAgentId()
                                + "] while delivering notices.", e).log(sCtx);
                    }
                }
            }
        }
    }
}