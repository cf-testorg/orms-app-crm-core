package com.redknee.app.crm.home.core;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.app.crm.bean.SystemAdjustTypeMapping;
import com.redknee.app.crm.bean.SystemAdjustTypeMappingTransientHome;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.xhome.home.ConfigShareTotalCachingHome;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

public class CoreSystemAdjustmentTypeMappingPipeLineFactory implements PipelineFactory
{

    public CoreSystemAdjustmentTypeMappingPipeLineFactory() 
    {
        super();
    }

    public Home createPipeline(Context ctx, Context serverCtx)
            throws RemoteException, HomeException, IOException 
    {
        Home  home = StorageSupportHelper.get(ctx).createHome(ctx, SystemAdjustTypeMapping.class, "SystemAdjustTypeMapping");
        
        home = new AuditJournalHome(ctx, home); 
        
        home = new ConfigShareTotalCachingHome(ctx, new SystemAdjustTypeMappingTransientHome(ctx), home); 
        
        return home; 

    }

}