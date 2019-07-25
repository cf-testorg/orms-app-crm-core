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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.redknee.app.crm.bean.CreditEventTypeEnum;
import com.redknee.app.crm.bean.CreditNotificationEvent;
import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.extension.creditcategory.NoticeScheduleCreditCategoryExtension;
import com.redknee.app.crm.extension.creditcategory.NoticeScheduleCreditCategoryExtensionHome;
import com.redknee.app.crm.lifecycle.LifecycleAgentScheduledTask;
import com.redknee.app.crm.notification.liaison.NotificationLiaison;
import com.redknee.app.crm.notification.liaison.NotificationLiaisonProxy;
import com.redknee.app.crm.notification.liaison.ScheduledTaskNotificationLiaison;
import com.redknee.app.crm.notification.template.NotificationTemplate;
import com.redknee.app.crm.support.BeanLoaderSupport;
import com.redknee.app.crm.support.BeanLoaderSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.framework.lifecycle.LifecycleAgentControl;
import com.redknee.framework.lifecycle.LifecycleException;
import com.redknee.framework.lifecycle.LifecycleStateEnum;
import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xlog.log.MajorLogMsg;


/**
 * This lifecycle agent provides a common execution environment for delivery of credit notices
 * which are configured in {@link NoticeScheduleCreditCategoryExtension} beans.
 * 
 * Implementations must specify exactly which notifications get sent, where they are to be send,
 * and which templates are used for message generation.
 * 
 * Implementations may override methods to customize the actual delivery context, decorate the
 * {@link ScheduledTaskNotificationLiaison} used for delivery, specify key/value replacement features
 * that are applicable, and remove unsupported {@link CreditNotificationEvent} objects to speed
 * up processing.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public abstract class AbstractCreditNoticeLifecycleAgent<BEAN extends AbstractBean> extends LifecycleAgentScheduledTask
{
    protected class CreditNotificationInfo
    {
        private boolean sendNotice_ = false;
        
        private Context deliveryContext_;
        
        public CreditNotificationInfo(Context context)
        {
            setDeliveryContext(context);
        }

        public boolean sendNotice()
        {
            return sendNotice_;
        }
        
        public void setSendNotice(boolean sendNotice)
        {
            sendNotice_ = sendNotice;
        }
        
        public Context getDeliveryContext()
        {
            return deliveryContext_;
        }
        
        public void setDeliveryContext(Context context)
        {
            deliveryContext_ = context;
        }
    }
    
    public AbstractCreditNoticeLifecycleAgent(Context ctx, String agentId, 
            PropertyInfo creditCategoryIdProperty) throws AgentException
    {
        super(ctx, agentId);
        
        creditCategoryIdProperty_ = creditCategoryIdProperty;
        
        try
        {
            LifecycleAgentControl ctl = HomeSupportHelper.get(ctx).findBean(ctx, LifecycleAgentControl.class, agentId);
            if (ctl == null)
            {
                ctl = createLifecycleAgentControl(ctx);
                if (ctl != null)
                {
                    ctl.setAgentId(agentId);
                    ctl.setTrans(getTrans());
                    ctl = HomeSupportHelper.get(ctx).createBean(ctx, ctl);
                }
            }
        }
        catch (HomeException e)
        {
            throw new AgentException(e);
        }
        
        visitor_ = new CreditNoticeScheduleVisitor<BEAN>(this);
        
        // Initialize the lifecycle agent with FW
        execute(ctx);
    }

    protected void start(Context ctx) throws LifecycleException
    {
        Home home = (Home) ctx.get(NoticeScheduleCreditCategoryExtensionHome.class);
        
        try
        {
            home.forEach(ctx, visitor_);
        }
        catch (HomeException e)
        {
            new MajorLogMsg(this, "Error occurred in lifecycle agent [" + getAgentId()
                    + "] while executing scheduled notification(s)", e).log(ctx);
        }
    }

    /**
     * This method creates a GUI controllable lifecycle agent bean.  It may be overridden in implemenation
     * class, although it is not likely to be.
     * 
     * @param ctx Operating Context
     * @param agentId
     * @return
     * @throws HomeInternalException
     * @throws HomeException
     */
    protected LifecycleAgentControl createLifecycleAgentControl(Context ctx) throws HomeInternalException, HomeException
    {
        LifecycleAgentControl ctl = new LifecycleAgentControl();
        
        ctl.setInitialState(LifecycleStateEnum.INITIALIZE);
        
        // Don't start this agent with the lifecycle manager
        ctl.setDependent(false);
        
        return ctl;
    }

    /**
     * This method returns a where clause (Elang) that will be applied to the home when selecting
     * beans (e.g. Accounts) that may have credit notices sent to them.
     *  
     * @return Elang used to select beans for processing
     */
    protected Object getCustomFilter()
    {
        return null;
    }

    /**
     * 
     * This method returns a where clause (Elang) that will be applied to the home when selecting
     * beans (e.g. Accounts) that may have credit notices sent to them. 
     *  
     * @param ctx
     * @param event
     * @return Elang used to select beans for processing
     */
    protected Object getCustomFilter(final Context ctx, final CreditNotificationEvent event, final int creditCategoryId)
    {
        return getCustomFilter();
    }
    /**
     * This method returns all templates applicable to the given bean and credit event type.
     * The implementation may consider the bean's service provider, language, etc when determining which
     * templates are applicable to the scenario.
     * 
     * Performance is critical for implementations of this method as it will be called for all beans
     * in the credit category.
     * 
     * @param ctx Operating Context
     * @param bean Bean which may have notifications delivered.
     * @param eventType Credit Notice Event Type
     * @param occurrence Which occurrence of the event this is (i.e. first time notification is being sent, second time, etc)
     * @return List of {@link NotificationTemplate} objects
     */
    protected abstract Collection<NotificationTemplate> getTemplatesForCreditNotificationEvent(Context ctx, BEAN bean, CreditEventTypeEnum eventType, int occurrence);

    /**
     * This method returns information related to the destination of messages for the given template.  If
     * no destination is available for any reason, then this method returns null.
     * 
     * Performance is critical for implementations of this method as it will be called for all beans
     * in the credit category.
     * 
     * @param ctx Operating Context
     * @param bean Bean which may have notifications delivered.
     * @param template Notification Template for which we need recipient information.
     * @return RecipientInfo object containing all relevent destination information for the given template.  Returns null if the bean does not have any destination information for the template.
     */
    protected abstract RecipientInfo getRecipientInfo(Context ctx, BEAN bean, NotificationTemplate template);
    
    /**
     * This method simply returns a CreditNotificationInfo object with information on whether a notification should be sent for the given event,
     * and the delivery context.
     * 
     * This is assumed to be a more expensive method call than {@link AbstractCreditNoticeLifecycleAgent#getTemplatesForCreditNotificationEvent(Context, AbstractBean, CreditEventTypeEnum)}
     * and {@link AbstractCreditNoticeLifecycleAgent#getRecipientInfo(Context, AbstractBean, NotificationTemplate)} 
     * 
     * @param ctx Operating Context
     * @param bean Bean which may have notifications delivered.
     * @param event Credit Event that is being checked for.
     * @return CreditNotificationInfo object with information on whether a notification should be sent for the given event,
     * and the delivery context.
     */
    protected abstract CreditNotificationInfo retrieveCreditNotificationInfo(Context ctx, BEAN bean, CreditNotificationEvent event);

    /**
     * This method is designed to be overridden.  It provides the implementation class an opportunity to
     * customize the context that will be used for notification delivery.
     * 
     * @param ctx Operating Context
     * @param bean Bean which will have notifications delivered.
     * @return
     */
    protected Context wrapNotificationDeliveryContext(Context ctx, BEAN bean)
    {
        Context sCtx = ctx.createSubContext();
        
        // Put object in context for BeanLoaderSupport
        sCtx.put(bean.getClass(), bean);
        
        BeanLoaderSupport beanLoaderSupport = BeanLoaderSupportHelper.get(ctx);
        beanLoaderSupport.setBeanLoaderMap(sCtx, beanLoaderSupport.getBeanLoaderMap(sCtx, bean.getClass()));
        
        return sCtx;
    }

    /**
     * This method is designed to be overridden.  It provides the implementation class an opportunity to wrap
     * the given scheduled liaison with whatever proxy implementations that it requires.  The implementation
     * should ensure that the provided liaison in the returned pipeline so that notifications are delivered
     * according to the schedule configured in the {@link NoticeScheduleCreditCategoryExtension}
     *  
     * @param ctx Operating Context
     * @param scheduledLiaison Liaison instance that may be wrapped with {@link NotificationLiaisonProxy} implementation(s)
     * @return {@link NotificationLiaison} that will be used for all
     */
    protected NotificationLiaison wrapScheduledLiaison(Context ctx, ScheduledTaskNotificationLiaison scheduledLiaison)
    {
        return scheduledLiaison;
    }

    /**
     * This method is designed to be overridden.  It provides the implementation class an opportunity to
     * specify which (if any) key/value replacement features should be used when substituting variables
     * in templates.
     * 
     * @param ctx Operating Context
     * @param eventType Credit Notice Event Type
     * @return List of {@link KeyValueFeatureEnum} objects to use for key/value replacement.
     */
    protected List<KeyValueFeatureEnum> getKeyValueReplacementFeatures(Context ctx, CreditEventTypeEnum eventType)
    {
        List<KeyValueFeatureEnum> features = new ArrayList<KeyValueFeatureEnum>();
        features.add(KeyValueFeatureEnum.GENERIC);
        return features;
    }


    /**
     * This method removes invalid events from the list of supported events.  It can be overridden
     * by implementation classes to filter out events that are not supported by that particular
     * implementation.
     * 
     * The default implementation removes null events and invalid event types.
     * 
     * @param events Source list of {@link CreditNotificationEvent} objects.  Unsupported elements are to be removed.
     */
    protected void cleanupNotificationEvents(final Collection<CreditNotificationEvent> events)
    {
        if (events != null)
        {
            Iterator<CreditNotificationEvent> iter = events.iterator();
            while (iter.hasNext())
            {
                CreditNotificationEvent event = iter.next();
                if (event == null)
                {
                    iter.remove();
                }
                
                CreditEventTypeEnum eventType = CreditEventTypeEnum.get(Integer.valueOf(event.getEventTypeIndex()).shortValue());
                if (eventType == null)
                {
                    iter.remove();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getTrans()
    {
        return true;
    }

    
    public PropertyInfo getCreditCategoryIdProperty()
    {
        return creditCategoryIdProperty_;
    }

    protected Visitor visitor_;
    protected final PropertyInfo creditCategoryIdProperty_;
}
