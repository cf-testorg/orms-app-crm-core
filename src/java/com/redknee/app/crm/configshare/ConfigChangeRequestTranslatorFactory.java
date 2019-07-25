package com.redknee.app.crm.configshare;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.core.BeanClassMapping;


public class ConfigChangeRequestTranslatorFactory
{

    public static ConfigChangeRequestTranslator instance(Context ctx, BeanClassMapping mapping)
    {
        ConfigChangeRequestTranslator translator = null;
        if (mapping == null || mapping.getTranslatorScript() == null || mapping.getTranslatorScript().isEmpty())
        {
            translator = DefaultConfigChangeRequestTranslator.instance();
        }
        else
        {
            translator = mapping.getTranslator(ctx);
        }
        return translator;
    }
}
