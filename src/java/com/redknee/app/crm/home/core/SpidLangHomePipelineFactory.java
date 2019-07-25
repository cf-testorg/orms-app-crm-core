package com.redknee.app.crm.home.core;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.cluster.RMIClusteredHome;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.SortingHome;

import com.redknee.app.crm.bean.IdentifierEnum;
import com.redknee.app.crm.bean.SpidLang;
import com.redknee.app.crm.core.agent.StorageInstall;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.sequenceId.IdentifierSettingHome;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;


public class SpidLangHomePipelineFactory implements PipelineFactory
{
    @Override
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home crmSpidLangHome = CoreSupport.bindHome(ctx, SpidLang.class);
        
        crmSpidLangHome = new AuditJournalHome(ctx, crmSpidLangHome);
        crmSpidLangHome = new IdentifierSettingHome(ctx, crmSpidLangHome, IdentifierEnum.CRM_SPID_LANG_ID, null);
        crmSpidLangHome = new SortingHome(crmSpidLangHome);
        crmSpidLangHome = new RMIClusteredHome(ctx, SpidLang.class.getName() + StorageInstall.CORE_CLUSTER_SUFFIX,crmSpidLangHome);
        
		crmSpidLangHome =
		    ConfigChangeRequestSupportHelper.get(ctx)
		        .registerHomeForConfigSharing(ctx, crmSpidLangHome,
		            SpidLang.class);
        
        return crmSpidLangHome;
    }
}
