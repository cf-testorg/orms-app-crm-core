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
package com.redknee.app.crm.billing.message;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.app.crm.bean.BillingMessage;
import com.redknee.app.crm.home.MsgConfigFactory;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.framework.xhome.beans.ID;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.ValidatingHome;
import com.redknee.framework.xlog.log.LogSupport;
/**
 * Installation factory for BillingMessage bean, supporting multiple languages.
 * This is the almost identical code to SpidLangMsgConfigHomePipelineFactory, EXCEPT
 * since we are installing BillingMessage bean homes (there are a few variations) we don't want the underlying bean 
 * storage system (XDB/Journal/TransientHome) to rely on the specific bean instead on the generic one (BillingMessage).
 * 
 * This makes the implementation in BillingMessageAdapter possible.
 * @author angie.li@redknee.com
 *
 * @param <MESSSAGE>
 */
public class BillingMessageHomePipelineFactory<MESSSAGE extends BillingMessage, MESSSAGEID extends ID> implements PipelineFactory 
{

    /* By default install non transient Home.
     */
    public BillingMessageHomePipelineFactory(Class<MESSSAGE> beanClass, Class<MESSSAGEID> idBeanClass, String tableName, MESSSAGEID ID)
    {
        this(beanClass, idBeanClass, tableName, ID, false);
    }
    
    public BillingMessageHomePipelineFactory(Class<MESSSAGE> beanClass, Class<MESSSAGEID> idBeanClass, String tableName, MESSSAGEID ID, boolean installTransient)
    {
        beanClass_ = beanClass;
        idBeanClass_ = idBeanClass;
        tableName_ = tableName;
        id_ = ID;
        isTransient_ = installTransient;
    }
    
    /* Install the Home under the Bean's Home key
     * Install the MessageConfigurationSupport under the Bean's LanguageSupportKey
     * All the underlying home are BillingMessageHomes.
     * (non-Javadoc)
     * @see com.redknee.app.crm.home.PipelineFactory#createPipeline(com.redknee.framework.xhome.context.Context, com.redknee.framework.xhome.context.Context)
     */
    public Home createPipeline(final Context ctx, final Context serverCtx) throws RemoteException, HomeException,
    IOException, AgentException
    {
        MsgConfigFactory<MESSSAGE, MESSSAGEID> factory = new MsgConfigFactory<MESSSAGE, MESSSAGEID>(isTransient_);
        Class homeClass = XBeans.getClass(ctx, beanClass_, Home.class);
        Home msgHome = factory.createStore(ctx, beanClass_, tableName_);
        msgHome = new ValidatingHome(new NoDefaultLanguageValidator(), msgHome);
        ctx.put(homeClass, msgHome);

        ctx.put(getBillingMessageConfigurationKey(beanClass_), factory.createSupport(serverCtx, homeClass, beanClass_, idBeanClass_, id_));
        
        if (LogSupport.isDebugEnabled(ctx))
        {
            LogSupport.debug(ctx, this, "Installed the SpidLangMsgConfigHome for bean class=" + beanClass_.getName() 
                    + " tableName=" + tableName_  + " (Transient=" + isTransient_ + ")");
        }
        return msgHome;
    }

    public static String getBillingMessageConfigurationKey(Class beanClass)
    {
        return beanClass.getName() + BILLING_MESSAGE_CONFIGURATION_SUPPORT_SUFIX;
    }
    
    Class<MESSSAGE> beanClass_;
    Class<MESSSAGEID> idBeanClass_;
    String tableName_;
    MESSSAGEID id_;
    boolean isTransient_;
    public static final String BILLING_MESSAGE_CONFIGURATION_SUPPORT_SUFIX = "BillingMessageConfigurationSupport";
}

