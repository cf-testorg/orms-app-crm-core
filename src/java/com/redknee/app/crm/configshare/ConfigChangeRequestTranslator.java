package com.redknee.app.crm.configshare;

import java.util.Collection;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.context.Context;


public interface ConfigChangeRequestTranslator
{
    
    public Collection<? extends AbstractBean> translate(Context ctx, ConfigChangeRequest request) throws ConfigChangeRequestTranslationException;
    
}
