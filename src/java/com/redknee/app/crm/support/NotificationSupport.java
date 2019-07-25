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

import java.util.Collection;
import java.util.List;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.xenum.EnumCollection;

import com.redknee.app.crm.bean.NotificationMethodEnum;
import com.redknee.app.crm.notification.NotificationTypeEnum;
import com.redknee.app.crm.notification.liaison.NotificationLiaison;
import com.redknee.app.crm.notification.template.NotificationTemplate;
import com.redknee.app.crm.notification.template.NotificationTemplateGroupID;


/**
 * This class provides methods to retrieve notification templates matching different
 * language/type settings.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0 
 */
public interface NotificationSupport extends Support
{
    public static final String NOTIFICATION_TYPE_LIAISON_CTX_KEY_PREFIX = "NotificationLiaison-";
    
    /**
     * Returns the notification types supported by the running application.
     * 
     * @param ctx
     * @return
     */
    public EnumCollection getSupportedNotificationTypes(Context ctx);
    
    /**
     * This method returns templates configured in an account category matching the
     * desired language and template types (i.e. features).
     * 
     * @param ctx
     * @param templateGroupName
     * @param language
     * @param preferredTypes
     * @return
     * @throws HomeException
     */
    public Collection<NotificationTemplate> getTemplates(
            Context ctx, 
            String templateGroupName, int spid, String language, 
            Class<NotificationTemplate>... preferredTypes) throws HomeException;

    /**
     * This method returns templates configured in an account category matching the
     * desired language and template type (i.e. feature).
     * 
     * @param ctx
     * @param accountCategoryId
     * @param language
     * @param type
     * @return
     * @throws HomeException
     */
    public Collection<NotificationTemplate> getTemplates(
            Context ctx, 
            long accountCategoryId, String language,
            NotificationTypeEnum type) throws HomeException;

    /**
     * This method returns templates configured in an account category matching the
     * desired language, occurrence number (i.e. for repeat notices), and template type
     * (i.e. feature).
     * 
     * @param ctx
     * @param accountCategoryId
     * @param language
     * @param occurrence
     * @param type
     * @return
     * @throws HomeException
     */
    public Collection<NotificationTemplate> getTemplates(
            Context ctx, 
            long accountCategoryId, String language, int occurrence,
            NotificationTypeEnum type) throws HomeException;
    
    /**
     * This method returns templates configured in an account category matching the
     * desired language, template type (i.e. feature), and template class types (i.e.
     * E-Mail, SMS, etc). Note that mandatory templates that are configured will be
     * returned regardless of their class type.
     * 
     * @param ctx
     * @param accountCategoryId
     * @param language
     * @param type
     * @param preferredTypes
     * @return
     * @throws HomeException
     */
    public Collection<NotificationTemplate> getTemplates(
            Context ctx, 
            long accountCategoryId, String language,
            NotificationTypeEnum type, 
            Class<NotificationTemplate>... preferredTypes) throws HomeException;
    
    /**
     * This method returns templates configured in an account category matching the
     * desired language, occurrence number (i.e. for repeat notices), template type
     * (i.e. feature), and template class types (i.e. E-Mail, SMS, etc). Note that
     * mandatory templates that are configured will be returned regardless of their
     * class type.
     * 
     * @param ctx
     * @param accountCategoryId
     * @param language
     * @param occurrence
     * @param type
     * @param preferredTypes
     * @return
     * @throws HomeException
     */
    public Collection<NotificationTemplate> getTemplates(
            Context ctx, 
            long accountCategoryId, String language, int occurrence,
            NotificationTypeEnum type, 
            Class<NotificationTemplate>... preferredTypes) throws HomeException;

    /**
     * This method returns the template group that is associated with the given account
     * category and template type (i.e. feature).  Specific templates that would be used
     * will be dependent on the language and preferred/mandatory notification templates
     * configured in the group.
     * 
     * @param ctx
     * @param accountCategoryId
     * @param type
     * @return
     */
    public NotificationTemplateGroupID getTemplateGroupID(Context ctx, long accountCategoryId, NotificationTypeEnum type);
    
    /**
     * This method returns the template group that is associated with the given account
     * category, occurrence number (i.e. for repeat notices), and template type
     * (i.e. feature).  Specific templates that would be used will be dependent on the
     * language and preferred/mandatory notification templates configured in the group.
     * 
     * @param ctx
     * @param accountCategoryId
     * @param occurrence
     * @param type
     * @return
     */
    public NotificationTemplateGroupID getTemplateGroupID(Context ctx, long accountCategoryId, int occurrence, NotificationTypeEnum type);

    /**
     * This method returns the template group that is associated with the given spid and 
     * template type (i.e. feature).  Specific templates that would be used will be 
     * dependent on the language and preferred/mandatory notification templates configured 
     * in the group.
     * 
     * @param ctx
     * @param spid
     * @param type
     * @return
     */
    public NotificationTemplateGroupID getTemplateGroupID(Context ctx, int spid, NotificationTypeEnum type);

    /**
     * This method returns the template group that is associated with the given spid, 
     * occurrence number (i.e. for repeat notices), and template type (i.e. feature).
     * Specific templates that would be used will be dependent on the language and 
     * preferred/mandatory notification templates configured in the group.
     * 
     * @param ctx
     * @param spid
     * @param occurrence
     * @param type
     * @return
     */
    public NotificationTemplateGroupID getTemplateGroupID(Context ctx, int spid, int occurrence, NotificationTypeEnum type);
    
    /**
     * This method returns a list of class names consistent with the notification method
     * enumeration that some things allow users to configure preferred delivery types with.
     * 
     * @param preferredMethod
     * @return
     */
    public List<Class<? extends NotificationTemplate>> getPreferredNotificationTypes(NotificationMethodEnum preferredMethod);
    
    /**
     * Returns the liaison that should be used for the given notification type.
     * 
     * @param ctx
     * @param type
     * @return
     */
    public NotificationLiaison getLiaisonForNotificationType(Context ctx, NotificationTypeEnum type);

    /**
     * Removes all entries that this application has created with the given cleanup key.
     * 
     * @param ctx
     * @param cleanupKey
     */
    public void removePendingNotifications(Context ctx, String cleanupKey);
}
