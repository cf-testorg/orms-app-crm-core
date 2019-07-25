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

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgentProxy;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;

import com.redknee.app.crm.bean.MsisdnGroup;
import com.redknee.app.crm.bean.MsisdnGroupXInfo;
import com.redknee.app.crm.bean.search.MsisdnGroupSearch;
import com.redknee.app.crm.bean.search.MsisdnGroupSearchWebControl;
import com.redknee.app.crm.bean.search.MsisdnGroupSearchXInfo;


/**
 * Provides a custom search border for the MsisdnGroupScreen that allows
 * templates to be searched by identifier and name.
 *
 * @author angie.li@redknee.com
 */
public
class MsisdnGroupSearchBorder
    extends SearchBorder
{
    /**
     * Creates a new search border.
     *
     * @param context The operating context.
     */
    public MsisdnGroupSearchBorder(Context context)
    {
        super(context, MsisdnGroup.class, new MsisdnGroupSearchWebControl());

        // Search agent for MsisdnGroup.id
        addAgent(
            new ContextAgentProxy()
            {
                @Override
                public void execute(Context context1)
                    throws AgentException
                {
                	MsisdnGroupSearch search = (MsisdnGroupSearch)getCriteria(context1);
                    if (search.getId() != -1)
                    {
                        addSelect(context1, new EQ(MsisdnGroupXInfo.ID, Integer.valueOf(search.getId())));
                    }

                    delegate(context1);
                }
            });

        // Search agent for MsisdnGroup.name
        addAgent(new WildcardSelectSearchAgent(MsisdnGroupXInfo.NAME, MsisdnGroupSearchXInfo.NAME, true));

    }
} // class
