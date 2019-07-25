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

import com.redknee.app.crm.bean.core.ResourceDevice;
import com.redknee.app.crm.web.control.SearchBeanToContextWebControl;

/**
 * Provides search functionality for the resource device.
 *
 * @author victor.stratan@redknee.com
 */
public class ResourceDeviceSearchBorder extends SearchBorder
{
    /**
     * Creates a new ResourceDeviceSearchBorder.
     *
     * @param context The operating context.
     */
    public ResourceDeviceSearchBorder(final Context context)
    {
        super(context, ResourceDevice.class, new SearchBeanToContextWebControl(new ResourceDeviceSearchWebControl()));

        // RESOURCE_ID
        final FindSearchAgent idAgent;
        idAgent = new FindSearchAgent(ResourceDeviceXInfo.RESOURCE_ID, ResourceDeviceSearchXInfo.RESOURCE_ID);
        addAgent(idAgent.addIgnore(ResourceDeviceSearch.DEFAULT_RESOURCEID));

        // SPID
        final SelectSearchAgent spidAgent;
        spidAgent = new SelectSearchAgent(ResourceDeviceXInfo.SPID, ResourceDeviceSearchXInfo.SPID);
        addAgent(spidAgent.addIgnore(Integer.valueOf(ResourceDeviceSearch.DEFAULT_SPID)));

        // GROUP_ID
        final SelectSearchAgent typeAgent;
        typeAgent = new SelectSearchAgent(ResourceDeviceXInfo.GROUP_ID, ResourceDeviceSearchXInfo.GROUP_ID);
        addAgent(typeAgent.addIgnore(ResourceDeviceSearch.DEFAULT_GROUPID));

        // STATE
        final SelectSearchAgent stateAgent;
        stateAgent = new SelectSearchAgent(ResourceDeviceXInfo.STATE, ResourceDeviceSearchXInfo.STATE);
        addAgent(stateAgent.addIgnore(Integer.valueOf(ResourceDeviceSearch.DEFAULT_STATE)));

        // DEALER_CODE
        final SelectSearchAgent dealerAgent;
        dealerAgent = new SelectSearchAgent(ResourceDeviceXInfo.DEALER_CODE, ResourceDeviceSearchXInfo.DEALER_CODE);
        addAgent(dealerAgent.addIgnore(ResourceDeviceSearch.DEFAULT_DEALERCODE));

        // SERIAL_NUMBER
        final SelectSearchAgent serialAgent;
        serialAgent = new SelectSearchAgent(ResourceDeviceXInfo.SERIAL_NUMBER, ResourceDeviceSearchXInfo.SERIAL_NUMBER);
        addAgent(serialAgent);
    }

} // class
