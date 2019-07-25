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
package com.redknee.app.crm.collection;

import java.util.Iterator;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NotifyingHomeCmd;
import com.redknee.framework.xhome.util.collection.trie.PrefixMap;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.MsisdnToProvinceMapping;
import com.redknee.app.crm.bean.MsisdnToProvinceMappingHome;
import com.redknee.app.crm.bean.MsisdnToProvinceMappingXInfo;
import com.redknee.app.crm.bean.ProvincePrefix;
import com.redknee.app.crm.bean.ProvincePrefixHome;

public class MsisdnPrefixToProvinceMapper extends AbstractPrefixToProvinceMapper
{
    /** 
     * @author bdhavalshankh
     * @since 9.5
     */
    
    public MsisdnPrefixToProvinceMapper(Context ctx)
    {
        super(ctx);
        registerForHomeNotifications(ctx);
    }

    protected void registerForHomeNotifications(Context ctx)
    {
        try
        {
            Home home = (Home) ctx.get(ProvincePrefixHome.class);
            home.cmd(ctx, new NotifyingHomeCmd(this));
            
            home = (Home) ctx.get(MsisdnToProvinceMappingHome.class);
            home.cmd(ctx, new NotifyingHomeCmd(this));
        }
        catch (HomeException e)
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                LogSupport.debug(ctx, this, e.getMessage(), e);
            }
        }
    }


    protected void populatePrefixMap(Context ctx, ProvincePrefix province, PrefixMap prefixMap)
    {

        if (ctx == null || province == null || prefixMap == null)
        {
            //Log error
            return;
        }
        
        Home home = (Home) ctx.get(ProvincePrefixConstants.TOTAL_MSISDN_TO_PROVINCE_MAPPING_HOME);
        if(home != null)
        {
            home = home.where(ctx, new EQ(MsisdnToProvinceMappingXInfo.PROVINCE_ID,province.getIdentifier()));
            
            try
            {
                for (Iterator j = home.selectAll().iterator(); j.hasNext();)
                {
                    MsisdnToProvinceMapping prefix = (MsisdnToProvinceMapping) j.next();
                    try
                    {
                        prefixMap.add(prefix.getPrefix().trim(), province.clone());
                    }
                    catch (CloneNotSupportedException e) {
                        // TODO: handle exception
                    }
                }
            }
            catch(HomeException e)
            {
            }
        }
    
    }
}
