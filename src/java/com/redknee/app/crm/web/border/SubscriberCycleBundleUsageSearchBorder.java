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
package com.redknee.app.crm.web.border;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;

import com.redknee.app.crm.bean.SubscriberCycleBundleUsage;
import com.redknee.app.crm.bean.SubscriberCycleBundleUsageXInfo;
import com.redknee.app.crm.bean.search.SubscriberCycleBundleUsageSearchWebControl;
import com.redknee.app.crm.bean.search.SubscriberCycleBundleUsageSearchXInfo;


/**
 * Provides a custom search border for the SubscriberCycleUsage screen that
 * allows searches on subscriber identifier and account identifier.
 *
 * @author amedina
 */
public class SubscriberCycleBundleUsageSearchBorder
    extends SearchBorder
{
    /**
     * Creates a new SubscriberCycleUsageSearchBorder.
     *
     * @param context The operating context.
     */
    public SubscriberCycleBundleUsageSearchBorder(final Context context)
    {
        super(context, SubscriberCycleBundleUsage.class, new SubscriberCycleBundleUsageSearchWebControl());

        addAgent(new SelectSearchAgent(SubscriberCycleBundleUsageXInfo.SUBSCRIBER_IDENTIFIER, SubscriberCycleBundleUsageSearchXInfo.SUBSCRIBER_IDENTIFIER));
        
        addAgent(new SelectSearchAgent(SubscriberCycleBundleUsageXInfo.ACCOUNT_IDENTIFIER, SubscriberCycleBundleUsageSearchXInfo.ACCOUNT_IDENTIFIER));
    }
} // class
