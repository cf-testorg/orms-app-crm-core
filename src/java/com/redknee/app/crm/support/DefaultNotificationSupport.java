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
package com.redknee.app.crm.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.redknee.framework.core.bean.Application;
import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.xenum.EnumCollection;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.AccountCategory;
import com.redknee.app.crm.bean.NotificationMethodEnum;
import com.redknee.app.crm.extension.spid.NotificationMethodSpidExtension;
import com.redknee.app.crm.notification.NotificationTypeEnum;
import com.redknee.app.crm.notification.ScheduledNotification;
import com.redknee.app.crm.notification.ScheduledNotificationXInfo;
import com.redknee.app.crm.notification.liaison.NotificationLiaison;
import com.redknee.app.crm.notification.template.EmailNotificationTemplate;
import com.redknee.app.crm.notification.template.NotificationTemplate;
import com.redknee.app.crm.notification.template.NotificationTemplateGroup;
import com.redknee.app.crm.notification.template.NotificationTemplateGroupHolder;
import com.redknee.app.crm.notification.template.NotificationTemplateGroupHolderID;
import com.redknee.app.crm.notification.template.NotificationTemplateGroupID;
import com.redknee.app.crm.notification.template.NotificationTemplateHolder;
import com.redknee.app.crm.notification.template.SmsNotificationTemplate;
import com.redknee.app.crm.notification.template.TemplateGroupGlobalRecord;
import com.redknee.app.crm.notification.template.TemplateGroupGlobalRecordXInfo;


