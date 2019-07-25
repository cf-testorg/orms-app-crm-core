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
package com.redknee.app.crm.home;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Comparator;

import com.redknee.app.crm.bean.externalapp.ExternalAppResultCode;
import com.redknee.app.crm.bean.externalapp.ExternalAppResultCodeTransientHome;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.xhome.home.TotalCachingHome;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.SortingHome;


/**
 * 
 *
 * @author Marcio Marques
 * @since 9.1.3
 */
public class ExternalAppResultCodeHomePipelineFactory implements PipelineFactory
{

    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = StorageSupportHelper.get(ctx).createHome(ctx, ExternalAppResultCode.class, "EXTERNALAPPRESULTCODE");
        home = new AuditJournalHome(ctx, home);
        home = new SortingHome(ctx, home, new Comparator() 
        {

            @Override
            public int compare(Object o1, Object o2)
            {
                ExternalAppResultCode obj1 = (ExternalAppResultCode) o1;
                ExternalAppResultCode obj2 = (ExternalAppResultCode) o2;
                if (o2==null)
                {
                    return -1;
                }
                else if (o1 == null)
                {
                    return 1;
                }
                else if (obj1.getResultCode()<obj2.getResultCode())
                {
                    return -1;
                }
                else if (obj1.getResultCode()>obj2.getResultCode())
                {
                    return 1;
                }
                else if (obj1.getExternalApp()<obj2.getExternalApp())
                {
                    return -1;
                }
                else if (obj1.getExternalApp()>obj2.getExternalApp())
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
            
        });        
        home = new TotalCachingHome(ctx, new ExternalAppResultCodeTransientHome(ctx), home);
        return home;
    }

}
