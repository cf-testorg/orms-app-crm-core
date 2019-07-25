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
package com.redknee.app.crm.bean.core;

import com.redknee.framework.core.bean.ScriptLanguageEnum;
import com.redknee.framework.core.scripting.ScriptExecutor;
import com.redknee.framework.core.scripting.support.ScriptExecutorFactory;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.configshare.ConfigChangeRequestTranslator;
import com.redknee.app.crm.configshare.NullConfigChangeRequestTranslator;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class BeanClassMapping extends com.redknee.app.crm.configshare.BeanClassMapping
{
    public ConfigChangeRequestTranslator getTranslator(Context ctx)
    {
        ScriptExecutor executor = ScriptExecutorFactory.create(ScriptLanguageEnum.JSH);
        ConfigChangeRequestTranslator result = NullConfigChangeRequestTranslator.instance();
        try
        {
            result = (ConfigChangeRequestTranslator) executor.retrieveObject(ctx, this.getTranslatorScript(), ConfigChangeRequestTranslator.class.getName());
        }
        catch (Exception e)
        {
            new MinorLogMsg(this, "Error retrieving translator for request referencing external class "
                    + this.getExternalBeanName() + ".  Translation will be skipped.", e).log(ctx);
        }
        return result;
    }
}
