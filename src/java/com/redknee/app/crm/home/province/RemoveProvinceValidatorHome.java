/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.home.province;

import com.redknee.app.crm.bean.CT23RuleHome;
import com.redknee.app.crm.bean.CT23RuleXInfo;
import com.redknee.app.crm.bean.ProvincePrefix;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.Or;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;


/**
 * Verifies if the ProvincePrefix can be removed.
 * 
 * @author bdhavalshankh
 * @since 9.5.0
 */
public class RemoveProvinceValidatorHome extends HomeProxy
{

    /**
     * Creates a new RemoveProvinceValidatorHome.
     * 
     * @param delegate
     *            The Home to which we delegate.
     */
    public RemoveProvinceValidatorHome(final Home delegate) throws HomeException
    {
        super(delegate);
    }


    @Override
    public void remove(Context ctx, Object obj) throws HomeException
    {
        final ProvincePrefix province = (ProvincePrefix) obj;
        final Home home = (Home) ctx.get(CT23RuleHome.class);
        
        Or filter = new Or();
        filter.add(new EQ(CT23RuleXInfo.HOME_PROV, province.getCode()));
        filter.add(new EQ(CT23RuleXInfo.ORIG_PROV, province.getCode()));
        filter.add(new EQ(CT23RuleXInfo.TERM_PROV, province.getCode()));
        final Object ct23Rule = home.find(ctx, filter);
        // Throws HomeException.
        if (ct23Rule != null)
        {
            throw new HomeException("Cannot delete Province while it is IN USE.");
        }
        getDelegate(ctx).remove(ctx, obj);
    }
}
