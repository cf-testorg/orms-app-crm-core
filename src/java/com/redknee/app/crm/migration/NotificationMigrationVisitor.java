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
package com.redknee.app.crm.migration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redknee.framework.core.cron.CronEntry;
import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.msg.EmailConfig;
import com.redknee.framework.msg.EmailTemplate;
import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.home.ReadOnlyHome;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.CloneingVisitor;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.AccountCategory;
import com.redknee.app.crm.bean.AccountCategoryXInfo;
import com.redknee.app.crm.bean.CreditCategoryXInfo;
import com.redknee.app.crm.bean.CreditEventTypeEnum;
import com.redknee.app.crm.bean.CreditNotificationEvent;
import com.redknee.app.crm.bean.KeyConfiguration;
import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.bean.PaymentNotificationMsg;
import com.redknee.app.crm.bean.SmsDisputeNotificationConfig;
import com.redknee.app.crm.bean.SpidLangMsgConfig;
import com.redknee.app.crm.bean.SpidLangTimedMsgConfig;
import com.redknee.app.crm.bean.StateNotificationMsg;
import com.redknee.app.crm.bean.SubPreWarnMsg;
import com.redknee.app.crm.bean.SubProfileNotificationMsg;
import com.redknee.app.crm.bean.SubServiceSuspendMsg;
import com.redknee.app.crm.bean.core.CreditCategory;
import com.redknee.app.crm.delivery.email.CRMEmailConfig;
import com.redknee.app.crm.delivery.email.CRMEmailTemplate;
import com.redknee.app.crm.delivery.email.EmailTemplateTypeEnum;
import com.redknee.app.crm.extension.creditcategory.NoticeScheduleCreditCategoryExtension;
import com.redknee.app.crm.extension.spid.NotificationMethodSpidExtension;
import com.redknee.app.crm.extension.spid.NotificationMethodSpidExtensionXInfo;
import com.redknee.app.crm.notification.LiaisonSchedule;
import com.redknee.app.crm.notification.NotificationTypeEnum;
import com.redknee.app.crm.notification.liaison.NotificationLiaison;
import com.redknee.app.crm.notification.liaison.NotificationLiaisonProxy;
import com.redknee.app.crm.notification.liaison.ScheduledTaskNotificationLiaison;
import com.redknee.app.crm.notification.template.EmailNotificationTemplate;
import com.redknee.app.crm.notification.template.NotificationTemplate;
import com.redknee.app.crm.notification.template.NotificationTemplateGroup;
import com.redknee.app.crm.notification.template.NotificationTemplateGroupHolder;
import com.redknee.app.crm.notification.template.NotificationTemplateGroupXInfo;
import com.redknee.app.crm.notification.template.NotificationTemplateHolder;
import com.redknee.app.crm.notification.template.SmsNotificationTemplate;
import com.redknee.app.crm.notification.template.TemplateGroupGlobalRecord;
import com.redknee.app.crm.notification.template.TemplateGroupGlobalRecordXInfo;
import com.redknee.app.crm.support.AccountTypeSupportHelper;
import com.redknee.app.crm.support.DeploymentTypeSupport;
import com.redknee.app.crm.support.DeploymentTypeSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.app.crm.support.KeyValueSupportHelper;
import com.redknee.app.crm.support.NotificationSupportHelper;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class NotificationMigrationVisitor implements Visitor
{

    /**
     * {@inheritDoc}
     */
    public void visit(Context ctx, Object obj) throws AgentException, AbortVisitException
    {
        NotificationTemplateGroup group = null;
        NotificationTypeEnum notificationType = null;
        
        if (obj instanceof CRMEmailTemplate)
        {
            CRMEmailTemplate template = (CRMEmailTemplate) obj;
            
            EmailTemplateTypeEnum type = EmailTemplateTypeEnum.get((short) template.getTemplateType());
            notificationType = getNotificationType(ctx, type);
            if (notificationType != null)
            {
                group = createTemplateGroup(ctx, template, notificationType);
            }
        }
        else if (obj instanceof SmsDisputeNotificationConfig)
        {
            DeploymentTypeSupport deploymentTypeSupport = DeploymentTypeSupportHelper.get(ctx);
            if (deploymentTypeSupport.isBas(ctx)
                    || deploymentTypeSupport.isEcare(ctx)
                    || deploymentTypeSupport.isSingle(ctx))
            {
                SmsDisputeNotificationConfig template = (SmsDisputeNotificationConfig) obj;
                
                String message = template.getMessage();
                if (template.isIncludeDisputeId())
                {
                    message += " [DisputeId=$DISPUTE_ID$]";
                }

                notificationType = NotificationTypeEnum.TRANSFER_DISPUTE;
                group = createTemplateGroup(ctx, template, notificationType, message);
            }
        }
        else if (obj instanceof PaymentNotificationMsg)
        {
            DeploymentTypeSupport deploymentTypeSupport = DeploymentTypeSupportHelper.get(ctx);
            if (deploymentTypeSupport.isInvoiceServer(ctx))
            {
                PaymentNotificationMsg template = (PaymentNotificationMsg) obj;

                String message = template.getMessage();

                notificationType = NotificationTypeEnum.PAYMENT_REMINDER;
                group = createTemplateGroup(ctx, template, notificationType, message);
            }
        }
        else if (obj instanceof SubPreWarnMsg)
        {
            DeploymentTypeSupport deploymentTypeSupport = DeploymentTypeSupportHelper.get(ctx);
            if (deploymentTypeSupport.isBas(ctx)
                    || deploymentTypeSupport.isEcare(ctx)
                    || deploymentTypeSupport.isSingle(ctx))
            {
                SubPreWarnMsg template = (SubPreWarnMsg) obj;
                
                String message = template.getPreWarnHeader() + "\n"
                    + "$PACKAGES$$SERVICES$$BUNDLES$$AUXILIARYSERVICES$\n"
                    + template.getPreWarnFooter();

                notificationType = NotificationTypeEnum.PREPAID_RECURRING_RECHARGE_PREWARNING;
                group = createTemplateGroup(ctx, template, notificationType, message);
            }
        }
        else if (obj instanceof SubServiceSuspendMsg)
        {
            DeploymentTypeSupport deploymentTypeSupport = DeploymentTypeSupportHelper.get(ctx);
            if (deploymentTypeSupport.isBas(ctx)
                    || deploymentTypeSupport.isEcare(ctx)
                    || deploymentTypeSupport.isSingle(ctx))
            {
                SubServiceSuspendMsg template = (SubServiceSuspendMsg) obj;

                try
                {
                    SpidLangTimedMsgConfig other = new SpidLangTimedMsgConfig();
                    other.setActive(template.isActive());
                    other.setLanguage(template.getLanguage());
                    other.setSpid(template.getSpid());
                    other.setTimeToSend(template.getTimeToSend());
                    
                    Context sCtx = ctx.createSubContext();
                    
                    String suspendMessage = template.getUnsuspendHeader() + "\n" 
                        + "$PACKAGES$$SERVICES$$BUNDLES$$AUXILIARYSERVICES$\n"
                        + template.getUnsuspendFooter();
                    
                    sCtx.put("MESSAGE", suspendMessage);
                    sCtx.put(NotificationTypeEnum.class, NotificationTypeEnum.SERVICE_UNSUSPENSION);
                    visit(sCtx, other);
                }
                catch (Exception e)
                {
                    // NOP - Proceed.  Any error should have been logged by visit() call.
                }

                String message = template.getSuspendHeader() + "\n"
                    + "$PACKAGES$$SERVICES$$BUNDLES$$AUXILIARYSERVICES$\n";

                notificationType = NotificationTypeEnum.SERVICE_SUSPENSION;
                group = createTemplateGroup(ctx, template, notificationType, message);
            }
        }
        else if (obj instanceof StateNotificationMsg)
        {
            DeploymentTypeSupport deploymentTypeSupport = DeploymentTypeSupportHelper.get(ctx);
            if (deploymentTypeSupport.isBas(ctx)
                    || deploymentTypeSupport.isEcare(ctx)
                    || deploymentTypeSupport.isSingle(ctx))
            {
                StateNotificationMsg template = (StateNotificationMsg) obj;

                String message = template.getMsg();

                notificationType = NotificationTypeEnum.STATE_CHANGE;
                group = createTemplateGroup(ctx, template, notificationType, message);
            }
        }
        else if (obj instanceof SubProfileNotificationMsg)
        {
            DeploymentTypeSupport deploymentTypeSupport = DeploymentTypeSupportHelper.get(ctx);
            if (deploymentTypeSupport.isBas(ctx)
                    || deploymentTypeSupport.isEcare(ctx)
                    || deploymentTypeSupport.isSingle(ctx))
            {
                SubProfileNotificationMsg template = (SubProfileNotificationMsg) obj;
                
                // Migrate the other notifications contained within this template
                Context sCtx = ctx.createSubContext();
                try
                {
                    SpidLangMsgConfig other = new SpidLangMsgConfig();
                    other.setActive(template.isActive());
                    other.setLanguage(template.getLanguage());
                    other.setSpid(template.getSpid());
                    
                    try
                    {
                        sCtx.put("MESSAGE", template.getExpiryDateExtensionMsg());
                        sCtx.put(NotificationTypeEnum.class, NotificationTypeEnum.EXPIRY_EXTENSION);
                        visit(sCtx, other);
                    }
                    finally
                    {
                        sCtx.put("MESSAGE", template.getRechargeMsg());
                        sCtx.put(NotificationTypeEnum.class, NotificationTypeEnum.PREPAID_RECHARGE);
                        visit(sCtx, other);
                    }
                }
                catch (Exception e)
                {
                    // NOP - Proceed.  Any error should have been logged by visit() call.
                }

                String message = template.getPreExpiryMsg();

                notificationType = NotificationTypeEnum.PRE_EXPIRY;
                group = createTemplateGroup(ctx, template, notificationType, message);
            }
        }
        else if (obj instanceof SpidLangMsgConfig)
        {
            // We hackily resend this in explicitly above to handle the existing one-to-many class-to-feature SMS templates
            String message = (String) ctx.get("MESSAGE");
            notificationType = (NotificationTypeEnum) ctx.get(NotificationTypeEnum.class);
            if (message != null && message.trim().length() > 0)
            {
                group = createTemplateGroup(ctx, (SpidLangMsgConfig) obj, notificationType, message);
            }
        }

        if (group != null)
        {
            group = updateNotificationConfiguration(ctx, obj, notificationType, group);

            CronEntry cronEntry = null;
            if (obj instanceof SpidLangTimedMsgConfig)
            {
                cronEntry = getCronEntryForTimeToSend(ctx, ((SpidLangTimedMsgConfig) obj).getTimeToSend());
            }
            else if (obj instanceof SubPreWarnMsg)
            {
                cronEntry = getCronEntryForTimeToSend(ctx, ((SubPreWarnMsg) obj).getSmsSendTime());
            }
            else if (obj instanceof SubProfileNotificationMsg)
            {
                cronEntry = getCronEntryForTimeToSend(ctx, ((SubProfileNotificationMsg) obj).getPreExpirySmsSendTime());
            }
            else if (obj instanceof StateNotificationMsg)
            {
                cronEntry = getCronEntryForTimeToSend(ctx, ((StateNotificationMsg) obj).getStateChangeSmsSendTime());
            }
            if (cronEntry != null)
            {
                String scheduleName = cronEntry.getName();
                if (notificationType != null)
                {
                    scheduleName = notificationType.getDescription(ctx);
                }
                
                LiaisonSchedule schedule = new LiaisonSchedule();
                schedule.setScheduleName(scheduleName);
                schedule.setSchedule((String) cronEntry.ID());
                
                try
                {
                    schedule = (LiaisonSchedule) createOrStore(ctx, schedule);
                }
                catch (HomeException e)
                {
                    new MajorLogMsg(this, "Error creating liaison schedule for schedule " + cronEntry.getName() + " [Liaison Schedule Name = " + scheduleName + "].", e).log(ctx);
                }
                
                if (obj instanceof PaymentNotificationMsg)
                {
                    PaymentNotificationMsg template = (PaymentNotificationMsg) obj;

                    int spid = template.getSpid();
                    
                    try
                    {
                        Collection<CreditCategory> creditCategories = HomeSupportHelper.get(ctx).getBeans(ctx, CreditCategory.class, new EQ(CreditCategoryXInfo.SPID, spid));
                        for (CreditCategory cat : creditCategories)
                        {
                            NoticeScheduleCreditCategoryExtension extension = HomeSupportHelper.get(ctx).findBean(ctx, NoticeScheduleCreditCategoryExtension.class, cat.getCode());
                            if (extension == null)
                            {
                                extension = new NoticeScheduleCreditCategoryExtension();
                                extension.setCreditCategory(cat.getCode());
                                extension.setSchedule(schedule.getScheduleName());
                            }
                            
                            CreditNotificationEvent event = new CreditNotificationEvent();
                            event.setEventDateOffset(template.getGraceDays());
                            event.setEventTypeIndex(CreditEventTypeEnum.PAYMENT_DUE_INDEX);
                            
                            Map<Object, CreditNotificationEvent> events = new HashMap<Object, CreditNotificationEvent>(extension.getEvents());
                            if (!events.containsKey(event.ID()))
                            {
                                events.put(event.ID(), event);
                                extension.setEvents(events);
                                
                                createOrStore(ctx, extension);
                            }
                        }
                    }
                    catch (HomeException e)
                    {
                        new MinorLogMsg(this, "Error migrating payment notification grace days to credit category for " + obj, e).log(ctx);
                    }
                }
                
                // Configure the scheduled liaison as per this message configuration
                NotificationLiaison liaison = NotificationSupportHelper.get(ctx).getLiaisonForNotificationType(ctx, notificationType);
                if (liaison instanceof NotificationLiaisonProxy)
                {
                    NotificationLiaison liaisonClone = liaison;
                    try
                    {
                        liaisonClone = (NotificationLiaison) ((NotificationLiaisonProxy) liaison).clone();
                    }
                    catch (CloneNotSupportedException e)
                    {
                        // NOP
                    }
                    NotificationLiaisonProxy proxy = (NotificationLiaisonProxy) liaisonClone;
                    ScheduledTaskNotificationLiaison scheduledLiaison = (ScheduledTaskNotificationLiaison) proxy.findDecorator(ScheduledTaskNotificationLiaison.class);
                    
                    // Dig to get the last proxy
                    while (proxy.getDelegate() instanceof NotificationLiaisonProxy)
                    {
                        proxy = (NotificationLiaisonProxy) proxy.getDelegate();
                    }
                    
                    // Set the schedule
                    if (scheduledLiaison == null)
                    {
                        scheduledLiaison = new ScheduledTaskNotificationLiaison();
                    }
                    scheduledLiaison.setLiaisonSchedule(schedule.getScheduleName());

                    NotificationLiaison delegate = scheduledLiaison;
                    if (proxy != liaisonClone)
                    {
                        // Put the scheduled liaison in the pipeline
                        proxy.setDelegate(scheduledLiaison);
                        delegate = ((NotificationLiaisonProxy) liaisonClone).getDelegate();
                    }
                    
                    // Must call setter here to trigger journal update
                    ((NotificationLiaisonProxy) liaison).setDelegate(delegate);
                }
                else if (liaison instanceof ScheduledTaskNotificationLiaison)
                {
                    ((ScheduledTaskNotificationLiaison)liaison).setLiaisonSchedule(schedule.getScheduleName());
                }
            }
        }
    }

    protected NotificationTemplateGroup createTemplateGroup(Context ctx, CRMEmailTemplate template,
            NotificationTypeEnum type)
    {
        NotificationTemplateGroup group;
        group = new NotificationTemplateGroup();
        
        group.setName(type.getDescription(ctx));
        group.setSpid(template.getSpid());
        
        EmailNotificationTemplate newTemplate = createEmailTemplate(template, type);
        EmailTemplate emailTemplate = template.getEmailTemplate();
        
        newTemplate.setSubject(removeFWPropertyPrefixes(ctx, emailTemplate.getSubject()));
        newTemplate.setMessage(removeFWPropertyPrefixes(ctx, emailTemplate.getEmail()));
        newTemplate.setContentHandler(emailTemplate.getContentHandler());
        
        try
        {
            CRMEmailConfig config = HomeSupportHelper.get(ctx).findBean(ctx, CRMEmailConfig.class, template.getSpid());
            if (config != null)
            {
                EmailConfig smtpConfig = config.getSmtpConfig();
                if (smtpConfig != null)
                {
                    newTemplate.setFrom(smtpConfig.getReplyToAddress());
                    newTemplate.setReplyTo(smtpConfig.getReplyToAddress());
                }
            }
        }
        catch (HomeException e)
        {
            // NOP - Not a big deal, from/replyTo address will be filled in by generator or delivery service anyways.
        }
        
        NotificationTemplateHolder holder = new NotificationTemplateHolder();
        holder.setEnabled(true);
        holder.setTemplate(newTemplate);
        
        List<NotificationTemplateHolder> templates = new ArrayList<NotificationTemplateHolder>();
        templates.add(holder);
        group.setTemplates(templates);
        return group;
    }

    private String replaceOldSmsVariables(String text)
    {
        String result = text;
        
        result = result.replace("%BALANCE_REMAINING%", "$SUBBALANCEREMAINING$");
        result = result.replace("%VOUCHER_CREDIT_VALUE%", "$VOUCHERCREDITVALUE$");
        result = result.replace("%SUBSCRIBER_STATE%", "$SUBSTATE$");
        result = result.replace("%EXPIRATION_DATE%", "$SUBEXPIRY$");
        result = result.replace("%MSISDN%", "$SUBMSISDN$");
        result = result.replace("%MDN%", "$SUBMSISDN$");
        result = result.replace("%IMSI%", "$SUBIMSI$");
        result = result.replace("%MIN%", "$SUBIMSI$");
        result = result.replace("%BILLING_DATE%", "$BILLINGDATE$");
        result = result.replace("%FREQUENCY%", "$FREQUENCY$");
        result = result.replace("%TOTAL_CHARGE%", "$TOTALCHARGE$");
        
        return result;
    }

    private String replaceOldServiceSmsVariables(String prefix, String text)
    {
        String result = text;
        
        result = result.replace("%SRV_ID%", "$" + prefix + "_ID$");
        result = result.replace("%SRV_NAME%", "$" + prefix + "_NAME$");
        result = result.replace("%SRV_FEE%", "$" + prefix + "_FEE$");
        
        return result;
    }

    private String removeFWPropertyPrefixes(Context ctx, String text)
    {
        String result = text;
        
        Collection<KeyConfiguration> keys = KeyValueSupportHelper.get(ctx).getConfiguredKeys(ctx, true, (KeyValueFeatureEnum[]) null);
        for (KeyConfiguration key : keys)
        {
            result = result.replace("$" + key.getKey(), key.getKey());
        }
        
        return result;
    }

    protected NotificationTemplateGroup createTemplateGroup(Context ctx, SpidLangMsgConfig template, NotificationTypeEnum type, String message)
    {
        NotificationTemplateGroup group = new NotificationTemplateGroup();

        group.setSpid(template.getSpid());
        if (type != null)
        {
            group.setName(type.getDescription(ctx)); 
        }
        else
        {
            String groupName = template.getClass().getSimpleName();
            XInfo xinfo = (XInfo) XBeans.getInstanceOf(ctx, template, XInfo.class);
            if (xinfo != null)
            {
                groupName = xinfo.getLabel(ctx);
            }
            group.setName(groupName); 
        }
        
        NotificationTemplateHolder holder = new NotificationTemplateHolder();
        holder.setEnabled(template.getActive());
        holder.setLanguage(getLanguage(template));
        
        SmsNotificationTemplate newTemplate = createSmsTemplate(template);
        newTemplate.setMessage(replaceOldSmsVariables(message));
        holder.setTemplate(newTemplate);
        
        List<NotificationTemplateHolder> templates = new ArrayList<NotificationTemplateHolder>();
        templates.add(holder);
        group.setTemplates(templates);
        
        return group;
    }

    protected SmsNotificationTemplate createSmsTemplate(SpidLangMsgConfig template)
    {
        return new SmsNotificationTemplate();
    }

    protected EmailNotificationTemplate createEmailTemplate(CRMEmailTemplate template, NotificationTypeEnum type)
    {
        return new EmailNotificationTemplate();
    }
    
    protected CronEntry getCronEntryForTimeToSend(Context ctx, String tts)
    {
        if (tts == null)
        {
            return null;
        }
        
        CronEntry cron = new CronEntry();
        
        String[] tokens = tts.split(":");
        if (tokens != null && tokens.length == 2)
        {
            cron.setHours(String.valueOf(Integer.parseInt(tokens[0].trim())));
            cron.setMinutes(String.valueOf(Integer.parseInt(tokens[1].trim())));
        }
        else
        {
            return null;
        }
        
        cron.setName("CRM_Daily_" + tts);

        try
        {
            cron = (CronEntry) createOrStore(ctx, cron);
        }
        catch (HomeException e)
        {
            new MajorLogMsg(this, "Error creating schedule " + cron.getName(), e).log(ctx);
            cron = null;
        }
        
        return cron;
    }

    protected String getLanguage(SpidLangMsgConfig template)
    {
        String language = template.getLanguage();
        if (SpidLangMsgConfig.DEFAULT_LANGUAGE.equals(language))
        {
            language = NotificationTemplateHolder.DEFAULT_LANGUAGE;
        }
        return language;
    }

    /**
     * This method returns notification types that are supported by the running application.
     */
    protected NotificationTypeEnum getNotificationType(Context ctx, EmailTemplateTypeEnum type)
    {
        NotificationTypeEnum notificationType = null;
        
        DeploymentTypeSupport deploymentTypeSupport = DeploymentTypeSupportHelper.get(ctx);
        if (deploymentTypeSupport.isInvoiceServer(ctx))
        {
            // Invoice Server owned templates
            if (type == EmailTemplateTypeEnum.INVOICE)
            {
                //notificationType = NotificationTypeEnum.INVOICE_NOTIFICATION;
            }
        }
        else if (deploymentTypeSupport.isBas(ctx)
                || deploymentTypeSupport.isEcare(ctx)
                || deploymentTypeSupport.isSingle(ctx))
        {
            // CRM owned templates
            switch (type.getIndex())
            {
            case EmailTemplateTypeEnum.EXPIRY_EXTENSION_INDEX:
                notificationType = NotificationTypeEnum.EXPIRY_EXTENSION;
                break;
            case EmailTemplateTypeEnum.PRE_EXPIRY_INDEX:
                notificationType = NotificationTypeEnum.PRE_EXPIRY;
                break;
            case EmailTemplateTypeEnum.PREPAID_RECHARGE_INDEX:
                notificationType = NotificationTypeEnum.PREPAID_RECHARGE;
                break;
            case EmailTemplateTypeEnum.PREPAID_RECURRING_RECHARGE_PREWARNING_INDEX:
                notificationType = NotificationTypeEnum.PREPAID_RECURRING_RECHARGE_PREWARNING;
                break;
            case EmailTemplateTypeEnum.SERVICE_SUSPENSION_INDEX:
                notificationType = NotificationTypeEnum.SERVICE_SUSPENSION;
                break;
            case EmailTemplateTypeEnum.SERVICE_UNSUSPENSION_INDEX:
                notificationType = NotificationTypeEnum.SERVICE_UNSUSPENSION;
                break;
            case EmailTemplateTypeEnum.STATE_CHANGE_INDEX:
                notificationType = NotificationTypeEnum.STATE_CHANGE;
                break;
            case EmailTemplateTypeEnum.TRANSFER_DISPUTE_INDEX:
                notificationType = NotificationTypeEnum.TRANSFER_DISPUTE;
                break;
            }
        }
        
        return notificationType;
    }

    protected NotificationTemplateGroup updateNotificationConfiguration(
            Context ctx, 
            Object source, 
            NotificationTypeEnum notificationType,
            NotificationTemplateGroup group)
    {
        NotificationTemplateGroup existingGroup = null;
        try
        {
            And filter = new And();
            filter.add(new EQ(NotificationTemplateGroupXInfo.NAME, group.getName()));
            filter.add(new EQ(NotificationTemplateGroupXInfo.SPID, group.getSpid()));
            existingGroup = HomeSupportHelper.get(ctx).findBean(ctx, NotificationTemplateGroup.class, filter);
        }
        catch (HomeException e)
        {
            new MinorLogMsg(this, "Error retrieving existing notification template group matching [Name=" + group.getName() + ",SPID=" + group.getSpid(), e).log(ctx);
        }
        if (existingGroup != null)
        {
            List<NotificationTemplateHolder> templates = new ArrayList<NotificationTemplateHolder>();

            List<NotificationTemplateHolder> existingTemplates = existingGroup.getTemplates();
            List<NotificationTemplateHolder> newTemplates = group.getTemplates();
            if (newTemplates != null)
            {
                for (NotificationTemplateHolder newTemplate : newTemplates)
                {
                    if (existingTemplates != null)
                    {
                        for (NotificationTemplateHolder existingTemplate : existingTemplates)
                        {
                            if (SafetyUtil.safeEquals(newTemplate.getLanguage(), existingTemplate.getLanguage()))
                            {
                                NotificationTemplate newImpl = newTemplate.getTemplate();
                                NotificationTemplate existingImpl = existingTemplate.getTemplate();

                                if (newImpl instanceof SmsNotificationTemplate)
                                {
                                    if (!(existingImpl instanceof SmsNotificationTemplate))
                                    {
                                        templates.add(existingTemplate);
                                    }
                                }
                                else if (newImpl instanceof EmailNotificationTemplate)
                                {
                                    if (!(existingImpl instanceof EmailNotificationTemplate))
                                    {
                                        templates.add(existingTemplate);
                                    }
                                }
                            }
                            else
                            {
                                templates.add(existingTemplate);
                            }
                        }
                    }
                    templates.add(newTemplate);
                }                        
            }

            group.setTemplates(templates);
        }

        try
        {
            group = (NotificationTemplateGroup) createOrStore(ctx, group);
        }
        catch (HomeException e)
        {
            new MajorLogMsg(this, "Error migrating " + source + " to notification template group configuration.", e).log(ctx);
        }

        if (group != null)
        {
            try
            {
                And filter = new And();
                filter.add(new EQ(TemplateGroupGlobalRecordXInfo.APP_NAME, CoreSupport.getApplication(ctx).getName()));
                filter.add(new EQ(TemplateGroupGlobalRecordXInfo.NAME, group.getName()));
                filter.add(new EQ(TemplateGroupGlobalRecordXInfo.SPID, group.getSpid()));
                TemplateGroupGlobalRecord record = HomeSupportHelper.get(ctx).findBean(ctx, TemplateGroupGlobalRecord.class, filter);
                if (record != null)
                {
                    final NotificationTemplateGroupHolder newGroupRef = new NotificationTemplateGroupHolder();
                    newGroupRef.setOccurrenceNumber(1);
                    newGroupRef.setNotificationTypeIndex(notificationType.getIndex());
                    newGroupRef.setTemplateGroup(((Long)record.ID()).longValue());
                    
                    Home home = null;
                    PropertyInfo property = null;
                    
                    try
                    {
                        if (HomeSupportHelper.get(ctx).hasBeans(ctx, 
                                NotificationMethodSpidExtension.class, 
                                new EQ(NotificationMethodSpidExtensionXInfo.SPID, group.getSpid())))
                        {
                            // If there is a SPID extension, then put the templates in it.  They will be used as SPID defaults.
                            home = HomeSupportHelper.get(ctx).getHome(ctx, NotificationMethodSpidExtension.class);
                            property = NotificationMethodSpidExtensionXInfo.SPID;
                        }
                    }
                    catch (HomeException e)
                    {
                    }
                    
                    if (home == null)
                    {
                        // If there was no SPID extension, then put the templates in all account types for the SPID
                        home = HomeSupportHelper.get(ctx).getHome(ctx, AccountTypeSupportHelper.get(ctx).getAccountTypeClass(ctx));
                        property = AccountCategoryXInfo.SPID;
                    }
                    
                    if (home != null)
                    {
                        home.forEach(ctx, 
                                new CloneingVisitor(new TemplateGroupReferenceUpdatingVisitor(newGroupRef, home)), 
                                new EQ(property, group.getSpid()));
                    }
                }
            }
            catch (HomeException e)
            {
                new MajorLogMsg(this, "Error adding reference to template group " + group.getName() + " to notification method SPID extensions.", e).log(ctx);
            }
        }
        
        return group;
    }

    protected AbstractBean createOrStore(Context ctx, AbstractBean bean) throws HomeException
    {
        try
        {
            bean = HomeSupportHelper.get(ctx).createBean(ctx, bean);
        }
        catch (HomeException e)
        {
            try
            {
                bean = HomeSupportHelper.get(ctx).storeBean(ctx, bean);
            }
            catch (HomeException e1)
            {
                throw e;
            }
        }
        return bean;
    }

    private static final class TemplateGroupReferenceUpdatingVisitor implements Visitor
    {
        private final NotificationTemplateGroupHolder newGroupRef_;
        private final Home home_;

        private TemplateGroupReferenceUpdatingVisitor(NotificationTemplateGroupHolder newGroupRef, Home home)
        {
            this.newGroupRef_ = newGroupRef;
            
            if (home instanceof HomeProxy
                    && ((HomeProxy) home).hasDecorator(ReadOnlyHome.class))
            {
                home = ((HomeProxy)((HomeProxy) home).findDecorator(ReadOnlyHome.class)).getDelegate();
            }
            this.home_ = home;
        }

        public void visit(Context ctx, Object obj) throws AgentException, AbortVisitException
        {
            NotificationTemplateGroupHolder oldRef = null;

            if (obj instanceof AccountCategory)
            {
                AccountCategory cat = (AccountCategory) obj;

                Map<Object, NotificationTemplateGroupHolder> groupRefs = new HashMap<Object, NotificationTemplateGroupHolder>(cat.getTemplateGroups());

                oldRef = groupRefs.get(newGroupRef_.ID());

                groupRefs.put(newGroupRef_.ID(), newGroupRef_);
                cat.setTemplateGroups(groupRefs);
            }

            if (obj instanceof NotificationMethodSpidExtension)
            {
                NotificationMethodSpidExtension ext = (NotificationMethodSpidExtension) obj;

                Map<Object, NotificationTemplateGroupHolder> groupRefs = new HashMap<Object, NotificationTemplateGroupHolder>(ext.getDefaultTemplateGroups());

                oldRef = groupRefs.get(newGroupRef_.ID());

                groupRefs.put(newGroupRef_.ID(), newGroupRef_);
                ext.setDefaultTemplateGroups(groupRefs);
            }

            TemplateGroupGlobalRecord record = null;
            if (oldRef != null)
            {
                try
                {
                    record = HomeSupportHelper.get(ctx).findBean(ctx, TemplateGroupGlobalRecord.class, oldRef.getTemplateGroup());
                }
                catch (HomeException e)
                {
                    // NOP
                }
            }
            
            if (record == null)
            {
                try
                {
                    home_.store(ctx, obj);
                }
                catch (HomeException e)
                {
                    new MajorLogMsg(this, "Error adding reference to template group " + newGroupRef_.getTemplateGroup() + " to home for " 
                            + obj.getClass().getName() + ".", e).log(ctx);
                }
            }
        }
    }
}
