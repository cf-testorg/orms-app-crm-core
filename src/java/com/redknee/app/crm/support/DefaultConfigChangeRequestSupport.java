package com.redknee.app.crm.support;

import java.security.Principal;
import java.util.Date;
import java.util.Set;

import com.redknee.app.crm.configshare.ConfigChangeRequest;
import com.redknee.app.crm.configshare.ConfigShareAPIUpdateEntityHome;
import com.redknee.app.crm.configshare.ConfigShareAPIUpdateEntityInfo;
import com.redknee.app.crm.configshare.ConfigSharingHome;
import com.redknee.app.crm.configshare.IndividualUpdate;
import com.redknee.app.crm.configshare.SharedBean;
import com.redknee.app.crm.configshare.SharedConfigChangeListener;
import com.redknee.app.crm.xhome.home.ConfigShareTotalCachingHome;
import com.redknee.app.crm.xhome.home.TotalCachingHome;
import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.auth.bean.User;
import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.beans.xi.XInfoSupport;
import com.redknee.framework.xhome.cluster.RMIClusteredHome;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextSupport;
import com.redknee.framework.xhome.home.AbstractClassAwareHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.support.IdentitySupport;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;


public class DefaultConfigChangeRequestSupport implements ConfigChangeRequestSupport
{
    public static String SHARE_IN_PROGRESS = "ConfigShareInProgress";
    public static String API_SOURCE_USERNAME = "API_SOURCE_USERNAME";
    public static String FILTER_MULTIPLE_ENTITIES_FOR_API_UPDATE = "FILTER_MULTIPLE_ENTITIES_FOR_API_UPDATE";
    
