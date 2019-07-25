package com.redknee.app.crm.calculator;

import java.util.ArrayList;
import java.util.Collection;

import com.redknee.framework.core.bean.Application;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.language.Lang;
import com.redknee.framework.xhome.language.MessageMgr;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.support.HomeSupportHelper;


public class LanguageFormattingValueCalculator extends AbstractLanguageFormattingValueCalculator
{
    public LanguageFormattingValueCalculator()
    {
        super();
    }
    
    public LanguageFormattingValueCalculator(ValueCalculator delegate)
    {
        super();
        setDelegate(delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Object> getDependentContextKeys(Context ctx)
    {
        Collection dependentContextKeys = super.getDependentContextKeys(ctx);
        if (dependentContextKeys == null)
        {
            dependentContextKeys = new ArrayList();
        }

        dependentContextKeys.add(Application.class);

        return dependentContextKeys;
    }

    @Override
    public Object getValueAdvanced(Context ctx)
    {
        Object value = super.getValueAdvanced(ctx);
        
        if (value != null)
        {
            try
            {
                String language = getLanguage(ctx);
                Lang lang = getLanguage(ctx, language);
                if (language != null)
                {
                    MessageMgr mmgr = new MessageMgr(ctx, this);
                    value = mmgr.get(String.valueOf(value), this.getClass(), lang, String.valueOf(value), null);
                }
                else
                {
                    LogSupport.minor(ctx, this, "Unable to retrieve language '" + language + "'", null);
                }
            }
            catch (Throwable t)
            {
                LogSupport.minor(ctx, this, "Unable to retrieve language dependent value for " + value + ": " + t.getMessage(), t);
            }
        }
        
        return value;
    }


    protected String getLanguage(Context ctx)
    {
        Application appConfig = (Application) ctx.get(Application.class);
        if (appConfig!=null)
        {
            return appConfig.getLocaleIsoLanguage();
        }
        return null;
    }

    private Lang getLanguage(Context ctx, String language)
    {
        if (language!=null)
        {
            try
            {
                return HomeSupportHelper.get(ctx).findBean(ctx, Lang.class, language);
            }
            catch (Throwable t)
            {
                LogSupport.minor(ctx, this, "Unable to retrieve language '" + language + "': " + t.getMessage(), t);
            }
        }
        return null;
    }
}

