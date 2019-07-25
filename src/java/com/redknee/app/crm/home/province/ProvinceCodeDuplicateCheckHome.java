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

import com.redknee.app.crm.bean.ProvincePrefix;
import com.redknee.app.crm.bean.ProvincePrefixXInfo;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.LogSupport;


/**
 * 
 * @author bhagyashree.dhavalshankh@redknee.com
 * @since 9.6
 **/
public class ProvinceCodeDuplicateCheckHome extends HomeProxy
{

    private static final String MODULE = ProvinceCodeDuplicateCheckHome.class.getSimpleName();


    public ProvinceCodeDuplicateCheckHome(Context ctx, final Home delegate) throws HomeException
    {
        super(delegate);
    }


    @Override
    public Object create(Context ctx, Object obj) throws HomeException, HomeInternalException
    {
        ProvincePrefix prov = (ProvincePrefix) obj;
        if (isDuplicate(ctx, prov))
        {
            throw new HomeException("Duplicate Province code exists. Can not save");
        }
        return super.create(ctx, obj);
    }


    /**
     * Checks whether duplicate records exists in Province
     * 
     * @param ctx
     *            The operating context.
     * @param provincePrefix
     *            The ProvincePrefix for which to find duplicate record
     * 
     * @return true if duplicate exists else false
     * @since 9.6
     * @author bhagyashree.dhavalshankh@redknee.com
     * @throws HomeException
     * @throws HomeInternalException
     */
    public static boolean isDuplicate(final Context ctx, final ProvincePrefix provincePrefix)
            throws HomeInternalException, HomeException
    {
        And filter = new And();
        filter.add(new EQ(ProvincePrefixXInfo.CODE, provincePrefix.getCode()));
        Long recordCount = HomeSupportHelper.get(ctx).getBeanCount(ctx, ProvincePrefix.class, filter);
        if (recordCount > 0)
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                LogSupport.debug(ctx, MODULE, "Duplicate Province code found.");
            }
            return true;
        }
        return false;
    }
}
