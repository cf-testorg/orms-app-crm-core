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

import com.redknee.app.crm.bean.externalapp.ExternalAppErrorCodeMsg;
import com.redknee.app.crm.bean.externalapp.ExternalAppErrorCodeMsgTransientHome;
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
public class ExternalAppErrorCodeMsgHomePipelineFactory implements PipelineFactory
{

    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = StorageSupportHelper.get(ctx).createHome(ctx, ExternalAppErrorCodeMsg.class, "EXTERNALAPPERRORCODEMSG");
        home = new AuditJournalHome(ctx, home);
        home = new SortingHome(ctx, home, new Comparator() 
        {

            @Override
            public int compare(Object o1, Object o2)
            {
                ExternalAppErrorCodeMsg obj1 = (ExternalAppErrorCodeMsg) o1;
                ExternalAppErrorCodeMsg obj2 = (ExternalAppErrorCodeMsg) o2;
                if (o2==null)
                {
                    return -1;
                }
                else if (o1 == null)
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
                else if (obj1.getErrorCode()<obj2.getErrorCode())
                {
                    return -1;
                }
                else if (obj1.getErrorCode()>obj2.getErrorCode())
                {
                    return 1;
                }
                else
                {
                    return (obj1.getLanguage().compareTo(obj2.getLanguage()));
                }
            }
        });        
        home = new TotalCachingHome(ctx, new ExternalAppErrorCodeMsgTransientHome(ctx), home);
        
        return home;
    }

}
