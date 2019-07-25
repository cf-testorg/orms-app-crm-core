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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.redknee.app.crm.bean.BillCycleBillingMessage;
import com.redknee.app.crm.bean.BillingMessage;
import com.redknee.app.crm.bean.CreditCategoryBillingMessage;
import com.redknee.app.crm.bean.CreditCategoryBillingMessageID;
import com.redknee.app.crm.bean.CreditCategoryXInfo;
import com.redknee.app.crm.bean.PricePlanBillingMessage;
import com.redknee.app.crm.billing.message.BillingMessageAdapter;
import com.redknee.app.crm.billing.message.BillingMessageAware;
import com.redknee.app.crm.billing.message.BillingMessageHomePipelineFactory;
import com.redknee.app.crm.extension.Extension;
import com.redknee.app.crm.extension.ExtensionHolder;
import com.redknee.app.crm.extension.ExtensionLoadingAdapter;
import com.redknee.app.crm.extension.creditcategory.CreditCategoryExtension;
import com.redknee.app.crm.extension.creditcategory.CreditCategoryExtensionXInfo;
import com.redknee.app.crm.support.ExtensionSupportHelper;
import com.redknee.app.crm.support.messages.MessageConfigurationSupport;
import com.redknee.app.crm.xhome.beans.xi.NonModelPropertyInfo;
import com.redknee.framework.xhome.beans.XCloneable;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextLocator;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;



/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CreditCategory extends com.redknee.app.crm.bean.CreditCategory implements com.redknee.framework.xhome.context.ContextAware, com.redknee.app.crm.extension.ExtensionAware, BillingMessageAware
{
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * {@inheritDoc}
     */
    public MessageConfigurationSupport<CreditCategoryBillingMessage, CreditCategoryBillingMessageID> getConfigurationSupport(Context ctx)
    {
        MessageConfigurationSupport<CreditCategoryBillingMessage, CreditCategoryBillingMessageID> support = (MessageConfigurationSupport<CreditCategoryBillingMessage, CreditCategoryBillingMessageID>) ctx
                .get(BillingMessageHomePipelineFactory
                        .getBillingMessageConfigurationKey(CreditCategoryBillingMessage.class));
        return support;
    }

    // the IdentifierAware interface
    public long getIdentifier()
    {
        return (long) getCode();
    }

    public void setIdentifier(long ID) throws IllegalArgumentException
    {
        setCode((int) ID);
    }


    /**
     * {@inheritDoc}
     */
    public Context getContext()
    {
        return context_;
    }

    /**
     * {@inheritDoc}
     */
    public void setContext(final Context context)
    {
        context_ = context;
    }

    public PropertyInfo getExtensionHolderProperty()
    {
        return EXTENSIONS_PROPERTY;
    }

    public Collection<Extension> getExtensions()
    {
        return ExtensionSupportHelper.get(getContext()).unwrapExtensions(creditCategoryExtensions_);
    }
    
    public List<ExtensionHolder> getExtensionHolders()
    {
        return creditCategoryExtensions_;
    }
    
    
    public Collection<Class> getExtensionTypes()
    {
        final Context ctx = ContextLocator.locate();
        Set<Class<CreditCategoryExtension>> extClasses = ExtensionSupportHelper.get(ctx).getRegisteredExtensions(ctx,
                CreditCategoryExtension.class);
        
        Collection<Class> desiredClass = new ArrayList<Class>();
        for (Class<CreditCategoryExtension> ext : extClasses)
        {
            desiredClass.add(ext);
        }
        return desiredClass;
    }

    /**
     * Lazy loading extensions. {@inheritDoc}
     */
    public List<ExtensionHolder> getCreditCategoryExtensions()
    {
        synchronized (this)
        {
            if (getExtensionHolders() == null)
            {
                final Context ctx = getContext();
                try
                {
                    /*
                     * To avoid deadlock, use a credit category
                     * "with extensions loaded" along with extension loading
                     * adapter.
                     */
                    CreditCategory categoryCopy = (CreditCategory) this.clone();
                    categoryCopy.setCreditCategoryExtensions(new ArrayList());

                    categoryCopy = (CreditCategory) new ExtensionLoadingAdapter<CreditCategoryExtension>(
                            CreditCategoryExtension.class,
                            CreditCategoryExtensionXInfo.CREDIT_CATEGORY)
                            .adapt(ctx, categoryCopy);

                    this.setCreditCategoryExtensions(categoryCopy
                            .getCreditCategoryExtensions());
                }
                catch (Exception e)
                {
                    LogSupport.minor(ctx, this,
                            "Exception occurred loading extensions. Extensions NOT loaded.");
                    LogSupport.debug(ctx, this,
                            "Exception occurred loading extensions. Extensions NOT loaded.", e);
                }
            }
        }

        return getExtensionHolders();
    }

    public void setCreditCategoryExtensions(List<ExtensionHolder> creditCategoryExtensions)
    {
        creditCategoryExtensions_ = creditCategoryExtensions;
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
                    LogSupport.minor(getContext(), this, "Unable to load Billing Message for Credit Category ["
                            + this.getCode() + "]. Error: " + e.getMessage(), e);
                }
            }
        }
        Collection<BillingMessage> existingRecords =  super.getBillingMessages();

        ArrayList<CreditCategoryBillingMessage> l = new ArrayList<CreditCategoryBillingMessage>(existingRecords.size());
        for (BillingMessage record : existingRecords)
        {
        	CreditCategoryBillingMessage msg = new CreditCategoryBillingMessage();
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
                    LogSupport.minor(ctx, this, "Unable to save Billing Message for Credit Category ["
                            + this.getCode() + "]. Error: " + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Adding cloning functionality to clone added fields.
     * 
     * @return the clone object
     * @throws CloneNotSupportedException
     *             should not be thrown
     */
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        CreditCategory clone = (CreditCategory) super.clone();
        return cloneCreditCategoryExtensionList(clone);
    }

    private CreditCategory cloneCreditCategoryExtensionList(
            final CreditCategory clone) throws CloneNotSupportedException
    {
        if ( creditCategoryExtensions_ != null)
        {
            final List extentionList = new ArrayList(creditCategoryExtensions_.size());
            clone.setCreditCategoryExtensions(extentionList);
            for (final Iterator it = creditCategoryExtensions_.iterator(); it
                    .hasNext();)
            {
                extentionList.add(safeClone((XCloneable) it.next()));
            }
        }
        return clone;
    }

    /**
     * The operating context.
     */
    protected transient Context context_;

    public static final String CREDIT_CATEGORY_EXTENSIONS_FIELD_NAME = "creditCategoryExtensions_";
    public static final PropertyInfo EXTENSIONS_PROPERTY = new NonModelPropertyInfo(CreditCategory.class, CREDIT_CATEGORY_EXTENSIONS_FIELD_NAME, CreditCategoryXInfo.instance());
    protected List<ExtensionHolder> creditCategoryExtensions_ = null;
}
