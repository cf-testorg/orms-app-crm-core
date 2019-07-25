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

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgentProxy;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.web.search.FindSearchAgent;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;

import com.redknee.app.crm.bean.CallType;
import com.redknee.app.crm.bean.CallTypeXInfo;
import com.redknee.app.crm.bean.search.CallTypeSearch;
import com.redknee.app.crm.bean.search.CallTypeSearchWebControl;
import com.redknee.app.crm.bean.search.CallTypeSearchXInfo;

/**
 * @author psperneac
 * @since May 4, 2005 2:09:45 PM
 */
public class CallTypeSearchBorder extends SearchBorder
{
    public CallTypeSearchBorder(Context context)
    {
        super(context,CallType.class,new CallTypeSearchWebControl());

        // Call Type ID
        addAgent(new ContextAgentProxy()
              {
                 @Override
                public void execute(Context ctx)
                    throws AgentException
                 {
                    CallTypeSearch criteria = (CallTypeSearch)getCriteria(ctx);
                    if (criteria.getId() != -1)
                    {
                        doSelect(
                                ctx, 
                                new EQ(CallTypeXInfo.ID, Integer.valueOf(criteria.getId())));
                    }
                    delegate(ctx);
                 }
              }
        );

        // Call Type spid
        addAgent(new ContextAgentProxy()
              {
                 @Override
                public void execute(Context ctx)
                    throws AgentException
                 {
                    CallTypeSearch criteria = (CallTypeSearch)getCriteria(ctx);
                    if (criteria.getSpid() != -1)
                    {
                       doSelect(
                          ctx, 
                          new EQ(CallTypeXInfo.SPID, Integer.valueOf(criteria.getSpid())));
                    }
                    delegate(ctx);
                 }
              }
        );

        // Call Type invoice description
        addAgent(new WildcardSelectSearchAgent(CallTypeXInfo.INVOICE_DESC, CallTypeSearchXInfo.INVOICE_DESC, true));

        // Call Type gl Code
        addAgent(new FindSearchAgent(CallTypeXInfo.GLCODE, CallTypeSearchXInfo.GL_CODE));
    }
}
