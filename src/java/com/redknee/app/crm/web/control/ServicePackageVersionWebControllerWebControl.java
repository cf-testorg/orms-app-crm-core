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
package com.redknee.app.crm.web.control;

import java.io.PrintWriter;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.ServicePackageVersion;
import com.redknee.app.crm.bean.ServicePackageVersionXInfo;
import com.redknee.app.crm.support.WebControlSupportHelper;


/**
 * Provides a custom WebController for the versions of a service package that set up
 * the action links and buttons properly.
 *
 * @author paul.sperneac@redknee.com
 */
public
class ServicePackageVersionWebControllerWebControl
    extends WebControllerWebControl57
{
    /**
     * Creates a new WebController.
     */
    public ServicePackageVersionWebControllerWebControl()
    {
        super(ServicePackageVersion.class);
    }

    /**
     * the SQL clause used for linking them
     * @param key
     * @return
     */
    @Override
    public String createSQLClause(Object key)
    {
        return "id = " + key;
    }

    @Override
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        ctx=ctx.createSubContext();
        
        WebControlSupportHelper.get(ctx).hideProperty(ctx, ServicePackageVersionXInfo.PACKAGE_FEES);

        super.toWeb(ctx, out, name, obj);
    }

}
