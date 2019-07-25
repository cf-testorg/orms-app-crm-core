package com.redknee.app.crm.core.agent;

import com.redknee.app.crm.bean.AccountCategory;
import com.redknee.framework.xhome.beans.FacetMgr;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgent;
import com.redknee.framework.xhome.msp.SetSpidProxyWebControl;
import com.redknee.framework.xhome.webcontrol.TableWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.AdjustmentInfo;
import com.redknee.app.crm.bean.AdjustmentType;
import com.redknee.app.crm.bean.AutoDepositReleaseCriteria;
import com.redknee.app.crm.bean.AuxiliaryService;
import com.redknee.app.crm.bean.BillCycle;
import com.redknee.app.crm.bean.BillingOptionMapping;
import com.redknee.app.crm.bean.CallType;
import com.redknee.app.crm.bean.CreditCategory;
import com.redknee.app.crm.bean.CreditNotificationEvent;
import com.redknee.app.crm.bean.EarlyRewardConfiguration;
import com.redknee.app.crm.bean.GSMPackage;
import com.redknee.app.crm.bean.KeyConfiguration;
import com.redknee.app.crm.bean.LateFeeConfiguration;
import com.redknee.app.crm.bean.Msisdn;
import com.redknee.app.crm.bean.MsisdnGroup;
import com.redknee.app.crm.bean.Note;
import com.redknee.app.crm.bean.PackageGroup;
import com.redknee.app.crm.bean.PricePlan;
import com.redknee.app.crm.bean.PricePlanVersion;
import com.redknee.app.crm.bean.ProvisionCommand;
import com.redknee.app.crm.bean.ServicePackage;
import com.redknee.app.crm.bean.ServicePackageFee;
import com.redknee.app.crm.bean.ServicePackageVersion;
import com.redknee.app.crm.bean.TDMAPackage;
import com.redknee.app.crm.bean.TaxAuthority;
import com.redknee.app.crm.bean.Transaction;
import com.redknee.app.crm.bean.VSATPackage;
import com.redknee.app.crm.bean.account.SubscriptionClass;
import com.redknee.app.crm.bean.account.SubscriptionType;
import com.redknee.app.crm.bean.calldetail.CallDetail;
import com.redknee.app.crm.bean.webcontrol.CustomAccountCategoryWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomAdjustmentInfoTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomAdjustmentInfoWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomAdjustmentTypeTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomAdjustmentTypeWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomAutoDepositReleaseCriteriaWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomAuxiliaryServiceTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomAuxiliaryServiceWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomBillCycleWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomBillingOptionMappingTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomBillingOptionMappingWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomBlackberryServiceExtensionTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomBlackberryServiceExtensionWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomBundleAuxiliaryServiceTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomBundleAuxiliaryServiceWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomBundleCategoryWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomBundleProfileTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomBundleProfileWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomCRMEmailTemplateTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomCRMEmailTemplateWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomCallDetailTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomCallDetailWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomCallTypeTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomCallTypeWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomCreditCategoryWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomCreditNotificationEventTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomCreditNotificationEventWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomEarlyRewardConfigurationTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomEarlyRewardConfigurationWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomGSMPackageTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomGSMPackageWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomKeyConfigurationWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomLateFeeConfigurationTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomLateFeeConfigurationWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomMsisdnGroupTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomMsisdnGroupWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomMsisdnTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomMsisdnWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomNoteTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomNoteWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomNotesPollerConfigTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomNotesPollerConfigWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomNoticeScheduleCreditCategoryExtensionTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomNoticeScheduleCreditCategoryExtensionWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomNotificationTemplateGroupWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomPackageGroupTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomPackageGroupWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomPricePlanTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomPricePlanVersionWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomPricePlanWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomProvisionCommandTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomProvisionCommandWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomResourceDeviceGroupTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomResourceDeviceGroupWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomServiceBalanceLimitTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomServiceBalanceLimitWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomServicePackageFeeTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomServicePackageFeeWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomServicePackageTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomServicePackageVersionWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomServicePackageWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomSubscriberNumberMgmtHistoryTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomSubscriberNumberMgmtHistoryWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomSubscriptionClassTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomSubscriptionClassWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomSubscriptionContractTermWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomSubscriptionTypeTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomSubscriptionTypeWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomTDMAPackageTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomTDMAPackageWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomTransactionWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomVSATPackageTableWebControl;
import com.redknee.app.crm.bean.webcontrol.CustomVSATPackageWebControl;
import com.redknee.app.crm.bundle.BundleAuxiliaryService;
import com.redknee.app.crm.bundle.BundleCategory;
import com.redknee.app.crm.bundle.BundleProfile;
import com.redknee.app.crm.bundle.ServiceBalanceLimit;
import com.redknee.app.crm.config.NotesPollerConfig;
import com.redknee.app.crm.contract.SubscriptionContractTerm;
import com.redknee.app.crm.delivery.email.CRMEmailTemplate;
import com.redknee.app.crm.extension.ExtensionHolder;
import com.redknee.app.crm.extension.creditcategory.CreditCategoryExtension;
import com.redknee.app.crm.extension.creditcategory.CreditCategoryExtensionHolder;
import com.redknee.app.crm.extension.creditcategory.NoticeScheduleCreditCategoryExtension;
import com.redknee.app.crm.extension.service.BlackberryServiceExtension;
import com.redknee.app.crm.extension.service.ServiceExtension;
import com.redknee.app.crm.extension.service.ServiceExtensionHolder;
import com.redknee.app.crm.notification.delivery.BinaryDeliveryService;
import com.redknee.app.crm.notification.delivery.EmailDeliveryService;
import com.redknee.app.crm.notification.delivery.MessageDeliveryService;
import com.redknee.app.crm.notification.delivery.MessageDeliveryServiceFacetFactory;
import com.redknee.app.crm.notification.delivery.SmsDeliveryService;
import com.redknee.app.crm.notification.generator.MessageGenerator;
import com.redknee.app.crm.notification.generator.MessageGeneratorFacetFactory;
import com.redknee.app.crm.notification.generator.SimpleEmailGenerator;
import com.redknee.app.crm.notification.generator.SimpleJasperMessageGenerator;
import com.redknee.app.crm.notification.generator.SimpleSmsGenerator;
import com.redknee.app.crm.notification.generator.SimpleUjacMessageGenerator;
import com.redknee.app.crm.notification.message.BasicBinaryNotification;
import com.redknee.app.crm.notification.message.BasicEmailNotification;
import com.redknee.app.crm.notification.message.BasicPostNotification;
import com.redknee.app.crm.notification.message.BasicSmsNotification;
import com.redknee.app.crm.notification.message.NotificationMessage;
import com.redknee.app.crm.notification.message.NotificationMessageFacetFactory;
import com.redknee.app.crm.notification.template.EmailNotificationTemplate;
import com.redknee.app.crm.notification.template.JasperNotificationTemplate;
import com.redknee.app.crm.notification.template.NotificationTemplateGroup;
import com.redknee.app.crm.notification.template.SmsNotificationTemplate;
import com.redknee.app.crm.notification.template.StaticJasperNotificationTemplate;
import com.redknee.app.crm.notification.template.StaticUjacNotificationTemplate;
import com.redknee.app.crm.notification.template.UjacNotificationTemplate;
import com.redknee.app.crm.numbermgn.ImsiMgmtHistory;
import com.redknee.app.crm.numbermgn.NumberMgmtHistory;
import com.redknee.app.crm.numbermgn.SubscriberNumberMgmtHistory;
import com.redknee.app.crm.resource.CustomResourceDeviceTableWebControl;
import com.redknee.app.crm.resource.CustomResourceDeviceWebControl;
import com.redknee.app.crm.resource.ResourceDevice;
import com.redknee.app.crm.resource.ResourceDeviceGroup;
import com.redknee.app.crm.technology.SetTechnologyProxyWebControl;
import com.redknee.app.crm.web.control.CustomTaxAuthorityWebControl;
import com.redknee.app.crm.web.control.FacetRedirectingTableWebControl;
import com.redknee.app.crm.web.control.FacetRedirectingWebControl;
import com.redknee.framework.core.platform.CoreSupport;


