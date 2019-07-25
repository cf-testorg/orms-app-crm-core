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

import com.redknee.app.crm.bean.DiscountClassTemplateInfo;
import com.redknee.app.crm.bean.DiscountClassTemplateInfoXDBHome;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NoSelectAllHome;
import com.redknee.framework.xhome.home.SortingHome;

/**
 * 
 * Pipelinefactory for discount class
 * 
 * @author ankit.nagpal@redknee.com
 * since 9_7_2
 */
public class CoreDiscountClassTemplateInfoPipelineFactory implements PipelineFactory
{

    /* (non-Javadoc)
     * @see com.redknee.app.crm.home.PipelineFactory#createPipeline(com.redknee.framework.xhome.context.Context, com.redknee.framework.xhome.context.Context)
     */

    public Home createPipeline(Context ctx, Context serverCtx)
            throws RemoteException, HomeException, IOException, AgentException {
        
    	Home home = new DiscountClassTemplateInfoXDBHome(ctx, "DISCOUNTCLASSTEMPLATEINFO");
        return home;
    }

}
