package com.redknee.app.crm.home.province;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.app.crm.bean.MscToProvinceMapping;
import com.redknee.app.crm.bean.MscToProvinceMappingTransientHome;
import com.redknee.app.crm.collection.AbstractPrefixToProvinceMapper;
import com.redknee.app.crm.collection.MscPrefixToProvinceMapper;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.CachingHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NotifyingHome;


public class MscToProvinceMappingHomePipelineFactory implements PipelineFactory
{

    @Override
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = StorageSupportHelper.get(ctx).createHome(ctx, MscToProvinceMapping.class, "MSCTOPROVINCEMAPPING");
        home = new NotifyingHome(home);
        home = new CachingHome(ctx, MscToProvinceMapping.class, new MscToProvinceMappingTransientHome(ctx), home);
        home = new ProvinceMapperCacheUpdateHome(ctx, home)
        {

            AbstractPrefixToProvinceMapper getProvinceMapper(Context ctx)
            {
                return (MscPrefixToProvinceMapper) ctx.get(MscPrefixToProvinceMapper.class);
            }
        };
        return home;
    }
}