public class FacetInstall extends CoreSupport implements ContextAgent
{

    /* (non-Javadoc)
     * @see com.redknee.framework.xhome.context.ContextAgent#execute(com.redknee.framework.xhome.context.Context)
     */
    public void execute(final Context ctx) throws AgentException
    {
        final FacetMgr fMgr = (FacetMgr) ctx.get(FacetMgr.class);

        registerExtensions(ctx, fMgr);

        registerNotificationClasses(ctx, fMgr);
        
        registerWebControls(ctx, fMgr);
    }

    protected void registerExtensions(final Context ctx, final FacetMgr fMgr)
    {
        // Register Extension Holder facets
        fMgr.register(ctx, ServiceExtension.class, ExtensionHolder.class, ServiceExtensionHolder.class);
        fMgr.register(ctx, CreditCategoryExtension.class, ExtensionHolder.class, CreditCategoryExtensionHolder.class);
    }

    /**
     * Install default notification related facets.  These will be used as a fallback if
     * no specific types/instances of the notification classes are registered.
     * 
     * Custom defaults can be registered by retrieving the factories from the context and
     * calling the register() methods on them as desired.
     */
    protected void registerNotificationClasses(final Context ctx, final FacetMgr fMgr)
    {
        MessageGeneratorFacetFactory defaultMessageGeneratorFactory = new MessageGeneratorFacetFactory();
        MessageDeliveryServiceFacetFactory defaultDeliveryServiceFactory = new MessageDeliveryServiceFacetFactory();
        NotificationMessageFacetFactory defaultMessageFactory = new NotificationMessageFacetFactory();
        
        ctx.put(MessageGeneratorFacetFactory.class, defaultMessageGeneratorFactory);
        ctx.put(MessageDeliveryServiceFacetFactory.class, defaultDeliveryServiceFactory);
        ctx.put(NotificationMessageFacetFactory.class, defaultMessageFactory);
        
        fMgr.register(ctx, MessageGenerator.class, defaultMessageGeneratorFactory);
        fMgr.register(ctx, MessageDeliveryService.class, defaultDeliveryServiceFactory);
        fMgr.register(ctx, NotificationMessage.class, defaultMessageFactory);
        
        // Register notification messages
        defaultMessageFactory.register(SmsNotificationTemplate.class, BasicSmsNotification.class);
        defaultMessageFactory.register(EmailNotificationTemplate.class, BasicEmailNotification.class);
        defaultMessageFactory.register(JasperNotificationTemplate.class, BasicBinaryNotification.class);
        defaultMessageFactory.register(StaticJasperNotificationTemplate.class, BasicBinaryNotification.class);
        defaultMessageFactory.register(UjacNotificationTemplate.class, BasicBinaryNotification.class);
        defaultMessageFactory.register(StaticUjacNotificationTemplate.class, BasicBinaryNotification.class);
        
        // Register generators mapped by template classes
        defaultMessageGeneratorFactory.register(SmsNotificationTemplate.class, SimpleSmsGenerator.class, SimpleSmsGenerator.class);
        defaultMessageGeneratorFactory.register(EmailNotificationTemplate.class, SimpleEmailGenerator.class, SimpleEmailGenerator.class);
        defaultMessageGeneratorFactory.register(JasperNotificationTemplate.class, SimpleJasperMessageGenerator.class, SimpleJasperMessageGenerator.class);
        defaultMessageGeneratorFactory.register(StaticJasperNotificationTemplate.class, SimpleJasperMessageGenerator.class, SimpleJasperMessageGenerator.class);
        defaultMessageGeneratorFactory.register(UjacNotificationTemplate.class, SimpleUjacMessageGenerator.class, SimpleUjacMessageGenerator.class);
        defaultMessageGeneratorFactory.register(StaticUjacNotificationTemplate.class, SimpleUjacMessageGenerator.class, SimpleUjacMessageGenerator.class);

        // Register generators mapped by message classes
        defaultMessageGeneratorFactory.register(BasicSmsNotification.class, SimpleSmsGenerator.class, SimpleSmsGenerator.class);
        defaultMessageGeneratorFactory.register(BasicEmailNotification.class, SimpleEmailGenerator.class, SimpleEmailGenerator.class);
        
        // Register delivery services
        defaultDeliveryServiceFactory.register(BasicSmsNotification.class, SmsDeliveryService.class, SmsDeliveryService.class);
        defaultDeliveryServiceFactory.register(BasicEmailNotification.class, EmailDeliveryService.class, EmailDeliveryService.class);
        defaultDeliveryServiceFactory.register(BasicBinaryNotification.class, BinaryDeliveryService.class, BinaryDeliveryService.class);
        defaultDeliveryServiceFactory.register(BasicPostNotification.class, BinaryDeliveryService.class, BinaryDeliveryService.class);
    }

