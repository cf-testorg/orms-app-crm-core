/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.home;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.app.crm.bean.SpidLangMsgConfig;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.support.messages.MessageConfigurationSupport;
import com.redknee.framework.xhome.beans.ID;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.LRUCachingHome;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.home.TransientHome;
import com.redknee.framework.xhome.msp.SpidAwareHome;

/**
 * 
 * @author simar.singh@redknee.com
 * A class the creates Support and Store for Parameterized Message Bean/Homes
 * @param <MESSAGE>
 * 
 */

public class MsgConfigFactory <MESSAGE extends SpidLangMsgConfig, MESSAGEID extends ID>
{
    private boolean isTransient_;
    
    /** Legacy **/
    public MsgConfigFactory()
    {
        this(false);
    }
    
    /**
     * Added to support Unit tests.
     * @param installTransient
     */
    public MsgConfigFactory(boolean installTransient)
    {
        isTransient_ = installTransient;
    }
    
   /**
    * create store for <MESSAGE> beans
    */
    public Home createStore(final Context ctx, final Class<MESSAGE> beanClass, String tableName) throws RemoteException, HomeException,
            IOException, AgentException
    {
        /*
         * We have not wrapped this home with RMICluster.. This may be a concern in E-Car
         * - BAS split deployments If we have an E-Care process that requires dealing with
         * these messages we need to cluster.
         */
        Home msgHome;
        if (isTransient_)
        {
            msgHome = (Home) XBeans.getInstanceOf(ctx, beanClass, TransientHome.class);
        }
        else
        {
            msgHome = StorageSupportHelper.get(ctx).createHome(ctx, beanClass, tableName);
            msgHome = new AuditJournalHome(ctx, msgHome);
            msgHome = new LRUCachingHome(ctx, beanClass, true, msgHome);
            // select() may be called from GUI; that happens to be the only place where sorted
            // results are required
            msgHome = new SortingHome(msgHome);
            msgHome = new SpidAwareHome(ctx, msgHome);
        }

        return msgHome;
    }
    
    /**
     * create support for <MESSAGE> beans 
     * @param ctx
     * @param homeKey
     * @return
     */
    public MessageConfigurationSupport<MESSAGE, MESSAGEID> createSupport(final Context ctx, Object homeKey, Class<MESSAGE> beanClass, Class<MESSAGEID> idBeanClass, MESSAGEID messageID)
    {
        return new MessageConfigurationSupport<MESSAGE, MESSAGEID>(ctx,homeKey, beanClass, idBeanClass, messageID); 
    } 
}
