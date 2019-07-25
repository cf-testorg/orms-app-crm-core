package com.redknee.app.crm.home.province;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.bean.ErIndexToProvinceConfig;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;


public class ErIndexToProvinceConfigHomePipelineFactory implements PipelineFactory
{

    @Override
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = CoreSupport.bindHome(ctx, ErIndexToProvinceConfig.class);
        home = ConfigChangeRequestSupportHelper.get(ctx).registerHomeForConfigSharing(ctx, home, ErIndexToProvinceConfig.class);
        
        return home;
    }
}
