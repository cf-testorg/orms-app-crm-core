/**
 * Copyright (c) 2010 Redknee, Inc. and its subsidiaries. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of REDKNEE.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with REDKNEE.
 *
 * REDKNEE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. REDKNEE SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR
 * ITS DERIVATIVES.
 */
 
package com.redknee.app.crm.core.agent;


import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.app.crm.CoreCrmConstants;
import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.ModelCrmLicenseConstants;
import com.redknee.app.crm.bean.AccountCategoryHome;
import com.redknee.app.crm.bean.AccountOverPaymentHistoryHome;
import com.redknee.app.crm.bean.AccountTypeBillingMessage;
import com.redknee.app.crm.bean.AccountTypeBillingMessageID;
import com.redknee.app.crm.bean.AdjustmentTypeHome;
import com.redknee.app.crm.bean.AgedDebtHome;
import com.redknee.app.crm.bean.AlternateInvoiceHome;
import com.redknee.app.crm.bean.AutoDepositReleaseCriteriaHome;
import com.redknee.app.crm.bean.AuxiliaryServiceHome;
import com.redknee.app.crm.bean.BillCycleBillingMessage;
import com.redknee.app.crm.bean.BillCycleBillingMessageID;
import com.redknee.app.crm.bean.BillCycleHistoryHome;
import com.redknee.app.crm.bean.BillCycleHome;
import com.redknee.app.crm.bean.CT23RuleHome;
import com.redknee.app.crm.bean.ChargedBundleInfoHome;
import com.redknee.app.crm.bean.ChargedBundleInfoXDBHome;
import com.redknee.app.crm.bean.CipherKeyHome;
import com.redknee.app.crm.bean.CipherTypeHome;
import com.redknee.app.crm.bean.ContractDescription;
import com.redknee.app.crm.bean.ContractDescriptionID;
import com.redknee.app.crm.bean.CreditCategoryBillingMessage;
import com.redknee.app.crm.bean.CreditCategoryBillingMessageID;
import com.redknee.app.crm.bean.CreditCategoryHome;
import com.redknee.app.crm.bean.CurrencyExchangeRateHistoryHome;
import com.redknee.app.crm.bean.DebtCollectionAgencyHome;
import com.redknee.app.crm.bean.DiscountClassTemplateInfoHome;
import com.redknee.app.crm.bean.EarlyRewardConfigurationHome;
import com.redknee.app.crm.bean.ErIndexToProvinceConfigHome;
import com.redknee.app.crm.bean.GLCodeMappingHome;
import com.redknee.app.crm.bean.GSMPackageHome;
import com.redknee.app.crm.bean.IdentifierSequence;
import com.redknee.app.crm.bean.IdentifierSequenceHome;
import com.redknee.app.crm.bean.KeyConfigurationHome;
import com.redknee.app.crm.bean.KeyValueEntry;
import com.redknee.app.crm.bean.KeyValueEntryHome;
import com.redknee.app.crm.bean.LateFeeConfigurationHome;
import com.redknee.app.crm.bean.LocationZoneToProvinceMappingHome;
import com.redknee.app.crm.bean.LoyaltyCard;
import com.redknee.app.crm.bean.LoyaltyCardHome;
import com.redknee.app.crm.bean.MscToProvinceMappingHome;
import com.redknee.app.crm.bean.MsisdnHome;
import com.redknee.app.crm.bean.MsisdnToProvinceMappingHome;
import com.redknee.app.crm.bean.NoteHome;
import com.redknee.app.crm.bean.NoteTypeHome;
import com.redknee.app.crm.bean.OverPaymentRunHome;
import com.redknee.app.crm.bean.PricePlanBillingMessage;
import com.redknee.app.crm.bean.PricePlanBillingMessageID;
import com.redknee.app.crm.bean.PricePlanHome;
import com.redknee.app.crm.bean.PricePlanVersionHome;
import com.redknee.app.crm.bean.PrivateCug;
import com.redknee.app.crm.bean.PrivateCugHome;
import com.redknee.app.crm.bean.ProvincePrefixHome;
import com.redknee.app.crm.bean.ReasonCodeHome;
import com.redknee.app.crm.bean.SAPDocHeader;
import com.redknee.app.crm.bean.SAPDocHeaderHome;
import com.redknee.app.crm.bean.ServicePackageHome;
import com.redknee.app.crm.bean.ServicePackageVersionHome;
import com.redknee.app.crm.bean.SmscTxConnectionConfigHome;
import com.redknee.app.crm.bean.SpidBillingMessage;
import com.redknee.app.crm.bean.SpidBillingMessageID;
import com.redknee.app.crm.bean.SpidLangHome;
import com.redknee.app.crm.bean.SubscriberCycleBundleUsageHome;
import com.redknee.app.crm.bean.SubscriberCycleUsageHome;
import com.redknee.app.crm.bean.SupplementaryDataHome;
import com.redknee.app.crm.bean.SystemAdjustTypeMappingHome;
import com.redknee.app.crm.bean.SubscriberRechargeRequestHome;
import com.redknee.app.crm.bean.TDMAPackageHome;
import com.redknee.app.crm.bean.TaxComponentTypeHome;
import com.redknee.app.crm.bean.TaxationMethodInfoHome;
import com.redknee.app.crm.bean.TransactionCollectionAgencyHome;
import com.redknee.app.crm.bean.TransactionHome;
import com.redknee.app.crm.bean.TransactionMethodHome;
import com.redknee.app.crm.bean.VSATPackageHome;
import com.redknee.app.crm.bean.VpnAuxiliarySubscriberHome;
import com.redknee.app.crm.bean.VpnAuxiliarySubscriberXDBHome;
import com.redknee.app.crm.bean.account.AccountConversionHistoryHome;
import com.redknee.app.crm.bean.account.SubscriptionClassHome;
import com.redknee.app.crm.bean.account.SubscriptionTypeHome;
import com.redknee.app.crm.bean.bank.BankHome;
import com.redknee.app.crm.bean.calldetail.BillingCategoryHome;
import com.redknee.app.crm.bean.externalapp.ExternalAppErrorCodeMsgHome;
import com.redknee.app.crm.bean.externalapp.ExternalAppResultCodeHome;
import com.redknee.app.crm.bean.payment.PaymentAgentHome;
import com.redknee.app.crm.bean.payment.PaymentPlanHistoryHome;
import com.redknee.app.crm.bean.payment.PaymentPlanHome;
import com.redknee.app.crm.billing.message.BillingMessageHomePipelineFactory;
import com.redknee.app.crm.collection.ProvincePrefixConstants;
import com.redknee.app.crm.configshare.BeanClassMappingHome;
import com.redknee.app.crm.configshare.ConfigChangeRequestHome;
import com.redknee.app.crm.configshare.SharedBeanHome;
import com.redknee.app.crm.configshare.SharedBeanTransientHome;
import com.redknee.app.crm.contract.SubscriptionContractTermHome;
import com.redknee.app.crm.delivery.email.CRMEmailConfigHome;
import com.redknee.app.crm.delivery.email.CRMEmailConfigMsgBoxProxy;
import com.redknee.app.crm.delivery.email.CRMEmailTemplateHome;
import com.redknee.app.crm.delivery.email.EmailRepeatingTemplateHome;
import com.redknee.app.crm.delivery.email.EmailResultCallback;
import com.redknee.app.crm.delivery.email.NullEmailResultCallback;
import com.redknee.app.crm.delivery.email.SMTPDeliveryCallbackMsgBox;
import com.redknee.app.crm.extension.ExtensionInstallationHome;
import com.redknee.app.crm.extension.auxiliaryservice.AddMsisdnAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.CallingGroupAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.DiscountAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.GroupChargingAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.HomeZoneAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.MultiSimAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.NGRCOptInAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.PRBTAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.ProvisionableAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.SPGAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.URCSPromotionAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.VPNAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.VoicemailAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.core.AddMsisdnAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.CallingGroupAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.DiscountAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.GroupChargingAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.HomeZoneAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.MultiSimAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.NGRCOptInAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.PRBTAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.ProvisionableAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.SPGAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.URCSPromotionAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.VPNAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.VoicemailAuxSvcExtension;
import com.redknee.app.crm.extension.creditcategory.EarlyRewardCreditCategoryExtensionHome;
import com.redknee.app.crm.extension.creditcategory.LateFeeCreditCategoryExtensionHome;
import com.redknee.app.crm.extension.creditcategory.NoticeScheduleCreditCategoryExtension;
import com.redknee.app.crm.extension.creditcategory.NoticeScheduleCreditCategoryExtensionHome;
import com.redknee.app.crm.extension.creditcategory.core.EarlyRewardCreditCategoryExtension;
import com.redknee.app.crm.extension.creditcategory.core.LateFeeCreditCategoryExtension;
import com.redknee.app.crm.extension.service.AlcatelSSCServiceExtensionHome;
import com.redknee.app.crm.extension.service.BlackberryServiceExtensionHome;
import com.redknee.app.crm.extension.service.URCSPromotionServiceExtensionHome;
import com.redknee.app.crm.extension.service.core.AlcatelSSCServiceExtension;
import com.redknee.app.crm.extension.service.core.BlackberryServiceExtension;
import com.redknee.app.crm.extension.service.core.URCSPromotionServiceExtension;
import com.redknee.app.crm.extension.spid.NotificationMethodSpidExtension;
import com.redknee.app.crm.extension.spid.NotificationMethodSpidExtensionHome;
import com.redknee.app.crm.extension.spid.SAPReportSpidExtension;
import com.redknee.app.crm.extension.spid.SAPReportSpidExtensionHome;
import com.redknee.app.crm.extension.spid.SAPReportSpidExtensionTransientHome;
import com.redknee.app.crm.home.AccountsDiscountPipelineFactory;
import com.redknee.app.crm.home.AlternateInvoiceHomePipelineFactory;
import com.redknee.app.crm.home.BankHomePipelineFactory;
import com.redknee.app.crm.home.CT23RuleHomePipelineFactory;
import com.redknee.app.crm.home.ConfigChangeRequestHomePipelineFactory;
import com.redknee.app.crm.home.CoreDiscountClassTemplateInfoPipelineFactory;
import com.redknee.app.crm.home.CurrencyExchangeRateHistoryPipelineFactory;
import com.redknee.app.crm.home.EarlyRewardConfigurationHomePipelineFactory;
import com.redknee.app.crm.home.EmailConfigHomePipelineFactory;
import com.redknee.app.crm.home.EmailRepeatingTemplateHomePipelineFactory;
import com.redknee.app.crm.home.EmailTemplateHomePipelineFactory;
import com.redknee.app.crm.home.ExternalAppErrorCodeMsgHomePipelineFactory;
import com.redknee.app.crm.home.ExternalAppResultCodeHomePipelineFactory;
import com.redknee.app.crm.home.KeyConfigurationHomePipelineFactory;
import com.redknee.app.crm.home.LateFeeConfigurationHomePipelineFactory;
import com.redknee.app.crm.home.LiaisonScheduleHomePipelineFactory;
import com.redknee.app.crm.home.NotificationTemplateGroupHomePipelineFactory;
import com.redknee.app.crm.home.NotificationThreadPoolHomePipelineFactory;
import com.redknee.app.crm.home.NotificationTypeScheduleHomePipelineFactory;
import com.redknee.app.crm.home.OverPaymentRunHomePipelineFactory;
import com.redknee.app.crm.home.ScheduledNotificationHomePipelineFactory;
import com.redknee.app.crm.home.TaxationMethodInfoHomePipelineFactory;
import com.redknee.app.crm.home.TemplateGroupGlobalRecordHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreAccountCategoryHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreSubscriberRechargeRequestHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreAccountConversionHistoryHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreAccountNoteHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreAccountOverPaymentHistoryHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreAdjustmentTypeHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreAgedDebtHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreAlcatelSSCServiceExtensionHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreAutoDepositReleaseCriteriaHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreAuxiliaryServiceHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreBeanClassMappingHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreBillCycleHistoryHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreBillCycleHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreBillingCategoryHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreBlackberryServiceExtensionHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreCipherKeyHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreCipherTypeHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreCreditCategoryHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreDebtCollectionAgencyHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreEarlyRewardCreditCategoryExtensionHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreGLCodeHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreGSMPackageHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreLateFeeCreditCategoryExtensionHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreMsisdnHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreMsisdnMgmtHistoryHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreNoteTypeHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreNoticeScheduleCreditCategoryExtensionHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreNotificationMethodSpidExtensionHomePipelineFactory;
import com.redknee.app.crm.home.core.CorePaymentAgentHomePipelineFactory;
import com.redknee.app.crm.home.core.CorePaymentPlanHistoryHomePipelineFactory;
import com.redknee.app.crm.home.core.CorePaymentPlanHomePipelineFactory;
import com.redknee.app.crm.home.core.CorePricePlanHomePipelineFactory;
import com.redknee.app.crm.home.core.CorePricePlanVersionHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreReasonCodeHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreServicePackageHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreServicePackageVersionHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreSmscTxConnectionConfigHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreSubscriberCycleBundleUsageHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreSubscriberCycleUsageHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreSubscriberNoteHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreSubscriptionClassHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreSubscriptionContractTermHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreSubscriptionTypeHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreSupplementaryDataHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreSystemAdjustmentTypeMappingPipeLineFactory;
import com.redknee.app.crm.home.core.CoreTaxComponentTypeHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreTransactionDebtCollectionAgencyHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreTransactionHomePipelineFactory;
import com.redknee.app.crm.home.core.CoreTransactionMethodHomePipelineFactory;
import com.redknee.app.crm.home.core.SpidLangHomePipelineFactory;
import com.redknee.app.crm.home.province.ErIndexToProvinceConfigHomePipelineFactory;
import com.redknee.app.crm.home.province.LocationZoneToProvinceMappingHomePipelineFactory;
import com.redknee.app.crm.home.province.MscToProvinceMappingHomePipelineFactory;
import com.redknee.app.crm.home.province.MsisdnToProvinceMappingHomePipelineFactory;
import com.redknee.app.crm.home.province.ProvincePrefixHomePipelineFactory;
import com.redknee.app.crm.notification.LiaisonScheduleHome;
import com.redknee.app.crm.notification.NotificationThreadPoolHome;
import com.redknee.app.crm.notification.NotificationTypeScheduleHome;
import com.redknee.app.crm.notification.ScheduledNotificationHome;
import com.redknee.app.crm.notification.template.NotificationTemplateGroupHome;
import com.redknee.app.crm.notification.template.TemplateGroupGlobalRecordHome;
import com.redknee.app.crm.numbermgn.MsisdnMgmtHistoryHome;
import com.redknee.app.crm.resource.ResourceDeviceDefaultPackageHome;
import com.redknee.app.crm.resource.ResourceDeviceDefaultPackageHomeFactory;
import com.redknee.app.crm.resource.ResourceDeviceGroupHome;
import com.redknee.app.crm.resource.ResourceDeviceGroupHomeFactory;
import com.redknee.app.crm.resource.ResourceDeviceHome;
import com.redknee.app.crm.resource.ResourceDeviceHomeFactory;
import com.redknee.app.crm.sequenceId.IdentifierSequenceIncrementHome;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;
import com.redknee.app.crm.support.ExtensionSupportHelper;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.xhome.adapter.ExtendedBeanAdapter;
import com.redknee.app.crm.xhome.home.ConfigShareTotalCachingHome;
import com.redknee.app.crm.xhome.home.ContextRedirectingHome;
import com.redknee.app.crm.xhome.home.FailureAuditingJournalHome;
import com.redknee.framework.application.RemoteApplication;
import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.core.platform.Ports;
import com.redknee.framework.license.License;
import com.redknee.framework.license.LicenseHome;
import com.redknee.framework.msg.Email;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgent;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.entity.EntityInfoHome;
import com.redknee.framework.xhome.entity.EntityInfoXInfo;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.home.LRUCachingHome;
import com.redknee.framework.xhome.home.NoSelectAllHome;
import com.redknee.framework.xhome.home.NotifyingHome;
import com.redknee.framework.xhome.home.ReadOnlyHome;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.home.ValidatingHome;
import com.redknee.framework.xhome.journal.JournalConfig;
import com.redknee.framework.xhome.rmi.RMIPropertyHome;
import com.redknee.framework.xlog.log.CritLogMsg;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.util.partitioning.partition.factory.DefaultNodeHomeFactory;

