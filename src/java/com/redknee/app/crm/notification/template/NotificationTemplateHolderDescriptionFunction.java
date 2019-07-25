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
package com.redknee.app.crm.notification.template;

import java.util.Collection;

import com.redknee.framework.xhome.beans.Function;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.entity.ByClassPredicate;
import com.redknee.framework.xhome.entity.EntityInfo;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.language.Lang;
import com.redknee.framework.xhome.language.MessageMgr;

import com.redknee.app.crm.support.HomeSupportHelper;

public class NotificationTemplateHolderDescriptionFunction implements Function
{
    protected static Function instance_ = null;
    public static Function instance()
    {
        if (instance_ == null)
        {
            instance_ = new NotificationTemplateHolderDescriptionFunction();
        }
        return instance_;
    }
    
    protected NotificationTemplateHolderDescriptionFunction()
    {
    }

    public Object f(Context ctx, Object obj)
    {
        MessageMgr mmgr = new MessageMgr(ctx, this);
        
        String result = obj != null ? obj.getClass().getSimpleName() : null;

        NotificationTemplate template = null;
        String language = null;
        if (obj instanceof NotificationTemplate)
        {
            template = (NotificationTemplate) obj;
        }
        else if (obj instanceof NotificationTemplateHolder)
        {
            NotificationTemplateHolder holder = (NotificationTemplateHolder) obj;
            template = holder.getTemplate();
            language = holder.getLanguage();
        }
        
        if (template != null)
        {
            result = mmgr.get("NotificationTemplate.None", "No Template");

            EntityInfo entityInfo = null;
            try
            {
                Collection<EntityInfo> entities = HomeSupportHelper.get(ctx).getBeans(ctx, EntityInfo.class, new ByClassPredicate(template.getClass()));
                if (entities != null)
                {
                    for (EntityInfo entity : entities)
                    {
                        if (template.getClass().getName().equals(entity.getClassName()))
                        {
                            entityInfo = entity;
                            break;
                        }
                    }
                }
            }
            catch (HomeException e)
            {
            }

            if (entityInfo != null)
            {
                result = mmgr.get("NotificationTemplate.EntityName." + entityInfo.getName(), entityInfo.getName());
            }
            else
            {
                result = mmgr.get("NotificationTemplate.EntityName." + template.getClass().getSimpleName(), template.getClass().getSimpleName());
            }

            Lang lang = null;
            if (language != null
                    && !NotificationTemplateHolder.DEFAULT_LANGUAGE.equals(language))
            {
                try
                {
                    lang = HomeSupportHelper.get(ctx).findBean(ctx, Lang.class, language);
                }
                catch (HomeException e)
                {
                }
            }

            if (lang != null)
            {
                result = result + " (" + lang.getName() + ")";
            }
        }
        
        if (obj instanceof NotificationTemplateHolder 
                && !((NotificationTemplateHolder)obj).isEnabled())
        {
            result = result + " (" + mmgr.get("NotifiationTemplate.Disabled", "Disabled") + ")";
        }
        
        return result;
    }
}