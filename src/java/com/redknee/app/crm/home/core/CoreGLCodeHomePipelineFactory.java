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
package com.redknee.app.crm.home.core;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.app.crm.bean.GLCodeMapping;
import com.redknee.app.crm.bean.GLCodeMappingTransientHome;
import com.redknee.app.crm.bean.GLCodeMappingXInfo;
import com.redknee.app.crm.bean.GLCodeStateEnum;
import com.redknee.app.crm.home.GLCodeERLogHome;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.xhome.home.ConfigShareTotalCachingHome;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.OrderByHome;
import com.redknee.framework.xhome.msp.SpidAwareHome;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CoreGLCodeHomePipelineFactory implements PipelineFactory
{

    /**
     * {@inheritDoc}
     */
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = StorageSupportHelper.get(ctx).createHome(ctx, GLCodeMapping.class, "GLCODEMAPPING");
        
        home = new AuditJournalHome(ctx, home);
        
        home = new ConfigShareTotalCachingHome( ctx, new GLCodeMappingTransientHome(ctx), home); 
        
        home = home.where(ctx, new EQ(GLCodeMappingXInfo.STATE, GLCodeStateEnum.ACTIVE_INDEX));
        
        home = new OrderByHome(ctx, home);
        ((OrderByHome)home).setSortOnForEach(true);
        ((OrderByHome)home).addOrderBy(GLCodeMappingXInfo.GL_CODE, true);
        
        home = new SpidAwareHome(ctx, home);
        
        home = new GLCodeERLogHome(home);
        
        return home;
    }

}
