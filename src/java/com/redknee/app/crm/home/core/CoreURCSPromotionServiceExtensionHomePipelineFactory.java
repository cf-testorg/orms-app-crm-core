package com.redknee.app.crm.home.core;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NoSelectAllHome;
import com.redknee.framework.xhome.home.ValidatingHome;

import com.redknee.app.crm.extension.ExtensionInstallationHome;
import com.redknee.app.crm.extension.service.core.URCSPromotionServiceExtension;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;


public class CoreURCSPromotionServiceExtensionHomePipelineFactory implements PipelineFactory
{
    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = StorageSupportHelper.get(ctx).createHome(
                ctx,
                URCSPromotionServiceExtension.class,
        "SVCEXTURCSPROMOTION");
        
        home = new AdapterHome(ctx, 
                new ExtendedBeanAdapter<com.redknee.app.crm.extension.service.URCSPromotionServiceExtension, com.redknee.app.crm.extension.service.core.URCSPromotionServiceExtension>(
                        com.redknee.app.crm.extension.service.URCSPromotionServiceExtension.class, 
                        com.redknee.app.crm.extension.service.core.URCSPromotionServiceExtension.class), 
                        home);

        home = new ExtensionInstallationHome(ctx, home);
        home = new ValidatingHome(home);
        home = new NoSelectAllHome(home);
        
        return home;
    }

}