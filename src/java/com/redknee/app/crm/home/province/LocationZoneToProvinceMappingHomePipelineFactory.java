package com.redknee.app.crm.home.province;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.CachingHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NotifyingHome;

import com.redknee.app.crm.bean.LocationZoneToProvinceMapping;
import com.redknee.app.crm.bean.LocationZoneToProvinceMappingTransientHome;
import com.redknee.app.crm.collection.AbstractPrefixToProvinceMapper;
import com.redknee.app.crm.collection.LocationZoneToProvinceMapper;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;


public class LocationZoneToProvinceMappingHomePipelineFactory implements PipelineFactory
{

    @Override
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = StorageSupportHelper.get(ctx).createHome(ctx, LocationZoneToProvinceMapping.class, "LOCATIONZONETOPROVINCEMAPPING");
        home=new NotifyingHome(home);
        home=new CachingHome(ctx, LocationZoneToProvinceMapping.class, new LocationZoneToProvinceMappingTransientHome(ctx), home);
        home = new ProvinceMapperCacheUpdateHome(ctx, home)
        {
            AbstractPrefixToProvinceMapper getProvinceMapper(Context ctx)
            {
                return (LocationZoneToProvinceMapper) ctx.get(LocationZoneToProvinceMapper.class);
            }
        };
        
        return home;
    }
}