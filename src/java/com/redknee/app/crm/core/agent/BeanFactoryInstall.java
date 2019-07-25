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
package com.redknee.app.crm.core.agent;

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgent;
import com.redknee.framework.xhome.context.ContextFactory;
import com.redknee.framework.xhome.context.ContextFactoryProxy;
import com.redknee.framework.xlog.log.CritLogMsg;

import com.redknee.app.crm.bean.Adjustment;
import com.redknee.app.crm.bean.calldetail.CallDetail;
import com.redknee.app.crm.bean.template.AdjustmentTemplate;
import com.redknee.app.crm.bean.template.AdjustmentTypeTemplate;
import com.redknee.app.crm.bean.template.GSMPackageTemplate;
import com.redknee.app.crm.bean.template.TDMAPackageTemplate;
import com.redknee.app.crm.bean.template.TransactionTemplate;
import com.redknee.app.crm.bean.template.VSATPackageTemplate;
import com.redknee.app.crm.factory.CallDetailFactory;
import com.redknee.app.crm.factory.ConstructorCallingBeanFactory;
import com.redknee.app.crm.factory.ConstructorStaticCtxCallingBeanFactory;
import com.redknee.app.crm.factory.ContextRedirectingContextFactory;
import com.redknee.app.crm.factory.CoreBeanAdaptingContextFactory;
import com.redknee.app.crm.factory.PrototypeContextFactory;
import com.redknee.app.crm.factory.core.AuxiliaryServiceFactory;
import com.redknee.app.crm.factory.core.GSMPackageFactory;
import com.redknee.app.crm.factory.core.MsisdnFactory;
import com.redknee.app.crm.factory.core.PricePlanFactory;
import com.redknee.app.crm.factory.core.PricePlanVersionFactory;
import com.redknee.app.crm.factory.core.ServicePackageFactory;
import com.redknee.app.crm.factory.core.ServicePackageVersionFactory;
import com.redknee.app.crm.factory.core.TDMAPackageFactory;
import com.redknee.app.crm.factory.core.TransactionFactory;
import com.redknee.app.crm.factory.core.VSATPackageFactory;

