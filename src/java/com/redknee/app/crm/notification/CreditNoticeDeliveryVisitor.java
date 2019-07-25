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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redknee.app.crm.bean.CreditEventTypeEnum;
import com.redknee.app.crm.bean.CreditNotificationEvent;
import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.notification.AbstractCreditNoticeLifecycleAgent.CreditNotificationInfo;
import com.redknee.app.crm.notification.liaison.NotificationLiaison;
import com.redknee.app.crm.notification.template.NotificationTemplate;
import com.redknee.framework.lifecycle.LifecycleStateEnum;
import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.LogSupport;

/**
 * Package-private visitor for use by {@link AbstractCreditNoticeLifecycleAgent}.
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
class CreditNoticeDeliveryVisitor<BEAN extends AbstractBean> implements Visitor
{
    private final AbstractCreditNoticeLifecycleAgent<BEAN> agent_;
    private final List<CreditNotificationEvent> events_;

    CreditNoticeDeliveryVisitor(AbstractCreditNoticeLifecycleAgent<BEAN> agent, List<CreditNotificationEvent> events)
    {
        agent_ = agent;
        events_ = events;

        // Events must be in order to properly identify the occurrence number of the notification 
        Collections.sort(events_);
    }

    public void visit(Context ctx, Object obj) throws AgentException, AbortVisitException
    {
        if (!LifecycleStateEnum.RUNNING.equals(agent_.getState()))
        {
            String msg = "Lifecycle agent " + agent_.getAgentId() + " no longer running.  Remaining credit notices will be sent next time it is run.";
            new InfoLogMsg(this, msg, null).log(ctx);
            throw new AbortVisitException(msg);
        }
        
        Map<CreditEventTypeEnum, Integer> eventOccurrences = new HashMap<CreditEventTypeEnum, Integer>();
        for (CreditNotificationEvent event : events_)
        {
            CreditEventTypeEnum eventType = CreditEventTypeEnum.get(Integer.valueOf(event.getEventTypeIndex()).shortValue());
            
            Integer occurrence = eventOccurrences.get(eventType);
            if (occurrence == null)
            {
                occurrence = Integer.valueOf(0);
            }
            if (eventType != null)
            {
                eventOccurrences.put(eventType, ++occurrence);
            }
            
            Collection<NotificationTemplate> templates = agent_.getTemplatesForCreditNotificationEvent(ctx, (BEAN) obj, eventType, occurrence);
            Map<NotificationTemplate, RecipientInfo> destinationMap = new HashMap<NotificationTemplate, RecipientInfo>();
            if (templates != null)
            {
                Iterator<NotificationTemplate> iter = templates.iterator();
                while (iter.hasNext())
                {
                    NotificationTemplate template = iter.next();
                    if (template == null)
                    {
                        iter.remove();
                        continue;
                    }
                    
                    RecipientInfo destination = agent_.getRecipientInfo(ctx, (BEAN) obj, template);
                    if (destination == null)
                    {
                        if (LogSupport.isDebugEnabled(ctx))
                        {
                            String templateType = (template == null ? null : template.getClass().getName());
                            String objectString = (obj == null ? null : obj.getClass().getName());
                            if (obj instanceof Identifiable)
                            {
                                objectString += " [ID=" + ((Identifiable)obj).ID() + "]";
                            }
                            new DebugLogMsg(this, "Skipping template of type [" + templateType + "] because no recipient info could be found for " + objectString, null).log(ctx);
                        }
                        iter.remove();
                        continue;
                    }
                    
                    destinationMap.put(template, destination);
                }
            }
            if (templates == null || templates.size() == 0)
            {
                String objectString = (obj == null ? null : obj.getClass().getName());
                if (obj instanceof Identifiable)
                {
                    objectString += " [ID=" + ((Identifiable)obj).ID() + "]";
                }
                new InfoLogMsg(this, "No templates available for " + eventType + " notification event [" + objectString + "].  Skipping credit notice delivery for this entry...", null).log(ctx);
                continue;
            }
            NotificationLiaison notificationLiaison = ((NotificationLiaison) ctx.get(NotificationLiaison.class));
            
            CreditNotificationInfo noticeInfoResult = agent_.retrieveCreditNotificationInfo(ctx.createSubContext(), (BEAN) obj, event);
            if (noticeInfoResult.sendNotice())
            {
                KeyValueFeatureEnum[] features = new KeyValueFeatureEnum[] {};
                List<KeyValueFeatureEnum> featureList = agent_.getKeyValueReplacementFeatures(ctx, eventType);
                if (featureList != null)
                {
                    features = featureList.toArray(features);
                }

                Context sCtx = noticeInfoResult.getDeliveryContext();
                sCtx = agent_.wrapNotificationDeliveryContext(sCtx, (BEAN) obj);
                
                for (NotificationTemplate template : templates)
                {
                    notificationLiaison.sendNotification(sCtx, template, destinationMap.get(template), features);
                }
            }
        }
    }
}