    protected static ConfigChangeRequestSupport instance_ = null;
    public static ConfigChangeRequestSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultConfigChangeRequestSupport();
        }
        return instance_;
    }

    public void registerBeanForConfigSharing(Context ctx, AbstractBean bean)
    {
        bean.removePropertyChangeListener(SharedConfigChangeListener.instance());
        bean.addPropertyChangeListener(SharedConfigChangeListener.instance());
        
        try
        {
            SharedBean sharedBeanRecord = new SharedBean();
            sharedBeanRecord.setBeanClassName(bean.getClass().getName());
            sharedBeanRecord.setShareHome(false);
            HomeSupportHelper.get(ctx).createBean(ctx, sharedBeanRecord);
        }
        catch (HomeException e)
        {
            new MajorLogMsg(this, "Error creating SharedBean record to indicate that bean " + bean.getClass().getName() + " is shared.", e).log(ctx);
        }
    }
    
    public Home registerHomeForConfigSharing(Context ctx, Home home) throws HomeException
    {
        Object beanClass = home.cmd(ctx, AbstractClassAwareHome.CLASS_CMD);
        if (beanClass instanceof Class)
        {
            return registerHomeForConfigSharing(ctx, home, (Class) beanClass);
        }
        
        throw new HomeException("Unable to install config sharing for home " + home + ".  Unable to determine bean class!");
    }
    
    public Home registerHomeForConfigSharing(Context ctx, Home home, Class beanClass)
    {
        if (home instanceof HomeProxy)
        {
            // Remove any existing ConfigSharingHomes from the pipeline.
            // This can happen if the multiple callers try to register the same pipeline.
            removeExistingConfigSharingHome(ctx, home);
            
            Home clusterHome = ((HomeProxy)home).findDecorator(RMIClusteredHome.class);
            if (clusterHome == null)
            {
                // Add the config sharing home right beside the storage layer
                ((HomeProxy)home).appendProxy(ctx, new ConfigSharingHome(ctx, null));
            }
            else
            {
                // Add the config sharing home as the delegate of the existing clustered home
                // This will prevent re-sharing clustered configuration
                ((RMIClusteredHome)clusterHome).addProxy(ctx, new ConfigSharingHome(ctx, null));
            }
        }
        else
        {
            home = new ConfigSharingHome(ctx, home);
        }

        try
        {
            SharedBean sharedBeanRecord = new SharedBean();
            sharedBeanRecord.setBeanClassName(beanClass.getName());
            sharedBeanRecord.setShareHome(true);
            try
            {
                HomeSupportHelper.get(ctx).createBean(ctx, sharedBeanRecord);
            }
            catch (HomeException e)
            {
                HomeSupportHelper.get(ctx).storeBean(ctx, sharedBeanRecord);
            }
        }
        catch (HomeException e)
        {
            new MajorLogMsg(this, "Error creating SharedBean record to indicate that bean " + beanClass.getName() + " is shared.", e).log(ctx);
        }
        
        return home;
    }

    /**
     * Remove any existing ConfigSharingHomes from the pipeline.
     * This can happen if the multiple callers try to register the same pipeline.
     * 
     * @param ctx
     * @param home
     */
    protected void removeExistingConfigSharingHome(Context ctx, Home home)
    {
        for (Home temp = home; temp instanceof HomeProxy; temp = ((HomeProxy)temp).getDelegate(ctx))
        {
            HomeProxy proxy = (HomeProxy) temp;
            Home delegate = proxy.getDelegate(ctx);
            if (delegate instanceof ConfigSharingHome)
            {
                ConfigSharingHome existingSharingHome = (ConfigSharingHome) delegate;
                proxy.setDelegate(existingSharingHome.getDelegate(ctx));
                existingSharingHome.setDelegate(null);
            }
            else if (delegate instanceof ConfigShareTotalCachingHome)
            {
                ConfigShareTotalCachingHome cache = (ConfigShareTotalCachingHome) delegate;
                Home cacheHome = cache.getCacheHome();
                while (cacheHome instanceof ConfigSharingHome)
                {
                    cacheHome = cache.removeFirstCacheHomeDecorator();
                }
                removeExistingConfigSharingHome(ctx, cacheHome);
            }
        }
    }
    

    
    public ConfigChangeRequest createConfigChangeRequest(Context ctx, Object bean, final short operation)
    {
        final Class<? extends Object> beanClass = bean.getClass();
        
        ConfigChangeRequest request = new ConfigChangeRequest();
        request.setBeanClass(beanClass.getName());
        request.setModifiedAppName(CoreSupport.getApplication(ctx).getName());
        
        request.setTimestamp(new Date());
        
        String ID = null;
        if (bean instanceof Identifiable)
        {
            final Object beanId = ((Identifiable)bean).ID();
            
            final IdentitySupport idSupport = (IdentitySupport) XBeans.getInstanceOf(ctx, beanClass, IdentitySupport.class);
            if (idSupport != null)
            {
                ID = idSupport.toStringID(beanId);
            }
            else
            {
                ID = String.valueOf(beanId);
            }
            request.setBeanId(ID);

        }
        
        String modifiedBy = getModifiedBy(ctx, bean, operation, ID);
        request.setModifiedBy(modifiedBy);
        
        request.setIdentifier(String.valueOf(request.hashCode()));
        
        return request;
    }


    private String getModifiedBy(final Context ctx, final Object bean, final short operation, final String ID)
    {
        String modifiedBy = "system";
        boolean changed = false;
        String source = (String) ctx.get(API_SOURCE_USERNAME);
        if (source != null && (!source.isEmpty()))
        {
            ConfigShareAPIUpdateEntityInfo apiUpdateEntityInfo = (ConfigShareAPIUpdateEntityInfo) ctx
                    .get(ConfigShareAPIUpdateEntityHome.CONFIG_SHARE_API_UPDATE_ENTITY);
            if (apiUpdateEntityInfo != null)
            {
                if (apiUpdateEntityInfo.getUpdateEntityClass().equals(bean.getClass())
                        && ((operation == HomeOperationEnum.CREATE_INDEX) || (operation == HomeOperationEnum.STORE_INDEX && ID
                                .equals(apiUpdateEntityInfo.getIdentifier()))))
                {
                    modifiedBy = source;
                    changed = true;
                }
                else
                {
                  
                    Set set= (Set) ctx.get(FILTER_MULTIPLE_ENTITIES_FOR_API_UPDATE);
                    if (set != null && set.contains(bean.getClass().getName()))
                    {
                        modifiedBy = source;
                        changed = true;
                    }
                }
            }
        }
        if (!changed)
        {
            User user = (User) ctx.get(Principal.class);
            if (user != null)
            {
                modifiedBy = user.getId();
            }
        }
        return modifiedBy;
    }
    
    

    public boolean isConfigSharingBeanInProgress(Context ctx)
    {
        return ctx.has(SHARE_IN_PROGRESS);
    }

    public Object getConfigSharingBean(Context ctx, Class beanClass)
    {
        return ctx.get(SHARE_IN_PROGRESS + beanClass.getName());
    }

    public void hideConfigSharingFlag(Context ctx)
    {
        if (ctx.has(SHARE_IN_PROGRESS))
        {
            ctx.put(SHARE_IN_PROGRESS, null);
        }
    }

    public Context getConfigSharingContext(Context ctx)
    {
        Context sCtx = ctx.createSubContext();
        sCtx.put(SHARE_IN_PROGRESS, Boolean.valueOf(true));
        return sCtx;
    }

    public Context getConfigSharingContext(Context ctx, Object obj)
    {
        if (obj != null)
        {
            if (!isConfigSharingBeanInProgress(ctx))
            {
                ctx = ctx.createSubContext();
            }
            ctx.put(SHARE_IN_PROGRESS + obj.getClass().getName(), obj);
        }
        return ctx;
    }

    public Object setPropertyInfo(Context ctx, IndividualUpdate update, AbstractBean bean)
    {
        if (bean != null
                && update.getNewValue() != null)
        {
            XInfo xinfo = (XInfo) XBeans.getInstanceOf(ctx, bean.getClass(), XInfo.class);
            if (xinfo == null)
            {
                throw new NullPointerException(
                        "XInfo not found for bean class [" + (bean != null ? bean.getClass() : "null") + "]");
            }
            
            PropertyInfo propertyInfo = XInfoSupport.getPropertyInfo(ctx, xinfo, update.getFieldName());
            if (propertyInfo != null)
            {
                Class propertyType = propertyInfo.getType();
                if ((Boolean.class.isAssignableFrom(propertyType)
                        || Boolean.TYPE.isAssignableFrom(propertyType))
                        && Boolean.parseBoolean(update.getNewValue()))
                {
                    // Handle 'true' string if some application is sending it instead of 'y'
                    propertyInfo.set(bean, Boolean.TRUE);
                }
                else
                {
                    propertyInfo.set(bean, propertyInfo.fromString(update.getNewValue()));
                }
            }
            else
            {
                if (LogSupport.isDebugEnabled(ctx))
                {
                    new DebugLogMsg(this,
                            "PropertyInfo not found for field [" + bean.getClass()
                            + "." + update.getFieldName() + "].  Unable to perform property update...", null).log(ctx);
                }
            }
        }
        
        return bean;
    }
}
