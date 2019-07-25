/*
 * Copyright (c) 2012, Redknee Inc. and its subsidiaries. All Rights Reserved.
 *
 * This code is a protected work and subject to domestic and international copyright law(s). 
 * A complete listing of authors of this work is readily available. Additionally, source
 * code is, by its very nature, confidential information and inextricably contains trade
 * secrets and other information proprietary, valuable and sensitive to Redknee. No unauthorized
 * use, disclosure, manipulation or otherwise is permitted, and may only be used in accordance
 * with the terms of the license agreement entered into with Redknee Inc. and/or its subsidiaries.
 */
package com.redknee.app.crm.bean.calldetail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.redknee.app.crm.bean.ChargedBundleInfo;
import com.redknee.app.crm.bean.ChargedBundleInfoTransientHome;
import com.redknee.app.crm.bean.ChargedBundleInfoXInfo;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.GTE;
import com.redknee.framework.xhome.elang.LTE;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;


/**
 * 
 * @author mangaraj.sahoo@redknee.com
 * @since 9.3.2
 */
public class ChargedBundlePicker
{
    private final ChargedBundleInfoTransientHome cache_;
    
    public ChargedBundlePicker(final Context ctx, final String subscriberId, final Date start, final Date end) throws HomeInternalException, HomeException
    {
        if (subscriberId == null || "".equals(subscriberId.trim()))
        {
            throw new HomeException("Failed to initialize: Subscriber ID is null or empty.");
        }
        
        cache_ = new ChargedBundleInfoTransientHome(ctx);
        
        final And where = new And();
        where.add(new EQ(ChargedBundleInfoXInfo.SUBSCRIBER_ID, subscriberId));
        
        if (start != null)
        {
            where.add(new GTE(ChargedBundleInfoXInfo.TRANS_DATE, start));
        }
        
        if (end != null)
        {
            where.add(new LTE(ChargedBundleInfoXInfo.TRANS_DATE, end));
        }
        
        Collection<ChargedBundleInfo> collection =  HomeSupportHelper.get(ctx).getBeans(ctx, ChargedBundleInfo.class, where);
        
        for (ChargedBundleInfo bundle : collection)
        {
            cache_.create(ctx, bundle);
        }
    }
    
    public Collection<ChargedBundleInfo> getChargedBundles(final Context ctx, final CallDetail callDetail) throws HomeInternalException, HomeException 
    {
        if (callDetail == null)
        {
            return new ArrayList<com.redknee.app.crm.bean.ChargedBundleInfo>();
        }
        
        final And where = new And();
        where.add(new EQ(ChargedBundleInfoXInfo.CALL_DETAIL_ID, callDetail.getId()));
        where.add(new EQ(ChargedBundleInfoXInfo.TRANS_DATE, callDetail.getTranDate()));
        
        return cache_.select(ctx, where);
    } 
    public static Collection<ChargedBundleInfo> getChargedBundlesForCallDetail(final Context ctx, long callDetailId) throws HomeInternalException, HomeException
    {
    	final And where = new And();
        where.add(new EQ(ChargedBundleInfoXInfo.CALL_DETAIL_ID, callDetailId));
        
        return HomeSupportHelper.get(ctx).getBeans(ctx, ChargedBundleInfo.class, where);
    }
}
