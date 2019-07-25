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
package com.redknee.app.crm.billing.message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import com.redknee.app.crm.bean.BillingMessage;
import com.redknee.app.crm.bean.BillingMessageXInfo;
import com.redknee.app.crm.support.FrameworkSupportHelper;
import com.redknee.app.crm.support.messages.MessageConfigurationSupport;
import com.redknee.framework.xhome.beans.ID;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.HomeException;

/**
 * This class takes care of adapting to/from the list of BillingMessages from/to the 
 * elements in the corresponding BillingMessagesHome 
 * @author angie.li@redknee.com
 *
 */
public class BillingMessageAdapter<MESSAGE extends BillingMessage, MESSAGEID extends ID> implements Adapter 
{
    /**
     * Serial version UID. For serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Have only one instance.
     */
    public static BillingMessageAdapter instance()
    {
        return instance_;
    }

    /**
     * Disallow external construction. Use the singleton instance.
     */
    private BillingMessageAdapter()
    {
    }

    /*
     * Adapt the records from the BillingMessages home to the list in the 
     * BillingMessageAware bean
     */
    public Object adapt(Context ctx, Object obj) throws HomeException 
    {
        BillingMessageAware bean = (BillingMessageAware) obj;
        MessageConfigurationSupport<MESSAGE, MESSAGEID> support = bean.getConfigurationSupport(ctx);
        if (support == null)
        {
            FrameworkSupportHelper.get(ctx).notifyExceptionListener(ctx, new IllegalPropertyArgumentException(
                    BillingMessageXInfo.IDENTIFIER,
                    "Could not adapt billing messages. [Billing Message Suppport] system not fully initalized."));
            bean.setBillingMessages(new ArrayList<MESSAGE>());
            return bean;
        }
        EQ filter = new EQ(BillingMessageXInfo.IDENTIFIER, Long.valueOf(bean.getIdentifier()));
        Collection<MESSAGE> existingRecords = support.getAllMessageConfiguration(ctx, bean.getSpid(), filter);
        ArrayList<MESSAGE> list = new ArrayList<MESSAGE>(existingRecords.size());
        for (MESSAGE record : existingRecords)
        {
            list.add(record);
        }
        bean.setBillingMessages(list);
        return bean;
    }

    /*
     * Adapt the records BillingMessageAware bean to the list in the 
     * BillingMessages home
     */
    public Object unAdapt(Context ctx, Object obj) throws HomeException 
    {
        BillingMessageAware bean = (BillingMessageAware)obj;

        try
        {
            MessageConfigurationSupport<MESSAGE, MESSAGEID> support = bean.getConfigurationSupport(ctx);
            if (support == null)
            {
                FrameworkSupportHelper.get(ctx).notifyExceptionListener(ctx, new IllegalPropertyArgumentException(
                        BillingMessageXInfo.IDENTIFIER,
                        "Could not un-adapt billing messages. [Billing Message Suppport] system not fully initalized."));
                return bean;
            }
            EQ filter = new EQ(BillingMessageXInfo.IDENTIFIER, Long.valueOf(bean.getIdentifier()));
            TreeMap<String, MESSAGE> existingRecords = getExistingRecords(support.getAllMessageConfiguration(ctx, bean.getSpid(), filter));
            
            List<MESSAGE> newSelection = bean.getBillingMessages();

            //Keeping track of what we have we can remove those that are in the new selection to know what to remove
            Set<String> configuredLanguages = existingRecords.keySet();
            boolean listHasChanged = false; 
            for (MESSAGE newRecord: newSelection)
            {
                /* Since the SPID and Identifier Columns are hidden in this table web-control then the fromWeb method will return
                 * the BillingMessage bean's with bean default values (which are incorrect). Forcibly, update them now. */
                newRecord.setSpid(bean.getSpid());
                newRecord.setIdentifier(bean.getIdentifier());
                newRecord.setActive(true);
                MESSAGE oldRecord = existingRecords.get(newRecord.getLanguage());
                if (oldRecord == null)
                {
                    //Create the record in the Home
                    support.createMessageConfiguration(ctx, newRecord);
                    listHasChanged = true;
                }
                else if (oldRecord != null && !oldRecord.equals(newRecord))
                {
                    //Update in the Home if the record has changed.
                    support.updateMessageConfiguration(ctx, newRecord);
                    listHasChanged = true;
                }
                configuredLanguages.remove(newRecord.getLanguage());
            }

            //Remove all Languages that were unselected.
            for (String unselectedRecord: configuredLanguages)
            {
                support.removeMessageConfiguration(ctx, existingRecords.get(unselectedRecord));
            }
        }
        catch(HomeException ex)
        {
            throw ex;
        }
        catch(Exception e)
        {
            HomeException exception = new HomeException("Failed to update the Billing Messages records.  Reload and submit changes again.", e);
            throw exception;
        }
        return bean;
    }

    private TreeMap<String, MESSAGE> getExistingRecords(Collection<MESSAGE> records) 
    {
        TreeMap<String, MESSAGE> value = new TreeMap<String, MESSAGE>();
        for (MESSAGE record: records)
        {
            value.put(record.getLanguage(), record);
        }
        return value;
    }

    protected static final BillingMessageAdapter instance_ = new BillingMessageAdapter();
}
