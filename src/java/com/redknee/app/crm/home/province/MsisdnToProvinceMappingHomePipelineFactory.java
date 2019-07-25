package com.redknee.app.crm.home.province;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.CachingHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NotifyingHome;

import com.redknee.app.crm.bean.MsisdnToProvinceMapping;
import com.redknee.app.crm.bean.MsisdnToProvinceMappingTransientHome;
import com.redknee.app.crm.collection.AbstractPrefixToProvinceMapper;
import com.redknee.app.crm.collection.MsisdnPrefixToProvinceMapper;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;


public class MsisdnToProvinceMappingHomePipelineFactory implements PipelineFactory
{

    @Override
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        
        Home home = StorageSupportHelper.get(ctx).createHome(ctx, MsisdnToProvinceMapping.class, "MSISDNTOPROVINCEMAPPING");
        home=new NotifyingHome(home);
        home=new CachingHome(ctx, MsisdnToProvinceMapping.class, new MsisdnToProvinceMappingTransientHome(ctx), home);
        home = new ProvinceMapperCacheUpdateHome(ctx, home)
        {

            AbstractPrefixToProvinceMapper getProvinceMapper(Context ctx)
            {
                return (MsisdnPrefixToProvinceMapper) ctx.get(MsisdnPrefixToProvinceMapper.class);
            }
        };
        
        return home;
    }
}