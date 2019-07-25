/*
 * 
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.bean.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.redknee.app.crm.bean.AccountTypeBillingMessage;
import com.redknee.app.crm.bean.BillCycleBillingMessage;
import com.redknee.app.crm.bean.BillCycleBillingMessageID;
import com.redknee.app.crm.bean.BillingMessage;
import com.redknee.app.crm.bean.ContractDescription;
import com.redknee.app.crm.bean.ContractDescriptionID;
import com.redknee.app.crm.billing.message.BillingMessageAdapter;
import com.redknee.app.crm.billing.message.BillingMessageAware;
import com.redknee.app.crm.billing.message.BillingMessageHomePipelineFactory;
import com.redknee.app.crm.support.messages.MessageConfigurationSupport;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;

/**
 * 
 * @author vijay.gote
 * @since 9.5
 *
 */
public class SubscriptionContractTerm extends com.redknee.app.crm.contract.SubscriptionContractTerm
        implements
            BillingMessageAware,
            ContextAware
{

    /**
     * Serial version UID. For serialization.
     */
    private static final long serialVersionUID = 1L;
    /** Default value for identifier **/
    public static final long DEFAULT_IDENTIFIER = 0L;
    protected long identifier_ = DEFAULT_IDENTIFIER;
    public final static String IDENTIFIER_PROPERTY = "identifier";


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


    // the IdentifierAware interface
    public long getIdentifier()
    {
        return (long) getId();
    }


    public void setIdentifier(long ID) throws IllegalArgumentException
    {
        setId((int) ID);
    }


    // from the BillingMessageAware interface
    @Override
    public MessageConfigurationSupport<ContractDescription, ContractDescriptionID> getConfigurationSupport(Context ctx)
    {
        MessageConfigurationSupport<ContractDescription, ContractDescriptionID> support = (MessageConfigurationSupport<ContractDescription, ContractDescriptionID>) ctx
                .get(BillingMessageHomePipelineFactory.getBillingMessageConfigurationKey(ContractDescription.class));
        return support;
    }

    /**
     * The operating context.
     */
    protected transient Context context_;


    @Override
    public List getBillingMessages()
    {
        synchronized (this)
        {
            if (contractDescriptions_ == null)
            {
                try
                {
                    BillingMessageAdapter.instance().adapt(getContext(), this);
                }
                catch (HomeException e)
                {
                    LogSupport.minor(getContext(), this,
                            "Unable to load Billing Message for Subscription Contract Error: " + e.getMessage(), e);
                }
            }
        }

        Collection<BillingMessage> existingRecords =  super.getContractDescriptions();

        ArrayList<ContractDescription> l = new ArrayList<ContractDescription>(existingRecords.size());
        for (BillingMessage record : existingRecords)
        {
            ContractDescription msg = new ContractDescription();
            msg.setActive(record.getActive());
            msg.setIdentifier(record.getIdentifier());
            msg.setLanguage(record.getLanguage());
            msg.setMessage(record.getMessage());
            msg.setSpid(record.getSpid());
            l.add(msg);
        }
        return l;     
    }
    



    @Override
    public void setBillingMessages(List billingMessages)
    {
        this.setContractDescriptions(billingMessages);
    }


    @Override
    public void saveBillingMessages(Context ctx)
    {
        synchronized (this)
        {
            if (contractDescriptions_ != null)
            {
                try
                {
                    BillingMessageAdapter.instance().unAdapt(ctx, this);
                }
                catch (HomeException e)
                {
                    LogSupport.minor(ctx, this,
                            "Unable to save Billing Message for Subscription Conract Error: " + e.getMessage(), e);
                }
            }
        }
    }
}
