/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily
 * available. Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee, no unauthorised use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the licence agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.web.border;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;

import com.redknee.app.crm.bean.PricePlan;
import com.redknee.app.crm.bean.PricePlanFunctionEnum;
import com.redknee.app.crm.bean.PricePlanXInfo;
import com.redknee.app.crm.bean.search.PricePlanSearchWebControl;
import com.redknee.app.crm.bean.search.PricePlanSearchXInfo;
import com.redknee.app.crm.web.search.PricePlanCustomSearchAgent;

/**
 * @author paul.sperneac@redknee.com
 * @since May 1, 2005 8:46:25 PM
 */
public class PricePlanSearchBorder extends SearchBorder
{
    public PricePlanSearchBorder(final Context ctx)
    {
        this(ctx, PricePlanFunctionEnum.NORMAL);
     	SelectSearchAgent spidAgent = new SelectSearchAgent(PricePlanXInfo.SPID, PricePlanSearchXInfo.SPID);
     	addAgent(spidAgent.addIgnore(Integer.valueOf(9999)));
    }

    public PricePlanSearchBorder(final Context ctx, final PricePlanFunctionEnum ppFunction)
    {
        super(ctx, PricePlan.class, new PricePlanSearchWebControl());

        final PricePlanCustomSearchAgent agent = new PricePlanCustomSearchAgent();
        agent.setPricePlanFunction(ppFunction);
        addAgent(agent);
    }
}