    protected void registerWebControls(final Context ctx, final FacetMgr fMgr)
    {
        // Register custom web controls to override those provided by data models in ModelAppCrm
        fMgr.register(ctx, AdjustmentInfo.class, WebControl.class, new CustomAdjustmentInfoWebControl());
        fMgr.register(ctx, AdjustmentType.class, WebControl.class, new CustomAdjustmentTypeWebControl());
        fMgr.register(ctx, AutoDepositReleaseCriteria.class, WebControl.class, new CustomAutoDepositReleaseCriteriaWebControl());
        fMgr.register(ctx, AuxiliaryService.class, WebControl.class, new CustomAuxiliaryServiceWebControl());
        fMgr.register(ctx, BillCycle.class, WebControl.class, new CustomBillCycleWebControl());
        fMgr.register(ctx, TaxAuthority.class, WebControl.class, new CustomTaxAuthorityWebControl());

        //TT#12091155018 . Providing Billing Message Configuration at Account Type level
        fMgr.register(ctx, AccountCategory.class, WebControl.class, new CustomAccountCategoryWebControl());

        fMgr.register(ctx, SubscriptionContractTerm.class, WebControl.class, new CustomSubscriptionContractTermWebControl());
        fMgr.register(ctx, BillingOptionMapping.class, WebControl.class, new CustomBillingOptionMappingWebControl());
        fMgr.register(ctx, BlackberryServiceExtension.class, WebControl.class, new CustomBlackberryServiceExtensionWebControl());

        fMgr.register(ctx, com.redknee.app.crm.extension.service.core.BlackberryServiceExtension.class, WebControl.class, new CustomBlackberryServiceExtensionWebControl());

        fMgr.register(ctx, BundleAuxiliaryService.class, WebControl.class, new CustomBundleAuxiliaryServiceWebControl());
        fMgr.register(ctx, BundleCategory.class, WebControl.class, new CustomBundleCategoryWebControl());
        fMgr.register(ctx, BundleProfile.class, WebControl.class, new CustomBundleProfileWebControl());
        fMgr.register(ctx, CallDetail.class, WebControl.class, new CustomCallDetailWebControl());
        fMgr.register(ctx, CallType.class, WebControl.class, new CustomCallTypeWebControl());
        fMgr.register(ctx, CreditCategory.class, WebControl.class, new CustomCreditCategoryWebControl());
        fMgr.register(ctx, CreditNotificationEvent.class, WebControl.class, new CustomCreditNotificationEventWebControl());
        fMgr.register(ctx, CRMEmailTemplate.class, WebControl.class, new CustomCRMEmailTemplateWebControl());
        fMgr.register(ctx, EarlyRewardConfiguration.class, WebControl.class, new CustomEarlyRewardConfigurationWebControl());
        fMgr.register(ctx, GSMPackage.class, WebControl.class, new SetTechnologyProxyWebControl(new CustomGSMPackageWebControl()));
        fMgr.register(ctx, ImsiMgmtHistory.class, WebControl.class,new FacetRedirectingWebControl(ctx, NumberMgmtHistory.class));
        fMgr.register(ctx, KeyConfiguration.class, WebControl.class, new CustomKeyConfigurationWebControl());
        fMgr.register(ctx, LateFeeConfiguration.class, WebControl.class, new CustomLateFeeConfigurationWebControl());
        fMgr.register(ctx, MsisdnGroup.class, WebControl.class, new CustomMsisdnGroupWebControl());
        fMgr.register(ctx, Msisdn.class, WebControl.class, new CustomMsisdnWebControl());
        fMgr.register(ctx, NotesPollerConfig.class, WebControl.class, new CustomNotesPollerConfigWebControl());
        fMgr.register(ctx, Note.class, WebControl.class, new CustomNoteWebControl());
        fMgr.register(ctx, NoticeScheduleCreditCategoryExtension.class, WebControl.class, new CustomNoticeScheduleCreditCategoryExtensionWebControl());
        fMgr.register(ctx, NotificationTemplateGroup.class, WebControl.class, new CustomNotificationTemplateGroupWebControl());
        fMgr.register(ctx, PackageGroup.class, WebControl.class, new CustomPackageGroupWebControl());
        fMgr.register(ctx, PricePlan.class, WebControl.class, new SetTechnologyProxyWebControl(new SetSpidProxyWebControl(new CustomPricePlanWebControl())));
        fMgr.register(ctx, PricePlanVersion.class, WebControl.class, new CustomPricePlanVersionWebControl());
        fMgr.register(ctx, ProvisionCommand.class, WebControl.class, new CustomProvisionCommandWebControl());
        fMgr.register(ctx, ResourceDeviceGroup.class, WebControl.class, new CustomResourceDeviceGroupWebControl());
        fMgr.register(ctx, ResourceDevice.class, WebControl.class, new CustomResourceDeviceWebControl());
        fMgr.register(ctx, ServiceBalanceLimit.class, WebControl.class, new CustomServiceBalanceLimitWebControl());
        fMgr.register(ctx, ServicePackage.class, WebControl.class, new SetTechnologyProxyWebControl(new CustomServicePackageWebControl()));
        fMgr.register(ctx, ServicePackageFee.class, WebControl.class, new CustomServicePackageFeeWebControl());
        fMgr.register(ctx, ServicePackageVersion.class, WebControl.class, new CustomServicePackageVersionWebControl());
        fMgr.register(ctx, SubscriberNumberMgmtHistory.class, WebControl.class, new CustomSubscriberNumberMgmtHistoryWebControl());
        fMgr.register(ctx, SubscriptionClass.class, WebControl.class, new CustomSubscriptionClassWebControl());
        fMgr.register(ctx, SubscriptionType.class, WebControl.class,new CustomSubscriptionTypeWebControl());
        fMgr.register(ctx, Transaction.class, WebControl.class, new CustomTransactionWebControl());
        fMgr.register(ctx, TDMAPackage.class, WebControl.class, new SetTechnologyProxyWebControl(new CustomTDMAPackageWebControl()));
        fMgr.register(ctx, VSATPackage.class, WebControl.class, new SetTechnologyProxyWebControl(new CustomVSATPackageWebControl()));

        // Register custom table web controls to override those provided by data models in ModelAppCrm
        fMgr.register(ctx, AdjustmentInfo.class, TableWebControl.class, new CustomAdjustmentInfoTableWebControl());
        fMgr.register(ctx, AdjustmentType.class, TableWebControl.class, new CustomAdjustmentTypeTableWebControl());
        fMgr.register(ctx, AuxiliaryService.class, TableWebControl.class, new CustomAuxiliaryServiceTableWebControl());
        fMgr.register(ctx, BillingOptionMapping.class, TableWebControl.class, new CustomBillingOptionMappingTableWebControl());
        fMgr.register(ctx, BlackberryServiceExtension.class, TableWebControl.class, new CustomBlackberryServiceExtensionTableWebControl());
        fMgr.register(ctx, BundleAuxiliaryService.class, TableWebControl.class, new CustomBundleAuxiliaryServiceTableWebControl());
        fMgr.register(ctx, BundleProfile.class, TableWebControl.class, new CustomBundleProfileTableWebControl());
        fMgr.register(ctx, CallDetail.class, TableWebControl.class, new CustomCallDetailTableWebControl());
        fMgr.register(ctx, CallType.class, TableWebControl.class, new CustomCallTypeTableWebControl());
        fMgr.register(ctx, CreditNotificationEvent.class, TableWebControl.class, new CustomCreditNotificationEventTableWebControl());
        fMgr.register(ctx, CRMEmailTemplate.class, TableWebControl.class, new CustomCRMEmailTemplateTableWebControl());
        fMgr.register(ctx, EarlyRewardConfiguration.class, TableWebControl.class, new CustomEarlyRewardConfigurationTableWebControl());
        fMgr.register(ctx, GSMPackage.class, TableWebControl.class,new CustomGSMPackageTableWebControl());
        fMgr.register(ctx, ImsiMgmtHistory.class, TableWebControl.class,new FacetRedirectingTableWebControl(ctx, NumberMgmtHistory.class));
        fMgr.register(ctx, LateFeeConfiguration.class, TableWebControl.class, new CustomLateFeeConfigurationTableWebControl());
        fMgr.register(ctx, MsisdnGroup.class, TableWebControl.class, new CustomMsisdnGroupTableWebControl());
        fMgr.register(ctx, Msisdn.class, TableWebControl.class, new CustomMsisdnTableWebControl());
        fMgr.register(ctx, NotesPollerConfig.class, TableWebControl.class, new CustomNotesPollerConfigTableWebControl());
        fMgr.register(ctx, Note.class, TableWebControl.class, new CustomNoteTableWebControl());
        fMgr.register(ctx, NoticeScheduleCreditCategoryExtension.class, TableWebControl.class, new CustomNoticeScheduleCreditCategoryExtensionTableWebControl());
        fMgr.register(ctx, PackageGroup.class, TableWebControl.class, new CustomPackageGroupTableWebControl());
        fMgr.register(ctx, PricePlan.class, TableWebControl.class, new CustomPricePlanTableWebControl());
        fMgr.register(ctx, ProvisionCommand.class, TableWebControl.class, new CustomProvisionCommandTableWebControl());
        fMgr.register(ctx, ResourceDeviceGroup.class, TableWebControl.class, new CustomResourceDeviceGroupTableWebControl());
        fMgr.register(ctx, ResourceDevice.class, TableWebControl.class, new CustomResourceDeviceTableWebControl());
        fMgr.register(ctx, ServiceBalanceLimit.class, TableWebControl.class, new CustomServiceBalanceLimitTableWebControl());
        fMgr.register(ctx, ServicePackage.class, TableWebControl.class, new CustomServicePackageTableWebControl());
        fMgr.register(ctx, ServicePackageFee.class, TableWebControl.class, new CustomServicePackageFeeTableWebControl());
        fMgr.register(ctx, SubscriberNumberMgmtHistory.class, TableWebControl.class, new CustomSubscriberNumberMgmtHistoryTableWebControl());
        fMgr.register(ctx, SubscriptionClass.class, TableWebControl.class, new CustomSubscriptionClassTableWebControl());
        fMgr.register(ctx, SubscriptionType.class, TableWebControl.class,new CustomSubscriptionTypeTableWebControl());
        fMgr.register(ctx, TDMAPackage.class, TableWebControl.class, new CustomTDMAPackageTableWebControl());
        fMgr.register(ctx, VSATPackage.class, TableWebControl.class,new CustomVSATPackageTableWebControl());
    }
}
