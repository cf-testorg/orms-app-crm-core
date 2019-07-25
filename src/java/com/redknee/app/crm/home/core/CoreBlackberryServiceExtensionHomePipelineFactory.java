package com.redknee.app.crm.home.core;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.app.crm.extension.ExtensionInstallationHome;
import com.redknee.app.crm.extension.service.BlackberryServiceExtension;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NoSelectAllHome;
import com.redknee.framework.xhome.home.ValidatingHome;


public class CoreBlackberryServiceExtensionHomePipelineFactory implements PipelineFactory
{
    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = StorageSupportHelper.get(ctx).createHome(
                ctx,
                BlackberryServiceExtension.class,
        "SVCEXTBLACKBERRY");
        
        home = new AdapterHome(ctx, 
                new ExtendedBeanAdapter<com.redknee.app.crm.extension.service.BlackberryServiceExtension, com.redknee.app.crm.extension.service.core.BlackberryServiceExtension>(
                        com.redknee.app.crm.extension.service.BlackberryServiceExtension.class, 
                        com.redknee.app.crm.extension.service.core.BlackberryServiceExtension.class), 
                        home);

        home = new ExtensionInstallationHome(ctx, home);
        
//        CompoundValidator validator = new CompoundValidator();
//        validator.add(new BlackBerryServiceExtensionMandatoryFieldValidator());
//        home = new ValidatingHome(home, validator);

        home = new ValidatingHome(home);
        
//        home = new BlackberryServiceExtensionServicesFillingHome(ctx, home);

        home = new NoSelectAllHome(home);
        
        return home;
    }

}