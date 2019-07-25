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
package com.redknee.app.crm.web.search;

import com.redknee.app.crm.CoreCrmConstants;
import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.bean.PricePlanFunctionEnum;
import com.redknee.app.crm.bean.PricePlanXInfo;
import com.redknee.app.crm.bean.search.PricePlanSearch;
import com.redknee.app.crm.support.LicensingSupportHelper;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgentProxy;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.LT;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.elang.Wildcard;
import com.redknee.framework.xhome.web.search.SearchBorder;

/**
 * @author paul.sperneac@redknee.com
 * @since May 1, 2005 8:59:00 PM
 */
public class PricePlanCustomSearchAgent extends ContextAgentProxy
{
    @Override
    public void execute(Context ctx) throws AgentException
    {
        final PricePlanSearch search = (PricePlanSearch) SearchBorder.getCriteria(ctx);
        if (search != null)
        {
            //ID Search
            Object lId = True.instance();
            if (search.getId() != -1)
            {
                lId = new EQ(PricePlanXInfo.ID, Long.valueOf(search.getId()));
            }

            //Spid Search
            Object lSpid = True.instance();
            if (search.getSpid() != -1 && search.getSpid() != 9999)
            {
                lSpid = new EQ(PricePlanXInfo.SPID, Integer.valueOf(search.getSpid()));
            }

            //Name search
            Object lName = True.instance();
            if (search.getName() != null && search.getName().trim().length() > 0)
            {
                lName = new Wildcard(PricePlanXInfo.NAME, search.getName());
            }

            And condition = new And();

            condition.add(new EQ(PricePlanXInfo.PRICE_PLAN_FUNCTION, pricePlanFunction_));

            if (!LicensingSupportHelper.get(ctx).isLicensed(ctx, CoreCrmLicenseConstants.RK_DEV_LICENSE))
            {
                // limit the ID space so that special PP do not get included
                // remove the ID filtering after migration of Pool price plans to Pool price plan function
                condition.add(new LT(PricePlanXInfo.ID, CoreCrmConstants.POOL_PP_ID_START));
            }

            if (lId != True.instance())
            {
                condition.add(lId);
            }
            if (lSpid != True.instance())
            {
                condition.add(lSpid);
            }
            if (lName != True.instance())
            {
                condition.add(lName);
            }

            SearchBorder.doSelect(ctx, condition);
        }

        super.execute(ctx);
    }

    public void setPricePlanFunction(final PricePlanFunctionEnum pricePlanFunction)
    {
        pricePlanFunction_ = pricePlanFunction;
    }

    private PricePlanFunctionEnum pricePlanFunction_ = PricePlanFunctionEnum.NORMAL;
}