/**
 * This will install core portions of bean pipelines that can be used by all applications.
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class StorageInstall extends CoreSupport implements ContextAgent
{
    public static final String RMI_SERVER_CTX_KEY = "RMI Server Context";

    public static final String CORE_CLUSTER_SUFFIX = ".CoreCluster";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Context ctx) throws AgentException
    {
        Context serverCtx = (Context) ctx.get(StorageInstall.RMI_SERVER_CTX_KEY);
        if (serverCtx == null)
        {
            serverCtx = ctx.createSubContext(StorageInstall.RMI_SERVER_CTX_KEY);
            ctx.put(StorageInstall.RMI_SERVER_CTX_KEY,serverCtx);
        }

        final RemoteApplication basApp = StorageSupportHelper.get(ctx).retrieveRemoteBASAppConfig(ctx);
        final int basRemotePort = basApp!=null? basApp.getBasePort()+ Ports.RMI_OFFSET : CoreCrmConstants.BAS_PORT;
        
        // Disable the partition clustering feature in UtilPartitioning.  When we use it to share configuration, we
        // generally want to send the sharing request to one of the nodes and have it locally clustered from there
        // rather than managing the clustering from the client-side.
        DefaultNodeHomeFactory.disablePartitionClustering(ctx);

        try
        {
            Home rmiPropertyHome = (Home) ctx.get(RMIPropertyHome.class);
            if (!(rmiPropertyHome instanceof HomeProxy) || !((HomeProxy)rmiPropertyHome).hasDecorator(NotifyingHome.class))
            {
                ctx.put(RMIPropertyHome.class, new NotifyingHome(rmiPropertyHome));
            }
            bindBean(ctx, FailureAuditingJournalHome.FAILED_OPERATIONS_AUDIT_JOURNAL_CONFIG, JournalConfig.class);
            FailureAuditingJournalHome.initJournal(ctx);
            
            ctx.put(ConfigChangeRequestHome.class, new ConfigChangeRequestHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(SharedBeanHome.class, new SharedBeanTransientHome(ctx));
            
            // Register FW's license home for config sharing
            Home licenseHome = (Home) ctx.get(LicenseHome.class);
            if (licenseHome != null)
            {
                ConfigChangeRequestSupportHelper.get(ctx).registerHomeForConfigSharing(ctx, licenseHome, License.class);
            }
            
            
            ctx.put(SpidLangHome.class, new SpidLangHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(ProvincePrefixHome.class, new ProvincePrefixHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(MsisdnToProvinceMappingHome.class, new MsisdnToProvinceMappingHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(ProvincePrefixConstants.TOTAL_MSISDN_TO_PROVINCE_MAPPING_HOME, ctx.get(MsisdnToProvinceMappingHome.class));
            
            ctx.put(MscToProvinceMappingHome.class, new MscToProvinceMappingHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(ProvincePrefixConstants.TOTAL_MSC_TO_PROVINCE_MAPPING_HOME, ctx.get(MscToProvinceMappingHome.class));
            
            ctx.put(LocationZoneToProvinceMappingHome.class, new LocationZoneToProvinceMappingHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(ProvincePrefixConstants.TOTAL_LOCATION_TO_PROVINCE_MAPPING_HOME, ctx.get(LocationZoneToProvinceMappingHome.class));
            
            ctx.put(ErIndexToProvinceConfigHome.class, new ErIndexToProvinceConfigHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            // [CW] controlled by BAS
            ctx.put(IdentifierSequenceHome.class,
                    new SortingHome(
                            ctx,
                            new IdentifierSequenceIncrementHome(
                                    StorageSupportHelper.get(ctx).createHome(ctx, IdentifierSequence.class,"IDENTIFIERSEQUENCE"))));

            ctx.put(KeyConfigurationHome.class, new KeyConfigurationHomePipelineFactory().createPipeline(ctx, ctx));
            
            ctx.put(KeyValueEntryHome.class, 
                    new LRUCachingHome(
                            ctx,
                            KeyValueEntry.class, 
                            true,
                            StorageSupportHelper.get(ctx).createHome(ctx, KeyValueEntry.class, "KEYVALUEENTRIES")));


            // Install the Email feature
            ctx.put(CRMEmailConfigHome.class, new EmailConfigHomePipelineFactory().createPipeline(ctx, serverCtx));

            // External app related homes
            ctx.put(ExternalAppResultCodeHome.class, new ExternalAppResultCodeHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(ExternalAppErrorCodeMsgHome.class, new ExternalAppErrorCodeMsgHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            //Install the Email template home.
            ctx.put(EmailRepeatingTemplateHome.class, new EmailRepeatingTemplateHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(CRMEmailTemplateHome.class, new EmailTemplateHomePipelineFactory().createPipeline(ctx, serverCtx));

            ctx.put(EmailResultCallback.class, NullEmailResultCallback.instance());
            ctx.put(Email.APPLICATION_OUTBOX,
                    new CRMEmailConfigMsgBoxProxy(
                            new SMTPDeliveryCallbackMsgBox()));
            
            ctx.put(SmscTxConnectionConfigHome.class, new CoreSmscTxConnectionConfigHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(ScheduledNotificationHome.class, new ScheduledNotificationHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(LiaisonScheduleHome.class, new LiaisonScheduleHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(NotificationTypeScheduleHome.class, new NotificationTypeScheduleHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(NotificationThreadPoolHome.class, new NotificationThreadPoolHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(TemplateGroupGlobalRecordHome.class, new TemplateGroupGlobalRecordHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(NotificationTemplateGroupHome.class, new NotificationTemplateGroupHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(GLCodeMappingHome.class, new CoreGLCodeHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(PaymentAgentHome.class, new CorePaymentAgentHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(ReasonCodeHome.class, new CoreReasonCodeHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(TaxComponentTypeHome.class, new CoreTaxComponentTypeHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(CT23RuleHome.class, new CT23RuleHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(TaxationMethodInfoHome.class, new TaxationMethodInfoHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(NoteTypeHome.class, new CoreNoteTypeHomePipelineFactory().createPipeline(ctx, serverCtx));

            ctx.put(CoreCrmConstants.ACCOUNT_NOTE_HOME,
                    new CoreAccountNoteHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(CoreCrmConstants.SUBSCRIBER_NOTE_HOME,
                    new CoreSubscriberNoteHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(NoteHome.class, new ContextRedirectingHome(ctx, CoreCrmConstants.SUBSCRIBER_NOTE_HOME));
            
			ctx.put(AccountCategoryHome.class,
			    new CoreAccountCategoryHomePipelineFactory().createPipeline(
			        ctx, serverCtx));
			
			ctx.put(SubscriberRechargeRequestHome.class,
	                new CoreSubscriberRechargeRequestHomePipelineFactory().createPipeline(
	                    ctx, serverCtx));
			
			//-----
			ctx.put(SubscriptionContractTermHome.class,
	                new CoreSubscriptionContractTermHomePipelineFactory().createPipeline(
	                    ctx, serverCtx));

			ctx.put(SupplementaryDataHome.class,
                    new CoreSupplementaryDataHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(AgedDebtHome.class, new CoreAgedDebtHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(SubscriptionTypeHome.class, new CoreSubscriptionTypeHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(SubscriptionClassHome.class, new CoreSubscriptionClassHomePipelineFactory().createPipeline(ctx, serverCtx));

            ctx.put(SAPDocHeaderHome.class, CoreSupport.bindHome(ctx, SAPDocHeader.class)); 
            ctx.put(AdjustmentTypeHome.class, new CoreAdjustmentTypeHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(SystemAdjustTypeMappingHome.class, new CoreSystemAdjustmentTypeMappingPipeLineFactory().createPipeline(ctx, serverCtx));

            ctx.put(CurrencyExchangeRateHistoryHome.class, new CurrencyExchangeRateHistoryPipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(BillCycleHome.class, new CoreBillCycleHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(AccountConversionHistoryHome.class, new CoreAccountConversionHistoryHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(BillCycleHistoryHome.class, new CoreBillCycleHistoryHomePipelineFactory().createPipeline(ctx, serverCtx));

            ctx.put(CreditCategoryHome.class, new CoreCreditCategoryHomePipelineFactory().createPipeline(ctx, serverCtx));

            ctx.put(PaymentPlanHome.class, new CorePaymentPlanHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(PaymentPlanHistoryHome.class, new CorePaymentPlanHistoryHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(AutoDepositReleaseCriteriaHome.class, new CoreAutoDepositReleaseCriteriaHomePipelineFactory().createPipeline(ctx, serverCtx));

            ctx.put(GSMPackageHome.class, new CoreGSMPackageHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(TDMAPackageHome.class, new CoreGSMPackageHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(VSATPackageHome.class, new CoreGSMPackageHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(MsisdnHome.class, new CoreMsisdnHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(MsisdnMgmtHistoryHome.class, new CoreMsisdnMgmtHistoryHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(ResourceDeviceGroupHome.class, new ResourceDeviceGroupHomeFactory().createPipeline(ctx, serverCtx));

            ctx.put(ResourceDeviceHome.class, new ResourceDeviceHomeFactory().createPipeline(ctx, serverCtx));

            ctx.put(ResourceDeviceDefaultPackageHome.class, new ResourceDeviceDefaultPackageHomeFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(BillingCategoryHome.class, new CoreBillingCategoryHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(ServicePackageVersionHome.class, new CoreServicePackageVersionHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(ServicePackageHome.class, new CoreServicePackageHomePipelineFactory().createPipeline(ctx, serverCtx));

            ctx.put(PricePlanHome.class, new CorePricePlanHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(DiscountClassTemplateInfoHome.class, new CoreDiscountClassTemplateInfoPipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(PricePlanVersionHome.class, new CorePricePlanVersionHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            Home transactionHome = new CoreTransactionHomePipelineFactory().createPipeline(ctx, serverCtx);
            ctx.put(TransactionHome.class, transactionHome);
            /*
             * [2008-04-04] Cindy Wong: the full home is put in context so that payment
             * plan loan reversal can be properly created when an account on payment plan
             * is dunned when making a payment.
             */
            ctx.put(CoreCrmConstants.FULL_TRANSACTION_HOME, transactionHome);
            
            ctx.put(TransactionMethodHome.class, new CoreTransactionMethodHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(DebtCollectionAgencyHome.class, new CoreDebtCollectionAgencyHomePipelineFactory().createPipeline(ctx,serverCtx));

            ctx.put(TransactionCollectionAgencyHome.class, new CoreTransactionDebtCollectionAgencyHomePipelineFactory().createPipeline(ctx,serverCtx));

            ctx.put(AuxiliaryServiceHome.class, new CoreAuxiliaryServiceHomePipelineFactory().createPipeline(ctx, serverCtx));

            ctx.put(VpnAuxiliarySubscriberHome.class,
                    new NoSelectAllHome(
                            new VpnAuxiliarySubscriberXDBHome(ctx, "VPNAUXILIARYINFO")));
            
            ctx.put(BeanClassMappingHome.class, new CoreBeanClassMappingHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(SubscriberCycleUsageHome.class, new CoreSubscriberCycleUsageHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(SubscriberCycleBundleUsageHome.class, new CoreSubscriberCycleBundleUsageHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(LateFeeConfigurationHome.class, new LateFeeConfigurationHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(EarlyRewardConfigurationHome.class, new EarlyRewardConfigurationHomePipelineFactory().createPipeline(ctx, serverCtx));

            ctx.put(CipherKeyHome.class, new CoreCipherKeyHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(CipherTypeHome.class, new CoreCipherTypeHomePipelineFactory().createPipeline(ctx, serverCtx));
			ctx.put(BankHome.class,
			    new BankHomePipelineFactory().createPipeline(ctx, serverCtx));
			
			ctx.put(com.redknee.app.crm.bundle.BundleProfileXDBHome.class, new com.redknee.app.crm.bundle.BundleProfileXDBHome(serverCtx, "BundleProfile"));
			
            
            ctx.put(PrivateCugHome.class,
                    StorageSupportHelper.get(ctx).createHome(ctx,PrivateCug.class, "PrivateCUG"));
            
            installSpidExtensions(ctx, serverCtx);
            installServiceExtensions(ctx, serverCtx);
            installAuxiliaryServiceExtensions(ctx, serverCtx);
            installCreditCategoryExtensions(ctx, serverCtx);

            //Billing Message Multi-language Support for Service Provider, Bill Cycle, Credit Category, Account Type, Price Plan, Contract term
            new BillingMessageHomePipelineFactory<SpidBillingMessage, SpidBillingMessageID>(SpidBillingMessage.class, SpidBillingMessageID.class, "BILLMSG_SPID",
                    new SpidBillingMessageID(SpidBillingMessage.DEFAULT_SPID, SpidBillingMessage.DEFAULT_LANGUAGE, SpidBillingMessage.DEFAULT_IDENTIFIER))
                    .createPipeline(ctx, serverCtx);
            new BillingMessageHomePipelineFactory<BillCycleBillingMessage, BillCycleBillingMessageID>(BillCycleBillingMessage.class, BillCycleBillingMessageID.class, "BILLMSG_BILLCYCLE",
                    new BillCycleBillingMessageID(BillCycleBillingMessage.DEFAULT_SPID, BillCycleBillingMessage.DEFAULT_LANGUAGE, BillCycleBillingMessage.DEFAULT_IDENTIFIER))
                    .createPipeline(ctx, serverCtx);
            new BillingMessageHomePipelineFactory<CreditCategoryBillingMessage, CreditCategoryBillingMessageID>(CreditCategoryBillingMessage.class, CreditCategoryBillingMessageID.class, "BILLMSG_CREDIT_CATEGORY",
                    new CreditCategoryBillingMessageID(CreditCategoryBillingMessage.DEFAULT_SPID, CreditCategoryBillingMessage.DEFAULT_LANGUAGE, CreditCategoryBillingMessage.DEFAULT_IDENTIFIER))
                    .createPipeline(ctx, serverCtx);
            new BillingMessageHomePipelineFactory<PricePlanBillingMessage, PricePlanBillingMessageID>(PricePlanBillingMessage.class, PricePlanBillingMessageID.class, "BILLMSG_PRICE_PLAN",
                    new PricePlanBillingMessageID(PricePlanBillingMessage.DEFAULT_SPID, PricePlanBillingMessage.DEFAULT_LANGUAGE, PricePlanBillingMessage.DEFAULT_IDENTIFIER))
                    .createPipeline(ctx, serverCtx);
           
            //TT#12091155018 . Providing Billing Message Configuration at Account Type level
            new BillingMessageHomePipelineFactory<AccountTypeBillingMessage, AccountTypeBillingMessageID>(
					AccountTypeBillingMessage.class, AccountTypeBillingMessageID.class,"BILLMSG_ACCOUNT_TYPE",
					new AccountTypeBillingMessageID(
							AccountTypeBillingMessage.DEFAULT_SPID,
							AccountTypeBillingMessage.DEFAULT_LANGUAGE,
							AccountTypeBillingMessage.DEFAULT_IDENTIFIER))
							.createPipeline(ctx, serverCtx);
            
            new BillingMessageHomePipelineFactory<ContractDescription, ContractDescriptionID>(
            		ContractDescription.class,ContractDescriptionID.class, "BILLMSG_CONTRACT_DESCRIPTION",
                    new ContractDescriptionID(
                    		ContractDescription.DEFAULT_SPID,
                    		ContractDescription.DEFAULT_LANGUAGE,
                    		ContractDescription.DEFAULT_IDENTIFIER))
                    		.createPipeline(ctx, serverCtx);
        
            ctx.put(LoyaltyCardHome.class,
                       // new ReadOnlyHome(ctx, 
                                StorageSupportHelper.get(ctx).createHome(ctx, LoyaltyCard.class,"LOYALTYCARD"));
            
            ctx.put(AlternateInvoiceHome.class, new AlternateInvoiceHomePipelineFactory().createPipeline(ctx, serverCtx));
            
            ctx.put(ChargedBundleInfoHome.class, new ReadOnlyHome(ctx, new ChargedBundleInfoXDBHome(ctx, "ChargedBundleInfo")));
            
            //TCB_F-0001339 OverPayment handling , store pipeline for AccountOverPaymentHistoryHome
            ctx.put(AccountOverPaymentHistoryHome.class, new CoreAccountOverPaymentHistoryHomePipelineFactory().createPipeline(ctx, serverCtx));
            ctx.put(OverPaymentRunHome.class, new OverPaymentRunHomePipelineFactory().createPipeline(ctx, serverCtx));
            new AccountsDiscountPipelineFactory().createPipeline(ctx, serverCtx);
        }
        catch (final Throwable t)
        {
            new CritLogMsg(this, "fail to install", t).log(ctx);
            throw new AgentException("Fail to complete StorageInstall", t);
        }
    }

    private void installSpidExtensions(Context ctx, Context serverCtx) throws RemoteException, IOException, AgentException
    {
        try
        {
            Home entityInfoHome = (Home)ctx.get(EntityInfoHome.class);
            if (entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, NotificationMethodSpidExtension.class.getName())) != null)
            {
                ctx.put(NotificationMethodSpidExtensionHome.class, new CoreNotificationMethodSpidExtensionHomePipelineFactory().createPipeline(ctx, serverCtx));
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, NotificationMethodSpidExtension.class, NotificationMethodSpidExtension.class.getName());
            }
            
            
            if (entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, SAPReportSpidExtension.class.getName())) != null)
            {
                Home home = StorageSupportHelper.get(ctx).createHome(ctx, SAPReportSpidExtension.class, "SAPReportSpidExtension");
                home = new ConfigShareTotalCachingHome( ctx, new SAPReportSpidExtensionTransientHome(ctx), home); 
                ctx.put(SAPReportSpidExtensionHome.class, home);
 
                ExtensionSupportHelper.get(ctx).registerExtension(ctx,
                        SAPReportSpidExtension.class, ModelCrmLicenseConstants.SAP_REPORT
                        );

            }


        }
        catch( HomeException he )
        {
            new MajorLogMsg(this, "Failed to install one or more service extensions." , he).log(ctx);
        }
    }

    private void installServiceExtensions(Context ctx, Context serverCtx) throws RemoteException, AgentException, IOException
    {
        try
        {
            Home entityInfoHome = (Home)ctx.get(EntityInfoHome.class);
            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, AlcatelSSCServiceExtension.class.getName())) != null )
            {
                ctx.put(AlcatelSSCServiceExtensionHome.class, new CoreAlcatelSSCServiceExtensionHomePipelineFactory().createPipeline(ctx, serverCtx));
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, AlcatelSSCServiceExtension.class, CoreCrmLicenseConstants.ALCATEL_LICENSE);
            }

            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, BlackberryServiceExtension.class.getName())) != null )
            {
                ctx.put(BlackberryServiceExtensionHome.class, new CoreBlackberryServiceExtensionHomePipelineFactory().createPipeline(ctx, serverCtx));
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, BlackberryServiceExtension.class, CoreCrmLicenseConstants.BLACKBERRY_LICENSE);
            }
            
            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, URCSPromotionServiceExtension.class.getName())) != null )
            {
                ctx.put(URCSPromotionServiceExtensionHome.class, new com.redknee.app.crm.home.core.CoreURCSPromotionServiceExtensionHomePipelineFactory().createPipeline(ctx, serverCtx));
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, URCSPromotionServiceExtension.class);
            }

        }
        catch( HomeException he )
        {
            new MajorLogMsg(this, "Failed to install one or more service extensions." , he).log(ctx);
        }
    }
    
    private void installAuxiliaryServiceExtensions(final Context ctx, final Context serverCtx) throws RemoteException
    {
        try
        {
            // TODO: Handle BAS/ECare deployments
            final Home entityInfoHome = (Home)ctx.get(EntityInfoHome.class);

            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, AddMsisdnAuxSvcExtension.class.getName())) != null )
            {
                ctx.put(AddMsisdnAuxSvcExtensionHome.class,
                        new NoSelectAllHome(
                                new ValidatingHome(
                                        new ExtensionInstallationHome(ctx,
                                                new AdapterHome(ctx, 
                                                        new ExtendedBeanAdapter<com.redknee.app.crm.extension.auxiliaryservice.AddMsisdnAuxSvcExtension, com.redknee.app.crm.extension.auxiliaryservice.core.AddMsisdnAuxSvcExtension>(
                                                                com.redknee.app.crm.extension.auxiliaryservice.AddMsisdnAuxSvcExtension.class, 
                                                                com.redknee.app.crm.extension.auxiliaryservice.core.AddMsisdnAuxSvcExtension.class), 
                                                new LRUCachingHome(
                                                        ctx,
                                                        AddMsisdnAuxSvcExtension.class, 
                                                        true,
                                                        StorageSupportHelper.get(ctx).createHome(ctx, AddMsisdnAuxSvcExtension.class, "AUXSVCEXTADDMSISDN")))))));
                
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, AddMsisdnAuxSvcExtension.class, CoreCrmLicenseConstants.ADDITIONAL_MOBILE_NUMBER);
            }

            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, MultiSimAuxSvcExtension.class.getName())) != null )
            {
                ctx.put(MultiSimAuxSvcExtensionHome.class,
                        new NoSelectAllHome(
                                new ValidatingHome(
                                        new ExtensionInstallationHome(ctx,
                                                new AdapterHome(ctx, 
                                                        new ExtendedBeanAdapter<com.redknee.app.crm.extension.auxiliaryservice.MultiSimAuxSvcExtension, com.redknee.app.crm.extension.auxiliaryservice.core.MultiSimAuxSvcExtension>(
                                                                com.redknee.app.crm.extension.auxiliaryservice.MultiSimAuxSvcExtension.class, 
                                                                com.redknee.app.crm.extension.auxiliaryservice.core.MultiSimAuxSvcExtension.class), 
                                                new LRUCachingHome(
                                                        ctx,
                                                        MultiSimAuxSvcExtension.class, 
                                                        true,
                                                        StorageSupportHelper.get(ctx).createHome(ctx, MultiSimAuxSvcExtension.class, "AUXSVCEXTMULTISIM")))))));
                
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, MultiSimAuxSvcExtension.class, CoreCrmLicenseConstants.MULTI_SIM_LICENSE);
            }

            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, NGRCOptInAuxSvcExtension.class.getName())) != null )
            {
                ctx.put(NGRCOptInAuxSvcExtensionHome.class,
                        new NoSelectAllHome(
                                new ValidatingHome(
                                        new ExtensionInstallationHome(ctx,
                                                new AdapterHome(ctx, 
                                                        new ExtendedBeanAdapter<com.redknee.app.crm.extension.auxiliaryservice.NGRCOptInAuxSvcExtension, com.redknee.app.crm.extension.auxiliaryservice.core.NGRCOptInAuxSvcExtension>(
                                                                com.redknee.app.crm.extension.auxiliaryservice.NGRCOptInAuxSvcExtension.class, 
                                                                com.redknee.app.crm.extension.auxiliaryservice.core.NGRCOptInAuxSvcExtension.class), 
                                                new LRUCachingHome(
                                                        ctx,
                                                        NGRCOptInAuxSvcExtension.class, 
                                                        true,
                                                        StorageSupportHelper.get(ctx).createHome(ctx, NGRCOptInAuxSvcExtension.class, "AUXSVCEXTNGRCOPTIN")))))));
                
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, NGRCOptInAuxSvcExtension.class);
            }

            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, CallingGroupAuxSvcExtension.class.getName())) != null )
            {
                ctx.put(CallingGroupAuxSvcExtensionHome.class,
                        new NoSelectAllHome(
                                new ValidatingHome(
                                        new ExtensionInstallationHome(ctx,
                                                new AdapterHome(ctx, 
                                                        new ExtendedBeanAdapter<com.redknee.app.crm.extension.auxiliaryservice.CallingGroupAuxSvcExtension, com.redknee.app.crm.extension.auxiliaryservice.core.CallingGroupAuxSvcExtension>(
                                                                com.redknee.app.crm.extension.auxiliaryservice.CallingGroupAuxSvcExtension.class, 
                                                                com.redknee.app.crm.extension.auxiliaryservice.core.CallingGroupAuxSvcExtension.class), 
                                                new LRUCachingHome(
                                                        ctx,
                                                        CallingGroupAuxSvcExtension.class, 
                                                        true,
                                                        StorageSupportHelper.get(ctx).createHome(ctx, CallingGroupAuxSvcExtension.class, "AUXSVCEXTCALLINGGROUP")))))));
                
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, CallingGroupAuxSvcExtension.class);
            }

            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, DiscountAuxSvcExtension.class.getName())) != null )
            {
                ctx.put(DiscountAuxSvcExtensionHome.class,
                        new NoSelectAllHome(
                                new ValidatingHome(
                                        new ExtensionInstallationHome(ctx,
                                                new AdapterHome(ctx, 
                                                        new ExtendedBeanAdapter<com.redknee.app.crm.extension.auxiliaryservice.DiscountAuxSvcExtension, com.redknee.app.crm.extension.auxiliaryservice.core.DiscountAuxSvcExtension>(
                                                                com.redknee.app.crm.extension.auxiliaryservice.DiscountAuxSvcExtension.class, 
                                                                com.redknee.app.crm.extension.auxiliaryservice.core.DiscountAuxSvcExtension.class), 
                                                new LRUCachingHome(
                                                        ctx,
                                                        DiscountAuxSvcExtension.class, 
                                                        true,
                                                        StorageSupportHelper.get(ctx).createHome(ctx, DiscountAuxSvcExtension.class, "AUXSVCEXTDISCOUNT")))))));
                
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, DiscountAuxSvcExtension.class);
            }

            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, HomeZoneAuxSvcExtension.class.getName())) != null )
            {
                ctx.put(HomeZoneAuxSvcExtensionHome.class,
                        new ReadOnlyHome(
                        new NoSelectAllHome(
                                new AdapterHome(ctx, 
                                        new ExtendedBeanAdapter<com.redknee.app.crm.extension.auxiliaryservice.HomeZoneAuxSvcExtension, com.redknee.app.crm.extension.auxiliaryservice.core.HomeZoneAuxSvcExtension>(
                                                com.redknee.app.crm.extension.auxiliaryservice.HomeZoneAuxSvcExtension.class, 
                                                com.redknee.app.crm.extension.auxiliaryservice.core.HomeZoneAuxSvcExtension.class), 
                                new LRUCachingHome(
                                        ctx,
                                        HomeZoneAuxSvcExtension.class, 
                                        true,
                                        new AuditJournalHome(ctx,
                                        StorageSupportHelper.get(ctx).createHome(ctx, HomeZoneAuxSvcExtension.class, "AUXSVCEXTHOMEZONE")))))));
                
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, HomeZoneAuxSvcExtension.class);
            }

            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, PRBTAuxSvcExtension.class.getName())) != null )
            {
                ctx.put(PRBTAuxSvcExtensionHome.class,
                        new NoSelectAllHome(
                                new ValidatingHome(
                                        new ExtensionInstallationHome(ctx,
                                                new AdapterHome(ctx, 
                                                        new ExtendedBeanAdapter<com.redknee.app.crm.extension.auxiliaryservice.PRBTAuxSvcExtension, com.redknee.app.crm.extension.auxiliaryservice.core.PRBTAuxSvcExtension>(
                                                                com.redknee.app.crm.extension.auxiliaryservice.PRBTAuxSvcExtension.class, 
                                                                com.redknee.app.crm.extension.auxiliaryservice.core.PRBTAuxSvcExtension.class), 
                                                new LRUCachingHome(
                                                        ctx,
                                                        PRBTAuxSvcExtension.class, 
                                                        true,
                                                        StorageSupportHelper.get(ctx).createHome(ctx, PRBTAuxSvcExtension.class, "AUXSVCEXTPRBT")))))));
                
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, PRBTAuxSvcExtension.class);
            }
            
            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, ProvisionableAuxSvcExtension.class.getName())) != null )
            {
                ctx.put(ProvisionableAuxSvcExtensionHome.class,
                        new NoSelectAllHome(
                                new ValidatingHome(
                                        new ExtensionInstallationHome(ctx,
                                                new AdapterHome(ctx, 
                                                        new ExtendedBeanAdapter<com.redknee.app.crm.extension.auxiliaryservice.ProvisionableAuxSvcExtension, com.redknee.app.crm.extension.auxiliaryservice.core.ProvisionableAuxSvcExtension>(
                                                                com.redknee.app.crm.extension.auxiliaryservice.ProvisionableAuxSvcExtension.class, 
                                                                com.redknee.app.crm.extension.auxiliaryservice.core.ProvisionableAuxSvcExtension.class), 
                                                new LRUCachingHome(
                                                        ctx,
                                                        ProvisionableAuxSvcExtension.class, 
                                                        true,
                                                        StorageSupportHelper.get(ctx).createHome(ctx, ProvisionableAuxSvcExtension.class, "AUXSVCEXTPROVISIONABLE")))))));

                ExtensionSupportHelper.get(ctx).registerExtension(ctx, ProvisionableAuxSvcExtension.class);
            }

            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, SPGAuxSvcExtension.class.getName())) != null )
            {
                ctx.put(SPGAuxSvcExtensionHome.class,
                        new NoSelectAllHome(
                                new ValidatingHome(
                                        new ExtensionInstallationHome(ctx,
                                                new AdapterHome(ctx, 
                                                        new ExtendedBeanAdapter<com.redknee.app.crm.extension.auxiliaryservice.SPGAuxSvcExtension, com.redknee.app.crm.extension.auxiliaryservice.core.SPGAuxSvcExtension>(
                                                                com.redknee.app.crm.extension.auxiliaryservice.SPGAuxSvcExtension.class, 
                                                                com.redknee.app.crm.extension.auxiliaryservice.core.SPGAuxSvcExtension.class), 
                                                new LRUCachingHome(
                                                        ctx,
                                                        SPGAuxSvcExtension.class, 
                                                        true,
                                                        StorageSupportHelper.get(ctx).createHome(ctx, SPGAuxSvcExtension.class, "AUXSVCEXTSPG")))))));
                
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, SPGAuxSvcExtension.class);
            }

            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, VPNAuxSvcExtension.class.getName())) != null )
            {
                ctx.put(VPNAuxSvcExtensionHome.class,
                        new NoSelectAllHome(
                                new ValidatingHome(
                                        new ExtensionInstallationHome(ctx,
                                                new AdapterHome(ctx, 
                                                        new ExtendedBeanAdapter<com.redknee.app.crm.extension.auxiliaryservice.VPNAuxSvcExtension, com.redknee.app.crm.extension.auxiliaryservice.core.VPNAuxSvcExtension>(
                                                                com.redknee.app.crm.extension.auxiliaryservice.VPNAuxSvcExtension.class, 
                                                                com.redknee.app.crm.extension.auxiliaryservice.core.VPNAuxSvcExtension.class), 
                                                new LRUCachingHome(
                                                        ctx,
                                                        VPNAuxSvcExtension.class, 
                                                        true,
                                                        StorageSupportHelper.get(ctx).createHome(ctx, VPNAuxSvcExtension.class, "AUXSVCEXTVPN")))))));
                
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, VPNAuxSvcExtension.class);
            }

            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, GroupChargingAuxSvcExtension.class.getName())) != null )
            {
                ctx.put(GroupChargingAuxSvcExtensionHome.class,
                        new NoSelectAllHome(
                                new ValidatingHome(
                                        new ExtensionInstallationHome(ctx,
                                                new AdapterHome(ctx, 
                                                        new ExtendedBeanAdapter<com.redknee.app.crm.extension.auxiliaryservice.GroupChargingAuxSvcExtension, com.redknee.app.crm.extension.auxiliaryservice.core.GroupChargingAuxSvcExtension>(
                                                                com.redknee.app.crm.extension.auxiliaryservice.GroupChargingAuxSvcExtension.class, 
                                                                com.redknee.app.crm.extension.auxiliaryservice.core.GroupChargingAuxSvcExtension.class), 
                                                new LRUCachingHome(
                                                        ctx,
                                                        GroupChargingAuxSvcExtensionHome.class, 
                                                        true,
                                                        StorageSupportHelper.get(ctx).createHome(ctx, GroupChargingAuxSvcExtension.class, "AUXSVCEXTGROUPCHARGING")))))));
                
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, GroupChargingAuxSvcExtension.class);
            }

            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, VoicemailAuxSvcExtension.class.getName())) != null )
            {
                ctx.put(VoicemailAuxSvcExtensionHome.class,
                        new NoSelectAllHome(
                                new ValidatingHome(
                                        new ExtensionInstallationHome(ctx,
                                                new AdapterHome(ctx, 
                                                        new ExtendedBeanAdapter<com.redknee.app.crm.extension.auxiliaryservice.VoicemailAuxSvcExtension, com.redknee.app.crm.extension.auxiliaryservice.core.VoicemailAuxSvcExtension>(
                                                                com.redknee.app.crm.extension.auxiliaryservice.VoicemailAuxSvcExtension.class, 
                                                                com.redknee.app.crm.extension.auxiliaryservice.core.VoicemailAuxSvcExtension.class), 
                                                new LRUCachingHome(
                                                        ctx,
                                                        VoicemailAuxSvcExtension.class, 
                                                        true,
                                                        new AuditJournalHome(ctx,
                                                        StorageSupportHelper.get(ctx).createHome(ctx, VoicemailAuxSvcExtension.class, "AUXSVCEXTVOICEMAIL"))))))));
                
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, VoicemailAuxSvcExtension.class);
            }
            
            

            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, URCSPromotionAuxSvcExtension.class.getName())) != null )
            {
                ctx.put(URCSPromotionAuxSvcExtensionHome.class,
                        new NoSelectAllHome(
                                new ValidatingHome(
                                        new ExtensionInstallationHome(ctx,
                                                new AdapterHome(ctx, 
                                                        new ExtendedBeanAdapter<com.redknee.app.crm.extension.auxiliaryservice.URCSPromotionAuxSvcExtension, com.redknee.app.crm.extension.auxiliaryservice.core.URCSPromotionAuxSvcExtension>(
                                                                com.redknee.app.crm.extension.auxiliaryservice.URCSPromotionAuxSvcExtension.class, 
                                                                com.redknee.app.crm.extension.auxiliaryservice.core.URCSPromotionAuxSvcExtension.class), 
                                                new LRUCachingHome(
                                                        ctx,
                                                        URCSPromotionAuxSvcExtension.class, 
                                                        true,
                                                        StorageSupportHelper.get(ctx).createHome(ctx, URCSPromotionAuxSvcExtension.class, "AUXSVCEXTURCSPROMOTION")))))));
                
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, URCSPromotionAuxSvcExtension.class);
            }
            
            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, com.redknee.app.crm.extension.auxiliaryservice.core.PromOptOutAuxSvcExtension.class.getName())) != null )
            {
                ctx.put(com.redknee.app.crm.extension.auxiliaryservice.PromOptOutAuxSvcExtensionHome.class,
                        new NoSelectAllHome(
                                new ValidatingHome(
                                        new ExtensionInstallationHome(ctx,
                                                new AdapterHome(ctx, 
                                                        new ExtendedBeanAdapter<com.redknee.app.crm.extension.auxiliaryservice.PromOptOutAuxSvcExtension,
                                                        com.redknee.app.crm.extension.auxiliaryservice.core.PromOptOutAuxSvcExtension>(
                                                                com.redknee.app.crm.extension.auxiliaryservice.PromOptOutAuxSvcExtension.class, 
                                                                com.redknee.app.crm.extension.auxiliaryservice.core.PromOptOutAuxSvcExtension.class), 
                                                new LRUCachingHome(
                                                        ctx,
                                                        com.redknee.app.crm.extension.auxiliaryservice.core.PromOptOutAuxSvcExtension.class, 
                                                        true,
                                                        StorageSupportHelper.get(ctx).createHome(ctx, com.redknee.app.crm.extension.auxiliaryservice.PromOptOutAuxSvcExtension.class, "AUXSVCEXTPROMOTIONOPTOUT")))))));
                
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, com.redknee.app.crm.extension.auxiliaryservice.core.PromOptOutAuxSvcExtension.class);
            }
            
            if(entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, com.redknee.app.crm.extension.auxiliaryservice.core.TFAAuxSvcExtension.class.getName())) != null )
            {
            	 ctx.put(com.redknee.app.crm.extension.auxiliaryservice.TFAAuxSvcExtensionHome.class,
                         new NoSelectAllHome(
                                 new ValidatingHome(
                                         new ExtensionInstallationHome(ctx,
                                                 new AdapterHome(ctx, 
                                                         new ExtendedBeanAdapter<com.redknee.app.crm.extension.auxiliaryservice.TFAAuxSvcExtension,
                                                         com.redknee.app.crm.extension.auxiliaryservice.core.TFAAuxSvcExtension>(
                                                                 com.redknee.app.crm.extension.auxiliaryservice.TFAAuxSvcExtension.class, 
                                                                 com.redknee.app.crm.extension.auxiliaryservice.core.TFAAuxSvcExtension.class), 
                                                 new LRUCachingHome(
                                                         ctx,
                                                         com.redknee.app.crm.extension.auxiliaryservice.core.TFAAuxSvcExtension.class, 
                                                         true,
                                                         StorageSupportHelper.get(ctx).createHome(ctx, com.redknee.app.crm.extension.auxiliaryservice.TFAAuxSvcExtension.class, "AUXSVCEXTTFA"))))))); 
            	 ExtensionSupportHelper.get(ctx).registerExtension(ctx, com.redknee.app.crm.extension.auxiliaryservice.core.TFAAuxSvcExtension.class);
            }
            
            
        }
        catch( final HomeException he )
        {
            new MajorLogMsg(this, "Failed to install one or more auxiliary service extensions." , he).log(ctx);
        }
    }

    


    private void installCreditCategoryExtensions(Context ctx, Context serverCtx) throws RemoteException, AgentException, IOException
    {
        try
        {
            // TODO: Handle BAS/ECare deployments
            Home entityInfoHome = (Home)ctx.get(EntityInfoHome.class);
            if( entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, EarlyRewardCreditCategoryExtension.class.getName())) != null )
            {
                ctx.put(EarlyRewardCreditCategoryExtensionHome.class, new CoreEarlyRewardCreditCategoryExtensionHomePipelineFactory().createPipeline(ctx, serverCtx));
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, EarlyRewardCreditCategoryExtension.class, CoreCrmLicenseConstants.EARLY_REWARD_LICENSE);
            }
            
            if (entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, LateFeeCreditCategoryExtension.class.getName())) != null)
            {
                ctx.put(LateFeeCreditCategoryExtensionHome.class, new CoreLateFeeCreditCategoryExtensionHomePipelineFactory().createPipeline(ctx, serverCtx));
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, LateFeeCreditCategoryExtension.class, CoreCrmLicenseConstants.LATE_FEE_LICENSE);
            }
            
            if (entityInfoHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, NoticeScheduleCreditCategoryExtension.class.getName())) != null)
            {
                ctx.put(NoticeScheduleCreditCategoryExtensionHome.class, new CoreNoticeScheduleCreditCategoryExtensionHomePipelineFactory().createPipeline(ctx, serverCtx));
                ExtensionSupportHelper.get(ctx).registerExtension(ctx, NoticeScheduleCreditCategoryExtension.class);
            }
        }
        catch( HomeException he )
        {
            new MajorLogMsg(this, "Failed to install one or more account extensions." , he).log(ctx);
        }
    }
}
