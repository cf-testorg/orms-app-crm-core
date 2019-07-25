package com.redknee.app.crm.support;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.configshare.ConfigChangeRequest;
import com.redknee.app.crm.configshare.IndividualUpdate;


public interface ConfigChangeRequestSupport extends Support
{
    public void registerBeanForConfigSharing(Context ctx, AbstractBean bean);
    
    public Home registerHomeForConfigSharing(Context ctx, Home home, Class beanClass);
    
    public Home registerHomeForConfigSharing(Context ctx, Home home) throws HomeException;
    
    public ConfigChangeRequest createConfigChangeRequest(Context ctx, Object bean, final short operation);

    public boolean isConfigSharingBeanInProgress(Context ctx);

    public Object getConfigSharingBean(Context ctx, Class beanClass);

    public void hideConfigSharingFlag(Context ctx);

    public Context getConfigSharingContext(Context ctx);

    public Context getConfigSharingContext(Context ctx, Object obj);
    
    public Object setPropertyInfo(Context ctx, IndividualUpdate update, AbstractBean bean);
}