public class BeanFactoryInstall
    extends CoreSupport
    implements ContextAgent
{
    /**
     * Installs custom bean factories. 
     *
     * @param ctx context where the components will be installed
     */
    public void execute(final Context ctx)
    {
        try
        {
            // Non-core beans with factories
            XBeans.putBeanFactory(ctx, CallDetail.class, CallDetailFactory.instance());
            XBeans.putBeanFactory(ctx, Adjustment.class, new PrototypeContextFactory(AdjustmentTemplate.class));

            // Core beans with custom bean factories
            installCoreBean(ctx,
                    com.redknee.app.crm.bean.Msisdn.class,
                    com.redknee.app.crm.bean.core.Msisdn.class,
                    MsisdnFactory.instance());
            
            installCoreBean(ctx,
                    com.redknee.app.crm.bean.AdjustmentType.class,
                    com.redknee.app.crm.bean.core.AdjustmentType.class,
                    new PrototypeContextFactory(AdjustmentTypeTemplate.class));

            installCoreBean(ctx,
                    com.redknee.app.crm.bean.AuxiliaryService.class,
                    com.redknee.app.crm.bean.core.AuxiliaryService.class,
                    AuxiliaryServiceFactory.instance());
            
            installCoreBean(ctx,
                    com.redknee.app.crm.bean.Transaction.class,
                    com.redknee.app.crm.bean.core.Transaction.class,
                    new TransactionFactory(new PrototypeContextFactory(TransactionTemplate.class)));

            installCoreBean(ctx,
                    com.redknee.app.crm.bean.GSMPackage.class,
                    com.redknee.app.crm.bean.core.GSMPackage.class,
                    new GSMPackageFactory(new PrototypeContextFactory(GSMPackageTemplate.class)));

            installCoreBean(ctx,
                    com.redknee.app.crm.bean.TDMAPackage.class,
                    com.redknee.app.crm.bean.core.TDMAPackage.class,
                    new TDMAPackageFactory(new PrototypeContextFactory(TDMAPackageTemplate.class)));

            installCoreBean(ctx,
                    com.redknee.app.crm.bean.VSATPackage.class,
                    com.redknee.app.crm.bean.core.VSATPackage.class,
                    new VSATPackageFactory(new PrototypeContextFactory(VSATPackageTemplate.class)));

            installCoreBean(ctx,
                    com.redknee.app.crm.bean.PricePlan.class,
                    com.redknee.app.crm.bean.core.PricePlan.class,
                    PricePlanFactory.instance());

            installCoreBean(ctx,
                    com.redknee.app.crm.bean.PricePlanVersion.class,
                    com.redknee.app.crm.bean.core.PricePlanVersion.class,
                    PricePlanVersionFactory.instance());

            installCoreBean(ctx,
                    com.redknee.app.crm.bean.ServicePackage.class,
                    com.redknee.app.crm.bean.core.ServicePackage.class,
                    ServicePackageFactory.instance());

            installCoreBean(ctx,
                    com.redknee.app.crm.bean.ServicePackageVersion.class,
                    com.redknee.app.crm.bean.core.ServicePackageVersion.class,
                    ServicePackageVersionFactory.instance());
            
            // Core beans without bean factories
            installCoreBean(ctx,
                    com.redknee.app.crm.configshare.BeanClassMapping.class,
                    com.redknee.app.crm.bean.core.BeanClassMapping.class);
            
            installCoreBean(ctx,
                    com.redknee.app.crm.notification.NotificationThreadPool.class,
                    com.redknee.app.crm.bean.core.NotificationThreadPool.class);
            
            installCoreBean(ctx,
                    com.redknee.app.crm.extension.service.AlcatelSSCServiceExtension.class,
                    com.redknee.app.crm.extension.service.core.AlcatelSSCServiceExtension.class);

            installCoreBean(ctx,
                    com.redknee.app.crm.extension.service.BlackberryServiceExtension.class,
                    com.redknee.app.crm.extension.service.core.BlackberryServiceExtension.class);

            installCoreBean(ctx,
                    com.redknee.app.crm.extension.service.URCSPromotionServiceExtension.class,
                    com.redknee.app.crm.extension.service.core.URCSPromotionServiceExtension.class);
            
            installCoreBean(ctx,
                    com.redknee.app.crm.extension.creditcategory.EarlyRewardCreditCategoryExtension.class,
                    com.redknee.app.crm.extension.creditcategory.core.EarlyRewardCreditCategoryExtension.class);

            installCoreBean(ctx,
                    com.redknee.app.crm.extension.creditcategory.LateFeeCreditCategoryExtension.class,
                    com.redknee.app.crm.extension.creditcategory.core.LateFeeCreditCategoryExtension.class);

            installCoreBean(ctx,
                    com.redknee.app.crm.bean.DefaultSubTypeMsisdnGroup.class,
                    com.redknee.app.crm.bean.core.DefaultSubTypeMsisdnGroup.class);

            installCoreBean(ctx,
                    com.redknee.app.crm.bean.BillCycle.class,
                    com.redknee.app.crm.bean.core.BillCycle.class);
            
            // TT#12091155018 . Providing Billing Message Configuration at Account Type level
            installCoreBean(ctx,
                    com.redknee.app.crm.bean.AccountCategory.class,
                    com.redknee.app.crm.bean.core.AccountCategory.class);
            
            installCoreBean(ctx,
                    com.redknee.app.crm.contract.SubscriptionContractTerm.class,
                    com.redknee.app.crm.bean.core.SubscriptionContractTerm.class);

			installCoreBean(ctx,
					com.redknee.app.crm.bean.AccountCategory.class,
					com.redknee.app.crm.bean.core.AccountCategory.class);
     	 	
            installCoreBean(ctx,
                    com.redknee.app.crm.bean.account.SubscriptionType.class,
                    com.redknee.app.crm.bean.core.SubscriptionType.class);

            installCoreBean(ctx,
                    com.redknee.app.crm.bean.account.SubscriptionClass.class,
                    com.redknee.app.crm.bean.core.SubscriptionClass.class);

            installCoreBean(ctx,
                    com.redknee.app.crm.bean.Service.class,
                    com.redknee.app.crm.bean.core.Service.class);

            installCoreBean(ctx,
                    com.redknee.app.crm.bean.CreditCategory.class,
                    com.redknee.app.crm.bean.core.CreditCategory.class);

            installCoreBean(ctx,
                    com.redknee.app.crm.numbermgn.MsisdnMgmtHistory.class,
                    com.redknee.app.crm.bean.core.MsisdnMgmtHistory.class);

            installCoreBean(ctx,
                    com.redknee.app.crm.bean.calldetail.PrepaidCallingCard.class,
                    com.redknee.app.crm.bean.core.PrepaidCallingCard.class);

            installCoreBean(ctx,
                    com.redknee.app.crm.resource.ResourceDevice.class,
                    com.redknee.app.crm.bean.core.ResourceDevice.class);

            installCoreBean(ctx,
                    com.redknee.app.crm.bundle.BundleCategoryAssociation.class,
                    com.redknee.app.crm.bean.core.BundleCategoryAssociation.class);
        		
    		installStaticCtxCoreBean(ctx,
                    com.redknee.app.crm.bundle.BundleProfile.class,
                    com.redknee.app.crm.bean.core.BundleProfile.class);

            installStaticCtxCoreBean(ctx,
                    com.redknee.app.crm.bundle.BundleFee.class,
                    com.redknee.app.crm.bean.core.BundleFee.class);

            installStaticCtxCoreBean(ctx,
                    com.redknee.app.crm.bean.ServiceFee2.class,
                    com.redknee.app.crm.bean.core.ServiceFee2.class);
        	
            installCoreBean(ctx,
                    com.redknee.app.crm.bean.ChargingComponents.class,
                    com.redknee.app.crm.bean.core.ChargingComponents.class);

            installCoreBean(ctx,
                    com.redknee.app.crm.bean.ChargingComponentsConfig.class,
                    com.redknee.app.crm.bean.core.ChargingComponentsConfig.class);
        }
        catch (Throwable t)
        {
            new CritLogMsg(this, "fail to install AppCrm Bean Factories [" + t.getMessage() + "]", t).log(ctx);
        }
    }

    protected <BASE extends AbstractBean, CORE extends BASE> void installCoreBean(Context ctx, Class<BASE> baseClass, Class<CORE> coreClass)
    {
        installCoreBean(ctx, baseClass, coreClass, null);
    }

    protected <BASE extends AbstractBean, CORE extends BASE> void installCoreBean(Context ctx, Class<BASE> baseClass, Class<CORE> coreClass, ContextFactory factory)
    {
        Context coreCtx;
        for (coreCtx = ctx; 
             coreCtx != null && !"core".equals(coreCtx.getName());
             coreCtx = (Context) coreCtx.get(".."))
        {
            // NOP - for loop does everything
        }
        
        installBeanFactory(ctx, baseClass, coreClass, factory);
        installSafetyNetContextReferences(coreCtx, baseClass, coreClass);
    }

    private <BASE extends AbstractBean, CORE extends BASE> void installBeanFactory(Context ctx, Class<BASE> baseClass, Class<CORE> coreClass, ContextFactory factory)
    {   
        if (factory == null)
        {
            factory = new ConstructorCallingBeanFactory<CORE>(coreClass);
        }
        else
        {
            boolean found = false;
            for (ContextFactory scanner = factory;
                    scanner instanceof ContextFactoryProxy;
                    scanner = ((ContextFactoryProxy)scanner).getDelegate())
            {
                if (scanner instanceof CoreBeanAdaptingContextFactory)
                {
                    found = true;
                    break;
                }
            }
            
            if (!found)
            {
                factory = new CoreBeanAdaptingContextFactory<BASE, CORE>(baseClass, coreClass, factory);
            }
            
            XBeans.putBeanFactory(ctx, coreClass, factory);
        }
        XBeans.putBeanFactory(ctx, baseClass, factory);
    }

    private <BASE extends AbstractBean, CORE extends BASE> void installSafetyNetContextReferences(Context ctx, Class<BASE> baseClass, Class<CORE> coreClass)
    {
        ctx.put(baseClass, new ContextRedirectingContextFactory(coreClass));
        ctx.put(coreClass, new ContextRedirectingContextFactory(baseClass));
    }
    
    protected <BASE extends AbstractBean, CORE extends BASE> void installStaticCtxCoreBean(Context ctx, Class<BASE> baseClass, Class<CORE> coreClass)
    {
    	Context coreCtx;
        for (coreCtx = ctx; 
             coreCtx != null && !"core".equals(coreCtx.getName());
             coreCtx = (Context) coreCtx.get(".."))
        {
            // NOP - for loop does everything
        }
        
        ContextFactory factory = new ConstructorStaticCtxCallingBeanFactory<CORE>(coreClass);
        XBeans.putBeanFactory(ctx, baseClass, factory);
        
        installSafetyNetContextReferences(coreCtx, baseClass, coreClass);
    }
    
}
