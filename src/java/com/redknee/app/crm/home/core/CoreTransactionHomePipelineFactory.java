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

import com.redknee.app.crm.CoreCrmConstants;
import com.redknee.app.crm.bean.Transaction;
import com.redknee.app.crm.bean.TransactionHome;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.MultiDbSupport;
import com.redknee.app.crm.support.MultiDbSupportHelper;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.transaction.TransactionDateComparator;
import com.redknee.app.crm.util.cipher.SpidAwareEncryptingAdapter;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;
import com.redknee.app.crm.xhome.home.CoreTransactionERLogHome;
import com.redknee.app.crm.xhome.home.CreateOrStoreHome;
import com.redknee.app.crm.xhome.home.GLCodeObtainHome;
import com.redknee.app.crm.xhome.home.TransactionIdentifierSettingHome;
import com.redknee.app.crm.xhome.home.TransactionOwnerTypeSettingHome;
import com.redknee.app.crm.xhome.validator.TransactionAdjustmentTypeValidator;
import com.redknee.framework.core.home.PMHome;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NoSelectAllHome;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.home.ValidatingHome;
import com.redknee.framework.xhome.msp.SpidAwareHome;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;


/**
 * Provides a class from which to create the pipeline of Home decorators that process a
 * Transaction traveling between the application and the given delegate.
 *
 * @author gary.anderson@redknee.com
 */
public class CoreTransactionHomePipelineFactory implements PipelineFactory
{
    /**
     * Creates and installs pipelines for Transaction.
     *
     * @param context
     *            The application context.
     * @param serverContext
     *            The context used for remote services.
     * @return The Home representing the head of the pipeline.
     * @exception AgentException
     *                Thrown if there are any problems creating the pipeline.
     */
    public Home createPipeline(final Context context, final Context serverContext) throws AgentException
    {
        // NOTE -- 2006-05-05 - Most of this code was copied directly from
        // com.redknee.app.crm.agent.StorageInstall, and slightly reformatted
        // for easier reading. That is, it still needs more re-design work.

        Home home = null;

        try
        {
            String tableName ="TRANSACTION"; 
            
            if ( MultiDbSupportHelper.get(context).getDbsType(context) == MultiDbSupport.SQLSERVER)
            {
                tableName = "XTRANSACTION"; 
            }
            home = StorageSupportHelper.get(context).createHome(context, Transaction.class, tableName);
            
            home = new PMHome(context, TransactionHome.class.getName() + ".factorybefore.01.XDB", home);

            // Install a home to adapt between business logic bean and data bean
            home = new AdapterHome(
                    context, 
                    home, 
                    new ExtendedBeanAdapter<com.redknee.app.crm.bean.Transaction, com.redknee.app.crm.bean.core.Transaction>(
                            com.redknee.app.crm.bean.Transaction.class, 
                            com.redknee.app.crm.bean.core.Transaction.class));
            
            home = new AdapterHome(
                    context, 
                    home,
                    new SpidAwareEncryptingAdapter());
            
            home = new CreateOrStoreHome(context, home);

            home = new CoreTransactionERLogHome(context, home);
            home = new PMHome(context, TransactionHome.class.getName() + ".factorybefore.02.Before.IdentifierSetting", home);
            home = new TransactionIdentifierSettingHome(context, home, "TransactionID_seq");
            home = new PMHome(context, TransactionHome.class.getName() + ".factorybefore.03.After.IdentifierSetting", home);

            // [CW] direct connection to DB
            // the factory returns a home that checks for the adjustment limit.
            context.put(CoreCrmConstants.DISCOUNT_PLAIN_TXN_HOME, home);

            home = new GLCodeObtainHome(home);

            home = new TransactionOwnerTypeSettingHome(context, home);

            home = new ValidatingHome(TransactionAdjustmentTypeValidator.instance(), home);

            home = new SpidAwareHome(context, home);

            home = new NoSelectAllHome(home);

            home = new SortingHome(home, new TransactionDateComparator(true));
            home = new PMHome(context, TransactionHome.class.getName() + ".factorybefore.09.TotalTime", home);
        }
        catch (final HomeException e)
        {
            if (LogSupport.isDebugEnabled(context))
            {
                new DebugLogMsg(CoreTransactionHomePipelineFactory.class, e.getMessage(), e).log(context);
            }
            throw new AgentException(e);
        }

        return home;
    }

} // class