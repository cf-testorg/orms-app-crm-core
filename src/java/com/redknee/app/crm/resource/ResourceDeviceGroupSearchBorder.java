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
package com.redknee.app.crm.resource;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.search.FindSearchAgent;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;

import com.redknee.app.crm.web.control.SearchBeanToContextWebControl;

/**
 * Provides search functionality for the resource device.
 *
 * @author victor.stratan@redknee.com
 */
public class ResourceDeviceGroupSearchBorder extends SearchBorder
{
    /**
     * Creates a new ResourceDeviceSearchBorder.
     *
     * @param context The operating context.
     */
    public ResourceDeviceGroupSearchBorder(final Context context)
    {
        super(context, ResourceDeviceGroup.class,
                new SearchBeanToContextWebControl(new ResourceDeviceGroupSearchWebControl()));

        // GROUP_ID
        final FindSearchAgent groupAgent;
        groupAgent = new FindSearchAgent(ResourceDeviceGroupXInfo.GROUP_ID, ResourceDeviceGroupSearchXInfo.GROUP_ID);
        addAgent(groupAgent.addIgnore(ResourceDeviceGroupSearch.DEFAULT_GROUPID));

        // SPID
        final SelectSearchAgent spidAgent;
        spidAgent = new SelectSearchAgent(ResourceDeviceGroupXInfo.SPID, ResourceDeviceGroupSearchXInfo.SPID);
        addAgent(spidAgent.addIgnore(Integer.valueOf(ResourceDeviceGroupSearch.DEFAULT_SPID)));

        // SUBSCRIPTION_TYPE
        final SelectSearchAgent idAgent;
        idAgent = new SelectSearchAgent(ResourceDeviceGroupXInfo.SUBSCRIPTION_TYPE, ResourceDeviceGroupSearchXInfo.SUBSCRIPTION_TYPE);
        addAgent(idAgent.addIgnore(Long.valueOf(ResourceDeviceGroupSearch.DEFAULT_SUBSCRIPTIONTYPE)));
    }

} // class
