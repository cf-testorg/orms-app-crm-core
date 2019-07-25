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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;

import com.redknee.app.crm.collection.AbstractPrefixToProvinceMapper;


/**
 * Updates the cache
 * 
 * @author bdhavalshankh
 * @since 9.5.0
 */
public abstract class ProvincePrefixCacheUpdateHome extends HomeProxy
{

    /**
     * Creates a new RemoveProvinceValidatorHome.
     * 
     * @param ctx
     * 
     * @param delegate
     *            The Home to which we delegate.
     */
    public ProvincePrefixCacheUpdateHome(Context ctx, final Home delegate) throws HomeException
    {
        super(delegate);
    }


    /**
     * {@inheritDoc}
     */
    public Object create(Context ctx, Object obj) throws HomeException
    {
        Object o = super.create(ctx, obj);
        updateCache(ctx);
        return o;
    }


    abstract AbstractPrefixToProvinceMapper getMsisdnProvinceMapper(Context ctx);
    abstract AbstractPrefixToProvinceMapper getMscProvinceMapper(Context ctx);
    abstract AbstractPrefixToProvinceMapper getLocationZoneProvinceMapper(Context ctx);

    /**
     * {@inheritDoc}
     */
    public Object store(Context ctx, Object obj) throws HomeException, HomeInternalException
    {
        Object o = super.store(ctx, obj);
        updateCache(ctx);
        return o;
    }


    @Override
    public void remove(Context ctx, Object obj) throws HomeException
    {
        super.remove(ctx, obj);
        updateCache(ctx);
    }


    private void updateCache(Context ctx)
    {
        AbstractPrefixToProvinceMapper msisdnMapper = getMsisdnProvinceMapper(ctx);
        AbstractPrefixToProvinceMapper mscMapper = getMscProvinceMapper(ctx);
        AbstractPrefixToProvinceMapper locationMapper = getLocationZoneProvinceMapper(ctx);
        
        msisdnMapper.validate(ctx);
        mscMapper.validate(ctx);
        locationMapper.validate(ctx);
    }
}
