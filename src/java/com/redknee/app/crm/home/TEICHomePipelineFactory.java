package com.redknee.app.crm.home;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.app.crm.bean.TaxExemptionInclusion;
import com.redknee.app.crm.bean.TaxExemptionInclusionTransientHome;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.xhome.home.ConfigShareTotalCachingHome;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.msp.SpidAwareHome;

/**
 * 
 * 
 * @author bhupendra.pandey@redknee.com
 * @since
 */
public class TEICHomePipelineFactory implements PipelineFactory
{

    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home teicHome = StorageSupportHelper.get(ctx).createHome(ctx, TaxExemptionInclusion.class, "TAXEXEMPTIONINCLUSION");
        teicHome = new AuditJournalHome(ctx, teicHome);
        teicHome = new ConfigShareTotalCachingHome(ctx, new TaxExemptionInclusionTransientHome(ctx), teicHome);
        teicHome = new SpidAwareHome(ctx, teicHome);
        return teicHome;
    }
}
