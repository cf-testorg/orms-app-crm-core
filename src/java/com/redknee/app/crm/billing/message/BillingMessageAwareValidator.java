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

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.bean.BillingMessage;
import com.redknee.app.crm.bean.BillingMessageID;
import com.redknee.app.crm.support.LicensingSupportHelper;

import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;

/**
 * This validator is used on BillingMessageAware Beans.
 * This validator validates that the given bean's Billing Messages belong to that bean
 * (the SPID and identifier correspond correctly), that there are no BillingMessages
 * configured for a "default" Language and that there are no duplicate BillingMessage 
 * configurations
 * 
 * @author angie.li@redknee.com
 *
 */
public class BillingMessageAwareValidator implements Validator 
{
    /**
     * Have only one instance.
     * @return instance
     */
    public static BillingMessageAwareValidator instance()
    {
        return instance_ ;
    }

    /**
     * Disallow external construction. Use the singleton instance.
     */
    private BillingMessageAwareValidator()
    {
    }

    public void validate(Context ctx, Object obj) throws IllegalStateException 
    {
        if(obj instanceof BillingMessageAware)
        {
            BillingMessageAware bean = (BillingMessageAware) obj;
    
            final boolean multiLicense = LicensingSupportHelper.get(ctx).isLicensed(ctx, CoreCrmLicenseConstants.MULTI_LANGUAGE);
    
            Collection<BillingMessage> messages = bean.getBillingMessages();
            ArrayList<String> configuredLanguages = new ArrayList<String>();
            for(BillingMessage record: messages)
            {
                if (record.getSpid() != bean.getSpid())
                {
                    throw new IllegalStateException("Billing Messages: Do not change the SPID value different than ID=" + bean.getSpid());
                }
                if (multiLicense && record.getLanguage().equals(BillingMessageID.DEFAULT_LANGUAGE))
                {
                    throw new IllegalStateException("Do not configure Billing Message for a DEFAULT language.  Choose a specific language.");
                }
                if (!multiLicense && !record.getLanguage().equals(BillingMessageID.DEFAULT_LANGUAGE))
                {
                    throw new IllegalStateException("Please remove all configured messages. Only one message allowed.");
                }
                if (configuredLanguages.contains(record.getLanguage()))
                {
                    //There exists duplicate Billing Message Language configurations 
                    throw new IllegalStateException("There are can only be one Billing Message per language.  Currently, there is a duplicate language configuration for Billing Messages.");
                }
                configuredLanguages.add(record.getLanguage());
            }
        }
    }

    protected static final BillingMessageAwareValidator instance_ = new BillingMessageAwareValidator();
}
