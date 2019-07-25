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
import com.redknee.framework.xhome.context.ReturnContextAgent;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;

import com.redknee.app.crm.bean.ServicePackage;
import com.redknee.app.crm.bean.search.ServicePackageSearch;
import com.redknee.app.crm.bean.search.ServicePackageSearchWebControl;
import com.redknee.app.crm.bean.search.ServicePackageSearchXInfo;
import com.redknee.app.crm.bean.ServicePackageXInfo;

/**
 * @author psperneac@redknee.com
 */
public class ServicePackageSearchBorder extends SearchBorder
{
    public ServicePackageSearchBorder(Context ctx)
    {
        super(ctx, ServicePackage.class, new ServicePackageSearchWebControl());

        addAgent(new ContextAgentProxy()
        {
            public void execute(Context ctx) throws AgentException
            {
                final ServicePackageSearch criteria = (ServicePackageSearch) getCriteria(ctx);
                final long id = criteria.getId();

                if (id != -1 && null != doFind(ctx, new EQ(ServicePackageXInfo.ID, Long.valueOf(id))))
                {
                    // no need to continue once we've found the account type
                    ReturnContextAgent.doReturn(ctx);
                    return;
                }
                delegate(ctx);
            }
        });

        addAgent(new WildcardSelectSearchAgent(ServicePackageXInfo.NAME, ServicePackageSearchXInfo.NAME,true));

        addAgent(new ContextAgentProxy()
        {
            public void execute(Context ctx) throws AgentException
            {
                final ServicePackageSearch criteria = (ServicePackageSearch) getCriteria(ctx);

                if (criteria.getSpid() != -1)
                {
                    SearchBorder.doSelect(ctx, new EQ(ServicePackageXInfo.SPID, Integer.valueOf(criteria.getSpid())));

                }
                delegate(ctx);
            }
        });

    }
}
