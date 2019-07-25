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
package com.redknee.app.crm.factory.core;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextFactory;
import com.redknee.framework.xhome.msp.Spid;

import com.redknee.app.crm.bean.core.VSATPackage;
import com.redknee.app.crm.factory.CoreBeanAdaptingContextFactory;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class VSATPackageFactory extends CoreBeanAdaptingContextFactory<com.redknee.app.crm.bean.VSATPackage, VSATPackage>
{
    public VSATPackageFactory(ContextFactory delegate)
    {
        super(com.redknee.app.crm.bean.VSATPackage.class, 
                VSATPackage.class, 
                delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx)
    {
        VSATPackage pkg = (VSATPackage) super.create(ctx);

        final Spid spid = (Spid)ctx.get(Spid.class);
        if (pkg != null && spid != null)
        {
            pkg.setSpid(spid.getId());
        }
        
        return pkg;
    }

}
