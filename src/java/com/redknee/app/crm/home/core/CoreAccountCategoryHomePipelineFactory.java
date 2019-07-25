/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily
 * available. Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee, no unauthorised use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the licence agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright &copy; Redknee Inc. and its subsidiaries. All Rights Reserved.
 *
 */
package com.redknee.app.crm.home.core;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.app.crm.bean.AccountCategory;
import com.redknee.app.crm.bean.AccountCategoryTransientHome;
import com.redknee.app.crm.bean.AccountCategoryXMLHome;
import com.redknee.app.crm.billing.message.BillingMessageAwareHomeDecorator;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.app.crm.home.AccountCategoryIDSettingHome;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.xhome.visitor.HomeMigrationVisitor;
import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;
import com.redknee.app.crm.xhome.home.ConfigShareTotalCachingHome;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NotifyingHome;
import com.redknee.framework.xlog.log.MajorLogMsg;

/**
 * @author cindy.wong@redknee.com
 * @since 9.0
 */
public class CoreAccountCategoryHomePipelineFactory implements PipelineFactory
{
	public CoreAccountCategoryHomePipelineFactory()
	{
		super();
	}

	@Override
	public Home createPipeline(Context ctx, Context serverCtx)
	    throws RemoteException, HomeException, IOException, AgentException
	{
		// Services used to be stored in the journal.  If journal contains bindHome 
        // entry then the old Home will be non-null and we will migrate data from 
        // the journal to the database.
        Home oldHome = CoreSupport.bindHome(ctx, AccountCategory.class); 

        Home home =
		    StorageSupportHelper.get(ctx).createHome(ctx,
		        AccountCategory.class, "AccountType");

		 //TT#12091155018 . Providing Billing Message Configuration at Account Type level
        home = new AdapterHome(
                ctx, 
                home, 
                new ExtendedBeanAdapter<com.redknee.app.crm.bean.AccountCategory, com.redknee.app.crm.bean.core.AccountCategory>(
                        com.redknee.app.crm.bean.AccountCategory.class, 
                        com.redknee.app.crm.bean.core.AccountCategory.class));

        
		home = new BillingMessageAwareHomeDecorator().decorateHome(ctx, home);
		home = new ConfigShareTotalCachingHome( ctx, new AccountCategoryTransientHome(ctx), home); 
		home = new NotifyingHome(home);
		home = new AuditJournalHome(ctx, home);
		home =  new AccountCategoryIDSettingHome(ctx, home);
		
		 if (oldHome!=null)
	        {
	            String failureFilename = CoreSupport.getFile(ctx, "MigrationFailures-AccountCategories.xml");
	            try
	            {
	                Home backupHome = new AccountCategoryXMLHome(ctx, failureFilename);
	                oldHome.forEach(ctx, new HomeMigrationVisitor(oldHome, home, backupHome, false));
	                if (!HomeSupportHelper.get(ctx).hasBeans(ctx, backupHome, True.instance()))
	                {
	                    backupHome.drop(ctx);
	                }
	            }
	            catch (Exception e)
	            {
	                new MajorLogMsg(
	                        this,
	                        "Error(s) occurred migrating account categories from journal to database.", e).log(ctx);
	            } 
	            
	        }

	   return home;
	}

}
