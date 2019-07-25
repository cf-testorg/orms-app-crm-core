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

import com.redknee.app.crm.bean.SubscriberCycleUsage;
import com.redknee.app.crm.bean.SubscriberCycleUsageXInfo;
import com.redknee.app.crm.bean.search.SubscriberCycleUsageSearchWebControl;
import com.redknee.app.crm.bean.search.SubscriberCycleUsageSearchXInfo;


/**
 * Provides a custom search border for the SubscriberCycleUsage screen that
 * allows searches on subscriber identifier and account identifier.
 *
 * @author gary.anderson@redknee.com
 */
public class SubscriberCycleUsageSearchBorder
    extends SearchBorder
{
    /**
     * Creates a new SubscriberCycleUsageSearchBorder.
     *
     * @param context The operating context.
     */
    public SubscriberCycleUsageSearchBorder(final Context context)
    {
        super(context, SubscriberCycleUsage.class, new SubscriberCycleUsageSearchWebControl());

        addAgent(new SelectSearchAgent(SubscriberCycleUsageXInfo.SUBSCRIBER_IDENTIFIER, SubscriberCycleUsageSearchXInfo.SUBSCRIBER_IDENTIFIER));
        
        addAgent(new SelectSearchAgent(SubscriberCycleUsageXInfo.ACCOUNT_IDENTIFIER, SubscriberCycleUsageSearchXInfo.ACCOUNT_IDENTIFIER));
    }
} // class
