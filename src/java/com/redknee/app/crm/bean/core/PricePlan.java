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
package com.redknee.app.crm.bean.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.redknee.app.crm.bean.BillingMessage;
import com.redknee.app.crm.bean.PricePlanBillingMessage;
import com.redknee.app.crm.bean.PricePlanBillingMessageID;
import com.redknee.app.crm.bean.PricePlanVersion;
import com.redknee.app.crm.bean.SpidBillingMessage;
import com.redknee.app.crm.bean.SubscriptionTypeAware;
import com.redknee.app.crm.bean.payment.Contract;
import com.redknee.app.crm.billing.message.BillingMessageAdapter;
import com.redknee.app.crm.billing.message.BillingMessageAware;
import com.redknee.app.crm.billing.message.BillingMessageHomePipelineFactory;
import com.redknee.app.crm.filter.ContractDurationPricePlanPredicate;
import com.redknee.app.crm.support.BeanLoaderSupport;
import com.redknee.app.crm.support.BeanLoaderSupportHelper;
import com.redknee.app.crm.support.messages.MessageConfigurationSupport;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;
import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xhome.context.ContextLocator;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.webcontrol.OptionalLongWebControl;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class PricePlan extends com.redknee.app.crm.bean.PricePlan implements SubscriptionTypeAware, ContextAware, BillingMessageAware
{
    public long getMinContractDurationInDays()
    {
        long min = getMinContractDuration();
        if (min == OptionalLongWebControl.DEFAULT_VALUE)
        {
            return min;
        }
        
        return Contract.getContractDurationInDays(getContractDurationUnits(), min);
    }
    
    public long getMaxContractDurationInDays()
    {
        long max = getMaxContractDuration();
        if (max == OptionalLongWebControl.DEFAULT_VALUE)
        {
            return max;
        }
        
        return Contract.getContractDurationInDays(getContractDurationUnits(), max);
    }
    
    public Predicate getRestrictionViolationPredicate(Context ctx)
    {
        if (this.isApplyContractDurationCriteria())
        {
            return new ContractDurationPricePlanPredicate();
        }
        
        return True.instance();
    }
    
    public boolean isRestrictionViolation(Context ctx, Object bean)
    {   
        Predicate predicate = getRestrictionViolationPredicate(ctx);
        if (predicate != null && !(predicate instanceof True))
        {
            Context subCtx = ctx.createSubContext();
            
            BeanLoaderSupport beanSupport = BeanLoaderSupportHelper.get(subCtx);
            Map<Class, Collection<PropertyInfo>> beanLoaderMap = beanSupport.getBeanLoaderMap(subCtx);
            if (beanLoaderMap == null)
            {
                if (bean instanceof AbstractBean)
                {
                    subCtx.put(bean.getClass(), bean);
                    beanLoaderMap = beanSupport.getBeanLoaderMap(ctx, ((AbstractBean)bean).getClass());
                }
                if (beanLoaderMap != null)
                {
                    beanSupport.setBeanLoaderMap(subCtx, beanLoaderMap);
                }
            }
            
            return !predicate.f(subCtx, this);
        }
        
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public PricePlanVersion getVersions()
    {
        return getVersions(getContext());
    }

    private PricePlanVersion getVersions(Context ctx)
    {

        try
        {
            PricePlanVersion versions = (PricePlanVersion) super.versions(ctx).selectAll().iterator().next();

            // Adapt between business logic bean and data bean
            return (PricePlanVersion) new ExtendedBeanAdapter<com.redknee.app.crm.bean.PricePlanVersion, com.redknee.app.crm.bean.core.PricePlanVersion>(
                    com.redknee.app.crm.bean.PricePlanVersion.class, 
                    com.redknee.app.crm.bean.core.PricePlanVersion.class).adapt(ctx, versions);
        }
        catch (HomeException e)
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, e.getClass().getSimpleName() + " occurred in " + PricePlan.class.getSimpleName() + ".getVersions(): " + e.getMessage(), e).log(ctx);
            }
            return null;
        }
    }

    public SubscriptionType getSubscriptionType(final Context ctx)
    {
        return SubscriptionType.getSubscriptionType(ctx, getSubscriptionType());
    }

    /**
     * {@inheritDoc}
     */
    public Context getContext()
    {
        if (context_ == null)
        {
            return ContextLocator.locate();
        }
        return context_;
    }

    /**
     * {@inheritDoc}
     */
    public void setContext(final Context context)
    {
        context_ = context;
    }


    /**
     * {@inheritDoc}
     */
    public MessageConfigurationSupport<PricePlanBillingMessage, PricePlanBillingMessageID> getConfigurationSupport(final Context ctx)
    {
        final MessageConfigurationSupport<PricePlanBillingMessage, PricePlanBillingMessageID> support = (MessageConfigurationSupport<PricePlanBillingMessage, PricePlanBillingMessageID>) ctx
                .get(com.redknee.app.crm.billing.message.BillingMessageHomePipelineFactory
                        .getBillingMessageConfigurationKey(PricePlanBillingMessage.class));
     return support;
    }

    @Override
    public List getBillingMessages()
    {
        synchronized (this)
        {
            if (billingMessages_ == null)
            {
                try
                {
                    BillingMessageAdapter.instance().adapt(getContext(), this);
                }
                catch (HomeException e)
                {
                    LogSupport.minor(getContext(), this, "Unable to load Billing Message for Price Plan ["
                            + this.getId() + "]. Error: " + e.getMessage(), e);
                }
            }
        }
        Collection<BillingMessage> existingRecords =  super.getBillingMessages();

        ArrayList<PricePlanBillingMessage> l = new ArrayList<PricePlanBillingMessage>(existingRecords.size());
        for (BillingMessage record : existingRecords)
        {
        	PricePlanBillingMessage msg = new PricePlanBillingMessage();
        	msg.setActive(record.getActive());
        	msg.setIdentifier(record.getIdentifier());
        	msg.setLanguage(record.getLanguage());
        	msg.setMessage(record.getMessage());
        	msg.setSpid(record.getSpid());
            l.add(msg);
        }
        return l;

    }

    public void saveBillingMessages(final Context ctx)
    {
        synchronized (this)
        {
            if (billingMessages_ != null)
            {
                try
                {
                    BillingMessageAdapter.instance().unAdapt(ctx, this);
                }
                catch (HomeException e)
                {
                    LogSupport.minor(ctx, this, "Unable to save Billing Message for Price Plan ["
                            + this.getId() + "]. Error: " + e.getMessage(), e);
                }
            }
        }
    }

    /**
    * The operating context.
    */
    protected transient Context context_;
}
