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

import com.redknee.app.crm.bean.AccountTypeBillingMessage;
import com.redknee.app.crm.bean.AccountTypeBillingMessageID;
import com.redknee.app.crm.bean.BillCycleBillingMessage;
import com.redknee.app.crm.bean.BillCycleBillingMessageID;
import com.redknee.app.crm.bean.BillingMessage;
import com.redknee.app.crm.billing.message.BillingMessageAdapter;
import com.redknee.app.crm.billing.message.BillingMessageAware;
import com.redknee.app.crm.support.messages.MessageConfigurationSupport;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.app.crm.billing.message.BillingMessageHomePipelineFactory;



/**
 * 
 *
 * @author Bhagyashree Dhavalshankh
 * TT#12091155018 . Providing Billing Message Configuration at Account Type level
 * @since 9.4.1 
 */
public class AccountCategory extends com.redknee.app.crm.bean.AccountCategory implements BillingMessageAware, ContextAware
{
    /**
     * Serial version UID. For serialization.
     */
    private static final long serialVersionUID = 1L;

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

    //from the BillingMessageAware interface
    @Override
    public MessageConfigurationSupport<AccountTypeBillingMessage, AccountTypeBillingMessageID> getConfigurationSupport(Context ctx)
    {
    	MessageConfigurationSupport<AccountTypeBillingMessage, AccountTypeBillingMessageID> support = (MessageConfigurationSupport<AccountTypeBillingMessage, AccountTypeBillingMessageID>) ctx
                .get(BillingMessageHomePipelineFactory.getBillingMessageConfigurationKey(AccountTypeBillingMessage.class));
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
                    LogSupport.minor(getContext(), this, "Unable to load Billing Message for Account Category ["
                            + this.getIdentifier() + "]. Error: " + e.getMessage(), e);
                }
            }
        }
        Collection<BillingMessage> existingRecords =  super.getBillingMessages();

        ArrayList<AccountTypeBillingMessage> l = new ArrayList<AccountTypeBillingMessage>(existingRecords.size());
        for (BillingMessage record : existingRecords)
        {
        	AccountTypeBillingMessage msg = new AccountTypeBillingMessage();
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
                    LogSupport.minor(ctx, this, "Unable to save Billing Message for Account Category ["
                            + this.getIdentifier() + "]. Error: " + e.getMessage(), e);
                }
            }
        }
    }

    /**
    * The operating context.
    */
    protected transient Context context_;

}