/**
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class DefaultNotificationSupport implements NotificationSupport
{
    protected static NotificationSupport instance_ = null;
    public static NotificationSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultNotificationSupport();
        }
        return instance_;
    }

    protected DefaultNotificationSupport()
    {
    }

    /**
     * {@inheritDoc}
     */
    public Collection<NotificationTemplate> getTemplates(
            Context ctx, 
            String templateGroupName, int spid, 
            String language, 
            Class<NotificationTemplate>... preferredTypes) throws HomeException
    {
        Collection<NotificationTemplate> result = new ArrayList<NotificationTemplate>();
        Collection<NotificationTemplate> defaultTemplates = new ArrayList<NotificationTemplate>();

        NotificationTemplateGroup group = HomeSupportHelper.get(ctx).findBean(ctx, 
                NotificationTemplateGroup.class, 
                new NotificationTemplateGroupID(templateGroupName, spid));
        if (group == null)
        {
            return result;
        }

        List<NotificationTemplateHolder> templates = group.getTemplates();
        if (templates == null)
        {
            return result;
        }
        
        for (NotificationTemplateHolder templateHolder : templates)
        {
            if (templateHolder == null || !templateHolder.isEnabled())
            {
                continue;
            }

            if (!NotificationTemplateHolder.DEFAULT_LANGUAGE.equals(templateHolder.getLanguage())
                    && !SafetyUtil.safeEquals(language, templateHolder.getLanguage()))
            {
                continue;
            }

            NotificationTemplate template = templateHolder.getTemplate();
            if (template == null)
            {
                continue;
            }

            if (templateHolder.isMandatory()
                    && (preferredTypes == null
                    || preferredTypes.length == 0))
            {
                result.add(template);
            }
            else
            {
                if (templateHolder.isDefaultTemplate()
                        && result.size() == 0)
                {
                    defaultTemplates.add(template);
                }
                
                for (Class<NotificationTemplate> preference : preferredTypes)
                {
                    if (preference != null && preference.isInstance(template))
                    {
                        result.add(template);
                        break;
                    }
                }
            }
        }

        if (result.size() == 0)
        {
            result = defaultTemplates;
        }
        
        return result;
    }


    /**
     * {@inheritDoc}
     */
    public Collection<NotificationTemplate> getTemplates(
            Context ctx, 
            long accountCategoryId, String language,
            NotificationTypeEnum type) throws HomeException
    {
        Collection<NotificationTemplate> result = new ArrayList<NotificationTemplate>();
        
        NotificationTemplateGroupID templateGroupID = getTemplateGroupID(ctx, accountCategoryId, type);
        if (templateGroupID != null)
        {
            result = getTemplates(ctx, templateGroupID.getName(), templateGroupID.getSpid(), language, new Class[]{});
        }
        
        return result;
    }


    /**
     * {@inheritDoc}
     */
    public Collection<NotificationTemplate> getTemplates(
            Context ctx, 
            long accountCategoryId, String language, int occurrence, 
            NotificationTypeEnum type) throws HomeException
    {
        return getTemplates(ctx, accountCategoryId, language, occurrence, type, new Class[]{});
    }


    /**
     * {@inheritDoc}
     */
    public Collection<NotificationTemplate> getTemplates(
            Context ctx, 
            long accountCategoryId, String language,
            NotificationTypeEnum type, 
            Class<NotificationTemplate>... preferredTypes) throws HomeException
    {
        Collection<NotificationTemplate> result = new ArrayList<NotificationTemplate>();
        
        NotificationTemplateGroupID templateGroupID = getTemplateGroupID(ctx, accountCategoryId, type);
        if (templateGroupID != null)
        {
            result = getTemplates(ctx, templateGroupID.getName(), templateGroupID.getSpid(), language, preferredTypes);
        }
        
        return result;
    }


    /**
     * {@inheritDoc}
     */
    public Collection<NotificationTemplate> getTemplates(
            Context ctx, 
            long accountCategoryId, String language, int occurrence, 
            NotificationTypeEnum type, 
            Class<NotificationTemplate>... preferredTypes) throws HomeException
    {
        Collection<NotificationTemplate> result = new ArrayList<NotificationTemplate>();
        
        NotificationTemplateGroupID templateGroupID = getTemplateGroupID(ctx, accountCategoryId, occurrence, type);
        if (templateGroupID != null)
        {
            result = getTemplates(ctx, templateGroupID.getName(), templateGroupID.getSpid(), language, preferredTypes);
        }
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public NotificationTemplateGroupID getTemplateGroupID(Context ctx, int spid, NotificationTypeEnum type)
    {
        NotificationTemplateGroupID result = null;
        
        Collection<NotificationTemplateGroupHolder> templateGroups = getTemplateGroups(ctx, spid);
        if (templateGroups == null)
        {
            return result;
        }

        result = getApplicableTemplateGroupID(ctx, type, -1, templateGroups);
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public NotificationTemplateGroupID getTemplateGroupID(Context ctx, int spid, int occurrence, NotificationTypeEnum type)
    {
        NotificationTemplateGroupID result = null;
        
        Collection<NotificationTemplateGroupHolder> templateGroups = getTemplateGroups(ctx, spid);
        if (templateGroups == null)
        {
            return result;
        }
        
        result = getApplicableTemplateGroupID(ctx, type, occurrence, templateGroups);
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public NotificationTemplateGroupID getTemplateGroupID(Context ctx, long accountCategoryId, int occurrence, NotificationTypeEnum type)
    {
        NotificationTemplateGroupID result = null;
        
        Collection<NotificationTemplateGroupHolder> templateGroups = getTemplateGroups(ctx, accountCategoryId);
        if (templateGroups == null)
        {
            return result;
        }
        
        result = getApplicableTemplateGroupID(ctx, type, occurrence, templateGroups);
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public NotificationTemplateGroupID getTemplateGroupID(Context ctx, long accountCategoryId, NotificationTypeEnum type)
    {
        NotificationTemplateGroupID result = null;
        
        Collection<NotificationTemplateGroupHolder> templateGroups = getTemplateGroups(ctx, accountCategoryId);
        if (templateGroups == null)
        {
            return result;
        }
        
        result = getApplicableTemplateGroupID(ctx, type, -1, templateGroups);
        
        return result;
    }

    
    /**
     * {@inheritDoc}
     */
    public List<Class<? extends NotificationTemplate>> getPreferredNotificationTypes(NotificationMethodEnum preferredMethod)
    {
        List<Class<? extends NotificationTemplate>> preferredTypes = new ArrayList<Class<? extends NotificationTemplate>>();
        if (preferredMethod != null)
        {
            switch (preferredMethod.getIndex())
            {
            case NotificationMethodEnum.BOTH_INDEX:
                preferredTypes.add(EmailNotificationTemplate.class);
                preferredTypes.add(SmsNotificationTemplate.class);
                break;
            case NotificationMethodEnum.EMAIL_INDEX:
                preferredTypes.add(EmailNotificationTemplate.class);
                break;
            case NotificationMethodEnum.SMS_INDEX:
                preferredTypes.add(SmsNotificationTemplate.class);
                break;
            case NotificationMethodEnum.DEFAULT_INDEX:
                // Adding null ensures that only mandatory or default templates are sent.
                preferredTypes.add(null);
                break;
            case NotificationMethodEnum.NONE_INDEX:
                // Don't do anything.  Only mandatory templates will be selected.
                break;
            }
        }
        return preferredTypes;
    }

    /**
     * {@inheritDoc}
     */
    public NotificationLiaison getLiaisonForNotificationType(Context ctx, NotificationTypeEnum type)
    {
        return (NotificationLiaison) ctx.get(NOTIFICATION_TYPE_LIAISON_CTX_KEY_PREFIX + type, ctx.get(NotificationLiaison.class));
    }

    /**
     * {@inheritDoc}
     */
    public void removePendingNotifications(Context ctx, String cleanupKey)
    {
        try
        {
            Home home = HomeSupportHelper.get(ctx).getHome(ctx, ScheduledNotification.class);
            if (home != null)
            {
                And filter = new And();
                filter.add(new EQ(ScheduledNotificationXInfo.APP_NAME, CoreSupport.getApplication(ctx).getName()));
                filter.add(new EQ(ScheduledNotificationXInfo.CLEANUP_KEY, cleanupKey));
                home.removeAll(ctx, filter);
            }
        }
        catch (HomeException e)
        {
            new MinorLogMsg(this, "Error removing pending notifications with cleanup key '" + cleanupKey + "'", e).log(ctx);
        }
    }
    
    public EnumCollection getSupportedNotificationTypes(Context ctx)
    {
        EnumCollection result = null;
        
        Application app = CoreSupport.getApplication(ctx);
        if (app != null)
        {
            if ("AppCrm".equals(app.getName()))
            {
                result = new EnumCollection(new NotificationTypeEnum[]
                                                                     {
                        NotificationTypeEnum.STATE_CHANGE, 
                        NotificationTypeEnum.PRE_EXPIRY, 
                        NotificationTypeEnum.EXPIRY_EXTENSION, 
                        NotificationTypeEnum.SERVICE_SUSPENSION, 
                        NotificationTypeEnum.SERVICE_UNSUSPENSION, 
                        NotificationTypeEnum.PREPAID_RECURRING_RECHARGE_PREWARNING, 
                        NotificationTypeEnum.PREPAID_RECURRING_RECHARGE_PREWARNING_INSUFFICIENT_BAL,
                        NotificationTypeEnum.PREPAID_RECHARGE, 
                        NotificationTypeEnum.PREPAID_RECHARGE_FAILURE,
                        NotificationTypeEnum.WARNING_NOTICE, 
                        NotificationTypeEnum.DUNNING_NOTICE, 
                        NotificationTypeEnum.IN_ARREARS_NOTICE, 
                        NotificationTypeEnum.TRANSFER_DISPUTE,
                        NotificationTypeEnum.PRICEPLAN_CHANGE_NOTIFICATION,
                        NotificationTypeEnum.PRICEPLAN_OPTION_REMOVAL_NOTIFICATION,
                        NotificationTypeEnum.VOUCHER_TOPUP,
                        NotificationTypeEnum.SERVICE_EXPIRY_NOTIFICATION,
                        NotificationTypeEnum.SERVICE_RECURRENCE_NOTIFICATION,
						NotificationTypeEnum.TFA_NOTIFICATION,
                        NotificationTypeEnum.EXT_NOTIFICATION,
						NotificationTypeEnum.PAYGO_MODE_ENABLED,
                        NotificationTypeEnum.PAYGO_MODE_DISABLED,
 						NotificationTypeEnum.VOUCHER_TOPUP_FAILURE,
 						NotificationTypeEnum.SCHEDULED_PRICEPLAN_CHANGE
                                                                     });
            }
            else if ("AppCrmInvoice".equals(app.getName()))
            {
                result = new EnumCollection(new NotificationTypeEnum[]
                                                                     {
                        /*
                        NotificationTypeEnum.INVOICE, 
                        NotificationTypeEnum.STATEMENT, 
                        NotificationTypeEnum.INVOICE_NOTIFICATION, 
                        NotificationTypeEnum.STATEMENT_NOTIFICATION, 
                         */
                        NotificationTypeEnum.PAYMENT_REMINDER, 
                        NotificationTypeEnum.LETTER_OF_DEMAND
                                                                     });
            }
        }
        
        if (result == null)
        {
            result = NotificationTypeEnum.COLLECTION;
        }
        
        return result;
    }

    protected NotificationTemplateGroupID getApplicableTemplateGroupID(
            Context ctx, 
            NotificationTypeEnum type, 
            int occurrence,
            Collection<NotificationTemplateGroupHolder> templateGroups)
    {
        NotificationTemplateGroupID result = null;
        
        TreeMap<Integer, NotificationTemplateGroupID> templateGroupIDs = getApplicableTemplateGroupIDs(ctx, type, templateGroups);
        if (templateGroupIDs == null)
        {
            return result;
        }
        
        for (Map.Entry<Integer, NotificationTemplateGroupID> entry : templateGroupIDs.entrySet())
        {
            int current = entry.getKey();
            if (occurrence == -1
                    || current == occurrence)
            {
                // Take the lowest configured occurrence value when no occurrence value is specified
                result = entry.getValue();
                break;
            }
            else if (current < occurrence)
            {
                // Take the highest occurrence number that is configured to be lower than the requested occurrence value
                result = entry.getValue();
            }
            else
            {
                break;
            }
        }
        
        return result;
    }

    protected TreeMap<Integer, NotificationTemplateGroupID> getApplicableTemplateGroupIDs(
            Context ctx, 
            NotificationTypeEnum type, 
            Collection<NotificationTemplateGroupHolder> templateGroups)
    {
        TreeMap<Integer, NotificationTemplateGroupID> result = new TreeMap<Integer, NotificationTemplateGroupID>();
        
        for (NotificationTemplateGroupHolder groupHolder : templateGroups)
        {
            if (groupHolder == null)
            {
                continue;
            }

            if (type != null
                    && type.getIndex() == (short) groupHolder.getNotificationTypeIndex())
            {
                long recordID = groupHolder.getTemplateGroup();
                if (recordID != NotificationTemplateGroupHolder.DEFAULT_TEMPLATEGROUP)
                {
                    try
                    {
                        Context whereCtx = HomeSupportHelper.get(ctx).getWhereContext(ctx, 
                                TemplateGroupGlobalRecord.class, 
                                new EQ(TemplateGroupGlobalRecordXInfo.APP_NAME, CoreSupport.getApplication(ctx).getName()));
                        TemplateGroupGlobalRecord record = HomeSupportHelper.get(whereCtx).findBean(whereCtx, TemplateGroupGlobalRecord.class, recordID);
                        if (record != null)
                        {
                            result.put(groupHolder.getOccurrenceNumber(), new NotificationTemplateGroupID(record.getName(), record.getSpid()));
                        }
                    }
                    catch (HomeException e)
                    {
                        new MinorLogMsg(this, "Error retrieving global template group record " + recordID, e).log(ctx);
                    }
                }
            }
        }

        return result;
    }

    protected Collection<NotificationTemplateGroupHolder> getTemplateGroups(Context ctx, int spid)
    {
        Collection<NotificationTemplateGroupHolder> result = null;

        NotificationMethodSpidExtension defaultNotificationExt = null;
        try
        {
            defaultNotificationExt = HomeSupportHelper.get(ctx).findBean(ctx, NotificationMethodSpidExtension.class, spid);
        }
        catch (HomeException e)
        {
            String msg = "Error retrieving SPID " + spid;
            new DebugLogMsg(this, msg, e).log(ctx);
            new MinorLogMsg(this, msg + ": " + e.getMessage(), null).log(ctx);
        }
        if (defaultNotificationExt == null)
        {
            return result;
        }
        
        Map<NotificationTemplateGroupHolderID, NotificationTemplateGroupHolder> templateGroupMap = defaultNotificationExt.getDefaultTemplateGroups();
        
        result = templateGroupMap.values();
        
        return result;
    }

    protected Collection<NotificationTemplateGroupHolder> getTemplateGroups(Context ctx, long accountCategoryId)
    {
        Collection<NotificationTemplateGroupHolder> result = null;
        
        AccountCategory accountCategory = AccountTypeSupportHelper.get(ctx).getTypedAccountType(ctx, accountCategoryId);
        if (accountCategory == null)
        {
            return result;
        }

        Map<NotificationTemplateGroupHolderID, NotificationTemplateGroupHolder> templateGroupMap = new HashMap<NotificationTemplateGroupHolderID, NotificationTemplateGroupHolder>();
        
        int spid = accountCategory.getSpid();
        NotificationMethodSpidExtension defaultNotificationExt = null;
        try
        {
            defaultNotificationExt = HomeSupportHelper.get(ctx).findBean(ctx, NotificationMethodSpidExtension.class, spid);
        }
        catch (HomeException e)
        {
            String msg = "Error retrieving SPID " + spid;
            new DebugLogMsg(this, msg, e).log(ctx);
            new MinorLogMsg(this, msg + ": " + e.getMessage(), null).log(ctx);
        }
        if (defaultNotificationExt != null)
        {
            templateGroupMap.putAll(defaultNotificationExt.getDefaultTemplateGroups());
        }
        
        templateGroupMap.putAll(accountCategory.getTemplateGroups());
        
        result = templateGroupMap.values();
        
        return result;
    }
}
