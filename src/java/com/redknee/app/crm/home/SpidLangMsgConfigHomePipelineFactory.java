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

import com.redknee.app.crm.bean.SpidLangMsgConfig;
import com.redknee.app.crm.billing.message.BillingMessageHomePipelineFactory;
import com.redknee.app.crm.support.messages.MessageConfigurationSupport;
import com.redknee.framework.xhome.beans.ID;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;
/**
 * Installation factory for SpidLanMsgConfig beans, supporting multiple languages.
 * @author angie.li@redknee.com
 *
 * @param <MESSSAGE>
 */
public class SpidLangMsgConfigHomePipelineFactory<MESSSAGE extends SpidLangMsgConfig, MESSAGEID extends ID> implements PipelineFactory 
{

    /* By default install non transient Home.
     */
    public SpidLangMsgConfigHomePipelineFactory(Class beanClass, Class idBeanClass, String tableName, MESSAGEID ID)
    {
        this(beanClass, idBeanClass, tableName, ID, false);
    }
    
    public SpidLangMsgConfigHomePipelineFactory(Class beanClass, Class idBeanClass, String tableName, MESSAGEID ID, boolean installTransient)
    {
        bean_ = beanClass;
        idBean_ = idBeanClass;
        tableName_ = tableName;
        id_ = ID;
        isTransient_ = installTransient;
    }
    
    /* Install the Home under the Bean's Home key
     * Install the MessageConfigurationSupport under the Bean's LanguageSupportKey
     * (non-Javadoc)
     * @see com.redknee.app.crm.home.PipelineFactory#createPipeline(com.redknee.framework.xhome.context.Context, com.redknee.framework.xhome.context.Context)
     */
    public Home createPipeline(final Context ctx, final Context serverCtx) throws RemoteException, HomeException,
    IOException, AgentException
    {
        Class homeClass = XBeans.getClass(ctx, bean_, Home.class);
        MsgConfigFactory<MESSSAGE, MESSAGEID> factory = new MsgConfigFactory<MESSSAGE, MESSAGEID>(isTransient_);
        Home msgHome = factory.createStore(ctx, bean_, tableName_);
        ctx.put(homeClass, msgHome);

        MessageConfigurationSupport msgSupport = factory.createSupport(serverCtx, homeClass, bean_, idBean_,  id_);
        ctx.put(BillingMessageHomePipelineFactory.getBillingMessageConfigurationKey(bean_), 
                msgSupport);
        
        if (LogSupport.isDebugEnabled(ctx))
        {
            LogSupport.debug(ctx, this, "Installed the SpidLangMsgConfigHome for bean class=" + bean_.getName() 
                    + " tableName=" + tableName_  + " (Transient=" + isTransient_ + ")");
        }
        return msgHome;
    }

    Class bean_;
    Class idBean_;
    String tableName_;
    MESSAGEID id_;
    boolean isTransient_;
}
